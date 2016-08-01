package com.demo.minesweeper.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Random;

import com.demo.minesweeper.resources.Board;
import com.demo.minesweeper.utils.CommonConstants;

public class Minesweeper {

    public Minesweeper() {
        super();
    }

    /*
     * Methods to init the game
     */

    /**
     * Init the full game of Minesweeper
     * 
     * @return the board to play the game
     */
    public Board initGame(InputStream in,PrintStream out) {
        displayPresentation(out);

        // Get grid size from user
        int[] gridSize = getBoardSize(System.in, System.out);

        // Start a new board
        Board board = new Board(gridSize[0], gridSize[1]);

        // Get number of mines
        board.setNumberOfMines(getNumberOfMines(board.getNumberOfCells(),in, out));
        board.setRemainingCells(board.getNumberOfCells() - board.getNumberOfMines());

        // Place the mines on the grid
        placeMinesOnBoard(board);

        // Print the grid
        displayBoard(board,out);

        return board;
    }

    /**
     * Ask user for the board size
     * 
     * @return the board size
     */
    public int[] getBoardSize(InputStream in,PrintStream out) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        // Ask user for grid size
        out.println(CommonConstants.USER_BOARD_SIZE);
        boolean twoNumbersEntered = false;
        String input = CommonConstants.EMPTY_STRING;
        int[] boardSize = new int[2];
        do {
            try {
                String regex = CommonConstants.REG_TWO_NUMBERS_SPACE;
                input = br.readLine();
                if (input != null && input.matches(regex)) {
                    String[] numbers = input.split(CommonConstants.REG_SPACE);
                    boardSize[0] = Integer.parseInt(numbers[0]);
                    boardSize[1] = Integer.parseInt(numbers[1]);
                    if (boardSize[0] < 1 || boardSize[1] < 1) {
                        out.println(CommonConstants.ERR_NOT_TWO_NUMBERS);
                    } else if (boardSize[0] == 1 && boardSize[1] == 1) {
                        // Board too small for mines
                        out.println(CommonConstants.ERR_BOARD_TOO_SMALL);
                    } else {
                        twoNumbersEntered = true;
                    }
                } else {
                    out.println(CommonConstants.ERR_NOT_TWO_NUMBERS);
                }
            } catch (IOException io) {
                out.println(CommonConstants.ERR_NOT_TWO_NUMBERS);
                // Logging error
            } catch (NumberFormatException nfe) {
                out.println(CommonConstants.ERR_TOO_LONG_FOR_INT);
                // Logging error
            }
        } while (!twoNumbersEntered);

        // Create the grid
        return boardSize;
    }

    /**
     * Ask user for number of mines
     * 
     * @param numberCells
     * @return number of mines to put on the board
     */
    public int getNumberOfMines(int numberOfCells, InputStream in,PrintStream out) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        // Ask for number of mines
        out.println(CommonConstants.USER_NUMBER_MINES);
        String input = CommonConstants.EMPTY_STRING;
        int numberOfMines = 0;
        do {
            try {
                String regex = CommonConstants.REG_NUMBER;
                input = br.readLine();
                if (input != null && input.matches(regex)) {
                    numberOfMines = Integer.parseInt(input);
                    if (numberOfMines >= numberOfCells) {
                        out.println(String.format(CommonConstants.ERR_TOO_MUCH_MINES, (numberOfCells - 1)));
                        numberOfMines = 0;
                    } else if (numberOfMines < 1) {
                        out.println(CommonConstants.ERR_NOT_VALID_NUMBER);
                        numberOfMines = 0;
                    }
                } else {
                    out.println(CommonConstants.ERR_NOT_VALID_NUMBER);
                }
            } catch (IOException io) {
                out.println(CommonConstants.ERR_NOT_VALID_NUMBER);
                // Logging error
            } catch (NumberFormatException nfe) {
                out.println(CommonConstants.ERR_TOO_LONG_FOR_INT);
                // Logging error
            }
        } while (numberOfMines == 0);
        return numberOfMines;
    }

    /**
     * Generate mines and place them on the board
     * 
     * @param board
     * @return the updated board with mines on the grid
     */
    public Board placeMinesOnBoard(Board board) {
        Random rand = new Random();
        int count = 0;
        int x;
        int y;
        while (count < board.getNumberOfMines()) {
            x = rand.nextInt(board.getWidth());
            y = rand.nextInt(board.getHeight());
            // -1 is for mines so we check on the position if this is not
            // already one
            if (board.getSolution()[x][y] != -1) {
                board.getSolution()[x][y] = -1;
                count++;
                // Then we increment all cells around (except for mines)
                // which is faster than incrementing after having generated all
                // the mines
                incrementCellsAroundMine(x, y, board);
            }
        }
        return board;
    }

    /**
     * Calculate the number of adjacent mines for each cell
     * 
     * @param x
     * @param y
     * @param board
     * @return the updated board with number of adjacent mines for each cell
     */
    public Board incrementCellsAroundMine(int x, int y, Board board) {
        // Determine the min/max around
        // Ternary is the same cost as Math.min/max
        int left = Math.max(0, x - 1);
        int right = Math.min(x + 1, board.getWidth() - 1);
        int top = Math.max(0, y - 1);
        int bottom = Math.min(y + 1, board.getHeight() - 1);

        for (int i = left; i <= right; i++) {
            for (int j = top; j <= bottom; j++) {
                if (board.getSolution()[i][j] != -1) {
                    board.getSolution()[i][j]++;
                }
            }
        }
        return board;
    }

    /*
     * Methods to play the game
     */

    /**
     * Loop to play the game
     * 
     * @param board
     */
    public void playGame(Board board, InputStream in, PrintStream out) {
        boolean isMineDiscovered = false;
        do {
            isMineDiscovered = unveilCeil(board, in, out);
            displayBoard(board, out);
            out.println(String.format(CommonConstants.INFO_REMAINING_CELLS, board.getRemainingCells()));
        } while (!isMineDiscovered && board.getRemainingCells() > 0);
        if (isMineDiscovered) {
            out.println(CommonConstants.INFO_GAME_LOST);
        } else {
            out.println(CommonConstants.INFO_GAME_WON);
        }
        out.println(CommonConstants.INFO_SOLUTION);
        displayBoard(board, true, out);
    }

    /**
     * Ask user a cell to unveil and check it
     * 
     * @param board
     * @return true if mine is discovered on board
     */
    public boolean unveilCeil(Board board, InputStream in,PrintStream out) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        boolean mineIsDiscovered = false;
        int x = -1, y = -1;
        // Ask for a position x y
        out.println(CommonConstants.USER_CELL_TO_UNVEIL);
        String input = CommonConstants.EMPTY_STRING;
        boolean twoNumbersEntered = false;
        do {
            try {
                String regex = CommonConstants.REG_TWO_NUMBERS_SPACE;
                input = br.readLine();
                // Check user's input
                if (input != null && input.matches(regex)) {
                    String[] numbers = input.split(CommonConstants.REG_SPACE);
                    x = Integer.parseInt(numbers[0]);
                    y = Integer.parseInt(numbers[1]);
                    if (x < 0 || x >= board.getWidth() || y < 0 || y >= board.getHeight()) {
                        out.println(CommonConstants.ERR_OUT_OF_BOARD);
                    } else if (board.getBoardGame()[x][y]) {
                        out.println(CommonConstants.ERR_ALREADY_UNVEILED);
                    } else {
                        twoNumbersEntered = true;
                    }
                } else {
                    // Not a number
                    out.println(CommonConstants.ERR_NOT_TWO_NUMBERS);
                }
            } catch (IOException io) {
                out.println(CommonConstants.ERR_NOT_TWO_NUMBERS);
                // Logging error
            } catch (NumberFormatException nfe) {
                // This exception cannot happen because x, y are on the board
                out.println(CommonConstants.ERR_TOO_LONG_FOR_INT);
                // Logging error
            }
        } while (!twoNumbersEntered);

        // Check if not mine
        if (board.getSolution()[x][y] == -1) {
            mineIsDiscovered = true;
            // Unveil cell for result
            board.getBoardGame()[x][y] = true;
        } else if (board.getSolution()[x][y] == 0) {
            // Display around cells
            unveilAroundCells(x, y, board);
        } else {
            board.getBoardGame()[x][y] = true;
            board.setRemainingCells(board.getRemainingCells() - 1);
        }

        return mineIsDiscovered;
    }

    /**
     * Unveil all cells around the given cell (supposed to be with no adjacent
     * mines)
     * 
     * @param x
     * @param y
     * @param board
     * @return the updated board with unveiled cells at (x, y) and around
     */
    public Board unveilAroundCells(int x, int y, Board board) {
        // Determine the cells around
        // Rem : ternary is the same cost as Math.min/max so for readability we
        // use Math.min/max
        int left = Math.max(0, x - 1);
        int right = Math.min(x + 1, board.getWidth() - 1);
        int top = Math.max(0, y - 1);
        int bottom = Math.min(y + 1, board.getHeight() - 1);

        for (int i = left; i <= right; i++) {
            for (int j = top; j <= bottom; j++) {
                if (!board.getBoardGame()[i][j]) {
                    board.getBoardGame()[i][j] = true;
                    board.setRemainingCells(board.getRemainingCells() - 1);
                    if (board.getSolution()[i][j] == 0) {
                        unveilAroundCells(i, j, board);
                    }
                }
            }
        }
        return board;
    }

    /*
     * Utilitary methods
     */

    /**
     * Display the board on screen Use showSolution (true) to display the whole
     * unveiled board
     * 
     * @param board
     * @param showSolution
     */
    public void displayBoard(Board board, boolean showSolution,PrintStream out) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < board.getWidth(); i++) {
            line.append(CommonConstants.LEFT_BRACKET);
            for (int j = 0; j < board.getHeight(); j++) {
                if (board.getBoardGame()[i][j] || showSolution) {
                    // Cell is unveiled
                    if (board.getSolution()[i][j] != -1) {
                        // If not mine, we add two spaces to align perfectly
                        line.append(CommonConstants.WHITESPACE);
                    }
                    line.append(CommonConstants.WHITESPACE + board.getSolution()[i][j]);
                } else {
                    // Cell is hidden
                    line.append(CommonConstants.WHITESPACE + CommonConstants.WHITESPACE + CommonConstants.STAR);
                }
            }
            line.append(CommonConstants.WHITESPACE + CommonConstants.RIGHT_BRACKET);
            out.println(line.toString());
            line = new StringBuilder();
        }
    }

    public void displayBoard(Board board,PrintStream out) {
        displayBoard(board, false,out);
    }

    /**
     * Print the presentation of the game in the console
     */
    public void displayPresentation(PrintStream out) {
        out.println(CommonConstants.MSG_LINE_SEPARATOR);
        out.println(CommonConstants.MSG_INTRODUCTION);
        out.println(CommonConstants.MSG_RULES);
        out.println(CommonConstants.MSG_GOOD_LUCK);
        out.println(CommonConstants.MSG_LINE_SEPARATOR);
    }
}
