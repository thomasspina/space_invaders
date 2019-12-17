public class MenuScreen extends Screen {
	
	public MenuScreen() {
		super();
		setXCenter(0);
		setYCenter(0);
		
		BarrierSprite rightBarrier = new BarrierSprite(350, -250, 340, 270);
		BarrierSprite leftBarrier = new BarrierSprite(-363, -250, -353, 270);
		AlienSprite alien = new AlienSprite(1, 200, 0);
		AlienSprite alien2 = new AlienSprite(1, 150, 0);
		
		activeSprites.add(alien2);
		activeSprites.add(alien);
		
		staticSprites.add(rightBarrier);
		staticSprites.add(leftBarrier);
	}

	@Override
	public boolean centerOnPlayer() {
		return false;
	}

	@Override
	public void update(KeyboardInput keyboard, long actual_delta_time) {
		updateSprites(keyboard, actual_delta_time);
		disposeSprites();
	}
}
