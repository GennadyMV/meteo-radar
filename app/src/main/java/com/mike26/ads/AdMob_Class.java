package com.mike26.ads;

import android.content.Context;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AdMob_Class extends AdListener {
    private static final String ADMOB_ID = "ca-app-pub-7479793863624228/1951419393";//Идентификатор рекламы из AdMob

    private Context mContext;
    private AdView  adView;

    public AdMob_Class(Context context, RelativeLayout layout){
        this.mContext = context;
        showAds(layout);
    }

    public void showAds(RelativeLayout layout){
        adView = new AdView(mContext);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(ADMOB_ID);

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        adView.setAdListener(this);

        layout.addView(adView, adParams);

        adView.loadAd(new AdRequest.Builder().build());
    }

    public void onDestroy() {
        if (adView != null){
            adView.destroy();
            adView = null;
        }
    }

    public void onResume() {
        if(adView != null){
            adView.resume();
        }
    }

    public void onPause() {
        if(adView != null){
            adView.pause();
        }
    }
}
