package com.dupreincaperu.dupree.mh_adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.Pagosrealizados;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_Adapter_Pagos_Realiz extends RecyclerView.Adapter<MH_Adapter_Pagos_Realiz.ViewHolder> {

    private List<Pagosrealizados> listPagos, listFilter;

    private Context mContext;


    private int posSelected=-1;
    public MH_Adapter_Pagos_Realiz(List<Pagosrealizados> listPagos, List<Pagosrealizados> listFilter, Context mContext){

        this.listPagos = listPagos;
        this.listFilter = listFilter;
        this.mContext=mContext;

        //para filtrar
        mFilter = new CustomFilter(MH_Adapter_Pagos_Realiz.this);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view= inflate.inflate(R.layout.adapter_pagos_realizados, parent, false);
        return new ViewHolder(view);
    }

    Boolean blockEventsAutomatic=false;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvFecha.setText("".concat(String.valueOf(listFilter.get(position).getFecha())));
        holder.tvBanco.setText("".concat(listFilter.get(position).getBanco()));

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        holder.tvValor.setText("S/. ".concat(formatter.format(listFilter.get(position).getValor())));

        holder.cardViedPagos.setCardBackgroundColor(position%2!=0 ? mContext.getResources().getColor(R.color.transp_Accent) : mContext.getResources().getColor(R.color.transp_azulDupree));

    }

    @Override
    public int getItemCount() {
        return (null != listFilter ? listFilter.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private TextView tvFecha, tvBanco, tvValor;
        private CardView cardViedPagos;


        protected TextView tvNearbyUserUsername;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);

            tvFecha = (TextView) v.findViewById(R.id.tvFecha);
            tvBanco = (TextView) v.findViewById(R.id.tvBanco);
            tvValor = (TextView) v.findViewById(R.id.tvValor);

            cardViedPagos = (CardView) v.findViewById(R.id.cardViedPagos);

            //imgB_Call = (ImageView) v.findViewById(R.id.imgB_Call);
            //imgB_Call.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {


        }

    }

    /**
     * click
     */
    private boolean clickListenerON=false;
    private ItemsClickListener itemsClickListener;

    public void setRVOnItemClickListener(ItemsClickListener ItemsClickListener) {
        this.itemsClickListener = ItemsClickListener;
        clickListenerON=true;
    }
    public interface ItemsClickListener {
        void onClickItem(View v, int position);
    }

    /**
     * lon click
     */
    private boolean longClickListenerON=false;
    private ItemsLongClickListener itemsLongClickListener;

    public void setRVOnItemLongClickListener(ItemsLongClickListener itemsLongClickListener) {
        this.itemsLongClickListener = itemsLongClickListener;
        longClickListenerON=true;
    }
    public interface ItemsLongClickListener {
        void onLongClick(View v, int position);
    }

    public void setPosSelected(int posSelected) {
        this.posSelected = posSelected;
    }

    /**
     * FILTER
     */
    private CustomFilter mFilter;

    public CustomFilter getmFilter() {
        return mFilter;
    }

    public class CustomFilter extends Filter {
        private MH_Adapter_Pagos_Realiz mAdapter;
        private CustomFilter(MH_Adapter_Pagos_Realiz mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            listFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                listFilter.addAll(listPagos);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Pagosrealizados mWords : listPagos) {
                    if (String.valueOf(mWords.getFecha()).toLowerCase().contains(filterPattern)) {
                        listFilter.add(mWords);
                    }
                }
            }
            System.out.println("Count Number " + listFilter.size());
            results.values = listFilter;
            results.count = listFilter.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //System.out.println("Count Number 2 " + ((List<RespondeUsers.Users>) results.values).size());
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
