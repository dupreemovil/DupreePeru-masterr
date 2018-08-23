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
import com.dupreincaperu.dupree.mh_response_api.PQR;

import java.util.List;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_Adapter_SeguimientoPQR extends RecyclerView.Adapter<MH_Adapter_SeguimientoPQR.ViewHolder> {

    private List<PQR> listPQR, listFilter;
    private Context mContext;

    private int posSelected=-1;
    public MH_Adapter_SeguimientoPQR(List<PQR> listPQR, List<PQR> listFilter, Context mContext){
        this.listPQR = listPQR;
        this.listFilter = listFilter;
        this.mContext=mContext;

        //para filtrar
        mFilter = new CustomFilter(MH_Adapter_SeguimientoPQR.this);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view= inflate.inflate(R.layout.adapter_seguimiento_pqr, parent, false);
        return new ViewHolder(view);
    }

    Boolean blockEventsAutomatic=false;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvCaso.setText("".concat(String.valueOf(listFilter.get(position).getCaso())));
        holder.tvfecha.setText("".concat(listFilter.get(position).getFecha()));
        holder.tvUsuario.setText("".concat(String.valueOf(listFilter.get(position).getUsuario())));
        holder.tvGestion.setText("".concat(String.valueOf(listFilter.get(position).getGestion())));
        holder.tvRazon.setText("".concat(listFilter.get(position).getRazon()));
        holder.tvDescripcion.setText("".concat(String.valueOf(listFilter.get(position).getDescripcion())));

        holder.cardViewBackGround.setCardBackgroundColor(position%2!=0 ? mContext.getResources().getColor(R.color.transp_Accent) : mContext.getResources().getColor(R.color.transp_azulDupree));
    }

    @Override
    public int getItemCount() {
        return (null != listFilter ? listFilter.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private TextView tvCaso, tvfecha, tvUsuario, tvGestion, tvRazon, tvDescripcion;
        //private ImageView imgB_Call;
        private CardView cardViewBackGround;


        protected TextView tvNearbyUserUsername;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);

            tvCaso = (TextView) v.findViewById(R.id.tvCaso);
            tvfecha = (TextView) v.findViewById(R.id.tvfecha);
            tvUsuario = (TextView) v.findViewById(R.id.tvUsuario);
            tvGestion = (TextView) v.findViewById(R.id.tvGestion);
            tvRazon = (TextView) v.findViewById(R.id.tvRazon);
            tvDescripcion = (TextView) v.findViewById(R.id.tvDescripcion);

            //imgB_Call = (ImageView) v.findViewById(R.id.imgB_Call);
            //imgB_Call.setOnClickListener(this);
            cardViewBackGround = (CardView) v.findViewById(R.id.cardViewBackGround);
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
        private MH_Adapter_SeguimientoPQR mAdapter;
        private CustomFilter(MH_Adapter_SeguimientoPQR mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            listFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                listFilter.addAll(listPQR);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final PQR mWords : listPQR) {
                    if (String.valueOf(mWords.getCaso()).toLowerCase().contains(filterPattern)) {
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
