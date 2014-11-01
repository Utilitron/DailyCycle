package components.ai.attributes;

import components.ai.AiObject;

public interface CanSleep extends AiObject{

	public boolean isAwake();
	public boolean isTired();


	public void sleep(long l);
	public void wakeup();

}
