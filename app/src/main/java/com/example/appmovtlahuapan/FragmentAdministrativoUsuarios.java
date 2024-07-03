package com.example.appmovtlahuapan;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class FragmentAdministrativoUsuarios extends Fragment {

    private AlertDialog.Builder builder;
    private Button btCerrarSesion;
    private CardView cdListado, cdOperaciones;
    private RequestQueue requestQueue;

    private String UsuarioActual = "";

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_administrativo_usuarios, container, false);

        builder = new AlertDialog.Builder(getContext());

        btCerrarSesion = view.findViewById(R.id.CerrarSesionAdministrativo);

        cdListado = view.findViewById(R.id.ListadoCardAdministrador);
        cdOperaciones = view.findViewById(R.id.OperacionesCardAdministrador);

        MySingleton singleton = MySingleton.getInstance(getContext());
        requestQueue = singleton.getRequestQueue();

        MetodoRecuperarUsuarioActual();

        cdOperaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityAutentificacion.NivelAdministracionUsuario.equals("Ayuntamiento")) {
                    Intent intent = new Intent(getContext(), ActivityOperacionesUsuariosAyuntamiento.class);
                    startActivity(intent);
                } else if (ActivityAutentificacion.NivelAdministracionUsuario.equals("Administrador")) {
                    Intent intent = new Intent(getContext(), ActivityOperacionesUsuariosAdministrativo.class);
                    startActivity(intent);
                }
            }
        });

        cdListado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityListadoUsuarios.class);
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