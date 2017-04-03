package com.example.aditya.discountfeed;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Signin_fragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    private EditText mLoginEmailField;
    private EditText mLoginPasswordField;
    private Button mLoginBtn;
    private Button mSignupBtn;
    private Button mLogoutBtn;

    private SignInButton signInButton;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private GoogleApiClient googleApiClient;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private ProgressDialog mProgress;
    private DatabaseReference mDatabaseUsers;
    public static final int SIGN_IN_CODE = 777;

    private ImageView photoImageView;
    private TextView nameTextView;
    private TextView locationTextView;





    public Signin_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_fragment, container, false);
        //TextView textView = new TextView(getActivity());
        mLoginPasswordField = (EditText) view.findViewById(R.id.loginPasswordField);
        mLoginEmailField = (EditText) view.findViewById(R.id.loginEmailField);


        photoImageView=(ImageView)view.findViewById(R.id.profile_image);
        nameTextView= (TextView)view.findViewById(R.id.username);
        locationTextView= (TextView)view.findViewById(R.id.mylocation);



        mLoginBtn = (Button) view.findViewById(R.id.loginBtn);
        mSignupBtn = (Button) view.findViewById(R.id.newAccount);
        mLogoutBtn = (Button)view.findViewById(R.id.action_logout);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Signin_fragment.this.getActivity(), "Logining Please wait", Toast.LENGTH_SHORT).show();
                checkLogin();
                //Intent i= new Intent(Signin_fragment.this.getActivity(),RegisterActivity.class);
                //startActivity(i);
            }
        });
        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.newAccount) {
                    Toast.makeText(Signin_fragment.this.getActivity(), "Please Enter Your Information", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Signin_fragment.this.getActivity(), RegisterActivity.class);
                    startActivity(i);
                }
            }
        });
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.action_logout){
                    logout();
                }
            }
        });



    //facebook
        callbackManager= CallbackManager.Factory.create();

        loginButton= (LoginButton)view.findViewById(R.id.loginButton);
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //goMainScreen();
                handleFacebookAccessToken(loginResult.getAccessToken());
                goMainScreen();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"can not log In",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"error occured",Toast.LENGTH_SHORT).show();


            }
        });
        mAuth=FirebaseAuth.getInstance();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);

        mProgress=new ProgressDialog(Signin_fragment.this.getActivity());
        //google
        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(Signin_fragment.this.getActivity())
                .enableAutoManage(Signin_fragment.this.getActivity(),this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        signInButton=(SignInButton)view.findViewById(R.id.googleBtn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });
        firebaseAuthListener =new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                }

            }
        };

        return view;
    }

    public void logout(){
        mAuth.signOut();
        Toast.makeText(Signin_fragment.this.getActivity(), "Success", Toast.LENGTH_SHORT).show();
        //LoginManager.getInstance().logOut();
        goMainScreen();


    }
    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(Signin_fragment.this.getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"firebase error login",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void goMainScreen() {
        Intent intent=new Intent(Signin_fragment.this.getActivity(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            /*this approach didnt work
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();


            nameTextView.setText(personName);
            locationTextView.setText(personEmail);
            photoImageView.setImageURI(personPhoto);


             */
            firebaseAuthWithGoogle(result.getSignInAccount());
            goMainScreen();

        }
        else{
            Toast.makeText(Signin_fragment.this.getActivity(),"login error please check",Toast.LENGTH_SHORT).show();
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        AuthCredential credential= GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(Signin_fragment.this.getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"no authentication",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
        if(firebaseAuthListener!=null){
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    private void checkLogin() {
        String email= mLoginEmailField.getText().toString().trim();
        String password=mLoginPasswordField.getText().toString().trim();

        if(!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)){
            mProgress.setMessage("Checking Login...");
            mProgress.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Signin_fragment.this.getActivity(),"Success",Toast.LENGTH_LONG).show();
                        mProgress.dismiss();
                       checkUserExist();

                    }else{
                        mProgress.dismiss();
                        Toast.makeText(Signin_fragment.this.getActivity(),"Error login",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    private void checkUserExist() {
        final String user_id=mAuth.getCurrentUser().getUid();
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    Intent mainIntent=new Intent(Signin_fragment.this.getActivity(),MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                }else{
                   /* Intent setupIntent=new Intent(Signin_fragment.this.getActivity(),SetupActivity.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);*/
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
