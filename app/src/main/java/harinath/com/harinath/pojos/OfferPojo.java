package harinath.com.harinath.pojos;

public class OfferPojo {

    String title, description, offeredBy;

    public OfferPojo() {
    }

    public OfferPojo(String title, String description, String offeredBy) {
        this.title = title;
        this.description = description;
        this.offeredBy = offeredBy;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOfferedBy() {
        return offeredBy;
    }

    public void setOfferedBy(String offeredBy) {
        this.offeredBy = offeredBy;
    }
}
