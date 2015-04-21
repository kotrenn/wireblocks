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
