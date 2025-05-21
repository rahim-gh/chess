package com.kern.Piece;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.kern.GUI.Board;
import com.kern.GUI.GamePanel;
import com.kern.Logic.Type;

public class Piece {

    public Type type;
    public BufferedImage image;
    public int x, y;
    public int column, row;
    public int preColumn, preRow;
    public int color;
    public Piece hittinPiece;
    public boolean isMoved, isTwoStepped;

    public Piece(int color, int column, int row) {
        this.color = color;
        this.column = column;
        this.row = row;
        x = getX(column);
        y = getY(row);
        preColumn = column;
        preRow = row;
    }

    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public final int getX(int coulmn) {
        return column * Board.SQUARE_SIZE;
    }

    public final int getY(int row) {
        return row * Board.SQUARE_SIZE;
    }

    public final int getColumn(int x) {
        return (x + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    public final int getRow(int y) {
        return (y + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
    }

    public Piece getHittingPiece(int targetColumn, int targetRow) {
        // Check if the target square is occupied by another piece
        for (Piece piece : GamePanel.simPieces) {
            // Check if the piece is in the same column and row as the target square
            // and is not the current piece
            if (piece.column == targetColumn && piece.row == targetRow && piece != this) {
                return piece;
            }
        }
        return null;
    }

    public void updatePosition() {
        // Check En Passant
        if (type == Type.PAWN) {
            if (Math.abs(row - preRow) == 2) {
                isTwoStepped = true;
            }
        }

        x = getX(column);
        y = getY(row);
        preColumn = getColumn(x);
        preRow = getRow(y);

        isMoved = true;
    }

    public boolean canMove(int targetColumn, int targetRow) {
        return false;
    }

    public boolean isValidSequare(int targetColumn, int targetRow) {
        hittinPiece = getHittingPiece(targetColumn, targetRow);

        if (hittinPiece == null) {
            // if sequare is empty
            return true;
        } else {
            // if sequare is occupied by another piece
            if (hittinPiece.color != color) {
                return true;
            } else {
                hittinPiece = null;
            }
        }

        return false;
    }

    public boolean isSameSequare(int targetColumn, int targetRow) {
        if (targetColumn == preColumn && targetRow == preRow) {
            return true;
        }
        return false;
    }

    public boolean isWithinBoard(int targetColumn, int targetRow) {
        if (targetColumn >= 0 && targetColumn < 8
                && targetRow >= 0 && targetRow < 8) {
            return true;
        }
        return false;
    }

    public void resetPosition() {
        column = preColumn;
        row = preRow;
        x = getX(column);
        y = getY(row);
    }

    public int getIndex() {
        for (int index = 0; index < GamePanel.simPieces.size(); index++) {
            if (GamePanel.simPieces.get(index) == this) {
                return index;
            }
        }
        return 0;
    }

    public boolean pieceIsOnStraightLine(int targetColumn, int targetRow) {
        // When piece moving to left
        for (int c = preColumn - 1; c > targetColumn; c--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.column == c && piece.row == targetRow) {
                    hittinPiece = piece;
                    return true;
                }
            }
        }

        // When piece moving to right
        for (int c = preColumn + 1; c < targetColumn; c++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.column == c && piece.row == targetRow) {
                    hittinPiece = piece;
                    return true;
                }
            }
        }

        // When piece moving to up
        for (int r = preRow - 1; r > targetRow; r--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == r && piece.column == targetColumn) {
                    hittinPiece = piece;
                    return true;
                }
            }
        }

        // When piece moving to down
        for (int r = preRow + 1; r < targetRow; r++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.row == r && piece.column == targetColumn) {
                    hittinPiece = piece;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean pieceIsOnDiagonalLine(int targetColumn, int targetRow) {
        if (targetRow < preRow) {
            // Up left
            for (int c = preColumn - 1; c > targetColumn; c--) {
                int diff = Math.abs(c - preColumn);
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.column == c && piece.row == preRow - diff) {
                        hittinPiece = piece;
                        return true;
                    }
                }
            }

            // Up right
            for (int c = preColumn + 1; c < targetColumn; c++) {
                int diff = Math.abs(c - preColumn);
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.column == c && piece.row == preRow - diff) {
                        hittinPiece = piece;
                        return true;
                    }
                }
            }
        }
        if (targetRow > preRow) {
            // Down left
            for (int c = preColumn - 1; c > targetColumn; c--) {
                int diff = Math.abs(c - preColumn);
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.column == c && piece.row == preRow + diff) {
                        hittinPiece = piece;
                        return true;
                    }
                }
            }

            // Down right
            for (int c = preColumn + 1; c < targetColumn; c++) {
                int diff = Math.abs(c - preColumn);
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.column == c && piece.row == preRow + diff) {
                        hittinPiece = piece;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isReachableByOponent(int targetColumn, int targetRow){
        for (Piece piece : GamePanel.pieces){
            if (piece.canMove(targetColumn, targetRow)
                    ) {
                return true;
            }
        }
        return false;
    }
}
