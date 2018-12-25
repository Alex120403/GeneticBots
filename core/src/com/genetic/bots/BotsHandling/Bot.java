package com.genetic.bots.BotsHandling;

import com.genetic.bots.Interaction;
import com.genetic.bots.UI.BotInfo;
import com.genetic.bots.WorldsHandling.Cell;
import com.genetic.bots.WorldsHandling.World;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.Serializable;
import java.util.Random;

public class Bot implements Comparable<Bot>, Serializable {

    static Random random;
    public static final int HEALING = 10;

    static {
        random = new Random();
    }

    private Gene[] genes;
    private BotInfo info;
    private String name = "Bot "+(random.nextInt(90000)+10000);

    // State
    private transient boolean alive = true,best;
    private transient boolean glow;
    public int operationFlag = 0,rotation;
    protected short x,y;
    protected int rescuedPeople,extinguishedFire,health = 80;

    // Can be created only by BotFactory
    protected Bot(Gene[] genes) {
        this.genes = genes;
    }

    protected void mutateOneGene() {
        genes[random.nextInt(genes.length)] = new Gene((byte) random.nextInt(64));
    }

    public long getFitnessFunc() {
        if(!alive){
            return -1;
        }

        return (rescuedPeople)*BotFactory.POINTS_PER_SAVED_PEOPLE + (extinguishedFire*BotFactory.POINTS_PER_EXTINGUISHED_FIRE);
    }

    public String getFitnessFuncString() {
        int ff = rescuedPeople*BotFactory.POINTS_PER_SAVED_PEOPLE + extinguishedFire*BotFactory.POINTS_PER_EXTINGUISHED_FIRE;
        if(ff>=1000000) {
            return (ff/1000000)+"."+((ff/100000)%10000)+"M";
        }
        else if(ff>=1000) {
            return (ff/1000)+"."+((ff/100)%10)+"K";
        }
        else {
            return ff+"";
        }
    }

    public void setCoords(short x,short y){
        this.x = x;
        this.y = y;
    }

    public short getX() {
        return x;
    }

    public boolean isBest() {
        return best;
    }

    public boolean isGlowing() {
        return glow;
    }

    public Gene[] getChromosome() {
        return genes;
    }

    public short getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public String getHealthString() {
        health = (health);
        String res = "";
        if(health>=1000) {
            return (health/1000)+"."+((health/100)%10)+"K";
        }
        else {
            return health+"";
        }
    }

    public void linkTo(BotInfo info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public int getRescuedPeople() {
        return rescuedPeople;
    }

    public int getExtinguishedFire() {
        return extinguishedFire;
    }

    public void makeStep() {  // Main bot method
        operationFlag = operationFlag%64;
        if((health)<=0){
            die(x,y);
        }
        byte operationsCount = 0;
        boolean isStepped = false;
        while (!isStepped && operationsCount<10) {
            operationsCount++;
            isStepped = doOperation(genes[Math.abs(operationFlag%genes.length)].getValue());
        }
        health--;
    }



    private boolean doOperation(int operationId) {

        glow = false;
        boolean isStepped = false;
        int deltaOperationFlag = 0;
        byte vectorX = 0, vectorY = 0;
        if(operationId<8) {        // Сделать шаг
            isStepped = true;
            switch ((operationId+rotation)%8) {
                case 0:
                    vectorX = -1;
                    vectorY = 1;
                    break;
                case 1:
                    vectorX = 0;
                    vectorY = 1;
                    break;
                case 2:
                    vectorX = 1;
                    vectorY = 1;
                    break;
                case 3:
                    vectorX = 1;
                    vectorY = 0;
                    break;
                case 4:
                    vectorX = 1;
                    vectorY = -1;
                    break;
                case 5:
                    vectorX = 0;
                    vectorY = -1;
                    break;
                case 6:
                    vectorX = -1;
                    vectorY = -1;
                    break;
                case 7:
                    vectorX = -1;
                    vectorY = 0;
                    break;
            }
            if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_NOTHING) {
                deltaOperationFlag+=5;
                World.getCell(x,y).removeBot();
                World.getCell(x+vectorX,y+vectorY).putBot(this);
                x+=vectorX;
                y+=vectorY;
            }
            else if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_FIRE) {
                deltaOperationFlag+=1;
                World.addRandomFire();
                die(x+vectorX,y+vectorY);
            }
            else if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_WALL) {
                deltaOperationFlag+=2;
            }
            else if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_HUMAN) {
                deltaOperationFlag+=4;
                World.getCell(x,y).removeBot();
                World.getCell(x+vectorX,y+vectorY).putBot(this);
                World.addRandomHuman();
                health+=HEALING;
                x+=vectorX;
                y+=vectorY;
                rescuedPeople++;
            }
            else if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_BOT) {
                deltaOperationFlag+=3;
            }
        }
        else if(operationId<16) {  // Взаимодействовать
            isStepped = true;
            switch ((operationId+rotation)%8) {
                case 0:
                    vectorX = -1;
                    vectorY = 1;
                    break;
                case 1:
                    vectorX = 0;
                    vectorY = 1;
                    break;
                case 2:
                    vectorX = 1;
                    vectorY = 1;
                    break;
                case 3:
                    vectorX = 1;
                    vectorY = 0;
                    break;
                case 4:
                    vectorX = 1;
                    vectorY = -1;
                    break;
                case 5:
                    vectorX = 0;
                    vectorY = -1;
                    break;
                case 6:
                    vectorX = -1;
                    vectorY = -1;
                    break;
                case 7:
                    vectorX = -1;
                    vectorY = 0;
                    break;
            }
            if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_NOTHING) {
                deltaOperationFlag+=5;
            }
            else if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_FIRE) {
                deltaOperationFlag+=1;
                World.getCell(x+vectorX,y+vectorY).update(Cell.TYPE_NOTHING);
                health+=HEALING;
                World.addRandomFire();
                extinguishedFire++;
            }
            else if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_WALL) {
                deltaOperationFlag+=2;
            }
            else if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_HUMAN) {
                deltaOperationFlag+=4;
                World.getCell(x+vectorX,y+vectorY).update(Cell.TYPE_NOTHING);
                rescuedPeople++;
                health+=HEALING;
                World.addRandomHuman();
            }
            else if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_BOT) {
                deltaOperationFlag+=3;
            }
        }
        else if(operationId<24) {  // Посмотреть
            switch ((operationId+rotation)%8) {
                case 0:
                    vectorX = -1;
                    vectorY = 1;
                    break;
                case 1:
                    vectorX = 0;
                    vectorY = 1;
                    break;
                case 2:
                    vectorX = 1;
                    vectorY = 1;
                    break;
                case 3:
                    vectorX = 1;
                    vectorY = 0;
                    break;
                case 4:
                    vectorX = 1;
                    vectorY = -1;
                    break;
                case 5:
                    vectorX = 0;
                    vectorY = -1;
                    break;
                case 6:
                    vectorX = -1;
                    vectorY = -1;
                    break;
                case 7:
                    vectorX = -1;
                    vectorY = 0;
                    break;
            }
            if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_NOTHING) {
                deltaOperationFlag+=5;
            }
            else if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_FIRE) {
                deltaOperationFlag+=1;
            }
            else if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_WALL) {
                deltaOperationFlag+=2;
            }
            else if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_HUMAN) {
                deltaOperationFlag+=4;
            }
            else if(World.getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_BOT) {
                deltaOperationFlag+=3;
            }
        }
        else if(operationId<32) {  // Поворот
            rotation+=operationId%8;
            deltaOperationFlag++;
        }
        else if(operationId<64) {  // Безусловный переход
            deltaOperationFlag+=operationId;
        }
        operationFlag+=deltaOperationFlag;
        return isStepped;
    }

    @Override
    public int compareTo(Bot o) {
        return (int)getFitnessFunc()-(int)o.getFitnessFunc();
    }

    private void die(int fireX,int fireY) {
        World.getCell(x,y).removeBot();
        World.getCell(fireX,fireY).update(Cell.TYPE_NOTHING);
        alive = false;
        World.aliveBots--;
    }



    public boolean isAlive() {
        return alive;
    }

    public int getOperationFlag() {
        return operationFlag;
    }

    public void addRescuedPeople() {
        this.rescuedPeople++;
    }

    public void addExtinguishedFire() {
        this.extinguishedFire++;
    }

    public void glow() {
        glow = true;
    }
}
