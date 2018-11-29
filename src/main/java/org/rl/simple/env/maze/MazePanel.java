package org.rl.simple.env.maze;

import javax.swing.*;
import java.awt.*;

import static org.rl.simple.env.maze.Constants.*;

public class MazePanel extends JPanel {

    private MazeMap map;

    public MazePanel(MazeMap map) {
        setBackground(Color.BLACK);
        this.map = map;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                int code = this.map.getMap()[i][j];
                g.setColor(chooseColor(code));
                g.fillRect(i * CUBE_PIXEL, j * CUBE_PIXEL, CUBE_PIXEL, CUBE_PIXEL);
            }
        }
    }

    private Color chooseColor(int code) {
        if (code == PLAYER) {
            return Color.RED;
        } else if (code == TRAP) {
            return Color.BLACK;
        } else if (code == GOAL) {
            return Color.YELLOW;
        } else {
            return Color.WHITE;
        }
    }
}
