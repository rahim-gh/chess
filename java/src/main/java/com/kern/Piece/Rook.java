package com.kern.Piece;

import com.kern.GUI.GamePanel;
import com.kern.Logic.Type;

public class Rook extends Piece {

    public Rook(int color, int column, int row) {
        super(color, column, row);

        type = Type.ROOK;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/white-rook");
        } else {
            image = getImage("/piece/black-rook");
        }
    }

    @Override
    public boolean canMove(int targetColumn, int targetRow) {
        if (isWithinBoard(targetColumn, targetRow) && !isSameSequare(targetColumn, targetRow)) {
            if (targetColumn == preColumn || targetRow == preRow) {
                if (isValidSequare(targetColumn, targetRow)
                        && !pieceIsOnStraightLine(targetColumn, targetRow)) {
                    return true;
                }
            }
        }
        return false;
    }

}
