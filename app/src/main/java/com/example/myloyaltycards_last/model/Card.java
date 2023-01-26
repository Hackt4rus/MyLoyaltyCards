package com.example.myloyaltycards_last.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {
    private String clientCode;
    private String companyName;
    private String logoPath;
    private int clientCodeFormat;
    private int color;


    public Card() {
        this.clientCode = null;
        this.companyName = null;
        this.logoPath = null;
        this.clientCodeFormat = -1;
    }

    public Card(String companyName, String clientCode, int clientCodeFormat, int color) {
        this.companyName = companyName;
        this.clientCode = clientCode;
        this.clientCodeFormat = clientCodeFormat;
        this.logoPath = null;
        this.color = color;
    }

    public Card(String companyName, String clientCode, int clientCodeFormat, int color, String logoPath) {
        this.companyName = companyName;
        this.clientCode = clientCode;
        this.clientCodeFormat = clientCodeFormat;
        this.logoPath = logoPath;
        this.color = color;
    }

    public boolean isEqualToCard(Card card) {
        if(this.logoPath != null && card.logoPath != null) {
            if(this.clientCode.equals(card.clientCode)
                    && this.companyName.equals(card.companyName)
                    && this.logoPath.equals(card.logoPath)
                    && this.clientCodeFormat == card.clientCodeFormat
                    && this.color == card.color)
                return true;
        }

        else if(this.logoPath == null || card.logoPath == null) {
            if(this.clientCode.equals(card.clientCode)
                    && this.companyName.equals(card.companyName)
                    && this.clientCodeFormat == card.clientCodeFormat
                    && this.color == card.color)
                return true;
        }

        return false;
    }


    protected Card(Parcel in) {
        clientCode = in.readString();
        companyName = in.readString();
        logoPath = in.readString();
        clientCodeFormat = in.readInt();
        color = in.readInt();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    public String getClientCode() {
        return this.clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getClientCodeFormat() {
        return clientCodeFormat;
    }

    public void setClientCodeFormat(int clientCodeFormat) {
        this.clientCodeFormat = clientCodeFormat;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getLogoPath() {
        return logoPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(clientCode);
        parcel.writeString(companyName);
        parcel.writeString(logoPath);
        parcel.writeInt(clientCodeFormat);
        parcel.writeInt(color);
    }
}
