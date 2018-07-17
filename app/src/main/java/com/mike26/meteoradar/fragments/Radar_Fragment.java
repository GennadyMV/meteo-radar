/*
Copyright © 2018 Alexey Lepskii | nobodypro.ru
All rights reserved.
*/

package com.mike26.meteoradar.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mike26.ads.AdMob_Class;
import com.mike26.meteoradar.Home_Activity;
import com.mike26.meteoradar.R;
import com.mike26.meteoradar.radar.Radar;
import com.ortiz.touch.TouchImageView;

public class Radar_Fragment extends Fragment {

    private Home_Activity        mHomeActivity;
    private View                 mView;

    private TouchImageView       mRadarImageView;
    private ImageView            mWindImageView;
    private ImageView            mTimeCreateRadarImageView;
    private ProgressBar          mProgressBar;

    private Radar                mRadar;
    private SharedPreferences    mSharedPreferences;
    private String               mRadarNameUrl;
    private String               mRadarName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_radar, container, false);

        mHomeActivity = (Home_Activity) getActivity();
        Toolbar toolbar = (Toolbar) mHomeActivity.findViewById(R.id.toolbar);

        /*****************************Рекламный блок******************************/
        RelativeLayout layout = (RelativeLayout)mView.findViewById(R.id.radarView);
        AdMob_Class mAds = new AdMob_Class(mView.getContext(), layout);
        /*************************************************************************/

        initImageView();
        initRadar();
        loadRadar();

        if (mRadarName != null & !mRadarName.equals(""))
            toolbar.setTitle(mRadarName + " - " + mHomeActivity.getString(R.string.app_name));
        else
            toolbar.setTitle(mHomeActivity.getString(R.string.app_name));

        setHasOptionsMenu(true);

        return mView;
    }

    private void initImageView(){
        mRadarImageView           = (TouchImageView) mView.findViewById(R.id.Radar_image);//радар
        mWindImageView            = (ImageView) mView.findViewById(R.id.Wind_Image);//ветер
        mTimeCreateRadarImageView = (ImageView) mView.findViewById(R.id.Time_Create_Radar_Image);//время получения снимка
        mProgressBar              = (ProgressBar)mView.findViewById(R.id.progressBar1);

        TypedValue outValue = new TypedValue();
        getResources().getValue(R.dimen.zoom, outValue, true);
        float zoom = outValue.getFloat();

        mRadarImageView.setZoom(zoom);
        mRadarImageView.setMinZoom(zoom);
    }

    private void initRadar(){
        Bundle args = getArguments();
        if (args != null) {
            mRadarNameUrl = getArguments().getString("RadarNameUrl");
            mRadarName = getArguments().getString("RadarName");
        } else {
            mRadarNameUrl = "UVKMinvody";
            mRadarName = "UVK Минеральные Воды";

            Toast.makeText(mHomeActivity, "Внимание! Произошла ошибка!", Toast.LENGTH_LONG).show();
        }

        mRadar = new Radar(mHomeActivity,
                mRadarImageView,
                mWindImageView,
                mTimeCreateRadarImageView,
                mProgressBar,
                mHomeActivity.getString(R.string.msgError));
    }

    private void loadRadar(){
        mRadar.loadRadar(mRadarNameUrl);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.radar_menu, menu);

        mSharedPreferences = mHomeActivity.getPreferences(Context.MODE_PRIVATE);
        if (mSharedPreferences.contains(Radar.APP_PREFERENCES_RD)) {
            String sRadarNameUrl = mSharedPreferences.getString(Radar.APP_PREFERENCES_RD, "");
            MenuItem menuItem = menu.findItem(R.id.setDefaultRadar);
            if(mRadarNameUrl.equals(sRadarNameUrl)){
                menuItem.setEnabled(false);
            } else {
                menuItem.setEnabled(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            //Обработка события если нажали - "По умолчанию"
            case R.id.setDefaultRadar:
                SharedPreferences.Editor preferencesEditor = mSharedPreferences.edit();
                preferencesEditor.putString(Radar.APP_PREFERENCES_RD, mRadarNameUrl);
                preferencesEditor.putString(Radar.APP_PREFERENCES_RDN, mRadarName);
                preferencesEditor.apply();

                item.setEnabled(false);

                Toast.makeText(mHomeActivity,"Теперь радар - " + mRadarName + " сделан по умолчанию!", Toast.LENGTH_SHORT).show();
                break;

            //Обработка события если нажали - "Обновить"
            case R.id.refreshRadar:
                mRadar.loadRadar(mRadarNameUrl);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        setHasOptionsMenu(false);
    }
}
