package com.kern.Piece;

import com.kern.GUI.GamePanel;
import com.kern.Logic.Type;

public class Queen extends Piece {

    public Queen(int color, int column, int row) {
        super(color, column, row);

        type = Type.QUEEN;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/white-queen");
        } else {
            image = getImage("/piece/black-queen");
        }
    }

    @Override
    public boolean canMove(int targetColumn, int targetRow) {
        if (isWithinBoard(targetColumn, targetRow) && !isSameSequare(targetColumn, targetRow)) {
            // Straight
            if (targetColumn == preColumn || targetRow == preRow) {
                if (isValidSequare(targetColumn, targetRow)
                        && !pieceIsOnStraightLine(targetColumn, targetRow)) {
                    return true;
                }
            }

            // Diagonal
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
