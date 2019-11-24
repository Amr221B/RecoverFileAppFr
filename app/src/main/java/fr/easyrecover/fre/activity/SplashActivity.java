package fr.easyrecover.fre.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fr.easyrecover.fre.R;
import fr.easyrecover.fre.share.MainApplication;
import com.google.android.gms.ads.AdListener;


public class SplashActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Runnable myRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setStoreToken(getPackageName());

        handler = new Handler();

        myRunnable = new Runnable() {
            public void run() {

                initViews();
            }
        };
        handler.postDelayed(myRunnable, 2000);

    }

    private void initViews() {

        if (!MainApplication.getInstance().requestNewInterstitial()) {

            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();

        } else {

            MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();

                    MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                    MainApplication.getInstance().mInterstitialAd = null;
                    MainApplication.getInstance().ins_adRequest = null;
                    MainApplication.getInstance().LoadAds();

                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }


                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }
            });
        }

    }

    private void setStoreToken(String token) {
        SharedPreferences sp = getSharedPreferences(getPackageName(), 0);
        String gm = sp.getString("gm", "");
        if (0 == 0 && gm.equals("")) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("gm", "0");
            editor.commit();
            gm = sp.getString("gm", "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!MainApplication.getInstance().isLoaded())
            MainApplication.getInstance().LoadAds();

    }

}
