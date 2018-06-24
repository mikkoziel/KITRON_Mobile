package com.example.myfirstapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewRoomActivity extends AppCompatActivity {
    public Socket socket;
    public PrintWriter out;
    public BufferedReader in;

    public String message;
    public String number;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_room);

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

    public void onAdd(View view) {
        EditText editText2 = findViewById(R.id.editText4);
        message = editText2.getText().toString();
        number = "";

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox2);
        if (checkBox.isChecked()) {
            number = "2";
        }
        final CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox3);
        if (checkBox1.isChecked()) {
            number = "3";
        }
        final CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox4);
        if (checkBox2.isChecked()) {
            number = "4";
        }

        new AsyncAction1().execute();
        intent = new Intent(this, WaitingActivity.class);

    }

    private class AsyncAction1 extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... args) {
            if (number.equals("")){
                System.out.println("Brak wyboru");
            }
            else {
                if (message.equals("")) {
                    System.out.println("Brak nazwy");
                    //      AlertView alert = new AlertView(owner, "Please enter room name!");
                } else {
                    out.println("hostRoom " + message + " " + number);
                    String msg = null;
                    try {
                        msg = in.readLine();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (msg.contains("success")) {
                        startActivity(intent);
                    }
                }
            }

            return null;
        }
    }
}
