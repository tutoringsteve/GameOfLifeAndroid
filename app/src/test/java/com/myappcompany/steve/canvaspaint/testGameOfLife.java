package com.myappcompany.steve.canvaspaint;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.myappcompany.steve.canvaspaint.GameOfLifeBoard;

/**
 *
 * @author ForcesOfOdin
 */
public class testGameOfLife {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int n = 32;
        GameOfLifeBoard board;
        String boardGiven =
                ".........." +
                        ".........." +
                        "..#......." +
                        "...#......" +
                        ".###......" +
                        ".........." +
                        ".........." +
                        ".........." +
                        ".........." +
                        "..........";

        String boardGiven2 =
                "................." +
                        "................." +
                        "....###...###...." +
                        "................." +
                        "..#..*.#.#.*..#.." +
                        "..#.*..#.#..*.#.." +
                        "..#....#.#....#.." +
                        "....###...###...." +
                        "................." +
                        "....###...###...." +
                        "..#....#.#....#.." +
                        "..#.*..#.#..*.#.." +
                        "..#..*.#.#.*..#.." +
                        "................." +
                        "....###...###...." +
                        "................." +
                        ".................";
        int height = 17;
        int width = 17;

        char[][] tempArray = new char[height][width];

        for(int i = 0; i < height; i ++) {
            for(int j = 0; j < width; j++) {
                tempArray[i][j] = boardGiven2.charAt(i*height + j);
            }
        }
        board = new GameOfLifeBoard(boardGiven2,height,width );

        System.out.println(board.toString());

        int turns = 1;
        while(turns <= n){
            board.oneTurn();
            turns++;

            System.out.println(board.toString());
            if(board.steadyState()) {
                System.out.println("Board reached steady state and will not change");
                break;
            }
        }

    }

}
