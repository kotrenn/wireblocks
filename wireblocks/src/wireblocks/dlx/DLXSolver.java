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

import java.util.List;
import java.util.Stack;

public class DLXSolver
{
	private SetCoverInstance m_instance;
	boolean                  m_solved;

	public DLXSolver(SetCoverInstance instance)
	{
		m_instance = instance;
		m_solved = false;
	}

	public List<LinkNode> solve()
	{
		/* Reset our state variable */
		m_solved = false;
		
		/* Initialize a list to store the solution in */
		Stack<LinkNode> solution = new Stack<LinkNode>();
		
		/* Now perform the actual search */
		search(0, solution);
		
		/* Return the final result (if any) */
		return solution;
	}

	private void search(int k, Stack<LinkNode> solution)
	{
		/* Get the root node */
		LinkHeader root = m_instance.getRoot();
		
		/* Check if there are any nodes left to look at */
		if (root.getRight() == root)
		{
			m_solved = true;
			return;
		}

		/* Find a column to select */
		LinkHeader column = chooseColumn();

		/* Cover the column */
		cover(column);
		
		/* Iterate over all nodes below column */
		for (LinkNode row = column.getDown(); row != column; row = row
				.getDown())
		{
			/* O_k <- r */
			solution.push(row);
			
			/*
			 * Iterate over all nodes in the same row as row and cover their
			 * columns.
			 */
			for (LinkNode j = row.getRight(); j != row; j = j.getRight())
				cover(j.getColumn());
			
			/* Recursive descent */
			search(k + 1, solution);
			
			/* Remove row from the solution */
			if (!m_solved) solution.pop();

			/* c <- C[r] */
			column = row.getColumn();
			
			/*
			 * Iterate over all nodes in the same row as row and uncover their
			 * columns. We do this in the reverse order from the covering loop
			 * to undo every cover operation in the reverse order of when they
			 * were covered.
			 */
			for (LinkNode j = row.getLeft(); j != row; j = j.getLeft())
				uncover(j.getColumn());

			/* Short-circuit exit if we've already found a solution */
			if (m_solved) break;
		}
		
		/* Uncover the column */
		uncover(column);
	}
	
	/* Finds the first column with a minimum number of nodes in it. */
	private LinkHeader chooseColumn()
	{
		/* Initialize our return value */
		LinkHeader ret = null;
		
		/*
		 * We want to find the largest number of columns. Knuth's paper suggests
		 * initializing curSize to infinity so that all header sizes are less
		 * than the initial value. We can "simulate" this by observing that no
		 * column can have a size greater than the number of rows. So if we just
		 * take the next largest number, we will be fine.
		 */
		int curSize = m_instance.getNumberOfRows() + 1;
		System.out.println("Our infinity today is " + curSize);
		
		/* Now iterate over the header nodes */
		LinkHeader root = m_instance.getRoot();
		for (LinkNode j = root.getRight(); j != root; j = j.getRight())
		{
			/*
			 * We can guarantee header is of subclass type LinkHeader since we
			 * are iterating through all of the column headers (which are
			 * LinkHeader objects).
			 */
			LinkHeader header = (LinkHeader)j;
			System.out.println("Checking header " + header.getName()
			                   + " with size " + header.getSize());
			if (header.getSize() < curSize)
			{
				/* Update the current minimum size and header */
				ret = header;
				curSize = header.getSize();
			}
		}

		return ret;
	}
	
	private void cover(LinkHeader column)
	{
		/* L[R[c]] <- L[c] */
		column.getRight().setLeft(column.getLeft());

		/* R[L[c]] <- R[c] */
		column.getLeft().setRight(column.getRight());

		/* Iterate over every node in the same column as column */
		for (LinkNode i = column.getDown(); i != column; i = i.getDown())
			/* Iterate over every node in the same row as i */
			for (LinkNode j = i.getRight(); j != i; j = j.getRight())
			{
				/* U[D[j]] <- U[j] */
				j.getDown().setUp(j.getUp());

				/* D[U[j]] <- D[j] */
				j.getUp().setDown(j.getDown());

				/* S[C[j]] <- S[C[j]] - 1 */
				j.getColumn().decrementSize();
			}
	}
	
	private void uncover(LinkHeader column)
	{
		/* Iterate over every node in the same column as column */
		for (LinkNode i = column.getUp(); i != column; i = i.getUp())
			/* Iterate over every node in the same row as i */
			for (LinkNode j = i.getLeft(); j != i; j = j.getLeft())
			{
				/* S[C[j]] <- S[C[j]] + 1 */
				j.getColumn().incrementSize();
				
				/* U[D[j]] <- j */
				j.getDown().setUp(j);
				
				/* D[U[j]] <- j */
				j.getUp().setDown(j);
			}
		
		/* L[R[c]] <- c */
		column.getRight().setLeft(column);

		/* R[L[c]] <- c */
		column.getLeft().setRight(column);
	}
}
