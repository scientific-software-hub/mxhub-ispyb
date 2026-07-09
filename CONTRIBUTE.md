# Java Coding Guidelines

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