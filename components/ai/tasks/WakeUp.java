package components.ai.tasks;

import java.util.List;

import components.ai.AiObject;
import components.ai.attributes.CanSleep;

public class WakeUp implements Task {

	@Override
	public boolean canDoTask(AiObject ai) {
		boolean canDo = false;
		
		if (!ai.isBusy()){
			if (ai instanceof CanSleep){
				CanSleep csAi = (CanSleep) ai;
			
				canDo = !csAi.isAwake();
			}
		}
		return canDo;
	}

	@Override
	public void doTask(AiObject ai) {
		if (ai instanceof CanSleep){
			CanSleep csAi = (CanSleep) ai;

			csAi.wakeup();
		}
	}
	
	
	public static boolean taskExists(List<Task> tasklist) {
		boolean foundTask = false;
		for (Task task : tasklist){
			if (task instanceof WakeUp){ 
				foundTask = true;
				break;
			}
		}
		return foundTask;
	}
	
	public static void removeTask(List<Task> tasklist) {
		WakeUp oldTask = null;
		//Remove a Task
		for (Task task : tasklist){
			if (task instanceof WakeUp)
					
				oldTask = (WakeUp) task;
		}
		tasklist.remove(oldTask);
	}
	
	@Override
	public String toString() {
		return "[Wake Up]";
	}

}
