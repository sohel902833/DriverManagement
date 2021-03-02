package com.sohel.drivermanagement.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.Adapter.FloorListAdapter;
import com.sohel.drivermanagement.User.DataModuler.FloorList;
import com.sohel.drivermanagement.User.DataModuler.FloorList2;

import java.util.ArrayList;
import java.util.List;

public class FloorListActivity extends AppCompatActivity {

    private TextView textView;
    private RecyclerView recyclerView;
    private FloorListAdapter floorAdapter;
    private List<FloorList2> floorDataList=new ArrayList<>();

    String homeName,homeId;


    //Firebase Database
    private DatabaseReference floorRef;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_list);

        mAuth=FirebaseAuth.getInstance();

       homeName= getIntent().getStringExtra("homeName");
       homeId= getIntent().getStringExtra("homeId");
       floorRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Floor");

        textView=findViewById(R.id.floor_HomeDetailsTextviewid);
        recyclerView=findViewById(R.id.floorListRecyclerViewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        textView.setText(homeName+" Floor Alote");

        floorAdapter=new FloorListAdapter(this,floorDataList);

        recyclerView.setAdapter(floorAdapter);


        floorAdapter.setOnItemClickListner(new FloorListAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                FloorList2 currentItem=floorDataList.get(position);
                Intent intent=new Intent(FloorListActivity.this,FloorAloteActivity.class);
                intent.putExtra("homeName",homeName);
                intent.putExtra("floorName",currentItem.getFloorName());
                intent.putExtra("floorId",currentItem.getFloorId());
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
    }

    @Override
    protected void onStart() {
        super.onStart();

        floorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    floorDataList.clear();
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        FloorList2 floor=snapshot1.getValue(FloorList2.class);
                        if(floor.getHomeId().equals(homeId)){
                            floorDataList.add(floor);
                            floorAdapter.notifyDataSetChanged();
                        }

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}