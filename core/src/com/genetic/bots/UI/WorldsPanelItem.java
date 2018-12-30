package com.genetic.bots.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.genetic.bots.InputHandler;
import com.genetic.bots.InputObserver;
import com.genetic.bots.Main;
import com.genetic.bots.Paint;
import com.genetic.bots.WorldsHandling.World;

public class WorldsPanelItem implements InputObserver {
    private static final Texture createWorld, world, flash,startIcon,pause,delete;
    private int order,populations;
    private long bestFitnessFuncOfAllTime;
    private boolean visible,flashing,click,start;
    private BitmapFont nameFont, stateFont;
    private World linkedWorld;
    int Y_OFFSET = 570;
    int X_OFFSET = 30;

    //TODO dispose
    // Textures initialization
    static {
        createWorld = new Texture(Gdx.files.internal("createWorld.png"));
        world = new Texture(Gdx.files.internal("worldIcon.png"));
        flash = new Texture(Gdx.files.internal("createWorldFlash.png"));
        startIcon = new Texture(Gdx.files.internal("start.png"));
        pause = new Texture(Gdx.files.internal("pause.png"));
        delete = new Texture(Gdx.files.internal("delete.png"));

    }

    public WorldsPanelItem(int order) {
        this.order = order;


        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.DARK_GRAY;
        parameter.size = 18;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("9522.ttf"));
        nameFont = generator.generateFont(parameter);
        parameter.size = 14;
        stateFont = generator.generateFont(parameter);
        generator.dispose();

        InputHandler.addToObservers(this);
    }

    // Draw button "Create new world" or "Short world state"
    public void render() {
        if(order == 0 || linkedWorld!=null || Main.worlds[order-1]!=null){
            visible = true;
        }
        if(visible) {
            if(linkedWorld == null) {
                Paint.draw(createWorld,X_OFFSET,Y_OFFSET-(order*(createWorld.getHeight()+6)));
            }
            else {
                Paint.drawWorldPanelItem(world, nameFont, stateFont, linkedWorld.getName(), bestFitnessFuncOfAllTime, populations, order);
                Paint.draw(delete,X_OFFSET+createWorld.getWidth()+4,Y_OFFSET-(order*(createWorld.getHeight()+6))+4);
                if(start) {
                    Paint.draw(pause,X_OFFSET+createWorld.getWidth()+4,Y_OFFSET-(order*(createWorld.getHeight()+6))+createWorld.getHeight()/2+4);
                }
                else {
                    Paint.draw(startIcon,X_OFFSET+createWorld.getWidth()+4,Y_OFFSET-(order*(createWorld.getHeight()+6))+createWorld.getHeight()/2+4);

                }
            }
            if (flashing) {
                Paint.draw(flash, X_OFFSET, Y_OFFSET - (order * (flash.getHeight() + 6)));
            }
            if (click) {
                Paint.draw(flash, X_OFFSET, Y_OFFSET - (order * (flash.getHeight() + 6)));
                click();
                click = false;
            }
        }
    }

    // Run if user clicks on this button
    public void click() {
        if(linkedWorld == null) {
            Main.worlds[order] = new World(null,32,this,null);
            linkedWorld = Main.worlds[order];
            bestFitnessFuncOfAllTime = 0;
            populations = 0;
        }
        else {
            Main.setSelectedWorldID(order);
        }
    }

    // Returns order
    public int getOrder() {
        return order;
    }

    // Returns true if visible
    public boolean isVisible() {
        return visible;
    }

    // Can set new bestFitnessFuncOfAllTime
    private void tryNewBestFitnessFunc(long ff) {
        if(ff>bestFitnessFuncOfAllTime) {
            bestFitnessFuncOfAllTime = ff;
        }
    }

    // Run when linked world creates new population
    public void nextPopulation(long ff) {
        tryNewBestFitnessFunc(ff);
        populations++;
    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in {@link com.badlogic.gdx.Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link com.badlogic.gdx.Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link com.badlogic.gdx.Input.Buttons} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(flashing) {
            click = true;
        }
        else if(linkedWorld!=null){
            Y_OFFSET = 570-(order*(createWorld.getHeight()+6));
            if(screenX>X_OFFSET+createWorld.getWidth() && screenX<X_OFFSET+createWorld.getWidth()+createWorld.getHeight()/2){
                if(screenY>Y_OFFSET && screenY<Y_OFFSET+createWorld.getHeight()/2){
                    linkedWorld.dispose();
                    linkedWorld = null;
                    Main.worlds[order] = null;
                    if(Main.getSelectedWorldID() == order) {
                        Main.setSelectedWorldID(-1);
                    }
                }
                else if(screenY>Y_OFFSET+createWorld.getHeight()/2 && screenY<Y_OFFSET+createWorld.getHeight()){
                    start = !start;
                }
            }
            Y_OFFSET = 570;
        }
        return false;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link com.badlogic.gdx.Input.Buttons} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if(visible && screenX>X_OFFSET && screenX<X_OFFSET+createWorld.getWidth() && screenY>570-(order*(createWorld.getHeight()+6)) && screenY<570-(order*(createWorld.getHeight()+6))+createWorld.getHeight()) {
            flashing = true;
        }
        else {
            flashing = false;
        }
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public boolean isStart() {
        return start;
    }
}
