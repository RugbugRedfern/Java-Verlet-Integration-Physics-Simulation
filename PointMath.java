public class PointMath
{
	// get the distance between two points
	public static double distance(double x1, double y1, double x2, double y2)
	{
		double a = x2 - x1;
		double b = y2 - y1;
		
		return Math.sqrt(a * a + b * b);
	}
	
	// get the magnitude of a vector
	public static double magnitude(double x, double y)
	{
		return distance(0, 0, x, y);
	}
	
	// https://academo.org/demos/rotation-about-point/
	// rotate a point around an origin by a rotation
	public static Point transformPoint(double originX, double originY, double pointX, double pointY, double degrees)
	{
		double radians = Math.toRadians(degrees);
		
		double x = pointX - originX;
		double y = pointY - originY;
		
		return new Point(
			x * Math.cos(radians) - y * Math.sin(radians) + originX,
			y * Math.cos(radians) + x * Math.sin(radians) + originY
		);
	}
}