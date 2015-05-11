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

package wireblocks.dlx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SetCoverInstance
{
	Map<String, Integer> m_rowLabels;
	Map<String, Integer> m_colLabels;
	List<LinkHeader>     m_columnHeaders;
	LinkHeader           m_root;
	
	public SetCoverInstance()
	{
		/* Initialize row and column labelings */
		m_rowLabels = new HashMap<String, Integer>();
		m_colLabels = new HashMap<String, Integer>();
		
		/* Initialize a global array of header references */
		m_columnHeaders = new ArrayList<LinkHeader>();
		
		/* Initialize our root node */
		m_root = new LinkHeader("");
		m_root.setLeft(m_root);
		m_root.setRight(m_root);
	}
	
	public String solutionToString(List<LinkNode> solution)
	{
		String ret = "[";
		boolean prev = false;
		for (LinkNode node : solution)
		{
			if (prev) ret += ", ";
			ret += rowToString(node);
			prev = true;
		}
		ret += "]";
		return ret;
	}
	
	private String rowToString(LinkNode node)
	{
		String ret = "[" + node.getColumn().getName();
		for (LinkNode cur = node.getRight(); cur != node; cur = cur.getRight())
		{
			ret += ", ";
			ret += cur.getColumn().getName();
		}
		ret += "]";
		return ret;
	}

	public List<String> solutionToLabels(List<LinkNode> solution)
	{
		List<String> ret = new LinkedList<String>();
		System.out.println("solution.size() = " + solution.size());
		for (LinkNode node : solution)
			ret.add(node.getRowLabel());
		return ret;
	}
	
	public void addColumnLabel(String label)
	{
		addColumnLabel(label, false);
	}
	
	public void addColumnLabel(String label, boolean optional)
	{
		/* Add the label */
		m_colLabels.put(label, m_colLabels.size());

		/* Create the new column header object */
		LinkHeader newColumn = new LinkHeader(label);
		
		/* Find the last column header in line */
		LinkHeader end = (LinkHeader)m_root.getLeft();
		
		/* Add it to our global list to keep track of */
		m_columnHeaders.add(newColumn);

		/* Set up dummy links in newColumn */
		newColumn.setUp(newColumn);
		newColumn.setDown(newColumn);

		/*
		 * If this column is optional, then just connect it to itself. Don't
		 * include it in the list of directly reachable header nodes from the
		 * root.
		 */
		if (optional)
		{
			newColumn.setLeft(newColumn);
			newColumn.setRight(newColumn);
			return;
		}

		/* Connect newColumn to the row of headers */
		newColumn.setLeft(end);
		newColumn.setRight(m_root);

		/* Make existing objects point to newColumn */
		end.setRight(newColumn);
		m_root.setLeft(newColumn);
	}

	public void addRow(String[] colLabels)
	{
		/* Add a row with an empty label */
		addRow("", colLabels);
	}

	public void addRow(String rowLabel, String[] colLabels)
	{
		/* Check that we have this row */
		if (m_rowLabels.containsKey(rowLabel) || rowLabel.length() == 0)
			rowLabel = "row_" + m_rowLabels.size();
		
		/* Check that we're not trying too add *too* many columns */
		if (colLabels.length > m_colLabels.size()) return;
		
		/* Now add the new row label */
		m_rowLabels.put(rowLabel, m_rowLabels.size());
		
		/* Now add in the cells */
		LinkNode prev = null;
		LinkNode first = null;
		for (String columnName : colLabels)
		{
			/* Create the new link node object */
			LinkNode newNode = new LinkNode();

			/*
			 * In case we are on the first node of the row, initialize these
			 * variables.
			 */
			if (prev == null) prev = newNode;
			if (first == null) first = newNode;

			/* Find the matching column header */
			int index = m_colLabels.get(columnName);
			LinkHeader header = getHeader(index);
			System.out.println("Found that column " + columnName
			                   + " with header label " + header.getName()
			                   + " has index " + index);

			/* Get the last node in the column */
			LinkNode lastInCol = header.getUp();

			/* Set up the links of newNode */
			newNode.setLeft(prev);
			newNode.setRight(first);
			newNode.setUp(lastInCol);
			newNode.setDown(header);

			/* Make existing objects point to newNode */
			prev.setRight(newNode);
			first.setLeft(newNode);
			lastInCol.setDown(newNode);
			header.setUp(newNode);
			
			/* Update the count */
			header.incrementSize();

			/* Initialize other local variables */
			newNode.setColumn(header);
			newNode.setRowLabel(rowLabel);
			
			/* Set up prev for the next round */
			prev = newNode;
		}
	}
	
	private LinkHeader getHeader(int index)
	{
		return m_columnHeaders.get(index);
	}
	
	public int getNumberOfRows()
	{
		return m_rowLabels.size();
	}
	
	public int getNumberOfColumns()
	{
		return m_colLabels.size();
	}

	public LinkHeader getRoot()
	{
		return m_root;
	}
}
