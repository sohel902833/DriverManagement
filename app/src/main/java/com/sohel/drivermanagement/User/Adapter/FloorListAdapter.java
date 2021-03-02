package com.sohel.drivermanagement.User.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.DataModuler.FloorList;
import com.sohel.drivermanagement.User.DataModuler.FloorList2;
import com.sohel.drivermanagement.User.DataModuler.HomeList;

import java.util.List;

public class FloorListAdapter extends RecyclerView.Adapter<FloorListAdapter.MyViewHolder>{

    private Context context;
    private List<FloorList2> floorDataList;
    private  OnItemClickListner listner;

    public FloorListAdapter(Context context, List<FloorList2> floorDataList) {
        this.context = context;
        this.floorDataList = floorDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.floor_item_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FloorList2 currentItem=floorDataList.get(position);
        String aloted=currentItem.getAloted();
        if(aloted.equals("false")){
            holder.paymentButton.setVisibility(View.GONE);
            holder.editButton.setVisibility(View.GONE);
            holder.paymentDueTextviewid.setText("Payment Due: 0");
        }else{
            holder.paymentButton.setVisibility(View.VISIBLE);
            holder.editButton.setVisibility(View.VISIBLE);
            holder.paymentDueTextviewid.setText("Payment Due: "+currentItem.getDueAmount());
        }


        holder.floorNameTextview.setText("Floor : "+currentItem.getFloorName());
        holder.alotedTextview.setText("Renter Name:  "+currentItem.getAlotedPerson());

    }

    @Override
    public int getItemCount() {
        return floorDataList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        TextView floorNameTextview,alotedTextview,paymentDueTextviewid;
        Button paymentButton,editButton;

      public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

            floorNameTextview=itemView.findViewById(R.id.floorList_FloorNameTextviewid);
            alotedTextview=itemView.findViewById(R.id.floorList_AlotedTextviewid);
            paymentDueTextviewid=itemView.findViewById(R.id.floorList_dueBalanceTextviewid);
            paymentButton=itemView.findViewById(R.id.floor_ListPaymentButtonid);
            editButton=itemView.findViewById(R.id.floorList_EditButtonid);


        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listner!=null){
                int position=getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:{
                            listner.onDelete(position);
                            return  true;
                        }
                        case 2:{
                            listner.onUpdate(position);
                            return  true;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            if(listner!=null){
                int position=getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    listner.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("choose an action");
            MenuItem delete=menu.add(Menu.NONE,1,1,"Delete Category");
            MenuItem update=menu.add(Menu.NONE,2,2,"Update Category");
            delete.setOnMenuItemClickListener(this);
            update.setOnMenuItemClickListener(this);

        }
    }
    public interface  OnItemClickListner{
        void onItemClick(int position);
        void onDelete(int position);
        void onUpdate(int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }


}
