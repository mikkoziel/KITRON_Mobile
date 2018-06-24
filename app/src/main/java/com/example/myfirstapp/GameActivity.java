package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.myfirstapp.logic.MyDrawable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameActivity extends AppCompatActivity {
    public Socket socket;
    public PrintWriter out;
    public BufferedReader in;

    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        MyDrawable mydrawing = new MyDrawable();
        ImageView image = findViewById(R.id.imageView);
        image.setImageDrawable(mydrawing);

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

    public void onMenu(View view) {
        intent = new Intent(this, MainActivity.class);
        new AsyncAction().execute();
    }

    public void onChange(View view) {
        intent = new Intent(this, RoomsViewActivity.class);
        new AsyncAction().execute();

    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncAction extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... args) {
            out.println("leaveRoom");
            String response = null;
            try {
                response = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response.contains("startGame")) {
                startActivity(intent);
            }
            return null;
        }
    }

}
