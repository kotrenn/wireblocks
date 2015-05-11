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

import java.util.LinkedList;
import java.util.List;

/**
 * Adjacency matrix implementation of the <code>Graph</code> interface. <p>
 *
 * All of the operations perform as could be expected for an adjacency matrix.
 * <p>
 *
 * The <code>numberOfVertices</code>, <code>numberOfEdges</code>,
 * <code>addEdge</code>, <code>removeEdge</code>, and <code>hasEdge</code>
 * operations run in constant time. The <code>degreeOfVertex</code> and
 * <code>getNeighbors</code> operations run in time linear to the number of
 * vertices. The <code>addVertex</code>, <code>addVertices</code>,
 * <code>removeVertex</code>, and <code>removeVertices</code> operations run in
 * time quadratic to the number of vertices. <p>
 *
 * In general, any operation which requires updating the size of the adjacency
 * matrix will require building a new matrix of the appropriate size and copying
 * relevant entries. <p>
 *
 * The <code>clone</code> operation will provide a deep copy of this
 * <code>Graph</code> instance.
 *
 */
public class MatrixGraph implements Graph
{
	private int         m_numVertices;
	private int         m_numEdges;
	private boolean[][] m_matrix;
	
	public MatrixGraph(int numVertices)
	{
		m_numVertices = numVertices;
		m_numEdges = 0;
		m_matrix = buildEmptyMatrix(numVertices);
	}

	@Override
	public String toString()
	{
		String ret = "";
		for (int u = 0; u < m_numVertices; ++u)
		{
			ret += "[";
			for (int v = 0; v < m_numVertices; ++v)
			{
				if (v > 0) ret += ", ";
				ret += hasEdge(u, v)? 1 : 0;
			}
			ret += "]\n";
		}
		return ret;
	}
	
	@Override
	public Graph clone()
	{
		MatrixGraph newGraph = new MatrixGraph(m_numVertices);
		newGraph.copyMatrix(m_matrix, newGraph.m_matrix);
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
		boolean[][] newMatrix = buildEmptyMatrix(newVertexCount);
		copyMatrix(m_matrix, newMatrix, m_numVertices);
		m_matrix = newMatrix;
		m_numVertices = newVertexCount;
	}
	
	@Override
	public void removeVertex(int v)
	{
		int newVertexCount = m_numVertices - 1;
		boolean[][] newMatrix = buildEmptyMatrix(newVertexCount);
		
		int[] newVertices = new int[newVertexCount];
		for (int i = 0, j = 0; i < m_numVertices; ++i)
			if (i != v) newVertices[j++] = i;
		copyMatrix(m_matrix, newMatrix, newVertices);
		m_matrix = newMatrix;
		m_numVertices = newVertexCount;
		computeEdgeCount();
	}
	
	@Override
	public void removeVertices(int[] vertices)
	{
		int newVertexCount = m_numVertices - vertices.length;
		boolean[][] newMatrix = buildEmptyMatrix(newVertexCount);
		
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
		copyMatrix(m_matrix, newMatrix, newVertices);
		m_matrix = newMatrix;
		m_numVertices = newVertexCount;
		computeEdgeCount();
	}
	
	@Override
	public boolean hasEdge(int u, int v)
	{
		return m_matrix[u][v];
	}
	
	@Override
	public void addEdge(int u, int v)
	{
		if (u >= m_numVertices || v >= m_numVertices)
		{
			System.err.println("Error:  At least one vertex does not exist:  "
					+ u + " and " + v);
			return;
		}
		
		m_matrix[u][v] = true;
		m_matrix[v][u] = true;

		computeEdgeCount();
	}
	
	@Override
	public void removeEdge(int u, int v)
	{
		if (u >= m_numVertices || v >= m_numVertices)
		{
			System.err.println("Error:  At least one vertex does not exist:  "
					+ u + " and " + v);
			return;
		}
		
		m_matrix[u][v] = false;
		m_matrix[v][u] = false;

		computeEdgeCount();
	}
	
	@Override
	public int degreeOfVertex(int v)
	{
		int degree = 0;
		for (int u = 0; u < m_numVertices; ++u)
			if (hasEdge(v, u)) degree++;
		return degree;
	}
	
	@Override
	public List<Integer> getNeighbors(int v)
	{
		List<Integer> neighbors = new LinkedList<Integer>();
		for (int u = 0; u < m_numVertices; ++u)
			if (hasEdge(v, u)) neighbors.add(u);
		return neighbors;
	}

	private boolean[][] buildEmptyMatrix(int size)
	{
		boolean[][] ret = new boolean[size][size];
		for (int u = 0; u < size; ++u)
			for (int v = 0; v < size; ++v)
				ret[u][v] = false;
		return ret;
	}
	
	private void copyMatrix(boolean[][] source, boolean[][] destination)
	{
		copyMatrix(source, destination, source.length);
	}
	
	private void copyMatrix(boolean[][] source, boolean[][] destination,
	                        int size)
	{
		for (int u = 0; u < size; ++u)
			for (int v = 0; v < size; ++v)
				destination[u][v] = source[u][v];
	}
	
	private void copyMatrix(boolean[][] source, boolean[][] destination,
	                        int[] vertices)
	{
		for (int i = 0; i < vertices.length; ++i)
			for (int j = 0; j < vertices.length; ++j)
				destination[i][j] = source[vertices[i]][vertices[j]];
	}

	private void computeEdgeCount()
	{
		int count = 0;
		for (int u = 0; u < m_numVertices; ++u)
			for (int v = u; v < m_numVertices; ++v)
				if (hasEdge(u, v)) count++;
		m_numEdges = count;
	}
}
