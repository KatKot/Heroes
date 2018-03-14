package com.kotwicka.heroes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotwicka.heroes.net.model.Result;

public class HeroViewModel implements Parcelable {

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

    protected HeroViewModel(Parcel in) {
        name = in.readString();
        description = in.readString();
        photoPath = in.readString();
        total = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(photoPath);
        dest.writeInt(total);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HeroViewModel> CREATOR = new Creator<HeroViewModel>() {
        @Override
        public HeroViewModel createFromParcel(Parcel in) {
            return new HeroViewModel(in);
        }

        @Override
        public HeroViewModel[] newArray(int size) {
            return new HeroViewModel[size];
        }
    };

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
