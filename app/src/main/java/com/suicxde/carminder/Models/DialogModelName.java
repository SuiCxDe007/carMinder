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


public class DialogModelName extends AppCompatDialogFragment {
    private EditText name1;
    private dlglistner1 listnername;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialogname,null);
        builder.setView(view).setIcon(R.drawable.nameico).setTitle(R.string.updatename).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String fireupdatename = name1.getText().toString();
                if(fireupdatename.isEmpty()){

                }else {
                    listnername.infosa(fireupdatename);
                }
            }
        });

        name1 = view.findViewById(R.id.fireupname);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listnername = (dlglistner1) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +"mustimplement");
        }
    }

    public interface dlglistner1 {
        void infosa(String name1);
    }
}