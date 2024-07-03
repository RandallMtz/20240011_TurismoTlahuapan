package com.example.appmovtlahuapan;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ActivityAutentificacion extends AppCompatActivity {

    private Button btAutentificarse;
    private CheckBox chMantenerSesion;
    private EditText edContraseña, edUsuario;
    private FloatingActionButton btFloatLozalizaciones;
    private RequestQueue requestQueue;
    private String ContraseñaAyuda, IdNivel, UsuarioAyuda;
    private TextView lbNivelAdministracion, lbRecuperarContraseña, lbRegistrarse;
    
    private boolean ContraseñaVisible;
    public static String CorreoUsuario = "", IdUsuario = "0", NivelAdministracionUsuario = "", NombreUsuario = "", TelefonoUsuario = "", URLPruebas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentificacion);

        ActivityBasic ActBasic = new ActivityBasic();

        btAutentificarse = (Button) findViewById(R.id.ButtonIniciarSesionAutentificacion);
        btFloatLozalizaciones = findViewById(R.id.fabBotonFlotante2);

        chMantenerSesion = findViewById(R.id.checkBoxGuardarContrasena);

        edContraseña = findViewById(R.id.EditTPasswordAutentificacion); edUsuario = findViewById(R.id.EditTUsernameAutentificacion);

        lbNivelAdministracion = findViewById(R.id.TextVNivelAdministrativoAutentificacion);
        lbRecuperarContraseña = findViewById(R.id.TextVRecuperarContraseñaAutentificacion); lbRegistrarse = findViewById(R.id.TextVSignUpAutentificacion);

        URLPruebas = getApplicationContext().getString(R.string.URL);

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        MetodoRecuperarUsuarioYContraseña(); ActBasic.MetodoLimpiarCache();

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

        btAutentificarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioAyuda = edUsuario.getText().toString();
                ContraseñaAyuda = edContraseña.getText().toString();
                if (!edUsuario.getText().toString().isEmpty() && !edContraseña.getText().toString().isEmpty()){
                    if (chMantenerSesion.isChecked()){
                        MetodoAutentificacion(  URLPruebas + "AutentificacionPOSTSesionesActivas.php");
                    } else {
                        MetodoAutentificacion(  URLPruebas + "AutentificacionPOST.php");
                    }
                    } else {
                        Toast.makeText(getApplicationContext(),"¡Introduce tu usuario y contraseña!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        lbRecuperarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityRecuperarContrasena.class);
                startActivity(intent);
                finish();
            }
        });

        lbRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoMostrarTerminosYCondiciones();
            }
        });

        btFloatLozalizaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://studio.onirix.com/exp/Lk1WVe";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    private void MetodoMostrarTerminosYCondiciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_terminos, null);

        TextView textViewTerminosCondiciones = dialogView.findViewById(R.id.textViewTerminosCondiciones);
        String textoTerminosCondiciones = "El uso de la información contenida en esta aplicación móvil implica la aceptación de las condiciones de uso siguientes: <br><br>" +
                "1. Los datos que integran la aplicación móvil de turismo de Tlahuapan provienen de múltiples fuentes, proporcionada por la Dirección de Turismo del Municipio de Santa Rita Tlahuapan (2022 -2024), por lo que la aplicación ofrece enlaces a otras páginas que no fueron creados ni son operados por la Dirección de Turismo ni el Tecnológico Nacional de México Campus San Martín Texmelucan (TecNM campus San Martín Texmelucan) y, que, por lo tanto, están fuera de su control.<br><br> En este sentido, el TecNM campus San Martín Texmelucan y la Dirección de Turismo no representan ni garantizan el contenido de ningún sitio de terceros que tengan enlaces con esta aplicación, ni asumen responsabilidad legal o de cualquier otra índole por problemas en su acceso, en su contenido u otros enlaces que contenga, ni por el uso que se dé a esos sitios.<br><br>" +
                "2. Los datos recolectados de los usuarios de la aplicación móvil solo son para fines estadísticos y de mejora de servicio, por lo que los campos no son validados y tampoco verificados, por lo que son responsabilidad del usuario que captura dichos datos. <br><br>" +
                "3. El uso y manipulación que se le dé a la información consultada en este sitio será responsabilidad de quien la utilice.<br><br>" +
                "4. El Tecnológico Nacional de México Campus San Martín Texmelucan no se responsabiliza en caso de que el usuario no cuente con la versión de sistema operativo sugerido, ni utiliza la herramienta conforme se sugiere en el manual de usuario de la aplicación, para lograr su óptimo funcionamiento.";
        textViewTerminosCondiciones.setText(Html.fromHtml(textoTerminosCondiciones));

        builder.setView(dialogView)
                .setTitle("Condiciones de uso")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getApplicationContext(), ActivityRegUsuarioNuevo.class);
                            startActivity(intent);
                            finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void MetodoAutentificacion(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d(TAG, "Response: " + response.toString());
                    boolean guardarContrasena = chMantenerSesion.isChecked();
                    lbNivelAdministracion.setText(jsonObject.getString("IdNivelAdministracion"));
                    IdNivel = lbNivelAdministracion.getText().toString();
                    NombreUsuario = jsonObject.getString("Nombres") + " " + jsonObject.getString("ApePaterno") + " " + jsonObject.getString("ApeMaterno");
                    TelefonoUsuario = jsonObject.getString("Telefono");
                    CorreoUsuario = jsonObject.getString("Correo");
                    if (lbNivelAdministracion.getText().equals("4")) {
                        IdUsuario = jsonObject.getString("IdUsuario");
                        if (guardarContrasena){
                            MetodoGuardarUsuarioYContraseña(edUsuario.getText().toString(),edContraseña.getText().toString(), guardarContrasena);
                        } else {
                            MetodoGuardarUsuarioYContraseña(edUsuario.getText().toString(),"", false);
                        }
                        Intent intent = new Intent(getApplicationContext(), ActivityBottomMenuAdministrativo.class);
                        NivelAdministracionUsuario = "Administrador";
                        startActivity(intent);
                        finish();
                    }
                    if (lbNivelAdministracion.getText().equals("3")) {
                        IdUsuario = jsonObject.getString("IdUsuario");
                        if (guardarContrasena){
                            MetodoGuardarUsuarioYContraseña(edUsuario.getText().toString(),edContraseña.getText().toString(), guardarContrasena);
                        } else {
                            MetodoGuardarUsuarioYContraseña(edUsuario.getText().toString(),"", false);
                        }
                        Intent intent = new Intent(getApplicationContext(), ActivityBottomMenuAdministrativo.class);
                        NivelAdministracionUsuario = "Ayuntamiento";
                        startActivity(intent);
                        finish();
                    }
                    if (lbNivelAdministracion.getText().equals("2")) {
                        IdUsuario = jsonObject.getString("IdUsuario");
                        if (guardarContrasena){
                            MetodoGuardarUsuarioYContraseña(edUsuario.getText().toString(),edContraseña.getText().toString(), guardarContrasena);
                        } else {
                            MetodoGuardarUsuarioYContraseña(edUsuario.getText().toString(),"", false);
                        }
                        Intent intent = new Intent(getApplicationContext(), ActivityBottomMenuAmanuense.class);
                        NivelAdministracionUsuario = "Amanuense";
                        startActivity(intent);
                        finish();
                    }
                    if (lbNivelAdministracion.getText().equals("1")) {
                        IdUsuario = jsonObject.getString("IdUsuario");
                        if (guardarContrasena){
                            MetodoGuardarUsuarioYContraseña(edUsuario.getText().toString(),edContraseña.getText().toString(), guardarContrasena);
                        } else {
                            MetodoGuardarUsuarioYContraseña(edUsuario.getText().toString(),"", false);
                        }
                        Intent intent = new Intent(getApplicationContext(), ActivityBottomMenuCliente.class);
                        NivelAdministracionUsuario = "Cliente";
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "" + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "¡Revisa tu conexión!", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String,String>();
                parametros.put("Usuario", edUsuario.getText().toString());
                parametros.put("Contrasena", edContraseña.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void MetodoGuardarUsuarioYContraseña(String Usuario1, String Contraseña1, boolean GuardarContra){
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Usuario", Usuario1);
        editor.putString("Contrasena", Contraseña1);
        editor.putBoolean("GuardarContra", GuardarContra);
        editor.putString("IdUsuario", IdUsuario);
        editor.putString("IdNivel", IdNivel);
        editor.putBoolean("sesion", true);
        editor.commit();

        ActivityBasic ActBasic = new ActivityBasic();
        ActBasic.MetodoLimpiarCache();
    }

    private void MetodoRecuperarUsuarioYContraseña(){
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        edUsuario.setText(preferences.getString("Usuario",""));
        edContraseña.setText(preferences.getString("Contrasena",""));
        chMantenerSesion.setChecked(preferences.getBoolean("GuardarContra",true));
        if (preferences.getBoolean("GuardarContra", true)){
            if (preferences.getString("IdNivel", "4").equals("4")){
                Intent intent = new Intent(getApplicationContext(), ActivityBottomMenuAdministrativo.class);
                NivelAdministracionUsuario = "Administrador";
                startActivity(intent);
                finish();
            }
            if (preferences.getString("IdNivel", "3").equals("3")){
                Intent intent = new Intent(getApplicationContext(), ActivityBottomMenuAdministrativo.class);
                NivelAdministracionUsuario = "Ayuntamiento";
                startActivity(intent);
                finish();
            }
            if (preferences.getString("IdNivel", "4").equals("2")){
                Intent intent = new Intent(getApplicationContext(), ActivityBottomMenuAmanuense.class);
                NivelAdministracionUsuario = "Amanuense";
                startActivity(intent);
                finish();
            }
            if (preferences.getString("IdNivel", "4").equals("1")){
                Intent intent = new Intent(getApplicationContext(), ActivityBottomMenuCliente.class);
                NivelAdministracionUsuario = "Cliente";
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}