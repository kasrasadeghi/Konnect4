package com.kaz.konnect4;

/**
 * Created by kasra on 7/25/2016.
 */
public class Control {
    private Game game;
    private View view;
    
    public Control(Game g, View v) {
        game = g;
        view = v;
    }
    
    public void handle(int col) {
        game.play(col);
        view.repaint();
    }
    
    public void sendReset() {
        game.reset();
        view.repaint();
    }
}
