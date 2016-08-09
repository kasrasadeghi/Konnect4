package com.kaz.konnect4;

import java.awt.*;

/**
 * Created by kasra on 7/26/2016.
 */
public enum Piece {
    ONE, TWO, NULL;
    
    static Color OFF_WHITE = new Color(237, 237, 237);
    Color getColor() {
        switch(name()) {
            case "ONE":
                return Color.RED;
            case "TWO":
                return Color.YELLOW;
            default:
                return OFF_WHITE;
        }
    }
}
