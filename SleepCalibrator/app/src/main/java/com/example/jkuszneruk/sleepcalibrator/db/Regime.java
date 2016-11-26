package com.example.jkuszneruk.sleepcalibrator.db;

import java.util.Date;

public class Regime {
    public static final int DEFAULT_REGIME_LENGTH = 5;
    private Date wakeUpTime;
    private Date startDate;
    private int sleepLength;  // in minutes
    private int regimeLength;  // in days

    private Integer[] scores;
    private Integer[] moods;
    private Integer[] energies;

    public Regime(Date wakeUpTime, Date startDate, int sleepLength, int regimeLength) {
        super();
        this.wakeUpTime = wakeUpTime;
        this.startDate = startDate;
        this.sleepLength = sleepLength;
        this.regimeLength = regimeLength;

        this.scores = new Integer[regimeLength];
        this.moods = new Integer[regimeLength];
        this.energies = new Integer[regimeLength];
    }

    public Date getWakeUpTime() {
        return wakeUpTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public int getSleepLength() {
        return sleepLength;
    }

    public int getRegimeLength() {
        return regimeLength;
    }

    public Integer[] getScores() {
        return scores;
    }

    public void setScores(Integer[] scores) {
        this.scores = scores;
    }

    public int getScore(int i) {
        return scores[i];
    }

    public void setScore(int i, int score) {
        this.scores[i] = score;
    }

    public Integer[] getMoods() {
        return moods;
    }

    public void setMoods(Integer[] moods) {
        this.moods= moods;
    }

    public int getMood(int i) {
        return moods[i];
    }

    public void setMood(int i, int mood) {
        this.moods[i] = mood;
    }

    public Integer[] getEnergies() {
        return energies;
    }

    public void setEnergies(Integer[] energies) {
        this.energies = energies;
    }

    public int getEnergy(int i) {
        return energies[i];
    }

    public void setEnergy(int i, int energy) {
        this.energies[i] = energy;
    }

    public String toString() {
        return "regime@" + startDate + " (" + startDate.getTime() / 1000 + ")";
    }
}
