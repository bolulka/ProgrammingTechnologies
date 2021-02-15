package base;

import java.awt.*;

public abstract class Shape {

	private Point theCenter;
	private Color frameColor = new Color(0, 0, 0);
	private int frameWidth = 1;

	public Shape() {
	}

	public Shape(Point theCenter) {
		this.theCenter = theCenter;
	}

	public Shape(Point theCenter, int frameWidth, Color frameColor) {
		super();
		this.theCenter = theCenter;
		this.frameColor = frameColor;
		this.frameWidth = frameWidth;
	}

	public void move(Point pt) {
		theCenter.setLocation(pt);
	}

	public abstract void draw(Graphics2D g);

	public abstract boolean contains(Point pt);
	
	public Point getLocation() {
        return theCenter;
    }

    public void setLocation(Point theCenter) {
        this.theCenter=theCenter;
    }

	public Point getTheCenter() {
		return theCenter;
	}

	public void setTheCenter(Point theCenter) {
		this.theCenter = theCenter;
	}

	public Color getFrameColor() {
		return frameColor;
	}

	public void setFrameColor(Color frameColor) {
		this.frameColor = frameColor;
	}

	public int getFrameWidth() {
		return frameWidth;
	}

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

}
