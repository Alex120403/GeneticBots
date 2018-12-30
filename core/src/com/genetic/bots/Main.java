package com.genetic.bots;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.genetic.bots.UI.BotInfo;
import com.genetic.bots.UI.Menu;
import com.genetic.bots.UI.PanelsHandler;
import com.genetic.bots.WorldsHandling.Cell;
import com.genetic.bots.WorldsHandling.MapGenerator;
import com.genetic.bots.WorldsHandling.World;
import com.genetic.bots.desktop.DesktopLauncher;

public class Main extends ApplicationAdapter {
	private Paint paint;
	public static World[] worlds;
	private InputHandler inputHandler;
	private Menu menu;
	private boolean paused,started = true,stopped;
	private PanelsHandler panelsHandler;
	private static int selectedWorldID = -1;
	public static Cell[][] map;


	// Runs when program starts
	@Override
	public void create () {
		MapGenerator generator = new MapGenerator(Config.DEGREE_OF_WALLS,Config.DEGREE_OF_HUMANS,Config.DEGREE_OF_FIRE);
		map = generator.generateMap();
		menu = new Menu(this);
		inputHandler = new InputHandler();
		paint = new Paint();
		panelsHandler = new PanelsHandler(this);
		worlds = new World[6];
		setSelectedWorldID(-1);
	}

	// Runs when user clicks on start button
	public void start() {
		started = true;
		//mainWorld = new World(null,Config.BOTS_COUNT);
	}

	// Runs if user clicks on pause button
	public void paused() {
		paused = true;
	}

	// TODO remove or update it
	public void stop() {
		stopped = true;
		started = false;
	}

	// Draw current world and UI
	@Override
	public void render () {
		if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			System.gc();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
			create();
		}
		Gdx.gl.glClearColor(0.9f, 0.95f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (selectedWorldID!= -1 && !stopped){
			worlds[selectedWorldID].render();
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
		for (World world:worlds) {
			if (world != null) {
				world.dispose();
			}
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

	public static void setSelectedWorldID(int selectedWorldID1) {
		selectedWorldID = selectedWorldID1;

	}

	public static int getSelectedWorldID() {
		return selectedWorldID;
	}
}
