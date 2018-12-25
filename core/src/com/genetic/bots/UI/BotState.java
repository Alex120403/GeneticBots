package com.genetic.bots.UI;

import com.genetic.bots.BotsHandling.Bot;
import com.genetic.bots.InputHandler;
import com.genetic.bots.InputObserver;
import com.genetic.bots.Main;
import com.genetic.bots.WorldsHandling.World;

public class BotState implements InputObserver {

    private BotInfo[] infos;
    private Bot[] bots;
    private int sortRate = 10,sortOrder;
    public static float globalOffset,SCROLL_OFFSET = 0.33f;

    public BotState(Bot[] bots) {
        InputHandler.addToObservers(this);
        this.bots = bots;
        infos = new BotInfo[bots.length];
        for (int i = 0; i < bots.length; i++) {
            infos[i] = new BotInfo(bots[i],i);
        }
    }

    public void render() {
        for (int i = infos.length-1; i >= 0; i--) {
            infos[i].render();
        }

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
        if((infos[infos.length-1].yOffset+globalOffset)-SCROLL_OFFSET*amount<=0 && amount == -1 || amount == 1) {
            if((infos[1].yOffset+globalOffset)>3.3f || amount == -1){
                globalOffset -= (SCROLL_OFFSET * amount);

            }
        }
        return false;
    }
    private void bubbleSorter(){     //МЕТОД ПУЗЫРЬКОВОЙ СОРТИРОВКИ
        for (int out = infos.length - 1; out >= 1; out--){  //Внешний цикл
            for (int in = 0; in < out; in++){       //Внутренний цикл
                if(infos[in].getBot().getFitnessFunc() > infos[in + 1].getBot().getFitnessFunc())//Если порядок элементов нарушен
                    toSwap(in, in + 1);             //вызвать метод, меняющий местами
            }
        }
        for (int i = 0; i < infos.length; i++) {
            infos[i].yOffset = infos.length-1-i;
        }
    }

    private void toSwap(int i1, int i2) {
        BotInfo buffer = infos[i1];
        infos[i1] = infos[i2];
        infos[i2] = buffer;

    }
}
