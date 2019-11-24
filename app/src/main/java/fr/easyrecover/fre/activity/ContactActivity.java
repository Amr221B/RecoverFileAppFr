package fr.easyrecover.fre.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import fr.easyrecover.fre.Constant;
import fr.easyrecover.fre.R;
import fr.easyrecover.fre.adapter.AllContactsAdapter;
import fr.easyrecover.fre.adapter.DeletedContactsAdapter;
import fr.easyrecover.fre.model.ContactModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    AllContactsAdapter allContactsAdaper;
    ImageView back8;
    Button btnAllContacts;
    Button btnDeletedContacts;
    JSONObject dataJson = new JSONObject();
    DeletedContactsAdapter deletedContactsAdapter;
    boolean fb_con_show = false;
    boolean isDeletedContacts = false;
    ProgressDialog mProgressDialog = null;
    ArrayList<ContactModel> myAllContactList = new ArrayList();
    ArrayList<ContactModel> myDeletedContactList = new ArrayList();
    ContactPagerAdapter pageAdapter;
    ViewPager pager;
    ImageView refresh;
    String tag = "Contacts";


    @SuppressLint({"NewApi"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_contact);

        addBanner();
        findViews();
        initViews();

    }

    private void findViews() {

        back8 = (ImageView) findViewById(R.id.back8);
        back8.setOnClickListener(new backClick());
        refresh = (ImageView) findViewById(R.id.refresh);
        refresh.setOnClickListener(new C08003());
        pager = (ViewPager) findViewById(R.id.pager);
        btnAllContacts = (Button) findViewById(R.id.btnallcontacts);
        btnDeletedContacts = (Button) findViewById(R.id.btndeletedcontacts);
    }

    private void initViews() {

        btnAllContacts.setOnClickListener(this);
        btnDeletedContacts.setOnClickListener(this);
        isDeletedContacts = false;
        new getAllContactsBackTask().execute(new String[0]);
        pager.setOnPageChangeListener(new pageChangeListener());
    }


    class backClick implements View.OnClickListener {


        backClick() {
        }

        public void onClick(View view) {
            onBackPressed();
        }
    }

    class C08003 implements View.OnClickListener {

        class C07992 implements Runnable {
            C07992() {
            }

            public void run() {
                btnDeletedContacts.setBackgroundDrawable(getResources().getDrawable(R.drawable.mydrawable_three));
                btnAllContacts.setBackgroundDrawable(getResources().getDrawable(R.drawable.mydrawable_two));
            }
        }

        class C11541 implements PopupMenu.OnMenuItemClickListener {

            class C07971 implements DialogInterface.OnClickListener {
                C07971() {
                }

                public void onClick(DialogInterface arg0, int arg1) {
                    new restoreAllContactsBackTask().execute(new String[0]);
                    arg0.dismiss();
                }
            }

            class C07982 implements DialogInterface.OnClickListener {
                C07982() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }

            C11541() {
            }

            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.refre:
                        new getAllContactsBackTask().execute(new String[0]);
                        break;
                    case R.id.resta:
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContactActivity.this);
                        alertDialogBuilder.setMessage("Do you Want Restore All Contacts ??");
                        alertDialogBuilder.setPositiveButton("Yes", new C07971());
                        alertDialogBuilder.setNegativeButton("No", new C07982());
                        alertDialogBuilder.create().show();
                        break;
                }
                return false;
            }
        }

        C08003() {
        }

        public void onClick(View view) {
            PopupMenu popup = new PopupMenu(ContactActivity.this, view);
            popup.setOnMenuItemClickListener(new C11541());
            popup.inflate(R.menu.main);
            popup.show();
        }
    }

    class pageChangeListener implements ViewPager.OnPageChangeListener {

        class C08011 implements Runnable {
            C08011() {
            }

            public void run() {
                btnAllContacts.setBackgroundDrawable(getResources().getDrawable(R.drawable.mydrawable_one));
                btnDeletedContacts.setBackgroundDrawable(getResources().getDrawable(R.drawable.mydrawable_four));
            }
        }

        class C08022 implements Runnable {
            C08022() {
            }

            public void run() {
                btnDeletedContacts.setBackgroundDrawable(getResources().getDrawable(R.drawable.mydrawable_three));
                btnAllContacts.setBackgroundDrawable(getResources().getDrawable(R.drawable.mydrawable_two));
            }
        }

        pageChangeListener() {
        }

        public void onPageSelected(int arg0) {
            if (arg0 == 0) {
                runOnUiThread(new C08011());
                isDeletedContacts = false;
            } else if (arg0 == 1) {
                runOnUiThread(new C08022());
                isDeletedContacts = true;
            }
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    }

    public class ContactPagerAdapter extends PagerAdapter {
        private final String[] TITLES = new String[]{"All", "Deleted"};
        LayoutInflater inflater;
        Context mContext;

        public CharSequence getPageTitle(int position) {
            return this.TITLES[position];
        }

        public ContactPagerAdapter(Context myProfileActivity) {
            this.mContext = myProfileActivity;
        }

        public int getCount() {
            return this.TITLES.length;
        }

        @SuppressLint({"WrongConstant"})
        public Object instantiateItem(ViewGroup container, int position) {

            inflater = (LayoutInflater) mContext.getSystemService("layout_inflater");
            View itemView = inflater.inflate(R.layout.row_viewpagers, container, false);
            final ListView lvDeletedContacts = (ListView) itemView.findViewById(R.id.my_listview);
            final TextView txtNoContacts = (TextView) itemView.findViewById(R.id.tv_no_cotnacts);
            if (position == 0) {
                getDeletedContacts("0", lvDeletedContacts, txtNoContacts);
            } else if (position == 1) {
                getDeletedContacts("1", lvDeletedContacts, txtNoContacts);
            }
            lvDeletedContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                class C08042 implements DialogInterface.OnClickListener {
                    C08042() {
                    }

                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }

                public void onItemClick(AdapterView<?> adapterView, View view, int arg0, long id) {
                    if (pager.getCurrentItem() == 1) {
                        final ContactModel cm = (ContactModel) myDeletedContactList.get(arg0);
                        new ContentValues().put("deleted", "0");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ContactActivity.this);
                        builder.setMessage("Do you Want to Restore Contact ??");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    fb_con_show = true;
                                    ArrayList<ContentProviderOperation> ops = new ArrayList();
                                    ops.add(ContentProviderOperation.newUpdate(ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").build()).withSelection("_id=?", new String[]{String.valueOf(cm.getConractRawId())}).withValue("deleted", Integer.valueOf(0)).withYieldAllowed(true).build());
                                    getContentResolver().applyBatch("com.android.contacts", ops);
                                    myDeletedContactList.remove(cm);
                                    deletedContactsAdapter.notifyDataSetChanged();
                                    if (myDeletedContactList.size() > 0) {
                                        txtNoContacts.setVisibility(8);
                                        lvDeletedContacts.setVisibility(0);
                                    } else {
                                        txtNoContacts.setVisibility(0);
                                        lvDeletedContacts.setVisibility(8);
                                        txtNoContacts.setText(getResources().getString(R.string.no_deleted_contacts));
                                    }
                                    showToast(ContactActivity.this, getResources().getString(R.string.contact_restore_success));
                                    dialogInterface.dismiss();
                                } catch (RemoteException e2) {
                                    e2.printStackTrace();
                                    showToast(ContactActivity.this, getResources().getString(R.string.contact_not_restore));
                                    Log.e(tag, "item click i " + e2.toString());
                                    dialogInterface.dismiss();
                                } catch (OperationApplicationException e3) {
                                    e3.printStackTrace();
                                    showToast(ContactActivity.this, getResources().getString(R.string.contact_not_restore));
                                    Log.e(tag, "item click we " + e3.toString());
                                    dialogInterface.dismiss();
                                }
                            }
                        });
                        builder.setNegativeButton("No", new C08042());
                        builder.create().show();
                    }
                }
            });
            container.addView(itemView);
            return itemView;
        }

        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }

        public boolean isViewFromObject(View v, Object o) {
            return v == o;
        }
    }

    public class getAllContactsBackTask extends AsyncTask<String, String, String> {
        String erroMessage = "";

        class C08061 implements Runnable {
            C08061() {
            }

            public void run() {
                pageAdapter = new ContactPagerAdapter(ContactActivity.this);
                pager.setAdapter(pageAdapter);
                if (isDeletedContacts) {
                    pager.setCurrentItem(1);
                    isDeletedContacts = true;
                    return;
                }
                pager.setCurrentItem(0);
                isDeletedContacts = false;
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        protected String doInBackground(String... args) {
            try {
                String obj;
                myAllContactList.clear();
                myDeletedContactList.clear();
                ContentResolver contentResolver = getContentResolver();
                Uri uri = ContactsContract.RawContacts.CONTENT_URI;
                String stringBuilder = new StringBuilder(String.valueOf(ContactActivity.hasHoneycomb() ? "sort_key" : "display_name")).append(" IS NOT NULL").toString();
                if (ContactActivity.hasHoneycomb()) {
                    obj = "sort_key";
                } else {
                    obj = "display_name";
                }
                Cursor rawContacts = contentResolver.query(uri, null, stringBuilder, null, new StringBuilder(String.valueOf(obj)).append(" COLLATE LOCALIZED ASC").toString());
                if (rawContacts != null && rawContacts.getCount() > 0 && rawContacts.moveToFirst()) {
                    while (!rawContacts.isAfterLast()) {
                        if (rawContacts.getPosition() == 0) {
                            int j = 0;
                            while (j < rawContacts.getColumnCount()) {
                                try {
                                    dataJson.putOpt(String.valueOf(j), rawContacts.getColumnName(j));
                                    j++;
                                } catch (Exception e) {
                                }
                            }
                        }
                        String conractRawId = String.valueOf(rawContacts.getInt(rawContacts.getColumnIndex("_id")));
                        String contactContactId = String.valueOf(rawContacts.getInt(rawContacts.getColumnIndex("contact_id")));
                        boolean deleted = rawContacts.getInt(rawContacts.getColumnIndex("deleted")) == 1;
                        String contactName = rawContacts.getString(rawContacts.getColumnIndex(ContactActivity.hasHoneycomb() ? "sort_key" : "display_name"));
                        if (deleted) {
                            myDeletedContactList.add(new ContactModel(conractRawId, contactContactId, contactName, "yes"));
                        } else {
                            myAllContactList.add(new ContactModel(conractRawId, contactContactId, contactName, "no"));
                        }
                        rawContacts.moveToNext();
                    }
                }
                if (rawContacts != null) {
                    try {
                        rawContacts.close();
                    } catch (Exception e2) {
                    }
                }
            } catch (Exception e3) {
            }
            return erroMessage;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideDialog();
            runOnUiThread(new C08061());
        }
    }

    public class restoreAllContactsBackTask extends AsyncTask<String, String, String> {
        String erroMessage = "";

        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        protected String doInBackground(String... args) {
            if (myDeletedContactList.size() > 0) {
                for (int i = 0; i < myDeletedContactList.size(); i++) {
                    ContactModel cm = (ContactModel) myDeletedContactList.get(i);
                    new ContentValues().put("deleted", "0");
                    try {
                        ArrayList<ContentProviderOperation> ops = new ArrayList();
                        ops.add(ContentProviderOperation.newUpdate(ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").build()).withSelection("_id=?", new String[]{String.valueOf(cm.getConractRawId())}).withValue("deleted", Integer.valueOf(0)).withYieldAllowed(true).build());
                        getContentResolver().applyBatch("com.android.contacts", ops);
                        erroMessage = getResources().getString(R.string.allcontact_restore_success);
                    } catch (RemoteException e) {
                        try {
                            e.printStackTrace();
                            erroMessage = getResources().getString(R.string.allcontact_not_restore);
                            Log.e(tag, "restore all eor " + e.toString());
                        } catch (Exception e3) {
                            erroMessage = getResources().getString(R.string.allcontact_not_restore);
                            Log.e(tag, "restore all errro e " + e3.toString());
                        }
                    } catch (OperationApplicationException e2) {
                        e2.printStackTrace();
                        erroMessage = getResources().getString(R.string.allcontact_not_restore);
                        Log.e(tag, "restore all error " + e2.toString());
                    }
                }
            }
            return erroMessage;
        }

        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            hideDialog();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (result.equalsIgnoreCase(getResources().getString(R.string.allcontact_restore_success))) {
                        myDeletedContactList.clear();
                        deletedContactsAdapter.notifyDataSetChanged();
                        pageAdapter = new ContactPagerAdapter(ContactActivity.this);
                        pager.setAdapter(pageAdapter);
                        if (isDeletedContacts) {
                            pager.setCurrentItem(1);
                            isDeletedContacts = true;
                        } else {
                            pager.setCurrentItem(0);
                            isDeletedContacts = false;
                        }
                        showToast(ContactActivity.this, getResources().getString(R.string.allcontact_restore_success));
                    } else if (result.equalsIgnoreCase(getResources().getString(R.string.allcontact_not_restore))) {
                        showToast(ContactActivity.this, getResources().getString(R.string.allcontact_not_restore));
                    }
                }
            });
        }
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 82) {
            return "LGE".equalsIgnoreCase(Build.BRAND) ? true : true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 82) {
            return super.onKeyUp(keyCode, event);
        }
        if ("LGE".equalsIgnoreCase(Build.BRAND)) {
            openOptionsMenu();
            return true;
        }
        openOptionsMenu();
        return true;
    }


    @SuppressLint({"WrongConstant"})
    public void getDeletedContacts(String isDeleted, ListView lvDeletedContacts, TextView txtNoContacts) {
        if (isDeleted.equalsIgnoreCase("1")) {
            ArrayList<ContactModel> deletedList = myDeletedContactList;
            deletedContactsAdapter = new DeletedContactsAdapter(this, deletedList);
            lvDeletedContacts.setAdapter(deletedContactsAdapter);
            if (deletedList.size() > 0) {
                txtNoContacts.setVisibility(8);
                lvDeletedContacts.setVisibility(0);
                return;
            }
            txtNoContacts.setVisibility(0);
            lvDeletedContacts.setVisibility(8);
            txtNoContacts.setText(getResources().getString(R.string.no_deleted_contacts));
            return;
        }
        ArrayList<ContactModel> allContactList = myAllContactList;
        allContactsAdaper = new AllContactsAdapter(this, allContactList);
        lvDeletedContacts.setAdapter(allContactsAdaper);
        if (allContactList.size() > 0) {
            txtNoContacts.setVisibility(8);
            lvDeletedContacts.setVisibility(0);
            return;
        }
        txtNoContacts.setVisibility(0);
        lvDeletedContacts.setVisibility(8);
        txtNoContacts.setText(getResources().getString(R.string.no_all_contacts));
    }

    public void onClick(View v) {
        if (v == btnAllContacts) {
            pager.setCurrentItem(0);
        } else if (v == btnDeletedContacts) {
            pager.setCurrentItem(1);
        }
    }

    protected void showDialog() {
        if (mProgressDialog == null) {
            setProgressDialog();
        }
        mProgressDialog.show();
    }

    protected void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void setProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("");
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
    }

    public Bitmap getPhoto(long userId) {
        ContentResolver cr = getContentResolver();
        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, userId);
        Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.user);
        if (photoUri == null) {
            return defaultPhoto;
        }
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, photoUri);
        if (input != null) {
            return BitmapFactory.decodeStream(input);
        }
        return defaultPhoto;
    }

    public void showToast(ContactActivity contactActivity, String string) {
        Toast t = Toast.makeText(contactActivity, string, 0);
        t.setGravity(17, 0, 0);
        t.show();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        return !Character.isUpperCase(first) ? Character.toUpperCase(first) + s.substring(1) : s;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void onResume() {
        super.onResume();
    }


    public void addBanner() {


        final AdView mAdView = new AdView(ContactActivity.this);
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
