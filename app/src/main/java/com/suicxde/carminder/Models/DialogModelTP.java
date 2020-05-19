package com.suicxde.carminder.Models;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.suicxde.carminder.R;

public class DialogModelTP extends AppCompatDialogFragment {

    private EditText tp1;
    private dlglistner3 listnertp;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialogtp,null);
        builder.setView(view).setIcon(R.drawable.mobileio).setTitle(R.string.mobile).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String fireupdatetp = tp1.getText().toString();
                if(fireupdatetp.isEmpty()){

                }else {
                    listnertp.infosb(fireupdatetp);

                }

            }
        });

        tp1 = view.findViewById(R.id.fireuptp);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listnertp = (dlglistner3) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +"mustimplement");
        }
    }

    public interface dlglistner3 {
        void infosb(String tp1);
    }
}