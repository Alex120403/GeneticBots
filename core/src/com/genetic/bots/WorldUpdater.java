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
<<<<<<< HEAD
        try {
            while (true) {
                world.update();
                Thread.sleep(1024/Config.SPEED);
            }
        } catch (Exception e) {
            e.printStackTrace();
=======
        while (active) {
            world.update();
            try {
                Thread.sleep(1024/Config.SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
>>>>>>> parent of 9e12590... WorldsItem (85%...)
        }
    }

    public void testWait() {
        final long INTERVAL = 65536;
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while(start + INTERVAL >= end);
    }
}
