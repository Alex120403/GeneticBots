package com.genetic.bots.UI;

import com.genetic.bots.InputHandler;
import com.genetic.bots.InputObserver;
import com.genetic.bots.Main;

public class PanelsHandler implements InputObserver {   // Main panels class
    private Panel worldsPanel;
    private Panel worldStatePanel;
    private Panel botsPanel;
    private Panel selectedPanel;
    private Main main;

    public PanelsHandler(Main main) {
        this.main = main;
        InputHandler.addToObservers(this); // Getting input events

        worldsPanel = new WorldsPanel(this);
        worldStatePanel = new WorldStatePanel(this);
        botsPanel = new BotsPanel(this);
        selectedPanel = worldsPanel;
    }

    // Draw all buttons and render() current panel
    public void render() {
        worldsPanel.drawButton();
        worldStatePanel.drawButton();
        botsPanel.drawButton();
        selectedPanel.render();
        selectedPanel.drawButton();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    // Runs when user clicks on the screen
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        worldsPanel.touchDown(screenX,screenY,pointer,button);
        worldStatePanel.touchDown(screenX,screenY,pointer,button);
        botsPanel.touchDown(screenX,screenY,pointer,button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    // Sets new panel-to-render
    public void setSelectedPanel(Panel selectedPanel) {
        this.selectedPanel = selectedPanel;
    }

    // Returns object of Main.class
    public Main getMain() {
        return main;
    }
}
