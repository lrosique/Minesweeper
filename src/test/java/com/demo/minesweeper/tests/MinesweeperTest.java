package com.demo.minesweeper.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import com.demo.minesweeper.resources.Board;
import com.demo.minesweeper.service.Minesweeper;
import com.demo.minesweeper.utils.TestUtils;

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
        int width = 2;
        int height = 5;
        String data = width+" "+height;
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        System.setOut(new PrintStream(outContent));
        int[] boardSize = minesweeper.getBoardSize(System.in, System.out);
        //Expect a board of 2 5
        assertNotNull(boardSize);
        assertEquals(boardSize[0], width);
        assertEquals(boardSize[1], height);
        
        //Given data is incorrect :
        //null, only one number, number is negative, number is over int, chars instead of int, wrong separator, one one, success
        width = 3;
        height = 6;
        data = TestUtils.INCORRECT_DATA_BOARD_SIZE+"\n"+width+" "+height;
        outContent = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        System.setOut(new PrintStream(outContent));
        boardSize = minesweeper.getBoardSize(System.in, System.out);
        assertNotNull(boardSize);
        String expectedOutput = TestUtils.INCORRECT_DATA_BOARD_SIZE_OUTPUT;

        assertEquals(expectedOutput,outContent.toString());
        assertEquals(boardSize[0], width);
        assertEquals(boardSize[1], height);
    }
    
    public void testGetNumberOfMines() {
        int numberOfCells = 10;
        //Given data is correct
        int numberMines = 3;
        String data = Integer.toString(numberMines);
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        System.setOut(new PrintStream(outContent));
        int numberOfMines = minesweeper.getNumberOfMines(numberOfCells, System.in, System.out);
        assertNotNull(numberOfMines);
        assertEquals(numberOfMines, numberMines);
        
        //Given data is incorrect
        //null, negative, no mines, over int, chars, spaces, too much mines, success
        numberMines = 4;
        data = TestUtils.INCORRECT_DATA_NB_MINES+"\n"+numberMines;
        outContent = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        System.setOut(new PrintStream(outContent));
        numberOfMines = minesweeper.getNumberOfMines(numberOfCells, System.in, System.out);
        assertNotNull(numberOfMines);
        String expectedOutput = TestUtils.INCORRECT_DATA_NB_MINES_OUTPUT;

        assertEquals(expectedOutput,outContent.toString());
        assertEquals(numberOfMines, numberMines);
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
                if (board.getSolution()[j][i] == -1){
                    count++;
                }
            }
        }
        assertEquals(count, mines);
        //Number of adjacent mines is verified in incrementCellsAroundMine
    }

    public void testIncrementCellsAroundMine() {
        int width = 3;
        int height = 4;
        Board board = new Board(width, height);
        //Mines at 0 0 and 1 2
        int xFirstMine = 0;
        int yFirstMine = 0;
        int xSecondMine = 1;
        int ySecondMine = 2;
        
        int[][] solution = TestUtils.solutionTwoMinesNoIncrement;
        board.setSolution(solution);
        //Incrementing around first mine
        //Expect change at (0,1), (1,0) and (1,1)
        //Should be +1 and not +2 since second mine was not calculated yet
        minesweeper.incrementCellsAroundMine(xFirstMine,yFirstMine,board);
        int[][] withFirstMine = TestUtils.solutionTwoMinesFirstIncrement;
        assertNotNull(board.getSolution());
        assertTrue(Arrays.deepEquals(board.getSolution(),withFirstMine));
        
        //Second mine
        minesweeper.incrementCellsAroundMine(xSecondMine,ySecondMine,board);
        int[][] withSecondMine = TestUtils.solutionTwoMinesIncremented;
        assertNotNull(board.getSolution());
        assertTrue(Arrays.deepEquals(board.getSolution(),withSecondMine));
    }

    public void testUnveilCeil() {
        int width = 3;
        int height = 4;
        Board board = new Board(width, height);
        int[][] solution = TestUtils.solutionTwoMinesIncremented;
        
        //No cell is unveiled
        boolean[][] boardGame = TestUtils.boardGameAllHiden;
        board.setSolution(solution);
        board.setBoardGame(boardGame);
        
        //Given data is correct
        int xCellToUnveil = 0;
        int yCellToUnveil = 1;
        String data = xCellToUnveil+" "+yCellToUnveil;
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        System.setOut(new PrintStream(outContent));
        minesweeper.unveilCeil(board, System.in, System.out);
        boolean[][] expectedBoard = TestUtils.boardGameAllHiden;
        expectedBoard[yCellToUnveil][xCellToUnveil] = true;
        String expectedOutput = TestUtils.SUCCESS_DATA_UNVEIL_CELL_OUTPUT;
        
        assertNotNull(board.getBoardGame());
        assertTrue(Arrays.deepEquals(board.getBoardGame(),expectedBoard));
        assertEquals(expectedOutput,outContent.toString());
        
        //Given data is incorrect :
        //null, only one number, number is negative, number is over int, chars instead of int, wrong separator, already unveiled, out of board width, out of board height, success
        xCellToUnveil = 2;
        xCellToUnveil = 2;
        data = TestUtils.INCORRECT_DATA_UNVEIL_CELL + "\n" + xCellToUnveil + " " + yCellToUnveil;
        outContent = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        System.setOut(new PrintStream(outContent));
        minesweeper.unveilCeil(board, System.in, System.out);
        
        expectedBoard[yCellToUnveil][xCellToUnveil] = true;
        expectedOutput = TestUtils.INCORRECT_DATA_UNVEIL_CELL_OUTPUT;

        assertNotNull(board.getBoardGame());
        assertTrue(Arrays.deepEquals(board.getBoardGame(),expectedBoard));
        assertEquals(expectedOutput,outContent.toString());
        
        //Reset
        expectedBoard[1][0] = false;
        expectedBoard[2][2] = false;
    }

    public void testUnveilAroundCells() {
        int width = 3;
        int height = 4;
        Board board = new Board(width, height);
        int[][] solution = TestUtils.solutionTwoMinesAndTwoZeros;
        //No cell is unveiled
        boolean[][] boardGame = TestUtils.boardGameAllHiden;
        board.setSolution(solution);
        board.setBoardGame(boardGame);
        int xCellToUnveil = 2;
        int yCellToUnveil = 0;
        minesweeper.unveilAroundCells(xCellToUnveil, yCellToUnveil, board);
        boolean[][] expectedUnveilFirst = TestUtils.boardGameTwoZerosUnveilFirst;
        assertNotNull(board.getBoardGame());
        assertTrue(Arrays.deepEquals(board.getBoardGame(),expectedUnveilFirst));
        xCellToUnveil = 0;
        yCellToUnveil = 3;
        minesweeper.unveilAroundCells(xCellToUnveil, yCellToUnveil, board);
        boolean[][] expectedUnveilWithPropagation = TestUtils.boardGameTwoZerosUnveilSecondAndPropagate;
        assertTrue(Arrays.deepEquals(board.getBoardGame(),expectedUnveilWithPropagation));
        
      //Reset
        boardGame[0][2] = false;
        boardGame[3][0] = false;
    }

    public void testDisplayBoard() {
        int width = 3;
        int height = 4;
        Board board = new Board(width, height);
        int[][] solution = TestUtils.solutionTwoMinesIncremented;
        //Only first line will be unveiled and last cell
        boolean[][] boardGame = TestUtils.boardGamePartiallyUnveiled;
        board.setSolution(solution);
        board.setBoardGame(boardGame);
        System.setOut(new PrintStream(outContent));
        minesweeper.displayBoard(board, System.out);
        String expectedOutput = TestUtils.printBoardGamePartiallyUnveiled;
        assertEquals(expectedOutput, outContent.toString());
        
        //Display solution
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        minesweeper.displayBoard(board, true, System.out);
        String expectedFullSolution = TestUtils.printBoardGameTotallyUnveiled;
        assertEquals(expectedFullSolution, outContent.toString());
    }

    public void testDisplayPresentation() {
        System.setOut(new PrintStream(outContent));
        String expectedOutput = TestUtils.gameHeader;
        minesweeper.displayPresentation(System.out);
        assertEquals(expectedOutput, outContent.toString());
    }

    //These two methods are calling back the other methods to construct the app
    
//    //TODO : crash with out of memory error due to sys out
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
//  
//  //TODO : crash with out of memory error due to sys out
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
