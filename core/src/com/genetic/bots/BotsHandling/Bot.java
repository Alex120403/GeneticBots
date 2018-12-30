package com.genetic.bots.BotsHandling;

import com.genetic.bots.Main;
import com.genetic.bots.TextFormat;
import com.genetic.bots.UI.BotInfo;
import com.genetic.bots.WorldsHandling.Cell;
import com.genetic.bots.WorldsHandling.World;

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
    private transient boolean alive = true;
    public int operationFlag = 0;
    protected short x,y,rotation;
    protected long rescuedPeople,extinguishedFire,health = 80;
    private int worldID = -1;

    // Can be created only by BotFactory
    protected Bot(Gene[] genes) {
        this.genes = genes;
    }

    // Sets bot's world id
    public void setWorldID(int worldID) {
        this.worldID = worldID;
    }

    // Sets random value to one random gene
    protected void mutateOneGene() {
        genes[random.nextInt(genes.length)] = new Gene((byte) random.nextInt(64));
    }

    // Returns bots fitness func (based on rescued people(10 points) and extinguished fire(6 points))
    public long getFitnessFunc() {
        if(!alive){
            return -1;
        }

        return (rescuedPeople)*BotFactory.POINTS_PER_SAVED_PEOPLE + (extinguishedFire*BotFactory.POINTS_PER_EXTINGUISHED_FIRE);
    }

    // Returns parsed fitness func (e.g. 12500 -> 12.5K)
    public String getFitnessFuncString() {
        return TextFormat.coolFormat(rescuedPeople*BotFactory.POINTS_PER_SAVED_PEOPLE + extinguishedFire*BotFactory.POINTS_PER_EXTINGUISHED_FIRE);
    }

    // Set new coordinates fot this bot
    public void setCoords(short x,short y){
        this.x = x;
        this.y = y;
    }

    // Returns x posotionon in current world's map
    public short getX() {
        return x;
    }

    // Returns y posotionon in current world's map
    public short getY() {
        return y;
    }

    // Returns chromosome
    public Gene[] getChromosome() {
        return genes;
    }

    // Returns health
    public long getHealth() {
        return health;
    }

    // Returns parsed health (e.g. 1400 -> 1.4K)
    public String getHealthString() {
        return TextFormat.coolFormat(health);
    }

    public void linkTo(BotInfo info) {
        this.info = info;
    }

    // Returns name of this bot
    public String getName() {
        return name;
    }

    // Returns count of rescued people
    public long getRescuedPeople() {
        return rescuedPeople;
    }

    // Returns count of extinguished fire
    public long getExtinguishedFire() {
        return extinguishedFire;
    }

    /*
      Main bot method.
      Operations "Move", "Interact" breaks operations cycle.
      If operations count > 10, operations cycle will be breaked.
      Each step removes 1 health point. If health value is lower then 1, bot will die.
      Every operation changes operation flag. Operation flag - number of gene,
      which have an information about next operation.
    */
    public void makeStep() {
        operationFlag = operationFlag%64;
        if((health)<=0){
            die(x,y);
        }
        byte operationsCount = 0;
        boolean isStepped = false;
        while (!isStepped && operationsCount<10) {
            operationsCount++;
            try {
                isStepped = doOperation(genes[Math.abs(operationFlag%genes.length)].getValue());
<<<<<<< HEAD
            } catch (ArrayIndexOutOfBoundsException e) {
                for (int i = 0; i < Main.worlds.length; i++) {
                    for (int j = 0; j < Main.worlds[i].getBots().length; j++) {
                        if(this.equals(Main.worlds[i].getBots()[j])) {
                            setWorldID(i);
                        }
                    }
                }
                System.out.println(name+" has incorrect world's id!");
=======
            } catch (Exception e) {
                e.printStackTrace();
>>>>>>> parent of 9e12590... WorldsItem (85%...)
            }
        }
        health--;
    }



    private boolean doOperation(int operationId) {

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
            if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_NOTHING) {
                deltaOperationFlag+=5;
                Main.worlds[worldID].getCell(x,y).removeBot();
                Main.worlds[worldID].getCell(x+vectorX,y+vectorY).putBot(this);
                x+=vectorX;
                y+=vectorY;
            }
            else if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_FIRE) {
                deltaOperationFlag+=1;
                Main.worlds[worldID].addRandomFire();
                die(x+vectorX,y+vectorY);
            }
            else if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_WALL) {
                deltaOperationFlag+=2;
            }
            else if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_HUMAN) {
                deltaOperationFlag+=4;
                Main.worlds[worldID].getCell(x,y).removeBot();
                Main.worlds[worldID].getCell(x+vectorX,y+vectorY).putBot(this);
                Main.worlds[worldID].addRandomHuman();
                health+=HEALING;
                x+=vectorX;
                y+=vectorY;
                rescuedPeople++;
            }
            else if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_BOT) {
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
            if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_NOTHING) {
                deltaOperationFlag+=5;
            }
            else if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_FIRE) {
                deltaOperationFlag+=1;
                Main.worlds[worldID].getCell(x+vectorX,y+vectorY).update(Cell.TYPE_NOTHING);
                health+=HEALING;
                Main.worlds[worldID].addRandomFire();
                extinguishedFire++;
            }
            else if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_WALL) {
                deltaOperationFlag+=2;
            }
            else if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_HUMAN) {
                deltaOperationFlag+=4;
                Main.worlds[worldID].getCell(x+vectorX,y+vectorY).update(Cell.TYPE_NOTHING);
                rescuedPeople++;
                health+=HEALING;
                Main.worlds[worldID].addRandomHuman();
            }
            else if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_BOT) {
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
            if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_NOTHING) {
                deltaOperationFlag+=5;
            }
            else if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_FIRE) {
                deltaOperationFlag+=1;
            }
            else if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_WALL) {
                deltaOperationFlag+=2;
            }
            else if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_HUMAN) {
                deltaOperationFlag+=4;
            }
            else if(Main.worlds[worldID].getCellValue(x+vectorX,y+vectorY) == Cell.TYPE_BOT) {
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

    // Runs if bots health < 1
    private void die(int fireX,int fireY) {
        Main.worlds[worldID].getCell(x,y).removeBot();
        Main.worlds[worldID].getCell(fireX,fireY).update(Cell.TYPE_NOTHING);
        alive = false;
        Main.worlds[worldID].aliveBots--;
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

}
