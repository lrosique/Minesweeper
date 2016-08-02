package com.demo.minesweeper.utils;

public class TestUtils {

    private TestUtils() {
        super();
    }
    
    public static String INPUT_SEPARATOR = "\n";
    public static String OUTPUT_SEPARATOR = "\r\n";
    
    public static String INCORRECT_DATA_BOARD_SIZE = 
            ""+INPUT_SEPARATOR+ //Null entry
            "1"+INPUT_SEPARATOR+ //Only one number
            "-1 2"+INPUT_SEPARATOR+ //First number is negative
            "2 -1"+INPUT_SEPARATOR+ //Second number is negative
            "2147483648 3"+INPUT_SEPARATOR+ //Too large for int
            "a 4"+INPUT_SEPARATOR+ //Char instead of int
            "1  5"+INPUT_SEPARATOR+ //Two spaces
            "1 1"; //Board too small
  
    public static String INCORRECT_DATA_BOARD_SIZE_OUTPUT = 
            CommonConstants.USER_BOARD_SIZE+OUTPUT_SEPARATOR+ //Title
            CommonConstants.ERR_NOT_TWO_NUMBERS+OUTPUT_SEPARATOR+ //Null entry
            CommonConstants.ERR_NOT_TWO_NUMBERS+OUTPUT_SEPARATOR+ //Only one number
            CommonConstants.ERR_NOT_TWO_NUMBERS+OUTPUT_SEPARATOR+ //First is negative
            CommonConstants.ERR_NOT_TWO_NUMBERS+OUTPUT_SEPARATOR+ //Second is negative
            CommonConstants.ERR_TOO_LONG_FOR_INT+OUTPUT_SEPARATOR+ //Too large for int
            CommonConstants.ERR_NOT_TWO_NUMBERS+OUTPUT_SEPARATOR+ //Char
            CommonConstants.ERR_NOT_TWO_NUMBERS+OUTPUT_SEPARATOR+ //Two spaces instead of one
            CommonConstants.ERR_BOARD_TOO_SMALL+OUTPUT_SEPARATOR; //Board too small

    public static String INCORRECT_DATA_NB_MINES = 
            ""+INPUT_SEPARATOR+ //Null entry
            "-1"+INPUT_SEPARATOR+ //Negative
            "0"+INPUT_SEPARATOR+ //No mines
            "2147483648"+INPUT_SEPARATOR+ //Too large for int
            "a"+INPUT_SEPARATOR+ //Char
            "1 "+INPUT_SEPARATOR+ //Space
            "1000"; //Too big for board

    public static String INCORRECT_DATA_NB_MINES_OUTPUT = 
            CommonConstants.USER_NUMBER_MINES+OUTPUT_SEPARATOR+ //Title
            CommonConstants.ERR_NOT_VALID_NUMBER+OUTPUT_SEPARATOR+ //Null
            CommonConstants.ERR_NOT_VALID_NUMBER+OUTPUT_SEPARATOR+ //Negative
            CommonConstants.ERR_NOT_VALID_NUMBER+OUTPUT_SEPARATOR+ //No mines
            CommonConstants.ERR_TOO_LONG_FOR_INT+OUTPUT_SEPARATOR+ //Too large
            CommonConstants.ERR_NOT_VALID_NUMBER+OUTPUT_SEPARATOR+ //Char
            CommonConstants.ERR_NOT_VALID_NUMBER+OUTPUT_SEPARATOR+ //Space
            String.format(CommonConstants.ERR_TOO_MUCH_MINES, 9)+OUTPUT_SEPARATOR; //Too big
    
    
    public static int[][] solutionTwoMinesNoIncrement =    {{-1, 0, 0},
                                                            { 0, 0, 0},
                                                            { 0,-1, 0},
                                                            { 0, 0, 0}};
    
    public static int[][] solutionTwoMinesFirstIncrement = {{-1, 1, 0},
                                                            { 1, 1, 0},
                                                            { 0,-1, 0},
                                                            { 0, 0, 0}};
    
    public static int[][] solutionTwoMinesIncremented ={{-1, 1, 0},
                                                        { 2, 2, 1},
                                                        { 1,-1, 1},
                                                        { 1, 1, 1}};
    
    public static boolean[][] boardGameAllHiden =  {{false, false, false},
                                                    {false, false, false},
                                                    {false, false, false},
                                                    {false, false, false}};
    public static String SUCCESS_DATA_UNVEIL_CELL_OUTPUT = 
            CommonConstants.USER_CELL_TO_UNVEIL+OUTPUT_SEPARATOR;

    public static String INCORRECT_DATA_UNVEIL_CELL = 
            ""+INPUT_SEPARATOR+ //Null
            "1"+INPUT_SEPARATOR+ //Only one number
            "-1 2"+INPUT_SEPARATOR+ //Negative
            "2 -1"+INPUT_SEPARATOR+ //Negative
            "2147483648 3"+INPUT_SEPARATOR+ //Too large
            "a 4"+INPUT_SEPARATOR+ //Char
            "1  5"+INPUT_SEPARATOR+ //Two spcaces
            "0 1"+INPUT_SEPARATOR+ //Already unveiled
            "100 1"+INPUT_SEPARATOR+ //Out of board x
            "1 100"; //Out of board y
    
    public static String INCORRECT_DATA_UNVEIL_CELL_OUTPUT = 
            CommonConstants.USER_CELL_TO_UNVEIL+OUTPUT_SEPARATOR+ //Title
            CommonConstants.ERR_NOT_TWO_NUMBERS+OUTPUT_SEPARATOR+ //Null
            CommonConstants.ERR_NOT_TWO_NUMBERS+OUTPUT_SEPARATOR+ //Only one number
            CommonConstants.ERR_NOT_TWO_NUMBERS+OUTPUT_SEPARATOR+ //Negative
            CommonConstants.ERR_NOT_TWO_NUMBERS+OUTPUT_SEPARATOR+ //Negative
            CommonConstants.ERR_TOO_LONG_FOR_INT+OUTPUT_SEPARATOR+ //Too large
            CommonConstants.ERR_NOT_TWO_NUMBERS+OUTPUT_SEPARATOR+ //Char
            CommonConstants.ERR_NOT_TWO_NUMBERS+OUTPUT_SEPARATOR+ //Two spaces
            CommonConstants.ERR_ALREADY_UNVEILED+OUTPUT_SEPARATOR+ //Already unveiled
            CommonConstants.ERR_OUT_OF_BOARD+OUTPUT_SEPARATOR+ //Out of board x
            CommonConstants.ERR_OUT_OF_BOARD+OUTPUT_SEPARATOR; //Out of board y
    
    public static int[][] solutionTwoMinesAndTwoZeros = {{-1, 1, 0},
                                                         { 1, 2, 1},
                                                         { 0, 1,-1},
                                                         { 0, 1, 1}};
    
    public static boolean[][] boardGameTwoZerosUnveilFirst =   {{false, true , true},
                                                                {false, true , true},
                                                                {false, false, false},
                                                                {false, false, false}};
    
    public static boolean[][] boardGameTwoZerosUnveilSecondAndPropagate =  {{false, true , true},
                                                                            {true, true , true},
                                                                            {true, true, false},
                                                                            {true, true, false}};
    
    public static boolean[][] boardGamePartiallyUnveiled = {{true,  true,  true},
                                                            {false, false, false},
                                                            {false, false, false},
                                                            {false, false, true}};
    
    public static String printBoardGamePartiallyUnveiled =  "[ -1  1  0 ]"+OUTPUT_SEPARATOR+
                                                            "[  *  *  * ]"+OUTPUT_SEPARATOR+
                                                            "[  *  *  * ]"+OUTPUT_SEPARATOR+
                                                            "[  *  *  1 ]"+OUTPUT_SEPARATOR;
    
    public static String printBoardGameTotallyUnveiled ="[ -1  1  0 ]"+OUTPUT_SEPARATOR+
                                                        "[  2  2  1 ]"+OUTPUT_SEPARATOR+
                                                        "[  1 -1  1 ]"+OUTPUT_SEPARATOR+
                                                        "[  1  1  1 ]"+OUTPUT_SEPARATOR;
    
    public static String gameHeader =   CommonConstants.MSG_LINE_SEPARATOR+OUTPUT_SEPARATOR+
                                        CommonConstants.MSG_INTRODUCTION+OUTPUT_SEPARATOR+
                                        CommonConstants.MSG_RULES+OUTPUT_SEPARATOR+
                                        CommonConstants.MSG_GOOD_LUCK+OUTPUT_SEPARATOR+
                                        CommonConstants.MSG_LINE_SEPARATOR+OUTPUT_SEPARATOR;
}
