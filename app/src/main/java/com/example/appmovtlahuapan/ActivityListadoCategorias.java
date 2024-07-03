package com.example.appmovtlahuapan;

import android.annotation.SuppressLint;
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

public class ActivityListadoCategorias extends ActivityBasic {

    private List<AdquirirCategoriasTuristicas> CategoriasLista;
    private RecyclerView rcCategorias;
    private RequestQueue requestQueue;
    
    private String URLCategorias = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerCategorias.php";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_categorias);

        CategoriasLista = new ArrayList<>();

        rcCategorias = (RecyclerView) findViewById(R.id.RecyclerCategorias);
        rcCategorias.setHasFixedSize(true);
        rcCategorias.setLayoutManager(new LinearLayoutManager(this));

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLCategorias, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject lugar = array.getJSONObject(i);
                        CategoriasLista.add(new AdquirirCategoriasTuristicas(
                                lugar.getInt("IdCategoria"),
                                lugar.getString("Categoria")
                        ));
                    }
                    AdaptadorCategoriasTuristicas adapterLugares = new AdaptadorCategoriasTuristicas(ActivityListadoCategorias.this, CategoriasLista);
                    rcCategorias.setAdapter(adapterLugares);
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