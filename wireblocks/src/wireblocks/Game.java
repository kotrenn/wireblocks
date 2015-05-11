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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Game implements KeyListener
{
	private JFrame      m_frame;
	private Grid        m_grid;
	private GridView    m_gridView;
	private GridPhysics m_gridPhysics;
	private GridPainter m_gridPainter;
	private Polyomino   m_polyomino;
	private PathFinder  m_pathFinder;
	private boolean     m_updatingPhysics;
	
	public Game()
	{
		System.out.println(System.getProperty("java.version"));
		m_frame = new JFrame("Wire Blocks");
		m_frame.setSize(800, 600);
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.addKeyListener(this);

		m_grid = new Grid(8, 16);
		// m_grid = new Grid(6, 10);
		// m_grid.randomize();
		m_gridView = new GridView(m_grid);
		m_frame.add(m_gridView);

		m_gridPhysics = new GridPhysics(Direction.SOUTH);
		m_gridPainter = new GridPainter(m_grid);
		
		buildNextPiece();
		
		m_pathFinder = new PathFinder();
		
		// Put this at the end to recursively make all components visible
		m_frame.setVisible(true);
		m_frame.pack();
	}
	
	private void buildNextPiece()
	{
		m_polyomino = new Polyomino();
		m_polyomino.placeInGrid(m_grid, m_gridPhysics);
	}

	private void updatePhysics()
	{
		m_updatingPhysics = true;
	}
	
	public void run() throws InterruptedException
	{
		int sleepDelay = 10;
		sleepDelay = 100;
		while (true)
		{
			if (m_updatingPhysics)
				m_updatingPhysics = m_gridPhysics.update(m_grid);
			else
			{
				if (m_polyomino == null) buildNextPiece();

				int width = m_grid.getWidth();
				int height = m_grid.getHeight();
				Vector2i[] src = new Vector2i[height];
				Vector2i[] dst = new Vector2i[height];
				for (int y = 0; y < height; ++y)
				{
					src[y] = new Vector2i(0, y);
					dst[y] = new Vector2i(width - 1, y);
				}
				Vector2i[] marked = m_pathFinder
						.findConnectedCells(m_grid, src, Direction.WEST, dst,
						                    Direction.EAST);
				for (Vector2i loc : marked)
					// m_grid.getBlock(loc).setActivated(true);
					m_grid.setBlock(loc, null);
				if (marked.length == 0) updatePhysics();
			}
			m_frame.repaint();
			Thread.sleep(sleepDelay);
		}
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		Game game = new Game();
		
		game.run();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		/* Make sure we have a piece queued up first */
		if (m_polyomino == null) return;
		
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP)
		{
			m_polyomino.release();
			m_polyomino = null;
			updatePhysics();
		}
		// else if (key == KeyEvent.VK_LEFT)
		// m_polyomino.rotate(m_grid, m_gridPhysics, Rotation.LEFT);
		// else if (key == KeyEvent.VK_RIGHT)
		// m_polyomino.rotate(m_grid, m_gridPhysics, Rotation.RIGHT);
		else if (key == KeyEvent.VK_DOWN)
			m_polyomino.rotate(m_grid, m_gridPhysics, Rotation.RIGHT);
		else if (key == KeyEvent.VK_LEFT)
			m_polyomino.translate(m_grid, m_gridPhysics, 1);
		else if (key == KeyEvent.VK_RIGHT)
			m_polyomino.translate(m_grid, m_gridPhysics, -1);
		else if (key == KeyEvent.VK_SPACE)
		{
			if (m_polyomino != null)
			{
				m_polyomino.removeFromGrid(m_grid, m_gridPhysics);
				m_polyomino = null;
			}
			m_gridPhysics.reverseGravity();
			updatePhysics();
		}
		else if (key == KeyEvent.VK_G) m_gridPainter = new GridPainter(m_grid);
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub

	}
}
