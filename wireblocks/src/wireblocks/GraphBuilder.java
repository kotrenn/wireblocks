package wireblocks;

import graphtools.AdjacencyGraph;
import graphtools.Graph;

import java.util.HashMap;
import java.util.Map;

public class GraphBuilder
{
	private Grid                  m_grid;
	private Graph                 m_graph;
	private Map<Integer, Integer> m_regions;
	private int                   m_numRegions;
	
	public GraphBuilder(Grid grid)
	{
		m_grid = grid;
	}
	
	public int indexOf(Vector2i v)
	{
		int x = v.getX();
		int y = v.getY();
		return y * m_grid.getWidth() + x;
	}
	
	public Vector2i reverseIndex(int index)
	{
		int x = index % m_grid.getWidth();
		int y = index / m_grid.getWidth();
		return new Vector2i(x, y);
	}

	@Override
	public String toString()
	{
		String ret = "";

		for (Map.Entry<Integer, Integer> entry : m_regions.entrySet())
		{
			int index = entry.getKey();
			int region = entry.getValue();
			ret += region + " ==> " + reverseIndex(index);
			ret += "\n";
		}
		return ret;
	}
	
	public Graph buildGraph()
	{
		computeConnectedRegions();
		
		m_graph = new AdjacencyGraph(m_numRegions);
		
		computeEdges();

		return m_graph;
	}
	
	public Map<Integer, Integer> getRegions()
	{
		return m_regions;
	}
	
	private void computeConnectedRegions()
	{
		m_regions = new HashMap<Integer, Integer>();
		m_numRegions = 0;
		for (int x = 0; x < m_grid.getWidth(); ++x)
			for (int y = 0; y < m_grid.getHeight(); ++y)
				markRegion(new Vector2i(x, y), -1);
	}
	
	private void markRegion(Vector2i cell, int region)
	{
		Block curBlock = m_grid.getBlock(cell);
		if (curBlock == null) return;
		
		int index = indexOf(cell);
		if (m_regions.containsKey(index)) return;

		int newRegion = region;
		if (region < 0) newRegion = m_numRegions++;
		m_regions.put(index, newRegion);
		System.out.println("Mapping cell " + cell + " to region " + newRegion);
		
		for (Direction direction : Direction.toArray())
		{
			/* Make sure the block has a wire in this direction first */
			if (!curBlock.hasWire(direction)) continue;

			Vector2i vel = direction.toVector();
			Vector2i newLoc = cell.add(vel);

			/* Make sure we're still in bounds */
			if (!m_grid.isInBounds(newLoc)) continue;

			/* Check there is actually a block in the neighboring cell */
			Block newBlock = m_grid.getBlock(newLoc);
			if (newBlock == null) continue;

			/*
			 * Make sure the other block also has a wire coming in from that
			 * direction
			 */
			Direction reverse = direction.reverse();
			if (!newBlock.hasWire(reverse)) continue;

			/* Recursively descend */
			markRegion(newLoc, newRegion);
		}
	}
	
	private void computeEdges()
	{
		for (int x = 0; x < m_grid.getWidth(); ++x)
			for (int y = 0; y < m_grid.getHeight(); ++y)
				computeEdgesOfCell(new Vector2i(x, y));
	}
	
	private void computeEdgesOfCell(Vector2i cell)
	{
		Block curBlock = m_grid.getBlock(cell);
		if (curBlock == null) return;
		
		int region = m_regions.get(indexOf(cell));
		
		for (Direction direction : Direction.toArray())
		{
			Vector2i vel = direction.toVector();
			Vector2i newLoc = cell.add(vel);

			/* Make sure we're still in bounds */
			if (!m_grid.isInBounds(newLoc)) continue;

			/* Check there is actually a block in the neighboring cell */
			Block newBlock = m_grid.getBlock(newLoc);
			if (newBlock == null) continue;

			/* Check if the two cells are in different regions */
			int newRegion = m_regions.get(indexOf(newLoc));
			if (region == newRegion) continue;

			/* Check if this edge has already been added */
			if (m_graph.hasEdge(region, newRegion)) continue;

			/* Now add the edge */
			m_graph.addEdge(region, newRegion);
		}
	}
}
