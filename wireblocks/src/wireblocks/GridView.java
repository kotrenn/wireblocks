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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class GridView extends JPanel implements MouseListener
{
	private static final long serialVersionUID = -4034524900302096409L;
	
	private Grid              m_grid;
	private BlockView         m_blockView;

	public GridView(Grid grid)
	{
		this.addMouseListener(this);
		
		m_grid = grid;
		m_blockView = new FancyBlockView();
		
		Vector2i blockDims = m_blockView.getDims();
		Vector2i gridDims = m_grid.getDims();
		Dimension panelDims = gridDims.scale(blockDims).toDimension();
		setPreferredSize(panelDims);
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		
		Vector2i borderDims = m_grid.getDims().scale(m_blockView.getDims());
		g2d.drawRect(0, 0, borderDims.getX(), borderDims.getY());

		Vector2i blockDims = m_blockView.getDims();
		for (int x = 0; x < m_grid.getWidth(); ++x)
			for (int y = 0; y < m_grid.getHeight(); ++y)
			{
				Block block = m_grid.getBlock(x, y);
				if (block == null) continue;
				Vector2i corner = new Vector2i(x, y).scale(blockDims);
				m_blockView.display(g2d, block, corner);
			}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		Vector2i mouseLoc = new Vector2i(e.getX(), e.getY());
		Vector2i blockCell = getBlockCoordFromCoord(mouseLoc);
		if (!m_grid.isInBounds(blockCell)) return;
		Block block = m_grid.getBlock(blockCell.getX(), blockCell.getY());
		if (block == null)
		{
			block = new Block();
			m_grid.setBlock(blockCell.getX(), blockCell.getY(), block);
			return;
		}

		int button = e.getButton();
		if (button == MouseEvent.BUTTON1) block.rotate(Rotation.LEFT);
		if (button == MouseEvent.BUTTON3) block.rotate(Rotation.RIGHT);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}
	
	public Vector2i getBlockCoordFromCoord(Vector2i coord)
	{
		Vector2i blockDims = m_blockView.getDims();
		Vector2i result = coord.scaleInverse(blockDims);
		return result;
	}
}
