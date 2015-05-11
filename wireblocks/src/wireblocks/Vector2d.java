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

public class Vector2d
{
	private double m_x;
	private double m_y;
	
	public Vector2d()
	{
		m_x = m_y = 0.0;
	}
	
	public Vector2d(double x, double y)
	{
		m_x = x;
		m_y = y;
	}
	
	public Vector2d(Vector2d rhs)
	{
		m_x = rhs.m_x;
		m_y = rhs.m_y;
	}
	
	public Vector2d(Vector2i rhs)
	{
		m_x = rhs.getX();
		m_y = rhs.getY();
	}
	
	public boolean equals(Vector2d rhs)
	{
		return m_x == rhs.m_x && m_y == rhs.m_y;
	}
	
	@Override
	public String toString()
	{
		return "[" + m_x + ", " + m_y + "]";
	}
	
	public Vector2d setX(double x)
	{
		m_x = x;
		return this;
	}
	
	public Vector2d setY(double y)
	{
		m_y = y;
		return this;
	}
	
	public Vector2d setXY(double x, double y)
	{
		m_x = x;
		m_y = y;
		return this;
	}
	
	public Vector2d setXY(Vector2d v)
	{
		m_x = v.m_x;
		m_y = v.m_y;
		return this;
	}
	
	public double getX()
	{
		return m_x;
	}
	
	public double getY()
	{
		return m_y;
	}
	
	public Vector2i toVector2i()
	{
		return new Vector2i(this);
	}
	
	public int lengthI()
	{
		return (int)lengthD();
	}
	
	public double lengthD()
	{
		return Math.sqrt(m_x * m_x + m_y * m_y);
	}
	
	public Vector2d scale(int s)
	{
		return new Vector2d(m_x * s, m_y * s);
	}
	
	public Vector2d scale(int x, int y)
	{
		return new Vector2d(m_x * x, m_y * y);
	}
	
	public Vector2d scale(double s)
	{
		return new Vector2d(s * m_x, s * m_y);
	}
	
	public Vector2d scale(double x, double y)
	{
		return new Vector2d(x * m_x, y * m_y);
	}
	
	public Vector2d scale(Vector2d v)
	{
		return new Vector2d(m_x * v.m_x, m_y * v.m_y);
	}
	
	public Vector2d scaleSelf(int s)
	{
		m_x *= s;
		m_y *= s;
		return this;
	}
	
	public Vector2d scaleSelf(int x, int y)
	{
		m_x *= x;
		m_y *= y;
		return this;
	}
	
	public Vector2d scaleSelf(double s)
	{
		m_x *= s;
		m_y *= s;
		return this;
	}
	
	public Vector2d scaleSelf(double x, double y)
	{
		m_x *= x;
		m_y *= y;
		return this;
	}
	
	public Vector2d scaleSelf(Vector2d v)
	{
		m_x *= v.m_x;
		m_y *= v.m_y;
		return this;
	}
	
	public Vector2d setLenI(int d)
	{
		return new Vector2d(this).scaleSelf(d / lengthD());
	}
	
	public Vector2d setLenD(double d)
	{
		return new Vector2d(this).scaleSelf(d / lengthD());
	}
	
	public Vector2d add(Vector2d v)
	{
		return new Vector2d(m_x + v.m_x, m_y + v.m_y);
	}
	
	public Vector2d sub(Vector2d v)
	{
		return new Vector2d(m_x - v.m_x, m_y - v.m_y);
	}
	
	public double dot(Vector2d v)
	{
		return m_x * v.m_x + m_y * v.m_y;
	}
}
