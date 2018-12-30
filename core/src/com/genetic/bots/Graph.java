package com.genetic.bots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.genetic.bots.UI.UIStage;

import java.util.ArrayList;

public class Graph implements Disposable {
    public static final int WIDTH = 1175, HEIGHT = 105,X = 1175-WIDTH,Y = 0;
    private static Texture graphElement;
    private ArrayList<Float> bestFitnessFuncPerPopulation = new ArrayList<Float>();
    private int bestFitnessFuncOfAllTime = 1;
    private UIStage stage;
    private Label max,min;

    // Restores used memory
    @Override
    public void dispose() {
       // graphElement.dispose();
       // stage.dispose();
    }

    static {
        // Creating graph element
        Pixmap gp = new Pixmap(1,1,Pixmap.Format.RGBA4444);
        gp.setColor(new Color(0.04f,0.04f,0.1f,0.3f));
        gp.fill();
        graphElement = new Texture(gp);
        gp.dispose();
    }

    public Graph() {

        for (int i = 0; i < WIDTH; i++) {
            add(0);
        }

        stage = new UIStage();
        Skin skin = new Skin(Gdx.files.internal("data/skin/cloud-form-ui.json"));
        min = new Label("0",skin);
        min.setX(X-min.getWidth()+WIDTH);
        min.setY(Y);
        max = new Label("1",skin);
        max.setX(X-max.getWidth()+WIDTH);
        max.setY(Y+HEIGHT);
        stage.addActor(min);
        stage.addActor(max);
    }

    // Add new population result for graph
    public void add(float value) {
        if(value>bestFitnessFuncOfAllTime) {
            bestFitnessFuncOfAllTime = (int) value;
            max.setText((int)value+"");
            max.setX(X-((max.getWidth()+4)*(max.getText().length-1))+WIDTH);
        }
        bestFitnessFuncPerPopulation.add(value);
        if(bestFitnessFuncPerPopulation.size()>WIDTH)bestFitnessFuncPerPopulation.remove(0);
    }

    // Draw all graph elements
    public void render() {
        try {
            for (int i = 0; i < WIDTH; i++) {
                Paint.drawGraphElement(graphElement, i, Y, bestFitnessFuncPerPopulation.get(bestFitnessFuncPerPopulation.size() - (WIDTH - i)) / bestFitnessFuncOfAllTime);
            }
            stage.draw();
        }
        catch (Exception e){

        }

    }
}
