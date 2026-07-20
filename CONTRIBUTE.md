# Java Coding Guidelines

## Read This First

This codebase is ~10 years old, originally written in 2016 with a late-'90s
mindset (hand-rolled loops instead of the standard library, `Vector`s, giant
service/web-service classes, `SimpleDateFormat` everywhere). It also went
through a multi-year dormancy — commit activity was heavy 2015-2018, then
dropped to a handful of commits a year from 2019-2023, before picking back up
in 2024. Large parts of the tree are effectively archaeological: dead code,
abandoned experiments, or scaffolding for sites/features nobody runs anymore.

**Treat files not touched since 2023 as second priority and possibly dead.**
Before investing time exploring or refactoring a file, sanity-check its
recency, e.g. `git log --since=2023-01-01 --oneline -- <path>` (empty output ⇒
likely dormant — don't assume it's still load-bearing). The active surface is
concentrated in the shipping/session/proposal service beans and the REST/SOAP
web services (2024-2026 activity); that's where care matters most.

**Prime directive for design work: surface, then ask.** When planning a change,
always scan for refactoring opportunities (see "Refactoring & Design" below)
and call them out in the plan — this is not optional busywork, it's expected.
But only *act* on the ones inside the current task's blast radius; for anything
wider, propose it and ask the user first. This mirrors the existing
`gitnexus_impact` HIGH/CRITICAL-risk gate from `CLAUDE.md` — reuse that tool to
decide what "wider" means.

- **Prefer the Stream API over manual loops** where it doesn't hurt readability
  (e.g. `list.forEach(...)` / `.stream().map(...).collect(...)` instead of
  `for (T t : list) { ... }`). Exception: leave classic for-loops in place for
  logic that carries mutable cross-iteration state or correlates positions
  across several parallel lists/arrays (e.g. a running "best match index"
  tracked while walking parallel lists) — streams tend to obscure that kind of
  imperative bookkeeping rather than clarify it.
- **Never use deprecated boxed-type string constructors** — `new Integer(String)`,
  `new Double(String)`, `new Boolean(String)`, `new Long(String)`, etc. are
  deprecated and marked for removal. Use `Integer.parseInt(...)`,
  `Double.parseDouble(...)`, `Boolean.parseBoolean(...)`, etc. instead. (Plain
  `new Integer(int)` etc. are likewise deprecated for removal — prefer
  `Integer.valueOf(...)` or, in most cases, simple autoboxing.)
- **Prefer an instance-level logger over a static one** in new classes:
  `private final Logger logger = Logger.getLogger(getClass());` rather than
  `private static final Logger LOG = Logger.getLogger(Foo.class);`.
- **DTOs over raw `Map<String, Object>` / untyped collections**: when a chunk
  of code is built around passing around raw maps or loosely-typed
  collections and there's a natural opportunity to introduce a typed DTO
  (e.g. a Java record) instead, prefer doing so. However, treat this as its
  own deliberate refactor — if introducing a DTO would mean touching code
  beyond the task at hand (e.g. a shared upstream data source used by other
  callers), **ask the user first** rather than doing it opportunistically
  inside an unrelated task.
- **Building a per-request utility class with `new` is fine and endorsed** —
  e.g. `ExiPdfRtfExporter`, `AutoProcBestResultExtractor`,
  `DataCollectionReportBuilder`. These are plain, stateless objects built
  fresh per report/request; do **not** turn them into CDI beans or give them
  `@Inject` fields just for the sake of it. If such a class needs an EJB
  service, have its **caller** supply it as an explicit method/constructor
  parameter (the caller already has it, one way or another) rather than the
  utility class fetching it itself — neither via `Ejb3ServiceLocator` nor via
  `@Inject`. See `AutoProcBestResultExtractor.buildSpaceGroupNumberMap(SpaceGroup3Service)`
  and `DataCollectionReportBuilder.build(List, SpaceGroup3Service)` for the
  pattern: the service travels in as a parameter, the utility class itself
  stays a plain, directly-testable POJO with no DI machinery at all.
- **Prefer CDI `@Inject` over `Ejb3ServiceLocator` specifically for the
  `this.getXyzService()`-style accessor methods** on the REST base classes
  (`ParentWebService` in `ispyb-ejb`, `MXRestWebService`/`RestWebService` in
  `ispyb-rest`) — e.g. replacing
  ```java
  protected XyzService getXyzService() {
      return (XyzService) Ejb3ServiceLocator.getInstance().getLocalService(XyzService.class);
  }
  ```
  with an `@Inject`-backed field. CDI is already enabled project-wide
  (`beans.xml` with `bean-discovery-mode="all"` in `ispyb-ejb`, `ispyb-rest`,
  and `ispyb-ws`) and there's a working precedent of field injection on a
  JAX-RS resource
  (`ispyb-rest/src/main/java/ispyb/ws/rest/notification/SendMailUtils.java`).
  **However — do this incrementally, not as a blanket retrofit.**
  `ParentWebService` + `MXRestWebService` together have ~47 such accessor
  methods, extended by ~30 REST resource classes across every domain
  (shipping, sample, saxs, em, mx, ...). `gitnexus_impact` on these two
  classes returns **HIGH risk** (66 and 19 impacted symbols respectively).
  Only convert:
  - a **newly added** `getXyzService()`-style accessor (one that doesn't
    exist yet), or
  - an **existing** one you're already touching for the task at hand, when a
    concrete need requires it (e.g. testability).
  Leave the rest of the existing ~47 accessors on `Ejb3ServiceLocator`
  untouched. A full migration is a large, staged effort spanning ~69 files
  project-wide (`Ejb3ServiceLocator` usage isn't limited to these two
  classes) — **ask the user first** before taking on more than the one or
  two accessors the current task actually needs.

## Refactoring & Design

- **Always scan for refactoring opportunities when planning.** Make this an
  explicit planning-phase checklist item, not an afterthought — run through the
  checks below every time you plan a change in a file you're touching anyway.
  Anything inside the task's blast radius, do; anything wider, propose in the
  plan and ask (see "Read This First" above).

- **Replace reinvented wheels with the standard library.** This codebase has
  several hand-rolled utilities that just reimplement JDK functionality, worse:
  - `StringUtils.replaceInString` (`ispyb-ejb/src/main/java/ispyb/common/util/StringUtils.java`,
    ~60 lines of char-array matching through a `Vector` buffer) reimplements
    `String.replace(CharSequence, CharSequence)`. The single clearest example in
    the codebase — if you touch this file, replace call sites with the JDK
    method rather than maintaining the hand-rolled version.
  - Manual `BufferedReader`/`readLine()` loops with a bare `close()` call
    instead of try-with-resources — e.g. `OneDimensionalFileReader.readFile()`,
    `ScatteringCurvesParser`, `AutoProcProgramaAttachmentFileReader.readAttachment()`
    (all under `ispyb-ejb/.../biosaxs/services/utils/reader/` and
    `.../mx/services/utils/reader/`). Prefer `Files.lines(Path)` /
    `Files.readAllLines` wrapped in try-with-resources. See
    `ispyb-ejb/src/main/java/ispyb/server/mx/services/ws/rest/WsServiceBean.java`
    for the target style already used elsewhere in the codebase.
  - `try { ... } finally { x.close(); }` boilerplate that try-with-resources
    replaces outright — e.g. `IspybFileUtils.getFile` (the whole method is
    equivalent to `Files.readAllBytes(Path)`), the repeated close blocks in
    `HDF5FileReader`, and the HTTP response reader in
    `Session3ServiceBean.java` (~line 653) that is never closed at all and
    string-concatenates the body in a loop instead of using a `StringBuilder`.
  - Scattered, per-call `new SimpleDateFormat(...)` instances — not
    thread-safe, and easy to typo — instead of `java.time`
    (`DateTimeFormatter`, `LocalDate`/`LocalDateTime`). Cite
    `UpdateFromSMISWebService` (four `SimpleDateFormat` instances back to back
    to parse/format the same two dates) and `Stats3ServiceBean` (recreated in
    6 separate methods). `ToolsForEMDataCollection.java` (~line 109) shows the
    real cost of the pattern: `new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss")` —
    five `y`s and lowercase `mm`/`hh` (minutes-as-month, 12-hour clock) — a bug
    that `java.time`'s stricter, more explicit API makes much harder to write.

- **Avoid deep nesting (pyramid of doom).** Cap nesting at ~2 levels: use guard
  clauses / early returns instead of wrapping the whole method body in an
  `if`, extract inner loop bodies into well-named private methods, and prefer
  Streams for flatten-and-collect logic (per the Stream API rule above). Cite
  `External3ServiceBean.storeShipping`/`storeShippingFull`
  (`ispyb-ejb/.../shipping/external/`) — two near-identical 4-deep
  `dewars → containers → samples → subSamples` loop nests — and
  `ISPyBParser.export` (~38 control statements at nesting depth 5+). Note:
  `UpdateDataBase.java` is the most extreme case in the tree, but it's a
  dormant one-off DB-migration utility (last touched early 2024) — lower
  priority per the recency rule above, not a template to imitate.

- **Push toward SOLID / SRP; watch for God classes.** Keep new code cohesive.
  If a task lands you inside an existing God class, *surface* a split proposal
  in the plan rather than silently piling more onto it. Cite
  `ToolsForCollectionWebService` (~1738 lines, 35 unrelated `@WebMethod`
  operations spanning beamline setup, sessions, data collections, energy
  scans, XFE spectra, images, workflows, grid info, robot actions, and
  ligands), `ToolsForAutoprocessingWebService` (~1779 lines, 26 web methods),
  and `DataCollectionRestWebService` (~713 lines, ~100 JAX-RS endpoints
  spanning data collections, xrfscan, PDF/CSV/RTF report generation, analysis
  reports, and workflows). Splitting classes at this scale is HIGH-impact —
  never a drive-by; propose it and ask first.

- **Reach for GoF patterns where they remove duplication or branching**, not
  as decoration. Factory for type-driven object construction, Strategy for
  per-site/per-type behavior, Facade to give a God class a narrower front
  door, Adapter for legacy-boundary shims, Observer/event for fire-and-notify
  spots. The team already reaches for Factory but implements it as a
  hand-rolled `switch` that re-encodes the same type set in multiple places —
  see `FactoryProducer.getFactory` and `PDBFactoryProducer.getFactory`
  (`ispyb-ejb/.../biosaxs/services/utils/reader/`), both `switch (type) return
  new XxxFactory()`; an enum-method or a registry map would collapse the
  duplication. The `structure.getType().equals("PDB")` /
  `.equals("SEQUENCE")` chain in `ATSASPipeline3ServiceBean` (~line 989) is a
  similar Factory candidate. `switch (site)` in `AuthenticationRestWebService`
  (~line 71) is a Strategy candidate — though per the Site Policy section
  below, the right fix there is now to drop the non-DESY/non-LOCAL arm
  entirely rather than add a pattern around it.

- **Extract typed DTOs over raw maps and parallel lists/arrays.** This
  reinforces the existing DTO rule above — same principle, two new triggers:
  untyped `Map<String, Object>` dispatch (e.g. the `result.containsKey("frames")`
  checks in the `.dat`-reader factories) and parallel-array/parallel-list
  construction, as in `AutoProcBestResultExtractor`
  (`ispyb-rest/.../common/util/export/`, ~15 near-identical
  `split(",") → ArrayList` columns built side by side). As with the existing
  DTO rule: treat this as its own deliberate refactor and ask first if it
  would touch code beyond the task at hand.

- **Think beyond the literal task — but ask before building new hierarchies.**
  If a new class hierarchy or abstraction would demonstrably improve
  readability or maintainability, propose it as an explicit plan item rather
  than silently building it — run `gitnexus_impact` on anything it would
  touch, and get the user's sign-off before committing to it.

- **Follow Java 21 best practices in general.** The build already targets
  Java 21 (`maven.compiler.source=21` in the root `pom.xml`), so reach for the
  modern language toolkit where it improves clarity: records for DTOs/value
  carriers, `switch` *expressions* and pattern matching
  (`switch`/`instanceof`) instead of if/else type-chains, sealed types where a
  closed hierarchy is intended, text blocks for multi-line strings
  (especially JPQL — see "Persistence & Queries" below), `var` for obvious
  locals, `Stream.toList()`, and `Optional` over null-returns at API
  boundaries. This generalizes the specific modernizations already listed in
  this file (Stream API, no deprecated boxed constructors, DTOs) — treat
  "Java 21 idiom" as the default lens rather than restating those rules.
  Still bounded by the prime directive above: apply within the task's blast
  radius, propose-and-ask beyond it.

## Persistence & Queries

The service beans currently mix three query styles. Standardize on this
direction when you touch a query:

- **Prefer JPQL string queries; replace the programmatic Criteria API with
  string-literal (text-block) JPQL where you're already touching a query.**
  `entityManager.createQuery(jpql, Class)` is the house default (~342 call
  sites) and is far more readable than the JPA Criteria API
  (`CriteriaBuilder`/`CriteriaQuery`/`Root`, ~252 references across ~30
  service beans). Cite `Protein3ServiceBean.findByAcronymAndProposalId`
  (~line 178) as the canonical example of why: it hand-builds a
  `CriteriaQuery` and calls `cq.where(...)` **twice** — once for the
  `proposalId` predicate, once for `acronym` — and the second call *silently
  replaces* the first rather than ANDing with it (Criteria's `where` overwrites,
  it doesn't accumulate), so the `proposalId` filter is quietly dropped. A
  single JPQL string with an explicit `WHERE ... AND ...` (using a Java 21
  text block for readability) is both shorter and correct. Because rewriting
  a query changes runtime behavior, verify the generated SQL/semantics before
  and after, and ask before touching queries outside the current task's
  scope.
- **Prefer `createQuery` (JPQL) over `createNativeQuery` (raw SQL).** Native
  queries (~140 call sites, e.g. throughout `EM3ServiceBean`) bypass JPA
  typing and bind the code to one database dialect. When adding or reworking
  a query, reach for JPQL first; keep `createNativeQuery` only where JPQL
  genuinely can't express what's needed (database-specific functions, bulk
  operations) — and say so in the plan. Don't convert existing native queries
  wholesale outside the task at hand; that's a cross-cutting change, ask
  first.

## Site Policy — DESY (+ LOCAL) only

The only supported deployment sites are **DESY** (production) and **LOCAL**
(local-development mode — this is a real, actively-used mode, *not* a legacy
site; `ISPyB.properties` defaults `ISPyB.site=LOCAL` for exactly this reason).
Everything inherited from upstream ISPyB for other synchrotron sites — ESRF,
EMBL, SOLEIL, ALBA, MAX IV, and similar (Diamond/DLS, BESSY, PSI, SOLARIS,
Elettra, ...) — is dead weight and should be treated as such. The `SITE` enum
in `Constants.java` (~line 100) is already just `DESY`/`LOCAL`; what's left is
scattered per-site helper classes, conditionals, and config.

- **The per-site beamline enums are the main target.** Package
  `ispyb-ejb/src/main/java/ispyb/common/util/beamlines/` holds
  `DESYBeamlineEnum` (keep) alongside `ESRFBeamlineEnum`, `EMBLBeamlineEnum`,
  `SOLEILBeamlineEnum`, `ALBABeamlineEnum`, and `MAXIVBeamlineEnum` (legacy).
  `ALBABeamlineEnum` has zero references anywhere — safe to delete outright.
  `EMBLBeamlineEnum`, `SOLEILBeamlineEnum`, and `MAXIVBeamlineEnum` are only
  imported (and likely unused) by `ToolsForBLSampleWebService.java`.
- **Distinguish "delete" from "migrate" — don't blindly remove what's still
  called.** `ESRFBeamlineEnum` is *not* dead: `Session3ServiceBean` (a hot,
  actively-maintained file) uses it for session-protection logic
  (`beamlinesToProtect`), and `ToolsForBLSampleWebService` uses it too. Legacy
  code that live DESY code still calls is a latent correctness bug, not dead
  code — it needs to be **migrated to the DESY equivalent**
  (`DESYBeamlineEnum`), not deleted wholesale. Confirm the DESY replacement
  data is actually correct before switching, and surface the migration in the
  plan.
- **Collapse residual site conditionals to the DESY/LOCAL pair.** When a task
  touches a `switch (Constants.getSite())` or `SITE_IS_DESY()`-style guard
  (e.g. in `UpdateFromSMIS.java`, `ToolsForShippingWebService.java`,
  `AuthenticationRestWebService.java`), drop any arms for sites other than
  DESY/LOCAL while you're there.
- **Scope this like every other refactor here: opportunistic within the task,
  ask before a sweep.** A full legacy-site purge touches schema SQL, WildFly
  `standalone*.xml` configs, and generated JS artifacts, not just Java — treat
  a project-wide removal as its own deliberate effort, not something to do
  inside an unrelated task. Out of scope entirely: the database-dialect axis
  (`DATABASE_IS_MYSQL`/`DATABASE_IS_ORACLE`) is not a "site" and stays as-is;
  login modules named after sites (`DESYLoginModule`, `DOORLoginModule` — DOOR
  is DESY's user portal) are infrastructure, not legacy-site cruft.