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
import com.dupreincaperu.dupree.mh_response_api.CDR;

import java.util.List;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_Adapter_cdr extends RecyclerView.Adapter<MH_Adapter_cdr.ViewHolder> {

    private List<CDR> listCDR, listFilter;
    private Context mContext;

    private int posSelected=-1;
    public MH_Adapter_cdr(List<CDR> listCDR, List<CDR> listFilter, Context mContext){
        this.listCDR = listCDR;
        this.listFilter = listFilter;
        this.mContext=mContext;

        //para filtrar
        mFilter = new CustomFilter(MH_Adapter_cdr.this);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view= inflate.inflate(R.layout.adapter_cdr, parent, false);
        return new ViewHolder(view);
    }

    Boolean blockEventsAutomatic=false;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvCamp.setText("".concat(String.valueOf(listFilter.get(position).getCampana())));
        holder.tvFecha.setText("".concat(listFilter.get(position).getFecha()));
        holder.tvFact.setText("".concat(String.valueOf(listFilter.get(position).getFactura())));
        holder.tvProduct.setText("".concat(String.valueOf(listFilter.get(position).getProductos())));
        holder.tvDescrip.setText("".concat(listFilter.get(position).getDescripcion()));
        holder.tvCantidad.setText("".concat(String.valueOf(listFilter.get(position).getCantidad())));
        holder.tvTReclamo.setText("".concat(String.valueOf(listFilter.get(position).gettReclamo())));
        holder.tvProcede.setText("".concat(listFilter.get(position).getProcede()));
        holder.tvObserv.setText("".concat(String.valueOf(listFilter.get(position).getObserv())));

        //holder.cardViewBackGround.setBackgroundResource(R.color.transp_Accent);

        holder.cardViewBackGround.setCardBackgroundColor(position%2!=0 ? mContext.getResources().getColor(R.color.transp_Accent) : mContext.getResources().getColor(R.color.transp_azulDupree));

    }

    @Override
    public int getItemCount() {
        return (null != listFilter ? listFilter.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private TextView tvCamp, tvFecha, tvFact, tvProduct, tvDescrip, tvCantidad, tvTReclamo, tvProcede, tvObserv;
        private CardView cardViewBackGround;
        //private ImageView imgB_Call;


        protected TextView tvNearbyUserUsername;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);

            tvCamp = (TextView) v.findViewById(R.id.tvCamp);
            tvFecha = (TextView) v.findViewById(R.id.tvFecha);
            tvFact = (TextView) v.findViewById(R.id.tvFact);
            tvProduct = (TextView) v.findViewById(R.id.tvProduct);
            tvDescrip = (TextView) v.findViewById(R.id.tvDescrip);
            tvCantidad = (TextView) v.findViewById(R.id.tvCantidad);
            tvTReclamo = (TextView) v.findViewById(R.id.tvTReclamo);
            tvProcede = (TextView) v.findViewById(R.id.tvProcede);
            tvObserv = (TextView) v.findViewById(R.id.tvObserv);

            cardViewBackGround = (CardView) v.findViewById(R.id.cardViewBackGround);

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
        private MH_Adapter_cdr mAdapter;
        private CustomFilter(MH_Adapter_cdr mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            listFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                listFilter.addAll(listCDR);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final CDR mWords : listCDR) {
                    if (String.valueOf(mWords.getProductos()).toLowerCase().contains(filterPattern)) {
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
