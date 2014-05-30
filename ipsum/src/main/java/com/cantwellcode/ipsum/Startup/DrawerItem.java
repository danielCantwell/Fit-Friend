package com.cantwellcode.ipsum.Startup;

/**
 * Created by Daniel on 4/11/2014.
 */
public class DrawerItem {

    public String title;
    public int iconRes;
    public boolean isHeader;

    public DrawerItem(String title, boolean header) {
        this.title = title;
        this.isHeader = header;
    }

    public DrawerItem(String title, int iconRes) {
        this.title = title;
        this.iconRes = iconRes;
        this.isHeader = false;
    }
}
