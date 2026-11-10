# Dynamic Satellite Network Optimization

A Java project for DAA based on **disaster recovery routing in a dynamic satellite mesh network**.

## Algorithms compared
1. Dijkstra
2. Congestion-Aware Dijkstra
3. Hybrid Recovery Routing

## Features
- Graph-based satellite network model
- Congestion-aware edge weighting
- Static failure simulation
- Dynamic route recovery in the hybrid planner
- CSV export for Power BI / Tableau

## How to compile
```bash
javac -d out $(find src -name "*.java")
```

## How to run demo
```bash
java -cp out app.Main demo
```

## How to run benchmark
```bash
java -cp out app.Main benchmark results.csv
```

## How to run correctness validation
```bash
java -cp out app.Main validate
```

## CSV columns
- algorithm
- nodes
- edges
- failureRate
- congestionLevel
- runtimeMs
- totalCost
- hopCount
- rerouteCount
- success
- path

## Report outline
Use the same project for:
- problem statement
- architecture
- algorithm comparison
- implementation
- correctness checks
- performance analysis
- graphs in Power BI / Tableau
