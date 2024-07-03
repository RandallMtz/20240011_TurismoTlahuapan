package com.example.appmovtlahuapan;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ActivityRecuperarContrasena extends AppCompatActivity {

    Session session;
    private Button btEnviarCorreo;
    private EditText edCorreoElectronico;
    private RequestQueue requestQueue;
    private String  ApellidoPaternoBusqueda, ApellidoMaternoBusqueda, Contraseña, ContraseñaBusqueda, Correo, NombreBusqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        btEnviarCorreo = findViewById(R.id.BtEnviarCorreoRecuperarContraseña);

        edCorreoElectronico = findViewById(R.id.EditTCorreoRecuperarContraseña);

        Correo = "jose.randall.principal.360@gmail.com";
        Contraseña = "idjg yogd yvtc zssh";

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        btEnviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edCorreoElectronico.getText().toString().isEmpty()){
                    edCorreoElectronico.setError("¡Este campo no puede estar vacio!");
                } else {
                    MetodoVerificarExistenciaCorreo(ActivityAutentificacion.URLPruebas + "RecuperacionContraseña.php?Correo=" + edCorreoElectronico.getText().toString());
                }
            }
        });
    }

    private void MetodoVerificarExistenciaCorreo (String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        NombreBusqueda = jsonObject.getString("Nombres");
                        ApellidoPaternoBusqueda = jsonObject.getString("ApePaterno");
                        ApellidoMaternoBusqueda = jsonObject.getString("ApeMaterno");
                        ContraseñaBusqueda = jsonObject.getString("Contraseña");

                        if (!jsonObject.getString("Correo").equals("")) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            Properties properties = new Properties();
                            properties.put("mail.smtp.host", "smtp.googlemail.com");
                            properties.put("mail.smtp.socketFactory.port", "465");
                            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                            properties.put("mail.smtp.auth", "true");
                            properties.put("mail.smtp.port", "465");
                            try {
                                session = javax.mail.Session.getDefaultInstance(properties, new Authenticator() {
                                    @Override
                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        return new PasswordAuthentication(Correo, Contraseña);
                                    }
                                });

                                if (session != null) {
                                    Message message = new MimeMessage(session);
                                    message.setFrom(new InternetAddress(Correo));
                                    message.setSubject("Recuperación de contraseña");
                                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(edCorreoElectronico.getText().toString()));
                                    message.setContent("Desde el H. Ayuntamiento Tlahuapan 2022 - 2024\n\n" +
                                            "Estimado " + NombreBusqueda + " " + ApellidoPaternoBusqueda + " " + ApellidoMaternoBusqueda + " \n\n" +
                                            "Hemos recibido una solicitud para restablecer la contraseña de tu cuenta en nuestra aplicación de turismo. \n" +
                                            "Conforme a lo solicitado, te enviamos la nueva contraseña de tu cuenta, que podrás utilizar para acceder a la aplicación:\n\n" +
                                            "Nueva contraseña: " + ContraseñaBusqueda + " \n\n" +
                                            "Por favor, asegúrate de cambiar tu contraseña inmediatamente después de iniciar sesión por primera vez. \n" +
                                            "Si no solicitaste un restablecimiento de contraseña, ponte en contacto con nosotros inmediatamente en jose.randall.principal.360@gmail.com", "text/plain; charset=utf-8");
                                    Transport.send(message);
                                    Toast.makeText(getApplicationContext(), "Correo enviado", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), ActivityAutentificacion.class);
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                edCorreoElectronico.setError("¡El correo no esta vinculado a ninguna cuenta!");
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), ActivityAutentificacion.class);
        startActivity(intent);
        finish();
    }
}