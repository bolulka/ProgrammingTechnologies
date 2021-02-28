package shapes.line;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Polyline extends Segment {

	private List<Segment> segments;

	public Polyline() {
	}

	public Polyline(Point theCenter, Point endPoint, int frameWidth, Color frameColor) {
		super(theCenter, endPoint, frameWidth, frameColor);
		segments = new ArrayList<>();
	}

	@Override
	public void draw(Graphics2D g) {
		for (int i = 0; i < segments.size(); i++) {
			g.setStroke(new BasicStroke(getFrameWidth()));
			g.setColor(getFrameColor());
			Segment segment = segments.get(i);
			g.drawLine(segment.getTheCenter().x, segment.getTheCenter().y, segment.getEndPoint().x,
					segment.getEndPoint().y);
		}
	}

	@Override
	public boolean contains(Point pt) {
		for (int i = 0; i < segments.size(); i++) {
			if (segments.get(i).contains(pt)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void move(Point pt) {
		Segment middleSegment = segments.get(segments.size() / 2);
		middleSegment.move(pt);
		for (int i = segments.size() / 2 + 1; i < segments.size(); i++) {
			segments.get(i).move(segments.get(i - 1).getEndPoint());
		}
		for (int i = segments.size() / 2 - 1; i >= 0; i--) {
			Point newEndPt = segments.get(i + 1).getLocation();
			Point centPt = segments.get(i).getLocation();
			Point endPt = segments.get(i).getEndPoint();
			int deltaX = newEndPt.x - endPt.x;
			int deltaY = newEndPt.y - endPt.y;
			Point movePoint = new Point(centPt.x + deltaX, centPt.y + deltaY);
			segments.get(i).move(movePoint);
		}
	}

	public void addPoint(Point pt) {
		Point endPoint = segments.isEmpty() ? getTheCenter() : segments.get(segments.size() - 1).getEndPoint();
		segments.add(new Segment(endPoint, pt, getFrameWidth(), getFrameColor()));
	}

	public List<Segment> getSegments() {
		return segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

}
