package com.example.appmovtlahuapan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityDetallesLugar extends ActivityBasic {

    private Button btGuardarComentario;
    private EditText edComentario;
    private List<AdquirirSitioTuristicoActividades> ActividadesLista;
    private List<AdquirirSitioTuristicoImagenes> ImagenesLista;
    private RecyclerView rcActividades, rcImagenes;
    private RequestQueue requestQueue;    
    private TextView lbCalle, lbColonia, lbCorreo, lbCostoAcceso, lbFacebook, lbInstagram, lbLatitud, lbLongitud, lbNombre, lbNumeroExterior, lbNumeroInterior, lbTelefono, lbLunes, lbMartes, lbMiercoles, lbJueves, lbViernes, lbSabado, lbDomingo;

    private String IdUsuarioRecuperado = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_lugar);

        MetodoRecuperarIdUsuario();

        Intent intent = getIntent();
        String NombreActivityAnterior = intent.getExtras().getString("Nombre");

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        ActividadesLista = new ArrayList<>();

        rcActividades = (RecyclerView) findViewById(R.id.RecyclerActividades);
        rcActividades.setHasFixedSize(true);
        rcActividades.setLayoutManager(new LinearLayoutManager(this));

        ImagenesLista = new ArrayList<>();

        rcImagenes = (RecyclerView) findViewById(R.id.RecyclerImagenes);
        rcImagenes.setHasFixedSize(true);
        rcImagenes.setLayoutManager(new LinearLayoutManager(this));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ActivityAutentificacion.URLPruebas + "Componentes/RecyclerActividades.php?Nombre="+NombreActivityAnterior+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject lugar = array.getJSONObject(i);
                        ActividadesLista.add(new AdquirirSitioTuristicoActividades(
                                lugar.getString("Nombre"),
                                lugar.getString("Descripcion"),
                                lugar.getString("Costo")
                        ));
                    }
                    AdaptadorSitioTuristicoActividades adapterActividades = new AdaptadorSitioTuristicoActividades(ActivityDetallesLugar.this, ActividadesLista);
                    rcActividades.setAdapter(adapterActividades);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        requestQueue.add(stringRequest);

        StringRequest stringRespuesta = new StringRequest(Request.Method.GET, ActivityAutentificacion.URLPruebas + "Componentes/RecyclerImagenes.php?Nombre="+NombreActivityAnterior+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject lugar = array.getJSONObject(i);
                        ImagenesLista.add(new AdquirirSitioTuristicoImagenes(
                                lugar.getString("IdImagen"),
                                lugar.getString("Foto"),
                                lugar.getString("Nombre"),
                                lugar.getString("IdLugar"),
                                lugar.getString("Descripcion")
                        ));
                    }
                    AdaptadorSitioTuristicoImagenes adapterImagenes = new AdaptadorSitioTuristicoImagenes(ActivityDetallesLugar.this, ImagenesLista);
                    rcImagenes.setAdapter(adapterImagenes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });

        requestQueue.add(stringRespuesta);

        btGuardarComentario = findViewById(R.id.ButtonDejarComentario);
        
        edComentario = findViewById(R.id.editTextComentario);

        lbCalle = findViewById(R.id.TextVCalleDetallesLugar); lbColonia = findViewById(R.id.TextVColoniaDetallesLugar);
        lbCorreo = findViewById(R.id.TextVCorreoDetallesLugar);
        lbCostoAcceso = findViewById(R.id.TextVCostoEntradaDetallesLugar);
        lbFacebook = findViewById(R.id.TextVFacebookDetallesLugar); lbInstagram = findViewById(R.id.TextVInstagaramDetallesLugar);
        lbLatitud = findViewById(R.id.TextVLatitudDetallesLugar); lbLongitud = findViewById(R.id.TextVLongitudDetallesLugar);
        lbNombre = findViewById(R.id.TextVNombreDetallesLugar);
        lbNumeroExterior = findViewById(R.id.TextVNoExtDetallesLugar); lbNumeroInterior = findViewById(R.id.TextVNoIntDetallesLugar);
        lbTelefono = findViewById(R.id.TextVTelefonoDetallesLugar);

        lbLunes = findViewById(R.id.TextVLunesDetallesLugar); lbMartes = findViewById(R.id.TextVMartesDetallesLugar);
        lbMiercoles = findViewById(R.id.TextVMiercolesDetallesLugar); lbJueves = findViewById(R.id.TextVJuevesDetallesLugar);
        lbViernes = findViewById(R.id.TextVViernesDetallesLugar); lbSabado = findViewById(R.id.TextVSabadoDetallesLugar);
        lbDomingo = findViewById(R.id.TextVDomingoDetallesLugar);

        MetodoBuscarDetallesLugar(ActivityAutentificacion.URLPruebas + "OperacionesLugares/BuscarPorNombreGET.php?Nombre=" + NombreActivityAnterior + "");
        MetodoBuscarHorarioLunes(ActivityAutentificacion.URLPruebas + "ConsultasPorDias/Lunes.php?Nombre=" + NombreActivityAnterior + "");
        MetodoBuscarHorarioMartes(ActivityAutentificacion.URLPruebas + "ConsultasPorDias/Martes.php?Nombre=" + NombreActivityAnterior + "");
        MetodoBuscarHorarioMiercoles(ActivityAutentificacion.URLPruebas + "ConsultasPorDias/Miercoles.php?Nombre=" + NombreActivityAnterior + "");
        MetodoBuscarHorarioJueves(ActivityAutentificacion.URLPruebas + "ConsultasPorDias/Jueves.php?Nombre=" + NombreActivityAnterior + "");
        MetodoBuscarHorarioViernes(ActivityAutentificacion.URLPruebas + "ConsultasPorDias/Viernes.php?Nombre=" + NombreActivityAnterior + "");
        MetodoBuscarHorarioSabado(ActivityAutentificacion.URLPruebas + "ConsultasPorDias/Sabado.php?Nombre=" + NombreActivityAnterior + "");
        MetodoBuscarHorarioDomingo(ActivityAutentificacion.URLPruebas + "ConsultasPorDias/Domingo.php?Nombre=" + NombreActivityAnterior + "");

        lbTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+lbTelefono.getText().toString()));
                startActivity(intent);
            }
        });

        btGuardarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoDejarComentario(ActivityAutentificacion.URLPruebas + "InsertarComentariosPOST.php");
            }
        });
    }

    private void MetodoBuscarDetallesLugar (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String nombre = new String(jsonObject.getString("Nombre").getBytes("ISO-8859-1"), "UTF-8");
                        String calle = new String(jsonObject.getString("Calle").getBytes("ISO-8859-1"), "UTF-8");
                        String colonia = new String(jsonObject.getString("Colonia").getBytes("ISO-8859-1"), "UTF-8");
                        lbNombre.setText(nombre);
                        lbLatitud.setText(jsonObject.getString("Latitud"));
                        lbLongitud.setText(jsonObject.getString("Longitud"));
                        lbCostoAcceso.setText(jsonObject.getString("CostoEntrada"));
                        lbCalle.setText(calle);
                        lbNumeroInterior.setText(jsonObject.getString("NoInterior"));
                        lbNumeroExterior.setText(jsonObject.getString("NoExterior"));
                        lbFacebook.setText(jsonObject.getString("Facebook"));
                        lbInstagram.setText(jsonObject.getString("Instagram"));
                        lbColonia.setText(colonia);
                        lbCorreo.setText(jsonObject.getString("Correo"));
                        lbTelefono.setText(jsonObject.getString("Telefono"));
                    } catch (JSONException | UnsupportedEncodingException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Lugar inexistente", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoBuscarHorarioLunes (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        lbLunes.setText(jsonObject.getString("HoraInicio") + " - " + jsonObject.getString("HoraFin"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoBuscarHorarioMartes (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        lbMartes.setText(jsonObject.getString("HoraInicio") + " - " + jsonObject.getString("HoraFin"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoBuscarHorarioMiercoles (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        lbMiercoles.setText(jsonObject.getString("HoraInicio") + " - " + jsonObject.getString("HoraFin"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoBuscarHorarioJueves (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        lbJueves.setText(jsonObject.getString("HoraInicio") + " - " + jsonObject.getString("HoraFin"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoBuscarHorarioViernes (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        lbViernes.setText(jsonObject.getString("HoraInicio") + " - " + jsonObject.getString("HoraFin"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoBuscarHorarioSabado (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        lbSabado.setText(jsonObject.getString("HoraInicio") + " - " + jsonObject.getString("HoraFin"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoBuscarHorarioDomingo (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        lbDomingo.setText(jsonObject.getString("HoraInicio") + " - " + jsonObject.getString("HoraFin"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoDejarComentario(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Gracias!", Toast.LENGTH_SHORT).show();
                edComentario.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String,String>();
                parametros.put("IdUsuario", IdUsuarioRecuperado);
                parametros.put("NombreLugar", lbNombre.getText().toString());
                parametros.put("Comentario", edComentario.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoRecuperarIdUsuario(){
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        IdUsuarioRecuperado = preferences.getString("IdUsuario","");
    }
}