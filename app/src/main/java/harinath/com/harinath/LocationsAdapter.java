package harinath.com.harinath;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import harinath.com.harinath.pojos.HistoryPojo;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.MyViewHolder>

{

    private Activity mContext;
    private List<HistoryPojo> mHistoryPojoList;
    int selected_position = 0;

    String TAG = UsersListAdapter.class.getName();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, mobile, email, type, meeting_for;
        final ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.meeting_title);
            mobile = (TextView) view.findViewById(R.id.meeting_description);
            email = (TextView) view.findViewById(R.id.meeting_time);
            type = (TextView) view.findViewById(R.id.meeting_venue);
            meeting_for = (TextView) view.findViewById(R.id.meeting_for);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public LocationsAdapter(Activity mContext, List<HistoryPojo> mHistoryPojoList) {
        this.mContext = mContext;
        this.mHistoryPojoList = mHistoryPojoList;
    }

    @Override
    public LocationsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meeting_list_item, parent, false);

        return new LocationsAdapter.MyViewHolder(itemView);
    }

    int position_;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        HistoryPojo mUser = mHistoryPojoList.get(position);

        String fontPath = "BigTetx.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
        holder.name.setTypeface(tf);
        holder.name.setText(" Time " + getDate(Long.parseLong(mUser.getTime()),"dd/MM/yyyy hh:mm:ss")+"");
        holder.mobile.setText("Type : " + mUser.getType());
        holder.email.setText("Name : " + mUser.getName());
        holder.type.setText("Battery Level: " + mUser.getBattry_status()+"%");

            holder.overflow.setVisibility(View.INVISIBLE);




    }


    @Override
    public int getItemCount() {
        return mHistoryPojoList.size();
    }



    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}