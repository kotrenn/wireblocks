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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wireblocks.dlx.DLXSolver;
import wireblocks.dlx.LinkNode;
import wireblocks.dlx.SetCoverInstance;
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

		System.out.println(builder);
		
		System.out.println(m_graph);
		int[] colors = colorGraph();

		paintCells(builder, colors);
	}

	private String vertexToString(int v)
	{
		return String.valueOf(v);
	}

	private String edgeToString(int u, int v, int color)
	{
		int lower = Math.min(u, v);
		int upper = Math.max(u, v);
		return "" + lower + "_" + upper + "_" + color;
	}
	
	private String attachColorToString(String label, int color)
	{
		return label + "_" + color;
	}
	
	private int[] colorGraph()
	{
		int numVertices = m_graph.numberOfVertices();
		int numColors = 4;
		
		String[] vertexLabels = new String[numVertices];
		
		for (int v = 0; v < numVertices; ++v)
			vertexLabels[v] = vertexToString(v);
		
		System.out.println("vertexLabels:");
		System.out.println(vertexLabels);
		
		List<Vector2i> edges = new LinkedList<Vector2i>();
		for (int v = 0; v < numVertices; ++v)
		{
			List<Integer> neighbors = m_graph.getNeighbors(v);
			for (int u : neighbors)
			{
				if (u <= v) continue;
				edges.add(new Vector2i(u, v));
			}
		}

		List<String> edgeLabels = new LinkedList<String>();
		for (Vector2i edge : edges)
		{
			int u = edge.getX();
			int v = edge.getY();
			for (int color = 0; color < numColors; ++color)
			{
				String edgeLabel = edgeToString(u, v, color);
				edgeLabels.add(edgeLabel);
			}
		}
		
		System.out.println("edgeLabels:");
		System.out.println(edgeLabels);
		
		/* Build our problem instance */
		SetCoverInstance instance = new SetCoverInstance();
		
		/* Add *required* vertex columns */
		for (String vertex : vertexLabels)
			instance.addColumnLabel(vertex);
		
		/* Add *optional* edge columns */
		for (String edge : edgeLabels)
			instance.addColumnLabel(edge, true);

		/* Add edge coloring constraints */
		for (int v = 0; v < numVertices; ++v)
		{
			List<Integer> neighbors = m_graph.getNeighbors(v);
			for (int color = 0; color < numColors; ++color)
			{
				/* Construct the name of this row */
				String rowLabel = attachColorToString(vertexToString(v), color);

				/* Create a list of row constraints */
				List<String> rowConstraints = new LinkedList<String>();

				/* Add in the requirement of the vertex */
				rowConstraints.add(vertexToString(v));

				/* Now add in edge coloring constraints */
				for (int u : neighbors)
				{
					String edgeLabel = edgeToString(u, v, color);
					rowConstraints.add(edgeLabel);
				}
				
				System.out.println("row " + rowLabel + ": " + rowConstraints);

				/* Finally, add the row to the instance */
				int numConstraints = rowConstraints.size();
				String[] constraints = rowConstraints
				        .toArray(new String[numConstraints]);
				instance.addRow(rowLabel, constraints);
			}
		}

		/* Build our solver */
		DLXSolver solver = new DLXSolver(instance);

		/* Find a solution and print it out */
		List<LinkNode> solution = solver.solve();
		List<String> solutionLabels = instance.solutionToLabels(solution);
		System.out.println(solutionLabels);
		
		int[] colorings = new int[numVertices];
		for (String label : solutionLabels)
		{
			String[] vals = label.split("_");
			int vertex = Integer.parseInt(vals[0]);
			int color = Integer.parseInt(vals[1]);
			colorings[vertex] = color;
		}

		return colorings;
	}

	private void paintCells(GraphBuilder builder, int[] colors)
	{
		Map<Integer, Integer> regionMap = builder.getRegions();

		for (int x = 0; x < m_grid.getWidth(); ++x)
			for (int y = 0; y < m_grid.getHeight(); ++y)
			{
				Vector2i cell = new Vector2i(x, y);
				if (m_grid.getBlock(cell) == null) continue;
				int index = builder.indexOf(cell);
				int region = regionMap.get(index);
				int color = colors[region];
				Color dstColor = GridPainter.PALETTE[color];
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
