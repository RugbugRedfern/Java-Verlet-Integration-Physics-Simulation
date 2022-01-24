import java.awt.*;

public class Node
{
	// how big the node should render as (radius)
	final int RENDER_SIZE = 8;

	// whether the node has physics applied or not
	public boolean fixed;

	// the position of the node
	public double x;
	public double y;

	// the position of the node last tick
	public double oldX;
	public double oldY;
	
	// initialize a new node
	public Node(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
	}
	
	// draw the node on the screen
	public void draw(Graphics g)
	{
		if(fixed)
			g.setColor(Color.BLUE);
		else
			g.setColor(Color.BLACK);
		
		g.fillOval(getIntX() - RENDER_SIZE / 2, getIntY() - RENDER_SIZE / 2, RENDER_SIZE, RENDER_SIZE);
	}

	// get a rounded version of the x
	public int getIntX()
	{
		return (int)Math.round(x);
	}

	// get a rounded version of the y
	public int getIntY()
	{
		return (int)Math.round(y);
	}

	// get a rounded version of the oldX
	public int getIntOldX()
	{
		return (int)Math.round(oldX);
	}

	// get a rounded version of the oldY
	public int getIntOldY()
	{
		return (int)Math.round(oldY);
	}
}