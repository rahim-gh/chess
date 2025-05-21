package com.kern.Piece;

import com.kern.GUI.GamePanel;
import com.kern.Logic.Type;

public class Bishop extends Piece {

    public Bishop(int color, int column, int row) {
        super(color, column, row);

        type = Type.BISHOP;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/white-bishop");
        } else {
            image = getImage("/piece/black-bishop");
        }
    }

    @Override
    public boolean canMove(int targetColumn, int targetRow) {
        if (isWithinBoard(targetColumn, targetRow) && !isSameSequare(targetColumn, targetRow)) {
            if (Math.abs(targetColumn - preColumn) == Math.abs(targetRow - preRow)) {
                if (isValidSequare(targetColumn, targetRow)
                        && !pieceIsOnDiagonalLine(targetColumn, targetRow)) {
                    return true;
                }
            }
        }
        return false;
    }


}
