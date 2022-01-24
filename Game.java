import java.util.*;

public class Game extends Simulation implements CollisionTriggerListener
{
	static final int WIDTH = 1500;
	static final int HEIGHT = 750;

	public static void main(String[] args) throws InterruptedException
	{
		Game app = new Game();
	}

	public Game() throws InterruptedException
	{
		super(WIDTH, HEIGHT);

		Random random = new Random();

		for(int x = 100; x < WIDTH - 100; x += 1)
		{
			Node node = new Node(x + randomInt(random, -10, 10), 100 + randomInt(random, -90, 0));
			nodes.add(node);
		}

		for(int x = 0; x < WIDTH; x += 25)
		{
			for(int y = 200; y < HEIGHT - 100; y += 25)
			{
				Collider col = new CircleCollider(x + (y % 2 == 0 ? 0 : 12) + randomInt(random, -5, 5), y, randomInt(random, 3, 14));
				colliders.add(col);
			}
		}

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

	// returns a random integer between the min and the max based on a seed (exclusive)
	public static int randomInt(Random random, int min, int max)
	{
		return random.nextInt(max - min) + min;
	}
}