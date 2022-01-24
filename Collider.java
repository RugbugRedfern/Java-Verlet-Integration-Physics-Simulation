import java.awt.*;

public abstract class Collider
{
	// the position of the collider
	public double x;
	public double y;

	// returns whether or not a point exists inside the collider bounds
	abstract boolean intersect(double x, double y);

	// draw the box collider on the screen
	abstract void draw(Graphics g);

	// this means a node has collided with the collider, so now we have to rebound it,
	// basically bouncing it off of the surface of this collider
	abstract void rebound(Node node);

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

	CollisionTriggerListener listener;

	public void addTriggerListener(CollisionTriggerListener listener)
	{
		this.listener = listener;
	}
}