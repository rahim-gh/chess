package com.kern.Piece;

import com.kern.GUI.GamePanel;
import com.kern.Logic.Type;

public class King extends Piece {

    public King(int color, int column, int row) {
        super(color, column, row);

        type = Type.KING;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/white-king");
        } else {
            image = getImage("/piece/black-king");
        }
    }

    @Override
    public boolean canMove(int targetColumn, int targetRow) {
        if (isWithinBoard(targetColumn, targetRow)) {

            // MOVEMENT
            if (Math.abs(targetColumn - preColumn) + Math.abs(targetRow - preRow) == 1 ||
                    Math.abs(targetColumn - preColumn) * Math.abs(targetRow - preRow) == 1) {
                if (isValidSequare(targetColumn, targetRow)) {
                    for (Piece piece : GamePanel.simPieces) {
                        if (piece.color != this.color
                                && piece.canMove(targetColumn, targetRow)) {
                            return false;
                        }
                    }
                    return true;
                }
            }

            // CASTLING
            if (isMoved == false) {
                // King side
                if (targetColumn == preColumn + 2
                        && targetRow == preRow
                        && pieceIsOnStraightLine(targetColumn, targetRow) == false) {
                    for (Piece piece : GamePanel.simPieces) {
                        if (piece.column == preColumn + 3
                                && piece.row == preRow
                                && piece.isMoved == false) {
                            GamePanel.castlingPiece = piece;
                            return true;
                        }
                    }
                }

                // Queen side
                if (targetColumn == preColumn - 2
                        && targetRow == preRow
                        && pieceIsOnStraightLine(targetColumn, targetRow) == false) {
                    Piece p[] = new Piece[2];
                    for (Piece piece : GamePanel.simPieces) {
                        if (piece.column == preColumn - 3 && piece.row == preRow) {
                            p[0] = piece;
                        }
                        if (piece.column == preColumn - 4 && piece.row == preRow) {
                            p[1] = piece;
                        }

                        if (p[0] == null && p[1] != null && p[1].isMoved == false) {
                            GamePanel.castlingPiece = p[1];
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
