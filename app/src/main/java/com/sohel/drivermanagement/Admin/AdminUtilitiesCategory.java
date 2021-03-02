package com.sohel.drivermanagement.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sohel.drivermanagement.Admin.Adapters.CategoryAdapter;
import com.sohel.drivermanagement.Admin.DataModuler.Category;
import com.sohel.drivermanagement.LoginActivity;
import com.sohel.drivermanagement.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AdminUtilitiesCategory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton addUtilitiesCategory;


    //for diolouge vaiable
    ImageView categoryImageView;
    private  Uri imageUri;
    private ProgressDialog progressBar;



    //Firebase
    private DatabaseReference utilitesRef;
    private StorageReference storageReference;

    private List<Category> categoryList=new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_utilities_category);

        progressBar=new ProgressDialog(this);
        storageReference= FirebaseStorage.getInstance().getReference().child("CategoryImages");
        utilitesRef= FirebaseDatabase.getInstance().getReference().child("Admin").child("Utilites").child("Category");




        recyclerView=findViewById(R.id.adminUtilitisCategoryListRecyclerView);
        addUtilitiesCategory=findViewById(R.id.addUtilitesFloatingButtnId);


        addUtilitiesCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUtiliteAddDiolouge();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        categoryAdapter=new CategoryAdapter(this,categoryList);
        recyclerView.setAdapter(categoryAdapter);


        categoryAdapter.setOnItemClickListner(new CategoryAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                Category category=categoryList.get(position);
                Intent intent=new Intent(AdminUtilitiesCategory.this,UtilitiesListActivity.class);
                intent.putExtra("categoryId",category.getCategoryId());
                startActivity(intent);

            }

            @Override
            public void onDelete(int position) {
                deleteCategory(categoryList.get(position).getCategoryId());
            }

            @Override
            public void onUpdate(int position) {
                showUtilitiUpdateDiolouge(categoryList.get(position));
            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();
        utilitesRef.addValueEventListener(new ValueEventListener() {
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
public void deleteCategory(String uid){
        progressBar.setMessage("Deleting Category.");
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();
    utilitesRef.child(uid)
        .removeValue()
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressBar.dismiss();
                   Toasty.success(AdminUtilitiesCategory.this, "Category Deleted", Toast.LENGTH_SHORT, true).show();
                }else{
                    progressBar.dismiss();
                    Toasty.warning(AdminUtilitiesCategory.this, "Category Delete Failed,Please Try again Later.", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
}
    private void showUtilitiUpdateDiolouge(Category category) {
        AlertDialog.Builder builder=new AlertDialog.Builder(AdminUtilitiesCategory.this);
        View view=getLayoutInflater().inflate(R.layout.utiliti_add_diolouge,null);
        builder.setView(view);

         categoryImageView=view.findViewById(R.id.utiliti_diolouge_ImageViewid);
        EditText editText=view.findViewById(R.id.utiliti_diolouge_CategoryNameEdittextid);
        Button saveButton=view.findViewById(R.id.categorySaveButtonid);

        Picasso.get().load(category.getImage()).into(categoryImageView);
        editText.setText(category.getCategoryName());
        saveButton.setText("Update Category");
        final AlertDialog dialog=builder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName=editText.getText().toString();

                 if(categoryName.isEmpty()){
                    editText.setError("Please Write Category Name");
                    editText.requestFocus();
                }else{
                    updateCategory(category,categoryName,dialog);
                }
            }
        });
        categoryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfilechooser();
            }
        });





    }
    private void showUtiliteAddDiolouge() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AdminUtilitiesCategory.this);
        View view=getLayoutInflater().inflate(R.layout.utiliti_add_diolouge,null);
        builder.setView(view);

        categoryImageView=view.findViewById(R.id.utiliti_diolouge_ImageViewid);
        EditText editText=view.findViewById(R.id.utiliti_diolouge_CategoryNameEdittextid);
        Button saveButton=view.findViewById(R.id.categorySaveButtonid);
        final AlertDialog dialog=builder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName=editText.getText().toString();
                if(imageUri==null){
                    Toasty.warning(AdminUtilitiesCategory.this, "Please Choose An Image First", Toast.LENGTH_SHORT, true).show();
                }
                else if(categoryName.isEmpty()){
                    editText.setError("Please Write Category Name");
                    editText.requestFocus();
                }else{
                    saveCategory(categoryName,dialog);
                }
            }
        });
        categoryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfilechooser();
            }
        });





    }
    private void saveCategory(String categoryName,AlertDialog dialog) {
        progressBar.setMessage("Saving Category.");
        progressBar.setTitle("Please Wait");
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();

        String key=utilitesRef.push().getKey();

        StorageReference filePath=storageReference.child(key+utilitesRef.push().getKey()+"."+getFileExtension(imageUri));
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!urlTask.isSuccessful());
                Uri downloaduri=urlTask.getResult();

                String categoryId=key+System.currentTimeMillis()+System.currentTimeMillis();

                HashMap<String,String> categoryMap=new HashMap<>();
                categoryMap.put("categoryName",categoryName);
                categoryMap.put("categoryId",categoryId);
                categoryMap.put("image",downloaduri.toString());


                utilitesRef.child(categoryId)
                            .setValue(categoryMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            progressBar.dismiss();
                                            dialog.dismiss();
                                            Toasty.success(AdminUtilitiesCategory.this, "Category Saved", Toast.LENGTH_SHORT, true).show();
                                        }else{
                                            progressBar.dismiss();
                                            Toasty.warning(AdminUtilitiesCategory.this, "Category Save Failed,Please Try again Later.", Toast.LENGTH_SHORT, true).show();
                                        }
                                    }
                                });

            }
        });





    }
    private void updateCategory(Category category,String categoryName,AlertDialog dialog) {
        progressBar.setMessage("Saving Category.");
        progressBar.setTitle("Please Wait");
        progressBar.setCanceledOnTouchOutside(false);

        if(imageUri==null){
            progressBar.show();
            utilitesRef.child(category.getCategoryId())
                    .child(categoryName)
                    .setValue(categoryName)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.dismiss();
                                dialog.dismiss();
                                Toasty.success(AdminUtilitiesCategory.this, "Category Update Success", Toast.LENGTH_SHORT, true).show();
                            }else{
                                progressBar.dismiss();
                                Toasty.warning(AdminUtilitiesCategory.this, "Category Update Failed,Please Try again Later.", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    });
        }else{
            progressBar.show();
            String key=utilitesRef.push().getKey();

            StorageReference filePath=storageReference.child(key+utilitesRef.push().getKey()+"."+getFileExtension(imageUri));
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                    while(!urlTask.isSuccessful());
                    Uri downloaduri=urlTask.getResult();
                    HashMap<String,Object> categoryMap=new HashMap<>();
                    categoryMap.put("categoryName",categoryName);
                    categoryMap.put("image",downloaduri.toString());


                    utilitesRef.child(category.getCategoryId())
                            .updateChildren(categoryMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressBar.dismiss();
                                        dialog.dismiss();
                                        Toasty.success(AdminUtilitiesCategory.this, "Category Updated", Toast.LENGTH_SHORT, true).show();
                                    }else{
                                        progressBar.dismiss();
                                        Toasty.warning(AdminUtilitiesCategory.this, "Category Update Failed,Please Try again Later.", Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                            });

                }
            });


        }

    }


    public String getFileExtension(Uri imageuri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageuri));
    }

    private void openfilechooser() {
        Intent intentf=new Intent();
        intentf.setType("image/*");
        intentf.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentf,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data.getData()!=null){
            imageUri=data.getData();
            categoryImageView.setImageURI(data.getData());
        }

    }
}