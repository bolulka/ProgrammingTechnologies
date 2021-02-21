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
		Segment firstSegment = segments.get(0);
		firstSegment.move(pt);
		for (int i = 1; i < segments.size(); ++i) {
			segments.get(i).move(segments.get(i - 1).getEndPoint());
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
