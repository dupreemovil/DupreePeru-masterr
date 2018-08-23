package com.dupreincaperu.dupree.mh_adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dupreincaperu.dupree.mh_fragments_menu.pedidos.CartFragment;
import com.dupreincaperu.dupree.mh_fragments_menu.pedidos.OffersFragment;

/**
 * Created by cloudemotion on 5/8/17.
 */

public class MH_PagerAdapter_Pedidos extends FragmentStatePagerAdapter {
    private final String TAG="MCHD->MH_PagerAdapter";
    public final int numPages=2;
    //public static final int PAGE_PRODUCTS=2;//se quito la lista de todos los productos
    public static final int PAGE_OFFERS=1;
    public static final int PAGE_CART=0;

    //private ProductsFragment productsFragment;
    private CartFragment cartFragment;
    private OffersFragment offersFragment;


    public MH_PagerAdapter_Pedidos(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case PAGE_CART:
                cartFragment = CartFragment.newInstance();
                return cartFragment;
            case PAGE_OFFERS:
                offersFragment = OffersFragment.newInstance();
                return offersFragment;
            /*case PAGE_PRODUCTS:
                productsFragment = ProductsFragment.newInstance();
                return productsFragment;*/
        }
        return null;
    }

    @Override
    public int getCount() {
        return numPages;
    }

    /*public ProductsFragment getProductsFragment() {
        return productsFragment;
    }*/

    public CartFragment getCartFragment() {
        return cartFragment;
    }

    public OffersFragment getOffersFragment() {
        return offersFragment;
    }
}
