package com.genetic.bots.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;
import com.genetic.bots.BotsHandling.Gene;
import com.genetic.bots.Main;
import com.genetic.bots.Paint;
import com.genetic.bots.WorldsHandling.Cell;
import com.genetic.bots.WorldsHandling.World;


public class ChromosomeDisplay implements Disposable {
    private static final int X_ALIGNMENT = 5,Y_ALIGNMENT = 470;

    private Gene[] chromosome;
    private String[] labels;
    private Texture box,flag;
    private BitmapFont font;
    public ChromosomeBot botInfo;

    // Restores used memory
    @Override
    public void dispose() {
        box.dispose();
        flag.dispose();
        font.dispose();
    }

    public ChromosomeDisplay(Gene[] chromosome) {
        this.chromosome = chromosome;
        labels = new String[chromosome.length];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = chromosome[i].getValue()+"";

        }
        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("9522.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.color = Color.BLACK;
        font = freeTypeFontGenerator.generateFont(parameter);
        freeTypeFontGenerator.dispose();

        box = new Texture("grid.png");
        flag = new Texture("operationFlag.png");
        botInfo = new ChromosomeBot();
    }

    // Refreshes all labels (sets new text based on new chromosome)
    private void refreshLabels() {
        for (int i = 0; i < labels.length; i++) {
            labels[i]=chromosome[i].getValue()+"";
        }
    }

    // Sets new chromosome for display
    public void setChromosome(Gene[] newChromosome) {
        chromosome = newChromosome;
        refreshLabels();
    }

    // Draw current chromosome content
    public void render() {
        botInfo.render();
        for (int i = 0; i < Math.sqrt(chromosome.length); i++) {
            for (int j = 0; j < Math.sqrt(chromosome.length); j++) {
                Paint.draw(box, X_ALIGNMENT + i * Cell.CELL_SIZE, Y_ALIGNMENT + j * Cell.CELL_SIZE);
            }
        }

        for (int i = 0; i < labels.length; i++) {
            Paint.drawText(font,labels[i],X_ALIGNMENT+ ((((int) (i % Math.sqrt(labels.length))))*(Cell.CELL_SIZE+0.1f))+27-Cell.CELL_SIZE,Y_ALIGNMENT+ ((7-((int) (i / Math.sqrt(labels.length))))*(Cell.CELL_SIZE+0.1f))+16);
        }

    }

    // Lights current bot's gene
    public void drawFlag() {
        Paint.draw(flag,X_ALIGNMENT + (7-(Main.worlds[Main.getSelectedWorldID()].bestBot.operationFlag%8)) * Cell.CELL_SIZE, Y_ALIGNMENT + (7-((Main.worlds[Main.getSelectedWorldID()].bestBot.operationFlag%64)/8)) * Cell.CELL_SIZE);
    }
}
