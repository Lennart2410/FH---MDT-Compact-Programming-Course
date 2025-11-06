# Hometask 3 — Theory Answers

## Comparison of Concurrency Models

### 1. Thread-Based Concurrency

**Pros:**
- Direct control over thread lifecycle
- Simple to implement for small-scale tasks

**Cons:**
- Risk of race conditions and deadlocks
- Difficult to scale and debug
- Manual synchronization required

### 2. Actor Model

**Pros:**
- Avoids shared state by using message passing
- Easier to reason about concurrency
- Scales well in distributed systems

**Cons:**
- Requires a framework (e.g., Akka)
- More abstract and less intuitive for beginners

### 3. Fork/Join Framework
**Pros:**
- Efficient for divide-and-conquer algorithms
- Automatically manages task splitting and joining

**Cons:**
- Best suited for CPU-bound tasks
- Less flexible for I/O or real-time systems

---

## Concurrency vs Parallelism
- **Concurrency** is about managing multiple tasks that make progress independently. Tasks may run on the same core but
  are interleaved.
- **Parallelism** is about executing multiple tasks simultaneously on different cores.

**Example from this project:**
- AGVs are **concurrent** — each task is submitted and executed independently.
- If run on a multi-core system, they may also be **parallel**, executing truly simultaneously.

---

## Blocking vs Non-blocking Concurrency Algorithms

### Blocking Algorithms
- Threads wait for resources or conditions (e.g., `Thread.sleep()`, `synchronized`)
- Easier to understand and implement
- Can lead to performance bottlenecks

### Non-blocking Algorithms
- Threads continue working without waiting (e.g., `AtomicInteger`, `ConcurrentLinkedQueue`)
- More responsive and scalable
- Harder to implement correctly

**In this project:**
- Blocking is used via `Thread.sleep()` and `ExecutorService`
- A non-blocking approach could improve responsiveness and reduce idle time

---

## Summary
This theory section complements the AGV simulation by explaining the underlying concurrency principles. It demonstrates
understanding of Java’s concurrency models and how they apply to real-world system design.