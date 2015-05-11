package wireblocks;

import graphtools.Graph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

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
