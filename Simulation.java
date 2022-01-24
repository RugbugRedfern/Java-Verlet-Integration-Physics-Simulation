import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class Simulation extends JFrame
{
	/* SETTINGS START */

	public static final int CONSTRAINT_ITERATIONS = 20; // The higher the iteration count, the more accurate the constraints will be
	public static final double CONSTRAINT_BREAKPOINT = 999; // 0 - infinity, the force required to break the constraint
	public static final double CONSTRAINT_STRENGTH = 1; // 0 - 1, how far the constraint can extend past its original distance
	public static final double GRAVITY = 0.05;
	public static final double COLLISION_DAMP = 0.75; // velocity multiplier on collision
	public static final double SIDE_PADDING = 5; // the distance to the side that nodes should collide with
	
	/* SETTINGS END */

	public ArrayList<Node> nodes = new ArrayList<Node>();
	public ArrayList<Constraint> constraints = new ArrayList<Constraint>();
	public ArrayList<Collider> colliders = new ArrayList<Collider>();

	int width; // width of the screen and borders
	int height; // height of the screen and borders

	GraphicsPanel graphicsPanel;

	// set up the window and variables
	public Simulation(int width, int height)
	{
		this.width = width;
		this.height = height;

		setTitle("Verlet Simulation");
		setSize(width, height + 40); // + 40 because the titlebar is apparently included in this method call and it's 40 pixels tall
		setVisible(true);
		graphicsPanel = new GraphicsPanel();
		getContentPane().add(graphicsPanel);

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}

	public void setPainter(Painter painter)
	{
		graphicsPanel.setPainter(painter);
	}

	// Handles rendering each frame
	public class GraphicsPanel extends JPanel
	{
		Painter painter;

		public GraphicsPanel()
		{
			
		}
		
		public void setPainter(Painter painter)
		{
			this.painter = painter;
		}

		// draw the graphics
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			// enable anti aliasing
			Graphics2D g2d = ((Graphics2D)g);
			g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
			
			if(painter != null)
				painter.paintComponent(g);

			// draw all nodes
			for(int i = 0; i < nodes.size(); i++)
			{
				nodes.get(i).draw(g);
			}
			
			// draw all constraints
			for(int i = 0; i < constraints.size(); i++)
			{
				constraints.get(i).draw(g);
			}
			
			// draw all colliders
			for(int i = 0; i < colliders.size(); i++)
			{
				colliders.get(i).draw(g);
			}
		}
	}

	// run the physics simulation once
	public void simulate()
	{
		simulateNodes();
		
		// Run the simulation for constraints CONSTRAINT_ITERATIONS times
		for(int s = 0; s < CONSTRAINT_ITERATIONS; s++)
		{
			simulateConstraints();
		}
		
		simulateCollision();
	}
	
	// simulate all nodes
	void simulateNodes()
	{
		for(int i = 0; i < nodes.size(); i++)
		{
			Node node = nodes.get(i);
			
			// Skip the node if it's fixed
			if(node.fixed)
				continue;
			
			// store the current position for later
			double tempX = node.x;
			double tempY = node.y;
			
			// calculate the node's current velocity
			double velX = node.x - node.oldX;
			double velY = node.y - node.oldY;
			
			// add the velocity to the position, apply external forces (gravity)
			node.x += velX;
			node.y += velY + GRAVITY;
			
			// set the old position to the position we had before applying forces
			node.oldX = tempX;
			node.oldY = tempY;
		}
	}

	// simulate all constraints
	void simulateConstraints()
	{
		for(int i = 0; i < constraints.size(); i++)
		{
			Constraint constraint = constraints.get(i);
			
			// calculate the difference in the nodes each axis
			double diffX = constraint.nodeA.x - constraint.nodeB.x;
			double diffY = constraint.nodeA.y - constraint.nodeB.y;
			
			// calculate the current distance between the nodes
			double distance = Math.sqrt(diffX * diffX + diffY * diffY);
			
			// break the constraint if it is too long
			if(distance > constraint.length * (CONSTRAINT_BREAKPOINT + 1)) {
				constraints.remove(i);
				i--;
				continue;
			}
			
			// calculate the difference between the target length and the current distance
			double diff = constraint.length - distance;
			
			// get the percent difference the nodes are off by (/2 because of 2 nodes)
			double percent = diff / distance / 2;
			
			// if they are fixed, revert the percent back to normal
			if(constraint.nodeA.fixed || constraint.nodeB.fixed)
			{
				percent *= 2;
			}
			
			// multiply the percent by the constraint strength
			percent *= CONSTRAINT_STRENGTH;
			
			// calculate the offset required for the nodes to get them to the optimal position
			double offsetX = diffX * percent;
			double offsetY = diffY * percent;
			
			// apply the offset to nodeA if it is not fixed
			if(!constraint.nodeA.fixed)
			{
				constraint.nodeA.x += offsetX;
				constraint.nodeA.y += offsetY;
			}
			
			// apply the offset to nodeB if it is not fixed
			if(!constraint.nodeB.fixed)
			{
				constraint.nodeB.x -= offsetX;			
				constraint.nodeB.y -= offsetY;
			}
		}
	}

	// simulate collision
	void simulateCollision()
	{
		// calculate the bounds for the walls
		double leftBound = SIDE_PADDING;
		double rightBound = width - SIDE_PADDING * 4;
		double topBound = SIDE_PADDING;
		double bottomBound = height - SIDE_PADDING;

		for(int i = 0; i < nodes.size(); i++)
		{
			Node node = nodes.get(i);
			
			// skip the node if it's fixed
			if(node.fixed)
				continue;
			
			// get the velocity of the node
			double velX = node.x - node.oldX;
			double velY = node.y - node.oldY;
			
			// handle collision for the top bound
			if(node.y < topBound)
			{
				node.y = topBound;
				node.oldY = topBound + velY * COLLISION_DAMP;
			}
			
			// handle collision for the bottom bound
			if(node.y > bottomBound)
			{
				node.y = bottomBound;
				node.oldY = bottomBound + velY * COLLISION_DAMP;
				node.oldX = node.oldX + velX * 0.1 * COLLISION_DAMP; // friction
			}
			
			// handle collision for the right bound
			if(node.x > rightBound)
			{
				node.x = rightBound;
				node.oldX = rightBound + velX * COLLISION_DAMP;
			}
			
			// handle collision for the left bound
			if(node.x < leftBound)
			{
				node.x = leftBound;
				node.oldX = leftBound + velX * COLLISION_DAMP;
			}
			
			for(int c = 0; c < colliders.size(); c++)
			{
				Collider col = colliders.get(c);
				
				if(col.intersect(node.x, node.y))
				{
					col.rebound(node);
				}
			}
		}
	}
	
	// returns a random integer between the min and the max (exclusive)
	public static int randomInt(int min, int max)
	{
		Random random = new Random(System.currentTimeMillis());
		return random.nextInt(max - min) + min;
	}
}