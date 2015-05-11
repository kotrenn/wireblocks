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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adjacency list implementation of the <code>Graph</code> interface. Neighbors
 * of a vertex are stored in a <i>sorted</i> list.<p>
 *
 * All of the operations perform as could be expected for an adjacency list. <p>
 *
 * The <code>numberOfVertices</code>, <code>numberOfEdges</code>,
 * <code>addVertex</code>, <code>addVertices</code>, <code>removeVertex</code>,
 * <code>removeVertices</code>, and <code>degreeOfVertex</code> operations run
 * in constant time. The <code>addEdge</code>, <code>removeEdge</code>, and
 * <code>hasEdge</code> operations run in time logarithmic to the degrees of the
 * vertices involved. The <code>getNeighbors</code> operation runs in time
 * linear to the degree of the vertex. <p>
 *
 * The <code>clone</code> operation will provide a deep copy of this
 * <code>Graph</code> instance.
 *
 */
public class AdjacencyGraph implements Graph
{
	private int                      m_numVertices;
	private int                      m_numEdges;
	private ArrayList<List<Integer>> m_adjacencyList;
	
	public AdjacencyGraph(int numVertices)
	{
		m_numVertices = numVertices;
		m_numEdges = 0;
		m_adjacencyList = buildEmptyAdjacencyList(numVertices);
		buildEmptyAdjacencyList(numVertices);
	}

	private List<Integer> newList()
	{
		return new ArrayList<Integer>();
	}

	@Override
	public String toString()
	{
		String ret = "";
		for (int v = 0; v < m_numVertices; ++v)
		{
			ret += v + ": ";
			
			List<Integer> neighbors = getNeighbors(v);
			boolean first = true;
			for (Integer neighbor : neighbors)
			{
				if (!first) ret += ", ";
				ret += neighbor;
				first = false;
			}
			
			ret += "\n";
		}
		return ret;
	}

	@Override
	public Graph clone()
	{
		AdjacencyGraph newGraph = new AdjacencyGraph(m_numVertices);
		newGraph.copyAdjacencyList(m_adjacencyList, newGraph.m_adjacencyList);
		return newGraph;
	}

	@Override
	public int numberOfVertices()
	{
		return m_numVertices;
	}

	@Override
	public int numberOfEdges()
	{
		return m_numEdges;
	}

	@Override
	public void addVertex()
	{
		addVertices(1);
	}

	@Override
	public void addVertices(int count)
	{
		int newVertexCount = m_numVertices + count;
		ArrayList<List<Integer>> newAdjacencyList = buildEmptyAdjacencyList(newVertexCount);
		copyAdjacencyList(m_adjacencyList, newAdjacencyList, m_numVertices);
		m_adjacencyList = newAdjacencyList;
		m_numVertices = newVertexCount;
	}

	@Override
	public void removeVertex(int v)
	{
		int newVertexCount = m_numVertices - 1;
		
		List<Integer> rowToRemove = m_adjacencyList.get(v);
		for (Integer u : rowToRemove)
		{
			List<Integer> neighborRow = m_adjacencyList.get(u);
			int index = Collections.binarySearch(neighborRow, v);
			
			/*
			 * Collections.binarySearch() is guaranteed to return a negative
			 * index if and only if the desired value is not found in the list.
			 */
			if (index < 0) continue;
			neighborRow.remove(index);
		}
		m_adjacencyList.remove(v);

		m_numVertices = newVertexCount;
		m_numEdges -= rowToRemove.size();

		/* Now decrement later vertices */
		for (int x = v; x < m_adjacencyList.size(); ++x)
		{
			List<Integer> curList = m_adjacencyList.get(x);
			List<Integer> streamList = curList.stream().map(y -> y - 1)
					.collect(Collectors.toCollection(ArrayList::new));
			m_adjacencyList.set(x, streamList);
		}
	}

	@Override
	public void removeVertices(int[] vertices)
	{
		int oldVertexCount = m_numVertices;
		int newVertexCount = m_numVertices - vertices.length;
		ArrayList<List<Integer>> newAdjacencyList = buildEmptyAdjacencyList(newVertexCount);

		int[] newVertices = new int[newVertexCount];
		/*
		 * Create an array newVertices which contains all vertices of the
		 * original vertex set *excluding* the vertices in the vertices array.
		 * Uses the following indices: i = index of current vertex [0 ..
		 * m_numVertices - 1] j = index within array vertices [0 ..
		 * vertices.length - 1] k = index within newVertices [0 .. m_numVertices
		 * - vertices.length - 1]
		 */
		for (int i = 0, j = 0, k = 0; i < m_numVertices; ++i)
			if (j < vertices.length && i == vertices[j])
				++j;
			else
				newVertices[k++] = i;
		
		/* Copy stuff over */
		copyAdjacencyList(m_adjacencyList, newAdjacencyList, newVertices);
		m_adjacencyList = newAdjacencyList;
		m_numVertices = newVertexCount;
		computeEdgeCount();

		/* Compute vertex id mappings */
		int[] vertexMapping = new int[oldVertexCount];
		for (int i = 0, j = 0; i < oldVertexCount; ++i)
		{
			if (j < vertices.length && i == vertices[j]) ++j;
			vertexMapping[i] = i - j;
		}
		
		/* Now decrement later vertices */
		for (int v = 0; v < m_numVertices; ++v)
		{
			List<Integer> curList = m_adjacencyList.get(v);
			List<Integer> streamList = curList.stream()
					.filter(x -> Arrays.binarySearch(vertices, x) < 0)
					.map(x -> vertexMapping[x])
					.collect(Collectors.toCollection(ArrayList::new));
			m_adjacencyList.set(v, streamList);
		}
	}
	
	@Override
	public boolean hasEdge(int u, int v)
	{
		List<Integer> neighbors = m_adjacencyList.get(u);
		int index = Collections.binarySearch(neighbors, v);
		/*
		 * Collections.binarySearch() is guaranteed to return a negative index
		 * if and only if the desired value is not found in the list.
		 */
		return index >= 0;
	}

	@Override
	public void addEdge(int u, int v)
	{
		boolean success = false;
		success |= insertEdgeFromTo(u, v);
		success |= insertEdgeFromTo(v, u);
		if (success) m_numEdges += 1;
	}
	
	private boolean insertEdgeFromTo(int u, int v)
	{
		List<Integer> neighbors = m_adjacencyList.get(u);
		int index = Collections.binarySearch(neighbors, v);

		/* Skip if v is already in neighbors */
		if (index >= 0) return false;
		if (index < 0)
			neighbors.add(v);
		else
			neighbors.add(v, index);
		return true;
	}

	@Override
	public void removeEdge(int u, int v)
	{
		boolean success = false;
		success |= deleteEdgeFromTo(u, v);
		success |= deleteEdgeFromTo(v, u);
		if (success) m_numEdges -= 1;
	}
	
	private boolean deleteEdgeFromTo(int u, int v)
	{
		List<Integer> neighbors = m_adjacencyList.get(u);
		int index = Collections.binarySearch(neighbors, v);
		
		/* Skip if v is not in neighbors */
		if (index < 0) return false;
		neighbors.remove(index);

		return true;
	}

	@Override
	public int degreeOfVertex(int v)
	{
		List<Integer> neighbors = m_adjacencyList.get(v);
		return neighbors.size();
	}

	@Override
	public List<Integer> getNeighbors(int v)
	{
		List<Integer> neighbors = m_adjacencyList.get(v);
		List<Integer> ret = new LinkedList<Integer>(neighbors);
		return ret;
	}
	
	private ArrayList<List<Integer>> buildEmptyAdjacencyList(int size)
	{
		ArrayList<List<Integer>> ret = new ArrayList<List<Integer>>(size);
		for (int v = 0; v < size; ++v)
			ret.add(newList());
		return ret;
	}
	
	private void copyAdjacencyList(ArrayList<List<Integer>> source,
	                               ArrayList<List<Integer>> destination)
	{
		copyAdjacencyList(source, destination, source.size());
	}
	
	private void copyAdjacencyList(ArrayList<List<Integer>> source,
	                               ArrayList<List<Integer>> destination,
	                               int size)
	{
		for (int v = 0; v < size; ++v)
			destination.get(v).addAll(source.get(v));
	}
	
	private void copyAdjacencyList(ArrayList<List<Integer>> source,
	                               ArrayList<List<Integer>> destination,
	                               int[] vertices)
	{
		for (int k = 0; k < vertices.length; ++k)
		{
			List<Integer> sourceRow = source.get(vertices[k]);
			List<Integer> destinationRow = destination.get(k);
			destinationRow.clear();

			/*
			 * Set destinationRow to be the set intersection of sourceRow and
			 * vertices. We know that both collections are sorted.
			 */
			int m = sourceRow.size();
			int n = vertices.length;
			int i = 0, j = 0;
			while (i < m && j < n)
			{
				int a = sourceRow.get(i);
				int b = vertices[j];
				if (a > b)
					j++;
				else if (b > a)
					i++;
				else
				{
					destinationRow.add(a);
					i++;
					j++;
				}
			}
		}
	}

	private void computeEdgeCount()
	{
		int count = 0;
		for (int u = 0; u < m_numVertices; ++u)
		{
			List<Integer> row = m_adjacencyList.get(u);
			for (int v = 0; v < row.size(); ++v)
				if (row.get(v) >= u) count++;
		}
		m_numEdges = count;
	}
}
