package com.example.mathe.movieteller.classes;


import android.os.Parcel;
import android.os.Parcelable;

public class ResultsTrailer implements Parcelable {
    private String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String key;
    private String name;
    private int size;
    private String site;
    private String type;

    protected ResultsTrailer(Parcel in) {
        id = in.readString();
        iso_639_1 = in.readString();
        iso_3166_1 = in.readString();
        key = in.readString();
        name = in.readString();
        size = in.readInt();
        site = in.readString();
        type = in.readString();
    }

    public ResultsTrailer(String id, String iso_639_1, String iso_3166_1, String key, String name, int size, String site, String type) {
        this.id = id;
        this.iso_639_1 = iso_639_1;
        this.iso_3166_1 = iso_3166_1;
        this.key = key;
        this.name = name;
        this.size = size;
        this.site = site;
        this.type = type;
    }

    public static final Creator<ResultsTrailer> CREATOR = new Creator<ResultsTrailer>() {
        @Override
        public ResultsTrailer createFromParcel(Parcel in) {
            return new ResultsTrailer(in);
        }

        @Override
        public ResultsTrailer[] newArray(int size) {
            return new ResultsTrailer[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
    }
}
