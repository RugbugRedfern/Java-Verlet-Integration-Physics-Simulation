import java.awt.*;

public class CircleCollider extends Collider
{
	int radius;

	// create a new circle collider
	public CircleCollider(double x, double y, int radius)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	// draw the box collider on the screen
	public void draw(Graphics g)
	{
		g.setColor(Color.BLUE);
		g.drawOval(getIntX() - radius, getIntY() - radius, radius * 2, radius * 2);
		g.fillOval(getIntX() - 2, getIntY() - 2, 4, 4);
	}
	
	// returns whether or not a point exists inside the collider bounds
	public boolean intersect(double x, double y)
	{
		return PointMath.distance(this.x, this.y, x, y) <= radius;
	}
	
	// this means a node has collided with the collider, so now we have to rebound it,
	// basically bouncing it off of the surface of this collider
	@Override
	public void rebound(Node node)
	{
		if(listener != null)
		{
			listener.onCollisionTriggered(this, node);
			return;
		}
		
		// get the direction the node is coming from
		double dirX = node.x - x;
		double dirY = node.y - y;
		
		// get the velocityu of the node
		double velX = node.x - node.oldX;
		double velY = node.y - node.oldY;
		
		// get the magnitude of the velocity
		double velocityMagnitude = PointMath.magnitude(velX, velY);
		
		// get the magnitude of the direction (which in this case is just the distance to the node)
		double magnitude = PointMath.magnitude(dirX, dirY);

		// normalize the x and y of the distance to the node, which gives us a direction vector
		double normalizedX = dirX / magnitude;
		double normalizedY = dirY / magnitude;
		
		// move the node to the edge of the collider based on the direction vector
		double pushedX = normalizedX * radius;
		double pushedY = normalizedY * radius;
		
		// add force to the node by setting the old position further towards the center of
		// the collider depending on the node's current velocity. this essentially inverts the
		// velocity of the node (while taking some for collision dampening)
		double pushedOldX = normalizedX * (radius - velocityMagnitude * Simulation.COLLISION_DAMP);
		double pushedOldY = normalizedY * (radius - velocityMagnitude * Simulation.COLLISION_DAMP);
		
		// apply the new positions
		node.x = x + pushedX;
		node.y = y + pushedY;
		
		node.oldX = x + pushedOldX;
		node.oldY = y + pushedOldY;
	}
}