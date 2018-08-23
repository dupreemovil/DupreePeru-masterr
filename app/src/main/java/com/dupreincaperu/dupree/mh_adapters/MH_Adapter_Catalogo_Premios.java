package com.dupreincaperu.dupree.mh_adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.CatalogoPremiosList;
import com.dupreincaperu.dupree.mh_response_api.Oferta;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_Adapter_Catalogo_Premios extends RecyclerView.Adapter<MH_Adapter_Catalogo_Premios.ViewHolder> {
    private List<CatalogoPremiosList.Folleto> folleto;
    private String TAG = "MH_Adapter_Catalogo_P";

    private Context mContext;

    private ImageLoader img;
    private int posSelected = -1;

    public MH_Adapter_Catalogo_Premios(List<CatalogoPremiosList.Folleto> folleto, Context mContext){
        this.folleto = folleto;
        this.mContext = mContext;
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
        View view= inflate.inflate(R.layout.adapter_catalogo_premios, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e(TAG,"position: "+String.valueOf(position));
        //Log.e(TAG, new Gson().toJson(listFilter.get(position)));

        //quitar la imagen que manda backend en caso de no disponibles
        if(folleto.get(position).getImage().contains("")) {
            img = ImageLoader.getInstance();
            img.init(configurarImageLoader());
            img.displayImage(folleto.get(position).getImage(), holder.imgPremio);
        }

        holder.txtNamePremio.setText(folleto.get(position).getName());
        holder.txtCodePremio.setText(folleto.get(position).getCode());
    }

    @Override
    public int getItemCount() {
        int size = null != folleto ? folleto.size() : 0;
        Log.e(TAG, "size: ".concat(String.valueOf(size)));
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private ImageView imgPremio;
        private TextView txtNamePremio, txtCodePremio;

        public ViewHolder(View v) {
            super(v);
            //v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);

            imgPremio = (ImageView) v.findViewById(R.id.imgPremio);
            txtNamePremio = (TextView) v.findViewById(R.id.txtNamePremio);
            txtCodePremio = (TextView) v.findViewById(R.id.txtCodePremio);
            imgPremio.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if(view.getId()==R.id.imgPremio){
                if(clickListenerON) {
                    setPosSelected(getAdapterPosition());
                    itemsClickListener.onClickItem(view, getAdapterPosition());
                }
            }
            //notifyDataSetChanged();
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
     * pos selected
     */
    public void setPosSelected(int posSelected) {
        this.posSelected = posSelected;
    }

    public int getPosSelected() {
        return posSelected;
    }

    private void msgToast(String msg){
        Log.e("Toast", msg);
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }


}
