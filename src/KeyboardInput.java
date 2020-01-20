import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

	private static final int KEY_COUNT = 256;

	private enum KeyState {
		RELEASED,
		PRESSED,
		ONCE
	}

	private boolean[] currentKeys;

	private KeyState[] keys;

	KeyboardInput() {
		currentKeys = new boolean[ KEY_COUNT ];
		keys = new KeyState[ KEY_COUNT ];
		for( int i = 0; i < KEY_COUNT; ++i ) {
			keys[ i ] = KeyState.RELEASED;
		}
	}

	synchronized void poll() {
		for( int i = 0; i < KEY_COUNT; ++i ) {
			if( currentKeys[ i ] ) {
				if( keys[ i ] == KeyState.RELEASED )
					keys[ i ] = KeyState.ONCE;
				else
					keys[ i ] = KeyState.PRESSED;
			} else {
				keys[ i ] = KeyState.RELEASED;
			}
		}
	}

	boolean keyDown( int keyCode ) {
		return keys[ keyCode ] == KeyState.ONCE ||
				keys[ keyCode ] == KeyState.PRESSED;
	}

	boolean keyDownOnce( int keyCode ) {
		return keys[ keyCode ] == KeyState.ONCE;
	}

	public synchronized void keyPressed( KeyEvent e ) {
		int keyCode = e.getKeyCode();
		if( keyCode >= 0 && keyCode < KEY_COUNT ) {
			currentKeys[ keyCode ] = true;
		}
	}

	public synchronized void keyReleased( KeyEvent e ) {
		int keyCode = e.getKeyCode();
		if( keyCode >= 0 && keyCode < KEY_COUNT ) {
			currentKeys[ keyCode ] = false;
		}
	}

	public void keyTyped( KeyEvent e ) {
		// Not needed
	}
}

