package fr.easyrecover.fre.common;

import android.os.Environment;


import fr.easyrecover.fre.activity.ImageActivity;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class Utils {

    public static int MIN_FILE_SIZE = 100;
    public static String[] encrypted_list = new String[]{"hm+rh7Ztxrw=", "6c5e9lUyL8o=", "aah+a7EdFuA=", "32lkJZPb2gqqdfgieUgdoA==", "SfYAlLKlOAM=", "u20z4bmZHkA=", "GvybyhO4CSQ=", "7z63YuObWdaNZqeoXZf3C2WMQ0iB2eDERkPOi5ouYlEELTgX6SqWghH6xyt1YY7jG9Q/WIWySx4=", "bIEUQPAYQIA=", "vn2E9WISPdg=", "uXgF97JlScM=", "5RtC1SSHstZhNx5o5dJJenfmf37Xwxr0"};

    public static HashMap<String, ArrayList<String>> getImagesFromFolder(String path) {
        try {
            HashMap<String, ArrayList<String>> results = new HashMap();
            ArrayList<String> listImage = new ArrayList();
            for (File file : new File(path).listFiles()) {
                if (isImageFile(file)) {
                    listImage.add(file.getAbsolutePath());
                } else if (isVideoFile(file)) {
                    listImage.add(file.getAbsolutePath());
                }
            }
            if (listImage.size() <= 0) {
                return null;
            }
            results.put(path, listImage);
            return results;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isImageFile(File file) {
        if (file != null) {
            try {
                if (file.exists()) {
                    file.setReadable(true);
                    String name = file.getName();
                    if (name.toLowerCase().contains(string_decrypt(encrypted_list[0])) || name.toLowerCase().contains(string_decrypt(encrypted_list[1])) || name.toLowerCase().contains(string_decrypt(encrypted_list[2])) || name.toLowerCase().contains(string_decrypt(encrypted_list[3])) || name.toLowerCase().contains(string_decrypt(encrypted_list[4])) || name.toLowerCase().contains(string_decrypt(encrypted_list[5])) || name.toLowerCase().contains(string_decrypt(encrypted_list[6]))) {
                        if (file.length() > ((long) MIN_FILE_SIZE)) {
                            return true;
                        }
                    } else if (!name.contains(".") && file.length() > ((long) MIN_FILE_SIZE)) {
                        try {
                            if (isJPEG(file).booleanValue() || isPNG(file)) {
                                return true;
                            }
                        } catch (Exception e) {
                            return false;
                        }
                    }
                }
            } catch (Exception e2) {
                return false;
            }
        }
        return false;
    }

    public static boolean isVideoFile(File file) {
        if (file != null) {
            try {
                if (file.exists()) {
                    file.setReadable(true);
                    String name = file.getName();
                    if (name.toLowerCase().contains(".mp4") || name.toLowerCase().contains(".avi") || name.toLowerCase().contains(".mkv") || name.toLowerCase().contains(".flv") || name.toLowerCase().contains(".gif") || name.toLowerCase().contains(".3gp")) {
                        if (file.length() > ((long) MIN_FILE_SIZE)) {
                            return true;
                        }
                    } else if (!name.contains(".") && file.length() > ((long) MIN_FILE_SIZE)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static Boolean isJPEG(File r15) {
        throw new UnsupportedOperationException("Method not decompiled: kitzone.deletedphotosrestore.Utils.isJPEG(java.io.File):java.lang.Boolean");
    }

    public static boolean isPNG(File r18) {
        throw new UnsupportedOperationException("Method not decompiled: kitzone.deletedphotosrestore.Utils.isPNG(java.io.File):boolean");
    }

    public static HashSet<String> getExternalMounts() {

        HashSet<String> out = new HashSet();
        String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
        String s = "";

        try {
            Process process = new ProcessBuilder(new String[0]).command(new String[]{string_decrypt(encrypted_list[8])}).redirectErrorStream(true).start();
            process.waitFor();
            InputStream is = process.getInputStream();
            byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                s = s + new String(buffer);
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String line : s.split("\n")) {
            if (!line.toLowerCase(Locale.US).contains(string_decrypt(encrypted_list[9])) && line.matches(reg)) {
                for (String part : line.split(" ")) {
                    if (part.startsWith("/") && !part.toLowerCase(Locale.US).contains(string_decrypt(encrypted_list[10]))) {
                        out.add(part);
                    }
                }
            }
        }
        out.add(Environment.getExternalStorageDirectory().getAbsolutePath());
        return out;
    }

    public static String string_decrypt(String src) {

        try {

            return new String(DES.decrypt(Base64.decode(src), ImageActivity.PKG.getBytes()));

        } catch (Exception e) {

            return "";
        }
    }
}
