package wireblocks;

import java.awt.Graphics2D;

public interface BlockView
{
	public abstract Vector2i getDims();

	public abstract void display(Graphics2D g2d, Block block, Vector2i corner);

}
