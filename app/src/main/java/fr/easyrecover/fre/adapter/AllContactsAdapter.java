package fr.easyrecover.fre.adapter;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import fr.easyrecover.fre.R;
import fr.easyrecover.fre.model.ContactModel;

import java.io.InputStream;
import java.util.ArrayList;

@SuppressLint({"InflateParams"})
public class AllContactsAdapter extends BaseAdapter {

    ArrayList<ContactModel> allContactList = new ArrayList();
    Context mContext;

    public class ViewHolder {
        ImageView imgProfile;
        TextView txtName;
    }

    public AllContactsAdapter(Context context, ArrayList<ContactModel> allcontactlist) {
        this.mContext = context;
        this.allContactList = allcontactlist;
    }

    public int getCount() {
        return this.allContactList.size();
    }

    public Object getItem(int paramInt) {
        return this.allContactList.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return (long) paramInt;
    }

    @SuppressLint({"WrongConstant"})
    public View getView(int position, View convertView, ViewGroup paramViewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.row_contact, null);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.imgProfile = (ImageView) convertView.findViewById(R.id.img_profile);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtName.setText(((ContactModel) this.allContactList.get(position)).getContactName());
        holder.imgProfile.setImageBitmap(getPhoto(Long.parseLong(((ContactModel) this.allContactList.get(position)).getContactContactId())));
        return convertView;
    }

    private Bitmap getPhoto(long userId) {
        ContentResolver cr = this.mContext.getContentResolver();
        Uri photoUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, userId);
        Bitmap defaultPhoto = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.user);
        if (photoUri == null) {
            return defaultPhoto;
        }
        InputStream input = Contacts.openContactPhotoInputStream(cr, photoUri);
        if (input != null) {
            return BitmapFactory.decodeStream(input);
        }
        return defaultPhoto;
    }
}
