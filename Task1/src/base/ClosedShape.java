package base;

import java.awt.*;

public abstract class ClosedShape extends Shape {

	private Color fillColor = new Color(255, 255, 255);;

	public ClosedShape() {
		super();
	}

	public ClosedShape(Point theCenter, int frameWidth,Color frameColor, Color fillColor) {
		super(theCenter,frameWidth, frameColor);
		this.fillColor = fillColor;
	}

	public ClosedShape(Point theCenter) {
		super(theCenter);
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

}
