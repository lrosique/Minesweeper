package com.demo.minesweeper.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import com.demo.minesweeper.resources.Board;
import com.demo.minesweeper.service.Minesweeper;

import junit.framework.TestCase;

public class MinesweeperTest extends TestCase {
    
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Minesweeper minesweeper = new Minesweeper();

    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    public void tearDown() {
        System.setOut(null);
        System.setErr(null);
    }

    public void testMinesweeper() {
        Minesweeper minesweeper = new Minesweeper();
        assertNotNull(minesweeper);
    }
    
//    public void testInitGame() {
//        fail("Not yet implemented");
//    }
//
    
    public void testGetBoardSize() {
        //Given data is correct
        String data = "2 5";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        System.setOut(new PrintStream(outContent));
        int[] boardSize = minesweeper.getBoardSize(System.in, System.out);
        //Expect a board of 2 5
        assertNotNull(boardSize);
        assertEquals(boardSize[0], 2);
        assertEquals(boardSize[1], 5);
        
        //Given data is incorrect :
        //null, only one number, number is negative, number is over int, chars instead of int, wrong separator, one one, success
        data = "\n"+"1\n"+"-1 2\n"+"2 -1\n"+"2147483648 3\n"+"a 4\n"+"1  5\n"+"1 1\n"+"3 6";
        outContent = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        System.setOut(new PrintStream(outContent));
        boardSize = minesweeper.getBoardSize(System.in, System.out);
        //Expect a board of 2 6
        assertNotNull(boardSize);
        String expectedOutput = "Please enter two numbers for the board size (width heigth), separated by one space :\r\n"+
                "These are not two numbers, try again\r\n"+
                "These are not two numbers, try again\r\n"+
                "These are not two numbers, try again\r\n"+
                "These are not two numbers, try again\r\n"+
                "Number too long, try again\r\n"+
                "These are not two numbers, try again\r\n"+
                "These are not two numbers, try again\r\n"+
                "I guess you won. Not a challenger, are you ? Let's try again, enter width and height please\r\n";

        assertEquals(expectedOutput,outContent.toString());
        assertEquals(boardSize[0], 3);
        assertEquals(boardSize[1], 6);
    }
    
    public void testGetNumberOfMines() {
        int numberOfCells = 10;
        //Given data is correct
        String data = "3";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        System.setOut(new PrintStream(outContent));
        int numberOfMines = minesweeper.getNumberOfMines(numberOfCells, System.in, System.out);
        assertNotNull(numberOfMines);
        assertEquals(numberOfMines, 3);
        
        //Given data is incorrect
        //null, negative, no mines, over int, chars, spaces, too much mines, success
        data = "\n"+"-1\n"+"0\n"+"2147483648\n"+"a\n"+"1 \n"+"10\n"+"4";
        outContent = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        System.setOut(new PrintStream(outContent));
        numberOfMines = minesweeper.getNumberOfMines(numberOfCells, System.in, System.out);
        assertNotNull(numberOfMines);
        String expectedOutput = "How many mines do you want ?\r\n"+
                "This is not a (non zero) number, try again\r\n"+
                "This is not a (non zero) number, try again\r\n"+
                "This is not a (non zero) number, try again\r\n"+
                "Number too long, try again\r\n"+
                "This is not a (non zero) number, try again\r\n"+
                "This is not a (non zero) number, try again\r\n"+
                "Too much mines (max : 9) ! You can't win that way, try again\r\n";

        assertEquals(expectedOutput,outContent.toString());
        assertEquals(numberOfMines, 4);
    }

    public void testPlaceMinesOnBoard() {
        int width = 3;
        int height = 4;
        int mines = 5;
        Board board = new Board(width, height);
        board.setNumberOfMines(mines);
        minesweeper.placeMinesOnBoard(board);
        
        assertNotNull(board);
        assertEquals(board.getWidth(),width);
        assertEquals(board.getHeight(),height);
        assertEquals(board.getNumberOfCells(),width*height);
        assertEquals(board.getNumberOfMines(),mines);
        assertNotNull(board.getSolution());
        //Verify we have the good number of -1
        int count = 0;
        for (int i = 0; i<width; i++){
            for (int j=0; j<height; j++){
                if (board.getSolution()[i][j] == -1){
                    count++;
                }
            }
        }
        assertEquals(count, mines);
        //Number of adjacent mines is verified in incrementCellsAroundMine
    }

    public void testIncrementCellsAroundMine() {
        int width = 4;
        int height = 3;
        Board board = new Board(width, height);
        //Mines at 0 0 and 2 1
        int[][] solution = {{-1, 0, 0},
                            { 0, 0, 0},
                            { 0,-1, 0},
                            { 0, 0, 0}};
        board.setSolution(solution);
        //Incrementing around first mine
        //Expect change at (0,1), (1,0) and (1,1)
        //Should be +1 and not +2 since second mine was not calculated yet
        minesweeper.incrementCellsAroundMine(0,0,board);
        int[][] withFirstMine = {{-1, 1, 0},
                                 { 1, 1, 0},
                                 { 0,-1, 0},
                                 { 0, 0, 0}};
        assertNotNull(board.getSolution());
        assertTrue(Arrays.deepEquals(board.getSolution(),withFirstMine));
        //Second mine
        minesweeper.incrementCellsAroundMine(2,1,board);
        int[][] withSecondMine = {{-1, 1, 0},
                                  { 2, 2, 1},
                                  { 1,-1, 1},
                                  { 1, 1, 1}};
        assertNotNull(board.getSolution());
        assertTrue(Arrays.deepEquals(board.getSolution(),withSecondMine));
    }

//    public void testPlayGame() {
//        fail("Not yet implemented");
//    }
//
//    public void testUnveilCeil() {
//        fail("Not yet implemented");
//    }
//
//    public void testUnveilAroundCells() {
//        fail("Not yet implemented");
//    }
//
//    public void testDisplayBoardBoardBoolean() {
//        fail("Not yet implemented");
//    }
//
//    public void testDisplayBoardBoard() {
//        fail("Not yet implemented");
//    }
//
//    public void testDisplayPresentation() {
//        fail("Not yet implemented");
//    }

}
