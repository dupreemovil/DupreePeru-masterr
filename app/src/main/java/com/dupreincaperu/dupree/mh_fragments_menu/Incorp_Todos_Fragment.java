package com.dupreincaperu.dupree.mh_fragments_menu;


import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_PagerAdapter_Incorporacion;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.dupreincaperu.dupree.mh_utilities.CustomViewPager_DisableSwipe;

/**
 * A simple {@link Fragment} subclass.
 */
public class Incorp_Todos_Fragment extends Fragment {

    private final String TAG = "Incorp_Todos_Fragment";

    TabLayout tabsIncorp;
    SwipeRefreshLayout swipeIncorp;
    CustomViewPager_DisableSwipe pagerIncorp;
    LinearLayout layoutDotIncorp;

    MH_PagerAdapter_Incorporacion adapterFragIncorp;

    public Incorp_Todos_Fragment() {
        // Required empty public constructor
    }

    private Perfil perfil;
    private int initPage;
    public void loadData(int initPage, Perfil perfil){
        this.initPage=initPage;
        this.perfil=perfil;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_incorp_todos, container, false);

        tabsIncorp = v.findViewById(R.id.tabsIncorp);

        swipeIncorp = v.findViewById(R.id.swipeIncorp);
        swipeIncorp.setOnRefreshListener(mOnRefreshListener);
        swipeIncorp.setEnabled(false);

        pagerIncorp = swipeIncorp.findViewById(R.id.pagerIncorp);
        pagerIncorp.setPagingEnabled(false);// esto lo permite la libreria externa

        //adapterFragIncorp = new MH_PagerAdapter_Incorporacion(getFragmentManager(), perfil);
        adapterFragIncorp = new MH_PagerAdapter_Incorporacion(getChildFragmentManager(), perfil);
        pagerIncorp.setAdapter(adapterFragIncorp);
        pagerIncorp.addOnPageChangeListener(mOnPageChangeListener);
        layoutDotIncorp = swipeIncorp.findViewById(R.id.layoutDotIncorp);

        tabsIncorp.setupWithViewPager(pagerIncorp);
        createTabIcons();

        pagerIncorp.setCurrentItem(initPage);


        //dashabilitar pestanas del tab numero 2 (iscripcion, solo se llega desde la lista)

        LinearLayout tabStrip = ((LinearLayout)tabsIncorp.getChildAt(0));
        //tabStrip.setEnabled(false);
        //for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(2).setClickable(false);
        //}

        return v;
    }

    private void createTabIcons() {
        //////////
        TextView tab1 = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_item, null);
        tab1.setText(getResources().getString(R.string.preinscripcion));

        Drawable mDrawable1 = getResources().getDrawable(R.drawable.ic_person_outline_white_24dp);
        mDrawable1.setColorFilter(new
                PorterDuffColorFilter(getResources().getColor(R.color.azulDupree), PorterDuff.Mode.MULTIPLY));

        tab1.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable1, null, null);
        tabsIncorp.getTabAt(0).setCustomView(tab1);

        //////////
        TextView tab2 = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_item, null);
        tab2.setText(getResources().getString(R.string.listado));

        Drawable mDrawable2 = getResources().getDrawable(R.drawable.ic_people_white_24dp);
        mDrawable2.setColorFilter(new
                PorterDuffColorFilter(getResources().getColor(R.color.azulDupree), PorterDuff.Mode.MULTIPLY));

        tab2.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable2, null, null);
        tabsIncorp.getTabAt(1).setCustomView(tab2);

        //////////
        TextView tab3 = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_item, null);
        tab3.setText(getResources().getString(R.string.inscripcion));
        tab3.setTextColor(getResources().getColor(R.color.gray_6));

        Drawable mDrawable3 = getResources().getDrawable(R.drawable.ic_person_add_white_24dp);
        mDrawable3.setColorFilter(new
                PorterDuffColorFilter(getResources().getColor(R.color.gray_6), PorterDuff.Mode.MULTIPLY));

        tab3.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable3, null, null);
        tabsIncorp.getTabAt(2).setCustomView(tab3);
    }

    /**
     * Eventos SwipeRefreshLayout
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener
            = new SwipeRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh() {

        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.i(TAG,"onPageSelected Page: "+position);
            switch (position){
                case MH_PagerAdapter_Incorporacion.PAGE_PREINSCRIPCION:
                    //setSelectedItem(R.id.navigation_home);
                    break;
                case MH_PagerAdapter_Incorporacion.PAGE_LIST_PRE:
                    //setSelectedItem(R.id.navigation_asesora);
                    if(update){
                        update=false;
                        adapterFragIncorp.getIncorp_listPre_fragment().updateList();
                    }
                    break;
                case MH_PagerAdapter_Incorporacion.PAGE_INCRIPCION:
                    //En este punto, se coloca los datos
                    adapterFragIncorp.getInscripcionFragment().loadData(nombre!=null ? nombre : "",cedula!=null ? cedula :"", modeEdit);
                    //setSelectedItem(R.id.navigation_asesora);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void filterCatalogo(String textFilter){
        //adapter_catalogo.getmFilter().filter(textFilter);
        Log.e("newText to: ", textFilter);

        if(pagerIncorp.getCurrentItem()==MH_PagerAdapter_Incorporacion.PAGE_PREINSCRIPCION){
            //fragmentoPedidos.getProductsFragment().filterCatalogo(textFilter);
        } else if(pagerIncorp.getCurrentItem()==MH_PagerAdapter_Incorporacion.PAGE_LIST_PRE){

        } else if(pagerIncorp.getCurrentItem()==MH_PagerAdapter_Incorporacion.PAGE_INCRIPCION){

        }

    }

    public String nombre="", cedula="";
    public boolean modeEdit;
    public void gotoPageInscription(String nombre, String cedula, boolean modeEdit){
        this.nombre = nombre;
        this.cedula = cedula;
        this.modeEdit = modeEdit;
        pagerIncorp.setCurrentItem(MH_PagerAdapter_Incorporacion.PAGE_INCRIPCION);
    }

    boolean update=false;
    public void gotoPagelistPre(){
        update=true;
        pagerIncorp.setCurrentItem(MH_PagerAdapter_Incorporacion.PAGE_LIST_PRE);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy()");
        //unregisterBroadcat();
    }
}
