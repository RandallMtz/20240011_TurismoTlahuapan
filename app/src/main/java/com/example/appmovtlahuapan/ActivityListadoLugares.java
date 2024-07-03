package com.example.appmovtlahuapan;

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

public class ActivityListadoLugares extends ActivityBasic {

    private List<AdquirirLugares> LugaresLista;
    private RecyclerView rcLugares;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_lugares);

        Intent intent = getIntent();
        String CategoriaActivityAnterior = intent.getExtras().getString("Valor");

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        LugaresLista = new ArrayList<>();

        rcLugares = (RecyclerView) findViewById(R.id.RecyclerLugaresCliente);
        rcLugares.setHasFixedSize(true);
        rcLugares.setLayoutManager(new LinearLayoutManager(this));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ActivityAutentificacion.URLPruebas + "Componentes/RecyclerLugares.php?Categoria="+CategoriaActivityAnterior+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject lugar = array.getJSONObject(i);
                        LugaresLista.add(new AdquirirLugares(
                                lugar.getString("Nombre"),
                                lugar.getInt("CostoEntrada"),
                                lugar.getString("Calle"),
                                lugar.getString("NoInterior"),
                                lugar.getString("NoExterior"),
                                lugar.getString("Colonia")
                        ));
                    }
                    AdaptadorLugares adapterLugares = new AdaptadorLugares(ActivityListadoLugares.this, LugaresLista);
                    rcLugares.setAdapter(adapterLugares);
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