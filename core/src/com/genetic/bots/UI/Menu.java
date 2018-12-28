package com.genetic.bots.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Disposable;
import com.genetic.bots.Config;
import com.genetic.bots.Main;
import com.genetic.bots.Paint;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

public class Menu implements Disposable {
    Slider speed;
    Button start,stop,pause;
    Label speedLabel,javaHeap;
    UIStage stage;
    Main main;
    int timer = 0;
    Texture hint;

    @Override
    public void dispose() {
        stage.dispose();
        hint.dispose();
    }

    public Menu(final Main main) {
        stage = new UIStage();
        hint = new Texture(Gdx.files.internal("previewHint.png"));
        this.main = main;
        Skin skin = new Skin(Gdx.files.internal("data/skin/cloud-form-ui.json"));
        speed = new Slider(0,11,1,false,skin);
        speed.setX(500);
        speed.setY(10);

        speed.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(speed.getValue() == 11 && Config.IS_DEVELOPER_MODE_ENABLED) {
                    speedLabel.setText("Speed: "+(int)speed.getValue()+"  WARNING: Exception can be thrown!\nThis mode requires a lot of memory");
                    speedLabel.setY(speed.getY()+speed.getHeight()+speedLabel.getHeight()/2);
                    Config.SPEED = (int) Math.pow(2, (int) speed.getValue());
                }
                else if(speed.getValue()!=11){
                    Config.SPEED = (int) Math.pow(2, (int) speed.getValue());
                    speedLabel.setText("Speed: " + (int) speed.getValue());
                }

                return false;
            }
        });
        speedLabel = new Label("Speed: ",skin);
        speedLabel.setY(speed.getY()+speed.getHeight());
        speedLabel.setX(speed.getX());
        speed.setValue(Config.SPEED);
        stage.addActor(speed);
        stage.addActor(speedLabel);
        javaHeap = new Label("Java heap: ???",skin);
        javaHeap.setX(500);
        javaHeap.setY(50);
        stage.addActor(javaHeap);

        start = new Button(skin);
        start.add("Start");
        start.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(!main.isStarted()) {
                    main.start();
                }
                return false;
            }
        });
        start.setX(100);
        //stage.addActor(start);
        pause = new Button(skin);
        pause.add("Pause");
       // stage.addActor(pause);
        pause.setX(200);
        stop = new Button(skin);
        stop.add("Stop");
        stop.setX(300);
       // stage.addActor(stop);

    }

    // Draw all menu components
    public void render() {

        stage.draw();
        if(!main.isStarted())Paint.draw(hint,360,300);
        /*if(speed.getValue() == 11 && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            speed.setValue(10);
            Config.SPEED = 1024;
            speedLabel.setY(speed.getY()+speed.getHeight());
        }*/
        timer++;
        if(timer>=6) {
            timer = 0;
            javaHeap.setText("Java heap: "+Gdx.app.getJavaHeap()/1024/1024+"Mb");
        }
    }
}
