package com.example.formtd.difficulty;

public class Hard implements Difficulty {
    public static final int waveGoldModifier = -1;
    public static final int livesModifier = -10;
    public static final float sellValueModifier = -0.1f;
    public static final double enemyHealthModifier = 1.25;
    public static final int startGoldModifier = -10;

    public int getWaveGoldModifier() {
        return waveGoldModifier;
    }

    public int getLivesModifier() {
        return livesModifier;
    }

    public float getSellValueModifier() {
        return sellValueModifier;
    }

    public double getEnemyHealthModifier() {
        return enemyHealthModifier;
    }

    public int getStartGoldModifier(){
        return startGoldModifier;
    }

}
