package components.ai.tasks;

import components.ai.AiObject;

public interface Task {

	public boolean canDoTask(AiObject ai);
	
	public void doTask(AiObject ai);

}
