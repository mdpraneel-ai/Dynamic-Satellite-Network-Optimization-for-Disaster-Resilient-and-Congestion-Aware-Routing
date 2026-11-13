package algo;

import model.Graph;
import model.RouteResult;

public interface RoutePlanner {
    RouteResult findRoute(Graph graph, int source, int destination);
    String getName();
}
