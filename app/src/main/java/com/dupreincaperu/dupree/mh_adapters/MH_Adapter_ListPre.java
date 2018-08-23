package com.dupreincaperu.dupree.mh_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.Preinscripcion;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_Adapter_ListPre extends RecyclerView.Adapter<MH_Adapter_ListPre.ViewHolder> {
    public static final String STATUS_AUTORIZADO = "AUTORIZADO";
    public static final String STATUS_RECHAZADO = "RECHAZADO";
    public static final String STATUS_PENDIENTE = "PENDIENTE";
    private List<Preinscripcion> listPre;


    private Context mContext;


    private int posSelected=-1;
    public MH_Adapter_ListPre(List<Preinscripcion> listPre, Context mContext){

        this.listPre = listPre;
        this.mContext=mContext;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view= inflate.inflate(R.layout.adapter_list_preinscrip, parent, false);
        return new ViewHolder(view);
    }

    Boolean blockEventsAutomatic=false;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvStatus.setText(listPre.get(position).getEstado());
        holder.tvName.setText("".concat(listPre.get(position).getNombre()));

        holder.tvCedula.setText("Cédula: ".concat(listPre.get(position).getCedula()));
        holder.tvCelular.setText("Celular: ".concat(listPre.get(position).getCelular()));
        holder.tvfechaNac.setText("FN: ".concat(listPre.get(position).getFecha()));
        holder.tvCiudad.setText("Ciudad: ".concat(listPre.get(position).getName_ciudad()));
        holder.tvBarrio.setText("Barrio ".concat(listPre.get(position).getBarrio()));
        holder.tvfechaReg.setText("F. Registro: ".concat(listPre.get(position).getFecha()));
        holder.tvObservacion.setText("Observacion: ".concat(listPre.get(position).getObservacion()));

        switch(listPre.get(position).getEstado()){
            case STATUS_AUTORIZADO:
                holder.imagen.setImageResource(R.drawable.registrado);
                break;
            case STATUS_RECHAZADO:
                holder.imagen.setImageResource(R.drawable.rechazado);
                break;
            case STATUS_PENDIENTE:
                holder.imagen.setImageResource(R.drawable.pendiente);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return (null != listPre ? listPre.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private TextView tvStatus, tvName, tvCedula, tvCelular, tvfechaNac, tvCiudad, tvBarrio, tvfechaReg, tvObservacion;
        private CircleImageView imagen;


        protected TextView tvNearbyUserUsername;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);
            tvStatus = (TextView) v.findViewById(R.id.tvStatus);
            tvName = (TextView) v.findViewById(R.id.tvName);
            tvCedula = (TextView) v.findViewById(R.id.tvCedula);
            tvCelular = (TextView) v.findViewById(R.id.tvCelular);
            tvfechaNac = (TextView) v.findViewById(R.id.tvfechaNac);
            tvCiudad = (TextView) v.findViewById(R.id.tvCiudad);
            tvBarrio = (TextView) v.findViewById(R.id.tvBarrio);
            tvfechaReg = (TextView) v.findViewById(R.id.tvfechaReg);
            tvObservacion = (TextView) v.findViewById(R.id.tvObservacion);

            imagen = (CircleImageView) v.findViewById(R.id.imagen);

        }

        @Override
        public void onClick(View view) {
            if (clickListenerON) {
                setPosSelected(getAdapterPosition());
                ItemsClickListener.onClickItem(view, getAdapterPosition());
            }
            //notifyDataSetChanged();
        }

    }

    private static boolean clickListenerON=false;
    static ItemsClickListener ItemsClickListener;


    public void setRVOnItemClickListener(ItemsClickListener ItemsClickListener) {
        this.ItemsClickListener = ItemsClickListener;
        clickListenerON=true;
    }

    public interface ItemsClickListener {
        void onClickItem(View v, int position);
    }


    private static boolean longClickListenerON=false;
    private static MH_Adapter_ListPre.ItemsLongClickListener ItemsLongClickListener;
    public void setRVOnItemLongClickListener(ItemsLongClickListener itemsLongClickListener) {
        ItemsLongClickListener = itemsLongClickListener;
        longClickListenerON=true;
    }

    public interface ItemsLongClickListener {
        void onLongClick(View v, int position);
    }

    public void setPosSelected(int posSelected) {
        this.posSelected = posSelected;
    }

    public int getPosSelected() {
        return posSelected;
    }
}
