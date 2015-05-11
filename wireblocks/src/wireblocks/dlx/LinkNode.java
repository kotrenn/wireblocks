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

package wireblocks.dlx;

public class LinkNode
{
	private LinkNode   m_up;
	private LinkNode   m_down;
	private LinkNode   m_left;
	private LinkNode   m_right;
	private LinkHeader m_column;
	private String     m_rowLabel;

	public LinkNode()
	{
		m_up = null;
		m_down = null;
		m_left = null;
		m_right = null;
		m_column = null;
		m_rowLabel = "";
	}

	public LinkNode getUp()
	{
		return m_up;
	}

	public LinkNode getDown()
	{
		return m_down;
	}

	public LinkNode getLeft()
	{
		return m_left;
	}

	public LinkNode getRight()
	{
		return m_right;
	}

	public LinkHeader getColumn()
	{
		return m_column;
	}
	
	public String getRowLabel()
	{
		return m_rowLabel;
	}

	public void setUp(LinkNode up)
	{
		m_up = up;
	}

	public void setDown(LinkNode down)
	{
		m_down = down;
	}

	public void setLeft(LinkNode left)
	{
		m_left = left;
	}

	public void setRight(LinkNode right)
	{
		m_right = right;
	}

	public void setColumn(LinkHeader column)
	{
		m_column = column;
	}

	public void setRowLabel(String rowLabel)
	{
		m_rowLabel = rowLabel;
	}
}
