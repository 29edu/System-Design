# Redis Cache Cluster - Complete Architecture

```mermaid
flowchart TB

    %% =========================
    %% USER TRAFFIC
    %% =========================

    User[Users]

    LB[Load Balancer]

    User --> LB

    %% =========================
    %% APPLICATION LAYER
    %% =========================

    Docker[Docker Image<br/>food-delivery:v1]

    subgraph APP["Application Layer (Multiple Instances of Same App)"]

        A1[Node.js Container 1]
        A2[Node.js Container 2]
        A3[Node.js Container 3]

    end

    Docker -. Creates .-> A1
    Docker -. Creates .-> A2
    Docker -. Creates .-> A3

    LB --> A1
    LB --> A2
    LB --> A3

    %% =========================
    %% REDIS CLIENT
    %% =========================

    RC[Redis Cluster Client<br/>Hash Key → Hash Slot]

    A1 --> RC
    A2 --> RC
    A3 --> RC

    %% =========================
    %% HASH SLOT ROUTING
    %% =========================

    HS[Redis Cluster Routing<br/>16384 Hash Slots]

    RC --> HS

    %% =========================
    %% REDIS CLUSTER
    %% =========================

    subgraph CACHE["Redis Cache Cluster"]

        P1[(Primary Node 1)]
        R1[(Replica Node 1)]

        P2[(Primary Node 2)]
        R2[(Replica Node 2)]

        P3[(Primary Node 3)]
        R3[(Replica Node 3)]

        P1 --> R1
        P2 --> R2
        P3 --> R3

    end

    HS -->|Slots 0-5460| P1
    HS -->|Slots 5461-10922| P2
    HS -->|Slots 10923-16383| P3

    %% =========================
    %% DATABASE
    %% =========================

    subgraph DATA["Database Layer"]

        DB[(PostgreSQL Primary)]

        REP[(Read Replica)]

    end

    P1 --> DB
    P2 --> DB
    P3 --> DB

    DB --> REP

    %% =========================
    %% EXAMPLE KEYS
    %% =========================

    Example1["User:1 → Hash Slot 3000 → Node 1"]
    Example2["User:50000 → Hash Slot 8000 → Node 2"]
    Example3["User:99999 → Hash Slot 15000 → Node 3"]

    HS -. Example .-> Example1
    HS -. Example .-> Example2
    HS -. Example .-> Example3
```

---

## What is a Cache Cluster?

A Cache Cluster is a group of Redis servers working together as a single logical cache.

Instead of:

Redis

You have:

- Redis Node 1
- Redis Node 2
- Redis Node 3

Together they form a Redis Cluster.

---

## Why Use a Cache Cluster?

### More Memory

Single Redis:

- 16 GB RAM

Cluster:

- Node 1 = 16 GB
- Node 2 = 16 GB
- Node 3 = 16 GB

Total = 48 GB

---

### More Throughput

Single Redis:

- 100K requests/sec

Cluster:

- Node 1 handles part of traffic
- Node 2 handles part of traffic
- Node 3 handles part of traffic

Total throughput increases significantly.

---

### Fault Tolerance

If:

- Primary Node 2 crashes

Then:

- Replica Node 2 becomes Primary

Traffic continues with minimal disruption.

---

## Who Chooses the Redis Node?

The application does NOT choose.

Node.js simply executes:

GET User:7000

The Redis Cluster Client:

1. Hashes the key
2. Computes a hash slot
3. Determines which Redis node owns that slot
4. Sends request to that node

---

## How Sharding Works

Redis Cluster contains:

16384 hash slots

Example distribution:

- Node 1 → Slots 0-5460
- Node 2 → Slots 5461-10922
- Node 3 → Slots 10923-16383

Examples:

- User:1 → Slot 3000 → Node 1
- User:50000 → Slot 8000 → Node 2
- User:99999 → Slot 15000 → Node 3

This distribution of data across nodes is called:

SHARDING

---

## Real Request Flow

User
↓
Load Balancer
↓
Node.js Container
↓
Redis Cluster Client
↓
Hash Key
↓
Compute Hash Slot
↓
Correct Redis Node
↓
Return Cached Data

If Cache Miss:
↓
Database
↓
Store Result Back Into Cache

---

## Interview Definition

A Redis Cache Cluster is a collection of Redis nodes that work together to provide higher memory capacity, increased throughput, fault tolerance, and horizontal scalability. Data is distributed across nodes using sharding through hash slots, and the Redis Cluster automatically routes requests to the correct node based on the cache key.