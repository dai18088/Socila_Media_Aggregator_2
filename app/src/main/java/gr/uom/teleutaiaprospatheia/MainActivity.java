package gr.uom.teleutaiaprospatheia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;


import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    //Facebook Login
    private CallbackManager callbackManagerFacabook;
    private LoginButton loginButtonFacebook;

    //Share in Facebook
    private ShareButton share_Link_Facebook;
    private  ShareButton share_Photo_Facebook;
    private ImageView imageView;


    //Instagram Login
    private Button InstagramSignInButton;

    //Share in Instagram
    private Button uploadButton;
    private Uri targetUri=null;


    //Twitter
    SharedPreferences pref;
    private static String CONSUMER_KEY = "xyyTE7Unpc1LcFRtsazib8Fs1";
    private static String CONSUMER_SECRET = "luv8WEvi5fCczinvqWSKSsXD7kkbc4IsKrmwkhvYtnSE7G59ck";


    //Hashtag
    private Button Hashtag_Button;


    private static final String TAG="Facebook";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hashtag
        Hashtag_Button=findViewById(R.id.Hashtag_Button);

        Hashtag_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrendingHashtag();
            }
        });
        //Hashtag



        //Facebook Login
        loginButtonFacebook =findViewById(R.id.login_button_facebook);

        //Share in Facebook
        share_Link_Facebook=findViewById(R.id.share_Link);
        share_Photo_Facebook =findViewById(R.id.share_photo);
        imageView=findViewById(R.id.iv_pic);
        imageView.setImageResource(R.drawable.marios);


        callbackManagerFacabook = CallbackManager.Factory.create();
        loginButtonFacebook.setPermissions(Arrays.asList("user_gender,user_friends"));
        loginButtonFacebook.registerCallback(callbackManagerFacabook, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"Login Successful!");
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"Login Canceled!");
                Toast.makeText(MainActivity.this, "Authentication Failed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG,"Login Error!");
                Toast.makeText(MainActivity.this, "Authentication Failed",Toast.LENGTH_LONG).show();
            }
        });


        //Instagram Log in
        InstagramSignInButton = (Button)findViewById(R.id.instagram_sign_in_button);
        InstagramSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithInstagram();
            }
        });


        //Instragram Post
        uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                if (intent != null)
                {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setPackage("com.instagram.android");
                    try {
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), "https://drive.google.com/drive/u/0/my-drive", "I am Happy", "Share happy !")));
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    shareIntent.setType("image/jpg");

                    startActivity(shareIntent);
                }
                else
                {
                    // bring user to the market to download the app.
                    // or let them choose an app?
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("market://details?id="+"com.instagram.android"));
                    startActivity(intent);
                }

            }

        });
        //Instragram Post






        //Twitter
        pref = getPreferences(0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("CONSUMER_KEY", CONSUMER_KEY);
        edit.putString("CONSUMER_SECRET", CONSUMER_SECRET);
        edit.commit();

        Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, login);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
        //Twitter


    }

    //Facebook Login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManagerFacabook.onActivityResult(requestCode, resultCode, data);

        //Share in Facebook
        ShareLinkContent shareLinkContent=new ShareLinkContent.Builder().setContentUrl(Uri.parse("https://www.youtube.com/watch?v=rgsKm8Ib8No")).setShareHashtag(new ShareHashtag.Builder().setHashtag("#Android_Studio").build()).build();

        share_Link_Facebook.setShareContent(shareLinkContent);

        BitmapDrawable bitmapDrawable=(BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap=bitmapDrawable.getBitmap();

        SharePhoto sharePhoto=new SharePhoto.Builder().setBitmap(bitmap).build();

        SharePhotoContent sharePhotoContent=new SharePhotoContent.Builder().addPhoto(sharePhoto).build();

        share_Photo_Facebook.setShareContent(sharePhotoContent);
        //Share in Facebook


        GraphRequest graphRequest=GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d(TAG,object.toString());
                    }
                });



        Bundle bundle=new Bundle();
        bundle.putString("fields","gender, name, id, first_name, last_name");

        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();

    }


    AccessTokenTracker accessTokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }





    //Instagram Login
    private void signInWithInstagram() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https")
                .authority("api.instagram.com")
                .appendPath("oauth")
                .appendPath("authorize")
                .appendQueryParameter("client_id", "18a8b74da9644bd7a9294caef1c5e76c")
                .appendQueryParameter("redirect_uri", "sociallogin://redirect")
                .appendQueryParameter("response_type", "token");
        final Intent browser = new Intent(Intent.ACTION_VIEW, uriBuilder.build());
        startActivity(browser);
    }



    private void checkForInstagramData() {
        final Uri data = this.getIntent().getData();
        if(data != null && data.getScheme().equals("sociallogin") && data.getFragment() != null) {
            final String accessToken = data.getFragment().replaceFirst("access_token=", "");
            if (accessToken != null) {
                // handleSignInResult(...);
            } else {
                // handleSignInResult(...);
            }
        }
    }

    //Instagram Login



    //Hashtag
    public void openTrendingHashtag(){
        Intent intent=new Intent(this,TrendingHashtags.class);
        startActivity(intent);
    }
    //Hashtag









}
