```mermaid
flowchart TD

    U[Users]

    CDN[CDN]
    LB[Load Balancer]

    Docker[Docker Image<br/>food-delivery:v1]

    subgraph APP["Application Layer"]
        A1[Node.js Container 1]
        A2[Node.js Container 2]
        A3[Node.js Container 3]
    end

    Redis[(Redis Cache)]

    subgraph DATA["Data Layer"]
        DB[(Primary PostgreSQL)]
        Replica[(Read Replica)]
    end

    U --> CDN
    CDN --> LB

    Docker -. creates .-> A1
    Docker -. creates .-> A2
    Docker -. creates .-> A3

    LB --> A1
    LB --> A2
    LB --> A3

    A1 --> Redis
    A2 --> Redis
    A3 --> Redis

    A1 --> DB
    A2 --> DB
    A3 --> DB

    DB --> Replica
```