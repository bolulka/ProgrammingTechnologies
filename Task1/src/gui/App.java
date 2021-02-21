package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import base.*;
import base.Shape;
import shapes.line.*;
import shapes.polygon.*;
import shapes.polygon.Polygon;
import shapes.squareness.*;

enum DrawAction {
	MOVE, SQUARENESS, ELLIPSE, REGULAR_POLYGON, SEGMENT, RAY, LINE, POLYGON, UPDATE_POLYGON, PARALLELOGRAM, RHOMBUS,
	POLYLINE, UPDATE_POLYLINE, TRIANGLE, UPDATE_TRIANGLE
}

public class App extends JFrame {

	private JPanel rootPanel = new JPanel();
	private JPanel drawPanel;
	private JButton squarenessButton, ellipseButton, regularPolygonButton, segmentButton, lineButton, rayButton,
			polygonButton, parallelogramButton, rhombusButton, polylineButton, triangleButton;
	private JButton moveShapesButton;
	private JSlider redSlider, greenSlider, blueSlider;
	private JButton frameColorButton, fillColorButton;
	private JLabel label;
	private JButton colorLabel;
	private RegularPolygonDialog sideNumDialog;
	private JComboBox<Icon> widthComboBox;
	private JCheckBox transparencyCheckBox;
	private ArrayList<Shape> shapes = new ArrayList<>();
	private DrawAction drawAction = DrawAction.MOVE;
	private int frameWidth = 1;
	private Color chosenColor;
	private Color frameColor = new Color(0, 0, 0);
	private Color fillColor = new Color(255, 255, 255);
	private boolean isDragged = false;

	private final int BUTTON_WIDTH = 200;
	private final int BUTTON_HEIGHT = 50;
	private final int FRAME_WIDTH = 720;
	private final int FRAME_HEIGHT = 560;
	private String originalStr = "Chose shape";

	public App() {
		super("Just Draw It");
		sideNumDialog = new RegularPolygonDialog(this);
		setContentPane(rootPanel);
		setUpGUI();
		setSize(1400, 750);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void setUpGUI() {
		createUIComponents();
		initButtons();
		rhombusButton.addActionListener(e -> {
			drawAction = DrawAction.RHOMBUS;
			label.setText("Rhombus");
		});
		parallelogramButton.addActionListener(e -> {
			drawAction = DrawAction.PARALLELOGRAM;
			label.setText("Parallelogram");
		});
		polygonButton.addActionListener(e -> {
			drawAction = DrawAction.POLYGON;
			label.setText("Polygon");
		});
		lineButton.addActionListener(e -> {
			drawAction = DrawAction.LINE;
			label.setText("Line");
		});
		rayButton.addActionListener(e -> {
			drawAction = DrawAction.RAY;
			label.setText("Ray");
		});
		segmentButton.addActionListener(e -> {
			drawAction = DrawAction.SEGMENT;
			label.setText("Segment");
		});
		regularPolygonButton.addActionListener(e -> {
			drawAction = DrawAction.REGULAR_POLYGON;
			sideNumDialog.showDialog();
			label.setText("Regular polygon");
		});
		squarenessButton.addActionListener(e -> {
			drawAction = DrawAction.SQUARENESS;
			label.setText("Squareness");
		});
		ellipseButton.addActionListener(e -> {
			drawAction = DrawAction.ELLIPSE;
			label.setText("Ellipse");
		});
		polylineButton.addActionListener(e -> {
			drawAction = DrawAction.POLYLINE;
			label.setText("Polyline");
		});
		triangleButton.addActionListener(e -> {
			drawAction = DrawAction.TRIANGLE;
			label.setText("Triangle");
		});

		moveShapesButton.addActionListener(e -> {
			drawAction = DrawAction.MOVE;
			label.setText("MOVE");
		});

		ColorListener listener = new ColorListener();
		redSlider.addChangeListener(listener);
		greenSlider.addChangeListener(listener);
		blueSlider.addChangeListener(listener);

		drawPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				if (drawAction == DrawAction.MOVE)
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				else
					setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					switch (drawAction) {
					case MOVE:
						ListIterator<Shape> li = shapes.listIterator(shapes.size());
						while (li.hasPrevious()) {
							int prevIndex = li.previousIndex();
							if (li.previous().contains(e.getPoint())) {
								isDragged = true;
								shapes.add(shapes.remove(prevIndex));
								break;
							}
						}
						break;
					case SQUARENESS:
						shapes.add(new Squareness(e.getPoint(), e.getPoint(), frameWidth, frameColor, fillColor));
						break;
					case ELLIPSE:
						shapes.add(new Ellipse(e.getPoint(), e.getPoint(), frameWidth, frameColor, fillColor));
						break;
					case REGULAR_POLYGON:
						shapes.add(new RegularPolygon(e.getPoint(), e.getPoint(), sideNumDialog.getSideNum(),
								frameWidth, frameColor, fillColor));
						break;
					case SEGMENT:
						shapes.add(new Segment(e.getPoint(), e.getPoint(), frameWidth, frameColor));
						break;
					case POLYLINE:
						shapes.add(new Polyline(e.getPoint(), e.getPoint(), frameWidth, frameColor));
						drawAction = DrawAction.UPDATE_POLYLINE;
						break;
					case UPDATE_POLYLINE:
						Shape curShape = shapes.get(shapes.size() - 1);
						((Polyline) curShape).addPoint(e.getPoint());
						break;
					case RAY:
						shapes.add(new Ray(e.getPoint(), e.getPoint(), frameWidth, frameColor));
						break;
					case LINE:
						shapes.add(new Line(e.getPoint(), e.getPoint(), frameWidth, frameColor));
						break;
					case POLYGON:
						List<Point> points = new ArrayList<>();
						points.add(e.getPoint());
						points.add(e.getPoint());
						shapes.add(new Polygon(e.getPoint(), points, frameWidth, frameColor, fillColor));
						drawAction = DrawAction.UPDATE_POLYGON;
						break;
					case UPDATE_POLYGON:
						Shape currentShape = shapes.get(shapes.size() - 1);
						((Polygon) currentShape).addPoint(e.getPoint());
						break;
					case PARALLELOGRAM:
						shapes.add(new Parallelogram(e.getPoint(), e.getPoint(), frameWidth, frameColor, fillColor));
						break;
					case RHOMBUS:
						shapes.add(new Rhombus(e.getPoint(), e.getPoint(), frameWidth, frameColor, fillColor));
						break;
					case TRIANGLE:
						List<Point> pointsForTriangle = new ArrayList<>();
						pointsForTriangle.add(e.getPoint());
						pointsForTriangle.add(e.getPoint());
						shapes.add(new Triangle(e.getPoint(), pointsForTriangle, frameWidth, frameColor, fillColor));
						drawAction = DrawAction.UPDATE_TRIANGLE;
						break;
					case UPDATE_TRIANGLE:
						Shape currShape = shapes.get(shapes.size() - 1);
						((Triangle) currShape).addPoint(e.getPoint());
						break;
					}
					repaint();
				} else if (SwingUtilities.isRightMouseButton(e)) {
					if (drawAction == DrawAction.UPDATE_POLYGON) {
						drawAction = DrawAction.POLYGON;
					}
					if (drawAction == DrawAction.UPDATE_TRIANGLE) {
						drawAction = DrawAction.TRIANGLE;
					}
					if (drawAction == DrawAction.UPDATE_POLYLINE) {
						drawAction = DrawAction.POLYLINE;
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (drawAction == DrawAction.MOVE)
					isDragged = false;
			}

		});

		drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && shapes.size() > 0) {
					Shape currentShape = shapes.get(shapes.size() - 1);
					switch (drawAction) {
					case MOVE:
						if (isDragged)
							currentShape.move(e.getPoint());
						break;
					case SQUARENESS:
						if (e.isShiftDown()) {
							if (currentShape.getClass() != RegularPolygon.class) {
								RegularPolygon square = new RegularPolygon(currentShape.getLocation(), e.getPoint(), 4,
										currentShape.getFrameWidth(), currentShape.getFrameColor(),
										((ClosedShape) currentShape).getFillColor());
								square.setRotating(false);
								shapes.set(shapes.size() - 1, square);
							} else
								((RegularPolygon) currentShape).setPointOnCircle(e.getPoint());
						} else {
							if (currentShape.getClass() != Squareness.class) {
								Squareness squareness = new Squareness(currentShape.getLocation(), e.getPoint(),
										currentShape.getFrameWidth(), currentShape.getFrameColor(),
										((ClosedShape) currentShape).getFillColor());
								shapes.set(shapes.size() - 1, squareness);
							} else
								((Squareness) currentShape).setCornerPoint(e.getPoint());
						}
						break;
					case ELLIPSE:
						Ellipse ellipse = (Ellipse) currentShape;
						ellipse.setCornerPoint(e.getPoint());
						if (e.isShiftDown() && currentShape.getClass() != Circle.class) {
							ellipse = new Circle(ellipse.getLocation(), ellipse.getCornerPoint(),
									ellipse.getFrameWidth(), ellipse.getFrameColor(), ellipse.getFillColor());
							shapes.set(shapes.size() - 1, ellipse);
						} else if (!e.isShiftDown() && currentShape.getClass() != Ellipse.class)
							ellipse = new Ellipse(ellipse.getLocation(), ellipse.getCornerPoint(),
									ellipse.getFrameWidth(), ellipse.getFrameColor(), ellipse.getFillColor());
						shapes.set(shapes.size() - 1, ellipse);
						break;
					case REGULAR_POLYGON:
						RegularPolygon polygon = (RegularPolygon) currentShape;
						polygon.setPointOnCircle(e.getPoint());
						if (e.isShiftDown() && polygon.isRotating())
							polygon.setRotating(false);
						else if (!e.isShiftDown() && !polygon.isRotating())
							polygon.setRotating(true);
						break;
					case SEGMENT:
					case RAY:
					case LINE:
						Segment segment = (Segment) currentShape;
						if (e.isShiftDown())
							segment.setEndPoint(e.getPoint(), true);
						else
							segment.setEndPoint(e.getPoint());
						break;
					case UPDATE_POLYGON:
						((Polygon) currentShape).setLastPoint(e.getPoint());
						break;
					case PARALLELOGRAM:
					case RHOMBUS:
						((Parallelogram) currentShape).setCornerPoint(e.getPoint());
						break;
					}
					repaint();
				}
			}
		});

		frameColorButton.addActionListener(e -> {
			getSliderColor();
			frameColor = chosenColor;
			frameColorButton.setBackground(frameColor);
			validate();
		});
		fillColorButton.addActionListener(e -> {
			getSliderColor();
			fillColor = chosenColor;
			fillColorButton.setBackground(fillColor);
			validate();
		});

		transparencyCheckBox.addItemListener(e -> {
			boolean isChecked = e.getStateChange() == ItemEvent.SELECTED;
			fillColor = new Color(fillColor.getRGB() & 0x00ffffff, isChecked);
			fillColorButton.setEnabled(!isChecked);
		});

		widthComboBox.addActionListener(e -> frameWidth = (int) Math.pow(2, widthComboBox.getSelectedIndex()));
		
		addButtonsToRootPanel();

	}

	private class ColorListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			if (e.getSource().getClass().equals(JSlider.class)) {
				getSliderColor();
				colorLabel.setBackground(chosenColor);
			}
		}
	}

	private void getSliderColor() {
		chosenColor = new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
	}

	private void createUIComponents() {
		drawPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				for (Shape s : shapes)
					s.draw(g2d);
			}
		};
	}

	private void initButtons() {

		squarenessButton = new JButton("squareness");
		squarenessButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		ellipseButton = new JButton("ellipse");
		ellipseButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		regularPolygonButton = new JButton("regular polygon");
		regularPolygonButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		segmentButton = new JButton("segment");
		segmentButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		lineButton = new JButton("line");
		lineButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		rayButton = new JButton("ray");
		rayButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		polygonButton = new JButton("polygon");
		polygonButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		parallelogramButton = new JButton("parallelogram");
		parallelogramButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		rhombusButton = new JButton("rhumbus");
		rhombusButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		polylineButton = new JButton("polyline");
		polylineButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		triangleButton = new JButton("triangle");
		triangleButton.setFont(new Font("Verdana", Font.PLAIN, 18));

		moveShapesButton = new JButton("move shape");
		moveShapesButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		transparencyCheckBox = new JCheckBox();

		frameColorButton = new JButton("frame color");
		frameColorButton.setFont(new Font("Verdana", Font.PLAIN, 18));
		fillColorButton = new JButton("fill color");
		fillColorButton.setFont(new Font("Verdana", Font.PLAIN, 18));

		redSlider = new JSlider(0, 255);
		greenSlider = new JSlider(0, 255);
		blueSlider = new JSlider(0, 255);
		redSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED), "Red"));
		greenSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GREEN), "Green"));
		blueSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Blue"));

		widthComboBox = new JComboBox<Icon>();
		widthComboBox.addItem(new ImageIcon(getClass().getResource("/resources/line_width_1.png")));
		widthComboBox.addItem(new ImageIcon(getClass().getResource("/resources/line_width_2.png")));
		widthComboBox.addItem(new ImageIcon(getClass().getResource("/resources/line_width_3.png")));
		widthComboBox.addItem(new ImageIcon(getClass().getResource("/resources/line_width_4.png")));

		label = new JLabel(originalStr);
		label.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		label.setFont(new Font("Verdana", Font.PLAIN, 25));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);

		colorLabel = new JButton();
		colorLabel.setBackground(fillColor);
		colorLabel.setEnabled(false);
	}

	public void addButtonsToRootPanel() {

		rootPanel.setLayout(new FlowLayout());

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		JPanel slidersPanel = new JPanel();
		slidersPanel.setLayout(new FlowLayout());

		slidersPanel.add(redSlider);
		slidersPanel.add(greenSlider);
		slidersPanel.add(blueSlider);
		slidersPanel.add(widthComboBox);

		redSlider.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 15));
		greenSlider.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 15));
		blueSlider.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 15));
		widthComboBox.setPreferredSize(new Dimension(BUTTON_WIDTH - 15, BUTTON_HEIGHT + 15));

		drawPanel.setLayout(new BorderLayout());
		drawPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		drawPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black)));
		drawPanel.setBackground(Color.WHITE);
		drawPanel.add(colorLabel, BorderLayout.AFTER_LINE_ENDS);
		colorLabel.setPreferredSize(new Dimension(25, 50));

		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new FlowLayout());

		optionsPanel.add(fillColorButton);
		optionsPanel.add(frameColorButton);
		optionsPanel.add(moveShapesButton);
		optionsPanel.add(transparencyCheckBox);

		fillColorButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		frameColorButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		moveShapesButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		transparencyCheckBox.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

		rightPanel.add(slidersPanel, BorderLayout.NORTH);
		rightPanel.add(drawPanel, BorderLayout.CENTER);
		rightPanel.add(optionsPanel, BorderLayout.SOUTH);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(6, 2, 10, 10));

		buttonPanel.add(squarenessButton);
		buttonPanel.add(ellipseButton);
		buttonPanel.add(lineButton);
		buttonPanel.add(rayButton);
		buttonPanel.add(segmentButton);
		buttonPanel.add(polylineButton);
		buttonPanel.add(parallelogramButton);
		buttonPanel.add(rhombusButton);
		buttonPanel.add(polygonButton);
		buttonPanel.add(regularPolygonButton);
		buttonPanel.add(triangleButton);

		squarenessButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 20));
		ellipseButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 20));
		regularPolygonButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 20));
		segmentButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 20));
		lineButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 20));
		rayButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 20));
		polygonButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 20));
		parallelogramButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 20));
		rhombusButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 20));
		polylineButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 20));
		triangleButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT + 20));

		leftPanel.add(buttonPanel);
		leftPanel.add(Box.createVerticalStrut(BUTTON_HEIGHT / 2));
		leftPanel.add(label);

		rootPanel.add(leftPanel);
		rootPanel.add(rightPanel);
	}

}
