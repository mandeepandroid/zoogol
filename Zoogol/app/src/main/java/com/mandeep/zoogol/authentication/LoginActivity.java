package com.mandeep.zoogol.authentication;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;
import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;
import com.mandeep.zoogol.app.AppController;
import com.mandeep.zoogol.authentication.ForgotPassword;
import com.mandeep.zoogol.authentication.SignUpActivity;
import com.mandeep.zoogol.gcm.GCMClientManager;
import com.mandeep.zoogol.utils.ConnectionDetector;
import com.mandeep.zoogol.utils.Const;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mandeep on 2/28/2016.
 */

public class LoginActivity extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final int PICK_MEDIA_REQUEST_CODE = 8;
    private static final int SHARE_MEDIA_REQUEST_CODE = 9;
    private static final int SIGN_IN_REQUEST_CODE = 10;
    private static final int ERROR_DIALOG_REQUEST_CODE = 11;


    // For communicating with Google APIs
    private GoogleApiClient mGoogleApiClient;
    private boolean mSignInClicked;
    private boolean mIntentInProgress;
    // contains all possible error codes for when a client fails to connect to
    // Google Play services
    private ConnectionResult mConnectionResult;

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefs;

    // CHECK INTERNET CONNECTION
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    //Facebook callback
    private CallbackManager callbackManager;
    String PROJECT_NUMBER = "82557879609";
    private String deviceID;
    private EditText et_email, et_password;
    private TextView tv_forgot_password, tv_signup;
    private Button btn_signin_button, btn_facebook, btn_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        gcmInitialisation();

        initView();
        facebookLogin();
        googlePLusInitialise();

    }


    private void gcmInitialisation() {

        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {

                Log.d("Registration id", registrationId);
                deviceID = registrationId;
                //send this registrationId to your server
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
    }

    private void googlePLusInitialise() {


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    private void initView() {

        //cd = new ConnectionDetector(jh.this);
        //isInternetPresent = cd.isConnectingToInternet();
        mPrefs = getSharedPreferences("Zoogol", MODE_PRIVATE);

        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        tv_forgot_password.setOnClickListener(this);
        tv_signup = (TextView) findViewById(R.id.tv_signup);
        tv_signup.setOnClickListener(this);

        btn_signin_button = (Button) findViewById(R.id.btn_signin);
        btn_signin_button.setOnClickListener(this);
        btn_facebook = (Button) findViewById(R.id.btn_facebook);
        btn_facebook.setOnClickListener(this);
        btn_google = (Button) findViewById(R.id.btn_google);
        btn_google.setOnClickListener(this);

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
    }

    private void showProgressDialog() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    private void facebookLogin() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.e("ONSUCCESS", "User ID: " + loginResult.getAccessToken().getUserId()
                        + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken()

                );

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                try {

                                    //str_facebook_name = object.getString("name");
                                    // str_facebook_email = object.getString("email");

                                    /*if (isInternetPresent) {

                                    }else{
                                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                                    }*/

                                } catch (Exception e) {

                                }
                              /*String [] str = str_facebook_name.split(" ");

                                str_facebook_first = str[0];
                               str_facebook_last = str[1];

                               Log.e("str_facebook_first", str_facebook_first+"");
                                Log.e("str_facebook_last", str_facebook_last + "");

                                if (str_facebook_email!=null) {

                                    new httpAsyncTask_facebooklogin().execute(Rest_Constants.str_Login_Facebook);
                                }else {
                                    dialogDelivery();
                                }*/
                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");

                request.setParameters(parameters);
                request.executeAsync();

            }


            @Override
            public void onCancel() {
                Log.e("Login Access", "Login cancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("ON ERROR", "Login attempt failed.");
            }
        });
    }

    /**
     * Making json object request
     */
    private void makeJsonLoginRequest() {

        showProgressDialog();
        String login_url = Const.LOGIN_URL + "username=" + et_email.getText().toString() + "&password="
                + et_password.getText().toString()
                + "&deviceid=" + deviceID
                + "&type=json";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, login_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                try {
                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONObject mainObject = response.getJSONObject("data");
                        mPrefs = getSharedPreferences("Zoogol", MODE_PRIVATE);
                        mEditor = mPrefs.edit();
                        mEditor.putString("id", mainObject.getString("id"));
                        mEditor.putString("email", mainObject.getString("email"));
                        mEditor.putString("fname", mainObject.getString("fname"));
                        mEditor.putString("lname", mainObject.getString("lname"));
                        mEditor.putString("user_city", mainObject.getString("user_add"));
                        mEditor.putString("user_country", mainObject.getString("user_country"));
                        mEditor.putString("profilepick", mainObject.getString("profilepick"));
                        mEditor.putString("status", mainObject.getString("status"));
                        mEditor.putString("zkey", mainObject.getString("zkey"));
                        mEditor.putString("mobile", mainObject.getString("mobile"));


                        mEditor.commit();


                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else {

                        Toast.makeText(LoginActivity.this, "Invalid Email or password.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("store_id", "13");
                /*params.put("latitude", "30.7129809");
                params.put("longitude", "76.7091668");*/


                return params;
            }

        };
        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        jsonObjReq.setShouldCache(false);
        //Adding request to the queue
        requestQueue.add(jsonObjReq);
        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    public boolean fn_emailvalidation(EditText argEditText) {

        try {
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(argEditText.getText());
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private GoogleApiClient buildGoogleAPIClient() {
        return new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // make sure to initiate connection
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // disconnect api if it is connected
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_forgot_password:
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
                break;

            case R.id.btn_signin:
                makeJsonLoginRequest();
                break;

            case R.id.tv_signup:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            case R.id.btn_facebook:
                try {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
                } catch (Exception e) {

                }
                break;

            case R.id.btn_google:
                processSignIn();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else if (requestCode == PICK_MEDIA_REQUEST_CODE) {
            // If picking media is success, create share post using
            // PlusShare.Builder
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                ContentResolver cr = this.getContentResolver();
                String mime = cr.getType(selectedImage);

                PlusShare.Builder share = new PlusShare.Builder(this);
                share.setText("Hello from AndroidSRC.net");
                share.addStream(selectedImage);
                share.setType(mime);
                startActivityForResult(share.getIntent(),
                        SHARE_MEDIA_REQUEST_CODE);
            }
        }
    }

    /**
     * API to revoke granted access After revoke user will be asked to grant
     * permission on next sign in
     */
    private void processRevokeRequest() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Toast.makeText(getApplicationContext(), "User permissions revoked", Toast.LENGTH_LONG).show();
                            mGoogleApiClient = buildGoogleAPIClient();
                            mGoogleApiClient.connect();

                        }

                    });

        }

    }

    /**
     * API to process media post request start activity with MIME type as video
     * and image
     */
    private void processShareMedia() {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("video/*, image/*");
        startActivityForResult(photoPicker, PICK_MEDIA_REQUEST_CODE);

    }

    /**
     * API to process post share request Use PlusShare.Builder to create share
     * post.
     */
    private void processSharePost() {
        // Launch the Google+ share dialog with attribution to your app.
        Intent shareIntent = new PlusShare.Builder(this).setType("text/plain")
                .setText("Google+ Demo http://androidsrc.net").setContentUrl(Uri.parse("http://androidsrc.net")).getIntent();

        startActivityForResult(shareIntent, 0);

    }


    /**
     * API to handler sign in of user If error occurs while connecting process
     * it in processSignInError() api
     */
    private void processSignIn() {

        if (!mGoogleApiClient.isConnecting()) {
            processSignInError();
            mSignInClicked = true;
        }

    }

    /**
     * API to process sign in error Handle error based on ConnectionResult
     */
    private void processSignInError() {
        if (mConnectionResult != null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, SIGN_IN_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /**
     * Callback for GoogleApiClient connection failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    ERROR_DIALOG_REQUEST_CODE).show();
            return;
        }
        if (!mIntentInProgress) {
            mConnectionResult = result;

            if (mSignInClicked) {
                processSignInError();
            }
        }

    }

    /**
     * Callback for GoogleApiClient connection success
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        mSignInClicked = false;
        Toast.makeText(getApplicationContext(), "Signed In Successfully", Toast.LENGTH_LONG).show();

        processUserInfoAndUpdateUI();


    }

    /**
     * Callback for suspension of current connection
     */
    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();

    }

    /**
     * API to update signed in user information
     */
    private void processUserInfoAndUpdateUI() {
        Person signedInUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        if (signedInUser != null) {

            if (signedInUser.hasDisplayName()) {
                String userName = signedInUser.getDisplayName();
//                this.userName.setText("Name: " + userName);
                Log.e("Name", userName);
            }

            if (signedInUser.hasTagline()) {
                String tagLine = signedInUser.getTagline();
//                this.userTagLine.setText("TagLine: " + tagLine);
//                this.userTagLine.setVisibility(View.VISIBLE);
            }

            if (signedInUser.hasAboutMe()) {
                String aboutMe = signedInUser.getAboutMe();
//                this.userAboutMe.setText("About Me: " + aboutMe);
//                this.userAboutMe.setVisibility(View.VISIBLE);
            }

            if (signedInUser.hasBirthday()) {
                String birthday = signedInUser.getBirthday();
//                this.userBirthday.setText("DOB " + birthday);
//                this.userBirthday.setVisibility(View.VISIBLE);
            }

            if (signedInUser.hasCurrentLocation()) {
                String userLocation = signedInUser.getCurrentLocation();
//                this.userLocation.setText("Location: " + userLocation);
//                this.userLocation.setVisibility(View.VISIBLE);
            }

//            String userEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
//            Log.e("Email: ",userEmail);

            if (signedInUser.hasImage()) {
                String userProfilePicUrl = signedInUser.getImage().getUrl();
                // default size is 50x50 in pixels.changes it to desired size
                int profilePicRequestSize = 250;

                userProfilePicUrl = userProfilePicUrl.substring(0,
                        userProfilePicUrl.length() - 2) + profilePicRequestSize;

            }

        }
    }


}