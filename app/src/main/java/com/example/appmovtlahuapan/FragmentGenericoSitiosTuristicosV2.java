package com.example.appmovtlahuapan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentGenericoSitiosTuristicosV2 extends Fragment {

    private AlertDialog.Builder builder;
    private Button btCerrarSesion;
    private CardView cdEventos;
    private List<AdquirirCategoriasTuristicas> CategoriasLista;
    private RecyclerView rcCategorias;
    private RequestQueue requestQueue;

    private String  UsuarioActual = "", URLLugaresNombres = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerCategorias.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_generico_sitios_turisticos_v2, container, false);

        builder = new AlertDialog.Builder(getContext());

        btCerrarSesion = view.findViewById(R.id.cerrarSesionGenericoV2);

        cdEventos = view.findViewById(R.id.EventosCardV2);

        CategoriasLista = new ArrayList<>();

        rcCategorias = (RecyclerView) view.findViewById(R.id.RecyclerCategorias);
        rcCategorias.setHasFixedSize(true);
        rcCategorias.setLayoutManager(new LinearLayoutManager(view.getContext()));

        MySingleton singleton = MySingleton.getInstance(getContext());
        requestQueue = singleton.getRequestQueue();

        MetodoRecuperarUsuarioActual();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLLugaresNombres, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject lugar = array.getJSONObject(i);
                        CategoriasLista.add(new AdquirirCategoriasTuristicas(
                                lugar.getInt("IdCategoria"),
                                lugar.getString("Categoria"),
                                lugar.getString("FotoPromocional")
                        ));
                    }
                    AdaptadorCategoriasTuristicas adapterLugares = new AdaptadorCategoriasTuristicas(view.getContext(), CategoriasLista);
                    rcCategorias.setAdapter(adapterLugares);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        requestQueue.add(stringRequest);

        cdEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityListadoEventos.class);
                startActivity(intent);
            }
        });

        btCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("¿Deseas cerrar sesión?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MetodoCerrarSesion(ActivityAutentificacion.URLPruebas + "CerrarSesionPOST.php");
                                MetodoGuardarUsuarioYContraseña();
                                Intent intent = new Intent(getContext(), ActivityAutentificacion.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        });

        return view;
    }

    private void MetodoCerrarSesion(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                } catch (Exception e) {
                    Toast.makeText(getContext(), "" + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String,String>();
                parametros.put("Usuario", UsuarioActual);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void MetodoGuardarUsuarioYContraseña(){
        SharedPreferences preferences = requireContext().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Usuario", "");
        editor.putString("Contrasena", "");
        editor.putString("IdNivel", "");
        editor.putBoolean("sesion", true);
        editor.commit();
    }

    private void MetodoRecuperarUsuarioActual(){
        SharedPreferences preferences = requireContext().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        UsuarioActual = (preferences.getString("Usuario",""));
    }
}