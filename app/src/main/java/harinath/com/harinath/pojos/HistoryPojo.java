package harinath.com.harinath.pojos;

import harinath.com.harinath.LatLng;

public class HistoryPojo {
    String name,time, type, parentID;
    LatLng location;
    int battry_status;

    public HistoryPojo(String name, String time, String type, String parentID, LatLng location, int battry_status) {
        this.name = name;
        this.time = time;
        this.type = type;
        this.parentID = parentID;
        this.location = location;
        this.battry_status = battry_status;
    }

    public HistoryPojo() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getBattry_status() {
        return battry_status;
    }

    public void setBattry_status(int battry_status) {
        this.battry_status = battry_status;
    }
}
