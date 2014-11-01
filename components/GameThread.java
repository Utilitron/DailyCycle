package components;

import java.util.Calendar;

import components.ai.AiObject;
import components.ai.Npc;

// Run the game logic in its own thread.
public class GameThread extends Thread {

	GraphicsFrame graphicsFrame;
	
	//1 minute
	public long dayLength = 1000 * 60 * 2;
	public long lastDayStart;
	
	public GameThread(GraphicsFrame graphicsFrame) {
		this.graphicsFrame = graphicsFrame;
	}

	/** Start */
	public void gameStart() {
		this.start(); // Invoke GaemThread.run()
	}

	public void run() {
		while (true) {
			long beginTimeMillis, timeTakenMillis, timeLeftMillis;
			beginTimeMillis = System.currentTimeMillis();
	
			// Execute one game step
			gameUpdate();
			
			// Refresh the display
			graphicsFrame.repaint();
	
	
			// Provide the necessary delay to meet the target rate
			timeTakenMillis = System.currentTimeMillis() - beginTimeMillis;
			timeLeftMillis = 1000L / GraphicsFrame.UPDATE_RATE - timeTakenMillis;
			if (timeLeftMillis < 5)
				timeLeftMillis = 5; // Set a minimum
	
			// Delay and give other thread a chance
			try {
				Thread.sleep(timeLeftMillis);
			} catch (InterruptedException ex) {
			}
		}
	}
	
	/**
	 * Set up the game
	 */
	public void gameSetup() {
		lastDayStart = Calendar.getInstance().getTimeInMillis();
		graphicsFrame.aiObjects.add(new Npc(dayLength, GraphicsFrame.UPDATE_RATE));
	}
	
	/**
	 * One game time-step. Update the game objects, with proper collision
	 * detection and response.
	 */
	public void gameUpdate() {
		boolean newday = false;
		long now = Calendar.getInstance().getTimeInMillis();
		if (now > (lastDayStart + dayLength)){
			lastDayStart = now;
			newday = true;
		}
		
		for (AiObject ai : graphicsFrame.aiObjects){
			ai.checkTasks();
			ai.updateTasks((lastDayStart + dayLength - now));
			ai.updateStates();
			if (newday)
				ai.addDailyTasks();

		}
	}
}
