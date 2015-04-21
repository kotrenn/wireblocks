package wireblocks;

public enum Rotation
{
	LEFT(1), RIGHT(-1);
	
	private final int m_vel;
	
	Rotation(int vel)
	{
		m_vel = vel;
	}

	public int getVelOffset()
	{
		return m_vel;
	}
	
	/* Rotate a vector by 90 degrees */
	public Vector2i rotateVector90(Vector2i v)
	{
		Vector2i ret = new Vector2i(v.getY(), -v.getX());
		if (m_vel < 0) ret.scaleSelf(-1); // Reverse
		return ret;
	}
}
