package com.dupreincaperu.dupree.mh_adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.Retenido;

import java.util.List;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_Adapter_Retenidos extends RecyclerView.Adapter<MH_Adapter_Retenidos.ViewHolder> {
    private final String STATUS_AUTORIZADO = "AUTORIZADO";
    private final String STATUS_RECHAZADO = "RECHAZADO";
    private final String STATUS_PENDIENTE = "PENDIENTE";
    private List<Retenido> listRetenidos, listFilter;

    private Context mContext;


    private int posSelected=-1;
    public MH_Adapter_Retenidos(List<Retenido> listRetenidos, List<Retenido> listFilter, Context mContext){

        this.listRetenidos = listRetenidos;
        this.listFilter = listFilter;
        this.mContext=mContext;

        //para filtrar
        mFilter = new CustomFilter(MH_Adapter_Retenidos.this);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view= inflate.inflate(R.layout.adapter_retenidos, parent, false);
        return new ViewHolder(view);
    }

    Boolean blockEventsAutomatic=false;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvNameRet.setText(listFilter.get(position).getName());
        holder.tvCedulaRet.setText("DNI:".concat(listFilter.get(position).getCedula()));

        holder.tvTelefonoRet.setText("Telef.: ".concat(listFilter.get(position).getCelular()));
        holder.tvAreaRef.setText("Area: ".concat(listFilter.get(position).getArea()));
        holder.tvZonaRef.setText("Zona: ".concat(listFilter.get(position).getZona()));
        holder.tvCodeRet.setText("".concat(listFilter.get(position).getCode()));
        holder.tvNumPedidosRet.setText("".concat(listFilter.get(position).getNumPedido()));
        holder.tvCarteraRet.setText("".concat(listFilter.get(position).getCartera()));

        holder.tvRCupoRet.setText("".concat(listFilter.get(position).getR_cupo()));
        holder.tvBloqueoRet.setText("".concat(listFilter.get(position).getBloqueo()));
        holder.tvNetoRet.setText("".concat(listFilter.get(position).getNeto()));
        holder.tvSaldoRet.setText("".concat(listFilter.get(position).getSaldo()));
        holder.tvCupoRet.setText("".concat(listFilter.get(position).getCupo()));
        holder.tvMinPunlicoRet.setText("".concat(listFilter.get(position).getMinimoPublico()));

        holder.tvMontoMinRet.setText("".concat(listFilter.get(position).getMontoMinimo()));
        holder.tvTotalPublicoRet.setText("".concat(listFilter.get(position).getTotalPublico()));

        holder.cardViewBackGround.setCardBackgroundColor(position%2!=0 ? mContext.getResources().getColor(R.color.transp_Accent) : mContext.getResources().getColor(R.color.transp_azulDupree));


    }

    @Override
    public int getItemCount() {
        return (null != listFilter ? listFilter.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private TextView tvNameRet, tvCedulaRet, tvTelefonoRet, tvAreaRef, tvZonaRef, tvCodeRet,
                tvNumPedidosRet, tvCarteraRet, tvRCupoRet, tvBloqueoRet, tvNetoRet, tvSaldoRet,
                tvCupoRet, tvMinPunlicoRet, tvMontoMinRet, tvTotalPublicoRet;
        private ImageView imgB_Call;
        private CardView cardViewBackGround;


        protected TextView tvNearbyUserUsername;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);

            tvNameRet = (TextView) v.findViewById(R.id.tvNameRet);
            tvCedulaRet = (TextView) v.findViewById(R.id.tvCedulaRet);
            tvTelefonoRet = (TextView) v.findViewById(R.id.tvTelefonoRet);
            tvAreaRef = (TextView) v.findViewById(R.id.tvAreaRef);
            tvZonaRef = (TextView) v.findViewById(R.id.tvZonaRef);
            tvCodeRet = (TextView) v.findViewById(R.id.tvCodeRet);
            tvNumPedidosRet = (TextView) v.findViewById(R.id.tvNumPedidosRet);
            tvCarteraRet = (TextView) v.findViewById(R.id.tvCarteraRet);
            tvRCupoRet = (TextView) v.findViewById(R.id.tvRCupoRet);
            tvBloqueoRet = (TextView) v.findViewById(R.id.tvBloqueoRet);
            tvNetoRet = (TextView) v.findViewById(R.id.tvNetoRet);
            tvSaldoRet = (TextView) v.findViewById(R.id.tvSaldoRet);
            tvCupoRet = (TextView) v.findViewById(R.id.tvCupoRet);
            tvMinPunlicoRet = (TextView) v.findViewById(R.id.tvMinPunlicoRet);
            tvMontoMinRet = (TextView) v.findViewById(R.id.tvMontoMinRet);
            tvTotalPublicoRet = (TextView) v.findViewById(R.id.tvTotalPublicoRet);

            cardViewBackGround = (CardView) v.findViewById(R.id.cardViewBackGround);

            imgB_Call = (ImageView) v.findViewById(R.id.imgB_Call);
            imgB_Call.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.imgB_Call){
                if(callistenerON){
                    callClickListener.onCallClick(view, getAdapterPosition());
                }

            }

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

    /**
     * event call clickk
     */
    private boolean callistenerON=false;
    private CallClickListener callClickListener;

    public void setRVOnCallClickListener(CallClickListener callClickListener) {
        this.callClickListener = callClickListener;
        callistenerON=true;
    }

    public interface CallClickListener {
        void onCallClick(View v, int position);
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
        private MH_Adapter_Retenidos mAdapter;
        private CustomFilter(MH_Adapter_Retenidos mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            listFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                listFilter.addAll(listRetenidos);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Retenido mWords : listRetenidos) {
                    if (mWords.getCedula().toLowerCase().contains(filterPattern)) {
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
