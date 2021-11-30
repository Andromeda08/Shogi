package io.shogi.core;

import io.shogi.pieces.*;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Shogi Tábla
 */
public class Board implements Serializable {
    // Enable logging in console?
    private final boolean log = true;

    // Board size
    private final int BOARD_SIZE = 9;

    // 9x9 Grid of fields on the board
    private final Field[][] board = new Field[BOARD_SIZE][BOARD_SIZE];

    // Player hands
    private final Hand[] hands = { new Hand(1), new Hand(2) };

    /**
     * Board class constructor
     * A játékmenethez szükséges táblát állítja elő.
     */
    public Board() {
        // Initialize Fields
        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = new Field(i, j);
            }
        }
        // Setup Board
        setupBoard();
    }

    /**
     * Egy egység léptetéséért felős függvény. (Lépés vagy elhelyezés)
     * @param s Ahonnan mozgatni szeretnénk az egységet.
     * @param t Ahová szeretnénk mozgatni az egységet.
     * @param turn Soron lévő játékos.
     * @return Sikeres volt-e a lépés.
     */
    public boolean movePiece(Field s, Field t, int turn) {
        if (s.getPiece() != null) {
            Piece p = s.getPiece();
            PieceType pt = p.getType();

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

                    // Mate check
                    if(!hasLegalMoves(turn)) {
                        // End game & Close window
                        JOptionPane.showMessageDialog(null, "Player [" + p.getOwner() + "] wins!", "Game Over!", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }

                    if (isCheck((turn == 2) ? 1: 2)) {
                        System.out.println("You are in check, make a legal move please.");
                        s.setPiece(p);
                        t.setPiece(null);
                        return false;
                    }

                    return true;
                }
            }
            // Is the move a drop?
            if (isDrop) {
                if (log) System.out.println("Dropping to [" + t.row() + ";" + t.col() + "]");

                // Check if we are dropping on an already occupied field
                if (t.getPiece() != null) {
                    s.setPiece(null);
                    hands[turn-1].addPiece(p);
                    if(log) System.out.println("Illegal drop: Field already occupied.");
                    return false;
                }
                else {
                    // Evaluate other rules
                    t.setPiece(p);
                    boolean isCheck = isCheck(turn);
                    boolean isPawn = p.getType() == PieceType.FUHYO;
                    t.setPiece(null);

                    // Check for bad row drops
                    if (isBadRowDrop(turn, pt, t)) {
                        s.setPiece(null);
                        hands[turn-1].addPiece(p);
                        if (log) System.out.println("Illegal drop: Piece wouldn't have any legal moves.");
                        return false;
                    }
                    // Check if there is already a pawn in the column where we are dropping
                    if (isPawn) {
                        for (int i = 0; i < 9; i++) {
                            if (board[i][t.col()].getPiece() != null) {
                                if (board[i][t.col()].getPiece().getType() == PieceType.FUHYO) {
                                    if (board[i][t.col()].getPiece().getOwner() == p.getOwner()) {
                                        s.setPiece(null);
                                        hands[turn-1].addPiece(p);
                                        if (log) System.out.println("Illegal drop: Pawn already in column.");
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    // Check if drop results in immediate check
                    if (isCheck) {
                        s.setPiece(null);
                        hands[turn-1].addPiece(p);
                        if (log) System.out.println("Illegal drop: Results in check.");
                        return false;
                    }

                    // Drop was legal
                    s.setPiece(null);
                    t.setPiece(p);
                    if (log) System.out.println("Successful drop.");
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Megkeresei az mezőt, amelyen tartózkodik az ellenséges király.
     * @param turn Soron lévő játékos.
     * @return A mező, amelyen tartózkodik az ellenséges király.
     */
    private Field findEnemyKing(int turn) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].getPiece() != null) {
                    if (board[i][j].getPiece().getOwner() != turn && board[i][j].getPiece().getType() == PieceType.OSHO) {
                        return board[i][j];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Kideríti van-e legális lépése egy sakkban lévő játékosnak.
     * @param turn Soron lévő játékos.
     * @return Van-e legális lépése a sakkban lévő játékosnak
     */
    private boolean hasLegalMoves(int turn) {
        if (!isCheck(turn)) {
            return true;
        }

        Field f = findEnemyKing(turn);

        if (f != null) {
            Piece king = f.getPiece();
            ArrayList<Field> legalKingMoves = new ArrayList<>();
            ArrayList<Field> attackers = new ArrayList<>();
            ArrayList<Boolean> isAttacked = new ArrayList<>();

            // Get valid king moves
            for (int dr = 0; dr < 3; dr++) {
                for (int dc = 0; dc < 3; dc++) {
                    if (f.row() + dr - 1 >= 0 && f.col() + dc -1 >= 0 && f.row() + dr - 1 < 9 && f.col() + dc - 1 < 9) {
                        Field target = board[f.row()+dr-1][f.col()+dc-1];
                        if (king.canMove(f, target, this)) {
                            legalKingMoves.add(target);
                        }
                    }
                }
            }

            // Check for attackers at valid moves
            for (Field t : legalKingMoves) {
                // Temporarily remove king
                f.setPiece(null);
                boolean atk = false;
                // Disable attacked fields
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (board[i][j].getPiece() != null && board[i][j].getPiece().getOwner() == turn) {
                            Piece attacker = board[i][j].getPiece();
                            if (attacker.canMove(board[i][j], t, this)) {
                                attackers.add(board[i][j]);
                                atk = true;
                            }
                        }
                    }
                }
                isAttacked.add(atk);
                f.setPiece(king);
            }

            // Check if we can kill attackers
            for (Field t : attackers) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (board[i][j].getPiece() != null && board[i][j].getPiece().getOwner() != turn) {
                            Piece defender = board[i][j].getPiece();
                            if (defender.canMove(board[i][j], t, this)) {
                                attackers.add(board[i][j]);
                                return true;
                            }
                        }
                    }
                }
            }

            System.out.println("====[Mate Log]====");
            int c = 0;
            for (Field m : legalKingMoves) {
                System.out.println("Move: [" + m.row() + ";" + m.col() + "] Attacked: " + (isAttacked.get(c) ? "Yes" : "No"));
                c++;
            }
            System.out.println("==================");

            // Count disabled moves
            int count = 0;
            for (Boolean b : isAttacked)
                if (b) count++;

            if (count == legalKingMoves.size()) {
                System.out.println("Mate Found, Winner: [" + turn + "]");
                return false;
            }
        }

        System.out.println("No Mate Found!");
        return true;
        }

    /**
     * Ellenőrzi, hogy sakkban van-e az ellenfél.
     * @param turn Soron lévő játékos.
     * @return Sakkban van-e az ellenfél.
     */
    private boolean isCheck(int turn) {
        Field king = findEnemyKing(turn);
        if (king != null) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Field f = board[i][j];
                    if (f.getPiece() != null && f != king) {
                        if (f.getPiece().canMove(f, king, this)) {
                            if(log)System.out.println("Check from piece at [" + f.row() + ";" + f.col() + "]");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Ellenőrzi az egységelhelyezés sorokra vonatkozó szabályát.
     * @param turn Soron lévő játékos.
     * @param pt Tesztelendő egység tipusa.
     * @param t Célmező
     * @return Elhelyezhető?
     */
    private boolean isBadRowDrop(int turn, PieceType pt, Field t) {
        if (pt == PieceType.FUHYO || pt == PieceType.KYOUSHA) {
            if (turn == 2 && t.row() == 0)
                return true;
            if (turn == 1 && t.row() == 8)
                return true;
        }
        if (pt == PieceType.KEIMA) {
            if (turn == 2 && t.row() <= 1)
                return true;
            if (turn == 1 && t.row() >= 7)
                return true;
        }
        return false;
    }

    /**
     * A Shogi szabályai szerint elhelyezi az egységeket a játékmenet elején.
     */
    private void setupBoard() {
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

    /**
     * A játéktábla egy mezőjét adja vissza.
     * @param i Sor
     * @param j Oszlop
     * @return A keresett mező.
     */
    public Field getField(int i, int j) {
        if(i >= 0 && i < 9 && j >= 0 && j < 9) return board[i][j];
        else return null;
    }

    /**
     * Egy játékos "kezét" adja vissza.
     * @param owner A kéz tulajdonosa
     * @return A kéz.
     */
    public Hand getHand(int owner) {
        if (owner >= 0 && owner < 2) return hands[owner];
        else return null;
    }
}
