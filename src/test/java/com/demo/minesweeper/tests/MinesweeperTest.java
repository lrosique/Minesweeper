package com.demo.minesweeper.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import com.demo.minesweeper.resources.Board;
import com.demo.minesweeper.service.Minesweeper;

import junit.framework.TestCase;

/**
 * Test of all methods in Minesweeper
 * The objective is to test the app functionally (and as a result per method)
 * @author lrosique
 *
 */
public class MinesweeperTest extends TestCase {
    
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Minesweeper minesweeper = new Minesweeper();

    public void setUp() {
        outContent = new ByteArrayOutputStream();
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

    public void testUnveilCeil() {
        int width = 4;
        int height = 3;
        Board board = new Board(width, height);
        int[][] solution =       {{-1, 1, 0},
                                  { 2, 2, 1},
                                  { 1,-1, 1},
                                  { 1, 1, 1}};
        //No cell is unveiled
        boolean[][] boardGame ={{false, false, false},
                                {false, false, false},
                                {false, false, false},
                                {false, false, false}};
        board.setSolution(solution);
        board.setBoardGame(boardGame);
        //Given data is correct
        String data = "1 0";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        System.setOut(new PrintStream(outContent));
        minesweeper.unveilCeil(board, System.in, System.out);
        boolean[][] expectedBoard ={{false, false, false},
                                    {true, false, false},
                                    {false, false, false},
                                    {false, false, false}};
        String expectedOutput = "Which cell do you want to unveil (give x y separated by one space) ?\r\n";
        assertNotNull(board.getBoardGame());
        assertTrue(Arrays.deepEquals(board.getBoardGame(),expectedBoard));
        assertEquals(expectedOutput,outContent.toString());
        
        //Given data is incorrect :
        //null, only one number, number is negative, number is over int, chars instead of int, wrong separator, already unveiled, out of board width, out of board height, success
        data = "\n"+"1\n"+"-1 2\n"+"2 -1\n"+"2147483648 3\n"+"a 4\n"+"1  5\n"+"1 0\n"+"100 1\n"+"1 100\n"+"2 2";
        outContent = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        System.setOut(new PrintStream(outContent));
        minesweeper.unveilCeil(board, System.in, System.out);
        boolean[][] expectedBoardWithTwoUnveil ={{false, false, false},
                                                {  true, false, false},
                                                { false, false, true},
                                                { false, false, false}};
        expectedOutput = "Which cell do you want to unveil (give x y separated by one space) ?\r\n"+
                            "These are not two numbers, try again\r\n"+
                            "These are not two numbers, try again\r\n"+
                            "These are not two numbers, try again\r\n"+
                            "These are not two numbers, try again\r\n"+
                            "Number too long, try again\r\n"+
                            "These are not two numbers, try again\r\n"+
                            "These are not two numbers, try again\r\n"+
                            "Cell already unveiled, choose an other one\r\n"+
                            "Cell out of the board ! Try again please\r\n"+
                            "Cell out of the board ! Try again please\r\n";

        assertNotNull(board.getBoardGame());
        assertTrue(Arrays.deepEquals(board.getBoardGame(),expectedBoardWithTwoUnveil));
        assertEquals(expectedOutput,outContent.toString());
    }

    public void testUnveilAroundCells() {
        int width = 4;
        int height = 3;
        Board board = new Board(width, height);
        int[][] solution =       {{-1, 1, 0},
                                  { 1, 2, 1},
                                  { 0, 1,-1},
                                  { 0, 1, 1}};
        //No cell is unveiled
        boolean[][] boardGame ={{false, false, false},
                                {false, false, false},
                                {false, false, false},
                                {false, false, false}};
        board.setSolution(solution);
        board.setBoardGame(boardGame);
        minesweeper.unveilAroundCells(0, 2, board);
        boolean[][] expectedUnveilFirst ={{false, true , true},
                                          {false, true , true},
                                          {false, false, false},
                                          {false, false, false}};
        assertNotNull(board.getBoardGame());
        assertTrue(Arrays.deepEquals(board.getBoardGame(),expectedUnveilFirst));
        minesweeper.unveilAroundCells(3, 0, board);
        boolean[][] expectedUnveilWithPropagation ={{false, true , true},
                                                    {true, true , true},
                                                    {true, true, false},
                                                    {true, true, false}};
        assertTrue(Arrays.deepEquals(board.getBoardGame(),expectedUnveilWithPropagation));
    }

    public void testDisplayBoard() {
        int width = 4;
        int height = 3;
        Board board = new Board(width, height);
        int[][] solution =       {{-1, 1, 0},
                                  { 2, 2, 1},
                                  { 1,-1, 1},
                                  { 1, 1, 1}};
        //Only first line will be unveiled and last cell
        boolean[][] boardGame ={{true,  true,  true},
                                {false, false, false},
                                {false, false, false},
                                {false, false, true}};
        board.setSolution(solution);
        board.setBoardGame(boardGame);
        System.setOut(new PrintStream(outContent));
        minesweeper.displayBoard(board, System.out);
        String expectedOutput = "[ -1  1  0 ]\r\n"+
                                "[  *  *  * ]\r\n"+
                                "[  *  *  * ]\r\n"+
                                "[  *  *  1 ]\r\n";
        assertEquals(expectedOutput, outContent.toString());
        
        //Display solution
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        minesweeper.displayBoard(board, true, System.out);
        String expectedFullSolution = "[ -1  1  0 ]\r\n"+
                                      "[  2  2  1 ]\r\n"+
                                      "[  1 -1  1 ]\r\n"+
                                      "[  1  1  1 ]\r\n";
        assertEquals(expectedFullSolution, outContent.toString());
    }

    public void testDisplayPresentation() {
        System.setOut(new PrintStream(outContent));
        String expectedOutput = "*****\r\n"+
                                "Welcome to the greatest Minesweeper !\r\n"+
                                "Rules are simple : unveil all cells that are not mines\r\n"+
                                "Good luck !\r\n"+
                                "*****\r\n";
        minesweeper.displayPresentation(System.out);
        assertEquals(expectedOutput, outContent.toString());
    }

    //These two methods are calling back the other methods to construct the app
    
    //TODO : crash with out of memory error due to sys out
//  public void testInitGame() {
//      //We verify that the board is initiated, coherence of data is tested in each method
//      int mines = 3;
//      int width = 4;
//      int height = 5;
//      String data = width+" "+height+"\n"+mines+"\n";
//      System.setIn(new ByteArrayInputStream(data.getBytes()));
//      outContent = new ByteArrayOutputStream();
//      System.setOut(new PrintStream(outContent));
//      Board board = minesweeper.initGame(System.in, System.out);
//      assertNotNull(board);
//      assertEquals(board.getWidth(),width);
//      assertEquals(board.getHeight(),height);
//      assertEquals(board.getNumberOfMines(),mines);
//  }
  
  //TODO : crash with out of memory error due to sys out
//  public void testPlayGame() {
//      int width = 4;
//      int height= 3;
//      int mines = 2;
//      Board board = new Board(width, height);
//      int[][] solution =       {{-1, 1, 0},
//                                { 2, 2, 1},
//                                { 1,-1, 1},
//                                { 1, 1, 1}};
//      board.setNumberOfMines(mines);
//      boolean[][] boardGame ={{false, false, false},
//                              {false, false, false},
//                              {false, false, false},
//                              {false, false, false}};
//      board.setSolution(solution);
//      board.setBoardGame(boardGame);
//      System.setOut(new PrintStream(outContent));
//      
//      //Loose at second turn
//      String data = "1 0\n0 0";
//      System.setIn(new ByteArrayInputStream(data.getBytes()));
//      
//      minesweeper.playGame(board, System.in, System.out);
//      String expectedResultLoose = "";
//      assertEquals(expectedResultLoose, outContent.toString());
//      
//      //Win the game
//      board.setBoardGame(new boolean[4][3]);
//      data = "0 2\n1 0\n2 0\n3 0\n2 2\n3 1\n3 2";
//      outContent = new ByteArrayOutputStream();
//      System.setIn(new ByteArrayInputStream(data.getBytes()));
//      System.setOut(new PrintStream(outContent));
//      
//      minesweeper.playGame(board, System.in, System.out);
//      String expectedResultWin = "";
//      assertEquals(expectedResultWin, outContent.toString());
//  }
}
