package com.dupreincaperu.dupree;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_PagerAdapter;
import com.dupreincaperu.dupree.mh_adapters.MH_PagerAdapter_Login;
import com.dupreincaperu.dupree.mh_dialogs.MH_Dialogs_Login;
import com.dupreincaperu.dupree.mh_response_api.Barrio;
import com.dupreincaperu.dupree.mh_response_api.ResponseAuth;
import com.dupreincaperu.dupree.mh_response_api.ResponseGeneric;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG="MCHD->MainActivity";

    ViewPager mViewPager;

    private BottomNavigationView bottomNavigation;

    private MH_PagerAdapter pagerAdapter;
    //Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inflando SwipeRefreshLayout
        SwipeRefreshLayout swipeRecycler = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshL_ActMenu);
        swipeRecycler.setOnRefreshListener(mOnRefreshListener);
        swipeRecycler.setEnabled(false);

        //inflando view pager
        mViewPager = (ViewPager) swipeRecycler.findViewById(R.id.pager);

        //mViewPager.setAdapter(avp);
        pagerAdapter = new MH_PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        //inflando barra inferior
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        localBroadcastReceiver = new LocalBroadcastReceiver();
    }


    /**
     * Selecciona una opcion en el menu inferior
     * @param idMenu
     */
    public void setSelectedItem(int idMenu) {
        if (bottomNavigation.getSelectedItemId()!=idMenu) {
            View view = bottomNavigation.findViewById(idMenu);
            view.performClick();
        }
    }

    private int oldItem=R.id.navigation_home;
    /**
     * Eventos de BottomNavigationView
     * DAR CLIC EN LOS BOTONES
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                case R.id.navigation_asesora:
                case R.id.navigation_atencion:
                case R.id.navigation_catalogos:
                    if(oldItem!=item.getItemId()) {
                        oldItem = item.getItemId();
                        changePage(oldItem);
                    }
                    return true;
                case R.id.navigation_login:
                    //bottomNavigation.getSelectedItemId();
                    //setSelectedItem(R.id.navigation_login);
                    showLoginDialog();
                    return true;
            }
            return false;
        }

    };

    /**
     * Eventos SwipeRefreshLayout
     * ARRASTRAR LAS PAGINAS
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener
            = new SwipeRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh() {

        }
    };

    public ViewPager getmViewPager() {
        return mViewPager;
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }


        //DESLIZA Y CAMBIA BOTON AL TIEMPO
        @Override
        public void onPageSelected(int position) {
            Log.i(TAG,"PAGE_MAIN Page: "+position);
            switch (position){
                case MH_PagerAdapter.PAGE_MAIN:
                    setSelectedItem(R.id.navigation_home);
                    //showMenuCatalogo(false);
                    break;
                case MH_PagerAdapter.PAGE_ASESORA:
                    setSelectedItem(R.id.navigation_asesora);
                    //showMenuCatalogo(false);
                    break;
                case MH_PagerAdapter.PAGE_ATENCION:
                    setSelectedItem(R.id.navigation_atencion);
                    //showMenuCatalogo(false);
                    break;
                case MH_PagerAdapter.PAGE_CATALOGOS:
                    setSelectedItem(R.id.navigation_catalogos);
                    //showMenuCatalogo(true);
                    break;
                case MH_PagerAdapter.PAGE_LOGIN:
                    setSelectedItem(R.id.navigation_login);
                    //showMenuCatalogo(false);
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    //AL DAR CLIC EN EL BOTON SE CAMBIA LA PAGINA
    public void changePage(int r_id){
        if (r_id == R.id.navigation_home) {
            if (mViewPager.getCurrentItem() != MH_PagerAdapter.PAGE_MAIN) {
                mViewPager.setCurrentItem(MH_PagerAdapter.PAGE_MAIN);

            }
            //toolbar.setVisibility(View.VISIBLE);
        } else if (r_id == R.id.navigation_asesora || r_id == R.id.imgVuelveteAsesora) {
            if (mViewPager.getCurrentItem() != MH_PagerAdapter.PAGE_ASESORA) {
                mViewPager.setCurrentItem(MH_PagerAdapter.PAGE_ASESORA);

            }
            //toolbar.setVisibility(View.GONE);
        } else if (r_id == R.id.navigation_atencion || r_id == R.id.imgAtencionCliente) {
            if (mViewPager.getCurrentItem() != MH_PagerAdapter.PAGE_ATENCION) {
                mViewPager.setCurrentItem(MH_PagerAdapter.PAGE_ATENCION);

            }
            //toolbar.setVisibility(View.GONE);
        } else if (r_id == R.id.navigation_catalogos || r_id == R.id.imgCatalogos) {
            if (mViewPager.getCurrentItem() != MH_PagerAdapter.PAGE_CATALOGOS) {
                mViewPager.setCurrentItem(MH_PagerAdapter.PAGE_CATALOGOS);

            }
            //toolbar.setVisibility(View.VISIBLE);
        }
    }

    MH_Dialogs_Login showLogin;
    public void showLoginDialog() {
        showLogin = MH_Dialogs_Login.newInstance();
        showLogin.show(getSupportFragmentManager(), "fragment_edit_name");
    }

    public void registerBroadcat(){
        LocalBroadcastManager.getInstance(this).registerReceiver(
                localBroadcastReceiver,
                new IntentFilter(MH_Dialogs_Login.BROACAST_LOGIN));
    }

    public void unregisterBroadcat(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                localBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcat();
        Log.i(TAG,"onResume()");
        //setSelectedItem(oldItem);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterBroadcat();
    }

    private BroadcastReceiver localBroadcastReceiver;
    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // safety check
            if (intent == null || intent.getAction() == null) {
                return;
            }
            switch (intent.getAction()) {
                case MH_Dialogs_Login.BROACAST_LOGIN:
                    switch (intent.getStringExtra(MH_Dialogs_Login.BROACAST_LOGIN)) {
                        case MH_Dialogs_Login.BROACAST_LOGIN_BTNENTRAR:
                            Log.i(TAG, "BROACAST_LOGIN_ENTRAR");
                            //startActivity(new Intent(MainActivity.this, MenuActivity.class));
                            break;
                        case MH_Dialogs_Login.BROACAST_LOGIN_BTNFORGOT:
                            Log.i(TAG, "BROACAST_LOGIN_FORGOT");
                            showLogin.gotoPage(MH_PagerAdapter_Login.PAGE_FORGOT);
                            break;
                        case MH_Dialogs_Login.BROACAST_LOGIN_EXIT:
                            Log.i(TAG, "BROACAST_LOGIN_EXIT");
                            setSelectedItem(oldItem);
                            break;

                    }
                break;

            }

        }
    }

    /**
     * METODOS DE RESPUESTAS HTTP
     */
    public void successfulAuth(ResponseAuth responseAuth){
        msgToast(responseAuth.getStatus());
        mPreferences.setJSON_TypePerfil(MainActivity.this, responseAuth.getPerfil().get(0));
        mPreferences.setLoggedIn(MainActivity.this, true);

        startActivity(new Intent(MainActivity.this, MenuActivity.class));
        finish();
    }
    public void successfulNotifyForgot(ResponseGeneric responseGeneric){
        msgToast(responseGeneric.getResult());
        showLogin.gotoPage(MH_PagerAdapter_Login.PAGE_CODE);
    }
    public void successfulValidateCode(ResponseGeneric responseGeneric, String codigo){
        msgToast(responseGeneric.getResult());
        showLogin.gotoPage(MH_PagerAdapter_Login.PAGE_PASSWORD);
        mPreferences.setCodeSMS(MainActivity.this, codigo);
    }
    public void successfulNewPwd(ResponseGeneric responseGeneric){
        msgToast(responseGeneric.getResult());
        showLogin.gotoPage(MH_PagerAdapter_Login.PAGE_LOGIN);
    }

    private void msgToast(String msg){
        Log.e("Toast", msg);
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }


}
