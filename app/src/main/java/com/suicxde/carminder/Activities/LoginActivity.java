package com.suicxde.carminder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.suicxde.carminder.R;

import java.util.Locale;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


    public class LoginActivity extends AppCompatActivity {
        private FirebaseAuth mAuth;
        EditText email,pass;
        CircularProgressButton btn;


        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_login);
            changeStatusBarColor();
            email = findViewById(R.id.email);
            pass = findViewById(R.id.password);
            mAuth = FirebaseAuth.getInstance();

            btn =  findViewById(R.id.loginbutton);
        }

        private void changeStatusBarColor() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
            }
        }


        public void onRegisterClick(View View){
            startActivity(new Intent(this,RegisterActivity.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        }

        public void onLoginClick(View View){

            String mail = email.getText().toString();
            String password = pass.getText().toString();
            if(validatePassword()){
                btn.startAnimation();
                signIn(mail, password);
            }
        }


        private boolean validatePassword(){

            boolean valid = true;
            String mail = email.getText().toString();
            String password = pass.getText().toString();

            if( !(Patterns.EMAIL_ADDRESS.matcher(mail).matches())){
                email.setError(getString(R.string.validemail));
                valid = false;
            } else {
                email.setError(null);
            }
            if (TextUtils.isEmpty(password)) {
                pass.setError(getString(R.string.invalidpass));
                valid = false;
            } else {
                pass.setError(null);
            }
            return valid;
        }


        @Override
        protected void onStart() {
            super.onStart();

            FirebaseUser user = mAuth.getCurrentUser();
            if ((user != null) && user.isEmailVerified()) {
               Intent x = new Intent(this,DashboardActivity.class);
               startActivity(x);
               finish();
            }
        }

        private void signIn(String mail, String password) {

            mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        if (mAuth.getCurrentUser().isEmailVerified()) {


                            btn.revertAnimation();

                            Intent HomeAC = new Intent(getApplicationContext(), DashboardActivity.class);
                            startActivity(HomeAC);
                            finish();
                        } else if (!mAuth.getCurrentUser().isEmailVerified()) {

                            mAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                                builder.setTitle(R.string.verifyemail);
                                                builder.setMessage(R.string.verifyemaildesc);
                                                builder.setNegativeButton(R.string.ok,null);
                                                builder.setIcon(R.drawable.email_no);
                                                AlertDialog alertDialog = builder.create();
                                                alertDialog.show();
                                                btn.revertAnimation();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, R.string.emailfailed, Toast.LENGTH_LONG).show();
                                    btn.revertAnimation();
                                }
                            });
                        }

                    } else {


                        if (task.getException() instanceof FirebaseAuthException) {
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            switch (errorCode) {

                                case "ERROR_INVALID_CREDENTIAL":
                                    Toast.makeText(LoginActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_EMAIL":

                                    email.setError(getString(R.string.validemail));
                                    email.requestFocus();
                                    email.setSelectAllOnFocus(true);
                                    email.selectAll();
                                    break;

                                case "ERROR_WRONG_PASSWORD":
                                    pass.setError(getString(R.string.invalidpass));
                                    pass.requestFocus();
                                    pass.selectAll();
                                    break;

                                case "ERROR_USER_DISABLED":
                                    Toast.makeText(LoginActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_NOT_FOUND":

                                    email.setError(getString(R.string.validemail));
                                    email.requestFocus();
                                    email.selectAll();
                                    break;
                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        btn.revertAnimation();
                    }
                }
            });

        }

        private void setLocale(String lang) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
            SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
            editor.putString("My_Lang",lang);
            editor.apply();
        }

        public void loadLocale(){

            SharedPreferences prefs =getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            String language = prefs.getString("My_Lang","");
            setLocale(language);

        }


        public void setSinhala(View view) {
            setLocale("si");
            recreate();
        }

        public void setEnglish(View view) {
            setLocale("en");
            recreate();
        }

        public void onFogotButtonClick(View view) {

            startActivity(new Intent(this,FogotPassword.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
            this.finish();
        }
    }



