package com.dupreincaperu.dupree.mh_adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dupreincaperu.dupree.mh_fragments_main.NewPreinsciptionFragment;
import com.dupreincaperu.dupree.mh_fragments_main.RegisterAsesoraFragment;
import com.dupreincaperu.dupree.mh_fragments_menu.incorporaciones.Incorp_ListPre_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.incorporaciones.InscripcionFragment;
import com.dupreincaperu.dupree.mh_fragments_menu.incorporaciones.InscripcionFragment_NEW;
import com.dupreincaperu.dupree.mh_response_api.Perfil;

/**
 * Created by cloudemotion on 5/8/17.
 */

public class MH_PagerAdapter_Incorporacion extends FragmentStatePagerAdapter {
    private final String TAG="MCHD->MH_PagerAdapter";
    public final int numPages=3;
    public static final int PAGE_PREINSCRIPCION=0;
    public static final int PAGE_LIST_PRE=1;
    public static final int PAGE_INCRIPCION=2;

    private NewPreinsciptionFragment newPreinsciptionFragment;
    private Incorp_ListPre_Fragment incorp_listPre_fragment;
    private InscripcionFragment_NEW inscripcionFragment;


    private Perfil perfil;
    public MH_PagerAdapter_Incorporacion(FragmentManager fm, Perfil perfil) {
        super(fm);
        this.perfil=perfil;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case PAGE_PREINSCRIPCION:
                newPreinsciptionFragment = NewPreinsciptionFragment.newInstance();

                return newPreinsciptionFragment;
            case PAGE_LIST_PRE:
                incorp_listPre_fragment = Incorp_ListPre_Fragment.newInstance();
                incorp_listPre_fragment.loadData(perfil);
                return incorp_listPre_fragment;
            case PAGE_INCRIPCION:
                inscripcionFragment = InscripcionFragment_NEW.newInstance();
                return inscripcionFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return numPages;
    }

    public NewPreinsciptionFragment getNewPreinsciptionFragment() {
        return newPreinsciptionFragment;
    }

    public Incorp_ListPre_Fragment getIncorp_listPre_fragment() {
        return incorp_listPre_fragment;
    }

    public InscripcionFragment_NEW getInscripcionFragment() {
        return inscripcionFragment;
    }
}
