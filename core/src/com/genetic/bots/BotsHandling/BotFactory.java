package com.genetic.bots.BotsHandling;

import com.genetic.bots.Config;

import java.util.Random;

public class BotFactory {
    private static Random random = new Random();
    public static int POINTS_PER_SAVED_PEOPLE = 10;
    public static int POINTS_PER_EXTINGUISHED_FIRE = 4;

    public BotFactory() {

    }

    public Bot createNewBot() { // Creating new bot with random genes
        Gene[] genes = new Gene[Config.BOTS_MEMORY_SIZE];

        for (int i = 0; i < genes.length; i++) {
            genes[i] = new Gene((byte) random.nextInt(64)); // Generating random genes
        }

        return new Bot(genes);
    }

    public Bot mutate(Bot bot,float chanceToMutateAnotherOneGene) { // Mutate bot
        do {
            bot.mutateOneGene();
        } while (chanceToMutateAnotherOneGene > Math.random());

        return bot;
    }

    public Bot generateByChromosome(Bot bot) {
        return new Bot(bot.getChromosome().clone());
    }



}
