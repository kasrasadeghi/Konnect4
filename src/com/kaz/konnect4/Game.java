package com.kaz.konnect4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kasra on 7/25/2016.
 */
public class Game {
    String name1, name2;
    private List<Col> board;
    private boolean isp1 = true;
    public boolean playing = false;
    
    Game(String n1, String n2) {
        name1 = n1; name2 = n2;
    
        board = new ArrayList<>();
        for (int i = 0; i < colCount(); ++i) {
            board.add(new Col());
        }
        playing = true;
    }
    
    public void reset() {
        isp1 = true;
        board.clear();
        for (int i = 0; i < colCount(); ++i) {
            board.add(new Col());
        }
        playing = true;
    }
    
    public boolean isPlaying() {
        return playing;
    }
    
    public boolean isp1() {
        return isp1;
    }
    
    public void play(int c) {
        if (! colIsFull(c) && playing) {
            col(c).add(isp1? Piece.ONE : Piece.TWO);
            if (checkWin()) {
                playing = false;
                return; }
            isp1 = !isp1;
        }
    }
    
    public boolean checkWin() {
        int[][] map = new int[colCount()][rowCount()];
        //put into arrays because getting stuff out of arrays is faster than method calls
        for(int c = 0; c < colCount(); ++c) {
            Col col = col(c);
            for (int r = 0; r < rowCount(); ++r) {
                if (r < col.size())
                    map[c][r] = (col.get(r) == (isp1?Piece.ONE:Piece.TWO))? 1 : 0;
                else
                    map[c][r] = 0;
            }
        }
        for(int c = 0; c < colCount(); ++c) {
            for (int r = 0; r < rowCount(); ++r) {
                //check right
                if (c + 3 < colCount()) {
                    if (map[c][r] + map[c+1][r] + map[c+2][r] + map[c+3][r] == 4) return true;
                }
//                try {
//                    if (map[c][r] + map[c+1][r] + map[c+2][r] + map[c+3][r] == 4) return true;
//                } catch (ArrayIndexOutOfBoundsException ignored) {}
                //check up
                if (r + 3 < rowCount()) {
                    if (map[c][r] + map[c][r+1] + map[c][r+2] + map[c][r+3] == 4) return true;
                }
//                try {
//                    if (map[c][r] + map[c][r+1] + map[c][r+2] + map[c][r+3] == 4) return true;
//                } catch (ArrayIndexOutOfBoundsException ignored) {}
                //check diagonal
                if (c + 3 < colCount() && r + 3 < rowCount()) {
                    if (map[c][r] + map[c+1][r+1] + map[c+2][r+2] + map[c+3][r+3] == 4) return true;
                }
//                try {
//                    if (map[c][r] + map[c+1][r+1] + map[c+2][r+2] + map[c+3][r+3] == 4) return true;
//                } catch (ArrayIndexOutOfBoundsException ignored) {}
                //check other diagonal
                if (r + 3 < rowCount() && c - 3 >= 0) {
                    if (map[c][r] + map[c-1][r+1] + map[c-2][r+2] + map[c-3][r+3] == 4) return true;
                }
//                try {
//                    if (map[c][r] + map[c-1][r+1] + map[c-2][r+2] + map[c-3][r+3] == 4) return true;
//                } catch (ArrayIndexOutOfBoundsException ignored) {}
            }
        }
        return false;
    }
    
    public Col col(int c) {
        return board.get(c);
    }
    
    private boolean colIsFull(int c) {
        return col(c).size() >= rowCount();
    }
    
    public static int colCount() {
        return 7;
    }
    
    public static int rowCount() {
        return 6;
    }
    
    static class Col {
        List<Piece> col;
    
        Col() {
            col = new ArrayList<>();
        }
    
        void add(Piece p) {
            col.add(p);
        }
        
        Piece get(int i) { return col.get(i); }
        
        int size() {
            return col.size();
        }
    }
}
