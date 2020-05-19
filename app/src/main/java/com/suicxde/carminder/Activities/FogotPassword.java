package com.suicxde.carminder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.suicxde.carminder.R;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import dmax.dialog.SpotsDialog;


public class FogotPassword extends AppCompatActivity {
    private EditText resetpasstxt;
    private CircularProgressButton resetbtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogot_password);
        resetpasstxt = findViewById(R.id.recoveremail);
        resetbtn = findViewById(R.id.resetbutton);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onBackClick(View view) {
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        this.finish();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        this.finish();
    }

    public void onResetClick(View view) {
        String useremail = resetpasstxt.getText().toString();
        final AlertDialog alertDialogx= new
                SpotsDialog.Builder().setContext(FogotPassword.this).build();
        if(validateForm()){
            resetbtn.startAnimation();
            alertDialogx.setMessage(getString(R.string.wait));
            alertDialogx.show();
            //
            mAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        alertDialogx.dismiss();
                        new AlertDialog.Builder(FogotPassword.this)
                                .setTitle(R.string.emailsent).setMessage(R.string.emailsentdesc).setIcon(R.drawable.resetemailsent)
                                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        ((EditText) findViewById(R.id.recoveremail)).getText().clear();
                                    }
                                })
                                .create()
                                .show();
                        resetbtn.revertAnimation();


                    } else {
                        resetbtn.revertAnimation();
                        alertDialogx.dismiss();
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();


                        if (task.getException() instanceof FirebaseAuthException) {


                            switch (errorCode) {


                                case "ERROR_USER_NOT_FOUND":

                                    new AlertDialog.Builder(FogotPassword.this)
                                            .setTitle(R.string.validemail).setMessage(R.string.notregistered).setIcon(R.drawable.emailerr)
                                            .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    ((EditText) findViewById(R.id.recoveremail)).selectAll();
                                                }
                                            })
                                            .create()
                                            .show();
                                    break;
                            }

                        }
                    }
                }
            });
        }
    }

    private boolean validateForm() {
        boolean valid = true;


        String email = resetpasstxt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            resetpasstxt.setError(getString(R.string.required));
            valid = false;
        } else {
            resetpasstxt.setError(null);
        }
        if( !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            resetpasstxt.setError(getText(R.string.validemail));
            valid = false;
        }
        else {
            resetpasstxt.setError(null);
        }
        return  valid;}
}
