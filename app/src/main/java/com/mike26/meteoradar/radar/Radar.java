/*
Copyright © 2018 Alexey Lepskii | nobodypro.ru
All rights reserved.
*/

package com.mike26.meteoradar.radar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class Radar {

  private Context mContext;
  private String mMsgError;

  private ImageView mRadarImageView; //Ссылка на ImageView в который грузится радар
  private ImageView mWindImageView;  //Ссылка на ImageView в который грузится скорость ветра
  private ImageView mTimeRadarImage; //Ссылка на ImageView в который грузится время получения снимка

  private ProgressBar mProgressBar;

  public static final String APP_PREFERENCES_RD = "radarDefault";
  public static final String APP_PREFERENCES_RDN = "radarDefaultName";

  //Проверка на соединение с интернетом
  private boolean isNetworkAvailable(Context context) {
    return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
        .getActiveNetworkInfo() != null;
  }

  //Конструктор класса
  public Radar(Context context, ImageView radarImageView, ImageView windImageView,
      ImageView timeRadarImage, ProgressBar progressBar, String msgError) {
    mContext = context;
    mRadarImageView = radarImageView;
    mWindImageView = windImageView;
    mTimeRadarImage = timeRadarImage;
    mProgressBar = progressBar;
    mMsgError = msgError;
  }

  //Загрузка радара в ImageView
  public void loadRadar(String RadarUrlName) {
    if (isNetworkAvailable(mContext)) {
      //Скрываем картинки с радаром, ветром и датой обновления
      mRadarImageView.setVisibility(View.INVISIBLE);
      mWindImageView.setVisibility(View.INVISIBLE);
      mTimeRadarImage.setVisibility(View.INVISIBLE);
      //показываем ProgressBar
      mProgressBar.setVisibility(View.VISIBLE);

      Picasso.with(mContext)
          .load("http://www.meteorad.ru/data/" + RadarUrlName + ".png")
          .memoryPolicy(MemoryPolicy.NO_CACHE)
          .networkPolicy(NetworkPolicy.NO_CACHE)
          .into(target);
    } else {
      Toast.makeText(mContext, mMsgError, Toast.LENGTH_LONG).show();
    }
  }

  //Метод для ресайза bitmap
  public Bitmap resizeBitmap(Bitmap bm, int newWidth, int newHeight) {
    int width = bm.getWidth();
    int height = bm.getHeight();
    float scaleWidth = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;
    // CREATE A MATRIX FOR THE MANIPULATION
    Matrix matrix = new Matrix();
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight);

    // "RECREATE" THE NEW BITMAP
    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    return resizedBitmap;
  }


  final Target target = new Target() {
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
      mProgressBar.setVisibility(View.GONE);//скрываем ProgressBar

      //Делаем видимыми картинки с радаром, ветром и датой обновления
      mRadarImageView.setVisibility(View.VISIBLE);
      mWindImageView.setVisibility(View.VISIBLE);
      mTimeRadarImage.setVisibility(View.VISIBLE);

      //Сохраняем оригинал на всякий случай и делаем ресайз к нужному размеру
      //Сделано на тот случай если в обозримом будущем изменят размер картинки
      Bitmap raw_radar = resizeBitmap(bitmap, 1855, 1036);

      Bitmap radar = Bitmap.createBitmap(raw_radar, 185, 55, 1180, 962);
      mRadarImageView.setImageBitmap(radar);
      Bitmap wind = Bitmap.createBitmap(raw_radar, 1682, 39, 170, 16);
      mWindImageView.setImageBitmap(wind);
      Bitmap time = Bitmap.createBitmap(raw_radar, 1682, 151, 145, 44);
      mTimeRadarImage.setImageBitmap(time);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
      Toast.makeText(mContext, mMsgError, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
  };
}