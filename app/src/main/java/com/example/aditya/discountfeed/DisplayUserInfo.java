package com.example.aditya.discountfeed;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by radha on 3/29/2017.
 */

public class DisplayUserInfo extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
//this approch also didnt work
    private ImageView photoImageView;
    private TextView nameTextView;
    private TextView locationTextView;

    private GoogleApiClient googleApiClient;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_main);

        photoImageView=(ImageView)findViewById(R.id.profile_image);
        nameTextView= (TextView)findViewById(R.id.username);
        locationTextView= (TextView)findViewById(R.id.mylocation);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){
            String name=user.getDisplayName();
            String email=user.getEmail();
            Uri photoUrl=user.getPhotoUrl();
            String uid=user.getUid();

            nameTextView.setText(name);
            locationTextView.setText(email);
            photoImageView.setImageURI(photoUrl);
        }

        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

    }


    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr =Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    handleSignInResult(result);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            nameTextView.setText(account.getDisplayName());
            locationTextView.setText(account.getEmail());
            Log.d("MIAPP",account.getPhotoUrl().toString());
            Glide.with(this).load(account.getPhotoUrl()).into(photoImageView);

        }else{

        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}

