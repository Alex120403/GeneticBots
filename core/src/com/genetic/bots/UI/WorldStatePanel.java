package com.genetic.bots.UI;

import com.genetic.bots.WorldsHandling.World;

import static com.genetic.bots.WorldsHandling.World.bestBot;

public class WorldStatePanel extends Panel {
    private static ChromosomeDisplay chromosomeDisplay;
    public WorldStatePanel(PanelsHandler handler) { //TODO dispose
        super(handler);
    }

    @Override
    void init() {
        button = new SelectButton(this,1,"CurrentWorldSelectButton.png");
    }

    @Override
    void render() {
        if (chromosomeDisplay == null) {
            chromosomeDisplay = new ChromosomeDisplay(handler.getMain().mainWorld.getBots()[0].getChromosome());
        }
        chromosomeDisplay.drawFlag();
        chromosomeDisplay.render();
        chromosomeDisplay.botInfo.setBot(handler.getMain().mainWorld.bestBot);
        chromosomeDisplay.setChromosome(bestBot.getChromosome());
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