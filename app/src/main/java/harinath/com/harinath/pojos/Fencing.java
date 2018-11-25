package harinath.com.harinath.pojos;

public class Fencing {
    UnitLocation location;

    public Fencing(UnitLocation location, String name) {
        this.location = location;
        this.name = name;
    }

    public Fencing() {
    }

    public UnitLocation getLocation() {
        return location;
    }

    public void setLocation(UnitLocation location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
}
