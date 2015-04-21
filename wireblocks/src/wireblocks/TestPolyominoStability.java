package wireblocks;

public class TestPolyominoStability
{
	public static void runTest()
	{
		Grid grid = new Grid(6, 12);
		GridPhysics physics = new GridPhysics(Direction.SOUTH);
		Polyomino polyomino = new Polyomino();
		polyomino.placeInGrid(grid, physics);
	}
	
	public static void main(String[] args)
	{
		for (int i = 0; i < 10000; ++i)
			TestPolyominoStability.runTest();
	}
}
