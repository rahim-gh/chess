package com.kern.Piece;

import com.kern.GUI.GamePanel;
import com.kern.Logic.Type;

public class Pawn extends Piece {

    public Pawn(int color, int column, int row) {
        super(color, column, row);

        type = Type.PAWN;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/white-pawn");
        } else {
            image = getImage("/piece/black-pawn");
        }
    }

    @Override
    public boolean canMove(int targetColumn, int targetRow) {
        if (isWithinBoard(targetColumn, targetRow)
                && !isSameSequare(targetColumn, targetRow)) {

            // Define the move value based on its color
            int moveValue;
            if (color == GamePanel.WHITE) {
                moveValue = -1;
            } else {
                moveValue = 1;
            }

            // Check the hitting piece
            hittinPiece = getHittingPiece(targetColumn, targetRow);

            // Move 1 square
            if (targetColumn == preColumn
                    && targetRow == preRow + moveValue
                    && hittinPiece == null) {
                return true;
            }

            // Move 2 square
            if (targetColumn == preColumn
                    && targetRow == preRow + moveValue * 2
                    && hittinPiece == null
                    && isMoved == false
                    && pieceIsOnStraightLine(targetColumn, targetRow) == false) {
                return true;
            }

            // Capture diagonaly
            if (Math.abs(targetColumn - preColumn) == 1
                    && targetRow == preRow + moveValue
                    && hittinPiece != null
                    && hittinPiece.color != color) {
                return true;
            }

            // En Passant
            if (Math.abs(targetColumn - preColumn) == 1 && targetRow == preRow + moveValue) {
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.column == targetColumn && piece.row == preRow && piece.isTwoStepped) {
                        hittinPiece = piece;
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
