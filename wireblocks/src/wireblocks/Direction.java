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
