package shapes.polygon;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

public class Triangle extends Polygon {

	protected static final int VERTIX_NUMBER = 3;

	public Triangle() {
	}

	public Triangle(Point theCenter, int frameWidth, Color frameColor, Color fillColor) {
		super(theCenter, frameWidth, frameColor, fillColor);
	}

	public Triangle(Point theCenter, List<Point> points, int frameWidth, Color frameColor, Color fillColor) {
		super(theCenter, points, frameWidth, frameColor, fillColor);
	}

	public void addPoint(Point pt) {
		if ((getnPoints() >= getxPoints().length || getnPoints() >= getyPoints().length)
				&& getnPoints() < VERTIX_NUMBER) {
			int newLength = getnPoints() * 2;
			if (newLength < MIN_LENGTH) {
				newLength = MIN_LENGTH;
			} else if ((newLength & (newLength - 1)) != 0) {
				newLength = Integer.highestOneBit(newLength);
			}
			setxPoints(Arrays.copyOf(getxPoints(), newLength));
			setyPoints(Arrays.copyOf(getyPoints(), newLength));
		}
		if (getnPoints() <= VERTIX_NUMBER) {
			getxPoints()[getnPoints()] = pt.x;
			getyPoints()[getnPoints()] = pt.y;
			setnPoints(getnPoints() + 1);
		}
		setLocation(computeCenter());
	}

	private Point computeCenter() {
		Point centroid = new Point(0, 0);
		double signedArea = 0.0;
		double x0; // Current vertex X
		double y0; // Current vertex Y
		double x1; // Next vertex X
		double y1; // Next vertex Y
		double a; // Partial signed area

		for (int i = 0; i < getnPoints() - 1; ++i) {
			x0 = getxPoints()[i];
			y0 = getyPoints()[i];
			x1 = getxPoints()[i + 1];
			y1 = getyPoints()[i + 1];
			a = x0 * y1 - x1 * y0;
			signedArea += a;
			centroid.x += (x0 + x1) * a;
			centroid.y += (y0 + y1) * a;
		}

		x0 = getxPoints()[getnPoints() - 1];
		y0 = getyPoints()[getnPoints() - 1];
		x1 = getxPoints()[0];
		y1 = getyPoints()[0];
		a = x0 * y1 - x1 * y0;
		signedArea += a;
		centroid.x += (x0 + x1) * a;
		centroid.y += (y0 + y1) * a;

		signedArea *= 0.5;
		centroid.x /= (6.0 * signedArea);
		centroid.y /= (6.0 * signedArea);

		return centroid;
	}

}
