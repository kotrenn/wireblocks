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

import java.awt.Color;
import java.awt.Graphics2D;

public class SimpleBlockView implements BlockView
{
	private int m_size;
	
	public SimpleBlockView()
	{
		m_size = 30;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see wireblocks.GridVi#getDims()
	 */
	@Override
	public Vector2i getDims()
	{
		return new Vector2i(m_size, m_size);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see wireblocks.GridVi#display(java.awt.Graphics2D, wireblocks.Block,
	 * wireblocks.Vector2i)
	 */
	@Override
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
