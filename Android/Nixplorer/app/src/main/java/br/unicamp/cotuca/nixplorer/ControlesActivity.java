package br.unicamp.cotuca.nixplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ControlesActivity extends AppCompatActivity {
    Button btnAjustar, btnConectar, btnDesconectar;
    EditText edtIp, edtPorta;

    private Socket socket = null;

    private static int SERVERPORT = 80;
    private static String SERVER_IP = "192.168.0.92";
    boolean  isRunning = false;

    private PrintWriter out;
    private BufferedReader input;

    private String val = "ok";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controles);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAjustar = findViewById(R.id.btnAjustar);
        btnConectar = findViewById(R.id.btnConectar);
        btnDesconectar = findViewById(R.id.btnDesconectar);
        edtIp = findViewById(R.id.edtIp);
        edtPorta = findViewById(R.id.edtPorta);

        btnConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtIp.getText().toString().trim().isEmpty()) {
                    SERVER_IP = edtIp.getText().toString().trim();
                    SERVERPORT = Integer.parseInt(edtPorta.getText().toString().trim());
                }
                if (socket != null && !socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                new Thread(new ClientThread()).start();
            }
        });

        btnDesconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    val = "\r\n\r\n";
                    new Thread(new OutThread()).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnAjustar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    val = "1";
                    new Thread(new OutThread()).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.controles, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id != R.id.action_info)
        {
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);
            return true;
        }
        return true;
    }

    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);
                isRunning = true;
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                new Thread(new OutThread()).start();
                new Thread(new Thread2()).start();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    class OutThread implements Runnable{
        @Override
        public void run() {
            out.write(val);
            out.flush();
        }
    }

    private class Thread2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    final String message = input.readLine();
                    if (message != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //response.setText("client: " + message + "\n");
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
