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
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Locale;

import com.suicxde.carminder.Models.User;
import com.suicxde.carminder.R;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    EditText name,emailx,passwordx,confimPassword,mobile;
    Button alreadyhave;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private User userx;
    private CircularProgressButton register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();
        loadLocale();
        name = findViewById(R.id.name);
        emailx = findViewById(R.id.email);
        passwordx = findViewById(R.id.password);
        confimPassword = findViewById(R.id.confirmpass);
        mobile = findViewById(R.id.mobile);
        register = findViewById(R.id.registerbutton);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("User");
        userx = new User();
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailx.getText().toString();
                String password = passwordx.getText().toString();
                String password2 = confimPassword.getText().toString();
                final String uname = name.getText().toString();
                final String usertel = mobile.getText().toString();

                userx.setEmail(email);
                userx.setUsertp(usertel);
                userx.setNameuser(uname);

                if (validateForm() && checkPassword()) {
                    register.startAnimation();
                    registerNewEmail(email, password);
                    ref.child(userx.getNameuser()).setValue(userx);
                    return;
                }

            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailx.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailx.setError(getText(R.string.required));
            valid = false;
        }
        if( !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            emailx.setError(getText(R.string.validemail));
            valid = false;
        }
        else {
            emailx.setError(null);
        }

        String nameu = name.getText().toString();
        if (TextUtils.isEmpty(nameu)) {
            name.setError(getText(R.string.required));

            valid = false;
        } else {
            name.setError(null);
        }

        String tpno = mobile.getText().toString();
        if (TextUtils.isEmpty(tpno)) {
            mobile.setError(getText(R.string.required));
            valid = false;
        }
        if(!(Patterns.PHONE.matcher(tpno).matches())){
            mobile.setError(getText(R.string.validcno));
            valid = false;
        }
        else {
            mobile.setError(null);
        }

        String password = passwordx.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordx.setError(getText(R.string.required));
            valid = false;
        } else {
            passwordx.setError(null);
        }

        String password2 = confimPassword.getText().toString();
        if (TextUtils.isEmpty(password2)) {
            confimPassword.setError(getText(R.string.required));
            valid = false;
        } else {
            confimPassword.setError(null);
        }

        return valid;
    }


    private boolean checkPassword(){
        boolean validx = true;
        String password = passwordx.getText().toString();
        String password2 = confimPassword.getText().toString();

        if (!password.equals(password2)) {

            passwordx.setError(getText(R.string.dnomatch));
            confimPassword.setError(getText(R.string.dnomatch));
            validx = false;

        } else {
            passwordx.setError(null);
            confimPassword.setError(null);
        }

        return validx;
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
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


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        this.finish();
    }



    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        this.finish();
    }




    public void registerNewEmail(final String email, String password) {

        final AlertDialog alertDialogx= new
                SpotsDialog.Builder().setContext(RegisterActivity.this).build();

        alertDialogx.setMessage(getString(R.string.wait));
        alertDialogx.show();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                            User user = new User();
                            user.setEmail(email);
                            user.setUsername(email.substring(0, email.indexOf("@")));
                            user.setUser_id(FirebaseAuth.getInstance().getUid());
                            final String x = mobile.getText().toString();
                            final String y = name.getText().toString();

                            user.setUsertp(x);
                            user.setNameuser(y);

                            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                                    .setTimestampsInSnapshotsEnabled(true)
                                    .build();

                            DocumentReference newUserRef = mDb
                                    .collection(getString(R.string.collection_users))
                                    .document(FirebaseAuth.getInstance().getUid());

                            newUserRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        register.setVisibility(View.VISIBLE);
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        FirebaseUser user = auth.getCurrentUser();

                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {
                                                            alertDialogx.dismiss();
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                                            builder.setTitle(R.string.regsuccesstitle);
                                                            builder.setNegativeButton(R.string.ok,null);
                                                            builder.setMessage(R.string.regsuccessdesc);
                                                            builder.setIcon(R.drawable.emailsent);
                                                            AlertDialog alertDialog = builder.create();
                                                            alertDialog.show();
                                                            register.revertAnimation();
                                                            ((EditText) findViewById(R.id.email)).getText().clear();
                                                            ((EditText) findViewById(R.id.name)).getText().clear();
                                                            ((EditText) findViewById(R.id.mobile)).getText().clear();
                                                            ((EditText) findViewById(R.id.password)).getText().clear();
                                                            ((EditText) findViewById(R.id.confirmpass)).getText().clear();
                                                        }else{
                                                            alertDialogx.dismiss();
                                                            register.revertAnimation();
                                                            Toast.makeText(RegisterActivity.this, "Please Try Again.Error Occured", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });


                                    } else {
                                        alertDialogx.dismiss();
                                        View parentLayout = findViewById(android.R.id.content);
                                        Snackbar.make(parentLayout, "Registration Failure:" + task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        } else {
                            alertDialogx.dismiss();
                            register.stopAnimation();
                            if (task.getException() instanceof FirebaseAuthException) {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                register.revertAnimation();
                                switch (errorCode) {

                                    case "ERROR_INVALID_CREDENTIAL":
                                        Toast.makeText(RegisterActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_INVALID_EMAIL":

                                        emailx.setError(getString(R.string.validemail));
                                        emailx.requestFocus();
                                        emailx.setSelectAllOnFocus(true);
                                        emailx.selectAll();
                                        break;

                                    case "ERROR_WRONG_PASSWORD":
                                        passwordx.setError(getString(R.string.invalidpass));
                                        passwordx.requestFocus();
                                        passwordx.selectAll();
                                        break;


                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        emailx.setError(getString(R.string.emailalready));
                                        emailx.requestFocus();
                                        emailx.setSelectAllOnFocus(true);
                                        emailx.selectAll();
                                        break;

                                    case "ERROR_WEAK_PASSWORD":
                                        passwordx.setError(getString(R.string.weakpassword));
                                        passwordx.requestFocus();
                                        break;
                                }

                            }
                            else{
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
