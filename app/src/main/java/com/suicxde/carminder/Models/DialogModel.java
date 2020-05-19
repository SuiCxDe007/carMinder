package com.suicxde.carminder.Models;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.suicxde.carminder.R;

public class DialogModel extends AppCompatDialogFragment {
    private EditText pass1,pass2;
    private dlglistner listner;
    private TextView checkpass;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialogx,null);
        builder.setView(view).setIcon(R.drawable.pass).setTitle(R.string.newpass).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String conpass = pass1.getText().toString();
                String conpass2 = pass2.getText().toString();
                if(conpass.isEmpty()||conpass2.isEmpty()){}else {
                    listner.infos(conpass, conpass2);
                }
            }
        });

        pass1 = view.findViewById(R.id.fireuppass);
        pass2 = view.findViewById(R.id.fireuppassconf);
        checkpass = view.findViewById(R.id.passmatch);
        addcha();
        return builder.create();
    }

    public void addcha(){

        pass2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String strPass1 = pass1.getText().toString();
                String strPass2 = pass2.getText().toString();

                if (strPass1.equals(strPass2)) {
                    checkpass.setText(R.string.passmatch);
                } else {
                    checkpass.setText(R.string.passnotmatch);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listner = (dlglistner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +"mustimplement");
        }
    }

    public interface dlglistner{
        void infos(String conpass,String conpass2);
    }

}