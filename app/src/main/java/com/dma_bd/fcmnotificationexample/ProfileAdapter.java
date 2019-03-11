package com.dma_bd.fcmnotificationexample;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{
    private Context context;
    private ArrayList<User> list;


    public ProfileAdapter(Context context, ArrayList<User> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(context).inflate(R.layout.single_received_view, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user, parent, false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull final ProfileAdapter.ViewHolder viewHolder, final int i) {
        final User user=list.get(i);

        viewHolder.id.setText(user.getEmail());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    public void filterList(ArrayList<ReceivedItemClass> filteredlist){
//        list=filteredlist;
//        notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id, uid;
        LinearLayout linearLayout;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.textEmail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user=list.get(getAdapterPosition());
                    Intent intent=new Intent(context,SendNotificationActivity.class);
                    intent.putExtra("user",user);
                    context.startActivity(intent);
                }
            });

        }
    }
}