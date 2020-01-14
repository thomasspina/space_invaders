import java.util.ArrayList;

public abstract class Screen {

	private boolean complete = false;

	private double accelarationX = 0; //per second per second
	private double accelarationY = 600; //per second per second

	private ActiveSprite player1 = null;
	protected ArrayList<ActiveSprite> activeSprites;
	protected ArrayList<StaticSprite> staticSprites;
	private ArrayList<ActiveSprite> disposalList = new ArrayList<>();

	Screen() {
		activeSprites = new ArrayList<>();
		staticSprites = new ArrayList<>();
	}
	
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public double getAccelarationX() {
		return accelarationX;
	}

	public double getAccelarationY() {
		return accelarationY;
	}

	protected ActiveSprite getPlayer1() {
		return player1;
	}

	ArrayList<ActiveSprite> getActiveSprites() {
		return activeSprites;
	}

	ArrayList<StaticSprite> getStaticSprites() {
		return staticSprites;
	}

	public abstract void update(KeyboardInput keyboard, long actual_delta_time);
	
	void updateSprites(KeyboardInput keyboard, long actual_delta_time) {
		for (ActiveSprite activeSprite : activeSprites) {
			activeSprite.update(this, keyboard, actual_delta_time);
    	}    	
    }
    
    void disposeSprites() {

		for (ActiveSprite activeSprite : activeSprites) {
    		if (activeSprite.getDispose()) {
    			disposalList.add(activeSprite);
    		}
    	}

		for (ActiveSprite activeSprite : disposalList) {
			activeSprites.remove(activeSprite);
    	}

    	if (disposalList.size() > 0) {
    		disposalList.clear();
    	}
    }	
}
