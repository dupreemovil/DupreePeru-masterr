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

public class MH_Adapter_Offers extends RecyclerView.Adapter<MH_Adapter_Offers.ViewHolder> {
    private List<Oferta> listFilter;
    private String TAG = "MH_Adapter_Offers";

    private Context mContext;

    private boolean enable=true;
    private ImageLoader img;
    private int posSelected=-1;

    private boolean change=false;

    private List<String> idEditable;//para saber si hay elementos que modificar

    public MH_Adapter_Offers(List<Oferta> listFilter, Context mContext){
        this.listFilter = listFilter;
        this.mContext=mContext;

        idEditable=new ArrayList<>();

        //para filtrar
        //mFilter = new CustomFilter(MH_Adapter_Catalogo.this);

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
        View view= inflate.inflate(R.layout.adapter_offers, parent, false);
        return new ViewHolder(view);
    }

    Boolean blockEventsAutomatic=false;
    int numEditable=0;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e(TAG,"position: "+String.valueOf(position));
        //Log.e(TAG, new Gson().toJson(listFilter.get(position)));

        //quitar la imagen que manda backend en caso de no disponibles
        if(listFilter.get(position).getUrl_img().contains("")) {
            img = ImageLoader.getInstance();
            img.init(configurarImageLoader());
            img.displayImage(listFilter.get(position).getUrl_img(), holder.imagen);
        }

        holder.tvPage.setText("Pág.".concat(listFilter.get(position).getPage()));
        holder.tvCode.setText("Código: ".concat(listFilter.get(position).getId()));
        holder.tvDescription.setText(listFilter.get(position).getName());

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        holder.tvValor.setText("S/. ".concat(formatter.format(Float.parseFloat(listFilter.get(position).getValor()))));
        holder.tvValor.setPaintFlags(holder.tvValor.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        NumberFormat formatter2 = NumberFormat.getInstance(Locale.US);
        holder.tvValorDescuento.setText("S/. ".concat(formatter2.format(Float.parseFloat(listFilter.get(position).getValor_descuento()))));

        holder.tvCantidad.setText(String.valueOf(listFilter.get(position).getCantidad()));

        //validar si ya esta (o no) en el carrito.
        holder.ctn_IncDecCant.setVisibility(listFilter.get(position).getCantidad()>0 ? View.VISIBLE : View.GONE);
        holder.ctn_AddCart.setVisibility(listFilter.get(position).getCantidad()>0 ? View.GONE : View.VISIBLE);

        //para deshabilitar
        //holder.imgB_Decrease.setEnabled(isEnable());
        //holder.imgB_Increase.setEnabled(isEnable());
        //holder.imgB_AddCart.setEnabled(isEnable());
        //holder.tvAddCart.setEnabled(isEnable());
        holder.imgB_Decrease.setColorFilter(isEnable() ? mContext.getResources().getColor(R.color.azulDupree) :mContext.getResources().getColor(R.color.gray_5),android.graphics.PorterDuff.Mode.MULTIPLY);
        holder.imgB_Increase.setColorFilter(isEnable() ? mContext.getResources().getColor(R.color.azulDupree) :mContext.getResources().getColor(R.color.gray_5),android.graphics.PorterDuff.Mode.MULTIPLY);
        holder.imgB_AddCart.setColorFilter(isEnable() ? mContext.getResources().getColor(R.color.azulDupree) :mContext.getResources().getColor(R.color.gray_5),android.graphics.PorterDuff.Mode.MULTIPLY);
        holder.tvAddCart.setTextColor(isEnable() ? mContext.getResources().getColor(R.color.azulDupree) : mContext.getResources().getColor(R.color.gray_5));
        //colorear segun el status del pedido
        //holder.imagen.setBorderColor(mContext.getResources().getColor(R.color.azulDupree));
        //Log.e(TAG,"Catidad: "+String.valueOf(listFilter.get(position).getCantidad())+", CantServer: "+listFilter.get(position).getCantidadServer());

        if(listFilter.get(position).getCantidad() == listFilter.get(position).getCantidadServer()) {
            //SON IGUALES, NO HAY CAMBIO
            if(listFilter.get(position).getCantidad()>=1) {// no esta en el server
                //holder.imagen.setImageResource(R.drawable.ic_flor180x180);//no hay cambios

                holder.tvStatus.setText("");
                holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.azulDupree));
            }else{// si esta en el server pero no hay cambios
                //holder.imagen.setImageResource(R.drawable.ic_flor180x180);//no hay cambior
                holder.tvStatus.setText("");
                holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.azulDupree));
            }
            //holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.red_1));
            removeEditable(listFilter.get(position).getId());
        }
        else if (listFilter.get(position).getCantidad() != listFilter.get(position).getCantidadServer())
        {
            if (listFilter.get(position).getCantidad() == 0 && listFilter.get(position).getCantidadServer() >= 1) {
                //holder.imagen.setImageResource(R.drawable.ic_flor_red_180x180);//eliminar
                holder.tvStatus.setText(R.string.eliminar);
                holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.red_1));
            } else if (listFilter.get(position).getCantidad() >= 1 && listFilter.get(position).getCantidadServer() == 0) {
                //holder.imagen.setImageResource(R.drawable.ic_flor_pick_180x180);//agregar
                holder.tvStatus.setText(R.string.aniadir);
                holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            } else {
                //holder.imagen.setImageResource(R.drawable.ic_flor_orange_180x180);//editar
                holder.tvStatus.setText(R.string.editar);
                holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.orange_600));
            }
            addEditable(listFilter.get(position).getId());
        }

        Log.e(TAG, "num. Items editable= "+String.valueOf(idEditable.size()));

        if(numEditable==0 && idEditable.size()==1){
            Log.e(TAG, "num. Items editable = SI EDITAR VARIABLE="+addEditableListenerON);
            if(addEditableListenerON) {
                Log.e(TAG, "num. Items editable = SI EVENTS");
                addEditableClickListener.onAddEditableClick(true);
            }
        } else if(numEditable==1 && idEditable.size()==0){
            Log.e(TAG, "num. Items editable = NO EDITAR");
            if(addEditableListenerON)
                addEditableClickListener.onAddEditableClick(false);
        }

        numEditable = idEditable.size();
    }

    private void addEditable(String id){
        for(int i=0; i<idEditable.size();i++){
            if(idEditable.get(i).equals(id)){
                return;
            }
        }
        idEditable.add(id);//significa que este id se debe modificar
    }

    private void removeEditable(String id){
        for(int i=0; i<idEditable.size();i++){
            if(idEditable.get(i).equals(id)){
                idEditable.remove(i);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != listFilter ? listFilter.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private ImageView imagen;
        private TextView tvPage, tvCode, tvDescription, tvValor, tvValorDescuento, tvCantidad, tvAddCart, tvStatus;
        private ImageButton imgB_Decrease, imgB_Increase, imgB_AddCart;
        private LinearLayout ctn_IncDecCant, ctn_AddCart;


        protected TextView tvNearbyUserUsername;
        public ViewHolder(View v) {
            super(v);
            //v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);
            //imagen = (CircleImageView) v.findViewById(R.id.imagen);

            //swtRedimir = (SwitchCompat) v.findViewById(R.id.swtRedimir);
            imagen = (ImageView) v.findViewById(R.id.imagen);

            tvStatus = (TextView) v.findViewById(R.id.tvStatus);
            //tvStatus.setVisibility(View.INVISIBLE);
            tvPage = (TextView) v.findViewById(R.id.tvPage);
            tvCode = (TextView) v.findViewById(R.id.tvCode);
            tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            tvValorDescuento = (TextView) v.findViewById(R.id.tvValorDescuento);
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

            if(!isEnable()) {
                msgToast(mContext.getString(R.string.pedido_no_puede_modificarse));
                return;
            }

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
     * event isEditable click
     */
    private boolean addEditableListenerON=false;
    private AddEditableClickListener addEditableClickListener;

    public void setRVOnAddEditableClickListener(AddEditableClickListener addEditableClickListener) {
        this.addEditableClickListener = addEditableClickListener;
        addEditableListenerON=true;
    }

    public interface AddEditableClickListener {
        void onAddEditableClick(boolean isEditable);
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
    /*
    private CustomFilter mFilter;

    public CustomFilter getmFilter() {
        return mFilter;
    }

    public class CustomFilter extends Filter {
        private MH_Adapter_Catalogo mAdapter;
        private CustomFilter(MH_Adapter_Catalogo mAdapter) {
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
                for (final Catalogo mWords : listCatalogo) {
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

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    private void msgToast(String msg){
        Log.e("Toast", msg);
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    public void clearEditable(){
        numEditable=0;
        idEditable.clear();
    }
}
