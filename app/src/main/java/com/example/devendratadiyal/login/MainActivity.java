package com.example.devendratadiyal.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class MainActivity extends AppCompatActivity {

    private CallbackManager cm;
    private TextView tv;
    private AccessTokenTracker att;
    private ProfileTracker pt;
    LoginButton lb;
    ImageView iv;
    FacebookCallback<LoginResult> cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.t);
        iv = (ImageView) findViewById(R.id.imageView);
        lb = (LoginButton) findViewById(R.id.login_button);

        //Deprecated
        FacebookSdk.sdkInitialize(getApplicationContext());

        //Now we are using latest callbackmanager api so no need to initialize facebook
        cm = CallbackManager.Factory.create();
        att = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

        pt = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                displayMessage(currentProfile);
            }
        };
        att.startTracking();
        pt.startTracking();

        cb = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken at = loginResult.getAccessToken();
                Profile p =Profile.getCurrentProfile();
                displayMessage(p);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };

        lb.setReadPermissions("user_friends");
        lb.registerCallback(cm,cb);


    }
@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
super.onActivityResult(requestCode,resultCode,data);
        cm.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        att.stopTracking();
        pt.stopTracking();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        Profile pp = Profile.getCurrentProfile();
        displayMessage(pp);
    }

    private void displayMessage(Profile p)
    {
        if(p!=null)
        {
            tv.setText(p.getName());
            String url = p.getProfilePictureUri(150,150).toString();
            Glide.with(getApplicationContext()).load(url).error(R.mipmap.ic_launcher).into(iv);
        }
    }
}
