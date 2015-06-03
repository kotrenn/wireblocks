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

import java.util.ArrayList;

public class Polyomino
{
	private class Cell
	{
		private Vector2i m_loc;
		private Block    m_block;

		public Cell(Vector2i loc, Block block)
		{
			m_loc = loc;
			m_block = block;
		}

		public Vector2i getLoc()
		{
			return m_loc;
		}

		public Block getBlock()
		{
			return m_block;
		}
		
		public void setLoc(Vector2i loc)
		{
			m_loc = loc;
		}
	}
	
	private ArrayList<Cell> m_cells;
	private int             m_offset;

	public Polyomino()
	{
		m_cells = new ArrayList<Cell>();
		m_offset = 0;

		/* Now build */
		build();
	}

	private void build()
	{
		// int[] sizeDistribution = { 2, 2, 2, 2, 3, 3, 3, 4, 4, 5 };
		int[] sizeDistribution = { 2, 2, 2, 2, 3 };
		int size = RandomUtils.randomFromDist(sizeDistribution);
		// size = 6;

		ArrayList<Vector2i> visited = new ArrayList<Vector2i>();
		ArrayList<Vector2i> available = new ArrayList<Vector2i>();

		/* Create a root node */
		available.add(new Vector2i(0, 0));

		while (visited.size() < size)
		{
			/* Pick a node to use as a parent */
			int index = RandomUtils.GENERATOR.nextInt(available.size());
			Vector2i curLoc = available.get(index);
			
			/* Make sure we haven't visited this one */
			if (visited.contains(curLoc)) continue;

			/* Compute the degree of this node */
			int neighbors = 0;
			for (Direction direction : Direction.toArray())
			{
				Vector2i vel = direction.toVector();
				Vector2i newLoc = curLoc.add(vel);
				if (visited.contains(newLoc)) neighbors++;
			}

			/* Skip if there's no room to add a node */
			if (neighbors >= Direction.numDirections())
			{
				available.remove(curLoc);
				continue;
			}
			
			visited.add(curLoc);

			/* Add the neighboring cells to the pool */
			for (Direction direction : Direction.toArray())
			{
				Vector2i vel = direction.toVector();
				Vector2i newLoc = curLoc.add(vel);
				available.add(newLoc);
			}
		}

		/* Final process over chosen locations */
		for (Vector2i v : visited)
		{
			System.out.println("v = " + v);

			/* Build the block */
			Block block = new Block();

			/* Make the block float */
			block.setFloating(true);

			/* Now connect it to neighboring cells */
			double internalConnectionChance = 0.25;
			for (Direction direction : Direction.toArray())
			{
				Vector2i neighbor = v.add(direction.toVector());
				if (visited.contains(neighbor))
				{
					boolean addWire = RandomUtils
							.randomDouble(internalConnectionChance);
					if (addWire) block.setWire(direction, true);
				}
			}

			/* Create the Cell instance for this Block instance */
			Cell cell = new Cell(v, block);
			m_cells.add(cell);
		}
	}
	
	public void removeFromGrid(Grid grid, GridPhysics gridPhysics)
	{
		updateInGrid(grid, gridPhysics, false);
	}

	public void placeInGrid(Grid grid, GridPhysics gridPhysics)
	{
		updateInGrid(grid, gridPhysics, true);
	}
	
	/*
	 * Requires GridPhysics so we can incorporate gravity.
	 */
	private void updateInGrid(Grid grid, GridPhysics gridPhysics,
	                          boolean present)
	{
		/* Find the middle of the board at the "top" */
		Vector2i middle;
		switch (gridPhysics.getGravity())
		{
			case NORTH:
				middle = new Vector2i(grid.getWidth() / 2, grid.getHeight() - 1);
				middle = middle.sub(new Vector2i(m_offset, 0));
				break;
			case SOUTH:
				middle = new Vector2i(grid.getWidth() / 2, 0);
				middle = middle.sub(new Vector2i(m_offset, 0));
				break;
			case WEST:
				middle = new Vector2i(grid.getWidth() - 1, grid.getHeight() / 2);
				middle = middle.sub(new Vector2i(0, m_offset));
				break;
			case EAST:
			default:
				middle = new Vector2i(0, grid.getHeight() / 2);
				middle = middle.sub(new Vector2i(0, m_offset));
				break;
		}
		System.out.println("offset = " + m_offset);

		/* Find where the origin goes in the grid */
		Vector2i center = getCenter(grid, gridPhysics);

		System.out.println("middle = " + middle);
		System.out.println("center = " + center);

		/* Build and place each block */
		for (Cell cell : m_cells)
		{
			/* Extract from cell */
			Vector2i v = cell.getLoc();
			Block block = present? cell.getBlock() : null;

			/* Compute where it will go */
			Vector2i loc = middle.sub(center).add(v);
			System.out.println("v = " + v + " ==> loc = " + loc);
			
			/* Place in the grid */
			grid.setBlock(loc, block);
		}
	}

	public void translate(Grid grid, GridPhysics gridPhysics, int delta)
	{
		removeFromGrid(grid, gridPhysics);
		m_offset += delta;
		placeInGrid(grid, gridPhysics);
	}

	public void rotate(Grid grid, GridPhysics gridPhysics, Rotation rotation)
	{
		/* Remove the blocks from the grid */
		removeFromGrid(grid, gridPhysics);
		
		/* Now update all vectors */
		for (Cell cell : m_cells)
		{
			Vector2i v = cell.getLoc();
			v = rotation.rotateVector90(v);
			cell.setLoc(v);
			
			Block block = cell.getBlock();
			block.rotate(rotation);
		}
		
		/* Put the blocks back into the grid */
		placeInGrid(grid, gridPhysics);
	}

	public void release()
	{
		for (Cell cell : m_cells)
		{
			Block block = cell.getBlock();
			block.setFloating(false);
		}
	}
	
	private Vector2i getMin()
	{
		/* We know for certain cell (0, 0) is in our set */
		Vector2i ret = new Vector2i(0, 0);
		for (Cell cell : m_cells)
		{
			Vector2i v = cell.getLoc();
			ret.setX(Math.min(ret.getX(), v.getX()));
			ret.setY(Math.min(ret.getY(), v.getY()));
		}
		return ret;
	}
	
	private Vector2i getMax()
	{
		/* We know for certain cell (0, 0) is in our set */
		Vector2i ret = new Vector2i(0, 0);
		for (Cell cell : m_cells)
		{
			Vector2i v = cell.getLoc();
			ret.setX(Math.max(ret.getX(), v.getX()));
			ret.setY(Math.max(ret.getY(), v.getY()));
		}
		return ret;
	}

	private Vector2i getCenter(Grid grid, GridPhysics gridPhysics)
	{
		/*
		 * We want to find the largest distances so that there is enough space
		 * at the top.
		 */
		Vector2i min = getMin();
		Vector2i max = getMax();
		
		int x = 0;
		int y = 0;
		
		Vector2i gravity = gridPhysics.getGravityVector();
		
		if (gravity.getY() < 0)
		{
			x = (min.getX() + max.getX()) / 2;
			y = Math.abs(max.getY());
		}
		else if (gravity.getY() > 0)
		{
			x = (min.getX() + max.getX()) / 2;
			y = -Math.abs(min.getY());
		}
		else if (gravity.getX() < 0)
		{
			x = Math.abs(max.getX());
			y = (min.getY() + max.getY()) / 2;
		}
		else if (gravity.getX() > 0)
		{
			x = -Math.abs(min.getX());
			y = (min.getY() + max.getY()) / 2;
		}

		return new Vector2i(x, y);
	}
}
