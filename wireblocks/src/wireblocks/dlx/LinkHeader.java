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

public class LinkHeader extends LinkNode
{
	private int     m_size;
	private boolean m_optional;
	private String  m_name;
	
	public LinkHeader(String name)
	{
		super();

		m_size = 0;
		m_optional = false;
		m_name = name;
	}
	
	public void decrementSize()
	{
		m_size -= 1;
	}
	
	public void incrementSize()
	{
		m_size += 1;
	}

	public int getSize()
	{
		return m_size;
	}

	public boolean isOptional()
	{
		return m_optional;
	}

	public String getName()
	{
		return m_name;
	}

	public void setOptional(boolean optional)
	{
		m_optional = optional;
	}
}
