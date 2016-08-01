package com.demo.minesweeper.utils;

public class CommonConstants {

    private CommonConstants() {
    }

    /*
     * Error messages
     */
    public static String ERR_BOARD_TOO_SMALL = "I guess you won. Not a challenger, are you ? Let's try again, enter width and height please";
    public static String ERR_OUT_OF_BOARD = "Cell out of the board ! Try again please";
    public static String ERR_ALREADY_UNVEILED = "Cell already unveiled, choose an other one";
    public static String ERR_NOT_TWO_NUMBERS = "These are not two numbers, try again";
    public static String ERR_TOO_LONG_FOR_INT = "Number too long, try again";
    public static String ERR_TOO_MUCH_MINES = "Too much mines (max : %d) ! You can't win that way, try again";
    public static String ERR_NOT_VALID_NUMBER = "This is not a (non zero) number, try again";

    /*
     * Info messages
     */
    public static String INFO_GAME_LOST = "Sorry you lost, do better next time !";
    public static String INFO_GAME_WON = "Well done, you won ! Why don't you add some mines now ?";
    public static String INFO_SOLUTION = "Solution : ";
    public static String INFO_REMAINING_CELLS = "Remaining cells (not mines) : %d";

    /*
     * Interactions with user
     */
    public static String USER_CELL_TO_UNVEIL = "Which cell do you want to unveil (give x y separated by one space) ?";
    public static String USER_NUMBER_MINES = "How many mines do you want ?";
    public static String USER_BOARD_SIZE = "Please enter two numbers for the board size (width heigth), separated by one space :";

    /*
     * Regex
     */
    public static String REG_NUMBER = "\\d+";
    public static String REG_SPACE = "\\s";
    public static String REG_TWO_NUMBERS_SPACE = REG_NUMBER + REG_SPACE + REG_NUMBER;

    /*
     * Common strings
     */
    public static String EMPTY_STRING = "";
    public static String LEFT_BRACKET = "[";
    public static String WHITESPACE = " ";
    public static String RIGHT_BRACKET = "]";
    public static String STAR = "*";

    /*
     * Title
     */
    public static String MSG_LINE_SEPARATOR = "*****";
    public static String MSG_INTRODUCTION = "Welcome to the greatest Minesweeper !";
    public static String MSG_RULES = "Rules are simple : unveil all cells that are not mines";
    public static String MSG_GOOD_LUCK = "Good luck !";
}
