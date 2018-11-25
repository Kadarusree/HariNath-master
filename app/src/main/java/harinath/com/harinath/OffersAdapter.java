package harinath.com.harinath;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import harinath.com.harinath.pojos.OfferPojo;
import harinath.com.harinath.pojos.UserRegPojo;


/**
 * Created by srikanthk on 7/26/2018.
 */

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder>

{

    private Context mContext;
    private List<OfferPojo> mOffersList;
    int selected_position = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, mobile, email, type, meeting_for;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.meeting_title);
            mobile = (TextView) view.findViewById(R.id.meeting_description);
            email = (TextView) view.findViewById(R.id.meeting_time);
            type = (TextView) view.findViewById(R.id.meeting_venue);
            meeting_for = (TextView) view.findViewById(R.id.meeting_for);
        }
    }


    public OffersAdapter(Context mContext, List<OfferPojo> mOffersList) {
        this.mContext = mContext;
        this.mOffersList = mOffersList;
    }

    @Override
    public OffersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_list_item, parent, false);

        return new OffersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        OfferPojo mOffer = mOffersList.get(position);

        String fontPath = "BigTetx.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
        holder.name.setTypeface(tf);
        holder.name.setText(mOffer.getTitle());
        holder.mobile.setText("Description : " + mOffer.getDescription());
        holder.email.setText("Offered By : " + mOffer.getOfferedBy());


    }


    @Override
    public int getItemCount() {
        return mOffersList.size();
    }


}
