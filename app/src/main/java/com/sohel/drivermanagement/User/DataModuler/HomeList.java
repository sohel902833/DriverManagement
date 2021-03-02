package com.sohel.drivermanagement.User.DataModuler;

public class HomeList {

    String homeId,homeName,totalFloor;

    public HomeList(){}
    public HomeList(String homeId, String homeName, String totalFloor) {
        this.homeId = homeId;
        this.homeName = homeName;
        this.totalFloor = totalFloor;
    }

    public String getHomeId() {
        return homeId;
    }

    public String getHomeName() {
        return homeName;
    }

    public String getTotalFloor() {
        return totalFloor;
    }
}
