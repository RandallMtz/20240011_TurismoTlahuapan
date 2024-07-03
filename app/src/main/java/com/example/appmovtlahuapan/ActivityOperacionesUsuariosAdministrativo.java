package com.example.appmovtlahuapan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ActivityOperacionesUsuariosAdministrativo extends ActivityBasic {

    private AlertDialog.Builder builder;
    private AsyncHttpClient cliente;
    private Button btBuscarUsuario, btEliminarUsuario, btInsertarUsuario, btLimpiarCampos, btModificarUsuario;
    private EditText edApellidoPaterno, edApellidoMaterno, edContraseña, edCorreo, edNombre, edTelefono, edUsuario;
    private RequestQueue requestQueue;
    private SearchableSpinner spUsuarios;
    private Spinner spNivelesAdministracion;
    private TextView lbUsuarioBusqueda, lbTitulo;

    private boolean ContraseñaVisible;
    String NivelAdministracion, UsuarioActual, UsuarioBusqueda;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operaciones_usuarios_administrativo);

        cliente = new AsyncHttpClient();

        builder = new AlertDialog.Builder(this);

        btBuscarUsuario = findViewById(R.id.ButtonUsuarioBuscarOp); btEliminarUsuario = findViewById(R.id.ButtonEliminarUsuarioOp);
        btInsertarUsuario = findViewById(R.id.ButtonInsertarUsuarioOp); btLimpiarCampos = findViewById(R.id.ButtonLimpiarCamposUsuarioOp);
        btModificarUsuario = findViewById(R.id.ButtonModificarUsuarioOp);

        edApellidoMaterno = findViewById(R.id.EditTApellidoMatUsuarioOp); edApellidoPaterno = findViewById(R.id.EditTApellidoPatUsuarioOp);
        edContraseña = findViewById(R.id.EditTContraseñaUsuarioOp);
        edCorreo = findViewById(R.id.EditTCorreoElectronicoUsuarioOp);
        edNombre = findViewById(R.id.EditTNombresUsuarioOp);
        edTelefono = findViewById(R.id.EditTTelefonoUsuarioOp);
        edUsuario = findViewById(R.id.EditTNombreUsuarioOp);
        
        lbTitulo = findViewById(R.id.TextVTituloUsuariosOp);
        lbUsuarioBusqueda = findViewById(R.id.TextVUsuarioBuscarOp);

        spNivelesAdministracion = (Spinner) findViewById(R.id.SpNivelUsuarioOp);
        spUsuarios = findViewById(R.id.SpinnerUsuariosBuscar);
        spUsuarios.setTitle("Selecciona un usuario");
        spUsuarios.setPositiveButton("Cerrar");

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        MetodoPoblarNivelesAdministracion(); MetodoPoblarUsuarios(); MetodoRecuperarUsuarioActual();

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

        btBuscarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoBuscarUsuario(ActivityAutentificacion.URLPruebas + "OperacionesUsuarios/BuscarGET.php?Usuario=" + spUsuarios.getSelectedItem() + "");
            }
        });

        btLimpiarCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoLimpiarCampos();
            }
        });

        spNivelesAdministracion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemIdAtPosition(i)==0){
                    NivelAdministracion = 4 + "";
                } else if (adapterView.getItemIdAtPosition(i)==1) {
                    NivelAdministracion = 2 + "";
                } else if (adapterView.getItemIdAtPosition(i)==2) {
                    NivelAdministracion = 1 + "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btInsertarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edTelefono.length() < 10){
                    edTelefono.setError("¡Ingresa un número valido!");
                } else {
                    if (edContraseña.length() < 8) {
                        edContraseña.setError("¡Debe contener 8 caracteres!");
                    } else {
                        MetodoValidarUsuarioInexistenteInsertar(ActivityAutentificacion.URLPruebas + "OperacionesUsuarios/ValidarUsuarioInexistente.php?Usuario="+edUsuario.getText()+"");
                    }
                }
            }
        });

        btModificarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edTelefono.length() < 10){
                    edTelefono.setError("¡Ingresa un número valido!");
                } else {
                    if (edContraseña.length() < 8) {
                        edContraseña.setError("¡Debe contener almenos 8 caracteres!");
                    } else {
                        if (!MetodoValidarCorreo(edCorreo) == false) {
                            if (!edUsuario.getText().toString().isEmpty() && !edNombre.getText().toString().isEmpty() && !edApellidoPaterno.getText().toString().isEmpty() && !edApellidoMaterno.getText().toString().isEmpty()
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

        btEliminarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edUsuario.getText().toString().isEmpty()) {
                    if (UsuarioActual.equals(edUsuario.getText().toString())) {
                        builder.setMessage("No puedes eliminar tu propia cuenta.")
                                .setCancelable(false)
                                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();
                    } else {
                        MetodoEliminarUsuario(ActivityAutentificacion.URLPruebas + "OperacionesUsuarios/EliminarPOST.php");
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MetodoPoblarUsuarios();
                            }
                        }, 100);
                    }
                } else {
                    edUsuario.setError("¡Se necesita un usuario!");
                }
            }
        });
    }

    private void MetodoPoblarUsuarios(){
        String url = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerUsuarios.php";
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    MetodoCargarUsuarios(new String((responseBody)));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void MetodoCargarUsuarios(String respuesta){
        ArrayList <AdquirirUsuarios> lista = new ArrayList<AdquirirUsuarios>();
        try {
            JSONArray jsonArreglo = new JSONArray(respuesta);
            for (int i = 0; i < jsonArreglo.length(); i++){
                AdquirirUsuarios nv = new AdquirirUsuarios();
                nv.setUsuario(jsonArreglo.getJSONObject(i).getString("Usuario"));
                lista.add(nv);
            }
            ArrayAdapter <AdquirirUsuarios> a = new ArrayAdapter <AdquirirUsuarios> (getApplicationContext(), android.R.layout.simple_dropdown_item_1line, lista);
            spUsuarios.setAdapter(a);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void MetodoPoblarNivelesAdministracion(){
        String url = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerNivelesUsuario.php";
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    MetodoCargarNivelesAdministracion(new String((responseBody)));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void MetodoCargarNivelesAdministracion(String respuesta){
        ArrayList <AdquirirNivelesAdministrativos> lista = new ArrayList<AdquirirNivelesAdministrativos>();
        try {
            JSONArray jsonArreglo = new JSONArray(respuesta);
            for (int i = 0; i < jsonArreglo.length(); i++){
                AdquirirNivelesAdministrativos nv = new AdquirirNivelesAdministrativos();
                nv.setNivel(jsonArreglo.getJSONObject(i).getString("NivelAdministracion"));
                lista.add(nv);
            }
            ArrayAdapter <AdquirirNivelesAdministrativos> a = new ArrayAdapter <AdquirirNivelesAdministrativos> (getApplicationContext(), android.R.layout.simple_dropdown_item_1line, lista);
            spNivelesAdministracion.setAdapter(a);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void MetodoBuscarUsuario (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edUsuario.setText(jsonObject.getString("Usuario"));
                        UsuarioBusqueda = jsonObject.getString("Usuario");
                        edNombre.setText(jsonObject.getString("Nombres"));
                        edApellidoPaterno.setText(jsonObject.getString("ApePaterno"));
                        edApellidoMaterno.setText(jsonObject.getString("ApeMaterno"));
                        edContraseña.setText(jsonObject.getString("Contraseña"));
                        edTelefono.setText(jsonObject.getString("Telefono"));
                        edCorreo.setText(jsonObject.getString("Correo"));
                        if (jsonObject.getString("IdNivelAdministracion").equals("4")){
                            spNivelesAdministracion.setSelection(0);
                        } else if (jsonObject.getString("IdNivelAdministracion").equals("2")){
                            spNivelesAdministracion.setSelection(1);
                        } else if (jsonObject.getString("IdNivelAdministracion").equals("1")){
                            spNivelesAdministracion.setSelection(2);
                        }
                        lbUsuarioBusqueda.setText(jsonObject.getString("Usuario"));
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

    private void MetodoValidarUsuarioInexistenteInsertar (String url){
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
                if (!MetodoValidarCorreo(edCorreo) == false){
                    if (edUsuario.getText().toString().isEmpty()) {
                        edUsuario.setError("¡Se necesita un usuario!");
                    } else if (!edNombre.getText().toString().isEmpty() && !edApellidoPaterno.getText().toString().isEmpty() && !edApellidoMaterno.getText().toString().isEmpty()
                            && !edCorreo.getText().toString().isEmpty() && !edContraseña.getText().toString().isEmpty() && !edTelefono.getText().toString().isEmpty()) {
                        MetodoInsertarUsuario(ActivityAutentificacion.URLPruebas + "OperacionesUsuarios/InsertarPOST.php");
                    } else {
                        Toast.makeText(getApplicationContext(), "¡Faltan campos por llenar!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoInsertarUsuario(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarUsuarios();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String,String>();
                parametros.put("Usuario", edUsuario.getText().toString());
                parametros.put("Nombres", edNombre.getText().toString());
                parametros.put("ApePaterno", edApellidoPaterno.getText().toString());
                parametros.put("ApeMaterno", edApellidoMaterno.getText().toString());
                parametros.put("IdNivelAdministracion", NivelAdministracion);
                parametros.put("Correo", edCorreo.getText().toString());
                parametros.put("Telefono", edTelefono.getText().toString());
                parametros.put("Contraseña", edContraseña.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoValidarUsuarioInexistenteModificar (String url){
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
                if (!MetodoValidarCorreo(edCorreo) == false){
                    if (edUsuario.getText().toString().isEmpty()) {
                        edUsuario.setError("¡Se necesita un usuario!");
                    } else if (!edNombre.getText().toString().isEmpty() && !edApellidoPaterno.getText().toString().isEmpty() && !edApellidoMaterno.getText().toString().isEmpty()
                            && !edCorreo.getText().toString().isEmpty() && !edContraseña.getText().toString().isEmpty() && !edTelefono.getText().toString().isEmpty()) {
                        MetodoActualizarUsuario(ActivityAutentificacion.URLPruebas + "OperacionesUsuarios/EditarPOST.php");
                    } else {
                        Toast.makeText(getApplicationContext(), "¡Faltan campos por llenar!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoActualizarUsuario(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarUsuarios();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String,String>();
                parametros.put("UsuarioBus", lbUsuarioBusqueda.getText().toString());
                parametros.put("Usuario", edUsuario.getText().toString());
                parametros.put("Nombres", edNombre.getText().toString());
                parametros.put("ApePaterno", edApellidoPaterno.getText().toString());
                parametros.put("ApeMaterno", edApellidoMaterno.getText().toString());
                parametros.put("IdNivelAdministracion", NivelAdministracion);
                parametros.put("Correo", edCorreo.getText().toString());
                parametros.put("Telefono", edTelefono.getText().toString());
                parametros.put("Contraseña", edContraseña.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoEliminarUsuario(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarUsuarios();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String,String>();
                parametros.put("Usuario", edUsuario.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoRecuperarUsuarioActual(){
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        UsuarioActual = (preferences.getString("Usuario",""));
        lbTitulo.setText(UsuarioActual);
    }

    public void MetodoLimpiarCampos (){
        edApellidoMaterno.setText("");
        edApellidoPaterno.setText("");
        edContraseña.setText("");
        edCorreo.setText("");
        edNombre.setText("");
        edTelefono.setText("");
        edUsuario.setText("");
        lbUsuarioBusqueda.setText("");
        spNivelesAdministracion.setSelection(0);
    }
}