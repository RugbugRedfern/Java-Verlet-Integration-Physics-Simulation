import java.util.*;

public class Slopes extends Simulation implements CollisionTriggerListener
{
	static final int WIDTH = 1500;
	static final int HEIGHT = 750;

	public static void main(String[] args) throws InterruptedException
	{
		Slopes app = new Slopes();
	}

	public Slopes() throws InterruptedException
	{
		super(WIDTH, HEIGHT);

		Random random = new Random();

		for(int x = 100; x < WIDTH - 100; x += 1)
		{
			nodes.add(new Node(x + random.nextInt(20) - 10, 10 + random.nextInt(90)));
		}

		colliders.add(new BoxCollider(900, 200, 1200, 10, -10));
		colliders.add(new BoxCollider(500, 500, 1300, 10, 10));
		colliders.add(new BoxCollider(310, 390, 10, 120, 0));

		Collider bottom = new BoxCollider(WIDTH / 2, HEIGHT - 5, WIDTH, 10, 0);
		bottom.addTriggerListener(this);
		colliders.add(bottom);

		// main simulation loop
		while(true)
		{
			simulate();
			repaint();
			Thread.sleep(5);
		}
	}

	public void onCollisionTriggered(Collider col, Node node)
	{
		double vel = node.oldY - node.y;
		node.y = 5;
		node.oldY = node.y + vel;
	}
}