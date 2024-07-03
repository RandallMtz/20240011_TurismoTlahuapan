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

public class ActivityListadoEventosBotones extends ActivityBasic {

    private List<AdquirirEventosProximos> EventosLista;
    private RecyclerView rcEventos;
    private RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_eventos_botones);

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        EventosLista = new ArrayList<>();

        rcEventos = (RecyclerView) findViewById(R.id.RecyclerEventosBotones);
        rcEventos.setHasFixedSize(true);
        rcEventos.setLayoutManager(new LinearLayoutManager(this));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ActivityAutentificacion.URLPruebas + "Componentes/RecyclerEventosEliminar.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject lugar = array.getJSONObject(i);
                        EventosLista.add(new AdquirirEventosProximos(
                                lugar.getString("IdEvento"),
                                lugar.getString("Nombre"),
                                lugar.getString("Descripcion"),
                                lugar.getString("Foto"),
                                lugar.getString("FotoNombre"),
                                lugar.getString("Fecha"),
                                lugar.getString("FechaFinal"),
                                lugar.getString("Hora"),
                                lugar.getString("HoraFinal"),
                                lugar.getString("Lugar")
                        ));
                    }
                    AdaptadorEventosBotones adapterEventos = new AdaptadorEventosBotones(ActivityListadoEventosBotones.this, EventosLista);
                    rcEventos.setAdapter(adapterEventos);
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