package com.genetic.bots;

import com.genetic.bots.WorldsHandling.World;

public class WorldUpdater extends Thread {
    private World world;
    //public boolean active = true;

    public WorldUpdater(World world) {
        this.world = world;
    }

    public synchronized void setWorld(World world) {
        this.world = world;
    }

    // Main loop for each world; Speed based on Config.SPEED value
    @Override
    public void run() {
        super.run();
        try {
        while (true) {
            world.update();
            Thread.sleep(1024/Config.SPEED);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
