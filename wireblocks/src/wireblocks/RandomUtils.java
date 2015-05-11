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

import java.util.Random;

public class RandomUtils
{
	public static final Random GENERATOR = new Random();

	/*
	 * Takes a distribution of values and returns one of the values uniformly at
	 * random.
	 */
	public static int randomFromDist(int[] distribution)
	{
		int index = RandomUtils.GENERATOR.nextInt(distribution.length);
		return distribution[index];
	}
	
	/*
	 * Creates an array of integers of length numValues, and shuffles according
	 * to the Fisher-Yates (Knuth) Shuffle
	 */
	public static int[] createShuffledArray(int numValues)
	{
		/* Initialize an array with values from 0 to numValues - 1 */
		int[] shuffledArray = new int[numValues];
		for (int i = 0; i < numValues; ++i)
			shuffledArray[i] = i;

		/* Now shuffle the array */
		for (int i = 0; i < numValues; ++i)
		{
			int limit = numValues - i;
			int a = RandomUtils.GENERATOR.nextInt(limit);
			int b = limit - 1;

			/* Perform the swap */
			int tmp = shuffledArray[a];
			shuffledArray[a] = shuffledArray[b];
			shuffledArray[b] = tmp;
		}

		return shuffledArray;
	}
}
