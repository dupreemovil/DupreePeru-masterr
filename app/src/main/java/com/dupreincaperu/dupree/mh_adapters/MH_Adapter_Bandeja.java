package com.dupreincaperu.dupree.mh_adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.ResponseBandeja;
import com.dupreincaperu.dupree.mh_utilities.ProcessImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import me.biubiubiu.justifytext.library.JustifyTextView;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_Adapter_Bandeja extends RecyclerView.Adapter<MH_Adapter_Bandeja.ViewHolder> {
    private List<ResponseBandeja.ResultBandeja> msg;
    private String TAG = "MH_Adapter_FilesDetail";


    private Context mContext;
    private ImageLoader img;

    private int rowSelected=-1;
    private int numItemSelected=0;
    //DisplayImageOptions options;

    public MH_Adapter_Bandeja(List<ResponseBandeja.ResultBandeja> msg, Context mContext){
        this.msg=msg;
        this.mContext=mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view = inflate.inflate(R.layout.adapter_bandeja_entrada, parent, false);
        return new ViewHolder(view);
    }

    private Bitmap mBitmap;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        img = ImageLoader.getInstance();
        img.init(ProcessImage.configurarImageLoader(mContext));
        ///Iimagen del album

        holder.textBody.setText(msg.get(position).getMensaje().concat("\n"));

        holder.txtvDate.setText(msg.get(position).getFecha());

        /*holder.checkImageFile.setVisibility(msg.get(position).getItemSelected() ? View.VISIBLE : View.INVISIBLE);
*/

        if(msg.get(position).getItemSelected()) {
            holder.imagProfile.setImageResource(R.drawable.img_check2);
        } else {
            holder.imagProfile.setImageResource(R.drawable.ic_flor180x180_no_transp);
            //holder.imagProfile.setImageResource(position%2!=0 ? R.drawable.ic_flor180x180_no_transp : R.drawable.ic_flor_pick_180x180_no_transp);
        }

        holder.ctxTop.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_background_chat_receive_top_blue));
        holder.ctxBottom.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_background_chat_receive_bottom_blue));
        //holder.ctxTop.setBackground(position%2!=0 ? mContext.getResources().getDrawable(R.drawable.rounded_background_chat_receive_top_blue) : mContext.getResources().getDrawable(R.drawable.rounded_background_chat_receive_top_acent));
        //holder.ctxBottom.setBackground(position%2!=0 ? mContext.getResources().getDrawable(R.drawable.rounded_background_chat_receive_bottom_blue) : mContext.getResources().getDrawable(R.drawable.rounded_background_chat_receive_bottom_acent));

        if(msg.get(position).getImagen().isEmpty())
            holder.imgUrl.setVisibility(View.GONE);
        else {
            holder.imgUrl.setVisibility(View.VISIBLE);
            img.displayImage(msg.get(position).getImagen(), holder.imgUrl, new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build());
        }
    }

    @Override
    public int getItemCount() {
        return (null != msg ? msg.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener{
        private ImageView imagProfile, checkImageFile;
        private RelativeLayout ctxFleDetail;
        private TextView txtvDate, textBody;
        private JustifyTextView justifyTextView;

        private ImageView imgUrl;
        LinearLayout ctxTop, ctxBottom;
        //protected TextView tvFile;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            //v.setOnCreateContextMenuListener(this);
            imagProfile = (ImageView) v.findViewById(R.id.profile_image);
            txtvDate = (TextView) v.findViewById(R.id.txtvDate);
            //checkImageFile = (ImageView) v.findViewById(R.id.checkImageFile);
            //ctxFleDetail = (RelativeLayout) v.findViewById(R.id.ctxFleDetail);
            textBody = (TextView) v.findViewById(R.id.textBody);

            ctxTop = (LinearLayout) v.findViewById(R.id.ctxTop);
            ctxBottom = (LinearLayout) v.findViewById(R.id.ctxBottom);

            imgUrl = (ImageView) v.findViewById(R.id.imgUrl);

            imgUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(numItemSelected > 0) {
                        if (clickListenerON) {
                            rowSelected=getAdapterPosition();
                            if(numItemSelected>0)
                                calculateItemsSelected(rowSelected);
                            ItemsClickListener.onClickItem(view, rowSelected);
                            notifyItemChanged(rowSelected);
                        }
                    } else {
                        if (imageListenerON) {
                            imageClickListener.onClickImage(view, getAdapterPosition());
                        }
                    }
                }
            });

            imgUrl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (longClickListenerON) {
                        rowSelected=getAdapterPosition();
                        calculateItemsSelected(rowSelected);

                        ItemsLongClickListener.onLongClick(view, rowSelected);
                        notifyItemChanged(rowSelected);
                    }
                    return true;
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (clickListenerON) {
                rowSelected=getAdapterPosition();
                if(numItemSelected>0)
                    calculateItemsSelected(rowSelected);
                ItemsClickListener.onClickItem(view, rowSelected);
                notifyItemChanged(rowSelected);
            }
            //notifyDataSetChanged();
        }

        @Override
        public boolean onLongClick(View v) {
            if (longClickListenerON) {
                rowSelected=getAdapterPosition();
                calculateItemsSelected(rowSelected);

                ItemsLongClickListener.onLongClick(v, rowSelected);
                notifyItemChanged(rowSelected);
            }
            return true;
        }
    }

    public void calculateItemsSelected(int rowSelected){
        if(msg.get(rowSelected).getItemSelected())
            numItemSelected=numItemSelected-1;
        else
            numItemSelected=numItemSelected+1;

        Log.e("calculateItemsSelected",String.valueOf(numItemSelected));

        //numItemSelected = files.get(rowSelected).getItemSelected() ? numItemSelected-- : numItemSelected++;
        msg.get(rowSelected).setItemSelected(!msg.get(rowSelected).getItemSelected());
    }

    public void clearItemSelected(){
        numItemSelected=0;
        for(int i=0;i<msg.size();i++){
            msg.get(i).setItemSelected(false);
        }
        notifyDataSetChanged();
    }

    private static boolean clickListenerON=false;
    private static ItemsClickListener ItemsClickListener;
    public void setRVOnItemClickListener(ItemsClickListener itemsClickListener) {
        ItemsClickListener = itemsClickListener;
        clickListenerON=true;
    }

    public interface ItemsClickListener {
        void onClickItem(View v, int position);
    }

    private static boolean longClickListenerON=false;
    private static ItemsLongClickListener ItemsLongClickListener;
    public void setRVOnItemLongClickListener(ItemsLongClickListener itemsLongClickListener) {
        ItemsLongClickListener = itemsLongClickListener;
        longClickListenerON=true;
    }
    public interface ItemsLongClickListener {
        void onLongClick(View v, int position);
    }


    private static boolean imageListenerON=false;
    private static ImageClickListener imageClickListener;
    public void setRVOnImageClickListener(ImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
        imageListenerON=true;
    }

    public interface ImageClickListener {
        void onClickImage(View v, int position);
    }



    public int getRowSelected() {
        return rowSelected;
    }

    public void setRowSelected(int posSelected) {
        this.rowSelected = posSelected;
    }

    public int getNumItemSelected() {
        return numItemSelected;
    }
}
