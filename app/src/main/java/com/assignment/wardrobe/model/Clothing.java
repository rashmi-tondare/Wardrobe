package com.assignment.wardrobe.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rashmi on 20/12/16.
 */

public class Clothing implements Parcelable{
    private long id;
    private int clothingType;
    private String filePath;

    public Clothing() {
    }

    public Clothing(int clothingType, String filePath) {
        this.clothingType = clothingType;
        this.filePath = filePath;
    }

    public Clothing(long id, int clothingType, String filePath) {
        this.id = id;
        this.clothingType = clothingType;
        this.filePath = filePath;
    }

    protected Clothing(Parcel in) {
        id = in.readLong();
        clothingType = in.readInt();
        filePath = in.readString();
    }

    public static final Creator<Clothing> CREATOR = new Creator<Clothing>() {
        @Override
        public Clothing createFromParcel(Parcel in) {
            return new Clothing(in);
        }

        @Override
        public Clothing[] newArray(int size) {
            return new Clothing[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getClothingType() {
        return clothingType;
    }

    public void setClothingType(int clothingType) {
        this.clothingType = clothingType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeInt(clothingType);
        parcel.writeString(filePath);
    }
}
