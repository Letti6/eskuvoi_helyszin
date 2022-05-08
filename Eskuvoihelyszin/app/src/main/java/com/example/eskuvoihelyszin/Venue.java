package com.example.eskuvoihelyszin;

import android.os.Parcel;
import android.os.Parcelable;

public class Venue implements Parcelable {
    private String name;
    private String city;
    private String info;
    private String price;
    private int imageResource;

    public Venue(String name, String city, String info, String price, int imageResource) {
        this.name = name;
        this.city = city;
        this.info = info;
        this.price = price;
        this.imageResource = imageResource;
    }

    public Venue(){}

    protected Venue(Parcel in) {
        name = in.readString();
        city = in.readString();
        info = in.readString();
        price = in.readString();
        imageResource = in.readInt();
    }

    public static final Creator<Venue> CREATOR = new Creator<Venue>() {
        @Override
        public Venue createFromParcel(Parcel in) {
            return new Venue(in);
        }

        @Override
        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getInfo() {
        return info;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(city);
        parcel.writeString(info);
        parcel.writeString(price);
        parcel.writeInt(imageResource);
    }
}
