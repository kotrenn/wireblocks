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

public class Block
{
	public static final Color NO_COLOR = Color.WHITE;

	private int               m_numWires;
	private boolean[]         m_wires;
	private boolean           m_floating;
	private Color             m_color;
	
	public Block()
	{
		m_numWires = 4;
		m_wires = new boolean[m_numWires];
		for (int i = 0; i < m_wires.length; ++i)
			m_wires[i] = false;
		
		/* Now randomize our active wires */

		/*
		 * First pick out how many wires to include. We want there to be fewer
		 * numbers of high count wires to not make things *too* easy.
		 */
		// int[] activeDistribution = { 2, 2, 2, 2, 3, 3, 4 };
		
		int[] activeDistribution = { 2, 2, 3, 3 };
		int activeWires = RandomUtils.randomFromDist(activeDistribution);
		
		/* Create a shuffled list from 0 to m_numWires - 1 */
		int[] shuffledWires = RandomUtils.createShuffledArray(m_numWires);
		
		/* Now set the first k wires to active (k = activeWires) */
		for (int i = 0; i < activeWires; ++i)
		{
			int curIndex = shuffledWires[i];
			m_wires[curIndex] = true;
		}
		
		/* Not floating by default */
		m_floating = false;

		/* Initial color */
		m_color = Block.NO_COLOR;
	}
	
	public void rotate(Rotation rotation)
	{
		int vel = rotation.getVelOffset();
		boolean[] wires = new boolean[m_numWires];
		for (int i = 0; i < m_numWires; ++i)
		{
			int index = (i + vel + m_numWires) % m_numWires;
			wires[i] = m_wires[index];
		}
		m_wires = wires;
	}

	public int getNumWires()
	{
		return m_numWires;
	}
	
	public boolean getWire(int index)
	{
		return m_wires[index];
	}

	public boolean[] getWires()
	{
		boolean[] ret = new boolean[m_wires.length];
		for (int i = 0; i < m_wires.length; ++i)
			ret[i] = m_wires[i];
		return ret;
	}

	public boolean hasWire(Direction direction)
	{
		int index = Direction.getIndex(direction);
		return m_wires[index];
	}
	
	public void setWire(Direction direction, boolean value)
	{
		int index = Direction.getIndex(direction);
		m_wires[index] = value;
	}
	
	public void setFull()
	{
		for (int i = 0; i < m_numWires; ++i)
			m_wires[i] = true;
	}

	public boolean isFloating()
	{
		return m_floating;
	}

	public void setFloating(boolean val)
	{
		m_floating = val;
	}
	
	public void toggleFloating()
	{
		// From an AP Computer Science exam
		m_floating = m_floating == false;
	}

	public Color getColor()
	{
		return m_color;
	}

	public void setColor(Color color)
	{
		m_color = color;
	}
}
