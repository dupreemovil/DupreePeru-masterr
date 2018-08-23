package com.dupreincaperu.dupree.mh_adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dupreincaperu.dupree.mh_fragments_main.CatalogosFragment;
import com.dupreincaperu.dupree.mh_fragments_main.ContactFragment;
import com.dupreincaperu.dupree.mh_fragments_main.MainFragment;
import com.dupreincaperu.dupree.mh_fragments_main.RegisterAsesoraFragment;
import com.dupreincaperu.dupree.mh_fragments_main.RegisterAsesoraFragment_NEW;

/**
 * Created by cloudemotion on 5/8/17.
 */

public class MH_PagerAdapter extends FragmentPagerAdapter/*FragmentStatePagerAdapter*/ {
    private final String TAG="MCHD->MH_PagerAdapter";
    public final int numPages=4;
    public static final int PAGE_MAIN=0;
    public static final int PAGE_ASESORA=1;
    public static final int PAGE_ATENCION=2;
    public static final int PAGE_CATALOGOS=3;
    public static final int PAGE_LOGIN=4;

    MainFragment mainFragment;
    RegisterAsesoraFragment_NEW asesoraFragment;
    ContactFragment contactFragment;
    CatalogosFragment catalogosFragment;

    public MH_PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case PAGE_MAIN:
                mainFragment = MainFragment.newInstance();
                return mainFragment;
            case PAGE_ASESORA:
                asesoraFragment =  RegisterAsesoraFragment_NEW.newInstance();
                return asesoraFragment;
            case PAGE_ATENCION:
                contactFragment = ContactFragment.newInstance();
                return contactFragment;
            case PAGE_CATALOGOS:
                catalogosFragment = CatalogosFragment.newInstance();
                return catalogosFragment;
            /*case PAGE_LOGIN:
                return MainFragment.newInstance();*/
        }
        return null;
    }

    @Override
    public int getCount() {
        return numPages;
    }


    public MainFragment getMainFragment() {
        return mainFragment;
    }

    public RegisterAsesoraFragment_NEW getAsesoraFragment() {
        return asesoraFragment;
    }

    public ContactFragment getContactFragment() {
        return contactFragment;
    }

    public CatalogosFragment getCatalogosFragment() {
        return catalogosFragment;
    }
}
