package com.dupreincaperu.dupree.mh_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.Catalogo;
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

public class MH_Adapter_Cart extends RecyclerView.Adapter<MH_Adapter_Cart.ViewHolder> {
    private List<Catalogo> listFilter;

    private Context mContext;

    ImageLoader img;
    private int posSelected=-1;

    public MH_Adapter_Cart(List<Catalogo> listFilter, Context mContext){
        this.listFilter = listFilter;
        this.mContext=mContext;

        //para filtrar
        //mFilter = new CustomFilter(MH_Adapter_Cart.this);
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
        View view= inflate.inflate(R.layout.adapter_catalogo, parent, false);
        return new ViewHolder(view);
    }

    Boolean blockEventsAutomatic=false;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvPage.setText("Pág.".concat(listFilter.get(position).getPage()));
        holder.tvCode.setText("CÓDIGO: ".concat(listFilter.get(position).getId()));
        holder.tvDescription.setText(listFilter.get(position).getName());

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        holder.tvValor.setText(" S/. ".concat(formatter.format(Float.parseFloat(listFilter.get(position).getValor()))));

        holder.tvCantidad.setText(String.valueOf(listFilter.get(position).getCantidad()));

        //validar si ya esta (o no) en el carrito.
        //holder.ctn_IncDecCant.setVisibility(listFilter.get(position).isInTheCart() ? View.VISIBLE : View.GONE);
        //holder.ctn_AddCart.setVisibility(listFilter.get(position).isInTheCart() ? View.GONE : View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return (null != listFilter ? listFilter.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private TextView tvPage, tvCode, tvDescription, tvValor, tvCantidad, tvAddCart;
        private ImageButton imgB_Decrease, imgB_Increase, imgB_AddCart;
        private LinearLayout ctn_IncDecCant, ctn_AddCart;
        private CircleImageView image;


        protected TextView tvNearbyUserUsername;
        public ViewHolder(View v) {
            super(v);
            //v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);
            //imagen = (CircleImageView) v.findViewById(R.id.imagen);

            image = (CircleImageView) v.findViewById(R.id.image);

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
            //imgB_Increase.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.imgB_Decrease){
                if(decreaseListenerON) {
                    decreaseClickListener.onDecreaseClick(view, getAdapterPosition());
                }
            } else if(view.getId()==R.id.imgB_Increase){
                if(increaseListenerON) {
                    increaseClickListener.onIncreaseClick(view, getAdapterPosition());
                }
            } else if((view.getId()==R.id.imgB_AddCart) || (view.getId()==R.id.tvAddCart)){
                if (removeCartListenerON) {
                    removeCartClickListener.onRemoveCartClick(view, getAdapterPosition());
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
    private boolean removeCartListenerON=false;
    private RemoveCartClickListener removeCartClickListener;

    public void setRVOnRemoveCartClickListener(RemoveCartClickListener removeCartClickListener) {
        this.removeCartClickListener = removeCartClickListener;
        removeCartListenerON=true;
    }

    public interface RemoveCartClickListener {
        void onRemoveCartClick(View v, int position);
    }

    /**
     * pos selected
     */
    public void setPosSelected(int posSelected) {
        this.posSelected = posSelected;
    }


    /**
     * FILTER
     */
    /*
    private CustomFilter mFilter;

    public CustomFilter getmFilter() {
        return mFilter;
    }

    public class CustomFilter extends Filter {
        private MH_Adapter_Cart mAdapter;
        private CustomFilter(MH_Adapter_Cart mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            listFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                listFilter.addAll(listCatalogo);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Productos mWords : listCatalogo) {
                    if (mWords.getId().toLowerCase().contains(filterPattern)) {
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
    */


}
