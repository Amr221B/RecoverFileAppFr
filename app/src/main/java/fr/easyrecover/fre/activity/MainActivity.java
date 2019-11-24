package fr.easyrecover.fre.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import fr.easyrecover.fre.BuildConfig;
import fr.easyrecover.fre.Constant;
import fr.easyrecover.fre.R;
import fr.easyrecover.fre.share.DisplayMetricsHandler;
import fr.easyrecover.fre.share.MainApplication;
import com.google.android.gms.ads.AdListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String opt_name = "";
    private List<String> listPermissionsNeeded = new ArrayList<>();
    private int STORAGE_PERMISSION_CODE = 23;

    Toolbar toolbar;

    RecyclerView list;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    int row_index = 0;
    int posofItem = 0;

    LinearLayout lnImage, lnVideos, lnContatc;

    RecyclerView.Adapter iv_recycler_Adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermissions();
        }
        //addBanner();
        findViews();
        initViews();
    }


    private void findViews() {

        toolbar = findViewById(R.id.toolbar);
        list = findViewById(R.id.list);
        lnImage = findViewById(R.id.iv_image);
        lnVideos = findViewById(R.id.iv_video);
        lnContatc = findViewById(R.id.iv_contact);

    }

    private void initViews() {

       // toolbar.setNavigationIcon((int) R.drawable.menu);
        setSupportActionBar(toolbar);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setBackground(getResources().getDrawable(R.drawable.navigation));
        //toggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
        //toggle.setHomeAsUpIndicator(R.color.colorWhite);
        toggle.syncState();
        drawer.setDrawerListener(toggle);

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();


        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        iv_recycler_Adapter = new RecyclerViewAdapter(MainActivity.this, screenTitles, screenIcons, posofItem);
        list.setAdapter(iv_recycler_Adapter);

        lnImage.setOnClickListener(this);
        lnVideos.setOnClickListener(this);
        lnContatc.setOnClickListener(this);

    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {

        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    /*public void addBanner() {


        final AdView mAdView = new AdView(MainActivity.this);
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
    }*/


    @Override
    public void onClick(View v) {

        if (v == lnImage) {

            Random rand = new Random();
            int randomNum = 0 + rand.nextInt(5);


            if (randomNum == 0) {
                if (checkAndRequestPermissions()) {


                    if (!MainApplication.getInstance().requestNewInterstitial()) {

                        opt_name = "image";
                        Intent i = new Intent(MainActivity.this, ImageActivity.class);
                        startActivity(i);

                    } else {

                        MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();

                                MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                                MainApplication.getInstance().mInterstitialAd = null;
                                MainApplication.getInstance().ins_adRequest = null;
                                MainApplication.getInstance().LoadAds();

                                opt_name = "image";
                                Intent i = new Intent(MainActivity.this, ImageActivity.class);
                                startActivity(i);

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


                } else {

                    opt_name = "image";
                    ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                }
            } else {
                if (checkAndRequestPermissions()) {

                    opt_name = "image";
                    Intent i = new Intent(MainActivity.this, ImageActivity.class);
                    startActivity(i);

                } else {

                    opt_name = "image";
                    ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                }
            }


        } else if (v == lnVideos) {

            Random rand = new Random();
            int randomNum = 0 + rand.nextInt(5);
            if (randomNum == 0) {
                if (checkAndRequestPermissions()) {


                    if (!MainApplication.getInstance().requestNewInterstitial()) {

                        opt_name = "video";
                        Intent i = new Intent(MainActivity.this, VideoActivity.class);
                        startActivity(i);

                    } else {

                        MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();

                                MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                                MainApplication.getInstance().mInterstitialAd = null;
                                MainApplication.getInstance().ins_adRequest = null;
                                MainApplication.getInstance().LoadAds();

                                opt_name = "video";
                                Intent i = new Intent(MainActivity.this, VideoActivity.class);
                                startActivity(i);

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


                } else {

                    opt_name = "video";
                    ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                }
            } else {
                if (checkAndRequestPermissions()) {

                    opt_name = "video";
                    Intent i = new Intent(MainActivity.this, VideoActivity.class);
                    startActivity(i);

                } else {

                    opt_name = "video";
                    ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                }
            }


        } else if (v == lnContatc) {


            Random rand = new Random();
            int randomNum = 0 + rand.nextInt(5);


            if (randomNum == 0) {
                if (checkAndRequestPermissions()) {


                    if (!MainApplication.getInstance().requestNewInterstitial()) {

                        opt_name = "contact";
                        Intent i = new Intent(MainActivity.this, ContactActivity.class);
                        startActivity(i);

                    } else {

                        MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();

                                MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                                MainApplication.getInstance().mInterstitialAd = null;
                                MainApplication.getInstance().ins_adRequest = null;
                                MainApplication.getInstance().LoadAds();

                                opt_name = "contact";
                                Intent i = new Intent(MainActivity.this, ContactActivity.class);
                                startActivity(i);

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


                } else {

                    opt_name = "contact";
                    ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                }
            } else {
                if (checkAndRequestPermissions()) {

                    opt_name = "contact";
                    Intent i = new Intent(MainActivity.this, ContactActivity.class);
                    startActivity(i);

                } else {

                    opt_name = "contact";
                    ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                }
            }

        }

    }


    private boolean checkAndRequestPermissions() {

        listPermissionsNeeded.clear();

        int read_storage = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int storage = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readphonestate = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE);
        int readcontact = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS);
        int writecontact = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CONTACTS);

        if (read_storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readphonestate != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (readcontact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }

        if (writecontact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
        }

        Log.e("TAG", "listPermissionsNeeded===>" + listPermissionsNeeded);
        if (!listPermissionsNeeded.isEmpty()) {
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        boolean isaccept = false;

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                isaccept = true;
            } else {
                isaccept = false;
                break;
            }
        }

        if (isaccept) {

            if (grantResults.length == listPermissionsNeeded.size()) {

                if (opt_name == "image") {


                    startActivity(new Intent(MainActivity.this, ImageActivity.class));

                } else if (opt_name == "video") {


                    startActivity(new Intent(MainActivity.this, VideoActivity.class));

                } else if (opt_name == "contact") {


                    startActivity(new Intent(MainActivity.this, ContactActivity.class));

                }

            } else {

                Toast.makeText(MainActivity.this, "Oops, you just denied the permission", Toast.LENGTH_LONG).show();
            }
        } else {

            Toast.makeText(MainActivity.this, "Oops, you just denied the permission", Toast.LENGTH_LONG).show();
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private String[] screenTitles;
        private Drawable[] screenIcons;
        Context context1;

        public RecyclerViewAdapter(Context context2, String[] screenTitles, Drawable[] screenIcons, int posofItem) {

            this.screenTitles = screenTitles;
            this.screenIcons = screenIcons;
            context1 = context2;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView icon;
            public TextView title, txtCoin;
            public LinearLayout layoutItem;

            public ViewHolder(View v) {
                super(v);
                icon = (ImageView) v.findViewById(R.id.icon);
                title = (TextView) v.findViewById(R.id.title);
                layoutItem = (LinearLayout) v.findViewById(R.id.layoutItem);
                //txtCoin = (TextView) v.findViewById(R.id.txtCoin);
            }
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view1 = LayoutInflater.from(context1).inflate(R.layout.item_option, parent, false);
            ViewHolder viewHolder1 = new ViewHolder(view1);
            return viewHolder1;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.icon.setImageDrawable(screenIcons[position]);

            holder.title.setText(screenTitles[position]);
            // holder.txtCoin.setVisibility(View.GONE);

            holder.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    row_index = position;

                    notifyDataSetChanged();

                    if (position == 0) {

                        showRateUs();
                        //goSettings();


                    } else if (position == 1) {

                        shareApp();

                    } else if (position == 2) {

                        openGmailApp(MainActivity.this);

                    } else if (position == 3) {

                        Random rand = new Random();
                        int randomNum = 0 + rand.nextInt(5);

                        if (randomNum == 3) {

                            if (!MainApplication.getInstance().requestNewInterstitial()) {

                                Intent i = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
                                startActivity(i);
                            } else {


                                MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();

                                        MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                                        MainApplication.getInstance().mInterstitialAd = null;
                                        MainApplication.getInstance().ins_adRequest = null;
                                        MainApplication.getInstance().LoadAds();

                                        Intent i = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
                                        startActivity(i);

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

                        } else {

                            Intent i = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
                            startActivity(i);

                        }

                    }
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                }
            });
        }

        @Override
        public int getItemCount() {
            return screenTitles.length;
        }
    }


    public void openGmailApp(Context context) {

        String[] TO = {Constant.contactId};
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, TO);

        intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Backup");
        //intent.putExtra(Intent.EXTRA_TEXT, "The message");
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Mail account not configured", Toast.LENGTH_SHORT).show();
        }

    }

    private void shareApp() {

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Récupérer les fichiers supprimés");
            String shareMessage = "Récupérer les fichiers supprimés" + "\n\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    private void showRateUs() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to rate this App?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {


        try {

            Log.e("TAG", "onBackPressed: ");

            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dlg_exit1);
            dialog.getWindow().setLayout((int) (DisplayMetricsHandler.getScreenWidth() - 50), Toolbar.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            LinearLayout rateus, shareapp, nothnks;

            rateus = dialog.findViewById(R.id.rateus);
            shareapp = dialog.findViewById(R.id.shareapp);
            nothnks = dialog.findViewById(R.id.nothnks);

            rateus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));

                    } catch (android.content.ActivityNotFoundException anfe) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    }

                }
            });

            shareapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                        String shareMessage = "Récupérer les fichiers supprimés" + "\n\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                        dialog.dismiss();

                    } catch (Exception e) {
                        //e.toString();
                    }

                }
            });

            nothnks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.cancel();
                    MainActivity.super.onBackPressed();
                    finish();

                }
            });

        } catch (Exception e) {
            e.getMessage();
        }


    }
}
