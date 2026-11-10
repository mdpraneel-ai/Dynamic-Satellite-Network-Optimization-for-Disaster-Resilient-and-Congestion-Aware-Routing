# Dynamic Satellite Network Optimization for Disaster-Resilient and Congestion-Aware Routing

## 1. Introduction
Satellite communication networks carry time-critical traffic. When satellites or links fail, routes must be recomputed quickly. Congestion also changes effective path quality. This project models the network as a weighted graph and compares three routing strategies.

## 2. Problem Statement
Given a satellite mesh network with dynamic failures and congestion, find an efficient route from source to destination while minimizing latency and maintaining route reliability.

## 3. Objectives
- Model satellites and ground stations as graph nodes.
- Model links as weighted edges.
- Compare multiple algorithm design techniques on the same problem.
- Simulate failures and congestion.
- Measure runtime, cost, success rate, and reroute count.

## 4. Assumptions
- The network is represented as a weighted graph.
- Edge weights represent base latency.
- Congestion increases effective weight.
- Some nodes and links may fail.
- The hybrid method may reroute during traversal.

## 5. Algorithms
### 5.1 Dijkstra
Classic shortest-path algorithm using only base latency.

### 5.2 Congestion-Aware Dijkstra
Modifies the edge cost using congestion and failure risk.

### 5.3 Hybrid Recovery Routing
Computes an initial shortest path and dynamically repairs the path if a failure is encountered.

## 6. Correctness Checks
- Verify source and destination are valid.
- Verify every consecutive pair in the returned path is connected.
- Recompute path cost manually.
- Test small graphs with known answers.
- Validate recovery behavior under failure scenarios.

## 7. Complexity
### Dijkstra
Time: O((V + E) log V)
Space: O(V + E)

### Congestion-Aware Dijkstra
Time: O((V + E) log V)
Space: O(V + E)

### Hybrid Recovery Routing
Worst case: O((V + E) log V) per reroute
Space: O(V + E), higher if backups are stored

## 8. Experimental Plan
### Vary
- number of nodes
- failure rate
- congestion level
- graph density

### Measure
- runtime
- total path cost
- hop count
- reroute count
- success rate

## 9. Graphs to Create
- Runtime vs number of nodes
- Success rate vs failure rate
- Cost vs congestion
- Reroute count vs failure rate
- Memory vs graph size

## 10. Conclusion
The baseline algorithm is fastest on stable networks. The congestion-aware method gives better routes under load. The hybrid method performs best when failures are dynamic and route continuity matters.
