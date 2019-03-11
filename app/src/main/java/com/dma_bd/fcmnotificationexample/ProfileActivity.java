package com.dma_bd.fcmnotificationexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    public static final String NODE_USERS = "users";

    private FirebaseAuth mAuth;

    private ArrayList<User> userList;

    private RecyclerView recyclerView;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //NotificationHelper.displayNotification(this,"title","body");

        mAuth=FirebaseAuth.getInstance();

        loadUsers();

        FirebaseMessaging.getInstance().subscribeToTopic("updates");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            String token=task.getResult().getToken();
                            saveToken(token);
                            Log.e("Token",token);
                        }else {

                        }
                    }
                });
    }

    private void loadUsers(){
        pDialog.setMessage("Loading...");
        pDialog.show();
        userList=new ArrayList<>();
        recyclerView=findViewById(R.id.rV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        DatabaseReference dbUsers=FirebaseDatabase.getInstance().getReference("users");
        dbUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pDialog.hide();
                if(dataSnapshot.exists()){

                    for(DataSnapshot dbUsers:dataSnapshot.getChildren()){
                        User user=dbUsers.getValue(User.class);
                        userList.add(user);
                    }

                    ProfileAdapter adapter=new ProfileAdapter(ProfileActivity.this,userList);
                    recyclerView.setAdapter(adapter);
                }else{
                    Toast.makeText(ProfileActivity.this,"No User Found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()==null){
            Intent intent=new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    private void saveToken(String token){
        String email=mAuth.getCurrentUser().getEmail();

        User user=new User(email,token);

        DatabaseReference dbUsers= FirebaseDatabase.getInstance().getReference(NODE_USERS);
        dbUsers.child(mAuth.getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ProfileActivity.this,"Token Saved",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
