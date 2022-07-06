package org.example.logic;

public class Data {
    private long id;
    private String date;
    private double price;
    private double bedrooms;
    private double bathrooms;
    private double sqft_living;
    private double sqft_lot;
    private double floors;
    private boolean waterfront;
    private double view;
    private double condition;
    private double grade;
    private double sqft_above;
    private double sqft_basement;
    private int yr_built;
    private int yr_renovated;
    private int zipcode;
    private double latitude;
    private double longitude;
    private double sqft_living15;
    private double sqft_lot15;

    public Data(long id, String date, double price, double bedrooms, double bathrooms, double sqft_living,
                double sqft_lot, double floors, boolean waterfront, double view, double condition, double grade,
                double sqft_above, double sqft_basement, int yr_built, int yr_renovated, int zipcode,
                double latitude, double longitude, double sqft_living15, double sqft_lot15) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.sqft_living = sqft_living;
        this.sqft_lot = sqft_lot;
        this.floors = floors;
        this.waterfront = waterfront;
        this.view = view;
        this.condition = condition;
        this.grade = grade;
        this.sqft_above = sqft_above;
        this.sqft_basement = sqft_basement;
        this.yr_built = yr_built;
        this.yr_renovated = yr_renovated;
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sqft_living15 = sqft_living15;
        this.sqft_lot15 = sqft_lot15;
    }

    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public double getBedrooms() {
        return bedrooms;
    }

    public double getBathrooms() {
        return bathrooms;
    }

    public double getSqft_living() {
        return sqft_living;
    }

    public double getSqft_lot() {
        return sqft_lot;
    }

    public double getFloors() {
        return floors;
    }

    public boolean isWaterfront() {
        return waterfront;
    }

    public double getView() {
        return view;
    }

    public double getCondition() {
        return condition;
    }

    public double getGrade() {
        return grade;
    }

    public double getSqft_above() {
        return sqft_above;
    }

    public double getSqft_basement() {
        return sqft_basement;
    }

    public int getYr_built() {
        return yr_built;
    }

    public int getYr_renovated() {
        return yr_renovated;
    }

    public int getZipcode() {
        return zipcode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getSqft_living15() {
        return sqft_living15;
    }

    public double getSqft_lot15() {
        return sqft_lot15;
    }
}
