package com.example.appmovtlahuapan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

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

public class ActivityListadoUsuarios extends ActivityBasic {

    private List<AdquirirUsuarios> UsuariosLista;
    private RecyclerView rcUsuarios;
    private RequestQueue requestQueue;
    private static String URLUsuarios = ActivityAutentificacion.URLPruebas + "Componentes/RecyclerUsuarios.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_usuarios);

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        UsuariosLista = new ArrayList<>();

        rcUsuarios = (RecyclerView) findViewById(R.id.RecyclerEmpleadosAdministrador);
        rcUsuarios.setHasFixedSize(true);
        rcUsuarios.setLayoutManager(new LinearLayoutManager(this));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLUsuarios, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject usuario = array.getJSONObject(i);
                        UsuariosLista.add(new AdquirirUsuarios(
                                usuario.getString("Usuario"),
                                usuario.getString("Nombres"),
                                usuario.getString("ApePaterno"),
                                usuario.getString("ApeMaterno"),
                                usuario.getString("Correo"),
                                usuario.getString("Telefono")
                        ));
                    }
                    AdaptadorUsuarios adapterUsuarios = new AdaptadorUsuarios(ActivityListadoUsuarios.this, UsuariosLista);
                    rcUsuarios.setAdapter(adapterUsuarios);
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