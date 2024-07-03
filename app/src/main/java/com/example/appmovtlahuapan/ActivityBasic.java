package com.example.appmovtlahuapan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Patterns;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ActivityBasic extends AppCompatActivity {

    private CountDownTimer CronometroInactividad;
    private RequestQueue requestQueue;
    
    private long TiempoUltimaInteraccion = 0;
    private static final long TiempoLimiteInactividad = 3 * 60 * 1000;
    private String UsuarioActual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        MetodoRecuperarUsuarioActual();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MetodoIniciarCronometroInactividad();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MetodoCancelarCronometroInactividad();
        TiempoUltimaInteraccion = System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
        TiempoUltimaInteraccion = System.currentTimeMillis();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        long tiempoInactividad = System.currentTimeMillis() - TiempoUltimaInteraccion;
        if (tiempoInactividad > TiempoLimiteInactividad) {
            MetodoCerrarSesionPorInactividad();
        } else {
            MetodoIniciarCronometroInactividad();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        MetodoReiniciarCronometroInactividad();
        return super.dispatchTouchEvent(event);
    }

    private void MetodoIniciarCronometroInactividad() {
        MetodoCancelarCronometroInactividad();

        CronometroInactividad = new CountDownTimer(TiempoLimiteInactividad, TiempoLimiteInactividad) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                MetodoCerrarSesionPorInactividad();
            }
        }.start();
    }

    private void MetodoCancelarCronometroInactividad() {
        if (CronometroInactividad != null) {
            CronometroInactividad.cancel();
        }
    }

    private void MetodoReiniciarCronometroInactividad() {
        MetodoCancelarCronometroInactividad();
        MetodoIniciarCronometroInactividad();
    }

    private void MetodoCerrarSesionPorInactividad() {
        MetodoCerrarSesion(ActivityAutentificacion.URLPruebas + "CerrarSesionPOST.php");
        MetodoGuardarUsuarioYContraseña();
        Intent intent = new Intent(getApplicationContext(), ActivityAutentificacion.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void MetodoCerrarSesion(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "" + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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

    public boolean MetodoValidarCorreo(EditText email) {
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

    public String MetodoCodificarTexto (String texto) {
        try {
            return URLEncoder.encode(texto, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void MetodoGuardarUsuarioYContraseña(){
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Usuario", "");
        editor.putString("Contrasena", "");
        editor.putString("IdNivel", "");
        editor.putBoolean("sesion", true);
        editor.apply();
    }

    private void MetodoRecuperarUsuarioActual(){
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        UsuarioActual = (preferences.getString("Usuario",""));
    }

    public void MetodoLimpiarCache() {
        try {
            File cacheDir = getCacheDir();
            if (cacheDir != null && cacheDir.isDirectory()) {
                MetodoEliminarDirectorioCache(cacheDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean MetodoEliminarDirectorioCache(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = MetodoEliminarDirectorioCache(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir != null && dir.delete();
    }
}