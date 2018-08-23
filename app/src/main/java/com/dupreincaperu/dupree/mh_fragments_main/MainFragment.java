package com.dupreincaperu.dupree.mh_fragments_main;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.dupreincaperu.dupree.MainActivity;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_AdapterSlider;
import com.dupreincaperu.dupree.mh_response_api.ImgBanner;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;
import com.dupreincaperu.dupree.mh_utilities.PinchZoomImageView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private String TAG="MainFragment";


    ImageLoader img;
    public MainFragment() {
        // Required empty public constructor
    }

    Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        AppBarLayout AppBarL_FragMain = (AppBarLayout) v.findViewById(R.id.AppBarL_FragMain);
        toolbar = (Toolbar) AppBarL_FragMain.findViewById(R.id.Toolbar_Act);
        toolbar.setNavigationIcon(null);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        SliderLayout slider = (SliderLayout) v.findViewById(R.id.mh_slider);

        SlidePresentacion(slider);

        ImageView imgVuelveteAsesora, imgSolicitaAsesora, imgCatalogos, imgLogin;
        imgVuelveteAsesora = (ImageView) v.findViewById(R.id.imgVuelveteAsesora);
        imgSolicitaAsesora = (ImageView) v.findViewById(R.id.imgAtencionCliente);

        imgCatalogos = (ImageView) v.findViewById(R.id.imgCatalogos);
        img = ImageLoader.getInstance();
        img.init(PinchZoomImageView.configurarImageLoader(getActivity()));
        img.displayImage(mPreferences.getImageCatalogo(getActivity()), imgCatalogos);


        imgLogin = (ImageView) v.findViewById(R.id.imgLogin);

        imgVuelveteAsesora.setOnClickListener(clickListener);
        imgSolicitaAsesora.setOnClickListener(clickListener);
        imgCatalogos.setOnClickListener(clickListener);
        imgLogin.setOnClickListener(clickListener);

        Log.e(TAG,"Token: "+FirebaseInstanceId.getInstance().getToken());

        return v;
    }

    public static MainFragment newInstance() {

        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    private View.OnClickListener clickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgVuelveteAsesora:
                case R.id.imgAtencionCliente:
                case R.id.imgCatalogos:
                    ((MainActivity) getActivity()).changePage(view.getId());
                    break;
                case R.id.imgLogin:
                    ((MainActivity) getActivity()).setSelectedItem(R.id.navigation_login);
                    //((MainActivity) getActivity()).showLoginDialog();
                    break;
            }

        }
    };

    public void SlidePresentacion(SliderLayout slider) {
        String objetcImge = mPreferences.getJSONImageBanner(getActivity());
        ImgBanner.Resolution list_image = new Gson().fromJson(objetcImge, ImgBanner.Resolution.class);



        HashMap<String, String> file_maps = new HashMap<String, String>();
        file_maps.put("1", list_image.getImg1());
        file_maps.put("2", list_image.getImg2());
        file_maps.put("3", list_image.getImg3());


        MH_AdapterSlider SliderView=null;
        for (String name : file_maps.keySet()) {

            SliderView = new MH_AdapterSlider(getContext());
            // initialize a SliderLayout
            SliderView
                    //.description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            //.setOnSliderClickListener(this);

            SliderView.bundle(new Bundle());
            SliderView.getBundle()
                    .putString("extra", name);

            slider.addSlider(SliderView);
        }


        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        slider.addOnPageChangeListener(SliderView.getListenerSlider());
    }

}
