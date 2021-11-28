package io.shogi.core;

import io.shogi.pieces.*;

import java.io.Serializable;
import java.util.ArrayList;

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

    public boolean movePiece(Field s, Field t, int turn, int check) {
        // TODO: Force king move in check
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
                        t.getPiece().setOwner(turn);
                        hands[p.getOwner() - 1].addPiece(t.getPiece());
                    }
                }

                s.setPiece(null);
                t.setPiece(p);

                isCheck(turn);

                checkMates();

                return true;
            }
        }
        if (isDrop) {
            System.out.println(s.getPiece().getOwner());
            System.out.println(p.getOwner());

            Piece temp = p;

            System.out.println("Dropping to {" + t.row() + ";" + t.col() + "}");

            // Check if dropped on piece
            if (t.getPiece() != null) {
                System.out.println("Illegal drop: Drop on piece");
                s.setPiece(null);
                hands[turn-1].addPiece(p);
                return false;
            }
            else {
                temp.setOwner(turn);
                t.setPiece(temp);
                boolean isCheck = isCheck(turn);
                t.setPiece(null);

                // Confirm pawn rule
                if (temp.getType().equals("Pawn")) {
                    for (int i = 0; i < 9; i++) {
                        if (board[i][t.col()].getPiece() != null) {
                            if (board[i][t.col()].getPiece().getType().equals("Pawn")) {
                                if (board[i][t.col()].getPiece().getOwner() == temp.getOwner()) {
                                    System.out.println("Illegal drop: Pawn already in column.");
                                    s.setPiece(null);
                                    hands[turn-1].addPiece(p);
                                    return false;
                                }
                            }
                        }
                    }
                }
                // Confirm check rule
                if (isCheck) {
                    System.out.println("Illegal drop: Results in check.");
                    s.setPiece(null);
                    hands[turn-1].addPiece(p);
                    return false;
                }

                // Legal drop
                System.out.println("Successful drop.");
                p.setOwner(turn);
                s.setPiece(null);
                t.setPiece(p);
                return true;
            }
        }

        return false;
    }

    private Field findEnemyKing(int turn) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].getPiece() != null) {
                    if (board[i][j].getPiece().getOwner() != turn && board[i][j].getPiece().getType().equals("King")) {
                        return board[i][j];
                    }
                }
            }
        }
        return null;
    }

    private boolean isCheck(int turn) {
        Field king = findEnemyKing(turn);

        if (king != null) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Field f = board[i][j];
                    if (f.getPiece() != null && f != king) {
                        if (f.getPiece().canMove(f, king, this)) {
                            System.out.println("Check from piece at [" + f.row() + ";" + f.col() + "]");
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private int checkMates() {
        if (isMate(1)) {
            return 2;
        }
        if (isMate(2)) {
            return 1;
        }
        return 0;
    }

    private boolean isMate(int turn) {
        Field f = findEnemyKing(turn);

        // Find any potential attackers for all valid king moves
        // 1) Get valid king move positions
        // 2) Check for attackers at valid move
        // 3) If attacked "disable" choice
        // 4) If all choices are "disabled" mate
        // TODO: "move" king while checking if king would be attacked @ valid move

        if (f != null && f.getPiece() != null) {
            Piece king = f.getPiece();
            ArrayList<Field> validKingMoves = new ArrayList<Field>();
            ArrayList<Boolean> isAttacked = new ArrayList<Boolean>();

            // Get valid king moves
            for (int dr = 0; dr < 3; dr++) {
                for (int dc = 0; dc < 3; dc++) {
                    if (f.row() + dr - 1 >= 0 && f.col() + dc -1 >= 0 && f.row() + dr - 1 < 9 && f.col() + dc - 1 < 9) {
                        Field target = board[f.row()+dr-1][f.col()+dc-1];
                        if (king.canMove(f, target, this)) {
                            validKingMoves.add(target);
                        }
                    }
                }
            }

            // Check for attackers at valid moves
            for (Field t : validKingMoves) {
                boolean atk = false;
                // Disable attacked fields
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (board[i][j].getPiece() != null && board[i][j].getPiece().getOwner() == turn) {
                            Piece attacker = board[i][j].getPiece();
                            if (attacker.canMove(board[i][j], t, this)) {
                                atk = true;
                            }
                        }
                    }
                }
                isAttacked.add(atk);
            }

            System.out.println("====[Mate Log]====");
            int c = 0;
            for (Field m : validKingMoves) {
                System.out.println("Move: [" + m.row() + ";" + m.col() + "] Attacked: " + (isAttacked.get(c) ? "Yes" : "No"));
                c++;
            }
            System.out.println("==================");

            // Count disabled moves
            int count = 0;
            for (Boolean b : isAttacked)
                if (b) count++;

            if (count == validKingMoves.size()) {
                System.out.println("Mate Found, Winner: [" + turn + "]");
                return true;
            }
        }

        System.out.println("No Mate Found!");
        return false;
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
