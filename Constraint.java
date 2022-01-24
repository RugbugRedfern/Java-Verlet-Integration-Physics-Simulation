import java.awt.*;

public class Constraint
{
	// the nodes that the constraint is attached to
	public Node nodeA;
	public Node nodeB;

	// the initial length of the constraint
	public double length;
	
	// create a new constraint
	public Constraint(Node a, Node b)
	{
		this.nodeA = a;
		this.nodeB = b;
		
		// initialize the length to the distance between the nodes
		length = PointMath.distance(a.x, a.y, b.x, b.y);
	}

	// draw the constraint on the screen
	public void draw(Graphics g)
	{
		g.drawLine((int)Math.round(nodeA.x), (int)Math.round(nodeA.y), (int)Math.round(nodeB.x), (int)Math.round(nodeB.y));
	}
	
	// Get a color corresponding to a stress percentage (0-1)
	/*Color getStressColor(double percent)
	{
		double inversePercent = 1 - percent;
		
		Color a = Color.RED;
		Color b = Color.GREEN;
		
		double red = a.getRed() * percent + b.getRed() * inversePercent;
		double green = a.getGreen() * percent + b.getGreen() * inversePercent;
		double blue = a.getBlue() * percent + b.getBlue() * inversePercent;
		
		Color result = new Color((int)Math.round(red), (int)Math.round(green), (int)Math.round(blue));
		
		return result;
	}*/
}