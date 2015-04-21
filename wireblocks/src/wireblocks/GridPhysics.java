package wireblocks;

public class GridPhysics
{
	private Direction m_gravity;
	
	public GridPhysics(Direction direction)
	{
		m_gravity = direction;
	}

	public void reverseGravity()
	{
		m_gravity = m_gravity.reverse();
	}
	
	public boolean update(Grid grid)
	{
		Vector2i gravity = getGravityVector();

		int xLower = gravity.getX() < 0? 0 : grid.getWidth();
		int xUpper = gravity.getX() < 0? grid.getWidth() : -1;
		int xDelta = gravity.getX() < 0? 1 : -1;

		int yLower = gravity.getY() < 0? 0 : grid.getHeight();
		int yUpper = gravity.getY() < 0? grid.getHeight() : -1;
		int yDelta = gravity.getY() < 0? 1 : -1;

		boolean updated = false;
		for (int x = xLower; x != xUpper; x += xDelta)
			for (int y = yLower; y != yUpper; y += yDelta)
			{
				boolean result = updateCell(grid, new Vector2i(x, y));
				if (result) updated = true;
			}

		return updated;
	}
	
	public Direction getGravity()
	{
		return m_gravity;
	}

	public Vector2i getGravityVector()
	{
		return m_gravity.toVector();
	}

	/*
	 * Recursive depth-first search of cell updating. Return value indicates
	 * whether we have successfully moved the block or not.
	 */
	private boolean updateCell(Grid grid, Vector2i cell)
	{
		/* Check if we're still in bounds first */
		if (!grid.isInBounds(cell)) return false;

		/* Get the block to update */
		Block block = grid.getBlock(cell);

		/* If there's nothing there, go back */
		if (block == null) return false;

		/* If the block is floating, go back */
		if (block.isFloating()) return false;

		/* Compute the cell to move into */
		Vector2i newLoc = cell.add(getGravityVector());

		/* If we're at the edge of the grid, go back */
		if (!grid.isInBounds(newLoc)) return false;
		
		/* Find out if there's anything in that space */
		Block newBlock = grid.getBlock(newLoc);
		
		/* If the space is empty, move in */
		if (newBlock == null)
		{
			moveBlock(grid, cell, newLoc);
			return true;
		}
		
		/*
		 * There's another block in the way. Recursively fall and return the
		 * success status
		 */
		boolean success = updateCell(grid, newLoc);
		if (success) moveBlock(grid, cell, newLoc);
		return success;
	}

	/* Swap the contents of two grid cells */
	private void moveBlock(Grid grid, Vector2i src, Vector2i dst)
	{
		Block a = grid.getBlock(src);
		Block b = grid.getBlock(dst);
		grid.setBlock(src, b);
		grid.setBlock(dst, a);
	}
}
