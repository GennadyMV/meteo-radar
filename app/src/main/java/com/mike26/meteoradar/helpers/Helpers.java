/*
Copyright © 2018 Alexey Lepskii | nobodypro.ru
All rights reserved.
*/

package com.mike26.meteoradar.helpers;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class Helpers {

  /**
   * getScreenOrientation - возвращает текущую ориентацию экрана. 1 или ORIENTATION_PORTRAIT -
   * вертикальная ориентация экрана 2 или ORIENTATION_LANDSCAPE - горизонтальная ориентация экрана
   *
   * Доступно с API Level 1
   */
  public static int getScreenOrientation(Context context) {
    return ((Activity) context).getResources().getConfiguration().orientation;
  }


  /**
   * isNetworkAvailable - проверяет наличие активного соединения с сетью и возвращает true если
   * такое имеется.
   *
   * Доступно с API Level 1 Требует разрешения <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"
   * />
   */
  public static boolean isNetworkAvailable(Context context) {
    return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
        .getActiveNetworkInfo() != null;
  }

  /**
   * setTitle - меняет заголовок в ActionBar
   *
   * Доступно с API Level 11
   */
  public static void setTitle(Context context, CharSequence title) {
    ActionBar actionBar = ((Activity) context).getActionBar();
    if (actionBar != null) {
      actionBar.setTitle(title);
    }
  }
}
