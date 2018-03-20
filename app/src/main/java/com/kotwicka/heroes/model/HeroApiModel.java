package com.kotwicka.heroes.model;

public class HeroApiModel {

    private static final int LIMIT = 20;
    private int total;
    private int offset;
    private String name;
    private boolean shouldGetFavourites;

    public HeroApiModel() {
        this.total = 0;
        this.offset = 0;
        this.name = "";
        this.shouldGetFavourites = false;
    }

    public void incrementOffset() {
        offset += LIMIT;
    }

    public void resetParameters() {
        offset = 0;
        total = 0;
        name = "";
        shouldGetFavourites = false;
    }

    public void resetOffset() {
        offset = 0;
    }

    public boolean isLastPage() {
        return (total - offset - LIMIT) <= 0;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean shouldGetFavourites() {
        return shouldGetFavourites;
    }

    public void setShouldGetFavourites(boolean shouldGetFavourites) {
        this.shouldGetFavourites = shouldGetFavourites;
    }

    public static int getLimit() {
        return LIMIT;
    }
}
