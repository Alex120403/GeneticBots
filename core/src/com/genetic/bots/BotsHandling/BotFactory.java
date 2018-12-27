package com.genetic.bots.BotsHandling;

import com.genetic.bots.Config;

import java.util.Random;

public class BotFactory {
    private static Random random = new Random();
    public static int POINTS_PER_SAVED_PEOPLE = 10;
    public static int POINTS_PER_EXTINGUISHED_FIRE = 4;

    public BotFactory() {

    }

    // Creates new bot with random genes
    public Bot createNewBot() {
        Gene[] genes = new Gene[Config.BOTS_MEMORY_SIZE];

        // Generating random genes
        for (int i = 0; i < genes.length; i++) {
            genes[i] = new Gene((byte) random.nextInt(64));
        }

        return new Bot(genes);
    }

    // Changes 1 to ? bot's genes to random values
    public Bot mutate(Bot bot,float chanceToMutateAnotherOneGene) {
        do {
            bot.mutateOneGene();
        } while (chanceToMutateAnotherOneGene > Math.random());

        return bot;
    }

    // Creation new bot with cloned chromosome of @param bot
    public Bot generateByChromosome(Bot bot) {
        return new Bot(bot.getChromosome().clone());
    }



}
