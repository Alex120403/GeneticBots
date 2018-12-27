package com.genetic.bots.UI;

public class BotsPanel extends Panel {
    public BotsPanel(PanelsHandler handler) {
        super(handler);
    }

    // Restores used memory
    public void dispose() {
        button.dispose();
    }

    // Runs with constructor
    @Override
    void init() {
        button = new SelectButton(this,2,"BotsSelectButton.png");
    }

    // Draw panel content
    @Override
    void render() {

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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
}
