package com.dupreincaperu.dupree.mh_adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.Tracking;

import java.util.List;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_Adapter_Tracking extends RecyclerView.Adapter<MH_Adapter_Tracking.ViewHolder> {

    private List<Tracking> listFilter;
    private Context mContext;
    private String[] title ={"Recibido","Facturado", "Embalado", "Despachado", "Entregado"};

    private int posSelected=-1;
    public MH_Adapter_Tracking(List<Tracking> listFilter, Context mContext){
        this.listFilter = listFilter;
        this.mContext=mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view= inflate.inflate(R.layout.adapter_tracking, parent, false);
        return new ViewHolder(view);
    }

    Boolean blockEventsAutomatic=false;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position<5)
            holder.tvTitleRcv.setText(title[position]);

        holder.tvDateRcv.setText(listFilter.get(position).getFecha());
        holder.tvDateRcv.setVisibility(listFilter.get(position).getFecha().isEmpty() ? View.GONE : View.VISIBLE);

        holder.tvDetailRcv.setText(listFilter.get(position).getNombre());
        //backend esta repitiendo el titulo
        holder.tvDetailRcv.setVisibility(listFilter.get(position).getNombre().equals(title[position]) ? View.GONE : View.VISIBLE);


        if(listFilter.get(position).getCheck().equals("1"))
        {
            holder.imgIconCircle.setBackground(mContext.getResources().getDrawable(R.drawable.circle_green));
            holder.tvTitleRcv.setTypeface(holder.tvTitleRcv.getTypeface(), Typeface.BOLD);
            holder.tvTitleRcv.setTextColor(mContext.getResources().getColor(R.color.green_check));
        }
        else{
            //holder.imgIconCircle.setBackground(mContext.getResources().getDrawable(R.drawable.circle_concentric_green));
            holder.imgIconCircle.setBackground(mContext.getResources().getDrawable(R.drawable.circle_concentric_gray));
            holder.tvTitleRcv.setTypeface(holder.tvTitleRcv.getTypeface(), Typeface.NORMAL);
            holder.tvTitleRcv.setTextColor(mContext.getResources().getColor(R.color.gray_5));
        }

    }

    @Override
    public int getItemCount() {
        return (null != listFilter ? listFilter.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitleRcv, tvDateRcv, tvDetailRcv;
        private ImageView imgIconCircle;

        protected TextView tvNearbyUserUsername;
        public ViewHolder(View v) {
            super(v);
            tvTitleRcv = (TextView) v.findViewById(R.id.tvTitleRcv);
            tvDateRcv = (TextView) v.findViewById(R.id.tvDateRcv);
            tvDetailRcv = (TextView) v.findViewById(R.id.tvDetailRcv);

            imgIconCircle = (ImageView) v.findViewById(R.id.imgIconCircle);

            tvTitleRcv.setText("");
            tvDateRcv.setText("");
            tvDetailRcv.setText("");
        }

    }

    public void setPosSelected(int posSelected) {
        this.posSelected = posSelected;
    }

}
