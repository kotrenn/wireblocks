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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <code>Graph</code> wrapper which allows vertices to be labeled by
 * {@link String}s. <code>GraphLabel</code> instances are designed to run
 * independent of the underlying <code>Graph</code> implementation. A
 * <code>LabeledGraph</code> is bound to a specific <code>Graph</code> instance
 * upon creation. Each vertex is mapped to a unique label. Operations which take
 * vertices as parameters can also be indexed by their labels rather than vertex
 * numbers. <p>
 *
 * Operations should not be performed on the underlying <code>Graph</code>
 * instance. Instead, operations should be performed on this wrapping
 * <code>LabeledGraph</code> instance so that label mappings can be updated
 * properly. No guarantee is made on the consistency of label mappings if
 * operations are performed on the underlying <code>Graph</code> instance
 * directly. <p>
 *
 * When a LabeledGraph instance is initialized, all labels are set to the result
 * of the <code>setLabelsToNumbers</code>operation. <p>
 *
 * Any operations which add a new vertex but do not take a label as a parameter
 * will assign numeric labels by default. The numbers are specified according to
 * the <code>setLabelsToNumbers</code> operation. <p>
 *
 * The operations <code>addVertex</code>, <code>addVertices</code>,
 * <code>removeVertex</code>, and <code>removeVertices</code> incur an
 * additional worst-case linear time cost to update label mappings. <p>
 *
 * The <code>getNeighborLabels</code> operation functions similarly to the
 * <code>getNeighbors</code> operation, but returns a <code>List</code> of the
 * labels of the neighboring vertices. This operation will run in time
 * proportional to performing the <code>getNeighbors</code> operation of the
 * underlying <code>Graph</code> instance. <p>
 *
 * The <code>clone</code> operation will provide a deep copy of this
 * <code>Graph</code> instance. In particular, the underlying <code>Graph</code>
 * instance will also have a deep copy performed.
 *
 * The <code>setLabel</code> and <code>setLabels</code> operations are used to
 * update the labels of vertices. These operations run in time proportional to
 * the number of vertices being updated. <p>
 *
 * The <code>setLabelsToNumbers</code> and <code>setLabelsToLetters</code> will
 * update label mappings to standard variants. The
 * <code>setLabelsToNumbers</code> operation sets all labels to a
 * <code>String</code> of the vertex id number. The
 * <code>setLabelsToLetters</code> operation assigns a unique capital letter of
 * the English to each vertex, up through the first 26 vertices. Beyond this,
 * integer mappings are used as in the <code>setLabelsToNumbers</code>
 * operation. <p>
 *
 * The <code>intToLabel</code> and <code>labelToInt</code> operations are used
 * to switch between the numerical id and <code>String</code> labeling of a
 * vertex.
 *
 */
public class LabeledGraph implements Graph
{
	private Graph                m_graph;
	private List<String>         m_vertexLabels;
	private Map<String, Integer> m_vertexLabelMap;

	public LabeledGraph(Graph graph)
	{
		int numVertices = graph.numberOfVertices();

		m_graph = graph;
		m_vertexLabels = new ArrayList<String>(numVertices);
		m_vertexLabelMap = new HashMap<String, Integer>();
		
		setLabelsToNumbers();
	}
	
	@Override
	public String toString()
	{
		String ret = "";
		for (int v = 0; v < numberOfVertices(); ++v)
		{
			String label = intToLabel(v);
			ret += label + ": ";

			List<String> neighbors = getNeighborLabels(v);
			boolean first = true;
			for (String neighbor : neighbors)
			{
				if (!first) ret += ", ";
				ret += neighbor;
				first = false;
			}

			ret += "\n";
		}
		ret += "m_vertexLabels = " + m_vertexLabels + "\n";
		ret += "m_vertexLabelMap = " + m_vertexLabelMap + "\n";
		return ret;
	}
	
	@Override
	public Graph clone()
	{
		Graph newGraph = m_graph.clone();
		LabeledGraph newLabeledGraph = new LabeledGraph(newGraph);
		
		newLabeledGraph.m_vertexLabels.clear();
		for (String label : m_vertexLabels)
			newLabeledGraph.m_vertexLabels.add(label);
		
		newLabeledGraph.m_vertexLabelMap.clear();
		Set<String> keys = m_vertexLabelMap.keySet();
		for (String key : keys)
		{
			Integer value = m_vertexLabelMap.get(key);
			newLabeledGraph.m_vertexLabelMap.put(key, value);
		}

		return newLabeledGraph;
	}
	
	/**
	 * Adds a new vertex with a given label.
	 *
	 * @param label label of the new vertex
	 */
	public void addLabel(String label)
	{
		int v = m_vertexLabels.size();
		m_vertexLabels.add("");
		setLabel(v, label);
	}
	
	/**
	 * Adds new vertices with provided labels.
	 *
	 * @param labels array of labels to assign to the new vertices
	 */
	public void addLabels(String[] labels)
	{
		for (String label : labels)
			addLabel(label);
	}
	
	/**
	 * Sets the labels of all vertices. If there are less labels than vertices,
	 * then only the first n vertices will have labels updated, where n is the
	 * length of the given array.
	 *
	 * @param labels array of labels to assign to verticews
	 */
	public void setLabels(String[] labels)
	{
		for (int i = 0; i < labels.length; ++i)
			setLabel(i, labels[i]);
	}

	/**
	 * Set the label of a specific vertex.
	 *
	 * @param v id of the vertex to update
	 * @param label new label for the vertex
	 */
	public void setLabel(int v, String label)
	{
		while (v >= m_vertexLabels.size())
			addLabel("");
		m_vertexLabels.set(v, label);
		m_vertexLabelMap.put(label, v);
	}

	/**
	 * Sets the labels of the graph to numbers. Each vertex will be assigned a
	 * label that is a {@link String} of the vertex id.
	 *
	 */
	public void setLabelsToNumbers()
	{
		for (int v = 0; v < numberOfEdges(); ++v)
		{
			String label = String.valueOf(v);
			if (m_vertexLabels.size() < v)
				addLabel(label);
			else
				setLabel(v, String.valueOf(v));
		}
	}

	// TODO: Extend to include multiple letter names: A, B, ..., X, Y, Z, AA,
	// AB, ..., AZ, BA, BB, BC, ...
	/**
	 * Sets the labels of the graph to numbers. Each vertex will be assigned a
	 * capital letter of the English language based on the position in the
	 * alphabet. For example, vertex 0 will be assigned "A" and vertex 4 will be
	 * assigned "D". If there are more than 26 letters, then the remaining
	 * vertices will be labeled in accordance to the
	 * <code>setLabelsToNumbers</code> operation.
	 *
	 */
	public void setLabelsToLetters()
	{
		for (int v = 0; v < numberOfVertices() && v < 26; ++v)
			setLabel(v, String.valueOf((char)('A' + v)));
	}

	/**
	 * Returns the vertex id for the vertex with the given label.
	 *
	 * @param label vertex label to look up
	 * @return integer id of the vertex with the given label
	 */
	public int labelToInt(String label)
	{
		return m_vertexLabelMap.get(label);
	}

	/**
	 * Returns the label for the given vertex id.
	 *
	 * @param vertex vertex id to look up
	 * @return label of the vertex with the given id
	 */
	public String intToLabel(int vertex)
	{
		return m_vertexLabels.get(vertex);
	}

	@Override
	public int numberOfVertices()
	{
		return m_graph.numberOfVertices();
	}

	@Override
	public int numberOfEdges()
	{
		return m_graph.numberOfEdges();
	}

	@Override
	public void addVertex()
	{
		m_graph.addVertex();
	}

	/**
	 * Adds a new vertex to this graph with the given label. The new vertex will
	 * have the smallest id possible, which will be the number of vertices in
	 * the graph.
	 *
	 * @param label label of the new vertex
	 */
	public void addVertex(String label)
	{
		addVertex();
		setLabel(m_graph.numberOfVertices() - 1, label);
	}

	@Override
	public void addVertices(int count)
	{
		m_graph.addVertices(count);
	}

	/**
	 * Adds several vertices to this graph with labels. The new vertices will
	 * have the smallest id's possible, which will start at the number of
	 * vertices of the graph and range to this value minus one.
	 *
	 * @param labels array of labels to assign to the new vertices
	 */
	public void addVertices(String[] labels)
	{
		int numVertices = numberOfVertices();
		int newVertices = labels.length;
		addVertices(newVertices);
		for (int i = 0; i < labels.length; ++i)
			setLabel(numVertices + i + 1, labels[i]);
	}

	private void removeVertexLabel(int v)
	{
		String label = m_vertexLabels.get(v);
		m_vertexLabels.remove(v);
		m_vertexLabelMap.remove(label);
		for (int i = v; i < m_vertexLabels.size(); ++i)
		{
			String curLabel = m_vertexLabels.get(i);
			int oldIndex = m_vertexLabelMap.get(curLabel);
			int newIndex = oldIndex - 1;
			m_vertexLabelMap.put(curLabel, newIndex);
			System.out.println("Updated index of label " + curLabel + " from "
					+ oldIndex + " to " + newIndex);
		}
		System.out.println("Removed vertex " + label + " which was vertex id "
		                   + v);
	}

	@Override
	public void removeVertex(int v)
	{
		m_graph.removeVertex(v);
		removeVertexLabel(v);
	}

	/**
	 * Removes a specified vertex from this graph. All edges incident to this
	 * vertex will also be removed.
	 *
	 * @param label label of the vertex to remove
	 */
	public void removeVertex(String label)
	{
		int v = labelToInt(label);
		removeVertex(v);
	}

	@Override
	public void removeVertices(int[] vertices)
	{
		m_graph.removeVertices(vertices);
		for (int i = 0; i < vertices.length; ++i)
			removeVertexLabel(vertices[i] - i);
		// for (int v : vertices)
		// removeVertexLabel(v);
	}

	/**
	 * Removes a list of vertices from this graph. All edges incident to any
	 * removed vertex will also be removed.
	 *
	 * @param vertexLabels array of vertex labels to remove
	 */
	public void removeVertices(String[] vertexLabels)
	{
		int verticesToRemove = vertexLabels.length;
		int[] vertices = new int[verticesToRemove];
		for (int i = 0; i < vertexLabels.length; ++i)
			vertices[i] = labelToInt(vertexLabels[i]);
		removeVertices(vertices);
	}

	@Override
	public boolean hasEdge(int u, int v)
	{
		return m_graph.hasEdge(u, v);
	}

	/**
	 * Returns <code>true</code> if this graph has an edge incident to the given
	 * vertices.
	 *
	 * @param u first vertex label
	 * @param v second vertex label
	 * @return <code>true</code> if there is an edge from u to v
	 */
	public boolean hasEdge(String u, String v)
	{
		int x = labelToInt(u);
		int y = labelToInt(v);
		return hasEdge(x, y);
	}

	@Override
	public void addEdge(int u, int v)
	{
		m_graph.addEdge(u, v);
	}

	/**
	 * Adds an edge incident to the specified vertices. If such an edge already
	 * exists, no changes will occur.
	 *
	 * @param u first vertex label
	 * @param v second vertex label
	 */
	public void addEdge(String u, String v)
	{
		int x = labelToInt(u);
		int y = labelToInt(v);
		addEdge(x, y);
	}

	@Override
	public void removeEdge(int u, int v)
	{
		m_graph.removeEdge(u, v);
	}

	/**
	 * Removes an edge (if any) incident to the specified vertices. If no such
	 * edge exists, no changes will occur.
	 *
	 * @param u first vertex label
	 * @param v second vertex label
	 */
	public void removeEdge(String u, String v)
	{
		int x = labelToInt(u);
		int y = labelToInt(v);
		removeEdge(x, y);
	}

	@Override
	public int degreeOfVertex(int v)
	{
		return m_graph.degreeOfVertex(v);
	}

	/**
	 * Returns the degree of the specified vertex.
	 *
	 * @param v vertex label
	 * @return degree of the specified vertex
	 */
	public int degreeOfVertex(String v)
	{
		int x = labelToInt(v);
		return degreeOfVertex(x);
	}

	@Override
	public List<Integer> getNeighbors(int v)
	{
		return m_graph.getNeighbors(v);
	}

	/**
	 * Returns a sorted <code>List</code> of all vertices adjacent to the
	 * specified vertex.
	 *
	 * @param v vertex label
	 * @return a sorted list of adjacent vertex id's
	 */
	public List<Integer> getNeighbors(String v)
	{
		int x = labelToInt(v);
		return getNeighbors(x);
	}

	/**
	 * Returns a sorted <code>List</code> of labels of all vertices adjacent to
	 * the specified vertex.
	 *
	 * @param v vertex id
	 * @return a sorted list of adjacent vertex labels
	 */
	public List<String> getNeighborLabels(int v)
	{
		List<Integer> neighbors = getNeighbors(v);
		List<String> ret = new LinkedList<String>();
		for (int u : neighbors)
			ret.add(intToLabel(u));
		return ret;
	}

	/**
	 * Returns a sorted <code>List</code> of labels of all vertices adjacent to
	 * the specified vertex.
	 *
	 * @param v vertex label
	 * @return a sorted list of adjacent vertex labels
	 */
	public List<String> getNeighborLabels(String v)
	{
		int x = labelToInt(v);
		return getNeighborLabels(x);
	}
}
