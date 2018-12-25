package com.genetic.bots.UI;

import com.genetic.bots.InputObserver;

public abstract class Panel implements InputObserver {
    PanelsHandler handler;
    private boolean isSelected = false;
    SelectButton button;

    public Panel(PanelsHandler handler) {
        this.handler = handler;
        init();
    }

    abstract void init();
    abstract void render();
    void drawButton() {
        button.render();
    }

    void select() {
        handler.setSelectedPanel(this);
    }

}
