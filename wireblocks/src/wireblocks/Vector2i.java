package wireblocks;

import java.awt.Dimension;

public class Vector2i implements Comparable<Vector2i>
{
	private int m_x;
	private int m_y;
	
	public Vector2i()
	{
		m_x = m_y = 0;
	}
	
	public Vector2i(int x, int y)
	{
		m_x = x;
		m_y = y;
	}
	
	public Vector2i(Vector2i rhs)
	{
		m_x = rhs.m_x;
		m_y = rhs.m_y;
	}
	
	public Vector2i(Vector2d rhs)
	{
		m_x = (int)rhs.getX();
		m_y = (int)rhs.getY();
	}
	
	@Override
	public boolean equals(Object other)
	{
		boolean ret = false;
		if (other instanceof Vector2i)
		{
			Vector2i rhs = (Vector2i)other;
			ret = m_x == rhs.m_x && m_y == rhs.m_y;
		}
		return ret;
	}
	
	@Override
	public String toString()
	{
		return "[" + m_x + ", " + m_y + "]";
	}
	
	public Dimension toDimension()
	{
		return new Dimension(m_x, m_y);
	}
	
	public Vector2i setX(int x)
	{
		m_x = x;
		return this;
	}
	
	public Vector2i setY(int y)
	{
		m_y = y;
		return this;
	}
	
	public Vector2i setXY(int x, int y)
	{
		m_x = x;
		m_y = y;
		return this;
	}
	
	public Vector2i setXY(Vector2i v)
	{
		m_x = v.m_x;
		m_y = v.m_y;
		return this;
	}
	
	public int getX()
	{
		return m_x;
	}
	
	public int getY()
	{
		return m_y;
	}
	
	public Vector2d toVector2d()
	{
		return new Vector2d(this);
	}
	
	public int lengthI()
	{
		return (int)Math.sqrt(m_x * m_x + m_y * m_y);
	}
	
	public double lengthD()
	{
		return Math.sqrt(m_x * m_x + m_y * m_y);
	}
	
	public Vector2i scale(int s)
	{
		return new Vector2i(m_x * s, m_y * s);
	}
	
	public Vector2i scale(int x, int y)
	{
		return new Vector2i(m_x * x, m_y * y);
	}
	
	public Vector2i scale(double s)
	{
		return new Vector2i((int)(s * m_x), (int)(s * m_y));
	}
	
	public Vector2i scale(double x, double y)
	{
		return new Vector2i((int)(x * m_x), (int)(y * m_y));
	}
	
	public Vector2i scale(Vector2i v)
	{
		return new Vector2i(m_x * v.m_x, m_y * v.m_y);
	}
	
	public Vector2i scaleInverse(Vector2i v)
	{
		return new Vector2i(m_x / v.m_x, m_y / v.m_y);
	}
	
	public Vector2i scaleSelf(int s)
	{
		m_x *= s;
		m_y *= s;
		return this;
	}
	
	public Vector2i scaleSelf(int x, int y)
	{
		m_x *= x;
		m_y *= y;
		return this;
	}
	
	public Vector2i scaleSelf(double s)
	{
		m_x = (int)(s * m_x);
		m_y = (int)(s * m_y);
		return this;
	}
	
	public Vector2i scaleSelf(double x, double y)
	{
		m_x = (int)(x * m_x);
		m_y = (int)(y * m_y);
		return this;
	}
	
	public Vector2i scaleSelf(Vector2i v)
	{
		m_x *= v.m_x;
		m_y *= v.m_y;
		return this;
	}
	
	public Vector2i setLenI(int d)
	{
		return new Vector2i(this).scaleSelf(d / lengthI());
	}
	
	public Vector2d setLenD(double d)
	{
		return new Vector2d(this).scaleSelf(d / lengthD());
	}
	
	public Vector2i add(Vector2i v)
	{
		return new Vector2i(m_x + v.m_x, m_y + v.m_y);
	}
	
	public Vector2i sub(Vector2i v)
	{
		return new Vector2i(m_x - v.m_x, m_y - v.m_y);
	}
	
	public int dot(Vector2i v)
	{
		return m_x * v.m_x + m_y * v.m_y;
	}
	
	public boolean distLeq(Vector2i v, int dist)
	{
		int dotSq = dot(v);
		int distSq = dist * dist;
		return dotSq <= distSq;
	}

	public Vector2i abs()
	{
		return new Vector2i(Math.abs(m_x), Math.abs(m_y));
	}
	
	@Override
	public int compareTo(Vector2i rhs)
	{
		if (m_x < rhs.m_x)
			return -1;
		else if (m_x > rhs.m_x)
			return 1;
		else if (m_y < rhs.m_y)
			return -1;
		else if (m_y > rhs.m_y)
			return 1;
		else
			return 0;
	}
}
