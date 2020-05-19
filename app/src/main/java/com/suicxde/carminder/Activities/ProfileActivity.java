package com.suicxde.carminder.Activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.suicxde.carminder.Models.DialogModel;
import com.suicxde.carminder.Models.DialogModelEmail;
import com.suicxde.carminder.Models.DialogModelName;
import com.suicxde.carminder.Models.DialogModelTP;
import com.suicxde.carminder.R;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class ProfileActivity extends AppCompatActivity implements DialogModel.dlglistner, DialogModelName.dlglistner1 , DialogModelEmail.dlglistner2 , DialogModelTP.dlglistner3 {

    private static final String TAG = "ProfileActivity";
    private TextView nameuser, useremail, usertp;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private String fname, ftp;
    private Button delaccbutton,langbutton,backbutton,changepass;
    private FirebaseFirestore mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = getInstance();
        currentUser = mAuth.getCurrentUser();
        mDb = FirebaseFirestore.getInstance();
        UpdateNavHeader();
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_profile);
        RelativeLayout nameupdate = (RelativeLayout) findViewById(R.id.updatename);
        RelativeLayout emailupdate = (RelativeLayout) findViewById(R.id.emaillayout);
        RelativeLayout tpupdate = (RelativeLayout) findViewById(R.id.tpupdate);
        delaccbutton = (Button) findViewById(R.id.testbtn);
        langbutton = (Button) findViewById(R.id.langbutton);
        backbutton = (Button) findViewById(R.id.backbtn);
        changepass = (Button) findViewById(R.id.changepass);
        nameuser = findViewById(R.id.firename);
        usertp = findViewById(R.id.firetp);
        useremail = findViewById(R.id.fireemail);

        langbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });

        tpupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opentpchangedlg();
            }
        });

        nameupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opennamechangedlg();
            }
        });

        emailupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openemailchangedlg();
            }
        });

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openchangepassdlg();
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        delaccbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setIcon(R.drawable.warn).setTitle(R.string.accdel).setMessage(R.string.accdeldesc).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String current = user.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();


                        db.collection("Users").document(current)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                        FirebaseAuth.getInstance().signOut();
                        HomeUI();
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User account deleted.");
                                        }
                                    }
                                });
                    }
                }).setNegativeButton(R.string.cancel, null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void opentpchangedlg() {
        DialogModelTP dialogb  = new DialogModelTP();
        dialogb.show(getSupportFragmentManager(), "kaveen");
    }

    private void openemailchangedlg() {
        DialogModelEmail dialoga  = new DialogModelEmail();
        dialoga.show(getSupportFragmentManager(), "xxx");
    }

    private void openchangepassdlg() {
        DialogModel dialogx  = new DialogModel();

        dialogx.show(getSupportFragmentManager(), "asd");

    }

    private void opennamechangedlg() {
        DialogModelName dialogz  = new DialogModelName();
        dialogz.show(getSupportFragmentManager(),"JHINx");

    }


    public void UpdateNavHeader() {
        final android.app.AlertDialog alertDialogx = new
                SpotsDialog.Builder().setContext(ProfileActivity.this).build();

        alertDialogx.setMessage(getString(R.string.wait));
        alertDialogx.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String current = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users").whereEqualTo("user_id", current).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    alertDialogx.dismiss();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        fname = document.getString("nameuser");
                        ftp = document.getString("usertp");
                        usertp.setText(ftp);
                        useremail.setText(currentUser.getEmail());
                        nameuser.setText(fname);
                    }
                } else {
                    alertDialogx.dismiss();
                    Toast.makeText(ProfileActivity.this, "Please Try Again.Error Occured", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
    @Override
    public void onBackPressed() {

        Intent dashboard = new Intent(this, DashboardActivity.class);
        startActivity(dashboard);
        this.finish();
    }

    private void HomeUI() {

        Intent HomeAC = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(HomeAC);
        finish();

    }
    private void showChangeLanguageDialog() {

        final String [] listItems = {"English","සිංහල"};
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(ProfileActivity.this);
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){

                    setLocale("en");
                    recreate();
                }
                else if(i==1){

                    setLocale("si");
                    recreate();
                }

                dialogInterface.dismiss();
            }
        });
        android.app.AlertDialog mDialog = mBuilder.create();
        mDialog.show();
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
    public void infos(String conpass, String conpass2) {

        if(conpass.equals(conpass2)&&(!conpass.isEmpty()||(!conpass2.isEmpty()))){

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String newPassword = conpass;

            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivity.this);
                                builder.setTitle(R.string.passupdated);
                                builder.setIcon(R.drawable.tick);
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                android.app.AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle(R.string.passerror);
                    builder.setMessage(e.getMessage());
                    builder.setIcon(R.drawable.pass);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    android.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            });
        }
        else {
            Toast.makeText(this, R.string.passnotmatch, Toast.LENGTH_SHORT).show();
            openchangepassdlg();
        }
    }

    public void onClick(View view) {
    }

    @Override
    public void infosa(String name1) {


        Map<String, Object> city = new HashMap<>();
        city.put("nameuser", name1);
        mDb.collection("Users").document(getInstance().getUid())
                .update(city)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        recreate();
                        Toast.makeText(ProfileActivity.this, R.string.succef, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, R.string.passerror, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void infosz(String email1) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, R.string.succef, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        Map<String, Object> city = new HashMap<>();

        city.put("email", email1);
        mDb.collection("Users").document(getInstance().getUid())
                .update(city)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        recreate();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, R.string.passerror, Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseAuth auth = FirebaseAuth.getInstance();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    @Override
    public void infosb(String tp1) {

        Map<String, Object> city2 = new HashMap<>();

        city2.put("usertp", tp1);
        mDb.collection("Users").document(getInstance().getUid())
                .update(city2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        recreate();
                        Toast.makeText(ProfileActivity.this, R.string.succef, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, R.string.passerror, Toast.LENGTH_SHORT).show();
                    }
                });

    }
}