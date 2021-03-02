package com.sohel.drivermanagement.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import es.dmoral.toasty.Toasty;

public class UtilitiesListActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private List<Products> productList=new ArrayList<>();
    private DatabaseReference productRef;
    private ProductAdapter productAdapter;
    private String categoryId;

    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities_list);
        categoryId=getIntent().getStringExtra("categoryId");
        progressBar=new ProgressDialog(this);

        productRef= FirebaseDatabase.getInstance().getReference().child("Admin").child("Utilites").child("Products");

        floatingActionButton=findViewById(R.id.addUtilsFloatingButtonId);
        recyclerView=findViewById(R.id.productListRecyclerViewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productAdapter=new ProductAdapter(this,productList);
        recyclerView.setAdapter(productAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToAddUtilsActivity();
            }
        });

        productAdapter.setOnItemClickListner(new ProductAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                Products currentItem=productList.get(position);

                Intent intent=new Intent(UtilitiesListActivity.this,AddUtilitisItemActivity.class);
                intent.putExtra("checker",2);
                intent.putExtra("categoryId",currentItem.getCategoryId());
                intent.putExtra("productId",currentItem.getId());
                intent.putExtra("productName",currentItem.getProductName());
                intent.putExtra("productImage",currentItem.getImage());
                intent.putExtra("productDescription",currentItem.getProductDescription());
                startActivity(intent);
            }

            @Override
            public void onDelete(int position) {
                deleteProduct(productList.get(position).getId());
            }

            @Override
            public void onUpdate(int position) {
                Products currentItem=productList.get(position);

                Intent intent=new Intent(UtilitiesListActivity.this,AddUtilitisItemActivity.class);
                  intent.putExtra("checker",2);
                  intent.putExtra("categoryId",currentItem.getCategoryId());
                  intent.putExtra("productId",currentItem.getId());
                  intent.putExtra("productName",currentItem.getProductName());
                  intent.putExtra("productImage",currentItem.getImage());
                  intent.putExtra("productDescription",currentItem.getProductDescription());
                startActivity(intent);
            }
        });
    }

    private void deleteProduct(String productId) {
        progressBar.setMessage("Deleting Product.");
        progressBar.setTitle("Please Wait..");
        productRef.child(productId)
                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressBar.dismiss();
                    Toasty.success(UtilitiesListActivity.this, "Product Updated", Toast.LENGTH_SHORT, true).show();
                }else{
                    progressBar.dismiss();
                    Toasty.warning(UtilitiesListActivity.this, "Product Update Failed,Please Try again Later.", Toast.LENGTH_SHORT, true).show();
                }
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

    public void sendUserToAddUtilsActivity(){
        Intent intent=new Intent(UtilitiesListActivity.this,AddUtilitisItemActivity.class);
        intent.putExtra("categoryId",categoryId);
        intent.putExtra("checker",1);
        startActivity(intent);

    }


}