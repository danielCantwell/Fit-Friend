package com.cantwellcode.fitfriend.startup;

/**
 * Created by Daniel on 8/8/2014.
 */
public class DrawerItem {

    public String text;
    public int iconRes;
    public boolean primary;

    public DrawerItem(String text) {
        this.text = text;
        primary = true;
    }

    public DrawerItem(String text, int iconRes) {
        this.text = text;
        this.iconRes = iconRes;
        primary = false;
    }
}
