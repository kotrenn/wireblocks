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

package wireblocks;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

public class PathFinder
{
	/*
	 * Tests if there is a connected path from any cell in src to any cell in
	 * dst. Returns a vector of all cell locations contained in any path, or an
	 * empty array if no such path exists.
	 */
	public Vector2i[] findConnectedCells(Grid grid, Vector2i[] src,
	                                     Direction srcWire, Vector2i[] dst,
	                                     Direction dstWire)
	{
		Set<Vector2i> ret = new TreeSet<Vector2i>();
		Set<Vector2i> visited = new TreeSet<Vector2i>();

		for (Vector2i srcLoc : src)
		{
			/* Make sure it's connected to the wall */
			Block srcBlock = grid.getBlock(srcLoc);
			if (srcBlock == null) continue;
			if (!srcBlock.hasWire(srcWire)) continue;

			/* Find all cells reachable from src */
			Set<Vector2i> reachable = findReachableFrom(grid, srcLoc, visited);
			
			/* Check if any of the cells in dst were reached */
			boolean foundGoal = false;
			for (Vector2i dstLoc : dst)
			{
				if (!reachable.contains(dstLoc)) continue;
				Block dstBlock = grid.getBlock(dstLoc);
				if (dstBlock == null) continue;
				if (!dstBlock.hasWire(dstWire)) continue;
				foundGoal = true;
			}
			
			/* If none were found, move on to the next source cell */
			if (!foundGoal) continue;
			
			/* Add all of the found cells */
			ret.addAll(reachable);
		}

		return ret.toArray(new Vector2i[ret.size()]);
	}

	/* Find the set of all reachable cells from the cell src */
	private Set<Vector2i> findReachableFrom(Grid grid, Vector2i src,
	                                        Set<Vector2i> visited)
	{
		Set<Vector2i> ret = new TreeSet<Vector2i>();

		/* Set up the queue for the breadth-first search */
		LinkedList<Vector2i> queue = new LinkedList<Vector2i>();
		queue.add(src);
		
		/* Perform the search */
		while (!queue.isEmpty())
		{
			/* Pop off the head of the list */
			Vector2i curLoc = queue.remove();
			
			/* Avoid repeat visits */
			if (visited.contains(curLoc)) continue;

			/*
			 * Add to global visited list and the set of reachable from this
			 * source
			 */
			visited.add(curLoc);
			ret.add(curLoc);
			
			/* Make sure there is actually a block here */
			Block curBlock = grid.getBlock(curLoc);
			if (curBlock == null) continue;

			/* Now visit all connected neighbors */
			for (Direction direction : Direction.toArray())
			{
				/* Make sure the block has a wire in this direction first */
				if (!curBlock.hasWire(direction)) continue;

				/* Compute the location of the neighboring block */
				Vector2i vel = direction.toVector();
				Vector2i newLoc = curLoc.add(vel);

				/* Make sure we're still in bounds */
				if (!grid.isInBounds(newLoc)) continue;

				/* Check there is actually a block in the neighboring cell */
				Block newBlock = grid.getBlock(newLoc);
				if (newBlock == null) continue;

				/*
				 * Make sure the other block also has a wire coming in from that
				 * direction
				 */
				Direction reverse = direction.reverse();
				if (!newBlock.hasWire(reverse)) continue;

				/* Add the new location to the queue */
				queue.add(newLoc);
			}
		}

		return ret;
	}
}
