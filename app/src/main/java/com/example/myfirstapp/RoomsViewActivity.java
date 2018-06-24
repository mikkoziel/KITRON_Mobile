package com.example.myfirstapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RoomsViewActivity extends AppCompatActivity {
    public Socket socket;
    public PrintWriter out;
    public BufferedReader in;

    public Intent intent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms_view);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView7);
        textView.setText(message);
        try {
            createSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createSocket() throws IOException {
        socket = LoginActivity.getSocket();
        // in & out streams
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void returnOn(View view){
        finish();
    }

    public void onRefresh(View view){

    }

    public void onAdd(View view){
        Intent intent = new Intent(this, NewRoomActivity.class);
        startActivity(intent);
    }

    public void onChoose(View view) {
        intent1 = new Intent(this, WaitingActivity.class);
        new AsyncAction().execute();
    }

    private class AsyncAction extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... args) {
            String room = "";
            if(room != null) {
                out.println("joinRoom " + room);
                String msg = null;
                try {
                    msg = in.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (msg.contains("success")) {
                    startActivity(intent1);
                }
            }
            return null;
        }
    }



}
