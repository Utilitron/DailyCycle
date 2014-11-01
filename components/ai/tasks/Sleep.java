package components.ai.tasks;

import java.util.Calendar;
import java.util.List;

import components.ai.AiObject;
import components.ai.attributes.CanSleep;

public class Sleep implements Task {
	public static final int durration = (1000 * 60 * 5)/4;

	@Override
	public boolean canDoTask(AiObject ai) {
		boolean canDo = false;
		
		if (!ai.isBusy()){
			if (ai instanceof CanSleep){
				CanSleep csAi = (CanSleep) ai;
			
				canDo = csAi.isAwake() && csAi.isTired();
			}
		}
		return canDo;
	}
	
	@Override
	public void doTask(AiObject ai) {
		if (ai instanceof CanSleep){
			CanSleep csAi = (CanSleep) ai;
		
			long now = Calendar.getInstance().getTimeInMillis();
			csAi.sleep(now + durration );
		}
	
	}
	
	public static boolean taskExists(List<Task> tasklist) {
		boolean foundTask = false;
		for (Task task : tasklist){
			if (task instanceof Sleep){ 
				foundTask = true;
				break;
			}
		}
		return foundTask;
	}
	
	public static void removeTask(List<Task> tasklist) {
		Sleep oldTask = null;
		//Remove a Task
		for (Task task : tasklist){
			if (task instanceof Sleep)
					
				oldTask = (Sleep) task;
		}
		tasklist.remove(oldTask);
	}
	
	@Override
	public String toString() {
		return "[Sleep]";
	}

}
