package io.shogi.core;

import io.shogi.pieces.*;

import java.io.Serializable;

public class Board implements Serializable {
    private final Field[][] board = new Field[9][9];
    private final Hand[] hands = { new Hand(1), new Hand(2) };

    /* L N S G K G S N L
     *   B           R
     * P P P P P P P P P
     *
     *
     * P P P P P P P P P
     *   B           R
     * L N G S K G S N L
     */

    public Board() {
        // Initialize Fields
        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = new Field(i, j);
            }
        }

        // Add Pawns
        for (int i = 0; i < 9; i++) {
            board[2][i].setPiece(new Pawn(1));
            board[6][i].setPiece(new Pawn(2));
        }

        // Add Lances
        board[0][0].setPiece(new Lance(1));
        board[0][8].setPiece(new Lance(1));
        board[8][0].setPiece(new Lance(2));
        board[8][8].setPiece(new Lance(2));

        // Add Knights
        board[0][1].setPiece(new Knight(1));
        board[0][7].setPiece(new Knight(1));
        board[8][1].setPiece(new Knight(2));
        board[8][7].setPiece(new Knight(2));

        // Add Silvers
        board[0][2].setPiece(new Silver(1));
        board[0][6].setPiece(new Silver(1));
        board[8][2].setPiece(new Silver(2));
        board[8][6].setPiece(new Silver(2));

        // Add Golds
        board[0][3].setPiece(new Gold(1));
        board[0][5].setPiece(new Gold(1));
        board[8][3].setPiece(new Gold(2));
        board[8][5].setPiece(new Gold(2));

        // Add Kings
        board[0][4].setPiece(new King(1));
        board[8][4].setPiece(new King(2));

        // Add Rooks
        board[1][1].setPiece(new Rook(1));
        board[7][7].setPiece(new Rook(2));

        // Add Bishops
        board[1][7].setPiece(new Bishop(1));
        board[7][1].setPiece(new Bishop(2));
    }

    public void movePiece(Field s, Field t, int turn) {
        // TODO: Check "check" and "mate" after move
        Piece p = s.getPiece();

        boolean isOwner = p.getOwner() == turn;
        boolean isMove = s.col() != t.col() || s.row() != t.row();
        boolean canMove = p.canMove(s, t, this);
        boolean isPromotionRow = (t.row() <= 2 && p.getOwner() == 2) || (t.row() >= 6 && p.getOwner() == 1);
        boolean isDrop = (s.col() == 99 && s.row() == 99);

        if (isOwner && isMove) {
            if (canMove) {
                // Promotion
                if (isPromotionRow) {
                    s.getPiece().promote();
                }
                // Capture
                if (t.getPiece() != null) {
                    if (t.getPiece().getOwner() != p.getOwner()) {
                        hands[p.getOwner() - 1].addPiece(t.getPiece());
                    }
                }
                s.setPiece(null);
                t.setPiece(p);
            }
        }
        if (isDrop) {
            if (t.getPiece() == null) {
                if (s.getPiece().getType().equals("Pawn")) {
                    for (int i = 0; i < 9; i++) {
                        if (board[i][t.col()].getPiece() != null) {
                            if (board[i][t.col()].getPiece().getType().equals("Pawn")) {
                                if (board[i][t.col()].getPiece().getOwner() == s.getPiece().getOwner()) {
                                    // TODO: Implement pawn drop rules.
                                    // TODO: Cancel drop if illegal.
                                    System.out.println("Illegal pawn drop");
                                }
                            }
                        }
                    }
                }
                s.setPiece(null);
                t.setPiece(p);
            }
            else {
                hands[p.getOwner()].addPiece(p);
            }
        }

    }

    public Field getField(int i, int j) {
        return board[i][j];
    }

    public Hand getHand(int owner) { return hands[owner]; }

    public void print() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].getPiece() == null)
                    output.append("+");
                else
                    output.append(board[i][j].getPiece().getSymbol());
            }
            output.append("\n");
        }
        System.out.println(output);
    }

}
