package harinath.com.harinath.pojos;

public class UserRegPojo {
    public UserRegPojo(String firstname, String lastname, String password, String mobileNumber, String emailID, String type, String businessName, UnitLocation mLocation, String fb_key,String reg_key) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.emailID = emailID;
        this.type = type;
        this.businessName = businessName;
        this.mLocation = mLocation;
        this.fb_key = fb_key;
        this.reg_key = reg_key;

    }

    public UserRegPojo() {
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    String firstname;
    String lastname;
    String password;
    String mobileNumber;
    String emailID;
    String type;
    String businessName;

    public String getFb_key() {
        return fb_key;
    }

    public void setFb_key(String fb_key) {
        this.fb_key = fb_key;
    }

    String fb_key;

    public String getmyKey() {
        return reg_key;
    }

    public void setReg_key(String reg_key) {
        this.reg_key = reg_key;
    }

    String reg_key;

    public UnitLocation getmLocation() {
        return mLocation;
    }

    public void setmLocation(UnitLocation mLocation) {
        this.mLocation = mLocation;
    }

    UnitLocation mLocation;
}
