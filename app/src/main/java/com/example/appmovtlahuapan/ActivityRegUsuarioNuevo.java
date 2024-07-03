package com.example.appmovtlahuapan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ActivityRegUsuarioNuevo extends AppCompatActivity {

    private Button btRegistrarUsuarioNuevo;
    private EditText edApellidoPaterno, edApellidoMaterno, edContraseña, edCorreo, edNombre, edTelefono, edUsuario;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{6,}" +
                    "$");
    private RequestQueue requestQueue;
    private String ApellidoPaterno, ApellidoMaterno, Contraseña, Correo, Nombre,  Telefono, Usuario;
    private TextView lbayudaValidar;
    
    private boolean ContraseñaVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_usuario_nuevo);

        btRegistrarUsuarioNuevo = findViewById(R.id.ButtonFinalizarRegN);

        edApellidoMaterno = (EditText) findViewById((R.id.EditTApeMatRegN)); edApellidoPaterno = (EditText) findViewById((R.id.EditTApePatRegN));
        edContraseña = (EditText) findViewById(R.id.EditTContraseñaRegN);
        edCorreo = (EditText) findViewById((R.id.EditTCorreoRegN));
        edNombre = (EditText) findViewById((R.id.EditTNombresRegN));
        edTelefono = (EditText) findViewById((R.id.EditTTelefonoRegN));
        edUsuario = (EditText) findViewById(R.id.EditTUsuarioRegN);
        
        lbayudaValidar = findViewById(R.id.TextVApoyoDatosPersonales);

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        edContraseña.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= edContraseña.getRight()-edContraseña.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = edContraseña.getSelectionEnd();
                        if (ContraseñaVisible) {
                            edContraseña.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_lock_24,0,R.drawable.baseline_visibility_off_24,0);
                            edContraseña.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            ContraseñaVisible = false;
                        } else {
                            edContraseña.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_lock_24,0,R.drawable.baseline_visibility_24,0);
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

        btRegistrarUsuarioNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edContraseña.length() < 8) {
                    edContraseña.setError("¡Debe contener 8 caracteres!");
                } else {
                    MetodoValidarUsuarioInexistenteInsertar(ActivityAutentificacion.URLPruebas + "InsertarUsuariosNuevos/ValidarUsuarioInexistente.php?Usuario=" + edUsuario.getText()+"");
                }
            }
        });
    }

    private void MetodoValidarUsuarioInexistenteInsertar(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        lbayudaValidar.setText(jsonObject.getString("Usuario"));
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
                    Usuario = edUsuario.getText().toString();
                    Nombre = edNombre.getText().toString();
                    ApellidoPaterno = edApellidoPaterno.getText().toString();
                    ApellidoMaterno = edApellidoMaterno.getText().toString();
                    Telefono = edTelefono.getText().toString();
                    Correo = edCorreo.getText().toString();
                    Contraseña = edContraseña.getText().toString();
                    if (!Nombre.isEmpty() && !ApellidoPaterno.isEmpty() && !ApellidoMaterno.isEmpty() && !Telefono.isEmpty() && !Correo.isEmpty() && !Contraseña.isEmpty()) {
                        MetodoInsertarUsuario(ActivityAutentificacion.URLPruebas + "InsertarUsuariosNuevos/InsertarUsuariosNuevosPOST.php");

                    } else {
                        Toast.makeText(getApplicationContext(), "¡Faltan campos por llenar!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoInsertarUsuario (String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                edNombre.setText("");
                edApellidoPaterno.setText("");
                edApellidoMaterno.setText("");
                edTelefono.setText("");
                edCorreo.setText("");
                edContraseña.setText("");
                Toast.makeText(getApplicationContext(),"¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ActivityAutentificacion.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String,String>();
                parametros.put("Usuario", Usuario);
                parametros.put("Nombre", Nombre);
                parametros.put("ApePater", ApellidoPaterno);
                parametros.put("ApeMater", ApellidoMaterno);
                parametros.put("Correo", Correo);
                parametros.put("Contraseña", Contraseña);
                parametros.put("Telefono", Telefono);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private boolean MetodoValidarCorreo(EditText email) {
        if (email.getText().toString().isEmpty()) {
            email.setError("¡Debes llenar este campo!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("¡Se necesita un correo valido!");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ActivityAutentificacion.class);
        startActivity(intent);
        finish();
    }
}