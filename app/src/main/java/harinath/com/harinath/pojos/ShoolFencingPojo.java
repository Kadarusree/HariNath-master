package harinath.com.harinath.pojos;

public class ShoolFencingPojo {
    UnitLocation mLocation;
    float radius;
    String name;

    public ShoolFencingPojo(UnitLocation mLocation, float radius, String name) {
        this.mLocation = mLocation;
        this.radius = radius;
        this.name = name;
    }

    public ShoolFencingPojo() {
    }

    public UnitLocation getmLocation() {
        return mLocation;
    }

    public void setmLocation(UnitLocation mLocation) {
        this.mLocation = mLocation;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
