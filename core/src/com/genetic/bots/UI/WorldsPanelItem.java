package com.genetic.bots;

public class WorldsPanelItem {
    private Texture createWorld, world;

    public WorldsPanelItem(int order) {

        // Textures initialization
        createWorld = new Texture(Gdx.files.internal("createWorld.png"));
        world = new Texture(Gdx.files.internal("worldIcon.png"));
    }

    // Draw ...
    public void render() {

    }
}
