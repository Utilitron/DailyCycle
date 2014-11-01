package components.ai.tasks;

import java.util.Calendar;
import java.util.List;

import components.ai.AiObject;
import components.ai.attributes.NeedsToDrink;


public class Drink implements Task {

	/* 1 minute */
	public static final int durration = 1000 * 2;
	
	public boolean canDoTask(AiObject ai) {
		boolean canDo = false;
		
		if (!ai.isBusy()){
			if (ai instanceof NeedsToDrink){
				NeedsToDrink ntdAi = (NeedsToDrink) ai;
			
				canDo = ntdAi.isThirsty();
			}
		}
	
		return canDo;
	}

	public void doTask(AiObject ai) {
		if (ai instanceof NeedsToDrink){
			NeedsToDrink ntdAi = (NeedsToDrink) ai;
			long now = Calendar.getInstance().getTimeInMillis();
			ntdAi.drink(now + durration);
		}
	}
	

	public static boolean taskExists(List<Task> tasklist) {
		boolean foundTask = false;
		for (Task task : tasklist){
			if (task instanceof Drink){ 
				foundTask = true;
				break;
			}
		}
		return foundTask;
	}
	
	public static void removeTask(List<Task> tasklist) {
		Drink oldTask = null;
		//Remove a Task
		for (Task task : tasklist){
			if (task instanceof Drink)
					
				oldTask = (Drink) task;
		}
		tasklist.remove(oldTask);
	}

	@Override
	public String toString() {
		return "[Drink]";
	}

}
