package com.sohel.drivermanagement.User.DataModuler;

public class FloorList2 {
    String homeId;
    String floorId;
    String alotedPerson;
    String alotedRenterId;
    String electricityBill;
    String floorName;
    String gasBill;
    String rentAmount;
    String utilitiesBill;
    String waterBill;
    String aloted;
    String dueAmount;
public FloorList2(){}
    public FloorList2(String homeId, String floorId, String alotedPerson, String alotedRenterId, String electricityBill, String floorName, String gasBill, String rentAmount, String utilitiesBill, String waterBill, String aloted, String dueAmount) {
        this.homeId = homeId;
        this.floorId = floorId;
        this.alotedPerson = alotedPerson;
        this.alotedRenterId = alotedRenterId;
        this.electricityBill = electricityBill;
        this.floorName = floorName;
        this.gasBill = gasBill;
        this.rentAmount = rentAmount;
        this.utilitiesBill = utilitiesBill;
        this.waterBill = waterBill;
        this.aloted = aloted;
        this.dueAmount = dueAmount;
    }


    public String getHomeId() {
        return homeId;
    }

    public String getFloorId() {
        return floorId;
    }

    public String getAlotedPerson() {
        return alotedPerson;
    }

    public String getAlotedRenterId() {
        return alotedRenterId;
    }

    public String getElectricityBill() {
        return electricityBill;
    }

    public String getFloorName() {
        return floorName;
    }

    public String getGasBill() {
        return gasBill;
    }

    public String getRentAmount() {
        return rentAmount;
    }

    public String getUtilitiesBill() {
        return utilitiesBill;
    }

    public String getWaterBill() {
        return waterBill;
    }

    public String getAloted() {
        return aloted;
    }

    public String getDueAmount() {
        return dueAmount;
    }
}
