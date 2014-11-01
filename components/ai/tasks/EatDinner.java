package components.ai.tasks;

import java.util.List;

import components.ai.AiObject;
import components.ai.attributes.NeedsToDrink;
import components.ai.attributes.NeedsToEat;


public class EatDinner implements Task {
	private Task[] tasks = {new Eat(), new Drink()};
	
	@Override
	public boolean canDoTask(AiObject ai) {
		boolean canDo = false;
		if (!ai.isBusy()){
			if (ai instanceof NeedsToEat && ai instanceof NeedsToDrink){
				NeedsToEat nteAi = (NeedsToEat) ai;
				NeedsToDrink ntdAi = (NeedsToDrink) ai;

				canDo = nteAi.getHunger() > 0 && ntdAi.getThirst() > 0;
			}
		}
		return canDo;
	}

	@Override
	public void doTask(AiObject ai) {
		ai.addTask(tasks[0]);
		ai.addTask(tasks[1]);
	}
	

	public static boolean taskExists(List<Task> tasklist) {
		boolean foundTask = false;
		for (Task task : tasklist){
			if (task instanceof EatDinner){ 
				foundTask = true;
				break;
			}
		}
		return foundTask;
	}
	
	public static void removeTask(List<Task> tasklist) {
		EatDinner oldTask = null;
		//Remove a Task
		for (Task task : tasklist){
			if (task instanceof EatDinner)
					
				oldTask = (EatDinner) task;
		}
		tasklist.remove(oldTask);
	}
	
	@Override
	public String toString() {
		return "[Eat Dinner]";
	}
}
