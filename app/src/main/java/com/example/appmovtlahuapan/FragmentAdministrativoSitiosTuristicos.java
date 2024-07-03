package com.example.appmovtlahuapan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentAdministrativoSitiosTuristicos extends Fragment {

    private AlertDialog.Builder builder;
    private Button btCerrarSesion, btEliminarEvento, btEliminarEventos;
    private CardView cdOperacionesActividades, cdOperacionCategorias, cdOperacionesEventos, cdOperacionesHorarios, cdSubirImagenes, cdOperacionLugares;
    private RequestQueue requestQueue;
    private String UsuarioActual = "";

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_administrativo_sitios_turisticos, container, false);

        builder = new AlertDialog.Builder(getContext());

        btCerrarSesion = view.findViewById(R.id.CerrarSesionAmanuense);
        btEliminarEvento = view.findViewById(R.id.eliminarUnEvento);
        btEliminarEventos = view.findViewById(R.id.eliminarEventos);

        cdOperacionesActividades = view.findViewById(R.id.ActividadesCard); cdOperacionCategorias = view.findViewById(R.id.LugaresCategoriasCard);
        cdOperacionesEventos = view.findViewById(R.id.EventosCard); cdOperacionesHorarios = view.findViewById(R.id.HorariosCard);
        cdSubirImagenes = view.findViewById(R.id.LugaresImagenesCard); cdOperacionLugares = view.findViewById(R.id.LugaresCard);

        MySingleton singleton = MySingleton.getInstance(getContext());
        requestQueue = singleton.getRequestQueue();

        if (ActivityAutentificacion.NivelAdministracionUsuario.equals("Amanuense")){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            layoutParams.width = 0;
            layoutParams.height = 0;

            cdOperacionCategorias.setLayoutParams(layoutParams);
            cdOperacionesActividades.setLayoutParams(layoutParams);
            cdOperacionesEventos.setLayoutParams(layoutParams);

            cdOperacionCategorias.setEnabled(false);
            cdOperacionesActividades.setEnabled(false);
            cdOperacionesEventos.setEnabled(false);
            cdOperacionCategorias.setBackgroundColor(Color.rgb(225, 225, 225));
            cdOperacionesActividades.setBackgroundColor(Color.rgb(225, 225, 225));
            cdOperacionesEventos.setBackgroundColor(Color.rgb(225, 225, 225));
            btEliminarEventos.setVisibility(View.INVISIBLE);
            btEliminarEventos.setEnabled(false);
            btEliminarEvento.setVisibility(View.INVISIBLE);
            btEliminarEvento.setEnabled(false);
        } else {
            btCerrarSesion.setEnabled(false);
            btCerrarSesion.setVisibility(View.INVISIBLE);
        }

        MetodoRecuperarUsuarioActual();

        cdOperacionCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityOperacionesCategorias.class);
                startActivity(intent);
            }
        });

        cdOperacionLugares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityOperacionesLugares.class);
                startActivity(intent);
            }
        });

        cdSubirImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityListadoLugaresNombres.class);
                startActivity(intent);
            }
        });

        cdOperacionesActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityOperacionesActividades.class);
                startActivity(intent);
            }
        });

        cdOperacionesEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityOperacionesEventos.class);
                startActivity(intent);
            }
        });

        cdOperacionesHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityOperacionesHorarios.class);
                startActivity(intent);
            }
        });

        btEliminarEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("¿Estas seguro de eliminar los eventos que ya han sucedido?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MetodoEliminarEventosPasados(ActivityAutentificacion.URLPruebas + "OperacionesEventos/Purgar.php");
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

        btEliminarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityListadoEventosBotones.class);
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

    private void MetodoEliminarEventosPasados(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getView().getContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getView().getContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        });
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