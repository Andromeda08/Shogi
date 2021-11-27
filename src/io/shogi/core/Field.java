package io.shogi.core;

import java.io.Serializable;

public class Field implements Serializable {
    private int r, c;
    private Piece piece;

    public Field(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public Piece getPiece() {
        return piece;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int row() {
        return r;
    }
    public int col() {
        return c;
    }
}
