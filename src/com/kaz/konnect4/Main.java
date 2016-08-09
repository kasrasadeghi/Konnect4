package com.kaz.konnect4;

import javax.swing.*;

public class Main {

public static void main(String[] args) {
    Game game = new Game("Player One", "Player Two");
    View view = new View(game);
    Control control = new Control(game, view);
    view.setControl(control);

JFrame frame = new JFrame() {
    /*Constructor*/{
        add(view);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
};
    
    while(true) {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        frame.repaint();
    }
}
}
