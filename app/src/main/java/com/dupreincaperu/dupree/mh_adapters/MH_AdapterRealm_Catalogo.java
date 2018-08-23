package com.dupreincaperu.dupree.mh_adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.Catalogo;

import java.text.NumberFormat;
import java.util.Locale;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_AdapterRealm_Catalogo extends RealmRecyclerViewAdapter<Catalogo, MH_AdapterRealm_Catalogo.ViewHolder> {

    private int posSelected=-1;


    public MH_AdapterRealm_Catalogo(@Nullable OrderedRealmCollection<Catalogo> catalogos) {
        super(catalogos, true);
        setHasStableIds(true);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view= inflate.inflate(R.layout.adapter_catalogo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Catalogo obj = getItem(position);
        holder.data = obj;

        holder.tvPage.setText("Pág.".concat(obj.getPage()));
        holder.tvCode.setText("CÓDIGO: ".concat(obj.getId()));
        holder.tvDescription.setText(obj.getName());

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        holder.tvValor.setText("S/. ".concat(formatter.format(Float.parseFloat(obj.getValor()))));

        holder.tvCantidad.setText(String.valueOf(obj.getCantidad()));

        //validar si ya esta (o no) en el carrito.
        //holder.ctn_IncDecCant.setVisibility(listFilter.get(position).isInTheCart() ? View.VISIBLE : View.GONE);
        //holder.ctn_AddCart.setVisibility(listFilter.get(position).isInTheCart() ? View.GONE : View.VISIBLE);

    }




    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private TextView tvPage, tvCode, tvDescription, tvValor, tvCantidad, tvAddCart;
        private ImageButton imgB_Decrease, imgB_Increase, imgB_AddCart;
        private LinearLayout ctn_IncDecCant, ctn_AddCart;


        protected TextView tvNearbyUserUsername;
        public Catalogo data;

        public ViewHolder(View v) {
            super(v);
            //v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);
            //imagen = (CircleImageView) v.findViewById(R.id.imagen);

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
            //imgB_Increase.setOnClickListener(this);
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
            }
            //notifyDataSetChanged();
        }

    }

    /**
     * click
     */
    private boolean clickListenerON=false;
    private MH_Adapter_Catalogo.ItemsClickListener itemsClickListener;

    public void setRVOnItemClickListener(MH_Adapter_Catalogo.ItemsClickListener ItemsClickListener) {
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
}
