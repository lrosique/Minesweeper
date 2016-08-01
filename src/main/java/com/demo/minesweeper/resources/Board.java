package com.demo.minesweeper.resources;

import java.util.Arrays;

public class Board {

    private int width;

    private int height;

    private int numberOfCells;

    private int numberOfMines;

    private int[][] solution;

    private boolean[][] boardGame;

    private int remainingCells;

    public Board(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        this.numberOfCells = width * height;
        this.solution = new int[height][width];
        this.numberOfMines = 0;
        this.boardGame = new boolean[height][width];
        this.remainingCells = this.numberOfCells;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getNumberOfCells() {
        return numberOfCells;
    }

    public int getNumberOfMines() {
        return numberOfMines;
    }

    public void setNumberOfMines(int numberOfMines) {
        this.numberOfMines = numberOfMines;
    }

    public int[][] getSolution() {
        return solution;
    }

    public void setSolution(int[][] solution) {
        this.solution = solution;
    }

    public boolean[][] getBoardGame() {
        return boardGame;
    }

    public void setBoardGame(boolean[][] boardGame) {
        this.boardGame = boardGame;
    }

    public int getRemainingCells() {
        return remainingCells;
    }

    public void setRemainingCells(int remainingCells) {
        this.remainingCells = remainingCells;
    }

    @Override
    public String toString() {
        return "Board [width=" + width + ", height=" + height + ", numberOfCells=" + numberOfCells + ", numberOfMines="
                + numberOfMines + ", solution=" + Arrays.toString(solution) + ", boardGame="
                + Arrays.toString(boardGame) + ", remainingCells=" + remainingCells + "]";
    }

}
