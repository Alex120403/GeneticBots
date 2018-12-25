package com.genetic.bots.WorldsHandling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.genetic.bots.*;
import com.genetic.bots.BotsHandling.Bot;
import com.genetic.bots.BotsHandling.BotFactory;
import com.genetic.bots.UI.BotState;
import com.genetic.bots.UI.ChromosomeDisplay;

import java.util.ArrayList;
import java.util.Random;

public class World implements Disposable {

    private static Random random = new Random();

    // Constants
    static final int OPERATION_TIME = 100;
    static final float DEFAULT_MUTATE_PERCENTS = 1/8;
    public static  int BOTS_COUNT;

    // Statement
    private int width,height;
    private BotFactory botFactory;
    private Bot[] bots;
    private static Cell[][] map;
    private BotState botState;
    private int steps,sleepIterations;
    private WorldUpdater worldUpdater;
    private static Graph graph;
    public static int aliveBots;
    public static Bot bestBot;

    private static MapGenerator generator;

    @Override
    public void dispose() {
        graph.dispose();
    }

    public World(Bot[] bots,int botsCount) {
        if(bots == null) {
            BOTS_COUNT = botsCount;
            botFactory = new BotFactory();
            generator = new MapGenerator(Config.DEGREE_OF_WALLS,Config.DEGREE_OF_HUMANS,Config.DEGREE_OF_FIRE);
            map = generator.generateMap();
            this.bots = generateStarterBots(BOTS_COUNT);
            botState = new BotState(this.bots);
            addBotsToMap(map, this.bots);
            graph = new Graph();
        }
        else {
            map = generator.generateMap();
            this.bots = bots;
            addBotsToMap(map, this.bots);
            botState = new BotState(bots);
        }

        worldUpdater = new WorldUpdater(this);
        worldUpdater.start();
        aliveBots = BOTS_COUNT;
        bestBot = this.bots[0];
    }

    private Bot[] generateStarterBots(int size) { // Generating array of bots with random genes
        Bot[] b = new Bot[size];
        for (int i = 0; i < size; i++) {
            b[i] = botFactory.createNewBot();
        }
        return b;
    }

    public static byte getCellValue(int x, int y) {
        return map[x][y].getContent();
    }
    public static Cell getCell(int x, int y) {
        return map[x][y];
    }

    public static void addRandomFire() {
        byte rX;
        byte rY;
        do {
            rX = (byte)random.nextInt(Config.MAP_WIDTH);
            rY = (byte)random.nextInt(Config.MAP_HEIGHT);
        } while (map[rX][rY].getContent()!=Cell.TYPE_NOTHING);
        map[rX][rY].update(Cell.TYPE_FIRE);
    }
    public static void addRandomHuman() {
        byte rX;
        byte rY;
        do {
            rX = (byte)random.nextInt(Config.MAP_WIDTH);
            rY = (byte)random.nextInt(Config.MAP_HEIGHT);
        } while (map[rX][rY].getContent()!=Cell.TYPE_NOTHING);
        map[rX][rY].update(Cell.TYPE_HUMAN);
    }


    private void addBotsToMap(Cell[][] map,Bot[] bots) {
        short order = 0;
        if(order<=bots.length-1) {
            byte nX;
            byte nY;
            for (int i = order; i < bots.length; i++) {
                do {
                    nX = (byte) random.nextInt(Config.MAP_WIDTH);
                    nY = (byte) random.nextInt(Config.MAP_HEIGHT);
                } while (map[nX][nY].getContent()!=Cell.TYPE_NOTHING);
                bots[order].setCoords(nX,nY);
                map[nX][nY].putBot(bots[order]);
                order++;
            }
        }
    }


    private synchronized void nextPopulation() {
        BotFactory botFactory = new BotFactory();
        Bot[] bestBots = new Bot[bots.length/8];
        bubbleSorter();
        graph.add((float)bots[0].getFitnessFunc());
        ArrayList<Bot> newBotsArrayList = new ArrayList<Bot>();
        byte multiply = 0;
        for (byte i = 0; i < bestBots.length; i++) {
            bestBots[i] = bots[i];
        }
        for (byte i = 0; i < bestBots.length*8; i++) {
            newBotsArrayList.add(bestBots[i/8]);

        }
        for (byte i = 0; i < bots.length; i++) {
            bots[i] = botFactory.generateByChromosome(newBotsArrayList.get(i));
            if(i%8==0) {
                botFactory.mutate(bots[i],Config.CHANCE_TO_MUTATE_ANOTHER_ONE_GENE);
            }
        }

        worldUpdater.active = false;
        Main.mainWorld = new World(bots,BOTS_COUNT);
    }

    public Cell[][] getMap() {
        return map;
    }

    public void render() {

        for (int i = 0; i < Config.MAP_WIDTH; i++) {
            for (int j = 0; j < Config.MAP_HEIGHT; j++) {
                map[i][j].render();
            }
        }
        botState.render();
        graph.render();

    }

    public synchronized void update() {
        for (int i = 0; i < bots.length; i++) {  // Main cycle
            if(bots[i].isAlive()) {
                bots[i].makeStep();
                if(aliveBots<=Config.BOTS_COUNT/8){
                    nextPopulation();
                    aliveBots = BOTS_COUNT;
                }
            }
            if(bots[i].getFitnessFunc()>bestBot.getFitnessFunc()) {
                bestBot = bots[i];


            }
        }
        steps++;
    }

    private void bubbleSorter(){     //МЕТОД ПУЗЫРЬКОВОЙ СОРТИРОВКИ
        for (int out = bots.length - 1; out >= 1; out--){  //Внешний цикл
            for (int in = 0; in < out; in++){       //Внутренний цикл
                if(bots[in+1].getFitnessFunc() > bots[in].getFitnessFunc())//Если порядок элементов нарушен
                    toSwap(in, in + 1);             //вызвать метод, меняющий местами
            }
        }
    }
    private void toSwap(int i1, int i2) {
        Bot buffer = bots[i1];
        bots[i1] = bots[i2];
        bots[i2] = buffer;

    }

    public Bot[] getBots() {
        return bots;
    }
}
