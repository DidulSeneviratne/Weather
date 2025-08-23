package com.izone.globalweather.air_quality;

public class CityList {

    private int meterValue;

    public int getMeterValue() {
        return meterValue;
    }

    public void setMeterValue(int meterValue) {
        this.meterValue = meterValue;
    }

    private double lat;
    private double lon;
    private String city;
    private double co;
    private double no;
    private double no2;
    private double o3;
    private double s02;
    private double nh3;

    private int getimage;

    public int getGetimage() {
        return getimage;
    }

    public void setGetimage(int getimage) {
        this.getimage = getimage;
    }

    public double getCo() {
        return co;
    }

    public void setCo(double co) {
        this.co = co;
    }

    public double getNo() {
        return no;
    }

    public void setNo(double no) {
        this.no = no;
    }

    public double getNo2() {
        return no2;
    }

    public void setNo2(double no2) {
        this.no2 = no2;
    }

    public double getO3() {
        return o3;
    }

    public void setO3(double o3) {
        this.o3 = o3;
    }

    public double getS02() {
        return s02;
    }

    public void setS02(double s02) {
        this.s02 = s02;
    }

    public double getNh3() {
        return nh3;
    }

    public void setNh3(double nh3) {
        this.nh3 = nh3;
    }

    public CityList(double lat, double lon, String city) {
        this.lat = lat;
        this.lon = lon;
        this.city = city;
    }

    public CityList(double lat, double lon, String city, double co, double no, double no2, double o3, double s02, double nh3,int getGetimage,int meterValue) {
        this.lat = lat;
        this.lon = lon;
        this.city = city;
        this.co = co;
        this.no = no;
        this.no2 = no2;
        this.o3 = o3;
        this.s02 = s02;
        this.nh3 = nh3;
        this.getimage = getGetimage;
        this.meterValue = meterValue;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
