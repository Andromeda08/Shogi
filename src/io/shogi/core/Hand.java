package io.shogi.core;

import java.io.Serializable;
import java.util.ArrayList;

public class Hand implements Serializable {
    private ArrayList<Piece> pieces = new ArrayList<Piece>();
    private final int owner;

    public Hand(int owner) {
        this.owner = owner;
        for (int i = 0; i < 40; i++) {
            pieces.add(null);
        }
    }

    public void addPiece(Piece piece) {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) == null) {
                pieces.set(i, piece);
                return;
            }
        }
    }

    public void removePiece(Piece piece) {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) == piece) {
                pieces.set(i, null);
                return;
            }
        }
    }

    public void setPiece(int idx, Piece piece) {
        pieces.set(idx, piece);
    }

    public Piece getPiece(int idx) {
        return pieces.get(idx);
    }

    public int getOwner() {
        return owner;
    }

    public ArrayList<Piece> getList() {
        return pieces;
    }
}
