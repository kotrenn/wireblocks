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
import java.util.ArrayList;
import java.util.Map;

import wireblocks.graphtools.Graph;

public class GridPainter
{
	private static final Color[] PALETTE    = { Color.RED, Color.YELLOW,
	        Color.GREEN, Color.BLUE        };
	private static final Color   EDGE_COLOR = Color.magenta;
	
	Grid                         m_grid;
	Graph                        m_graph;
	ArrayList<Integer>           m_buckets;
	int                          m_numNodes;
	int                          m_numRegions;

	public GridPainter(Grid grid)
	{
		m_grid = grid;
		
		GraphBuilder builder = new GraphBuilder(m_grid);
		m_graph = builder.buildGraph();
		Map<Integer, Integer> regionMap = builder.getRegions();

		System.out.println(builder);

		for (int x = 0; x < m_grid.getWidth(); ++x)
			for (int y = 0; y < m_grid.getHeight(); ++y)
			{
				Vector2i cell = new Vector2i(x, y);
				if (m_grid.getBlock(cell) == null) continue;
				int index = builder.indexOf(cell);
				int region = regionMap.get(index);
				Color dstColor = GridPainter.PALETTE[region
				                                     % GridPainter.PALETTE.length];
				paintPolyomino(builder, cell, region, dstColor);
			}
	}
	
	public void update()
	{
	}

	/* DFS paint can filling algorithm */
	private void paintPolyomino(GraphBuilder builder, Vector2i root,
	                            int region, Color dstColor)
	{
		Block block = m_grid.getBlock(root);
		if (block == null) return;
		
		int index = builder.indexOf(root);
		Map<Integer, Integer> regionMap = builder.getRegions();
		if (regionMap.get(index) != region) return;

		if (block.getColor() == dstColor) return;
		block.setColor(dstColor);

		for (Direction direction : Direction.toArray())
		{
			Vector2i vel = direction.toVector();
			Vector2i newLoc = root.add(vel);

			if (!m_grid.isInBounds(newLoc)) continue;

			paintPolyomino(builder, newLoc, region, dstColor);
		}
	}
}
