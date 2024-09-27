/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.flowsandcut;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elton
 */
public class FlowsAndCut {

    public static void main(String[] args) {
        String[]vertexes = {"A","B","C","D","E"};
        List<String> solution;
        // the minCut method will return the adges that need to be removed
        solution = minCut(initialGraph(),ff(initialGraph(),0,4),vertexes );
        //Print solution
        System.out.println(solution);
        
    }
    
    public static List<String> minCut(int[][] startingGraph, int[][]residualGraph, String[]vertexes ){
        // create a List to store the cutting edges
        List<String> cutEdges = new ArrayList();
        // loop through the both the initial and the residual arrays to find the edges that are saturated
        // after the max flow is found, i.e. the the edges that had a value in the original graph,
        // but have 0 in the residual graph because they were used to the max.
        for(int row = 0; row < startingGraph.length; row ++ )
            for(int column = 0; column < startingGraph.length; column++)
            {
                if((startingGraph [row][column] > 0) && (residualGraph[row][column] == 0))
                {
                    // add the saturated edges to the returning arraylist
                    cutEdges.add(vertexes[row] + vertexes[column]);
                }
            }  
           return cutEdges;
       }
        
    
   
   
    public static List<Integer> findPath(int[][] graph, int source, int target, List<Integer> path) {
        //Finds a path between two vertices in a directed acyclic graph.
//          Inputs
//  ------
//      graph : list
//    adjacency matrix of the graph to search source, target : int
//    The two vertices at both ends of the path
//      path : list
//    path from previous recursive call; [] for initial call
//
//      Returns
//      -------
//      path : list
//     vertices along the path from source to vertex; None if path doesn't exist.
//  
//     The source is always part of the path
        path.add(source);
//        Base case
        if (source == target) {
            return path;
        }
//    Consider every vertex adjacent to the source; because we use adjacency
//    matrix representation, we examine the row corresponding to the source
//    vertex: graph[source]. Every element in that row that is not zero represents
//    an edge. If the vertex on the other side of that edge is not already in the
//    path, we add it to the path. Then we ask if there is a path from that
//    vertex to the target vertex.
        for (int v = 0; v < graph.length; v++) {
            if (graph[source][v] > 0 && !path.contains(v)) {
                List<Integer> possiblePath = new ArrayList<>(path);
                List<Integer> result = findPath(graph, v, target, possiblePath);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public static int[][] ff(int[][] graph, int source, int target) {
//     Finds the maximum flow across a flow graph and identifies the minimum cuts
//     that will disable the flow between a source and a target vertex in the graph.
//
//    Inputs
//    ------
//    graph : list
//    adjacency matrix of the input graph
//    source : int
//    label of the source vertex
//    target : int
//    label of the target vertex
//
//    Returns
//    -------
//    max_flow : int
//    The max flow can can travel from source to target in the input graph
//    min_cuts : list
//    The small set of edges that can reduce the graph's flow to 0.
 

//   Variable to return        
        int maxFlow = 0;
//       Initially, the residual graph is identical to the input graph
        int[][] residualGraph = new int[graph.length][graph[0].length];
        for (int i = 0; i < graph.length; i++) {
            System.arraycopy(graph[i], 0, residualGraph[i], 0, graph[i].length);
        }
//     Find an augmenting path; any such path is fine, to get the iteration loop
//     started. The loop continues as long as there is an augmenting path in the
//     residual graph. (Reminder: an augmenting path in the residual graph is a
//     path from the source to the target vertex).
        List<Integer> augmentingPath = findPath(residualGraph, source, target, new ArrayList<>());

        while (augmentingPath != null) {
//    # Find the edge with the least capacity along this path. Edges can be
//    # identified by the elements of augmenting_path: adjacent elements of the
//    # list are the vertices on that edge, e.g., an edge u --> v in the path will
//    # have u = augmenting_path[i] and
//    #      v = augmenting_path[i+1]
//    # This is a standard search for a min value in a collection of values. It
//    # begins by assuming that the first edge in the augmenting path has the least
//    # capacity of the path. That edge is between the vertices in positions [0]
//    # and [1] in the augmenting_path list. The weight of their edge is obtained
//    # by referencing the residual_graph list for these two vertices. Then we
//    # traverse the remaining edges in the augmenting path in to see if there is
//    # one with less capacity.
            int minCapacity = Integer.MAX_VALUE;
            for (int i = 0; i < augmentingPath.size() - 1; i++) {
//                Obtain vertices u, v for edge u --> v in augmenting path
                int u = augmentingPath.get(i);
                int v = augmentingPath.get(i + 1);
                minCapacity = Math.min(minCapacity, residualGraph[u][v]);
            }
//#   Now that we know the bottleneck of the augmenting path, we can add its
//    # capacity to the max flow of the graph, because that's as much flow as we
//    # can push through this path.
            maxFlow += minCapacity;
//    # Update residual capacities along the augmenting path. This is done by
//    # traversing the augmenting path (yes, again), subtracting the path's
//    # minimum capacity from its existing edges, and adding back-edges where
//    # necessary based on the skew symmetry property.

            for (int i = 0; i < augmentingPath.size() - 1; i++)/*# -1 to compensate for [i+1] below
      # Obtain vertices u, v for edge u --> v in augmenting path*/ {
                int u = augmentingPath.get(i);
                int v = augmentingPath.get(i + 1);
                residualGraph[u][v] -= minCapacity;//# existing edge
                residualGraph[v][u] += minCapacity;//back-edge due to skew symmetry
            }
//# Now that the residual graph has been updated let's see if there is another
//   # path between source and target. If None, the while loop ends.
            augmentingPath = findPath(residualGraph, source, target, new ArrayList<>());
        }

        List<int[]> minCuts = new ArrayList<>();
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j] > 0 && residualGraph[i][j] == 0) {
                    minCuts.add(new int[]{i, j});
                }
            }
        }

        int[] result = new int[minCuts.size() * 2 + 1];
        result[0] = maxFlow;
        for (int i = 0; i < minCuts.size(); i++) {
            result[i * 2 + 1] = minCuts.get(i)[0];
            result[i * 2 + 2] = minCuts.get(i)[1];
        }
// done
        return residualGraph;
    }
   
   public static int[][] initialGraph(){
           int[][] G = {
            //  A   B   C   D   E
            {  0, 20,  0,  0,  0}, // A
            {  0,  0,  5,  6,  0}, // B
            {  0,  0,  0,  3,  7}, // C
            {  0,  0,  0,  0,  8}, // D
            {  0,  0,  0,  0,  0}  // E
        };

        // If you need to print the graph
//        for (int[] row : G) {
//            System.out.println(Arrays.toString(row));
//        }
        return G;
   }
}