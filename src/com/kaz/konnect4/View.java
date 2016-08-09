package com.kaz.konnect4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.kaz.konnect4.Game.Col;

/**
 * Created by kasra on 7/25/2016.
 */
public class View extends JPanel {
    Game game;
    Control ctrl;
    
    private static Dimension PREFERRED = new Dimension(1000, 600);
    private static Color
            RED     = Color.RED,
            YELLOW  = Color.YELLOW,
            SHADOW  = new Color(0, 0, 0, 150),
            LIGHT   = new Color(255, 255, 255, 100);
    
    private int preview = 3;
    
    public View(Game g) {
        game = g;
        setBackground(Color.DARK_GRAY);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                int sideLength = sideLength();
                int tlx = btlx();
                int tly = btly();

                for(int c = 0; c < Game.colCount(); ++c) {
                    Rectangle r = new Rectangle(tlx + c * sideLength, tly, sideLength, sideLength * 6);
                    if (r.contains(p)) {
                        ctrl.handle(c);
                    }
                }
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'r')
                    ctrl.sendReset();
            }
        });
    }
    
    @Override
    public Dimension getPreferredSize() {
        return PREFERRED;
    }
    
    public void setControl(Control control) {
        ctrl = control;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        requestFocusInWindow();
        paintActivePlayer(g);
        paintBoard(g);
        if (!game.isPlaying()) paintVictory(g);
    }
    
    private void paintActivePlayer(Graphics g) {
    
        Point p = getMousePosition();
        if (p == null) preview = 3;
        else {
            int sideLength = sideLength();
            int tlx = btlx();
            int tly = btly();
    
            for(int c = 0; c < Game.colCount(); ++c) {
                Rectangle r = new Rectangle(tlx + c * sideLength, tly, sideLength, sideLength * 6);
                if (r.contains(p)) {
                    preview = c;
                }
            }
        }
        
        Piece piece = game.isp1()? Piece.ONE: Piece.TWO;
        paintPiece(g, piece, btlx() + preview * sideLength(), tly(), sideLength());
    }
    
    private void paintVictory(Graphics g) {
        int w = bw()/2;
        int h = bh()/2;
        int tlx = btlx() + w/2;
        int tly = btly() + h/2;
        
        g.setColor(game.isp1()? RED: YELLOW);
        Color c = g.getColor();
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 170));
        g.fillRect(tlx-2, tly-2, w+4,h+4 );
        g.setColor(SHADOW);
        g.fillRect(tlx, tly, w, h);
        
        //draw text
        String s = (game.isp1()?"Player 1":"Player 2") + " Wins";
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, sideLength()/4));
        tlx = tlx + w/2 - SwingUtilities.computeStringWidth(g.getFontMetrics(), s )/2;
        tly = tly + h/2 - g.getFontMetrics().getHeight()*3/7;
        g.setColor(Color.WHITE);
        g.drawString(s, tlx, tly);
        
        String s2 = "Press R to Reset";
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, sideLength()/7));
        tlx = tlx + w/2 - SwingUtilities.computeStringWidth(g.getFontMetrics(), s2 )/2;
        tly = tly + h/2 - g.getFontMetrics().getHeight()*3/7;
        g.setColor(LIGHT);
        g.drawString(s2, tlx, tly);
    }
    
    private void paintBoard(Graphics g) {
        //if the width / number of columns is smaller than the height / number of rowCount then use the width, otherwise use the height
        int sideLength = sideLength();
        //go to the center, then go half of the board's size outwards to find the top-left corner
        int tlx = btlx();
        int tly = btly();
    
        // - PAINT BLUE BOARD
        g.setColor(Color.BLUE);
        g.fillRect(tlx, tly, Game.colCount() * sideLength, Game.rowCount() * sideLength);
        for (int c = 0; c < Game.colCount(); ++c) {
            for (int r = 0; r < Game.rowCount(); ++r) {
                int ptlx = tlx + c * sideLength;
                int ptly = tly + r * sideLength;
                paintPiece(g, Piece.NULL, ptlx, ptly, sideLength);
            }
        }
        // - PAINT RED AND YELLOW PIECES
        for (int c = 0; c < Game.colCount(); ++c) {
            Col col = game.col(c);
            //to paint a column, get the row and draw each piece from the bottom.
            for (int r = 0; r < col.size(); ++r) {
                Piece piece = col.get(r);
    
                //to find the bottom, find out which row we're on, go to the bottom of the board, then paint the board.
                int ptlx = tlx + c * sideLength;
                int ptly = tly + (Game.rowCount()-1-r) * sideLength;
                
                paintPiece(g, piece, ptlx, ptly, sideLength);
            }
        }
    }
    
    private void paintPiece(Graphics g, Piece piece, int ptlx, int ptly, int sideLength) {
        g.setColor(piece.getColor());
        g.fillOval(ptlx + sideLength/10, ptly + sideLength/10, sideLength * 4/5, sideLength * 4/5);
        if (piece == Piece.NULL) return;
        g.setColor(LIGHT);
        g.fillOval(ptlx + sideLength/4,
                ptly + sideLength/4,
                sideLength/2, sideLength/2);
    }
    
    
    //region private int w(), h(), sideLength(), btlx(), tly(), btly(), bw(), bh()
    
    private int w() {
        return getWidth();
    }
    
    private int h() {
        return getHeight();
    }
    
    private int sideLength() {
        return (w()/Game.colCount() < h()/(Game.rowCount()+1))? w()/Game.colCount() : h()/(Game.rowCount()+1);
    }
    
    private int btlx() {
        return w()/2 - Game.colCount() * sideLength()/2;
    }
    
    private int tly() {
        return h()/2 - Game.rowCount() * sideLength()/2 - sideLength()/2;
    }
    
    private int btly() {
        return h()/2 - (Game.rowCount()-1) * sideLength()/2;
    }
    
    private int bw() {
        return Game.colCount() * sideLength();
    }
    
    private int bh() {
        return Game.rowCount() * sideLength();
    }
    
    //endregion
}
