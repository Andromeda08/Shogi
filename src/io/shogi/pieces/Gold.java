package io.shogi.pieces;

import io.shogi.core.Board;
import io.shogi.core.Field;
import io.shogi.core.Piece;

public class Gold extends Piece {
    public Gold(int owner) {
        super(owner);
        setSymbol("G");
        setType("Gold");
    }

    public boolean canMove(Field current, Field target, Board board) {
        if (Math.abs(current.row() - target.row()) <= 1 &&
                Math.abs(current.col() - target.col()) <= 1) {
            // Don't allow backwards diagonal movement
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
            if (target.getPiece() != null && current.getPiece().getOwner() == target.getPiece().getOwner()) {
                return false;
            }
            return true;
        }
        return false;
    }

    // Gold pieces don't get promoted
    public void promote() {
        return;
    }
}
