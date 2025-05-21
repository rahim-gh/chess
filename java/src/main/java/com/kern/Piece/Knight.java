package com.kern.Piece;

import com.kern.GUI.GamePanel;
import com.kern.Logic.Type;

public class Knight extends Piece {

    public Knight(int color, int column, int row) {
        super(color, column, row);

        type = Type.KNIGHT;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/white-knight");
        } else {
            image = getImage("/piece/black-knight");
        }
    }

    @Override
    public boolean canMove(int targetColumn, int targetRow) {
        if (isWithinBoard(targetColumn, targetRow)) {
            if (Math.abs(targetColumn - preColumn) * Math.abs(targetRow - preRow) == 2) {
                if (isValidSequare(targetColumn, targetRow)) {
                    return true;
                }
            }
        }
        return false;
    }

}
