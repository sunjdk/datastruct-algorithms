/*
 * @(#)DiGraphs.java
 */

package ds.util;

import java.awt.geom.IllegalPathStateException;

/**
 * This class contains static methods for the classical graph scanning algorithms, topological
 * sort, strong components, and path optimization. <p>
 *
 */
public class DiGraphs
{
	// largest positive value an integer can have
	final public static int INFINITY = Integer.MAX_VALUE;

	/**
	 * Performs the breadth-first traversal from sVertex and
	 * returns the list of visited vertices.
	 * @param g  directed graph
	 * @param sVertex  starting vertex of the breadth-first traversal.
	 * @return list of visited vertices.
	 */
	public static <T> LinkedList<T> bfs(DiGraph<T> g, T sVertex)
	{
		// queue stores adjacent vertices; list stored visited
		// vertices
		LinkedQueue<T> visitQueue = new LinkedQueue<T>();
		LinkedList<T> visitList = new LinkedList<T>();

		// set and iterator retrieve and scan neighbors of a vertex
		Set<T> edgeSet;
		Iterator<T> edgeIter;

		T currVertex = null, neighborVertex = null;

		// check that starting vertex is valid
		if (!g.containsVertex(sVertex))
				throw new IllegalArgumentException(
					"bfs(): starting vertex not in the graph");

		// color all vertices WHITE
		g.colorWhite();

		// initialize queue with starting vertex
		visitQueue.push(sVertex);

		while (!visitQueue.isEmpty())
		{
			// remove a vertex from the queue, color it black, and
			// add to the list of visited vertices
			currVertex = visitQueue.pop();
			g.setColor(currVertex,VertexColor.BLACK);
			visitList.add(currVertex);

			// obtain the set of neighbors for current vertex
			edgeSet = g.getNeighbors(currVertex);
			// sequence through the neighbors and look for vertices
			// that have not been visited
			edgeIter = edgeSet.iterator();
			while (edgeIter.hasNext())
			{
				neighborVertex = edgeIter.next();

				if (g.getColor(neighborVertex) == VertexColor.WHITE)
				{
					// color unvisited vertex GRAY and
					// push it onto queue
					g.setColor(neighborVertex,VertexColor.GRAY);
					visitQueue.push(neighborVertex);
				}
			}
		}

		return visitList;
	}

	/**
	 * Returns a <tt>LinkedList</tt> that gives a path computed by a graph
	 * traversal algorithm from the starting vertex sVertex to the ending
	 * vertex eVertex. The method is called after executing path optimization
	 * algorithms such as <tt>shortestPath</tt>, <tt>minumumPath</tt> and
	 * <tt>dagMinimumPath</tt> to identify the path from a starting vertex and
	 * a reachable vertex v.
	 * @param g  directed graph
	 * @param sVertex  starting vertex.
	 * @param eVertex  ending vertex.
	 * @return list of vertices on a path from sVertex to eVertex.
	 */
	public static <T> LinkedList<T> path(DiGraph<T> g,
													 T sVertex, T eVertex)
	{
		T currVertex = eVertex;
		LinkedList<T> path = new LinkedList<T>();

		if (g.getData(eVertex) == DiGraphs.INFINITY)
			return path;

		while (!currVertex.equals(sVertex))
		{
			path.addFirst(currVertex);
			currVertex = g.getParent(currVertex);
		}

		path.addFirst(sVertex);

		return path;
	}


	/**
	 * Uses the breadth-first traversal algorithm to determine the
	 * minimum number of edges in a path from sVertex to a
	 * vertex in the graph reachable from sVertex. upon return,
	 * the dataValue field of each vertex in g is either the
	 * shortest path length to the vertex or is INFINITY if the
	 * vertex was not reacheable from sVertex. call
	 * path(g, sVertex, v) to find the shortest path from sVertex to v.
	 *
	 * @param g  directed graph
	 * @param sVertex  starting vertex.
	 */
	public static <T> void shortestPath(DiGraph<T> g, T sVertex)
	{
		// BFS uses a queue to store adjacent vertices
		LinkedQueue<T> visitQueue = new LinkedQueue<T>();
		Set<T> edgeSet;
		Iterator<T> edgeIter;
		T currVertex = null, neighborVertex = null;
		int currentPathLength;

		if (!g.containsVertex(sVertex))
			throw new IllegalArgumentException(
				"shortestPath(): starting vertex not in the graph");

		// set each vertex data value to INFINITY
		g.initData();

		// sVertex is its own parent and the shortest path
		// to itself has length 0
		g.setParent(sVertex, sVertex);
		g.setData(sVertex, 0);

		// insert starting vertex into the queue
		visitQueue.push(sVertex);

		// process vertices until the queue is empty
		while (!visitQueue.isEmpty())
		{
			// delete a queue entry
			currVertex = visitQueue.pop();

			edgeSet = g.getNeighbors(currVertex);
			// sequence through the edge set and look for vertices
			// that have not been visited. assign each such vertex
			// a dataValue of currentPathLength + 1
			currentPathLength = g.getData(currVertex);
			edgeIter = edgeSet.iterator();
			while (edgeIter.hasNext())
			{
				neighborVertex = edgeIter.next();

				if (g.getData(neighborVertex) == INFINITY)
				{
					g.setData(neighborVertex, currentPathLength + 1);
					g.setParent(neighborVertex, currVertex);
					visitQueue.push(neighborVertex);
				}
			}
		}
	}

	/**
	 * Depth-first visit takes a starting vertex and creates a list
	 * that contains the visited vertices in reverse order of finishing
	 * time. When the boolean value checkForCycle is true, the method
	 * throws an IllegalPathStateException if it detects a
	 * cycle.
	 * @param g  directed graph
	 * @param sVertex  starting vertex.
	 * @param dfsList  List of visited nodes created by the algorithm.
	 * @param checkForCycle  test whether the presence of a cycle results
	 *                 in an exception.
	 * @throws IllegalPathStateException if it detects a cycle when checkForCycle
	 *                  is true.
	 */
	public static <T> void dfsVisit(DiGraph<T> g, T sVertex, LinkedList<T> dfsList,
										 	  boolean checkForCycle)
	{
		T neighborVertex;
		Set<T> edgeSet;
		// iterator to scan the adjacency set of a vertex
		Iterator<T> edgeIter;
		VertexColor color;

		if (!g.containsVertex(sVertex))
			throw
				new IllegalArgumentException(
					"dfsVisit(): vertex not in the graph");

		// color vertex GRAY to note its discovery
		g.setColor(sVertex, VertexColor.GRAY);

		edgeSet = g.getNeighbors(sVertex);

		// sequence through the adjacency set and look for vertices
		// that are not yet discovered (colored WHITE).
		// recursively call dfsVisit() for each such vertex. if a
		// vertex in the adjacency list is GRAY, the
		// vertex was discovered during a previous call and there
		// is a cycle that begins and ends at the vertex; if
		// checkForCycle is true, throw an exception
		edgeIter = edgeSet.iterator();
		while (edgeIter.hasNext())
		{
			neighborVertex = edgeIter.next();
			color = g.getColor(neighborVertex);
			if (color == VertexColor.WHITE)
				dfsVisit(g,neighborVertex, dfsList, checkForCycle);
			else if (color == VertexColor.GRAY && checkForCycle)
				throw new IllegalPathStateException(
					"dfsVisit(): cycle involving vertices " +
					sVertex + " and " + neighborVertex);
		}

		// finished with vertex sVertex. make it BLACK
		// and add it to the front of dfsList
		g.setColor(sVertex, VertexColor.BLACK);
		dfsList.addFirst(sVertex);
	}

	/**
	 * In the depth-first search. dfsList contains all the graph vertices
	 * in the reverse order of their finishing times.
	 * @param g  directed graph
	 * @param dfsList  list that is created by the depth-first search.
	 */
	public static <T> void dfs(DiGraph<T> g,
										LinkedList<T> dfsList)
	{
		Iterator<T> graphIter;
		T vertex = null;

		// clear dfsList
		dfsList.clear();

		// initialize all vertices to WHITE
		g.colorWhite();

		// call dfsVisit() for each WHITE vertex
		graphIter = g.vertexSet().iterator();
		while (graphIter.hasNext())
		{
			vertex = graphIter.next();
			if (g.getColor(vertex) == VertexColor.WHITE)
				dfsVisit(g,vertex, dfsList, false);
		}
	}

	/**
	 * The linked list provides a topological sort ordering of the vertices.
	 * @param g  directed graph
	 * @param tlist  list that is created to describe a topological sort order of the vertices.
	 */
	public static <T> void topologicalSort(DiGraph<T> g,LinkedList<T> tlist)
	{
		Iterator<T> graphIter;
		T vertex = null;

		// clear the list that will contain the sort
		tlist.clear();

		g.colorWhite();

		// cycle through the vertices, calling dfsVisit() for each
		// WHITE vertex. check for a cycle
		try
		{
			// call dfsVisit() for each WHITE vertex
			graphIter = g.vertexSet().iterator();
			while (graphIter.hasNext())
			{
				vertex = graphIter.next();
				if (g.getColor(vertex) == VertexColor.WHITE)
					dfsVisit(g,vertex, tlist, true);
			}
		}

		catch (IllegalPathStateException ipse)
		{
			throw new
				IllegalPathStateException(
					"topologicalSort(): graph has a cycle");
		}
	}

	/**
	 * Returns true if the graph is acyclic.
	 * @param g  directed graph
	 * @return true if it detects a cycle.
	 */
	public static <T> boolean acyclic(DiGraph<T> g)
	{
		// use for calls to dfsVisit()
		LinkedList<T> dfsList = new LinkedList<T>();
		Iterator<T> graphIter;
		T vertex = null;

		// initialize all vertices to WHITE
		g.colorWhite();

		// call dfsVisit() for each WHITE vertex.
		// catch an IllegalPathStateException in a call to
		// dfsVisit()
		try
		{
			// call dfsVisit() for each WHITE vertex
			graphIter = g.vertexSet().iterator();
			while (graphIter.hasNext())
			{
				vertex = graphIter.next();
				if (g.getColor(vertex) == VertexColor.WHITE)
					dfsVisit(g,vertex, dfsList, true);
			}
		}

		catch (IllegalPathStateException iae)
		{
			return false;
		}

		return true;
	}

	/**
	 * Returns the transpose of the graph.
	 * @param g  directed graph
	 * @return the transpose of the graph.
	 */
	public static <T> DiGraph<T> transpose(DiGraph<T> g)
	{
		T currVertex = null, neighborVertex = null;
		Set<T> edgeSet, vertexSet;
		Iterator<T> edgeIter, graphIter;
		DiGraph<T> gt = new DiGraph<T>();

		// insert each vertex of g into gt
		vertexSet = g.vertexSet();
		graphIter = vertexSet.iterator();
		while (graphIter.hasNext())
		{
			currVertex = graphIter.next();
			gt.addVertex(currVertex);
		}

		// insert reveresed edges of g into gt
		graphIter = vertexSet.iterator();
		while (graphIter.hasNext())
		{
			currVertex = graphIter.next();
			edgeSet = g.getNeighbors(currVertex);
			edgeIter = edgeSet.iterator();
			while (edgeIter.hasNext())
			{
				neighborVertex = edgeIter.next();
				gt.addEdge(neighborVertex, currVertex,
							  g.getWeight(currVertex, neighborVertex));
			}
		}

		return gt;
	}

	/**
	 * Depth-first visit takes a starting vertex and creates a list
	 * that contains the visited vertices in reverse order of finishing
	 * time. When the boolean value checkForCycle is true, the method
	 * throws an IllegalPathStateException if it detects a
	 * cycle.
	 * @param g  DiGraph
	 * @param component  List of visited nodes created by the algorithm.
	 * @throws IllegalPathStateException if it detects a cycle when checkForCycle
	 *                  is true.
	 */
	// find the strong components of the graph. each element of
	// component is a LinkedList of the elements in a strong
	// component
	public static <T> void
	strongComponents(DiGraph<T> g,
						  ArrayList<LinkedList<T>> component)
	{
		T currVertex = null;
		// list of vertices visited by dfs() for graph g
		LinkedList<T> dfsList = new LinkedList<T>();
		// list of vertices visited by dfsVisit() for g transpose
		LinkedList<T> dfsGTList = null;
		// used to scan dfsList
		Iterator<T> gIter;
		// transpose of the graph
		DiGraph<T> gt = null;

		// clear the return vector
		component.clear();

		// execute depth-first tranversal of g
		dfs(g, dfsList);

		// compute gt
		gt = transpose(g);

		// initialize all vertices in gt to WHITE
		// (unvisited)
		gt.colorWhite();

		// call dfsVisit() for gt from vertices in dfsList
		gIter = dfsList.iterator();
		while(gIter.hasNext())
		{
			currVertex = gIter.next();
			// call dfsVisit() only if vertex has not been visited
			if (gt.getColor(currVertex) == VertexColor.WHITE)
			{
				// create a new LinkedList to hold next strong
				// component
				dfsGTList = new LinkedList<T>();
				// do dfsVisit() in gt for starting vertex currVertex
				dfsVisit(gt, currVertex, dfsGTList, false);
				// add strong component to the ArrayList
				component.add(dfsGTList);
			}
		}
	}

	/**
	 * Finds the path with minimum total weight from <tt>sVertex</tt> to
	 * each vertex in the graph reachable from sVertex. Upon
	 * return, the dataValue field of each vertex in graph <tt>g</tt> is either
	 * the minimum path weight to the vertex or is INFINITY if the
	 * vertex was not reacheable from sVertex. Call
	 * the method path(g, sVertex, v) to find the minimum path from sVertex
	 * to v
	 * @param g  directed graph
	 * @param sVertex  starting vertex.
	 */
	public static <T> void minimumPath(DiGraph<T> g, T sVertex)
	{
		T currVertex = null, neighborVertex = null;
		// heap (priority queue) that stores MinInfo objects
		HeapPQueue<MinInfo<T>> minPathPQ =
			new HeapPQueue<MinInfo<T>>(new Less<MinInfo<T>>());
		// used when inserting MinInfo entries
		// into the priority queue or erasing entries
		MinInfo<T> vertexData = null;
		// edgeSet is edge set of vertex we are visiting. edgeIter
		// is used to traverse edgeSet
		Set<T> edgeSet;
		Iterator<T> edgeIter;
		// computed minimum weight
		int newMinWeight, numVisited = 0, numVertices;
	
		if (!g.containsVertex(sVertex))
			throw new IllegalArgumentException(
				"minimumPath(): vertex not in the graph");
	
		// color the vertices WHITE and set each vertex
		// dataValue to INFINITY
		g.colorWhite();
		g.initData();
	
		// formulate initial priority queue entry
		vertexData = new MinInfo<T>();
		vertexData.endV = sVertex;
		// path weight from sVertex to sVertex is 0
		vertexData.pathWeight = 0;
	
		// update dataValue and set sVertex as its own parent
		g.setData(sVertex, 0);
		g.setParent(sVertex, sVertex);
	
		// insert starting vertex into the priority queue
		minPathPQ.push(vertexData);
	
		// record the number of vertices in numVertices
		numVertices = g.numberOfVertices();
		// terminate on an empty priority queue or when
		// the number of vertices visited is numVertices
		while (numVisited < numVertices && !minPathPQ.isEmpty())
		{
			// delete a priority queue entry and record its vertex.
			vertexData = minPathPQ.pop();
			currVertex = vertexData.endV;
	
			// if currVertex is BLACK, we have already
			// found the optimal path from sVertex to currVertex
			if (g.getColor(currVertex) != VertexColor.BLACK)
			{
				// mark the vertex so we don't look at it again and
				// increment numVisited;
				g.setColor(currVertex, VertexColor.BLACK);
				numVisited++;
	
				// find all neighbors of the current vertex. for each
				// neighbor that has not been visited, generate a
				// MinInfo object and insert it into the priority
				// queue provided the total weight to get to the
				// neighbor is better than the current dataValue of
				// neighborVertex
				edgeSet = g.getNeighbors(currVertex);
				edgeIter = edgeSet.iterator();
				while (edgeIter.hasNext())
				{
					neighborVertex = edgeIter.next();
	
					if (g.getColor(neighborVertex) ==
						 VertexColor.WHITE)
					{
						newMinWeight = g.getData(currVertex) +
								 g.getWeight(currVertex, neighborVertex);
	
						// if we have found a better path to
						// neighborVertex, create a new MinInfo object
						// for neighborVertex and push it onto the
						// priority queue. update the dataValue and
						// parent of neighborVertex. NOTE: if
						// neighborVertex is WHITE, its
						// data value is INFINITY and a new MinInfo
						// object will enter the priority queue
						if (newMinWeight < g.getData(neighborVertex))
						{
							vertexData = new MinInfo<T>();
							vertexData.endV = neighborVertex;
							vertexData.pathWeight = newMinWeight;
							minPathPQ.push(vertexData);
							g.setData(neighborVertex, newMinWeight);
							g.setParent(neighborVertex, currVertex);
						}
					}
				}
			}
		}
	}

	/**
	 * In a directed acyclic graph, it finds the path with minimum
	 * total weight from sVertex to each vertex reachable from
	 * sVertex. Upon return, the dataValue field of
	 * each vertex in g is either the minimum path weight to the vertex or is
	 * INFINITY if the vertex was not reacheable from sVertex.
	 * Call path(g, sVertex, v) to find the minimum path from sVertex to v.
	 * @param g  directed graph
	 * @param sVertex  starting vertex.
	 */
	public static <T> void dagMinimumPath(DiGraph<T> g, T sVertex)
	{
		LinkedList<T> tlist = new LinkedList<T>();
		Iterator<T> topSortIter, setIter;
		T currVertex, neighborVertex;
		int w, dataValue;

		// perform a topological sort of g
		topologicalSort(g, tlist);

		// set all dataValues to INFINITY
		g.initData();
		// set dataValue and parent for sVertex
		g.setData(sVertex, 0);
		g.setParent(sVertex, sVertex);

		topSortIter = tlist.iterator();
		// sequence through the topological sort and update the
		// distance to each neighbor
		while (topSortIter.hasNext())
		{
			// get the next vertex in the topological sort
			currVertex = topSortIter.next();

			// get the dataValue of currVertex. if it is
			// INFINITY, the vertex is not reachable from sVertex
			if ((dataValue = g.getData(currVertex)) != INFINITY)
			{
				// obtain an iterator for the neighbors of currVertex
				setIter = g.getNeighbors(currVertex).iterator();

				while (setIter.hasNext())
				{
					// get the next neighbor of currVertex
					neighborVertex = setIter.next();

					// reset dataValue and parent if adding the
					// edge (currVertex, neighborVertex) to the
					// path sVertex --> currVertex provides a
					// better path to neighborVertex
					w = dataValue +
							g.getWeight(currVertex, neighborVertex);
					if (w < g.getData(neighborVertex))
					{
						g.setData(neighborVertex, w);
						g.setParent(neighborVertex, currVertex);
					}
				}
			}
		}
	}

	/**
	 * Identifies the edges that combine with vertices to form
	 * a minimum spanning tree for the connected graph g.
	 * Graph g is represented as a digraph with bidirectional edges
	 * of equal weight
	 * @param g  directed graph
	 * @param MST  vertices and edges that form the minimum spanning tree.
	 */
	public static <T> int minSpanTree(DiGraph<T> g,
												 DiGraph<T> MST)
	{
		// priority queue that stores MinInfo objects
		HeapPQueue<MinInfo<T>> minTreePQ =
			new HeapPQueue<MinInfo<T>>(new Less<MinInfo<T>>());
		// used when inserting MinInfo entries
		// into the priority queue or erasing entries
		MinInfo<T> vertexData = null;
		// edgeSet is adjacency set of vertex we are visiting.
		// edgeIter is an iterator that scans the list. vertexSet
		// used to traverse graph vertices
		Set<T> edgeSet, vertexSet;
		Iterator<T> edgeIter;
		T sVertex = null, currVertex = null, neighborVertex = null;
		Iterator<T> graphIter;
		int edgeWeight;
		// size of the minimum spanning tree
		int minSpanTreeSize = 0;
		// current minimum total weight for spanning tree
		int minSpanTreeWeight = 0;

		if (g.isEmpty())
			return -1;

		// clear MST
		MST.clear();

		// color the vertices WHITE and set each vertex
		// dataValue to INFINITY
		g.colorWhite();
		g.initData();

		vertexSet = g.vertexSet();
		// record first vertex in iteration as starting vertex
		sVertex = vertexSet.iterator().next();

		// create MinInfo object for starting vertex
		vertexData = new MinInfo<T>();
		vertexData.endV = sVertex;

		// total weight of spanning tree with only the
		// starting vertex is 0
		vertexData.pathWeight = 0;

		// initially the tree has 1 vertex and no edges. set
		// sVertex as its own parent
		g.setData(sVertex, 0);
		g.setParent(sVertex, sVertex);

		// insert starting vertex into the priority queue
		minTreePQ.push(vertexData);

		// add vertices until we span the entire graph
		for (;;)
		{
			if (minTreePQ.isEmpty())
				throw new IllegalArgumentException(
					"minSpanTree(): graph is not connected");

			// delete a priority queue entry
			vertexData = minTreePQ.pop();
			currVertex = vertexData.endV;

			// if vertex is not part of the new graph (unvisited)
			// add the weight of the edge to the total tree weight
			// and increment the number of vertices in the tree
			if (g.getColor(currVertex) == VertexColor.WHITE)
			{
				minSpanTreeWeight += vertexData.pathWeight;
				minSpanTreeSize++;

				// if we spanned all vertices, break
				if (minSpanTreeSize == g.numberOfVertices())
					break;

				// mark the vertex BLACK so we don't look
				// at it again.
				g.setColor(currVertex, VertexColor.BLACK);

				// find all unmarked neighbors of the vertex.
				edgeSet = g.getNeighbors(currVertex);
				edgeIter = edgeSet.iterator();
				while (edgeIter.hasNext())
				{
					neighborVertex = edgeIter.next();
					// if neighbor is unmarked, check whether adding
					// the new edge to the tree is better than using
					// the current edge
					if (g.getColor(neighborVertex) == VertexColor.WHITE)
					{
						edgeWeight =
							g.getWeight(currVertex,neighborVertex);
						if (edgeWeight < g.getData(neighborVertex))
						{
							// if new edge is a better connection,
							// create MinInfo object for new vertex.
							// update dataValue and parent variables
							vertexData = new MinInfo<T>();
							vertexData.endV = neighborVertex;
							vertexData.pathWeight = edgeWeight;
							g.setData(neighborVertex, edgeWeight);
							g.setParent(neighborVertex, currVertex);
							minTreePQ.push(vertexData);
						}
					}
				}
			}
		}

		// add all of the vertices
		graphIter = vertexSet.iterator();
		while (graphIter.hasNext())
			MST.addVertex(graphIter.next());

		// add the edges to the minimum spanning tree
		graphIter = vertexSet.iterator();

		T parentVertex = null;

		graphIter.next();
		while (graphIter.hasNext())
		{
			currVertex = graphIter.next();
			parentVertex = g.getParent(currVertex);
			edgeWeight = g.getWeight(parentVertex, currVertex);
			MST.addEdge(parentVertex, currVertex, edgeWeight);
			MST.addEdge(currVertex, parentVertex, edgeWeight);
		}

		return minSpanTreeWeight;
	}

	// priority queue data used by minimumPath() and
	// minSpanningTree() algorithms
	private static class MinInfo<T>
		implements Comparable<MinInfo<T>>
	{
		public T endV;
		public int pathWeight;

		public int compareTo(MinInfo<T> item)
		{
			if (pathWeight < item.pathWeight)
				return -1;
			else if (pathWeight == item.pathWeight)
				return 0;
			else
				return 1;
		}
	}
}
