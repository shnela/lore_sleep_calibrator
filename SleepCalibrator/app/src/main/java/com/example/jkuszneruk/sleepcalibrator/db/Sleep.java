package com.example.jkuszneruk.sleepcalibrator.db;

import java.util.Date;

public class Sleep {
    private Date wakeUpDate;
    private int sleepLength;  // in minutes
    private int score;
    private int mood;
    private int energy;

    public Sleep(Date wakeUpDate, int sleepLength, int score, int mood, int energy) {
        this.wakeUpDate = wakeUpDate;
        this.sleepLength = sleepLength;
        this.score = score;
        this.mood = mood;
        this.energy = energy;
    }

    public Date getWakeUpDate() {
        return wakeUpDate;
    }

    public void setWakeUpDate(Date wakeUpDate) {
        this.wakeUpDate = wakeUpDate;
    }

    public int getSleepLength() {
        return sleepLength;
    }

    public void setSleepLength(int sleepLength) {
        this.sleepLength = sleepLength;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public String toString() {
        return "Sleep@" + wakeUpDate + " (" + sleepLength / 60 + " h): "
                + "score: " + score + ", mood: " + mood + ", energy: " + energy;
    }
}
