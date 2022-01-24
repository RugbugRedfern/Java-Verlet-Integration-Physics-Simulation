public class Point
{
	// the position of the point
	public double x;
	public double y;

	// create a new point
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
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
}