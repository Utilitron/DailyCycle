package components.ai.tasks;

import java.util.List;


public class EatSnack extends Eat implements Task{
	
	
	/* 1 minute */
	public static final int durration = 1000 * 2;
	

	public static boolean taskExists(List<Task> tasklist) {
		boolean foundTask = false;
		for (Task task : tasklist){
			if (task instanceof EatSnack){ 
				foundTask = true;
				break;
			}
		}
		return foundTask;
	}
	
	public static void removeTask(List<Task> tasklist) {
		EatSnack oldTask = null;
		//Remove a Task
		for (Task task : tasklist){
			if (task instanceof EatSnack)
					
				oldTask = (EatSnack) task;
		}
		tasklist.remove(oldTask);
	}

	@Override
	public String toString() {
		return "[Eat Snack]";
	}

}
