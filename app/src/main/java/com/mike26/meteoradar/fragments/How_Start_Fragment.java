/*
Copyright © 2018 Alexey Lepskii | nobodypro.ru
All rights reserved.
*/

package com.mike26.meteoradar.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mike26.meteoradar.R;
import com.mike26.meteoradar.helpers.Helpers;

public class How_Start_Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_how_start, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getActivity().getString(R.string.how_start_title) + " - " + getActivity().getString(R.string.app_name));

        //Загрузка логотипа в зависимости от ориентации экрана
        ImageView logo = (ImageView) view.findViewById(R.id.logo_howstart);
        if (Helpers.getScreenOrientation(getActivity()) == 1)
        {
            logo.setImageResource(R.drawable.logo);
        } else {
            logo.setImageResource(R.drawable.logo_land);
        }

        return view;
    }
}