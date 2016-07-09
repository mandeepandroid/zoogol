package com.mandeep.zoogol.authentication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mandeep.zoogol.R;

/**
 * Created by mandeep on 3/21/2016.
 */
public class ThirdFragment extends Fragment {
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
        View v = inflater.inflate(R.layout.third_fragment, container, false);



        return v;
    }

}