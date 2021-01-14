package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener{
    private EditText editUserName, editUserMail, editPassword;
    private Button buttonSignUp, buttonLogInSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        editUserMail = findViewById(R.id.editUserMaiSignUp);
        editUserName = findViewById(R.id.editUserNameSignUp);
        editPassword = findViewById(R.id.editPasswordSignUp);
        editPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(buttonSignUp);
                }
                return false;
            }

        });
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonLogInSwitch = findViewById(R.id.buttonLogInSwitch);

        buttonSignUp.setOnClickListener(this);
        buttonLogInSwitch.setOnClickListener(this);

//        if (ParseUser.getCurrentUser() != null){
//            transitionToSocialMediaActivity();
////            ParseUser.getCurrentUser().logOut();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSignUp:
                fnSignUp();
                break;
            case R.id.buttonLogInSwitch:
                fnLogInSwitch();
                break;
        }
    }

    public void fnSignUp() {
        if (editUserName.getText().toString().equals("") || editPassword.getText().toString().equals("") || editUserMail.getText().toString().equals("")){
            fnErrorMsg();
            return;
        }
        final ParseUser appUser = new ParseUser();
        appUser.setUsername(editUserName.getText().toString());
        appUser.setPassword(editPassword.getText().toString());
        appUser.setEmail(editUserMail.getText().toString());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing up" + editUserName.getText().toString());
        progressDialog.show();

        appUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    FancyToast.makeText(SignUp.this, appUser.getUsername() + " sing up successfully!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                    transitionToSocialMediaActivity();
//                            Intent intent = new Intent(SignUpTwitter.this, WelcomeActivity.class);
//                            startActivity(intent);
                }else{
                    fnErrorMsg();
                }
            }
        });

        progressDialog.dismiss();
    }

    public void rootLayoutTapped(View View){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void fnErrorMsg(){
        FancyToast.makeText(SignUp.this, "ERROR", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
    }

    public void fnLogInSwitch() {
        Intent intent = new Intent(SignUp.this, LogIn.class);
        startActivity(intent);
    }

    private void transitionToSocialMediaActivity(){

        Intent intent = new Intent(SignUp.this, WhatsAppUsers.class);
        startActivity(intent);
        finish();
    }
}