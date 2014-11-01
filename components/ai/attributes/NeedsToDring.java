package components.ai.attributes;

import components.ai.AiObject;

public interface NeedsToDrink extends AiObject{

	public boolean isThirsty();
	public long getThirst();
	
	public void drink(long l);

}
