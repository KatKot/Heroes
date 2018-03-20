package com.kotwicka.heroes.list.adapter;

public enum ViewHolderType {

    HERO(1),
    LOADING(2);

    private int type;

    ViewHolderType (final int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
