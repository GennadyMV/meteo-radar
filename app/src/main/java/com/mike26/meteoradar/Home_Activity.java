/*
Copyright © 2018 Alexey Lepskii | nobodypro.ru
All rights reserved.
*/

package com.mike26.meteoradar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mike26.meteoradar.fragments.Help_Color_Fragment;
import com.mike26.meteoradar.fragments.How_Start_Fragment;
import com.mike26.meteoradar.fragments.Radar_Fragment;
import com.mike26.meteoradar.radar.Radar;

public class Home_Activity extends AppCompatActivity {

  private String[] mNameRadar;//Переменная со списком доступных радаров для NavigationDrawer
  private String[] mUNameRadar;//Переменная со списком названий png радаров на сервере

  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;
  private LinearLayout mDrawer;

  private Fragment mFragment;
  private FragmentManager mFragmentManager;

  public boolean isDrawerLocked = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    mFragmentManager = getFragmentManager();

    initDrawer();

    if (savedInstanceState == null) {

      SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

      if (sharedPreferences.contains(Radar.APP_PREFERENCES_RD)) {
        mFragment = mFragmentManager.findFragmentByTag("radarFragment");

        if (mFragment != null) {
          mFragmentManager.beginTransaction().remove(mFragment);
        }

        mFragment = new Radar_Fragment();

        Bundle args = new Bundle();
        args.putString("RadarNameUrl", sharedPreferences.getString(Radar.APP_PREFERENCES_RD, ""));
        args.putString("RadarName", sharedPreferences.getString(Radar.APP_PREFERENCES_RDN, ""));
        mFragment.setArguments(args);

        mFragmentManager.beginTransaction().replace(R.id.content_frame, mFragment, "radarFragment")
            .commit();
      } else {
        mFragment = mFragmentManager.findFragmentByTag("howStartFragment");
        if (mFragment == null) {
          mFragment = new How_Start_Fragment();
        }
        mFragmentManager.beginTransaction()
            .replace(R.id.content_frame, mFragment, "howStartFragment").commit();
      }
    }
  }


  //Инициализация всего всего для NavigationDrawer
  private void initDrawer() {
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);
    mDrawer = (LinearLayout) findViewById(R.id.drawer_view);

    FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);

    if (((ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams()).leftMargin
        == (int) getResources().getDimension(R.dimen.drawer_size)) {
      mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, mDrawer);
      mDrawerLayout.setScrimColor(Color.TRANSPARENT);
      isDrawerLocked = true;
    }

    mDrawerToggle = new ActionBarDrawerToggle(
        this,
        mDrawerLayout,
        com.mike26.meteoradar.R.string.drawer_open,
        com.mike26.meteoradar.R.string.drawer_close);

    if (isDrawerLocked) {
      mDrawerToggle.setDrawerIndicatorEnabled(false);
      getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    if (!isDrawerLocked) {
      mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
          mDrawerToggle.setDrawerIndicatorEnabled(false);
          getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
      });
      mDrawerLayout.setDrawerListener(mDrawerToggle);
      mDrawerLayout.setDrawerShadow(com.mike26.meteoradar.R.drawable.drawer_shadow, Gravity.START);
    }

    //Получаем список радаров из ресурсов values\strings.xml для списка
    mNameRadar = getResources().getStringArray(R.array.name_radar_array);
    //Получаем список радаров из ресурсов values\strings.xml для формирования url
    mUNameRadar = getResources().getStringArray(R.array.name_url_radar_array);
    //Заполняем ListView списком и назначаем стиль пункта drawer_list_item
    mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mNameRadar));
    //Вешаем обработчик на нажатие пункта в ListView
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.home_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.how_start) {
      mFragment = mFragmentManager.findFragmentByTag("howStartFragment");
      if (mFragment == null) {
        mFragment = new How_Start_Fragment();
      }
      mFragmentManager.beginTransaction().replace(R.id.content_frame, mFragment, "howStartFragment")
          .addToBackStack("howStartFragment").commit();

      return true;
    }

    if (id == com.mike26.meteoradar.R.id.map_color) {
      mFragment = mFragmentManager.findFragmentByTag("helpColorFragment");
      if (mFragment == null) {
        mFragment = new Help_Color_Fragment();
      }
      mFragmentManager.beginTransaction()
          .replace(R.id.content_frame, mFragment, "helpColorFragment")
          .addToBackStack("helpColorFragment").commit();

      return true;
    }

    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  //Закрываем NavigationDrawer по нажатию системной кнопки «Назад»
  @Override
  public void onBackPressed() {
    if (!isDrawerLocked & mDrawerLayout.isDrawerOpen(mDrawer)) {
      mDrawerLayout.closeDrawer(mDrawer);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    mDrawerToggle.onConfigurationChanged(newConfig);
    super.onConfigurationChanged(newConfig);
  }


  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    mDrawerToggle.syncState();
    super.onPostCreate(savedInstanceState);
  }


  private class DrawerItemClickListener implements ListView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
      selectItem(pos);
    }

    private void selectItem(int pos) {
      mFragment = mFragmentManager.findFragmentByTag("radarFragment");
      if (mFragment != null) {
        mFragmentManager.beginTransaction().remove(mFragment);
      }

      mFragment = new Radar_Fragment();

      Bundle args = new Bundle();
      args.putString("RadarNameUrl", mUNameRadar[pos]);
      args.putString("RadarName", mNameRadar[pos]);
      mFragment.setArguments(args);

      mFragmentManager.beginTransaction().replace(R.id.content_frame, mFragment, "radarFragment")
          .addToBackStack("radarFragment").commit();

      mDrawerList.setItemChecked(pos, true);

      if (!isDrawerLocked) {
        mDrawerLayout.closeDrawer(mDrawer);
      }
    }
  }
}
