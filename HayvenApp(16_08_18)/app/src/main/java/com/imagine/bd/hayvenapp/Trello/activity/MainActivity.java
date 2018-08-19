package com.imagine.bd.hayvenapp.Trello.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.Trello.controller.TrelloController;
import com.imagine.bd.hayvenapp.Trello.model.TrelloModel;
import com.imagine.bd.hayvenapp.Trello.service.TrelloService;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.PersistData;

import java.sql.ClientInfoStatus;


public class MainActivity extends Activity {

    // Dialog static definitions
    Context con;
    private static final int DIALOG_PROGRESS = 0;
    private static final int DIALOG_LOGIN_ERROR = 1;

    // Shared preferences definitions
    private static final String TOKEN = "token";
    
    // View Items
    private EditText mTokenEdit;
    private TextView mLinkText;
    
    // Models
    private TrelloModel mModel;
    
    // Controllers
    private TrelloController mController;
    private TrelloService trelloService;


    
    // Listeners
    private TrelloModel.OnUserDataReceivedListener mOnUserDataReceivedListener;

    // Activity variables
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mPrefsEditor;
    private WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        con=this;
        String usertrello = PersistData.getStringData(con, AppConstant.userName);
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new Client());

        String url = new Uri.Builder()
                .scheme("https")
                .authority("trello.com")
                .path("1/authorize")
                .appendQueryParameter("key", "ee531a6418b5706b6a4d8ed0452dd396")
                .appendQueryParameter("name", usertrello)
                .appendQueryParameter("response_type", "token")
                .appendQueryParameter("scope", "read,write")
                .appendQueryParameter("return_url", "https://developers.trello.com")
                .appendQueryParameter("callback_method", "fragment")
                .appendQueryParameter("expiration", "never")
                .build()
                .toString();
        mWebView.loadUrl(url);


        // Instantiate view items
        //mTokenEdit = (EditText) findViewById(R.id.token);
        //mLinkText  = (TextView) findViewById(R.id.request_token_link);
        
        // Instantiate models
        mModel = TrelloModel.getInstance();
        
        // Instantiate controllers
        mController = TrelloController.getInstance();
        trelloService = new TrelloService();
        
        // Create listeners
        mOnUserDataReceivedListener = new TrelloModel.OnUserDataReceivedListener() {
            @Override
            public void onUserDataReceived(TrelloModel model, Boolean result) {
                if (result != null) {
                    Intent intent = new Intent(MainActivity.this, TrelloTabActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    dismissDialog(DIALOG_PROGRESS);
                    
                    finish();
                } else {
                    dismissDialog(DIALOG_PROGRESS);
                    showDialog(DIALOG_LOGIN_ERROR);
                }
            }
        };
        
//        findViewById(R.id.proceed).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(DIALOG_PROGRESS);
//
//                String token = mTokenEdit.getText().toString();
//                mController.setToken(token);
//                mController.getUserData();
//
//                mPrefsEditor.putString(TOKEN, token);
//                mPrefsEditor.commit();
//            }
//        });
        
        // Add listeners
        mModel.addListener(mOnUserDataReceivedListener);
        
        // Instantiate activity variables
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mPrefsEditor = mPrefs.edit();
        
        String token = mPrefs.getString(TOKEN, "");
        
        if (!token.equals("")) {
            showDialog(DIALOG_PROGRESS);
            mController.setToken(token);
            trelloService.setToken(token);
            mController.getUserData();
        } else {
           // mLinkText.setText(mController.getRequestLink());
        }
    }

    private final class Client extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("https://developers.trello.com/#token=")) {
                String token = url.substring(url.indexOf("=") + 1);

                System.out.println("Token"+token);

                showDialog(DIALOG_PROGRESS);

                mController.setToken(token);
                mController.getUserData();

                mPrefsEditor.putString(TOKEN, token);
                mPrefsEditor.commit();

                //view.loadUrl(url);
                //MainActivity.this.finish();
                return true;
            }
            return false;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_PROGRESS:
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setCustomTitle(null);
                dialog.setMessage(getString(R.string.loading));
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                return dialog;
            case DIALOG_LOGIN_ERROR:
                return new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(getResources().getString(R.string.login_error_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .create();
        }
        
        return super.onCreateDialog(id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        mModel.removeListener(mOnUserDataReceivedListener);

    }

}