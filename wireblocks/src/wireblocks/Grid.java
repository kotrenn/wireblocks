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

public class Grid
{
	private int       m_width;
	private int       m_height;
	private Block[][] m_blocks;

	public Grid(int rows, int cols)
	{
		m_width = rows;
		m_height = cols;
		m_blocks = new Block[rows][cols];
		for (int x = 0; x < m_width; ++x)
			for (int y = 0; y < m_height; ++y)
				m_blocks[x][y] = null;
	}
	
	public void randomize()
	{
		for (int x = 0; x < m_width; ++x)
			for (int y = 0; y < m_height; ++y)
				m_blocks[x][y] = new Block();
	}
	
	public Block getBlock(int x, int y)
	{
		return m_blocks[x][y];
	}
	
	public Block getBlock(Vector2i v)
	{
		int x = v.getX();
		int y = v.getY();
		return m_blocks[x][y];
	}

	public void setBlock(int x, int y, Block block)
	{
		m_blocks[x][y] = block;
	}
	
	public void setBlock(Vector2i v, Block block)
	{
		int x = v.getX();
		int y = v.getY();
		m_blocks[x][y] = block;
	}

	public int getWidth()
	{
		return m_width;
	}

	public int getHeight()
	{
		return m_height;
	}
	
	public Vector2i getDims()
	{
		return new Vector2i(m_width, m_height);
	}

	public boolean isInBounds(Vector2i loc)
	{
		return loc.getX() >= 0 && loc.getY() >= 0 && loc.getX() < m_width
		       && loc.getY() < m_height;
	}
}
