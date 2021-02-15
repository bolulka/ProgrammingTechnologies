package shapes.polygon;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon extends base.ClosedShape {

	private int nPoints;
	private int[] xPoints;
	private int[] yPoints;

	protected static final int MIN_LENGTH = 4;

	public Polygon() {
	}

	public Polygon(Point theCenter, int frameWidth, Color frameColor, Color fillColor) {
		super(theCenter, frameWidth, frameColor, fillColor);
	}

	public Polygon(Point theCenter, List<Point> points, int frameWidth, Color frameColor, Color fillColor) {
		super(theCenter, frameWidth, frameColor, fillColor);
		setPoints(points);
	}

	public void setPoints(List<Point> points) {
		setnPoints(points.size());
		setxPoints(new int[getnPoints()]);
		setyPoints(new int[getnPoints()]);
		int i = 0;
		for (Point p : points) {
			getxPoints()[i] = p.x;
			getyPoints()[i++] = p.y;
		}
	}

	public List<Point> getPoints() {
		List<Point> points = new ArrayList<>(getnPoints());
		for (int i = 0; i < getnPoints(); ++i)
			points.add(new Point(getxPoints()[i], getyPoints()[i]));
		return points;
	}

	public int getPointsSize() {
		return getnPoints();
	}

	public void addPoint(Point pt) {
		if (getnPoints() >= getxPoints().length || getnPoints() >= getyPoints().length) {
			int newLength = getnPoints() * 2;
			if (newLength < MIN_LENGTH) {
				newLength = MIN_LENGTH;
			} else if ((newLength & (newLength - 1)) != 0) {
				newLength = Integer.highestOneBit(newLength);
			}
			setxPoints(Arrays.copyOf(getxPoints(), newLength));
			setyPoints(Arrays.copyOf(getyPoints(), newLength));
		}
		getxPoints()[getnPoints()] = pt.x;
		getyPoints()[getnPoints()] = pt.y;
		setnPoints(getnPoints() + 1);
		setLocation(computeCenter());
	}

	public void setLastPoint(Point pt) {
		getxPoints()[getnPoints() - 1] = pt.x;
		getyPoints()[getnPoints() - 1] = pt.y;
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

	@Override
	public void draw(Graphics2D g) {
		g.setStroke(new BasicStroke(getFrameWidth()));
		g.setColor(getFillColor());
		g.fillPolygon(getxPoints(), getyPoints(), getnPoints());
		g.setColor(getFrameColor());
		g.drawPolygon(getxPoints(), getyPoints(), getnPoints());
	}

	@Override
	public boolean contains(Point pt) {
		int hits = 0;

		int lastx = getxPoints()[getnPoints() - 1];
		int lasty = getyPoints()[getnPoints() - 1];
		int curx, cury;

		// Walk the edges of the polygon
		for (int i = 0; i < getnPoints(); lastx = curx, lasty = cury, i++) {
			curx = getxPoints()[i];
			cury = getyPoints()[i];

			if (cury == lasty) {
				continue;
			}

			int leftx;
			if (curx < lastx) {
				if (pt.x >= lastx) {
					continue;
				}
				leftx = curx;
			} else {
				if (pt.x >= curx) {
					continue;
				}
				leftx = lastx;
			}

			double test1, test2;
			if (cury < lasty) {
				if (pt.y < cury || pt.y >= lasty) {
					continue;
				}
				if (pt.x < leftx) {
					hits++;
					continue;
				}
				test1 = pt.x - curx;
				test2 = pt.y - cury;
			} else {
				if (pt.y < lasty || pt.y >= cury) {
					continue;
				}
				if (pt.x < leftx) {
					hits++;
					continue;
				}
				test1 = pt.x - lastx;
				test2 = pt.y - lasty;
			}

			if (test1 < (test2 / (lasty - cury) * (lastx - curx))) {
				hits++;
			}
		}
		return ((hits & 1) != 0);
	}

	@Override
	public void move(Point pt) {
		Point theCenter = getLocation();
		int deltaX = pt.x - theCenter.x;
		int deltaY = pt.y - theCenter.y;
		for (int i = 0; i < getnPoints(); i++) {
			getxPoints()[i] += deltaX;
			getyPoints()[i] += deltaY;
		}
		super.move(pt);
	}

	public int[] getxPoints() {
		return xPoints;
	}

	public void setxPoints(int[] xPoints) {
		this.xPoints = xPoints;
	}

	public int[] getyPoints() {
		return yPoints;
	}

	public void setyPoints(int[] yPoints) {
		this.yPoints = yPoints;
	}

	public int getnPoints() {
		return nPoints;
	}

	public void setnPoints(int nPoints) {
		this.nPoints = nPoints;
	}
}