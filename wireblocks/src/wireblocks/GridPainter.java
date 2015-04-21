package wireblocks;

import java.awt.Color;

public class GridPainter
{
	private static final Color[] PALETTE    = { Color.RED, Color.YELLOW,
	        Color.GREEN, Color.BLUE        };
	private static final Color   EDGE_COLOR = Color.MAGENTA;
	
	Grid                         m_grid;

	public GridPainter(Grid grid)
	{
		m_grid = grid;
	}
	
	public void update()
	{
	}

	/* DFS paint can filling algorithm */
	private void paintPolyomino(Vector2i root, Color srcColor, Color dstColor)
	{
		Block block = m_grid.getBlock(root);
		if (block.getColor() != srcColor) return;
		
		block.setColor(dstColor);

		for (Direction direction : Direction.toArray())
		{
			Vector2i vel = direction.toVector();
			Vector2i newLoc = root.add(vel);
			paintPolyomino(newLoc, srcColor, dstColor);
		}
	}
}
