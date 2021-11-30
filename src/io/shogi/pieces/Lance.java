package io.shogi.pieces;

import io.shogi.core.Board;
import io.shogi.core.Field;
import io.shogi.core.Piece;
import io.shogi.core.PieceType;

/**
 * Jari
 */
public class Lance extends Piece {
    /**
     * Jari constructor
     * @param owner Tulajdonos
     */
    public Lance(int owner) {
        super(owner);
        setSymbol("L");
        setType(PieceType.KYOUSHA);
    }

    /**
     * Lance movement code
     * @param current A mező, ahol az egység tartózkodik.
     * @param target A mező, ahova szeretnénk lépni.
     * @param board A játéktábla.
     * @return Tud-e lépni az egység.
     */
    @Override
    public boolean canMove(Field current, Field target, Board board) {
        if (promoted) {
            // Narikyou movement = Tokin movement
            if (Math.abs(current.row() - target.row()) <= 1 &&
                    Math.abs(current.col() - target.col()) <= 1) {
                if (owner == 1) {
                    if (current.row() - target.row() == 1 &&
                            current.col() != target.col()) {
                        return false;
                    }
                }
                if (owner == 2) {
                    if (current.row() - target.row() == -1 &&
                            current.col() != target.col()) {
                        return false;
                    }
                }
                if(target.getPiece() != null) {
                    if (current.getPiece().getOwner() == target.getPiece().getOwner()) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        if (current.col() == target.col()) {
            // Kyousha movement
            if(owner == 2) {
                if(current.row() - target.row() > 0) {
                    for (int i = current.row() - 1; i > target.row(); i--) {
                        if (board.getField(i, current.col()).getPiece() != null) {
                            return  false;
                        }
                    }
                }
                else {
                    return false;
                }
            }
            if (owner == 1) {
                if(current.row() - target.row() < 0) {
                    for (int i = current.row() + 1; i < target.row(); i++) {
                        if (board.getField(i, current.col()).getPiece() != null) {
                            return  false;
                        }
                    }
                }
                else {
                    return false;
                }
            }
            if(target.getPiece() != null) {
                if (current.getPiece().getOwner() == target.getPiece().getOwner()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
