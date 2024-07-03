package com.example.appmovtlahuapan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityListadoEliminarImagenes extends ActivityBasic {

    private List<AdquirirSitioTuristicoImagenes> ImagenesLista;
    private RecyclerView rcImagenes;
    private RequestQueue requestQueue;
    
    private int IdLugarActivityAnterior;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_eliminar_imagenes);

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        ImagenesLista = new ArrayList<>();

        Intent intent = getIntent();
        IdLugarActivityAnterior = intent.getExtras().getInt("IdLugar");

        rcImagenes = (RecyclerView) findViewById(R.id.RecyclerEliminarImagenes);
        rcImagenes.setHasFixedSize(true);
        rcImagenes.setLayoutManager(new LinearLayoutManager(this));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ActivityAutentificacion.URLPruebas + "Componentes/RecyclerImagenesIdLugar.php?IdLugar=" + IdLugarActivityAnterior + "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject lugar = array.getJSONObject(i);
                        ImagenesLista.add(new AdquirirSitioTuristicoImagenes(
                                lugar.getString("IdImagen"),
                                lugar.getString("Foto"),
                                lugar.getString("Nombre"),
                                lugar.getString("IdLugar"),
                                lugar.getString("Descripcion")
                        ));
                    }
                    AdaptadorEliminarImagenes adapterLugares = new AdaptadorEliminarImagenes(ActivityListadoEliminarImagenes.this, ImagenesLista);
                    rcImagenes.setAdapter(adapterLugares);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        requestQueue.add(stringRequest);
    }
}