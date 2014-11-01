package components;
import java.awt.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JPanel;

import components.ai.AiObject;
import components.graphics.GraphicsObject;

/**
 * The control logic and main display panel for game.
 */
public class GraphicsFrame extends JPanel {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	public final static int UPDATE_RATE = 30; // Frames per second (fps)

	private GameThread gameThread; // The thread that rns the game logic
	private ContainerBox[] box = new ContainerBox[2]; // The container rectangular box
	private int canvasWidth;
	private int canvasHeight;
	private int infoHeight = 80;

	public List<GraphicsObject> graphicsObjects = new ArrayList<GraphicsObject>();
	public List<AiObject> aiObjects = new ArrayList<AiObject>();
	
	/**
	 * Constructor to create the UI components and init the game objects. Set
	 * the drawing canvas to fill the screen (given its width and height).
	 * 
	 * @param width
	 *            : screen width
	 * @param height
	 *            : screen height
	 */
	public GraphicsFrame(int width, int height) {
		canvasWidth = width;
		canvasHeight = height - infoHeight;


		// Init the Container Box to fill the screen
		box[0] = new ContainerBox(0, 0, canvasWidth, canvasHeight, Color.BLACK, Color.WHITE);

		// Init the custom drawing panel for drawing the game
		DrawCanvas drawCanvas = new DrawCanvas();

		
		// Init the Container Box to fill the screen
		box[1] = new ContainerBox(0, 0, canvasWidth, infoHeight, Color.BLACK, Color.WHITE);
		
		// Init the custom info panel for drawing the game
		InfoCanvas infoCanvas = new InfoCanvas();
		
		// Layout the drawing panel and control panel
		this.setLayout(new BorderLayout());
		this.add(drawCanvas, BorderLayout.CENTER);
		this.add(infoCanvas, BorderLayout.SOUTH);
		
		// Setup and Start
		gameThread = new GameThread(this);
		gameThread.gameSetup();
		gameThread.gameStart();
	}

	/** The custom drawing panel for the game (inner class). */
	class DrawCanvas extends JPanel {

		/**
		 * Serial Version UID
		 */
		private static final long serialVersionUID = 1L;

		/** Custom drawing codes */
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g); // Paint background
			// Draw the box
			box[0].draw(g);
			for (GraphicsObject graphicsObject : graphicsObjects) 
				graphicsObject.draw(g);

		}

		/** Called back to get the preferred size of the component. */
		@Override
		public Dimension getPreferredSize() {
			return (new Dimension(canvasWidth, canvasHeight));
		}
	}
	
	/** The custom info panel for the game (inner class). */
	class InfoCanvas extends JPanel {

		/**
		 * Serial Version UID
		 */
		private static final long serialVersionUID = 1L;

		/** Custom drawing codes */
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g); // Paint background
			
			// Draw the box
			box[1].draw(g);
			
			// Display the information
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier New", Font.PLAIN, 12));
			
			long now = Calendar.getInstance().getTimeInMillis();
			g.drawString("time till tomorrow:" + ((gameThread.lastDayStart + gameThread.dayLength - now)/1000) + "(in seconds)", 10, 20);

			int lineNumber = 35;
			for (AiObject aiObject : aiObjects) {
				g.drawString(aiObject.toString(), 10, lineNumber);
				g.drawString(aiObject.tasksToString(), 10, lineNumber + 15);
				lineNumber += 30;
			}
		}

		/** Called back to get the preferred size of the component. */
		@Override
		public Dimension getPreferredSize() {
			return (new Dimension(canvasWidth, infoHeight));
		}
	}

}
