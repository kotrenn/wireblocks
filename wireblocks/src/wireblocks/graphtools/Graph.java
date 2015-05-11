/*******************************************************************************
 * Copyright (c) 2015 Ramona Seay
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/

package wireblocks.graphtools;

import java.util.List;

/**
 * A simple undirected graph. The user of the interface can add and remove
 * vertices and edges at will. Basic information about the graph can also be
 * queried. <p>
 *
 * Vertices in graphs (like Java arrays) are zero based. <p>
 *
 * The <code>Graph</code> interface is only for simple undirected graphs. A
 * simple graph refers to one in which there is at most one edge incident to a
 * pair of vertices. An undirected graph implies that if there is an edge from
 * one vertex to another, then there is also an edge from the second vertex back
 * to the first. Alternatively, each edge is bi-directional. <p>
 *
 * The <code>Graph</code> interface provides four methods for manipulating
 * vertices. Note that these operations may execute in time proportional to the
 * square of the number of vertices for some implementations (the
 * <code>MatrixGraph</code> class, for example). <p>
 *
 * The <code>Graph</code> interface provides three methods for accessing and
 * manipulating edges. Currently the <code>Graph</code> interface specifies that
 * every graph is undirected and simple: edges are bi-directional and appear at
 * most once between a pair of vertices. Note that the execution time of these
 * operations is dependent on the underlying data model. <p>
 *
 * The <code>Graph</code> interface provides two methods for accessing
 * information about the neighbors of a vertex.
 */
public interface Graph
{
	/**
	 * Returns a deep copy of this <code>Graph</code> instance.
	 *
	 * @return a clone of this <code>Graph</code> instance
	 */
	public Graph clone();

	/**
	 * Returns the number of vertices in this graph.
	 *
	 * @return the number of vertices in this graph
	 */
	public int numberOfVertices();

	/**
	 * Returns the number of edges in this graph.
	 *
	 * @return the number of edges in this graph
	 */
	public int numberOfEdges();

	/**
	 * Adds a new vertex to this graph. The new vertex will have the smallest id
	 * possible, which will be the number of vertices in the graph.
	 */
	public void addVertex();

	/**
	 * Adds several vertices to this graph. The new vertices will have the
	 * smallest id's possible, which will start at the number of vertices of the
	 * graph and range to this value minus one.
	 *
	 * @param count number of vertices to add
	 */
	public void addVertices(int count);

	/**
	 * Removes a specified vertex from this graph. All edges incident to this
	 * vertex will also be removed.
	 *
	 * @param v id of the vertex to remove
	 */
	public void removeVertex(int v);

	/**
	 * Removes a list of vertices from this graph. All edges incident to any
	 * removed vertex will also be removed.
	 *
	 * @param vertices array of vertex id's to remove
	 */
	public void removeVertices(int[] vertices);

	/**
	 * Returns <code>true</code> if this graph has an edge incident to the given
	 * vertices.
	 *
	 * @param u first vertex id
	 * @param v second vertex id
	 * @return <code>true</code> if there is an edge incident to u and v
	 */
	public boolean hasEdge(int u, int v);

	/**
	 * Adds an edge incident to the specified vertices. If such an edge already
	 * exists, no changes will occur.
	 *
	 * @param u first vertex id
	 * @param v second vertex id
	 */
	public void addEdge(int u, int v);

	/**
	 * Removes an edge (if any) incident to the specified vertices. If no such
	 * edge exists, no changes will occur.
	 *
	 * @param u first vertex id
	 * @param v second vertex id
	 */
	public void removeEdge(int u, int v);

	/**
	 * Returns the degree of the specified vertex.
	 *
	 * @param v vertex id
	 * @return degree of the specified vertex
	 */
	public int degreeOfVertex(int v);

	/**
	 * Returns a sorted <code>List</code> of all vertices adjacent to the
	 * specified vertex.
	 *
	 * @param v vertex id
	 * @return a sorted list of adjacent vertices
	 */
	public List<Integer> getNeighbors(int v);
}
