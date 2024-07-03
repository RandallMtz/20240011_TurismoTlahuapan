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

public class ActivityListadoEliminarComentarios extends ActivityBasic {

    private List<AdquirirComentarios> ComentariosLista;
    private RecyclerView rcComentarios;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_eliminar_comentarios);

        Intent intent = getIntent();
        String NombreActivityAnterior = intent.getExtras().getString("Nombre");

        ComentariosLista = new ArrayList<>();

        rcComentarios = (RecyclerView) findViewById(R.id.RecyclerEliminarComentarios);
        rcComentarios.setHasFixedSize(true);
        rcComentarios.setLayoutManager(new LinearLayoutManager(this));

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ActivityAutentificacion.URLPruebas + "Componentes/RecyclerComentariosNombreLugar.php?Nombre=" + NombreActivityAnterior + "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject lugar = array.getJSONObject(i);
                        ComentariosLista.add(new AdquirirComentarios(
                                lugar.getString("IdComentario"),
                                lugar.getString("Comentario"),
                                lugar.getString("Fecha"),
                                lugar.getString("IdUsuario"),
                                lugar.getString("IdLugar"),
                                lugar.getString("Nombre")
                        ));
                    }
                    AdaptadorEliminarComentarios adapterLugares = new AdaptadorEliminarComentarios(ActivityListadoEliminarComentarios.this, ComentariosLista);
                    rcComentarios.setAdapter(adapterLugares);
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