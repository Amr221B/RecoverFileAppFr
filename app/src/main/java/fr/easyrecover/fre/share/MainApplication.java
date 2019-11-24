package fr.easyrecover.fre.share;

import android.support.multidex.MultiDexApplication;

import fr.easyrecover.fre.Constant;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

public class MainApplication extends MultiDexApplication {

    public PublisherInterstitialAd mInterstitialAd;
    public PublisherAdRequest ins_adRequest;

    private static final String TAG = "Application";

    private static MainApplication appInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        appInstance = this;
        LoadAds();

    }

    public void LoadAds() {

        try {

            mInterstitialAd = new PublisherInterstitialAd(this);

            mInterstitialAd.setAdUnitId(Constant.interstitialId);

            ins_adRequest = new PublisherAdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            mInterstitialAd.loadAd(ins_adRequest);
        } catch (Exception e) {
        }
    }

    public boolean requestNewInterstitial() {

        try {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isLoaded() {

        try {
            if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static synchronized MainApplication getInstance() {
        return appInstance;
    }

}
