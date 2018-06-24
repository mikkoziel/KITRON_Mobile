package com.example.myfirstapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static android.support.v4.content.ContextCompat.startActivity;

public class WaitingActivity extends AppCompatActivity {
    public Socket socket;
    public PrintWriter out;
    public BufferedReader in;

    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        try {
            createSocket();
            waitForRespponse();
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

    public void waitForRespponse() throws IOException {
        intent = new Intent(this, GameActivity.class);
        new AsyncAction2().execute();

    }

    private class AsyncAction2 extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... args) {
            String response = null;
            try {
                response = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.contains("startGame")) {
                startActivity(intent);
            }
            return null;
        }
    }
}
