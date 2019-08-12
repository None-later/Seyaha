package com.example.seyaha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "anas";

    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;
    //firebase
    private FirebaseAuth mFirebaseAuth;
    private  FirebaseAuth.AuthStateListener mFirebaseAuthListner;
    private FirebaseUser user;
    private final  int RC_SIGN_IN=1;
    private boolean internet_checked=false;
    private boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        coordinatorLayout=findViewById(R.id.coordinator);
        checkInternet();
        firebaseLogin();
    }
    private void start_activity_with_delay()
    {
        // here if internet is connected
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                Intent i=new Intent(SplashScreenActivity.this, TourActivity.class);
                startActivity(i);
            }
        },2000);
    }
    private void start_activity()
    {
        Intent i=new Intent(SplashScreenActivity.this, TourActivity.class);
        startActivity(i);
    }
    private void checkInternet()
    {
        if(!SeyahaUtils.checkInternetConnectivity(this))
        {
            showSnackBar();
        }
        else
        {
            internet_checked=true;
        }
    }

    private void firebaseLogin() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {

                    flag = true;
                    List <AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build(), new AuthUI.IdpConfig.GoogleBuilder().build());

                    AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout.Builder(R.layout.activity_login).setGoogleButtonId(R.id.gmail_btn).setFacebookButtonId(R.id.facbook_btn).build();

                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).setIsSmartLockEnabled(false).setTheme(R.style.AppThemeFirebaseAuth).setAuthMethodPickerLayout(customLayout).build(), RC_SIGN_IN);
                } else {
                    if (flag)
                    {
                        start_activity();
                    }
                    else
                        start_activity_with_delay();
                }
            }
        };
    }
    private void showSnackBar()
    {
        snackbar=Snackbar.make(coordinatorLayout,getResources().getString(R.string.lost_internet),Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if (snackbar.isShown())
                        {
                            snackbar.dismiss();
                        }
                        checkInternet();
                        onResume();
                    }
                });
        snackbar.show();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        if (internet_checked) {
            mFirebaseAuth.addAuthStateListener(mFirebaseAuthListner);
        }
        else
            showSnackBar();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListner);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RESULT_OK)
        {
            Log.d(TAG, "onActivityResult: intered ");
            start_activity();
        }
        else if(resultCode==RESULT_CANCELED)
        {
            Toast.makeText(this, "sign in canceled", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

