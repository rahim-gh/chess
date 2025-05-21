package com.kern.GUI;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * The Board class provides functionality to render a chess board.
 * <p>
 * It establishes an 8x8 grid where each cell is drawn with a constant size
 * defined by {@link #SQUARE_SIZE}.
 * The board is rendered using a checkerboard pattern with alternating color
 * schemes.
 * Specifically, two distinct colors are used to fill each square,
 * and the starting color alternates for each row to maintain the checkerboard
 * layout.
 * </p>
 *
 * <p>
 * Constants defined in this class include:
 * <ul>
 * <li>{@link #MAX_COLUMNS} - the number of columns in the board.</li>
 * <li>{@link #MAX_ROWS} - the number of rows in the board.</li>
 * <li>{@link #SQUARE_SIZE} - the dimension in pixels of an individual
 * square.</li>
 * <li>{@link #HALF_SQUARE_SIZE} - half the size of a square, which may be
 * useful for positioning elements.</li>
 * </ul>
 * </p>
 *
 * <p>
 * The primary method, {@code draw(Graphics2D g2d)}, iterates through each row
 * and column,
 * setting the appropriate color before rendering each square.
 * </p>
 */
public class Board {
    final static int MAX_COLUMNS = 8;
    final static int MAX_ROWS = 8;
    public static final int SQUARE_SIZE = 80;
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE / 2;

    public static void draw(Graphics2D g2d) {
        boolean isLight = false;

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int column = 0; column < MAX_COLUMNS; column++) {

                if (isLight) {
                    g2d.setColor(new Color(238, 238, 210));
                    isLight = false;
                } else {
                    g2d.setColor(new Color(186, 202, 68));
                    isLight = true;
                }

                g2d.fillRect(column * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }

            isLight = !isLight;
        }
    }
}
