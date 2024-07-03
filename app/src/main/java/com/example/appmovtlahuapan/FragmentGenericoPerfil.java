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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentGenericoPerfil extends Fragment {

    private AlertDialog.Builder builder;
    private AsyncHttpClient cliente;
    private TextView lbApellidoPaterno, lbCorreo, lbNombre, lbTelefono, lbUsuario;
    private Button btEliminarPerfil, btModificarPerfil;
    private RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_generico_perfil, container, false);

        cliente = new AsyncHttpClient();

        btEliminarPerfil = view.findViewById(R.id.buttonEliminarPerfilGenerico); btModificarPerfil = view.findViewById(R.id.buttonModificarPerfilGenerico);

        builder = new AlertDialog.Builder(getContext());

        lbApellidoPaterno = view.findViewById(R.id.TextVApePatPerfilGenerico);
        lbCorreo = view.findViewById(R.id.TextVCorreoPerfilGenerico);
        lbNombre = view.findViewById(R.id.TextVNombrePerfilGenerico);
        lbTelefono = view.findViewById(R.id.TextVTelefonoPerfilGenerico);
        lbUsuario = view.findViewById(R.id.TextVUsuarioPerfilGenerico);

        MySingleton singleton = MySingleton.getInstance(getContext());
        requestQueue = singleton.getRequestQueue();

        MetodoRecuperarIdUsuario();

        btModificarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityModificarPerfilGenerico.class);
                startActivity(intent);
            }
        });

        btEliminarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("¿Deseas eliminar tu cuenta?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MetodoEliminarPerfilCliente(ActivityAutentificacion.URLPruebas + "OperacionesUsuarios/EliminarPOSTPerfilGenerico.php");
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

        MetodoBuscarPerfilCliente(ActivityAutentificacion.URLPruebas + "OperacionesUsuarios/BuscarGETPerfilGenerico.php?IdUsuario=" + lbUsuario.getText().toString() + "");

        return view;
    }

    private void MetodoBuscarPerfilCliente (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        lbUsuario.setText(jsonObject.getString("Usuario"));
                        lbNombre.setText(jsonObject.getString("Nombres"));
                        lbApellidoPaterno.setText(jsonObject.getString("ApePaterno") + " " + jsonObject.getString("ApeMaterno"));
                        lbTelefono.setText(jsonObject.getString("Telefono"));
                        lbCorreo.setText(jsonObject.getString("Correo"));
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Usuario inexistente", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoEliminarPerfilCliente(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoGuardarUsuarioYContraseña();
                Intent i = new Intent(getContext(), ActivityAutentificacion.class);
                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String,String>();
                parametros.put("IdUsuario", ActivityAutentificacion.IdUsuario);
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

    private void MetodoRecuperarIdUsuario(){
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        lbUsuario.setText(preferences.getString("IdUsuario",""));
    }
}