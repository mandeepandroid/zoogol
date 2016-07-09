package com.mandeep.zoogol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mandeep on 5/21/2016.
 */
public class HomeFragmentDrawer2 extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    View view1, view2, view3;

    /* public static final FirstFragment newInstance(String message)
     {
         FirstFragment f = new FirstFragment();
         Bundle bdl = new Bundle(1);
         bdl.putString(EXTRA_MESSAGE, message);
         f.setArguments(bdl);
         return f;
     }
 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.second_drawer, container, false);



        return v;
    }

}