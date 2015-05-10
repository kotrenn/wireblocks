package wireblocks;

import java.awt.Color;
import java.awt.Graphics2D;

public class BlockView
{
	private int m_size;

	public BlockView()
	{
		m_size = 30;
	}

	public Vector2i getDims()
	{
		return new Vector2i(m_size, m_size);
	}

	public void display(Graphics2D g2d, Block block, Vector2i corner)
	{
		/* Color choices */
		Color borderColor = Color.BLACK;
		Color wireColor = block.getColor();
		
		/* Draw the bounding rectangle */
		g2d.setColor(borderColor);
		g2d.drawRect(corner.getX(), corner.getY(), m_size, m_size);
		
		/* Draw the active wires */
		Vector2i half = new Vector2i(m_size, m_size).scale(0.5);
		Vector2i center = corner.add(half);
		g2d.setColor(wireColor);
		
		/* For each direction, compute where the wire goes and draw it */
		Direction[] dirs = Direction.toArray();
		for (int i = 0; i < block.getNumWires(); ++i)
		{
			/* Skip the wire if it's not even present in this block */
			boolean active = block.getWire(i);
			if (!active) continue;
			
			/* Now compute where on the border the wire ends at */
			Vector2i dir = dirs[i].toVector();
			Vector2i offset = dir.scale(m_size / 2);
			Vector2i edge = center.add(offset);

			/* Finally draw the line */
			g2d.drawLine(center.getX(), center.getY(), edge.getX(), edge.getY());
		}
	}
}
