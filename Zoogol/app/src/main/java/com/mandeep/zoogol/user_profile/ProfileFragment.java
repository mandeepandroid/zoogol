package com.mandeep.zoogol.user_profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;
import com.mandeep.zoogol.help.RequestCallBackFragment;
import com.mandeep.zoogol.utils.Const;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by mandeep on 3/23/2016.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout rl_myPurchse, rl_myCashback, rl_myPartner, rl_myZoogol, rl_upload_puchase, rl_untracced_purchase;
    private View view;
    private TextView tv_nameZMP, tv_status, tv_zkey, tv_location;

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String TAG = HomeActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefs;
    private ImageView iv_navigationDrawer;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    public static CircularImageView circular_profilePic;
    private ImageView iv_edit_picture;
    private Intent pictureActionIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);

        initView();
        return view;
    }

    private void initView() {

        HomeActivity.rl_topbar.setVisibility(View.VISIBLE);
        iv_edit_picture = (ImageView) view.findViewById(R.id.iv_edit_picture);
        iv_edit_picture.setOnClickListener(this);

        iv_navigationDrawer = (ImageView) view.findViewById(R.id.iv_navigationDrawer);
        iv_navigationDrawer.setOnClickListener(this);

        rl_myPurchse = (RelativeLayout) view.findViewById(R.id.rl_myPurchse);
        rl_myPurchse.setOnClickListener(this);

        rl_myCashback = (RelativeLayout) view.findViewById(R.id.rl_myCashback);
        rl_myCashback.setOnClickListener(this);

        rl_myPartner = (RelativeLayout) view.findViewById(R.id.rl_myPartner);
        rl_myPartner.setOnClickListener(this);

        rl_myZoogol = (RelativeLayout) view.findViewById(R.id.rl_myZoogol);
        rl_myZoogol.setOnClickListener(this);

        rl_upload_puchase = (RelativeLayout) view.findViewById(R.id.rl_upload_puchase);
        rl_upload_puchase.setOnClickListener(this);

        rl_untracced_purchase = (RelativeLayout) view.findViewById(R.id.rl_untracced_purchase);
        rl_untracced_purchase.setOnClickListener(this);

        mPrefs = getActivity().getSharedPreferences("Zoogol", getActivity().MODE_PRIVATE);
        tv_nameZMP = (TextView) view.findViewById(R.id.tv_nameZMP);
        tv_status = (TextView) view.findViewById(R.id.tv_status);
        tv_zkey = (TextView) view.findViewById(R.id.tv_zkey);

        tv_nameZMP.setText(mPrefs.getString("fname", ""));
        tv_status.setText(mPrefs.getString("status", ""));
        tv_zkey.setText("Z Key - " + mPrefs.getString("zkey", ""));

        circular_profilePic = (CircularImageView) view.findViewById(R.id.circular_profilePic);
        Picasso.with(getActivity()).load(Const.IMAGE_BASE_URL + mPrefs.getString("profilepick", "")).placeholder(R.drawable.user_pic)
                .error(R.drawable.user_pic).into(circular_profilePic);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.rl_myPurchse:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                PurchaseFragment purchaseFragment = new PurchaseFragment();
                HomeActivity.profileFragmentStack.add(purchaseFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, purchaseFragment);
                mFragmentTransaction.commit();
                break;

            case R.id.rl_myCashback:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                CashBackFragment cashBackFragment = new CashBackFragment();
                HomeActivity.profileFragmentStack.add(cashBackFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, cashBackFragment);
                mFragmentTransaction.commit();
                break;

            case R.id.rl_myPartner:
                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                MyPartnersFragment myPartnersFragment = new MyPartnersFragment();
                HomeActivity.profileFragmentStack.add(myPartnersFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, myPartnersFragment);
                mFragmentTransaction.commit();

                break;

            case R.id.rl_myZoogol:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                MyZoogolFragment myZoogolFragment = new MyZoogolFragment();
                HomeActivity.profileFragmentStack.add(myZoogolFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, myZoogolFragment);
                mFragmentTransaction.commit();
                break;

            case R.id.rl_upload_puchase:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                UploadPurchaseFragment uploadPurchaseFragment = new UploadPurchaseFragment();
                HomeActivity.profileFragmentStack.add(uploadPurchaseFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, uploadPurchaseFragment);
                mFragmentTransaction.commit();
                break;

            case R.id.rl_untracced_purchase:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                UntracedPurchaseFragment untracedPurchaseFragment = new UntracedPurchaseFragment();
                HomeActivity.profileFragmentStack.add(untracedPurchaseFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, untracedPurchaseFragment);
                mFragmentTransaction.commit();
                break;


            case R.id.iv_navigationDrawer:

                HomeActivity.mDrawerLayout.openDrawer(HomeActivity.ll_drawer_list);

                break;

            case R.id.iv_edit_picture:

                startDialog();
                break;


        }
    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                pictureActionIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                pictureActionIntent.setType("image/*");
                pictureActionIntent.putExtra("return-data", true);
                getActivity().startActivityForResult(pictureActionIntent, HomeActivity.GALLERY_PICTURE);


            }

        });

        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(pictureActionIntent, HomeActivity.CAMERA_REQUEST);

            }
        });
        myAlertDialog.show();
    }
}