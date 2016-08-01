package com.demo.minesweeper;

import com.demo.minesweeper.resources.Board;
import com.demo.minesweeper.service.Minesweeper;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        Minesweeper minesweeper = new Minesweeper();
        // Init game
        Board board = minesweeper.initGame(System.in, System.out);
        // Play until mine or win
        minesweeper.playGame(board,System.in, System.out);
    }
}
