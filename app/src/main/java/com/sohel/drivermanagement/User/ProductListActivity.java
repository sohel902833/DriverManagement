package com.sohel.drivermanagement.User;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.Admin.Adapters.ProductAdapter;
import com.sohel.drivermanagement.Admin.DataModuler.Products;
import com.sohel.drivermanagement.R;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Products> productList=new ArrayList<>();
    private DatabaseReference productRef;
    private ProductAdapter productAdapter;
    private String categoryId;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        categoryId=getIntent().getStringExtra("categoryId");
        progressBar=new ProgressDialog(this);

        productRef= FirebaseDatabase.getInstance().getReference().child("Admin").child("Utilites").child("Products");

        recyclerView=findViewById(R.id.productListRecyclerViewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productAdapter=new ProductAdapter(this,productList);
        recyclerView.setAdapter(productAdapter);


        productAdapter.setOnItemClickListner(new ProductAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
               Products currentItem=productList.get(position);
                Intent intent=new Intent(ProductListActivity.this,ProductCategoryActivity.class);
                intent.putExtra("categoryId",currentItem.getCategoryId());
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
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    productList.clear();
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        Products products=snapshot1.getValue(Products.class);
                        if(products.getCategoryId().equals(categoryId)){
                            productList.add(products);
                            productAdapter.notifyDataSetChanged();
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