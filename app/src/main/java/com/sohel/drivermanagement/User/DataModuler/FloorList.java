package com.sohel.drivermanagement.User.DataModuler;

public class FloorList {
    String floorId;
    String floorName;
    String alotedPerson;
    String homeId;

    public FloorList(){}

    public FloorList(String floorId, String floorName, String alotedPerson,String homeId) {
        this.floorId = floorId;
        this.floorName = floorName;
        this.alotedPerson = alotedPerson;
        this.homeId=homeId;
    }


    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getFloorName() {
        return floorName;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getAlotedPerson() {
        return alotedPerson;
    }

    public void setAlotedPerson(String alotedPerson) {
        this.alotedPerson = alotedPerson;
    }
}
