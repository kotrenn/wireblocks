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
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class FancyBlockView implements BlockView
{
	private int           m_size;
	Map<String, Image>    m_images;
	Map<Integer, String>  m_imageNameMap;
	Map<Integer, Integer> m_rotationMap;
	
	public FancyBlockView()
	{
		m_size = 32;
		m_images = new HashMap<String, Image>();
		m_imageNameMap = new HashMap<Integer, String>();
		m_rotationMap = new HashMap<Integer, Integer>();

		addImage("4WayWire");
		addImage("LWire");
		addImage("StraightWire");
		addImage("3WayWire");

		addTile(new boolean[] { true, true, false, false }, "LWire", 0);
		addTile(new boolean[] { false, true, true, false }, "LWire", 1);
		addTile(new boolean[] { false, false, true, true }, "LWire", 2);
		addTile(new boolean[] { true, false, false, true }, "LWire", 3);

		addTile(new boolean[] { false, true, false, true }, "StraightWire", 0);
		addTile(new boolean[] { true, false, true, false }, "StraightWire", 1);

		addTile(new boolean[] { true, true, false, true }, "3WayWire", 0);
		addTile(new boolean[] { true, true, true, false }, "3WayWire", 1);
		addTile(new boolean[] { false, true, true, true }, "3WayWire", 2);
		addTile(new boolean[] { true, false, true, true }, "3WayWire", 3);

		addTile(new boolean[] { true, true, true, true }, "4WayWire", 0);
	}

	private void addImage(String name)
	{
		String directory = "rsc/";
		String extension = ".png";
		try
		{
			Image image = ImageIO.read(new File(directory + name + extension));
			m_images.put(name, image);
		}
		catch (IOException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}

	private int indexOf(boolean[] wires)
	{
		int ret = 0;
		int pow = 1;
		for (boolean wire : wires)
		{
			if (wire) ret += pow;
			pow *= 2;
		}
		
		return ret;
	}

	private void addTile(boolean[] wires, String name, int rotation)
	{
		int index = indexOf(wires);
		m_imageNameMap.put(index, name);
		m_rotationMap.put(index, rotation);
	}
	
	@Override
	public Vector2i getDims()
	{
		return new Vector2i(m_size, m_size);
	}

	@Override
	public void display(Graphics2D g2d, Block block, Vector2i corner)
	{
		/* Hash this block */
		boolean[] wires = block.getWires();
		int index = indexOf(wires);

		/* Look up information on this block */
		String name = m_imageNameMap.get(index);
		Image image = m_images.get(name);
		int rotation = m_rotationMap.get(index);

		/* Compute the angle of rotation in radians */
		double theta = rotation * Math.PI / 2.0;

		/* Useful coordinates */
		int x = corner.getX();
		int y = corner.getY();
		int x2 = x + m_size / 2;
		int y2 = y + m_size / 2;
		
		/* Draw the background */
		Color wireColor = block.getColor();
		g2d.setColor(wireColor);
		g2d.fillRect(x, y, m_size, m_size);
		
		/* Draw the image */
		g2d.rotate(theta, x2, y2);
		g2d.drawImage(image, x, y, null);
		g2d.rotate(-theta, x2, y2);
	}
}
