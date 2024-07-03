package com.example.appmovtlahuapan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ActivityModificarPerfilGenerico extends ActivityBasic {

    private AsyncHttpClient cliente;
    private Button btModificarPerfil;
    private EditText edApellidoMaterno, edApellidoPaterno, edContraseña, edCorreo, edNombres, edTelefono, edUsuario;
    private RequestQueue requestQueue;
    
    private boolean ContraseñaVisible;
    private String UsuarioBusqueda, UsuarioActual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_perfil_generico);

        cliente = new AsyncHttpClient();

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        btModificarPerfil = findViewById(R.id.BtModificarModificarPerfilGenerico);

        edApellidoMaterno = findViewById(R.id.EditTApeMatModificarPerfilGenerico); edApellidoPaterno = findViewById(R.id.EditTApePatModificarPerfilGenerico);
        edContraseña = findViewById(R.id.EditTContraseñaModificarPerfilGenerico);
        edCorreo = findViewById(R.id.EditTCorreoModificarPerfilGenerico);
        edNombres = findViewById(R.id.EditTNombresModificarPerfilGenerico);
        edTelefono = findViewById(R.id.EditT1erTelefonoModificarPerfilGenerico);
        edUsuario = findViewById(R.id.EditTUsuarioModificarPerfilGenerico);

        MetodoRecuperarIdUsuario(); MetodoBuscarUsuario(ActivityAutentificacion.URLPruebas + "OperacionesUsuarios/BuscarGETIdUsuario.php?IdUsuario=" + UsuarioActual + "");

        edContraseña.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= edContraseña.getRight()-edContraseña.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = edContraseña.getSelectionEnd();
                        if (ContraseñaVisible) {
                            edContraseña.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            edContraseña.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            ContraseñaVisible = false;
                        } else {
                            edContraseña.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            edContraseña.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            ContraseñaVisible = true;
                        }
                        edContraseña.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        btModificarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edTelefono.length() < 10) {
                    edTelefono.setError("¡Ingresa un número valido!");
                } else {
                    if (edContraseña.length() < 8) {
                        edContraseña.setError("¡Debe contener 8 caracteres!");
                    } else {
                        if (!MetodoValidarCorreo(edCorreo) == false) {
                            if (!edUsuario.getText().toString().isEmpty() && !edNombres.getText().toString().isEmpty() && !edApellidoPaterno.getText().toString().isEmpty() && !edApellidoMaterno.getText().toString().isEmpty()
                                    && !edCorreo.getText().toString().isEmpty() && !edContraseña.getText().toString().isEmpty() && !edTelefono.getText().toString().isEmpty()) {
                                MetodoValidarUsuarioInexistenteModificar(ActivityAutentificacion.URLPruebas + "OperacionesUsuarios/ValidarUsuarioInexistenteModificar.php?Usuario=" + edUsuario.getText().toString() + "&Usuario1=" + UsuarioBusqueda + "");
                            } else if (edUsuario.getText().toString().isEmpty()) {
                                edUsuario.setError("¡Se necesita un usuario!");
                            } else {
                                Toast.makeText(getApplicationContext(), "¡Faltan campos por llenar!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });
    }

    private void MetodoBuscarUsuario(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        UsuarioBusqueda = jsonObject.getString("Usuario");
                        edUsuario.setText(jsonObject.getString("Usuario"));
                        edNombres.setText(jsonObject.getString("Nombres"));
                        edApellidoPaterno.setText(jsonObject.getString("ApePaterno"));
                        edApellidoMaterno.setText(jsonObject.getString("ApeMaterno"));
                        edContraseña.setText(jsonObject.getString("Contraseña"));
                        edTelefono.setText(jsonObject.getString("Telefono"));
                        edCorreo.setText(jsonObject.getString("Correo"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Usuario inexistente", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoValidarUsuarioInexistenteModificar(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edUsuario.setError("¡Intenta con un usuario distinto!");
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!MetodoValidarCorreo(edCorreo) == false) {
                    if (edUsuario.getText().toString().isEmpty()) {
                        edUsuario.setError("¡Se necesita un usuario!");
                    } else if (!edNombres.getText().toString().isEmpty() && !edApellidoPaterno.getText().toString().isEmpty() && !edApellidoMaterno.getText().toString().isEmpty()
                            && !edCorreo.getText().toString().isEmpty() && !edContraseña.getText().toString().isEmpty() && !edTelefono.getText().toString().isEmpty()) {
                        MetodoActualizarUsuario(ActivityAutentificacion.URLPruebas + "OperacionesUsuarios/EditarPOSTPerfilGenerico.php");
                        Intent intent = new Intent(getApplicationContext(), ActivityBottomMenuCliente.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "¡Faltan campos por llenar!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoActualizarUsuario(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                edUsuario.setText("");
                edNombres.setText("");
                edApellidoPaterno.setText("");
                edApellidoMaterno.setText("");
                edTelefono.setText("");
                edCorreo.setText("");
                edContraseña.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("UsuarioBus", UsuarioBusqueda);
                parametros.put("Usuario", edUsuario.getText().toString());
                parametros.put("Nombre", edNombres.getText().toString());
                parametros.put("ApePater", edApellidoPaterno.getText().toString());
                parametros.put("ApeMater", edApellidoMaterno.getText().toString());
                parametros.put("Correo", edCorreo.getText().toString());
                parametros.put("Telefono", edTelefono.getText().toString());
                parametros.put("Contraseña", edContraseña.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoRecuperarIdUsuario(){
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        UsuarioActual = preferences.getString("IdUsuario","");
    }
}