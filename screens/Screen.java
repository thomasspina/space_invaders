import java.util.ArrayList;

public abstract class Screen {
	ArrayList<ActiveSprite> activeSprites;
	ArrayList<StaticSprite> staticSprites;
	private ArrayList<ActiveSprite> disposalList = new ArrayList<>();

	Screen() {
		activeSprites = new ArrayList<>();
		staticSprites = new ArrayList<>();
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
