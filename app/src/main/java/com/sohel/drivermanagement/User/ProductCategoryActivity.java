package com.sohel.drivermanagement.User;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.Admin.Adapters.CategoryAdapter;
import com.sohel.drivermanagement.Admin.DataModuler.Category;
import com.sohel.drivermanagement.R;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryActivity extends AppCompatActivity {

    private List<Category> categoryList=new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressBar;
    private DatabaseReference productCategoryRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);

        progressBar=new ProgressDialog(this);
        productCategoryRef= FirebaseDatabase.getInstance().getReference().child("Admin").child("Utilites").child("Category");

        recyclerView=findViewById(R.id.productCategoryRecyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter=new CategoryAdapter(this,categoryList);
        recyclerView.setAdapter(categoryAdapter);







    }

    @Override
    protected void onStart() {
        super.onStart();

        productCategoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    categoryList.clear();
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        Category category=snapshot1.getValue(Category.class);
                        categoryList.add(category);
                        categoryAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}