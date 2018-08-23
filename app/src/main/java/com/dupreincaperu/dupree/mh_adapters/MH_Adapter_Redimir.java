package com.dupreincaperu.dupree.mh_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.Premios;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_Adapter_Redimir extends RecyclerView.Adapter<MH_Adapter_Redimir.ViewHolder> {
    private List<Premios> listPremios;
    private static boolean clickListenerON=false;
    static MH_Adapter_Redimir.ItemsClickListener ItemsClickListener;

    private static boolean longClickListenerON=false;
    private static MH_Adapter_Redimir.ItemsLongClickListener ItemsLongClickListener;

    private Context mContext;

    ImageLoader img;


    private int posSelected=-1;
    public MH_Adapter_Redimir(List<Premios> listPremios, Context mContext){

        this.listPremios = listPremios;
        this.mContext=mContext;

    }

    private ImageLoaderConfiguration configurarImageLoader() {
        DisplayImageOptions opcionesDefault = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)//@drawable/logocompany
                .showImageOnFail(R.drawable.no_image)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnLoading(R.drawable.loading)
                .resetViewBeforeLoading(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(opcionesDefault)
                .threadPriority(Thread.NORM_PRIORITY-2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();
        return config;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view= inflate.inflate(R.layout.adapter_redimir_incentivos, parent, false);
        return new ViewHolder(view);
    }

    Boolean blockEventsAutomatic=false;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvCode.setText(listPremios.get(position).getCodigo());
        holder.tvName.setText(listPremios.get(position).getNombre());
        holder.tvDescription.setText(listPremios.get(position).getDescripcion());
        holder.tvPuntos.setText(listPremios.get(position).getPuntos().concat(" pts."));
        holder.tvCantidad.setText("N. ".concat(listPremios.get(position).getCantidad()));

        img = ImageLoader.getInstance();
        img.init(configurarImageLoader());
        img.displayImage(listPremios.get(position).getImagen(), holder.imagen);//cuadrar la imagen del grupo

        //si ya solicito premio se inhbilita
        holder.swtRedimir.setChecked(listPremios.get(position).isSelected());
        holder.swtRedimir.setEnabled(listPremios.get(position).isEnable());

    }

    @Override
    public int getItemCount() {
        return (null != listPremios ? listPremios.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private CircleImageView imagen;
        private SwitchCompat swtRedimir;
        private TextView tvCode, tvName, tvDescription, tvPuntos, tvCantidad;


        protected TextView tvNearbyUserUsername;
        public ViewHolder(View v) {
            super(v);
            //v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);
            imagen = (CircleImageView) v.findViewById(R.id.imagen);

            swtRedimir = (SwitchCompat) v.findViewById(R.id.swtRedimir);

            tvCode = (TextView) v.findViewById(R.id.tvCode);
            tvName = (TextView) v.findViewById(R.id.tvName);
            tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            tvPuntos = (TextView) v.findViewById(R.id.tvPuntos);
            tvCantidad = (TextView) v.findViewById(R.id.tvCantidad);

            imagen.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.imagen){
                if (clickListenerON) {
                    ItemsClickListener.onClickItem(view, getAdapterPosition());
                }
            }
            //notifyDataSetChanged();
        }

    }

    public void setRVOnItemClickListener(MH_Adapter_Redimir.ItemsClickListener ItemsClickListener) {
        this.ItemsClickListener = ItemsClickListener;
        clickListenerON=true;
    }

    public interface ItemsClickListener {
        void onClickItem(View v, int position);
    }

    public void setRVOnItemLongClickListener(MH_Adapter_Redimir.ItemsLongClickListener itemsLongClickListener) {
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
