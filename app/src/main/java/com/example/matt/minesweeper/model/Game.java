/**
 * Game class
 *
 * @author Matt Doyle (mwdoyle), Emma Hughes (eha38)
 */

package com.example.matt.minesweeper.model;

import java.util.Random;

// Source: http://www.progressivejava.net/2012/10/How-to-make-a-Minesweeper-game-in-Java.html
public class Game {
    private int[][] boardGame;
    private int[][] buttonsSelected;
    private int clicks = 0;
    private int numRow;
    private int numCol;
    private int mines;
    private int minesFound;

    public Game(GameInfo gameInfo) {
        GameInfo gameInformation = gameInfo;
        numRow = gameInformation.getNumRows();
        numCol = gameInformation.getNumCols();
        mines = gameInformation.getNumMines();
        buttonsSelected = new int[numRow][numCol];
        boardGame = new int[numRow][numCol];

        initializeButtonsSelected();
        initializeGameBoard();
        setRandomMines();
        countMineLocations();
    }

    public void initializeButtonsSelected() {
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                buttonsSelected[i][j] = 0;
            }
        }
    }

    public void initializeGameBoard() {
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                boardGame[i][j] = 0;
            }
        }
    }

    public void setRandomMines() {
        Random random = new Random();

        for (int i = 0; i < mines; i++) {
            int mineX, mineY;
            do {
                mineX = random.nextInt(numRow);
                mineY = random.nextInt(numCol);
            } while (boardGame[mineX][mineY] == -1);

            boardGame[mineX][mineY] = -1;
        }
    }

    private void countMineLocations() {
        // Go through board
        for (int Row = 0; Row < numRow; Row++) {
            for (int Column = 0; Column < numCol; Column++) {
                // if mine (-1) move on
                if ( boardGame[Row][Column] != -1) {
                    // if > 0 run  int checkForMine(); put int into board game position; move on
                    boardGame[Row][Column] = checkForMines(Row, Column);
                }
            }
        }
    }

    public void setBoardGame( int row, int col, int value) {
        this.boardGame[row][col] = value;
    }

    public int getNumRow() {
        return numRow;
    }

    public int getNumCol() {
        return numCol;
    }

    public int getMines() {
        return mines;
    }

    public int getMinesFound() {
        return minesFound;
    }

    public void incrementMinesFound() {
        this.minesFound++;
    }

    public int getClicks() {
        return clicks;
    }

    public int getButtonsSelected(int row, int col) {
        return buttonsSelected[row][col];
    }

    public void incrementButtonsSelected(int row, int col) {
        buttonsSelected[row][col] += 1;
    }

    public int checkForMines(int row, int col) {
        int minesFound = 0;

        // Check rows for mines
        for (int i = 0; i < numCol; i++) {
            if ( boardGame[row][i] == -1) {
                minesFound++;
            }
        }

        // Check columns for mines
        for (int j = 0; j < numRow; j++) {
            if ( boardGame[j][col] == -1) {
                minesFound++;
            }
        }
        return minesFound;
    }

    public int checkIfMine(int row, int col) {
        return boardGame[row][col];
    }

    public void reduceMineCount(int row, int col) {
        // Check rows for mines
        for (int i = 0; i < numCol; i++) {
            if ( boardGame[row][i] > 0) {
                boardGame[row][i] -= 1;
            }
        }

        // Check columns for mines
        for (int j = 0; j < numRow; j++) {
            if ( boardGame[j][col] > 0) {
                boardGame[j][col] -= 1;
            }
        }
    }

    public int getBoardGamePosition(int row, int col) {
        int position;
        position = boardGame[row][col];
        return position;
    }

    public void incrementClicks() {
        clicks += 1;
    }

    public int checkForMinesAtMine(int row, int col) {
        int minesFound = 0;

        // Check rows for mines
        for (int i = 0; i < numCol; i++) {
            if ( boardGame[row][i] == -1 && buttonsSelected[row][i] < 1) {
                minesFound++;
            }
        }

        // Check columns for mines
        for (int j = 0; j < numRow; j++) {
            if ( boardGame[j][col] == -1 && buttonsSelected[j][col] < 1) {
                minesFound++;
            }
        }
        return minesFound;
    }
}
