package shapes.line;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Polyline extends Segment {

	private int len;
	private List<Segment> segments;

	public Polyline() {
	}

	public Polyline(Point theCenter, Point endPoint, int frameWidth, Color frameColor) {
		super(theCenter, endPoint, frameWidth, frameColor);
		segments = new ArrayList<>();
		segments.add(new Segment(theCenter, endPoint, frameWidth, frameColor));
	}

	@Override
	public void draw(Graphics2D g) {
		for (int i = 0; i < getLen(); i++) {
			g.setStroke(new BasicStroke(getFrameWidth()));
			g.setColor(getFrameColor());
			Segment segment = segments.get(i);
			g.drawLine(segment.getTheCenter().x, segment.getTheCenter().y, segment.getEndPoint().x,
					segment.getEndPoint().y);
		}
	}

	@Override
	public boolean contains(Point pt) {
		int a, b;
		double d = 0;
		for (int i = 0; i < getLen(); i++) {
			Point endPoint = segments.get(i).getEndPoint();
			Point theCenter = segments.get(i).getTheCenter();
			a = endPoint.y - theCenter.y;
			b = endPoint.x - theCenter.x;
			d = (a * pt.x - b * pt.y + b * theCenter.y - a * theCenter.x) / (Math.sqrt(a * a + b * b));
		}
		return Math.abs(d) < getFrameWidth() / 2;
	}

	@Override
	public void move(Point pt) {
		for (int i = 0; i < getLen() - 1; ++i) {
			Segment segm = segments.get(i);
//			Point theCenter = segm.getTheCenter();
//			Point endPoint = segm.getEndPoint();
//			setEndPoint(new Point(endPoint.x + pt.x - theCenter.x, endPoint.y + pt.y - theCenter.y));
			segm.move(pt);  
		}
		
	}

	public void addPoint(Point pt) {
		segments.add(
				new Segment(segments.get(segments.size() - 1).getEndPoint(), pt, getFrameWidth(), getFrameColor()));
	}

	public List<Segment> getSegments() {
		return segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	public int getLen() {

		return segments.size();
	}

	public void setLen(int len) {
		this.len = len;
	}

}
