package com.ecoorbit.api.model;

public class Coordenadas {

    private Double latitude;
    private Double longitude;

    public Coordenadas() {
    }

    public Coordenadas(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
