package wireblocks;

public enum Direction
{
	NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);

	private final Vector2i m_vector;

	Direction(int x, int y)
	{
		m_vector = new Vector2i(x, y);
	}

	public Vector2i toVector()
	{
		return m_vector;
	}
	
	public Direction reverse()
	{
		Direction ret;
		switch (this)
		{
			case NORTH:
				ret = SOUTH;
				break;
			case SOUTH:
				ret = NORTH;
				break;
			case WEST:
				ret = EAST;
				break;
			case EAST:
				ret = WEST;
				break;
			default:
				/* TODO: Throw an exception */
				System.err.println("Uh oh!  Reversing an unknown direction.");
				ret = NORTH;
				break;
		}
		return ret;
	}
	
	private static final Direction[] DIRECTION_ARRAY = { NORTH, EAST, SOUTH,
	                                                     WEST                                    };
	
	/*
	 * Provides an array of directions in a standard ordering.
	 */
	public static Direction[] toArray()
	{
		return Direction.DIRECTION_ARRAY;
	}

	public static int numDirections()
	{
		return Direction.DIRECTION_ARRAY.length;
	}
	
	public static int getIndex(Direction direction)
	{
		for (int i = 0; i < Direction.DIRECTION_ARRAY.length; ++i)
			if (Direction.DIRECTION_ARRAY[i] == direction) return i;
		/* TODO: Throw an invalid argument exception */
		return -1;
	}
}
