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
		int[] activeDistribution = { 2, 2, 2, 2, 3, 3, 4 };
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
