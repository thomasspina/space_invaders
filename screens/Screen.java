import java.util.ArrayList;

public abstract class Screen {

	//STATIC VARIABLES
	
	//INSTANCE VARIABLES
	protected double scale = 1;
	protected double xCenter = 0;		//point in universe on which the screen will center
	protected double yCenter = 0;
	
	protected boolean complete = false;
	
	protected double accelarationX = 0; //per second per second
	protected double accelarationY = 600; //per second per second    
	
	protected ActiveSprite player1 = null;
	protected ArrayList<ActiveSprite> activeSprites = new ArrayList<ActiveSprite>();
	protected ArrayList<StaticSprite> staticSprites = new ArrayList<StaticSprite>();

	//require a separate list for sprites to be removed to avoid a concurence exception
	private ArrayList<ActiveSprite> disposalList = new ArrayList<ActiveSprite>();

	public Screen() {
		activeSprites = new ArrayList<ActiveSprite>();
		staticSprites = new ArrayList<StaticSprite>();
	}
	
	public double getScale() {
		return scale;
	}


	public double getXCenter() {
		return xCenter;
	}


	public double getYCenter() {
		return yCenter;
	}
	
	public void setXCenter(double xCenter) {
		this.xCenter = xCenter;
	}

	public void setYCenter(double yCenter) {
		this.yCenter = yCenter;
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

	public ArrayList<ActiveSprite> getActiveSprites() {
		return activeSprites;
	}

	public ArrayList<StaticSprite> getStaticSprites() {
		return staticSprites;
	}

	public void setStaticSprites(ArrayList<StaticSprite> staticSprites) {
		this.staticSprites = staticSprites;
	}

	public abstract boolean centerOnPlayer();

	public abstract void update(KeyboardInput keyboard, long actual_delta_time);
	
    protected void updateSprites(KeyboardInput keyboard, long actual_delta_time) {
		for (int i = 0; i < activeSprites.size(); i++) {
			ActiveSprite sprite = activeSprites.get(i);
    		sprite.update(this, keyboard, actual_delta_time);
    	}    	
    }
    
    protected void disposeSprites() {
    
		for (int i = 0; i < activeSprites.size(); i++) {
			ActiveSprite sprite = activeSprites.get(i);
    		if (sprite.getDispose() == true) {
    			disposalList.add(sprite);
    		}
    	}
		for (int i = 0; i < disposalList.size(); i++) {
			ActiveSprite sprite = disposalList.get(i);
			activeSprites.remove(sprite);
    	}
    	if (disposalList.size() > 0) {
    		disposalList.clear();
    	}
    }	
}
