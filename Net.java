import java.awt.*;
import javax.swing.*;

public class Net extends Simulation implements Painter
{
	static final int WIDTH = 1500;
	static final int HEIGHT = 750;

	static final int NET_WIDTH = 41;
	static final int NET_HEIGHT = 41;

	public static final int MOUSE_RANGE = 15; // radius in pixels of the mouse destruction

	// the position of the mouse
	int mouseX;
	int mouseY;

	public static void main(String[] args) throws InterruptedException
	{
		Net net = new Net();
	}

	public Net() throws InterruptedException
	{
		super(WIDTH, HEIGHT);

		setPainter(this);
		
		generateNet();

		colliders.add(new BoxCollider(1100, 600, 500, 20, -5));

		// main simulation loop
		while(true)
		{
			destroyWithMouse();
			
			simulate();
			repaint();
			Thread.sleep(1);
		}
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(Color.RED);
		g.fillOval(mouseX - 3, mouseY - 3, 6, 6);
		g.drawOval(mouseX - 15, mouseY - 15, 30, 30);
		g.setColor(Color.BLACK);
	}

		void generateNet()
	{
		// generate the nodes
		for(int x = 0; x < NET_WIDTH; x++)
		{
			for(int y = 0; y < NET_HEIGHT; y++)
			{
				Node node = new Node(50 + x * 20, 30 + y * 20);
				if(y == 0 && x % 8 == 0)
					node.fixed = true;
					
				nodes.add(node);
			}
		}
		
		// generate the constraints
		for(int x = 0; x < NET_WIDTH; x++)
		{
			for(int y = 0; y < NET_HEIGHT; y++)
			{
				Node currentNode = nodes.get(x + NET_WIDTH * y);
				
				// generate the vertical constraint
				if(y < NET_HEIGHT - 1)
				{
					Node bottomNode = nodes.get(x + NET_WIDTH * (y + 1));
					constraints.add(new Constraint(currentNode, bottomNode));
				}
				
				// generate the horizontal constraint
				if(x < NET_WIDTH - 1)
				{
					Node rightNode = nodes.get((x + 1) + NET_WIDTH * y);
					constraints.add(new Constraint(currentNode, rightNode));
				}
			}
		}
	}

	void destroyWithMouse()
	{
		java.awt.Point screenPoint = getLocationOnScreen();
		java.awt.Point mousePoint = MouseInfo.getPointerInfo().getLocation();

		mouseX = (int)Math.round(mousePoint.getX() - screenPoint.getX() - 8);
		mouseY = (int)Math.round(mousePoint.getY() - screenPoint.getY() - 30);

		for(int i = 0; i < constraints.size(); i++)
		{
			Constraint constraint = constraints.get(i);

			// calculate the center of both nodes
			double centerX = (constraint.nodeA.x + constraint.nodeB.x) / 2;
			double centerY = (constraint.nodeA.y + constraint.nodeB.y) / 2;

			// if the constraint is within the mouse range, break it
			if(mouseX > centerX - MOUSE_RANGE &&
				mouseX < centerX + MOUSE_RANGE &&
				mouseY > centerY - MOUSE_RANGE &&
				mouseY < centerY + MOUSE_RANGE)
			{
				constraints.remove(i);
				i--;
				continue;
			}
		}
	}
}