package com.dma_bd.fcmnotificationexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SendNotificationActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editTextTitle,editTextBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        final User user= (User) getIntent().getSerializableExtra("user");

        textView=(TextView) findViewById(R.id.textViewUser);
        editTextTitle=(EditText)findViewById(R.id.editTestTitle);
        editTextBody=(EditText)findViewById(R.id.editTextBody);

        textView.setText("Sending To : "+user.email);

        findViewById(R.id.btSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(user);
            }
        });
    }

    private void sendNotification(User user){
        String title=editTextTitle.getText().toString().trim();
        String body=editTextBody.getText().toString().trim();

        if(title.isEmpty()){
            editTextTitle.setError("Title Required");
            editTextTitle.requestFocus();
            return;
        }
        if(body.isEmpty()){
            editTextBody.setError("Body is Required");
            editTextBody.requestFocus();
            return;
        }

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://fcmnotificationexample-c0655.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api=retrofit.create(Api.class);
        Call<ResponseBody> call=api.sendNotification(user.token,title,body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(SendNotificationActivity.this,response.body().string(),Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
