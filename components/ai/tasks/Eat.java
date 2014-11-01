package components.ai.tasks;

import java.util.Calendar;
import java.util.List;

import components.ai.AiObject;
import components.ai.attributes.NeedsToEat;


public class Eat implements Task {

	/* 1 minute */
	public static final int durration = 1000 * 5;
	
	public boolean canDoTask(AiObject ai) {
		boolean canDo = false;
		if (!ai.isBusy()){
			if (ai instanceof NeedsToEat){
				NeedsToEat nteAi = (NeedsToEat) ai;
			
				canDo = nteAi.isHungry();
			}
		}
		return canDo;
	}

	public void doTask(AiObject ai) {
		NeedsToEat csAi;
		
		if (ai instanceof NeedsToEat){
			csAi = (NeedsToEat) ai;
			long now = Calendar.getInstance().getTimeInMillis();
			csAi.eat(now + durration);
		}
	}


	public static boolean taskExists(List<Task> tasklist) {
		boolean foundTask = false;
		for (Task task : tasklist){
			if (task instanceof Eat){ 
				foundTask = true;
				break;
			}
		}
		return foundTask;
	}
	
	public static void removeTask(List<Task> tasklist) {
		Eat oldTask = null;
		//Remove a Task
		for (Task task : tasklist){
			if (task instanceof Eat)
					
				oldTask = (Eat) task;
		}
		tasklist.remove(oldTask);
	}
	
	@Override
	public String toString() {
		return "[Eat]";
	}

}
