package snake;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A graphical view of the simulation grid. The view displays a colored
 * rectangle for each location representing its contents. It uses a default
 * background color. Colors for each type of species can be defined using the
 * setColor method.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 */
public class Gameview extends JFrame implements KeyListener {
	/**
	 * Provide a graphical view of a rectangular field. This is a nested class (a
	 * class defined inside a class) which defines a custom component for the user
	 * interface. This component displays the field. This is rather advanced GUI
	 * stuff - you can ignore this for your project if you like.
	 */
	private class FieldView extends JPanel {
		private final int GRID_VIEW_SCALING_FACTOR = 6;

		private int gridWidth, gridHeight;
		private int xScale, yScale;
		Dimension size;
		private Graphics g;
		private Image fieldImage;

		/**
		 * Create a new FieldView component.
		 */
		public FieldView(int height, int width) {
			gridHeight = height;
			gridWidth = width;
			size = new Dimension(0, 0);

		}

		/**
		 * Paint on grid location on this field in a given color.
		 */
		public void drawMark(int x, int y, Color color) {

			g.setColor(color);
			g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
		}

		/**
		 * Tell the GUI manager how big we would like to be.
		 */
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR, gridHeight * GRID_VIEW_SCALING_FACTOR);
		}

		/**
		 * The field view component needs to be redisplayed. Copy the internal image to
		 * screen.
		 */
		@Override
		public void paintComponent(Graphics g) {
			if (fieldImage != null) {
				Dimension currentSize = getSize();
				if (size.equals(currentSize)) {
					g.drawImage(fieldImage, 0, 0, null);
				} else {
					// Rescale the previous image.
					g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
				}
			}
		}

		/**
		 * Prepare for a new round of painting. Since the component may be resized,
		 * compute the scaling factor again.
		 */
		public void preparePaint() {
			if (!size.equals(getSize())) { // if the size has changed...
				size = getSize();
				fieldImage = fieldView.createImage(size.width, size.height);
				g = fieldImage.getGraphics();

				xScale = size.width / gridWidth;
				if (xScale < 1) {
					xScale = GRID_VIEW_SCALING_FACTOR;
				}
				yScale = size.height / gridHeight;
				if (yScale < 1) {
					yScale = GRID_VIEW_SCALING_FACTOR;
				}
			}
		}
	}

	// Colors used for empty locations.
	private static final Color EMPTY_COLOR = Color.white;

	private final String STEP_PREFIX = "Step: ";
	private final String POPULATION_PREFIX = "Population: ";
	private final String CONDITION_PREFIX = "It is ";
	private JLabel stepLabel, population, infoLabel;
	private JLabel conditionLabel;
	private FieldView fieldView;
	// A map for storing colors for participants in the simulation
	private Map<Class, Color> colors;

	// A statistics object computing and storing simulation information
	private FieldStats stats;

	/**
	 * Create a view of the given width and height.
	 *
	 * @param height
	 *            The simulation's height.
	 * @param width
	 *            The simulation's width.
	 */
	public Gameview(int height, int width) {
		stats = new FieldStats();
		colors = new LinkedHashMap<>();

		setTitle("Fox and Rabbit Simulation");
		stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
		infoLabel = new JLabel(" ", JLabel.CENTER);
		population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
		conditionLabel = new JLabel(CONDITION_PREFIX, JLabel.CENTER);
		setLocation(100, 50);

		fieldView = new FieldView(height, width);

		Container contents = getContentPane();
		addKeyListener(this);
		setFocusable(true);
		JPanel infoPane = new JPanel(new BorderLayout());
		infoPane.add(stepLabel, BorderLayout.WEST);
		infoPane.add(infoLabel, BorderLayout.CENTER);
		infoPane.add(conditionLabel, BorderLayout.CENTER);
		contents.add(infoPane, BorderLayout.NORTH);
		contents.add(fieldView, BorderLayout.CENTER);
		contents.add(population, BorderLayout.SOUTH);
		pack();
		setVisible(true);
	}

	private void arrowPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP && !SnakeGame.snake.getFirst().getCurrentDirection().equals("down")) {
			SnakeGame.snake.getFirst().setCurrentDirection("up");
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN
				&& !SnakeGame.snake.getFirst().getCurrentDirection().equals("up")) {
			SnakeGame.snake.getFirst().setCurrentDirection("down");
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT
				&& !SnakeGame.snake.getFirst().getCurrentDirection().equals("right")) {
			SnakeGame.snake.getFirst().setCurrentDirection("left");
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT
				&& !SnakeGame.snake.getFirst().getCurrentDirection().equals("left")) {
			SnakeGame.snake.getFirst().setCurrentDirection("right");
		}

	}

	private Color getColor(Class<? extends Object> objectClass) {
		// TODO Auto-generated method stub
		return colors.get(objectClass);
	}

	/**
	 * Determine whether the simulation should continue to run.
	 *
	 * @param field
	 * @return true If there is more than one species alive.
	 */
	public boolean isViable(Field field) {
		return stats.isViable(field);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		arrowPressed(e);

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Define a color to be used for a given class of animal.
	 *
	 * @param animalClass
	 *            The animal's Class object.
	 * @param color
	 *            The color to be used for the given class.
	 */
	public void setColor(Class animalClass, Color color) {
		colors.put(animalClass, color);
	}

	/**
	 * Display a short information label at the top of the window.
	 */
	public void setInfoText(String text) {
		infoLabel.setText(text);
	}

	/**
	 * Show the current status of the field.
	 *
	 * @param step
	 *            Which iteration step it is.
	 * @param field
	 *            The field whose status is to be displayed.
	 * @param isDay
	 */
	public void showStatus(Field field) {
		if (!isVisible()) {
			setVisible(true);
		}

		stepLabel.setText(STEP_PREFIX);
		stats.reset();

		fieldView.preparePaint();

		for (int row = 0; row < field.getDepth(); row++) {
			for (int col = 0; col < field.getWidth(); col++) {
				Object object = field.getObjectAt(row, col);
				if (object != null) {
					stats.incrementCount(object.getClass());
					fieldView.drawMark(col, row, getColor(object.getClass()));
				} else {
					fieldView.drawMark(col, row, EMPTY_COLOR);
				}
			}
		}
		stats.countFinished();

		population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
		fieldView.repaint();
	}
}
