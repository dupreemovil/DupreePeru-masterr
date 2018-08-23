package com.dupreincaperu.dupree.mh_fragments_menu.panel_asesoras;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Faltantes;
import com.dupreincaperu.dupree.mh_response_api.Faltante;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FaltantesAsesoraFragment extends Fragment {


    public FaltantesAsesoraFragment() {
        // Required empty public constructor
    }

    public static FaltantesAsesoraFragment newInstance() {

        Bundle args = new Bundle();

        FaltantesAsesoraFragment fragment = new FaltantesAsesoraFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<Faltante> faltanteList;
    private MH_Adapter_Faltantes adapter_faltantes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_faltantes_asesora, container, false);

        RecyclerView rcvFaltantesAsesora = v.findViewById(R.id.rcvFaltantesAsesora);
        rcvFaltantesAsesora.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvFaltantesAsesora.setHasFixedSize(true);

        faltanteList = new ArrayList<>();
        adapter_faltantes = new MH_Adapter_Faltantes(faltanteList, faltanteList, getActivity());
        rcvFaltantesAsesora.setAdapter(adapter_faltantes);


        return v;
    }

    public void setData(List<Faltante> faltanteList){
        this.faltanteList.clear();
        this.faltanteList.addAll(faltanteList);
        adapter_faltantes.notifyDataSetChanged();
    }

}
