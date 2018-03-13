package com.kotwicka.heroes.model;

import com.kotwicka.heroes.net.model.Result;

public class HeroViewModel {

    private static final String NO_DESCRIPTION = "No description available.";
    private static final String PHOTO_EXTENSION_PREFIX = ".";

    private final String name;
    private final String description;
    private final String photoPath;
    private final int total;

    public HeroViewModel() {
        this.name = null;
        this.description = null;
        this.photoPath = null;
        this.total = 0;
    }

    public HeroViewModel(final Result result, final String total) {
        this.name = result.getName();
        this.description = result.getDescription();
        this.photoPath = result.getThumbnail().getPath() + PHOTO_EXTENSION_PREFIX + result.getThumbnail().getExtension();
        this.total = Integer.valueOf(total);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        if(description == null || description.isEmpty()) {
            return NO_DESCRIPTION;
        }
        return description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public int getTotal() {
        return total;
    }
}
