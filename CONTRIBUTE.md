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