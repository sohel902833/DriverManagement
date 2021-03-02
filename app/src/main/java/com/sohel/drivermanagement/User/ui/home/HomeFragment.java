package com.sohel.drivermanagement.User.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.DataModuler.HomeList;
import com.sohel.drivermanagement.User.HomeListActivity;


public class HomeFragment extends Fragment {

    private Button paymentEditButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_home, container, false);

        paymentEditButton=root.findViewById(R.id.f_home_paymentEditButton);

        paymentEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToHomeListActivity();
            }
        });
        return root;
    }

    private void sendUserToHomeListActivity() {
        Intent intent=new Intent(getContext(), HomeListActivity.class);
        intent.putExtra("check","edit");
        startActivity(intent);


    }
}