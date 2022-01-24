import java.awt.*;

public class BoxCollider extends Collider
{
	// amount to overshoot for determining the collision direction
	final int DIRECTION_FORGIVENESS = 2;

	double width;
	double height;
	double degrees;
	
	// local points
	Point localTL;
	Point localTR;
	Point localBL;
	Point localBR;

	// create a new box collider
	public BoxCollider(double x, double y, double width, double height, double degrees)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.degrees = degrees;
		
		calculateBounds();
	}

	// calculate the local points for each vertex
	public void calculateBounds()
	{
		localTL = PointMath.transformPoint(x, y, (x - width / 2), (y + height / 2), degrees);
		localTR = PointMath.transformPoint(x, y, (x + width / 2), (y + height / 2), degrees);
		localBL = PointMath.transformPoint(x, y, (x - width / 2), (y - height / 2), degrees);
		localBR = PointMath.transformPoint(x, y, (x + width / 2), (y - height / 2), degrees);
	}

	// draw the box collider on the screen
	public void draw(Graphics g)
	{
		g.drawLine(localTL.getIntX(), localTL.getIntY(), localTR.getIntX(), localTR.getIntY());
		g.drawLine(localTR.getIntX(), localTR.getIntY(), localBR.getIntX(), localBR.getIntY());
		g.drawLine(localBR.getIntX(), localBR.getIntY(), localBL.getIntX(), localBL.getIntY());
		g.drawLine(localBL.getIntX(), localBL.getIntY(), localTL.getIntX(), localTL.getIntY());
	}
	
	// returns whether or not a point exists inside the collider bounds
	public boolean intersect(double x, double y)
	{
		Point localPoint = worldToLocal(x, y);
		return localPoint.x < this.x + width / 2 &&
			localPoint.x > this.x - width / 2 &&
			localPoint.y < this.y + height / 2 &&
			localPoint.y > this.y - height / 2;
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

		// convert the node's position and oldPosition from world space
		// to local space, which will make calculations easier
		Point pos = worldToLocal(node.x, node.y);
		Point oldPos = worldToLocal(node.oldX, node.oldY);
		
		double velX = pos.x - oldPos.x;
		double velY = pos.y - oldPos.y;
		
		// if it's rebounding, it means the node has intersected THIS TICK.
		// meaning the old position will still be outside this collider.
		// therefore, we can use the old position to check which direction it entered from.
		
		// calculate the bounds of this collider
		double top = this.y - height / 2;
		double bottom = this.y + height / 2;
		double left = this.x - width / 2;
		double right = this.x + width / 2;
		
		// store the position variables to modify later
		double newX = pos.x;
		double newY = pos.y;
		
		double newOldX = oldPos.x;
		double newOldY = oldPos.y;
		
		// check all potential collision directions, and apply force as needed to rebound the node
		if(oldPos.y > bottom - DIRECTION_FORGIVENESS) // below
		{
			newY = bottom;
			newOldY = bottom + velY * Simulation.COLLISION_DAMP;
		}
		else if(oldPos.y < top + DIRECTION_FORGIVENESS) // above
		{
			newY = top;
			newOldY = top + velY * Simulation.COLLISION_DAMP;
		}
		else if(oldPos.x > right - DIRECTION_FORGIVENESS) // right
		{
			newX = right;
			newOldX = right + velX * Simulation.COLLISION_DAMP;
		}
		else if(oldPos.x < left + DIRECTION_FORGIVENESS) // left
		{
			newX = left;
			newOldX = left + velX * Simulation.COLLISION_DAMP;
		}
		
		// convert positions from local space to world space so we can apply them to the node
		Point newPos = localToWorld(newX, newY);
		Point newOldPos = localToWorld(newOldX, newOldY);

		// apply the new positions to the node
		node.x = newPos.x;
		node.y = newPos.y;
		node.oldX = newOldPos.x;
		node.oldY = newOldPos.y;
	}
	
	// convert a point from world to local space
	Point worldToLocal(double x, double y)
	{
		return PointMath.transformPoint(this.x, this.y, x, y, -degrees);
	}
	
	// convert a point from local to world space
	Point localToWorld(double x, double y)
	{
		return PointMath.transformPoint(this.x, this.y, x, y, degrees);
	}
}