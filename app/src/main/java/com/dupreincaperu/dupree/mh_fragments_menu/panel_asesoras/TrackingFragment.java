package com.dupreincaperu.dupree.mh_fragments_menu.panel_asesoras;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Tracking;
import com.dupreincaperu.dupree.mh_response_api.Tracking;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackingFragment extends Fragment {


    public TrackingFragment() {
        // Required empty public constructor
    }

    public static TrackingFragment newInstance() {
        Bundle args = new Bundle();

        TrackingFragment fragment = new TrackingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<Tracking> trackingList;
    private MH_Adapter_Tracking mhAdapterTracking;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tracking, container, false);

        RecyclerView rcvTracking = v.findViewById(R.id.rcvTracking);
        rcvTracking.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvTracking.setHasFixedSize(true);

        trackingList = new ArrayList<>();
        mhAdapterTracking = new MH_Adapter_Tracking(trackingList, getActivity());
        rcvTracking.setAdapter(mhAdapterTracking);

        return v;
    }

    public void setData(List<Tracking> trackingList){
        this.trackingList.clear();
        this.trackingList.addAll(trackingList);
        mhAdapterTracking.notifyDataSetChanged();
    }

}
