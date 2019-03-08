/**
 * GameInfo class
 * @author Matt Doyle (mwdoyle), Emma Hughes (eha38)
 *
 */

package com.example.matt.minesweeper.model;

public class GameInfo {
    private static final int MIN_NUM_OF_ROWS = 4;
    private static final int MIN_NUM_OF_COLS = 6;
    private static final int MIN_NUM_OF_MINES = 6;
    private static final int MIN_NUM_OF_TIMES_PLAYED = 0;

    private int numRows;
    private int numCols;
    private int numMines;
    private int numTimesPlayed;
    private static GameInfo instance;

    private GameInfo(){
        // Private constructor to prevent anyone else from instantiating
        numRows = MIN_NUM_OF_ROWS;
        numCols = MIN_NUM_OF_COLS;
        numMines = MIN_NUM_OF_MINES;
        numTimesPlayed = MIN_NUM_OF_TIMES_PLAYED;
    }

    public static GameInfo getInstance(){
        if(instance == null){
            instance = new GameInfo();
        }

        return instance;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }

    public int getNumMines() {
        return numMines;
    }

    public void setNumMines(int numMines) {
        this.numMines = numMines;
    }

    public int getNumTimesPlayed() {
        return numTimesPlayed;
    }

    public void setNumTimesPlayed(int numTimesPlayed) {
        this.numTimesPlayed = numTimesPlayed;
    }

    public void incrementTimePlayed() {
        numTimesPlayed += 1;
    }
}

