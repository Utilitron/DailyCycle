package components.ai.attributes;

import components.ai.AiObject;

public interface NeedsToEat extends AiObject{

	public boolean isHungry();
	public long getHunger();
	
	public void eat(long l);

}
