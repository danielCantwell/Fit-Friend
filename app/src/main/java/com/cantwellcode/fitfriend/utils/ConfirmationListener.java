package com.cantwellcode.fitfriend.utils;

/**
 * Created by danielCantwell on 4/10/15.
 */
public interface ConfirmationListener {
    void onNo(String msg);
    void onYes(String msg);
}
