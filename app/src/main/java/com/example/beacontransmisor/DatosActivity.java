package com.example.beacontransmisor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DatosActivity extends AppCompatActivity {


    ArrayList<String> tiemposList;
    ArrayAdapter<String> listAdapter;
    ListView listaTiempos;
    Handler mainHadler = new Handler();
    ProgressDialog progressDialog;

    String DNI;

    protected static final String TAG = "Fran";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        DNI = getIntent().getStringExtra("dni");
        String url = "http://192.168.4.1/"+DNI+".json";
        Log.e(TAG,url);

        initializeUserList();
        new fetchData().start();


    }

    private void initializeUserList() {

        tiemposList = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,tiemposList);
        listaTiempos = (ListView)findViewById(R.id.datosList);
        listaTiempos.setAdapter(listAdapter);

    }

    class fetchData extends Thread{
        String data = " ";



        @Override
        public void run() {

            mainHadler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(DatosActivity.this);
                    progressDialog.setMessage("Fletching data");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                }
            });
            try {
                URL url = new URL("http://192.168.4.1/"+DNI+".json");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();  //para abrir la conexion
                InputStream inputStream =  httpURLConnection.getInputStream();     //recibimos los datos
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));   //se le pasan los datos el lector
                String line;

                while((line = bufferedReader.readLine()) != null){    //se leen los datos linea a linea
                    data = data + line;
                }

                if(!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);  //meto los datos en un obejto de tipo JSON
                    JSONArray users =jsonObject.getJSONArray("tiempos");    //saco del JSON los datos que me interesan con el identificador correspondiente
                    tiemposList.clear(); //para limpiar el array cada vez que pulse el boton
                    for(int i = 0;i<users.length();i++){

                        JSONObject names = users.getJSONObject(i);  //guardo en un json todos los nombres
                        String name = names.getString("nodo_1");
                        tiemposList.add(name);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mainHadler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    listAdapter.notifyDataSetChanged();
                }
            });

        }
    }
}
