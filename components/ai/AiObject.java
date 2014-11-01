package components.ai;

import components.ai.tasks.Task;

public interface AiObject {
	public String getStatus();
	public boolean isBusy();
	

	public void checkTasks();
	public void addTask(Task task);
	public void addDailyTasks();

	public void updateTasks(long timeleftinday);	
	

	public void updateStates();

	public String tasksToString();
	
}
