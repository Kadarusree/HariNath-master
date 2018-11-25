package harinath.com.harinath.pojos;

public class UnitLocation {

    double latitude, longitude;

    public UnitLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
//Test

    //Bug Resolved
    public UnitLocation() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
