package com.cnwir.gongxin.ui.loading;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Windows on 2015/6/29.
 */
public class GuideFragment extends Fragment{

    private  static final String LAYOUT_ID = "layout_id";


    public static GuideFragment newInstance(int layoutId){

            GuideFragment gf = new GuideFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID, layoutId);
        gf.setArguments(bundle);

        return gf;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getArguments().getInt(LAYOUT_ID), container, false);
        return  view;
    }
}
