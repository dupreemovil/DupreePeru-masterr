package com.dupreincaperu.dupree.mh_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.PanelGteDetail;
import com.dupreincaperu.dupree.mh_utilities.Validate;

import java.util.List;

import at.grabner.circleprogress.CircleProgressView;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_Adapter_PanelGte extends RecyclerView.Adapter<MH_Adapter_PanelGte.ViewHolder> {
    private List<PanelGteDetail> summaryGte;
    private static boolean clickListenerON=false;
    static MH_Adapter_PanelGte.ItemsClickListener ItemsClickListener;

    private static boolean longClickListenerON=false;
    private static MH_Adapter_PanelGte.ItemsLongClickListener ItemsLongClickListener;

    private Context mContext;

    private int posSelected=-1;
    public MH_Adapter_PanelGte(List<PanelGteDetail> summaryGte, Context mContext){
        this.summaryGte = summaryGte;
        this.mContext=mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view= inflate.inflate(R.layout.adapter_circleprogress, parent, false);
        return new ViewHolder(view);
    }

    Boolean blockEventsAutomatic=false;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvNameCamp.setText(summaryGte.get(position).getCampana());
        holder.tvCantidad.setText("".concat(summaryGte.get(position).getCantidad()));

        String porcent = summaryGte.get(position).getPorcentaje();//quita el simbolo de "%"
        String value = porcent.substring(0, porcent.length() -1);

        holder.circleProgress.setValue(Validate.isNumeric(value) ? Float.parseFloat(value) : 0);

        //decide color
        if(summaryGte.get(position).getStatus().equals("3")){
            holder.circleProgress.setBarColor(mContext.getResources().getColor(R.color.red_1));
            holder.circleProgress.setTextColor(mContext.getResources().getColor(R.color.red_1));
        } else if(summaryGte.get(position).getStatus().equals("1")){
            holder.circleProgress.setBarColor(mContext.getResources().getColor(R.color.green_check));
            holder.circleProgress.setTextColor(mContext.getResources().getColor(R.color.green_check));
        }

    }

    @Override
    public int getItemCount() {
        return (null != summaryGte ? summaryGte.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private TextView tvNameCamp, tvCantidad;
        private CircleProgressView circleProgress;


        protected TextView tvNearbyUserUsername;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);
            tvNameCamp = (TextView) v.findViewById(R.id.tvNameCamp);
            tvCantidad = (TextView) v.findViewById(R.id.tvCantidad);
            circleProgress = (CircleProgressView) v.findViewById(R.id.circleProgress);
            circleProgress.setMaxValue(100);
        }

        @Override
        public void onClick(View view) {

            if (clickListenerON) {
                ItemsClickListener.onClickItem(view, getAdapterPosition());
            }
            //notifyDataSetChanged();
        }

    }

    public void setRVOnItemClickListener(MH_Adapter_PanelGte.ItemsClickListener ItemsClickListener) {
        this.ItemsClickListener = ItemsClickListener;
        clickListenerON=true;
    }

    public interface ItemsClickListener {
        void onClickItem(View v, int position);
    }

    public void setRVOnItemLongClickListener(MH_Adapter_PanelGte.ItemsLongClickListener itemsLongClickListener) {
        ItemsLongClickListener = itemsLongClickListener;
        longClickListenerON=true;
    }

    public interface ItemsLongClickListener {
        void onLongClick(View v, int position);
    }

    public void setPosSelected(int posSelected) {
        this.posSelected = posSelected;
    }
}
