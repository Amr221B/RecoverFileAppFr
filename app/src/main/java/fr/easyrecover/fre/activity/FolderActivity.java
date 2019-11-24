package fr.easyrecover.fre.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import fr.easyrecover.fre.R;
import fr.easyrecover.fre.common.Utils;
import fr.easyrecover.fre.model.ImageObject;
import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FolderActivity extends AppCompatActivity {

    ImageView back4;
    private LinearLayout back_btn;
    private LinearLayout btn_layout;
    private boolean clicked = false;
    private int count_copied = 0;
    private int count_error = 0;
    private LinearLayout delete_btn;
    ImageView deselect;
    private LinearLayout deselect_all_btn;
    boolean fb_folder_show = false;
    private ArrayList<String> folder = new ArrayList();
    private String folder_name;
    private int folder_size = 0;
    Handler handler = new Handler();
    private ArrayList<ImageObject> images = new ArrayList();
    private FrameLayout loading_layout;
    private NumberProgressBar loading_view;
    private int progress = 0;
    ProgressDialog progressDialog;
    private SimpleStringRecyclerViewAdapter rec_adapter;
    private RecyclerView recyclerView;
    private LinearLayout restore_btn;
    private ArrayList<String> saved_selected_path = new ArrayList();
    ImageView select;
    private LinearLayout select_all_btn;
    private ArrayList<String> selected_path = new ArrayList();
    private ArrayList<Integer> selected_position = new ArrayList();
    private Toolbar toolbar;


    @SuppressLint({"ResourceType"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_folder);

        findViews();
        initViews();


    }

    private void findViews() {

        select = (ImageView) findViewById(R.id.select);
        deselect = (ImageView) findViewById(R.id.deselect);
        back4 = (ImageView) findViewById(R.id.back4);
        back4.setOnClickListener(new backClick());
        delete_btn = (LinearLayout) findViewById(R.id.delete_btn);
        restore_btn = (LinearLayout) findViewById(R.id.restore_btn);
        btn_layout = (LinearLayout) findViewById(R.id.btn_layout);
        btn_layout.setVisibility(8);
        loading_view = (NumberProgressBar) findViewById(R.id.loading_view);
        loading_layout = (FrameLayout) findViewById(R.id.loading_layout);
        back_btn = (LinearLayout) findViewById(R.id.back_btn);
        select_all_btn = (LinearLayout) findViewById(R.id.select_all_btn);
        deselect_all_btn = (LinearLayout) findViewById(R.id.deselect_all_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void initViews() {

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        int position = getIntent().getIntExtra("position", 0);
        folder = (ArrayList) ListActivity.image_path.get(position);
        Iterator i$ = folder.iterator();
        while (i$.hasNext()) {
            images.add(new ImageObject((String) i$.next(), false));
        }
        folder_name = (String) ListActivity.folder.get(position);
        rec_adapter = new SimpleStringRecyclerViewAdapter(this, images);
        folder_size = images.size();
        if (images.size() == 0) {
            findViewById(R.id.loadingPanel).setVisibility(8);
            Toast.makeText(this, R.string.no_image, 1).show();
        }
        recyclerView.setAdapter(rec_adapter);
        select_all_btn.setOnClickListener(new selectAllclick());
        deselect_all_btn.setOnClickListener(new deselectAllclick());
        delete_btn.setOnClickListener(new deleteClick());
        restore_btn.setOnClickListener(new restoreClick());

    }


    class backClick implements View.OnClickListener {
        backClick() {
        }

        public void onClick(View view) {
            onBackPressed();
        }
    }

    class selectAllclick implements View.OnClickListener {
        selectAllclick() {
        }

        public void onClick(View v) {
            select.setImageResource(R.drawable.ic_select);
            deselect.setImageResource(R.drawable.ic_deselect);
            selected_path.clear();
            selected_position.clear();
            for (int i = 0; i < images.size(); i++) {
                ((ImageObject) rec_adapter.mValues.get(i)).setCheck(true);
                selected_path.add(((ImageObject) images.get(i)).getPath());
                selected_position.add(Integer.valueOf(i));
            }
            rec_adapter.notifyDataSetChanged();
            btn_layout.setVisibility(0);
        }
    }

    class deselectAllclick implements View.OnClickListener {
        deselectAllclick() {
        }

        public void onClick(View v) {
            select.setImageResource(R.drawable.ic_deselect);
            deselect.setImageResource(R.drawable.ic_select);
            selected_path.clear();
            selected_position.clear();
            for (int i = 0; i < images.size(); i++) {
                ((ImageObject) rec_adapter.mValues.get(i)).setCheck(false);
            }
            rec_adapter.notifyDataSetChanged();
            btn_layout.setVisibility(8);
        }
    }

    class deleteClick implements View.OnClickListener {
        deleteClick() {
        }

        public void onClick(View v) {
            int del_count = 0;
            Collections.sort(selected_position);
            Iterator i$ = selected_path.iterator();
            while (i$.hasNext()) {
                String path = (String) i$.next();
                new File(path).delete();
                folder.remove(path);
                images.remove(((Integer) selected_position.get(del_count)).intValue() - del_count);
                saved_selected_path.add(path);
                del_count++;
            }
            clicked = true;
            rec_adapter.notifyDataSetChanged();
            Toast.makeText(FolderActivity.this, del_count + " " + getResources().getString(R.string.delete_toast), 1).show();
            selected_position.clear();
            selected_path.clear();
            btn_layout.setVisibility(8);
        }
    }

    class restoreClick implements View.OnClickListener {
        restoreClick() {
        }

        public void onClick(View v) {
            fb_folder_show = true;
            loading_layout.setVisibility(0);
            new RestoreBackground().execute(new Void[0]);
        }
    }

    public class RestoreBackground extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FolderActivity.this);
            progressDialog.setMessage("Please wait Photo Will Restore");
            progressDialog.show();
        }

        private RestoreBackground() {
        }

        protected Void doInBackground(Void... params) {
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            try {
                Iterator i$ = selected_path.iterator();
                while (i$.hasNext()) {
                    String path = (String) i$.next();
                    String[] name = path.split("/");
                    File source_file = new File(path);
                    String type = "";
                    if (!name[name.length - 1].contains(".")) {
                        if (Utils.isJPEG(source_file).booleanValue()) {
                            type = ".jpg";
                        } else if (Utils.isPNG(source_file)) {
                            type = ".png";
                        }
                    }
                    File des_file = new File(ImageActivity.RESTORE_DIR + name[name.length - 1] + type);
                    try {
                        copyDirectory(source_file, des_file);
                        mediaScanIntent.setData(Uri.fromFile(des_file));
                        sendBroadcast(mediaScanIntent);
                        count_copied = count_copied + 1;
                        progress = (count_copied * 100) / selected_path.size();
                        publishProgress(new Void[0]);
                    } catch (Exception e) {
                        count_error = count_error + 1;
                        e.printStackTrace();
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
            count_error = folder_size - count_copied;
            Toast.makeText(FolderActivity.this, String.format(getResources().getString(R.string.restore_toast), new Object[]{Integer.valueOf(count_copied), ImageActivity.RESTORE_DIR + "\n    " + count_error}), 0).show();
            selected_path.clear();
            progress = 0;
            count_copied = 0;
            count_error = 0;
            btn_layout.setVisibility(8);
            for (int i = 0; i < images.size(); i++) {
                ((ImageObject) rec_adapter.mValues.get(i)).setCheck(false);
            }
            rec_adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    public class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {
        private int mBackground;
        private final Context mContext;
        private final TypedValue mTypedValue = new TypedValue();
        private List<ImageObject> mValues;

        public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {

            public final ImageView check_img;
            public final LinearLayout des_layout;
            public String mBoundString;
            public final ImageView mImageView;
            public final View mView;

            @SuppressLint({"WrongConstant"})
            public ViewHolder(View view) {
                super(view);
                this.mView = view;
                this.mImageView = (ImageView) view.findViewById(R.id.image);
                this.des_layout = (LinearLayout) view.findViewById(R.id.des_layout);
                this.des_layout.setVisibility(8);
                this.check_img = (ImageView) view.findViewById(R.id.check_img);
                this.check_img.setVisibility(8);
            }

            public String toString() {
                return super.toString();
            }
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<ImageObject> items) {
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

        @SuppressLint({"WrongConstant"})
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (((ImageObject) images.get(position)).isCheck()) {
                holder.check_img.setVisibility(0);
                holder.mImageView.setColorFilter(Color.argb(65, 64, 66, 1));
            } else {
                holder.check_img.setVisibility(8);
                holder.mImageView.setColorFilter(Color.argb(0, 0, 0, 0));
            }
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint({"WrongConstant"})
                public void onClick(View v) {
                    if (((ImageObject) SimpleStringRecyclerViewAdapter.this.mValues.get(position)).isCheck()) {
                        ((ImageObject) SimpleStringRecyclerViewAdapter.this.mValues.get(position)).setCheck(false);
                        holder.check_img.setVisibility(8);
                        holder.mImageView.setColorFilter(Color.argb(0, 0, 0, 0));
                        selected_path.remove(folder.get(position));
                        selected_position.remove(Integer.valueOf(position));
                        if (selected_path.size() != 0) {
                            btn_layout.setVisibility(0);
                        } else {
                            btn_layout.setVisibility(8);
                        }
                    } else {
                        ((ImageObject) SimpleStringRecyclerViewAdapter.this.mValues.get(position)).setCheck(true);
                        holder.check_img.setVisibility(0);
                        holder.mImageView.setColorFilter(Color.argb(65, 64, 66, 1));
                        selected_path.add(folder.get(position));
                        selected_position.add(Integer.valueOf(position));
                        if (selected_path.size() != 0) {
                            btn_layout.setVisibility(0);
                        } else {
                            btn_layout.setVisibility(8);
                        }
                    }
                    clicked = false;
                }
            });

            Glide.with(holder.mImageView.getContext())
                    .load(((ImageObject) this.mValues.get(position)).getPath())
                    .placeholder((int) R.drawable.no)
                    .fitCenter()
                    .into(holder.mImageView);

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


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                if (!this.clicked) {
                    this.selected_path.clear();
                }
                Intent send_broad = new Intent("Deleted Paths");
                send_broad.putExtra("Deleted List", this.saved_selected_path);
                send_broad.putExtra("folder_name", this.folder_name);
                LocalBroadcastManager.getInstance(this).sendBroadcast(send_broad);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        if (!this.clicked) {
            this.selected_path.clear();
        }
        Intent send_broad = new Intent("Deleted Paths");
        send_broad.putExtra("Deleted List", this.saved_selected_path);
        send_broad.putExtra("folder_name", this.folder_name);
        LocalBroadcastManager.getInstance(this).sendBroadcast(send_broad);
        finish();
        super.onBackPressed();
    }

    protected void onResume() {
        super.onResume();
    }
}
