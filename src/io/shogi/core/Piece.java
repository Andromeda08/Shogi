package io.shogi.core;

import java.io.Serializable;

public abstract class Piece implements Serializable {
    protected int owner;
    protected boolean promoted = false;
    private String type = "N";
    private String symbol = "N";

    public Piece(int owner) {
        this.owner = owner;
    }

    public abstract boolean canMove(Field current, Field target, Board board);

    public void promote() {
        if (!promoted) {
            promoted = true;
            setSymbol(symbol + "_P");
        }
    }

    public void demote() {
        promoted = false;
        setSymbol(symbol.split("_")[0]);
    }

    public boolean isPromoted() {
        return promoted;
    }

    public int getOwner() {
        return owner;
    }
    public String getSymbol() {
        return symbol;
    }
    public String getType() {
        return type;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public void setType(String type) {
        this.type = type;
    }
}
