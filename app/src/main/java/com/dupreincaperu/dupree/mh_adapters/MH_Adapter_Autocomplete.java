package com.dupreincaperu.dupree.mh_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.Barrio;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Marwuin on 8/3/2017.
 */

public class MH_Adapter_Autocomplete extends RecyclerView.Adapter<MH_Adapter_Autocomplete.ViewHolder> {

    private List<Barrio> barrioList;
    private List<Barrio> filterBarrio;
    private Context mContext;
    ImageLoader img;

    private static boolean clickListenerON=false;
    private static ItemsClickListener ItemsClickListener;

    private int posSelected=-1;

    private CustomFilter mFilter;
    public MH_Adapter_Autocomplete(List<Barrio> filterBarrio, List<Barrio> barrioList, Context mContext){
        this.filterBarrio = filterBarrio;
        this.barrioList = barrioList;
        this.mContext=mContext;
        mFilter = new CustomFilter(MH_Adapter_Autocomplete.this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view= inflate.inflate(R.layout.adapter_add_barrio, parent, false);
        return new ViewHolder(view);
    }

    public CustomFilter getmFilter() {
        return mFilter;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtvNameBarrio.setText(filterBarrio.get(position).getName_barrio());
    }

    @Override
    public int getItemCount() {
        return (null != filterBarrio ? filterBarrio.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView txtvNameBarrio;
        RadioButton rbBarrio;
        View row;
        public ViewHolder(View v) {
            super(v);
            row=v;
            v.setOnClickListener(this);
            txtvNameBarrio = (TextView) v.findViewById(R.id.txtvNameBarrio);
        }

        @Override
        public void onClick(View view) {
            setPosSelected(getAdapterPosition());
            if (clickListenerON) {
                ItemsClickListener.onClickItem(view, getAdapterPosition());
            }
            notifyDataSetChanged();
        }
    }

    public void setRVOnItemClickListener(ItemsClickListener itemsClickListener) {
        ItemsClickListener = itemsClickListener;
        clickListenerON=true;
    }

    public interface ItemsClickListener {
        void onClickItem(View v, int position);
    }

    public int getPosSelected() {
        return posSelected;
    }

    public void setPosSelected(int posSelected) {
        this.posSelected = posSelected;
    }


    public class CustomFilter extends Filter {
        private MH_Adapter_Autocomplete mAdapter;
        private CustomFilter(MH_Adapter_Autocomplete mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filterBarrio.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filterBarrio.addAll(barrioList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Barrio mWords : barrioList) {
                    if (mWords.getName_barrio().toLowerCase().contains(filterPattern)) {
                        filterBarrio.add(mWords);
                    }
                }
            }
            System.out.println("Count Number " + filterBarrio.size());
            results.values = filterBarrio;
            results.count = filterBarrio.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

}
