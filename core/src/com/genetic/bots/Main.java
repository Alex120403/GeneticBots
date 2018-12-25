package com.genetic.bots;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.genetic.bots.UI.BotInfo;
import com.genetic.bots.UI.Menu;
import com.genetic.bots.UI.PanelsHandler;
import com.genetic.bots.WorldsHandling.Cell;
import com.genetic.bots.WorldsHandling.World;

public class Main extends ApplicationAdapter {
	private Paint paint;
	public static World mainWorld;
	private InputHandler inputHandler;
	private Menu menu;
	private boolean paused,started,stopped;
	private PanelsHandler panelsHandler;

	@Override
	public void create () {
		menu = new Menu(this);
		inputHandler = new InputHandler();
		paint = new Paint();
		panelsHandler = new PanelsHandler(this);
	}

	public void start() {
		started = true;
		mainWorld = new World(null,Config.BOTS_COUNT);
	}

	public void paused() {
		paused = true;
	}

	public void stop() {
		stopped = true;
		started = false;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.9f, 0.95f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (started && !paused && !stopped){
			mainWorld.render();
		}
		menu.render();
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isKeyPressed(Input.Keys.D)) {
			Config.IS_DEVELOPER_MODE_ENABLED = true;
		}
		panelsHandler.render();
	}
	
	@Override
	public void dispose () {
		Cell.dispose();
		BotInfo.dispose();
		paint.dispose();
		menu.dispose();
		if(mainWorld!=null) {
			mainWorld.dispose();
		}
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isStarted() {
		return started;
	}

	public boolean isStopped() {
		return stopped;
	}
}
