package harinath.com.harinath;

import android.content.Context;
import android.content.SharedPreferences;

import harinath.com.harinath.pojos.UserRegPojo;

public class SessionManager {

    Context mContext;
    SharedPreferences mSharedPref;
    SharedPreferences.Editor mEditor;


    private final String FIRST_NAME = "FIRST_NAME";
    private final String LAST_NAME = "LAST_NAME";
    private final String FB_ID = "FB_ID";
    private final String EMAIL = "EMAIL";
    private final String MOBILE = "MOBILE";
    private final String TYPE = "TYPE";
    private final String REG_ID = "REG_ID";
    private final String PASSWORD = "PASSWORD";
    private final String PARENT_ID = "PARENT_ID";


    public SessionManager(Context gContex) {
        this.mContext = gContex;
        mSharedPref = mContext.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPref.edit();
    }

    public void createSession(UserRegPojo mPojo){

        mEditor.putString(FIRST_NAME,mPojo.getFirstname());
        mEditor.putString(LAST_NAME,mPojo.getLastname());
        mEditor.putString(FB_ID,mPojo.getFb_key());
        mEditor.putString(EMAIL,mPojo.getEmailID());
        mEditor.putString(MOBILE,mPojo.getMobileNumber());
        mEditor.putString(TYPE,mPojo.getType());
        mEditor.putString(REG_ID,mPojo.getmyKey());
        mEditor.putString(PASSWORD,mPojo.getPassword());
        mEditor.putString(PARENT_ID,mPojo.getBusinessName());

        mEditor.commit();


    }

    public String getFB_ID() {
        return mSharedPref.getString(FB_ID,"");
    }

    public String getTYPE() {
         return mSharedPref.getString(TYPE,"");
    }

    public String getFIRST_NAME() {
        return mSharedPref.getString(FIRST_NAME,"");    }

    public String getPARENT_ID() {
        return mSharedPref.getString(PARENT_ID,"");    }

    public String getREG_ID() {
        return mSharedPref.getString(REG_ID,"");    }
}
