package com.genetic.bots.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.genetic.bots.Config;

public class WorldCreating {
    Label botsCountLabel,botsCountMin,botsCountMax;
    Slider botsCount;
    Stage stage;

    public WorldCreating() {
        stage = new Stage();

        Skin skin = new Skin(Gdx.files.internal("data/skin/cloud-form-ui.json"));

        botsCount = new Slider(8,256,8,false,skin);
        botsCount.setY(30);
        botsCount.setX(100);
        botsCountMin = new Label("8",skin);
        botsCountMin.setY(botsCount.getY());
        botsCountMin.setX(botsCount.getX()-botsCountMin.getWidth());
        stage.addActor(botsCountMin);
        botsCountMax = new Label("256",skin);
        botsCountMax.setY(botsCount.getY());
        botsCountMax.setX(botsCount.getX()+botsCount.getWidth());
        stage.addActor(botsCountMax);

        botsCount.setValue(Config.BOTS_COUNT);
        botsCount.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                Config.BOTS_COUNT = (int)botsCount.getValue();
                botsCountLabel.setText("Bots count: "+(int)botsCount.getValue());
                return false;
            }
        });

        stage.addActor(botsCount);

        botsCountLabel = new Label("Bots count: "+(int)botsCount.getValue(),skin);
        botsCountLabel.setX(botsCount.getX());
        botsCountLabel.setY(botsCount.getY()+botsCount.getHeight());
        stage.addActor(botsCountLabel);
    }
}
