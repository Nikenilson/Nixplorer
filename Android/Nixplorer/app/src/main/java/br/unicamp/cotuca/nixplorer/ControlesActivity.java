package br.unicamp.cotuca.nixplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

public class ControlesActivity extends AppCompatActivity {
    Button btnAjustar, btnConectar, btnDesconectar;
    EditText edtIp, edtPorta;
    Spinner spPlanetas, spGrausH, spGrausV;
    TextView tvSaidaTeste;

    private Socket socket = null;
    private static int SERVERPORT = 80;
    private static String SERVER_IP = "192.168.0.92";
    boolean  isRunning = false;
    private PrintWriter out;
    private BufferedReader input;
    private String val = "";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controles);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spPlanetas = findViewById(R.id.spPlanetas);
        spGrausH = findViewById(R.id.spGrausH);
        spGrausV = findViewById(R.id.spGrausV);
        btnAjustar = findViewById(R.id.btnAjustar);
        btnConectar = findViewById(R.id.btnConectar);
        btnDesconectar = findViewById(R.id.btnDesconectar);
        edtIp = findViewById(R.id.edtIp);
        edtPorta = findViewById(R.id.edtPorta);
        tvSaidaTeste = findViewById(R.id.tvSaidaTeste);

        String[] arraySpinnerPlanetas = new String[] { "Jupiter","Lua", "M42" };
        String[] arraySpinnerGrausH = new String[] { "Graus","10º" };
        String[] arraySpinnerGrausV = new String[] { "Graus","10º" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerPlanetas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerGrausH);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerGrausV);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPlanetas.setAdapter(adapter);
        spGrausH.setAdapter(adapter2);
        spGrausV.setAdapter(adapter3);

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
                    val = "\r\n";
                    new Thread(new OutThread()).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        queue = Volley.newRequestQueue(this);
        btnAjustar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCoords(spPlanetas.getSelectedItem().toString(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        String coordsVet[] = result.split(" ");
                        Integer c1 = Math.round(Float.parseFloat(coordsVet[0]));
                        Integer c2 = Math.round(Float.parseFloat(coordsVet[1]));
                        try {
                            String ra = "", dec = "";
                            Integer raI = (c1*2048)/360;
                            Integer decI = (c2*2048)/360;
                            if(String.valueOf(raI).length() < 4)
                                ra += "0";
                            if(Math.signum(decI) == -1)
                            {
                                decI *= -1;
                                if(String.valueOf(decI).length() < 4)
                                    dec += "-0";
                                dec += decI.toString();
                            }
                            else
                            {
                                if(String.valueOf(decI).length() < 4)
                                    dec += "0";
                                dec += decI.toString();
                            }
                            ra += raI.toString();
                            val = ra + ";" + dec;
                            new Thread(new OutThread()).start();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void getCoords(String corpo, final VolleyCallback callback)
    {
        tvSaidaTeste.setText("");
        String url = "http://host-python.herokuapp.com/astropy/" + corpo;
        StringRequest request = null;

        try{
            request = new StringRequest (Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    callback.onSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
        }catch (Exception ex){
            Log.e("Mensagem: ", ex.getMessage() + " AQUI!");
        }
        queue.add(request);
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
