package fr.easyrecover.fre.activity;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.easyrecover.fre.Constant;
import fr.easyrecover.fre.R;
import fr.easyrecover.fre.common.ImageScanner;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import net.bohush.geometricprogressview.GeometricProgressView;


public class ImageActivity extends AppCompatActivity {

    private ImageView scan_icon;

    LinearLayout show_button;
    private TextView number_text;
    public static String PKG;
    public static String RESTORE_DIR;


    public static ImageActivity imageActivity;
    GeometricProgressView progress;

    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageActivity = ImageActivity.this;
        addBanner();
        findViews();
        initViews();
    }

    private void findViews() {

        show_button = findViewById(R.id.show_button);
        number_text = findViewById(R.id.number_text);
        scan_icon = findViewById(R.id.scan_icon);
        progress = findViewById(R.id.progress);
        iv_back = findViewById(R.id.iv_back);


    }

    private void initViews() {

        PKG = getPackageName();
        RESTORE_DIR = PreferenceManager.getDefaultSharedPreferences(this).getString("restore_path", Environment.getExternalStorageDirectory().toString() + "/Delete Photo Recovery/");

        show_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.setVisibility(View.VISIBLE);
                ImageActivity.this.number_text.setText(ImageActivity.this.getResources().getString(R.string.number_text) + " 0 " + ImageActivity.this.getResources().getString(R.string.image) + " (0 B)");

                new ImageScanner(ImageActivity.this, ImageActivity.this.number_text, ImageActivity.this.scan_icon).execute(new Void[0]);

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    public void addBanner() {

        final AdView mAdView = new AdView(ImageActivity.this);
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
