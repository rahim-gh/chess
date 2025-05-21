package com.kern.GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.kern.Logic.Mouse;
import com.kern.Logic.Type;
import com.kern.Piece.Bishop;
import com.kern.Piece.King;
import com.kern.Piece.Knight;
import com.kern.Piece.Pawn;
import com.kern.Piece.Piece;
import com.kern.Piece.Queen;
import com.kern.Piece.Rook;

/**
 * The GamePanel class is the main component for the chess game's graphical user
 * interface.
 * It extends JPanel and implements Runnable to manage game rendering and
 * updating within its own thread.
 *
 * <p>
 * This class is responsible for initializing the game board, pieces, and
 * handling the game loop.
 * It sets up an 840x640 window, configures the initial positions for both White
 * and Black chess pieces,
 * and processes the game updates at a fixed FPS (frames per second) of 60.
 * </p>
 *
 * <p>
 * The class maintains two lists of pieces: one for the main game pieces
 * (pieces) and a duplicate list (simPieces),
 * which could be used for simulation or move validation purposes.
 * </p>
 *
 * <p>
 * Key methods include:
 * <ul>
 * <li>{@code setPieces()} - Initializes the chess pieces in their starting
 * positions.</li>
 * <li>{@code copyPieces(ArrayList<Piece> source, ArrayList<Piece> target)} -
 * Creates a copy of the game pieces list.</li>
 * <li>{@code launchGame()} - Starts the game loop in a separate thread.</li>
 * <li>{@code run()} - Contains the main loop that updates and repaints the game
 * at regular intervals.</li>
 * <li>{@code update()} - Updates game state (currently a placeholder for future
 * game logic).</li>
 * <li>{@code paintComponent(Graphics g)} - Renders the board and pieces onto
 * the panel.</li>
 * </ul>
 * </p>
 *
 * <p>
 * The game loop is designed to update and redraw the screen in a timely manner
 * based on the FPS setting.
 * </p>
 *
 * @see javax.swing.JPanel
 * @see java.lang.Runnable
 */
public class GamePanel extends JPanel implements Runnable {
    // Set the size of the window
    public static final int WINDOW_WIDTH = 840;
    public static final int WINDOW_HEIGHT = 640;

    final int FPS = 60;
    Thread gameThread;
    Mouse mouse = new Mouse();

    // PIECES
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    public static ArrayList<Piece> promotionPieces = new ArrayList<>();
    public static Piece castlingPiece;
    Piece activePiece;

    // COLORS
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;

    // BOOLEANS
    boolean canMove;
    boolean validSequare;
    Boolean canPromotion = false;

    public GamePanel() {
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(new Color(118, 150, 86));

        addMouseMotionListener(mouse);
        addMouseListener(mouse);

        setPieces();
        copyPieces(pieces, simPieces);
    }

    public void setPieces() {
        // White Pieces
        pieces.add(new Pawn(WHITE, 0, 6));
        pieces.add(new Pawn(WHITE, 1, 6));
        pieces.add(new Pawn(WHITE, 2, 6));
        pieces.add(new Pawn(WHITE, 3, 6));
        pieces.add(new Pawn(WHITE, 4, 6));
        pieces.add(new Pawn(WHITE, 5, 6));
        pieces.add(new Pawn(WHITE, 6, 6));
        pieces.add(new Pawn(WHITE, 7, 6));
        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));

        // Black Pieces
        pieces.add(new Pawn(BLACK, 0, 1));
        pieces.add(new Pawn(BLACK, 1, 1));
        pieces.add(new Pawn(BLACK, 2, 1));
        pieces.add(new Pawn(BLACK, 3, 1));
        pieces.add(new Pawn(BLACK, 4, 1));
        pieces.add(new Pawn(BLACK, 5, 1));
        pieces.add(new Pawn(BLACK, 6, 1));
        pieces.add(new Pawn(BLACK, 7, 1));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Knight(BLACK, 6, 0));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new King(BLACK, 4, 0));

    }

    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
        target.clear();

        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // Game loop
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {

        if (canPromotion) {
            promoting();
        } else {

            // If the mouse is pressed
            if (mouse.pressed) {
                // if there IS NO active piece, NO piece being held by mouse
                if (activePiece == null) {
                    // Iterate throught the pieces that meet the conditions Color (Ally) & Position
                    // (X & Y)
                    for (Piece piece : simPieces) {
                        if (piece.color == currentColor
                                && piece.column == mouse.x / Board.SQUARE_SIZE
                                && piece.row == mouse.y / Board.SQUARE_SIZE) {
                            // Set the active piece to current held piece
                            activePiece = piece;
                        }
                    }
                } else {
                    // if player already holding a piece, simulate the move
                    simulate();
                }
            }

            // If the mouse is released
            if (!mouse.pressed) {
                // if there IS an acive piece, piece being held by mouse
                if (activePiece != null) {
                    if (validSequare) {
                        // Move is confirmed

                        // Update the piece list in case a piece has been captured and removed during
                        // the simulation
                        copyPieces(simPieces, pieces);
                        activePiece.updatePosition();

                        if (castlingPiece != null) {
                            castlingPiece.updatePosition();
                        }

                        if (canPromote()) {
                            canPromotion = true;
                        } else {
                            changePlayer();
                        }
                    } else {
                        // Move is cancled

                        // Reset everything
                        activePiece.resetPosition();
                        activePiece = null;
                    }
                }
            }
        }
    }

    private void promoting() {
        if (mouse.pressed) {
            for (Piece piece : promotionPieces) {
                if (piece.column == mouse.x / Board.SQUARE_SIZE
                        && piece.row == mouse.y / Board.SQUARE_SIZE) {
                    switch (piece.type) {
                        case QUEEN:
                            simPieces.add(new Queen(currentColor, activePiece.column, activePiece.row));
                            break;
                        case ROOK:
                            simPieces.add(new Rook(currentColor, activePiece.column, activePiece.row));
                            break;
                        case KNIGHT:
                            simPieces.add(new Knight(currentColor, activePiece.column, activePiece.row));
                            break;
                        case BISHOP:
                            simPieces.add(new Bishop(currentColor, activePiece.column, activePiece.row));
                            break;
                        default:
                            break;
                    }
                    simPieces.remove(activePiece.getIndex());
                    copyPieces(simPieces, pieces);
                    activePiece = null;
                    canPromotion = false;
                    changePlayer();
                }
            }
        }
    }

    private void simulate() {

        canMove = false;
        validSequare = false;

        // Reset the piece list in every move
        // this is for restoring the removed piece during simulation
        copyPieces(pieces, simPieces);

        // Reset castling piece
        if (castlingPiece != null) {
            castlingPiece.column = castlingPiece.preColumn;
            castlingPiece.x = castlingPiece.getX(castlingPiece.column);
            castlingPiece = null;
        }

        // Update the active piece position to move along with the mouse
        activePiece.x = mouse.x - Board.HALF_SQUARE_SIZE;
        activePiece.y = mouse.y - Board.HALF_SQUARE_SIZE;

        activePiece.column = activePiece.getColumn(activePiece.x);
        activePiece.row = activePiece.getRow(activePiece.y);

        // check if the piece can move to the target square
        if (activePiece.canMove(activePiece.column, activePiece.row)) {
            canMove = true;

            // If hitting an oponent piece, remove it from the list
            if (activePiece.hittinPiece != null) {
                simPieces.remove(activePiece.hittinPiece.getIndex());
            }

            checkCastling();

            validSequare = true;
        }
    }

    public void checkCastling() {
        if (castlingPiece != null) {
            if (castlingPiece.column == 0) {
                castlingPiece.column += 3;
            } else if (castlingPiece.column == 7) {
                castlingPiece.column -= 2;
            }
            castlingPiece.x = castlingPiece.getX(castlingPiece.column);
        }
    }

    public void changePlayer() {
        if (currentColor == WHITE) {
            currentColor = BLACK;

            // Reset Two Stepped flag
            for (Piece piece : pieces) {
                if (piece.color == BLACK) {
                    piece.isTwoStepped = false;
                }
            }
        } else {
            currentColor = WHITE;

            // Reset Two Stepped flag
            for (Piece piece : pieces) {
                if (piece.color == WHITE) {
                    piece.isTwoStepped = false;
                }
            }
        }

        activePiece = null;
    }

    public boolean canPromote() {
        if (activePiece.type == Type.PAWN) {
            if (activePiece.color == WHITE && activePiece.row == 0
                    || activePiece.color == BLACK && activePiece.row == 7) {
                promotionPieces.clear();
                promotionPieces.add(new Queen(currentColor, 8, 2));
                promotionPieces.add(new Rook(currentColor, 8, 3));
                promotionPieces.add(new Knight(currentColor, 8, 4));
                promotionPieces.add(new Bishop(currentColor, 8, 5));
                return true;
            }
        }

        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the board
        Graphics2D g2d = (Graphics2D) g;
        Board.draw(g2d);

        // Draw the pieces
        for (Piece p : pieces) {
            p.draw(g2d);
        }

        if (activePiece != null) {
            // The original square highlight
            g2d.setColor(new Color(168, 200, 136));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.fillRect(
                    activePiece.preColumn * Board.SQUARE_SIZE,
                    activePiece.preRow * Board.SQUARE_SIZE,
                    Board.SQUARE_SIZE,
                    Board.SQUARE_SIZE);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            if (canMove) {
                g2d.setColor(new Color(118, 150, 86));
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.fillRect(
                        activePiece.column * Board.SQUARE_SIZE,
                        activePiece.row * Board.SQUARE_SIZE,
                        Board.SQUARE_SIZE,
                        Board.SQUARE_SIZE);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }

            // Draw the active piece in the end so it won't be hidden by the board or the
            // colored sequare
            activePiece.draw(g2d);
        }

        // Status message
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Book Antiqua", Font.PLAIN, 20));
        g2d.setColor(Color.white);

        if (canPromotion) {
            g2d.drawString("Promote to:", Board.SQUARE_SIZE * 8 + 20, 150);
            for (Piece piece : promotionPieces) {
                g2d.drawImage(piece.image, piece.getX(piece.column), piece.getY(piece.row),
                        Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
            }
        }

        if (currentColor == WHITE) {
            g2d.drawString("White's turn", Board.SQUARE_SIZE * 8 + 20, WINDOW_HEIGHT * 9 / 10);
        } else {
            g2d.drawString("Black's turn", Board.SQUARE_SIZE * 8 + 20, WINDOW_HEIGHT / 10);
        }
    }
}
