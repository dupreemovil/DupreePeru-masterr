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
import com.dupreincaperu.dupree.mh_response_api.PtosByCamp;

import java.util.List;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_Adapter_puntos_asesora extends RecyclerView.Adapter<MH_Adapter_puntos_asesora.ViewHolder> {

    private List<PtosByCamp> listPtosCamp, listFilter;
    private Context mContext;

    private int posSelected=-1;
    public MH_Adapter_puntos_asesora(List<PtosByCamp> listPtosCamp, List<PtosByCamp> listFilter, Context mContext){
        this.listPtosCamp = listPtosCamp;
        this.listFilter = listFilter;
        this.mContext=mContext;

        //para filtrar
        mFilter = new CustomFilter(MH_Adapter_puntos_asesora.this);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view= inflate.inflate(R.layout.adapter_puntos_asesora, parent, false);
        return new ViewHolder(view);
    }

    Boolean blockEventsAutomatic=false;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvCamp.setText("".concat(String.valueOf(listFilter.get(position).getCampana())));
        holder.tvPedido.setText("".concat(String.valueOf(listFilter.get(position).getPuntos_Pedido())));
        holder.tvReferidos.setText("".concat(listFilter.get(position).getPuntos_Referido()));
        holder.tvPtsReferido.setText("".concat(String.valueOf(listFilter.get(position).getTotal_Referidos())));//
        holder.tvTotal.setText("".concat(String.valueOf(listFilter.get(position).getTotal_Puntos())));//
        holder.tvDevolucion.setText("".concat(listFilter.get(position).getPerdidos_Devolucion()));
        holder.tvCartera.setText("".concat(String.valueOf(listFilter.get(position).getPerdidos_Cartera())));
        holder.tvInactividad.setText("".concat(String.valueOf(listFilter.get(position).getPerdidos_Inactividad())));
        holder.tvPerdidos.setText("".concat(listFilter.get(position).getPuntos_Pedido()));
        holder.tvAdicionales.setText("".concat(String.valueOf(listFilter.get(position).getPuntos_Adicionales())));

        holder.tvEfectivos.setText("".concat(String.valueOf(listFilter.get(position).getPuntos_Efectivos())));
        holder.tvRedimidos.setText("".concat(listFilter.get(position).getPuntos_Redimidos()));
        holder.tvPago.setText("".concat(String.valueOf(listFilter.get(position).getEstado_Pago())));

        //holder.cardViewBackGround.setBackgroundResource(R.color.transp_Accent);

        holder.cardViewBackGround.setCardBackgroundColor(position%2!=0 ? mContext.getResources().getColor(R.color.transp_Accent) : mContext.getResources().getColor(R.color.transp_azulDupree));

    }

    @Override
    public int getItemCount() {
        return (null != listFilter ? listFilter.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private TextView tvCamp, tvPedido, tvReferidos, tvPtsReferido, tvTotal, tvDevolucion, tvCartera,
                tvInactividad, tvPerdidos, tvAdicionales, tvEfectivos, tvRedimidos, tvPago;
        private CardView cardViewBackGround;
        //private ImageView imgB_Call;


        protected TextView tvNearbyUserUsername;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);

            tvCamp = (TextView) v.findViewById(R.id.tvCamp);
            tvPedido = (TextView) v.findViewById(R.id.tvPedido);
            tvReferidos = (TextView) v.findViewById(R.id.tvReferidos);
            tvPtsReferido = (TextView) v.findViewById(R.id.tvPtsReferido);
            tvTotal = (TextView) v.findViewById(R.id.tvTotal);
            tvDevolucion = (TextView) v.findViewById(R.id.tvDevolucion);
            tvCartera = (TextView) v.findViewById(R.id.tvCartera);
            tvInactividad = (TextView) v.findViewById(R.id.tvInactividad);
            tvPerdidos = (TextView) v.findViewById(R.id.tvPerdidos);
            tvAdicionales = (TextView) v.findViewById(R.id.tvAdicionales);

            tvEfectivos = (TextView) v.findViewById(R.id.tvEfectivos);
            tvRedimidos = (TextView) v.findViewById(R.id.tvRedimidos);
            tvPago = (TextView) v.findViewById(R.id.tvPago);

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
        private MH_Adapter_puntos_asesora mAdapter;
        private CustomFilter(MH_Adapter_puntos_asesora mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            listFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                listFilter.addAll(listPtosCamp);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final PtosByCamp mWords : listPtosCamp) {
                    if (String.valueOf(mWords.getCampana()).toLowerCase().contains(filterPattern)) {
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
