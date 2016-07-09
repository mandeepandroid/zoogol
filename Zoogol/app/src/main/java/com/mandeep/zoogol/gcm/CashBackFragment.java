package com.mandeep.zoogol.gcm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;

/**
 * Created by mandeep on 6/5/2016.
 */
public class CashBackFragment extends Fragment {

    private View view;
    private ListView listView;
    private CashBackAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notification_fragment, container, false);

        listView = (ListView) view.findViewById(R.id.listView);

        customAdapter = new CashBackAdapter(getActivity(), HomeActivity.cashBackNotificationBean);
        listView.setAdapter(customAdapter);


        return view;
    }
}
