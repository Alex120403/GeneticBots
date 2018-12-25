package com.genetic.bots;

import com.genetic.bots.BotsHandling.Bot;

public class Interaction {
    int x,y,value;
    Bot bot;

    public Interaction(int x, int y, int value, Bot bot) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.bot = bot;
    }
}
