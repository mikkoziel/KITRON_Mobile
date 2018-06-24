package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.example.myfirstapp.MainActivity.EXTRA_MESSAGE;

public class LoginActivity extends AppCompatActivity {
    private static Socket socket;
    String hostName;
    int portNumber = 12345;
    public PrintWriter out;
    public BufferedReader in;
    public String rooms  = null;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void submitButton(View view) {
        EditText editText2 = findViewById(R.id.editText2);
        hostName = editText2.getText().toString();

        new AsyncAction().execute();
        intent = new Intent(this, RoomsViewActivity.class);

    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncAction extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... args) {
            if(hostName != null) {
                try {
                    socket = new Socket(hostName, portNumber);


                    // in & out streams
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                EditText editText3 = findViewById(R.id.editText3);
                String message = editText3.getText().toString();

                String msg = "initPlayer ".concat(" ".concat(message));

                //
                if (message.equals("")) {
                    // AlertView alert = new AlertView(owner, "Please enter your name!");

                } else {
                    out.println(msg);

                    String response = null;
                    try {
                        response = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(response);

                    if (response.contains("init OK")) {
                        rooms = setRooms();
                        intent.putExtra(EXTRA_MESSAGE, rooms);
                        startActivity(intent);
                    }
                }

            }
            return null;
        }
    }

    public String setRooms(){
        out.println("roomList");
        String line = null;
        while(line == null) {
            try {
                line = in.readLine();
            } catch (IOException e1) {

            }
        }
        System.out.println(line);
        return line;
    }

    public static Socket getSocket() {
        return socket;
    }
}
