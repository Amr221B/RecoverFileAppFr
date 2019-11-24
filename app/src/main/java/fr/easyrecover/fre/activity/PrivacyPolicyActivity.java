package fr.easyrecover.fre.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import fr.easyrecover.fre.Constant;
import fr.easyrecover.fre.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class PrivacyPolicyActivity extends AppCompatActivity {

    WebView txtInformtation;
    ImageView imgBack;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        addBanner();
        findViews();
        initViews();
    }

    private void findViews() {

        toolbar = findViewById(R.id.toolbar);
        imgBack = findViewById(R.id.ic_back);

    }

    private void initViews() {

        txtInformtation = findViewById(R.id.txtInformtation);
        txtInformtation.loadUrl("file:///android_asset/privacyPolicy.html");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }

    public void addBanner() {

        final AdView mAdView = new AdView(PrivacyPolicyActivity.this);
        mAdView.setAdSize(AdSize.BANNER);
        final View adContainer = findViewById(R.id.layoutViewAdd);


        mAdView.setAdUnitId(Constant.bannerId);

        ((LinearLayout) adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("EA965DE183B804F71E5E6D353E6607DE")
                .addTestDevice("5CE992DB43E8F2B50F7D2201A724526D")
                .addTestDevice("6E5543AE954EAD6702405BFCCC34C9A2")
                .addTestDevice("28373E4CC308EDBD5C5D39795CD4956A")
                .addTestDevice("3C5740EB2F36FB5F0FEFA773607D27CE") // mi white
                .addTestDevice("79E8DED973BDF7477739501E228D88E1") //samsung max
                .build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);


                adContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                adContainer.setVisibility(View.VISIBLE);

            }
        });
    }
}