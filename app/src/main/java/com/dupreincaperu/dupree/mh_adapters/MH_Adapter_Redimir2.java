package com.dupreincaperu.dupree.mh_adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.Premios;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_Adapter_Redimir2 extends RecyclerView.Adapter<MH_Adapter_Redimir2.ViewHolder> {
    private List<Premios> listPremio, listFilter;

    private Context mContext;

    ImageLoader img;
    private int posSelected=-1;

    public MH_Adapter_Redimir2(List<Premios> listPremio, List<Premios> listFilter, Context mContext){

        this.listPremio = listPremio;
        this.listFilter = listFilter;
        this.mContext=mContext;

        //para filtrar
        mFilter = new CustomFilter(MH_Adapter_Redimir2.this);

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
        View view= inflate.inflate(R.layout.adapter_redimir2, parent, false);
        return new ViewHolder(view);
    }

    Boolean blockEventsAutomatic=false;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        img = ImageLoader.getInstance();
        img.init(configurarImageLoader());
        img.displayImage(listFilter.get(position).getImagen(), holder.imagen);//cuadrar la imagen del grupo

        holder.tvCode.setText("CÓDIGO: ".concat(listFilter.get(position).getCodigo()));
        holder.tvDescription.setText(listFilter.get(position).getNombre());
        holder.tvPage.setText("".concat(listFilter.get(position).getDescripcion()));

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        holder.tvValor.setText("".concat(formatter.format(Float.parseFloat(listFilter.get(position).getPuntos()))).concat(" pts."));

        holder.tvCantidad.setText(String.valueOf(listFilter.get(position).getCantidad()));

        //verifica si ya esta en el server seleccionado (no se puede modificar)
        if(listFilter.get(position).isSelected()){
            listFilter.get(position).setInTheCart(true);//indica que ya esta en el carrito (server), y debe inhabilitar este elemento
            holder.imgB_Increase.setEnabled(false);
            holder.imgB_Decrease.setEnabled(false);
            holder.imgB_Increase.setClickable(false);
            holder.imgB_Decrease.setClickable(false);

            holder.imgB_Increase.setColorFilter(R.color.gray_5, PorterDuff.Mode.MULTIPLY);
            holder.imgB_Decrease.setColorFilter(R.color.gray_5, PorterDuff.Mode.MULTIPLY);
        }


        //validar si ya esta (o no) en el carrito para mostrar las vistas.
        holder.ctn_IncDecCant.setVisibility(listFilter.get(position).isInTheCart() ? View.VISIBLE : View.GONE);
        holder.ctn_AddCart.setVisibility(listFilter.get(position).isInTheCart() ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return (null != listFilter ? listFilter.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private CircleImageView imagen;
        View row;

        private TextView tvPage, tvCode, tvDescription, tvValor, tvCantidad, tvAddCart;
        private ImageButton imgB_Decrease, imgB_Increase, imgB_AddCart;
        private LinearLayout ctn_IncDecCant, ctn_AddCart;


        protected TextView tvNearbyUserUsername;
        public ViewHolder(View v) {
            super(v);
            this.row=v;
            //v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);
            imagen = (CircleImageView) v.findViewById(R.id.imagen);

            //swtRedimir = (SwitchCompat) v.findViewById(R.id.swtRedimir);

            tvPage = (TextView) v.findViewById(R.id.tvPage);
            tvCode = (TextView) v.findViewById(R.id.tvCode);
            tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            tvValor = (TextView) v.findViewById(R.id.tvValor);
            tvCantidad = (TextView) v.findViewById(R.id.tvCantidad);

            tvValor = (TextView) v.findViewById(R.id.tvValor);
            tvCantidad = (TextView) v.findViewById(R.id.tvCantidad);

            ctn_IncDecCant = (LinearLayout) v.findViewById(R.id.ctn_IncDecCant);
            imgB_Decrease = (ImageButton) v.findViewById(R.id.imgB_Decrease);
            imgB_Increase = (ImageButton) v.findViewById(R.id.imgB_Increase);

            ctn_AddCart = (LinearLayout) v.findViewById(R.id.ctn_AddCart);
            imgB_AddCart = (ImageButton) v.findViewById(R.id.imgB_AddCart);
            tvAddCart = (TextView) v.findViewById(R.id.tvAddCart);

            imgB_Decrease.setOnClickListener(this);
            imgB_Increase.setOnClickListener(this);
            imgB_AddCart.setOnClickListener(this);
            tvAddCart.setOnClickListener(this);
            imagen.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.imgB_Decrease){
                if(decreaseListenerON) {
                    setPosSelected(getAdapterPosition());
                    decreaseClickListener.onDecreaseClick(view, getAdapterPosition());
                }
            } else if(view.getId()==R.id.imgB_Increase){
                if(increaseListenerON) {
                    setPosSelected(getAdapterPosition());
                    increaseClickListener.onIncreaseClick(view, getAdapterPosition());
                }
            } else if((view.getId()==R.id.imgB_AddCart) || (view.getId()==R.id.tvAddCart)){
                if (addCartListenerON) {
                    setPosSelected(getAdapterPosition());
                    addCartClickListener.onAddCartClick(view, getAdapterPosition());
                }
            } else if((view.getId()==R.id.imagen)){
                if (clickImageListenerON) {
                    setPosSelected(getAdapterPosition());
                    imageClickListener.onClickImage(view, getAdapterPosition());
                }
            }
            //notifyDataSetChanged();
        }

    }

    /**
     * click
     */
    private boolean clickImageListenerON=false;
    private ImageClickListener imageClickListener;

    public void setRVOnImageClickListener(ImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
        clickImageListenerON=true;
    }
    public interface ImageClickListener {
        void onClickImage(View v, int position);
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
     * event decrease click
     */
    private boolean decreaseListenerON=false;
    private DecreaseClickListener decreaseClickListener;

    public void setRVOnDecreaseClickListener(DecreaseClickListener decreaseClickListener) {
        this.decreaseClickListener = decreaseClickListener;
        decreaseListenerON=true;
    }

    public interface DecreaseClickListener {
        void onDecreaseClick(View v, int position);
    }

    /**
     * event increase click
     */
    private boolean increaseListenerON=false;
    private IncreaseClickListener increaseClickListener;

    public void setRVOnIncreaseClickListener(IncreaseClickListener increaseClickListener) {
        this.increaseClickListener = increaseClickListener;
        increaseListenerON=true;
    }

    public interface IncreaseClickListener {
        void onIncreaseClick(View v, int position);
    }

    /**
     * event addcart click
     */
    private boolean addCartListenerON=false;
    private AddCartClickListener addCartClickListener;

    public void setRVOnAddCartClickListener(AddCartClickListener addCartClickListener) {
        this.addCartClickListener = addCartClickListener;
        addCartListenerON=true;
    }

    public interface AddCartClickListener {
        void onAddCartClick(View v, int position);
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

    /**
     * FILTER
     */
    private CustomFilter mFilter;

    public CustomFilter getmFilter() {
        return mFilter;
    }

    public class CustomFilter extends Filter {
        private MH_Adapter_Redimir2 mAdapter;
        private CustomFilter(MH_Adapter_Redimir2 mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            listFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                listFilter.addAll(listPremio);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Premios mWords : listPremio) {
                    if (mWords.getCodigo().toLowerCase().contains(filterPattern)) {
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
