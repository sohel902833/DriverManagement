
package com.sohel.drivermanagement.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.Adapter.HomeListAdapter;
import com.sohel.drivermanagement.User.DataModuler.HomeList;

import java.util.ArrayList;
import java.util.List;

public class HomeListActivity extends AppCompatActivity {

    private RecyclerView homeListRecyclerview;
    private HomeListAdapter homeListAdapter;
    private List<HomeList> homeDataList=new ArrayList<>();

    private DatabaseReference homeRef;
    private FirebaseAuth mAuth;


    private Button aloteLaterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_list);
        mAuth=FirebaseAuth.getInstance();
        homeRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Home");

        homeListRecyclerview=findViewById(R.id.homeListRecylerviewid);
        aloteLaterButton=findViewById(R.id.aloteLaterButton);
        homeListRecyclerview.setHasFixedSize(true);
        homeListRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        homeListAdapter=new HomeListAdapter(this,homeDataList);
        homeListRecyclerview.setAdapter(homeListAdapter);




        homeListAdapter.setOnItemClickListner(new HomeListAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                HomeList currentItem=homeDataList.get(position);

                Intent intent=new Intent(HomeListActivity.this,FloorListActivity.class);
                intent.putExtra("homeName",currentItem.getHomeName());
                intent.putExtra("homeId",currentItem.getHomeId());
                startActivity(intent);
            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onUpdate(int position) {

            }
        });


        aloteLaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToMainActivity();
            }
        });



    }

    private void sendUserToMainActivity() {
        Intent intent=new Intent(HomeListActivity.this,MainBottomNavigationActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        homeDataList.clear();

        homeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    homeDataList.clear();
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        HomeList homes=snapshot1.getValue(HomeList.class);
                        homeDataList.add(homes);
                        homeListAdapter.notifyDataSetChanged();

                    }
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}