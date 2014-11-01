package components.ai;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import components.ai.attributes.CanSleep;
import components.ai.attributes.NeedsToDrink;
import components.ai.attributes.NeedsToEat;
import components.ai.tasks.Drink;
import components.ai.tasks.Eat;
import components.ai.tasks.EatBreakfast;
import components.ai.tasks.EatDinner;
import components.ai.tasks.EatLunch;
import components.ai.tasks.EatSnack;
import components.ai.tasks.Sleep;
import components.ai.tasks.Task;
import components.ai.tasks.WakeUp;

public class Npc implements AiObject, CanSleep, NeedsToEat, NeedsToDrink {

	long dayLength;
	int timeOffset;
	
	public static final String STATUS_IDLE = "Idle"	;
	private String status = STATUS_IDLE;
	public void setStatus(String status) { this.status = status; }
	public String getStatus() { return status; }

	private boolean isBusy;
	public void setBusy(boolean isBusy) {
		if (!isBusy)
			status = "Idle";
		
		this.isBusy = isBusy; 
	}
	public boolean isBusy() { return isBusy; }
	
	private long busyUntil;
	
	private long energy = 0; 
	private void updateEnergy() {
		if (status != "Sleeping" && status != "Unconsious")
			this.energy -= 1;
		else 
			this.energy += 3;
		
		if (isAwake() && isUnconsious()){
			long now = Calendar.getInstance().getTimeInMillis();
			passout((long)( now + (-this.energy * timeOffset)));
		}
	}
	private boolean isAwake = true;
	private void setAwake(boolean isAwake) { this.isAwake = isAwake; }
	public boolean isAwake() { return this.isAwake; }

	public boolean isTired() { return this.energy <= 0 && this.energy >= -(dayLength/timeOffset)/2; }
	public boolean isUnconsious() { return this.energy <= -(dayLength/timeOffset)/2; }
	
	public void sleep(long l) {
		this.status = "Sleeping";

		this.setAwake(false);
		this.setBusy(true);
		busyUntil = l;
	}
	
	public void passout(long l) {
		this.status = "Unconsious";

		this.setAwake(false);
		this.setBusy(true);
		busyUntil = l;
	}
	
	@Override
	public void wakeup() {
		this.status = STATUS_IDLE;

		this.setAwake(true);
	}
	
	private long hunger = 0;
	public long getHunger(){ return hunger; }
	public boolean isHungry() { return this.hunger >= ((dayLength/timeOffset)/3) && this.hunger <= (dayLength/timeOffset); }
	public boolean isStarving() { return this.hunger >= (dayLength/timeOffset); }
	private void updateHunger() {
		if (status != "Eating"){
			this.hunger += 1;
		} else {
			if (this.hunger > ((dayLength/timeOffset)/3) / (Eat.durration/timeOffset)) 
				this.hunger -= ((dayLength/timeOffset)/3) / (Eat.durration/timeOffset);
			else 
				this.hunger = 0;
		}
		
		if (isAwake()){
			if ((isHungry() || isStarving()) && status != "Eating"){
				if (!Eat.taskExists(tasksToAdd)
				 && !Eat.taskExists(tasks)
				 && !EatSnack.taskExists(tasksToAdd)
				 && !EatSnack.taskExists(tasks))
					addTask(new EatSnack());
			} else if ((!isHungry() && !isStarving()) && status != "Eating"){
				if (EatSnack.taskExists(tasksToAdd))
					EatSnack.removeTask(tasksToAdd);
				if (EatSnack.taskExists(tasks))
					EatSnack.removeTask(tasks);
				if (Eat.taskExists(tasksToAdd))
					Eat.removeTask(tasksToAdd);
				if (Eat.taskExists(tasks))
					Eat.removeTask(tasks);
			}
		}
	}
	
	public void eat(long l) {
		this.status = "Eating";

		this.setBusy(true);
		busyUntil = l;
	}

	private long thirst = 0;
	public long getThirst(){ return thirst; }
	public boolean isThirsty() { return this.thirst >= ((dayLength/timeOffset)/3)/2 && this.thirst <= (dayLength/timeOffset)/2; }
	public boolean isDehydrated() { return this.thirst >= (dayLength/timeOffset)/2; }
	private void updateThirst() {
		if (status != "Drinking"){
			this.thirst += 1;
		} else {
			if (this.hunger > (((dayLength/timeOffset)/3)/2) / (Drink.durration/timeOffset))
				this.thirst -= (((dayLength/timeOffset)/3)/2) / (Drink.durration/timeOffset);
			else 
				this.thirst = 0;
			
		}
		
		if (isAwake()){
			if ((isThirsty() || isDehydrated()) && status != "Drinking"){
				if (!Drink.taskExists(tasksToAdd)
				 && !Drink.taskExists(tasks))
					addTask(new Drink());
			} else if ((!isThirsty() && !isDehydrated()) && status != "Drinking"){
				if (Drink.taskExists(tasksToAdd))
				Drink.removeTask(tasksToAdd);
				if (Drink.taskExists(tasks))
				Drink.removeTask(tasks);
			}
		}
	}
		
	public void drink(long l) {
		this.status = "Drinking";

		this.setBusy(true);
		busyUntil = l;
	}
	
	private List<Task> tasks = new ArrayList<Task>();
	private List<Task> dailyTasks = new ArrayList<Task>();
	private List<Task> tasksToAdd = new ArrayList<Task>();
	public List<Task> getTasks() {
		return tasks; 
	}

	public void addTask(Task task) {
		tasksToAdd.add(task);
	}
		
	public void updateTasks(long timeleftinday) {
		long daypart = (dayLength)/4;
		
		if (isHungry() && this.status != "Eating"){

			if (timeleftinday > (daypart * 3)
				&& EatBreakfast.taskExists(dailyTasks)
				&& !EatBreakfast.taskExists(tasksToAdd)
				&& !EatBreakfast.taskExists(tasks)){
				tasksToAdd.add(new EatBreakfast());
				EatBreakfast.removeTask(dailyTasks);

			
			} else if (timeleftinday > (daypart * 2)
				&& timeleftinday < (daypart * 3)
				&& EatLunch.taskExists(dailyTasks)
				&& !EatLunch.taskExists(tasksToAdd)
				&& !EatLunch.taskExists(tasks)){
				tasksToAdd.add(new EatLunch());
				EatLunch.removeTask(dailyTasks);
				if (EatBreakfast.taskExists(dailyTasks))
					EatBreakfast.removeTask(dailyTasks);
				if (EatBreakfast.taskExists(tasksToAdd))
					EatBreakfast.removeTask(tasksToAdd);
				if (EatBreakfast.taskExists(tasks))
					EatBreakfast.removeTask(tasks);

			} else if (timeleftinday > (daypart) 
				&& timeleftinday < (daypart * 2) 
				&& timeleftinday < (daypart * 3)
				&& EatDinner.taskExists(dailyTasks)
				&& !EatDinner.taskExists(tasksToAdd)
				&& !EatDinner.taskExists(tasks)){
				tasksToAdd.add(new EatDinner());
				EatDinner.removeTask(dailyTasks);
				if (EatDinner.taskExists(dailyTasks))
					EatDinner.removeTask(dailyTasks);
				if (EatDinner.taskExists(tasksToAdd))
					EatDinner.removeTask(tasksToAdd);
				if (EatDinner.taskExists(tasks))
					EatDinner.removeTask(tasks);

			}
		} 
		
		if (isTired()){
			if (timeleftinday < (daypart) 
					&& Sleep.taskExists(dailyTasks)
					&& !Sleep.taskExists(tasksToAdd)
					&& !Sleep.taskExists(tasks)){
					//tasksToAdd.add(new Sleep());
					Sleep.removeTask(dailyTasks);

				}
		}
		
		if (!isAwake()){
			if (WakeUp.taskExists(dailyTasks)
					&& !WakeUp.taskExists(tasksToAdd)
					&& !WakeUp.taskExists(tasks)){
					tasksToAdd.add(new WakeUp());
					Sleep.removeTask(dailyTasks);

				}
		}
		
		
		if (tasksToAdd.size() > 0){
			tasks.addAll(tasksToAdd);
			tasksToAdd.retainAll(new ArrayList<Task>());
		}
	}
		

	public void addDailyTasks() {
		if (tasks.size() > 0){
			List<Task> oldTasks = new ArrayList<Task>();
			//Remove any old Daily Tasks
			for (Task task : tasks){
				if (task instanceof EatBreakfast ||
					task instanceof EatLunch ||
					task instanceof EatDinner ||
					task instanceof Sleep)
						
					oldTasks.add(task);
			}
			tasks.remove(oldTasks);
		}
	
		dailyTasks = new ArrayList<Task>();
		//Add New Daily Tasks
		dailyTasks.add(new EatBreakfast());
		dailyTasks.add(new EatLunch());
		dailyTasks.add(new EatDinner());
				
		dailyTasks.add(new Sleep());
	}


	
	public Npc(long dayLength, int timeOffset){
		this.dayLength = dayLength;
		this.timeOffset = timeOffset;
		
		energy = (((dayLength/timeOffset)/4)*3);
		hunger = ((dayLength/timeOffset)/3);
		thirst = (((dayLength/timeOffset)/3)/2);
		
		getTasks();
		addDailyTasks();
	}

	public void checkTasks(){
		if (tasks.size() > 0) {
			List<Task> completedTasks = new ArrayList<Task>();
			for (Task task : tasks){
				if (task.canDoTask(this)){
					task.doTask(this);
					completedTasks.add(task);
				}
			}
			tasks.removeAll(completedTasks);
		}
	}
	
	public void updateStates(){
		if (isBusy){
			long now = Calendar.getInstance().getTimeInMillis();
			if (busyUntil <= now)
				this.setBusy(false);
		}

		updateHunger();

		updateThirst();
		
		updateEnergy();

	}
	
	
	@Override
	public String toString() {
		String returnString = "Npc [status=" + status + "] [tasks=" + tasks.size() + "]";
		//returnString += "[hunger= "+ hunger +" thirst= "+ thirst +" energy= "+ energy +"]";
		if (isHungry() || status == "Eating")
			returnString += " [hungry]";

		if (isStarving())
			returnString += " [STARVING]";
		
		if (isThirsty() || status == "Drinking")
			returnString += " [thirsty]";
		
		if (isDehydrated())
			returnString += " [DEHYDRATED]";
		
		if (isTired() || status == "Sleeping")
			returnString += " [tired]";
		
		if (isUnconsious())
			returnString += " [UNCONSIOUS]";
		
		return returnString;
		
	}

	public String tasksToString() {
		String returnString = "[No Tasks]";
		if (tasks.size() > 0){
			returnString = "";
			for (Task task : tasks)
				returnString += task.toString() + " ";
			
		}
		
		return returnString;
		
	}
	
}
