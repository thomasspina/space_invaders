public class SpaceInvadersScreen extends Screen {

	public SpaceInvadersScreen() {
		super();
		setXCenter(0);
		setYCenter(0);
		
		BarrierSprite rightBarrier = new BarrierSprite(350, -250, 340, 270);
		BarrierSprite leftBarrier = new BarrierSprite(-363, -250, -353, 270);
		
		int alienType = 3;
		int yPosition = 0;
		for (int i = 0; i < 5; i++) {
			int xPosition = -300;
			for (int j = 0; j < 11; j++) {
				AlienSprite alien = new AlienSprite(alienType, xPosition, yPosition);
				activeSprites.add(alien);
				xPosition += 40;
			}
			yPosition -= 40;
			if (i == 1 || i == 3) {
				alienType--;
			}
		}
		
		
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
