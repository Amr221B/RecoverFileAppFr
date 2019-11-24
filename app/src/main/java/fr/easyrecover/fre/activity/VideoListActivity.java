package fr.easyrecover.fre.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import fr.easyrecover.fre.Constant;
import fr.easyrecover.fre.R;
import fr.easyrecover.fre.common.VideoScanner;
import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    public static ArrayList<String> folder = new ArrayList();
    public static ArrayList<ArrayList<String>> image_path = new ArrayList();
    ImageView back6;
    private LinearLayout back_btn;
    private LinearLayout btn_layout;
    private int count_copied = 0;
    private int count_error = 0;
    private LinearLayout delete_btn;
    private ArrayList<String> deleted_images = new ArrayList();
    private LinearLayout deselect_all_btn;
    private ArrayList<String> image_full_list = new ArrayList();
    private HashMap<String, ArrayList<String>> list = new HashMap();
    private FrameLayout loading_layout;
    private NumberProgressBar loading_view;

    private int position_a = 0;
    private int progress = 0;
    private SimpleStringRecyclerViewAdapter rec_adapter;
    BroadcastReceiver receiver = new broadcastCall();
    private RecyclerView recyclerView;
    private LinearLayout restore_btn;
    private LinearLayout select_all_btn;
    private boolean selected = false;
    private String selected_folder;
    private int size = 0;
    private ArrayList<String> thumbnail_list = new ArrayList();


    @SuppressLint({"ResourceType"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_video_list);

        addBanner();
        findViews();
        initViews();

    }


    private void findViews() {

        back6 = (ImageView) findViewById(R.id.back6);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        select_all_btn = (LinearLayout) findViewById(R.id.select_all_btn);
        deselect_all_btn = (LinearLayout) findViewById(R.id.deselect_all_btn);
        btn_layout = (LinearLayout) findViewById(R.id.btn_layout);
        delete_btn = (LinearLayout) findViewById(R.id.delete_btn);
        restore_btn = (LinearLayout) findViewById(R.id.restore_btn);
        loading_view = (NumberProgressBar) findViewById(R.id.loading_view);
        loading_layout = (FrameLayout) findViewById(R.id.loading_layout);
        back_btn = (LinearLayout) findViewById(R.id.back_btn);

    }

    private void initViews() {

        back6.setOnClickListener(new backClick());
        btn_layout.setVisibility(8);
    }


    class broadcastCall extends BroadcastReceiver {
        broadcastCall() {
        }

        public void onReceive(Context context, Intent intent) {
            deleted_images = intent.getStringArrayListExtra("Deleted List");
            selected_folder = intent.getStringExtra("folder_name");
        }
    }

    class backClick implements View.OnClickListener {
        backClick() {
        }

        public void onClick(View view) {
            Intent intent = new Intent(VideoListActivity.this, MainActivity.class);
            intent.setFlags(268468224);
            startActivity(intent);

        }
    }

    class C08543 implements Comparator<ArrayList<String>> {
        C08543() {
        }

        public int compare(ArrayList<String> lhs, ArrayList<String> rhs) {
            return rhs.size() - lhs.size();
        }
    }

    class selectAllclick implements View.OnClickListener {
        selectAllclick() {
        }

        @SuppressLint({"WrongConstant"})
        public void onClick(View v) {
            selected = true;
            rec_adapter.notifyDataSetChanged();
            btn_layout.setVisibility(0);
        }
    }

    class deselectAllclick implements View.OnClickListener {
        deselectAllclick() {
        }

        @SuppressLint({"WrongConstant"})
        public void onClick(View v) {
            selected = false;
            rec_adapter.notifyDataSetChanged();
            btn_layout.setVisibility(8);
        }
    }

    class deleteClick implements View.OnClickListener {
        deleteClick() {
        }

        public void onClick(View v) {
            int count = 0;
            Iterator it = VideoListActivity.image_path.iterator();
            while (it.hasNext()) {
                Iterator i$ = ((ArrayList) it.next()).iterator();
                while (i$.hasNext()) {
                    new File((String) i$.next()).delete();
                    count++;
                }
            }
            VideoListActivity.folder.clear();
            VideoListActivity.image_path.clear();
            thumbnail_list.clear();
            Toast.makeText(VideoListActivity.this, count + " " + getResources().getString(R.string.delete_toast), 0).show();
            rec_adapter.notifyDataSetChanged();
        }
    }

    class restoreClick implements View.OnClickListener {
        restoreClick() {
        }

        @SuppressLint({"WrongConstant"})
        public void onClick(View v) {
            loading_layout.setVisibility(0);
            new RestoreBackground().execute(new Void[0]);
        }
    }

    @SuppressLint({"StaticFieldLeak"})
    private class RestoreBackground extends AsyncTask<Void, Void, Void> {
        private RestoreBackground() {
        }

        protected Void doInBackground(Void... params) {
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            try {
                Iterator it = VideoListActivity.image_path.iterator();
                while (it.hasNext()) {
                    Iterator i$ = ((ArrayList) it.next()).iterator();
                    while (i$.hasNext()) {
                        String path = (String) i$.next();
                        String[] name = path.split("/");
                        File source_file = new File(path);
                        String type = "";
                        if (!name[name.length - 1].contains(".")) {
                            type = ".mp4";
                        }
                        File des_file = new File(VideoActivity.RESTORE_DIR + name[name.length - 1] + type);
                        try {
                            copyDirectory(source_file, des_file);
                            mediaScanIntent.setData(Uri.fromFile(des_file));
                            sendBroadcast(mediaScanIntent);
                            count_copied = count_copied + 1;
                            progress = (count_copied * 100) / size;
                            publishProgress(new Void[0]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e2) {
            }
            return null;
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @SuppressLint({"WrongConstant"})
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            count_error = size - count_copied;
            Toast.makeText(VideoListActivity.this, String.format(getResources().getString(R.string.restore_toast), new Object[]{Integer.valueOf(count_copied), VideoActivity.RESTORE_DIR + "\n    " + count_error}), 0).show();
            progress = 0;
            count_copied = 0;
            count_error = 0;
            btn_layout.setVisibility(8);
            selected = false;
            rec_adapter.notifyDataSetChanged();
        }
    }

    public class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {
        private int mBackground;
        private final Context mContext;
        private final TypedValue mTypedValue = new TypedValue();
        private List<String> mValues;

        public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            public final ImageView check_img;
            public final TextView countTextView;
            public String mBoundString;
            public final ImageView mImageView;
            public final View mView;

            @SuppressLint({"WrongConstant"})
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.image);
                countTextView = (TextView) view.findViewById(R.id.folder_count);
                check_img = (ImageView) view.findViewById(R.id.check_img);
                this.check_img.setVisibility(8);
            }

            public String toString() {
                return super.toString();
            }
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<String> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, this.mTypedValue, true);
            this.mBackground = this.mTypedValue.resourceId;
            this.mValues = items;
            this.mContext = context;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
            view.setBackgroundResource(this.mBackground);
            return new ViewHolder(view);
        }

        @SuppressLint({"WrongConstant", "SetTextI18n"})
        public void onBindViewHolder(ViewHolder holder, @SuppressLint({"RecyclerView"}) final int position) {
            if (((ArrayList) VideoListActivity.image_path.get(position)).size() == 0) {
                position_a = position;
                holder.countTextView.setText(((ArrayList) VideoListActivity.image_path.get(position + 1)).size() + " " + getResources().getString(R.string.video));
            } else if (position_a == 0) {
                holder.countTextView.setText(((ArrayList) VideoListActivity.image_path.get(position)).size() + " " + getResources().getString(R.string.video));
            } else if (position >= position_a) {
                holder.countTextView.setText(((ArrayList) VideoListActivity.image_path.get(position + 1)).size() + " " + getResources().getString(R.string.video));
            } else {
                holder.countTextView.setText(((ArrayList) VideoListActivity.image_path.get(position)).size() + " " + getResources().getString(R.string.video));
            }
            if (selected) {
                holder.check_img.setVisibility(0);
                holder.mImageView.setColorFilter(Color.argb(65, 64, 66, 1));
            } else {
                holder.check_img.setVisibility(8);
                holder.mImageView.setColorFilter(Color.argb(0, 0, 0, 0));
            }
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        Intent mIntent = new Intent(VideoListActivity.this, VideoFolderActivity.class);
                        if (((ArrayList) VideoListActivity.image_path.get(position)).size() != 0) {
                            mIntent.putExtra("position", position);
                        } else {
                            mIntent.putExtra("position", position + 1);
                        }
                        startActivity(mIntent);
                    } catch (Exception e) {
                        Toast.makeText(VideoListActivity.this, "+e", 0).show();
                    }
                }
            });
            Glide.with(holder.mImageView.getContext()).load((String) this.mValues.get(position)).placeholder((int) R.drawable.no).fitCenter().into(holder.mImageView);
            findViewById(R.id.loadingPanel).setVisibility(8);
        }

        public int getItemCount() {
            try {
                return this.mValues.size();
            } catch (NullPointerException e) {
                return 0;
            }
        }
    }


    protected void onResume() {
        super.onResume();
        position_a = 0;
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("Deleted Paths"));
        size = getIntent().getIntExtra("size", 0);
        list = VideoScanner.folderImage;
        Iterator i$ = deleted_images.iterator();
        while (i$.hasNext()) {
            try {
                ((ArrayList) list.get(selected_folder)).remove(i$.next());
            } catch (Exception e) {
            }
        }
        folder.clear();
        image_path.clear();
        thumbnail_list.clear();
        for (String folder_name : list.keySet()) {
            if (((ArrayList) list.get(folder_name)).size() > 0) {
                image_path.add(list.get(folder_name));
            }
        }
        Collections.sort(image_path, new C08543());
        i$ = image_path.iterator();
        while (i$.hasNext()) {
            ArrayList<String> list1 = (ArrayList) i$.next();
            try {
                Iterator i$2 = deleted_images.iterator();
                while (i$2.hasNext()) {
                    list1.remove(i$2.next());
                }
                if (list1.size() > 0) {
                    String temp_str = (String) list1.get(0);
                    thumbnail_list.add(temp_str);
                    String[] temp = temp_str.split("/");
                    folder.add(temp_str.substring(temp_str.length() - temp[temp.length - 1].length()));
                }
            } catch (Exception e2) {
            }
        }
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        rec_adapter = new SimpleStringRecyclerViewAdapter(this, thumbnail_list);
        recyclerView.setAdapter(rec_adapter);
        select_all_btn.setOnClickListener(new selectAllclick());
        deselect_all_btn.setOnClickListener(new deselectAllclick());
        delete_btn.setOnClickListener(new deleteClick());
        restore_btn.setOnClickListener(new restoreClick());
    }

    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void copyDirectory(File sourceLocation, File targetLocation) throws IOException {
        if (!sourceLocation.isDirectory()) {
            File directory = targetLocation.getParentFile();
            if (directory == null || directory.exists() || directory.mkdirs()) {
                InputStream in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);
                byte[] buf = new byte[1024];
                while (true) {
                    int len = in.read(buf);
                    if (len > 0) {
                        out.write(buf, 0, len);
                    } else {
                        in.close();
                        out.close();
                        return;
                    }
                }
            }
            throw new IOException("Cannot create dir " + directory.getAbsolutePath());
        } else if (targetLocation.exists() || targetLocation.mkdirs()) {
            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
            }
        } else {
            throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(268468224);
        startActivity(intent);

    }

    public void addBanner() {

        final AdView mAdView = new AdView(VideoListActivity.this);
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
