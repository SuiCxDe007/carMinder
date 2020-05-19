package com.suicxde.carminder.Models;

public class Data {
    private String vehicleName;
    private String serRem;
    private String oilRem;
    private String filRem;
    private String airRem;
    private int id;

    public Data(int id, String vehicleName, String serviceRemind, String oilRemind, String filterRemind, String airRemind) {
        this.vehicleName =vehicleName;
        this.serRem = serviceRemind;
        this.oilRem = oilRemind;
        this.filRem = filterRemind;
        this.id = id;
        this.airRem = airRemind;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getSerRem() {
        return serRem;
    }

    public String getOilRem() {
        return oilRem;
    }

    public String getFilRem() {
        return filRem;
    }

    public int getId() {
        return id;
    }

    public String getAirRem() {
        return airRem;
    }
}
