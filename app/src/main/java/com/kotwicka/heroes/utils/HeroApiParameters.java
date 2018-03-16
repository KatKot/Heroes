package com.kotwicka.heroes.utils;

public final class HeroApiParameters {

    public static final int LIMIT = 20;
    public static int TOTAL = 0;
    public static int OFFSET = 0;
    public static String NAME = "";
    public static boolean GET_FAVOURITES = false;

    private HeroApiParameters() {

    }

    public static void incrementOffset() {
        OFFSET += LIMIT;
    }

    public static void resetParameters() {
        OFFSET = 0;
        TOTAL = 0;
        NAME = "";
        GET_FAVOURITES = false;
    }

    public static void resetOffset() {
        OFFSET = 0;
    }

    public static boolean isLastPage() {
        return HeroApiParameters.TOTAL - HeroApiParameters.OFFSET - HeroApiParameters.LIMIT <= 0;
    }
}
