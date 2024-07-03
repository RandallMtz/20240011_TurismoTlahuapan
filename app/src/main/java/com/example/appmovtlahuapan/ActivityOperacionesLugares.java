package com.example.appmovtlahuapan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ActivityOperacionesLugares extends ActivityBasic {

    private AsyncHttpClient cliente;
    private Button btBuscarLugar, btEliminarLugar, btInsertarLugar, btLimpiarCampos, btModificarLugar;
    private EditText edCalle, etCorreo, edCostoAcceso, etFacebook, etInstagram, edLatitud, edLongitud, edNombre, edNumeroExterior, edNumeroInterior, etTelefono;
    private RequestQueue requestQueue;
    private Spinner spCodigosPostales, spColonias;
    private SearchableSpinner spCategorias, spLugares;
    private TextView lbIdLugarBusqueda;
    
    private String CategoriaBusqueda, CodigoPostal, Colonia, NombreBusqueda, URLLugares = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerLugares.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operaciones_lugares);

        cliente = new AsyncHttpClient();

        btBuscarLugar = findViewById(R.id.ButtonLugarBuscarOp); btEliminarLugar = findViewById(R.id.ButtonEliminarLugarOp);
        btInsertarLugar = findViewById(R.id.ButtonInsertarLugarOp); btLimpiarCampos = findViewById(R.id.ButtonLimpiarCamposLugarOp);
        btModificarLugar = findViewById(R.id.ButtonModificarLugarOp);

        edCalle = findViewById(R.id.EditTCalleLugarOp);
        etCorreo = findViewById(R.id.EditTCorreoLugarOp);
        edCostoAcceso = findViewById(R.id.EditTCostoEntradaLugarOp);
        etFacebook = findViewById(R.id.EditTFacebookLugarOp); etInstagram = findViewById(R.id.EditTInstagramLugarOp);
        edLatitud = findViewById(R.id.EditTLatitudLugarOp); edLongitud = findViewById(R.id.EditTLongitudLugarOp);
        edNombre = findViewById(R.id.EditTNombreLugarOp);
        edNumeroExterior = findViewById(R.id.EditTNoExtLugarOp); edNumeroInterior = findViewById(R.id.EditTNoIntLugarOp);
        etTelefono = findViewById(R.id.EditTTelefonoLugarOp);

        lbIdLugarBusqueda = findViewById(R.id.TextViewAuxiliarLugares);

        spCategorias = findViewById(R.id.SpCategoriaLugarOp);
        spCodigosPostales = findViewById(R.id.SpinnerCodigoPostalLugarOp);
        spColonias = findViewById(R.id.SpinnerColoniaLugarOp);
        spLugares = findViewById(R.id.SpinnerLugaresBuscar);
        
        spCategorias.setTitle("Selecciona una categoría");
        spCategorias.setPositiveButton("Cerrar");

        spLugares.setTitle("Selecciona un sitio turístico");
        spLugares.setPositiveButton("Cerrar");

        if (ActivityAutentificacion.NivelAdministracionUsuario.equals("Amanuense")){
            URLLugares = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerLugaresAmanuense.php";
        }

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        MetodoPoblarCategorias(); MetodoPoblarCodigosPostales(); MetodoPoblarLugares();

        btBuscarLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoBuscarLugar(ActivityAutentificacion.URLPruebas + "OperacionesLugares/BuscarPorNombreGET.php?Nombre=" + spLugares.getSelectedItem() + "");
            }
        });

        btLimpiarCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoLimpiarCampos();
            }
        });

        spCodigosPostales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CodigoPostal = adapterView.getItemIdAtPosition(i+1)+"";
                MetodoPoblarColonias(Integer.parseInt(adapterView.getItemIdAtPosition(i+1)+""));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spColonias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Colonia = adapterView.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        btInsertarLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etTelefono.length() < 10){
                    etTelefono.setError("¡Ingresa un número valido!");
                } else {
                    MetodoValidarLugarInexistenteInsertar(ActivityAutentificacion.URLPruebas + "OperacionesLugares/ValidarLugarInexistente.php?Nombre=" + edNombre.getText() + "");
                }
            }
        });

        btModificarLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edNombre.getText().toString().isEmpty()){
                    edNombre.setError("¡Se necesita un nombre!");
                } else {
                    if (etTelefono.length() < 10) {
                        etTelefono.setError("¡Ingresa un número valido!");
                    } else {
                        if (!MetodoValidarCorreo(etCorreo) == false){
                            MetodoValidarLugarInexistenteModificar(ActivityAutentificacion.URLPruebas + "OperacionesLugares/ValidarLugarInexistenteModificar.php?Nombre=" + edNombre.getText().toString() + "&Nombre1=" + NombreBusqueda + "");
                        }
                    }
                }
            }
        });

        btEliminarLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edNombre.getText().toString().isEmpty()) {
                    MetodoEliminarLugar(ActivityAutentificacion.URLPruebas + "OperacionesLugares/EliminarPOST.php");
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MetodoPoblarLugares();
                        }
                    }, 100);
                } else {
                    edNombre.setError("¡Se necesita un nombre!");
                }
                spCategorias.setSelection(0);
            }
        });
    }

    private void MetodoPoblarLugares(){
        cliente.post(URLLugares, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    MetodoCargarLugares(new String((responseBody)));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });
    }

    private void MetodoCargarLugares(String respuesta){
        ArrayList<AdquirirLugares> lista = new ArrayList<AdquirirLugares>();
        try {
            JSONArray jsonArreglo = new JSONArray(respuesta);
            for (int i = 0; i < jsonArreglo.length(); i++){
                AdquirirLugares rz = new AdquirirLugares();
                rz.setNombre(jsonArreglo.getJSONObject(i).getString("Nombre"));
                lista.add(rz);
            }
            ArrayAdapter<AdquirirLugares> a = new ArrayAdapter <AdquirirLugares> (getApplicationContext(), android.R.layout.simple_dropdown_item_1line, lista);
            spLugares.setAdapter(a);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void MetodoPoblarCategorias(){
        String url = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerCategorias.php";
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    MetodoCargarCategorias(new String((responseBody)));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void MetodoCargarCategorias(String respuesta){
        ArrayList<AdquirirCategoriasTuristicas> lista = new ArrayList<AdquirirCategoriasTuristicas>();
        try {
            JSONArray jsonArreglo = new JSONArray(respuesta);
            for (int i = 0; i < jsonArreglo.length(); i++){
                AdquirirCategoriasTuristicas nv = new AdquirirCategoriasTuristicas();
                nv.setCategoria(jsonArreglo.getJSONObject(i).getString("Categoria"));
                lista.add(nv);
            }
            ArrayAdapter<AdquirirCategoriasTuristicas> a = new ArrayAdapter <AdquirirCategoriasTuristicas> (getApplicationContext(), android.R.layout.simple_dropdown_item_1line, lista);
            spCategorias.setAdapter(a);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void MetodoPoblarCodigosPostales(){
        String url = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerCodigosPostales.php";
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    MetodoCargarCodigosPostales(new String((responseBody)));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void MetodoCargarCodigosPostales(String respuesta){
        ArrayList<AdquirirCodigosPostales> listaC = new ArrayList<AdquirirCodigosPostales>();
        try {
            JSONArray jsonArreglo = new JSONArray(respuesta);
            for (int i = 0; i < jsonArreglo.length(); i++){
                AdquirirCodigosPostales nv = new AdquirirCodigosPostales();
                nv.setCodigoPostal(jsonArreglo.getJSONObject(i).getString("CodigoPostal"));
                listaC.add(nv);
            }
            ArrayAdapter<AdquirirCodigosPostales> a = new ArrayAdapter <AdquirirCodigosPostales> (getApplicationContext(), android.R.layout.simple_dropdown_item_1line, listaC);
            spCodigosPostales.setAdapter(a);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void MetodoPoblarColonias(int Indice){
        String url = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerColonias.php?IdCodigoPostal=" + Indice + "";
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    MetodoCargarColonias(new String((responseBody)));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void MetodoCargarColonias(String respuesta){
        ArrayList<AdquirirColonias> listaC = new ArrayList<AdquirirColonias>();
        try {
            JSONArray jsonArreglo = new JSONArray(respuesta);
            for (int i = 0; i < jsonArreglo.length(); i++){
                AdquirirColonias nv = new AdquirirColonias();
                nv.setColonia(jsonArreglo.getJSONObject(i).getString("Colonia"));
                listaC.add(nv);
            }
            ArrayAdapter<AdquirirColonias> a = new ArrayAdapter <AdquirirColonias> (getApplicationContext(), android.R.layout.simple_dropdown_item_1line, listaC);
            spColonias.setAdapter(a);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void MetodoBuscarLugar (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String nombre = new String(jsonObject.getString("Nombre").getBytes("ISO-8859-1"), "UTF-8");
                        String calle = new String(jsonObject.getString("Calle").getBytes("ISO-8859-1"), "UTF-8");
                        edNombre.setText(nombre);
                        edLatitud.setText(jsonObject.getString("Latitud"));
                        edLongitud.setText(jsonObject.getString("Longitud"));
                        edCostoAcceso.setText(jsonObject.getString("CostoEntrada"));
                        edCalle.setText(calle);
                        edNumeroInterior.setText(jsonObject.getString("NoInterior"));
                        edNumeroExterior.setText(jsonObject.getString("NoExterior"));
                        spCodigosPostales.setSelection(Integer.parseInt(jsonObject.getString("IdCodPost"))-1);
                        lbIdLugarBusqueda.setText(jsonObject.getString("IdLugar"));
                        etFacebook.setText(jsonObject.getString("Facebook"));
                        etInstagram.setText(jsonObject.getString("Instagram"));
                        etCorreo.setText(jsonObject.getString("Correo"));
                        etTelefono.setText(jsonObject.getString("Telefono"));
                        NombreBusqueda = jsonObject.getString("Nombre");
                        CategoriaBusqueda = jsonObject.getString("Categoria");
                        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(ActivityAutentificacion.URLPruebas + "OperacionesLugares/IdTemporales.php", new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                JSONObject jsonObject2 = null;
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        jsonObject2 = response.getJSONObject(i);
                                        if ((jsonObject2.getString("Categoria")).equals(CategoriaBusqueda)){
                                            spCategorias.setSelection(Integer.parseInt(jsonObject2.getString("id_temporal"))-1);
                                        }
                                    } catch (JSONException e) {
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
                        requestQueue.add(jsonArrayRequest2);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
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

    private void MetodoValidarLugarInexistenteInsertar (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edNombre.setError("¡Intenta con un nombre distinto!");
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!MetodoValidarCorreo(etCorreo) == false){
                    if (edNombre.getText().toString().isEmpty()) {
                        edNombre.setError("¡Hace falta un nombre!");
                    } else if (!edNombre.getText().toString().isEmpty() && !edLatitud.getText().toString().isEmpty() && !edLongitud.getText().toString().isEmpty() && !edCalle.getText().toString().isEmpty()
                            && !edCostoAcceso.getText().toString().isEmpty() && !edNumeroExterior.getText().toString().isEmpty()) {
                        if (ActivityAutentificacion.NivelAdministracionUsuario.equals("Amanuense")){
                            MetodoInsertarLugar(ActivityAutentificacion.URLPruebas + "OperacionesLugares/InsertarPOSTAmanuense.php");
                        } else {
                            MetodoInsertarLugar(ActivityAutentificacion.URLPruebas + "OperacionesLugares/InsertarPOST.php");
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "¡Faltan campos por llenar!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

     private void MetodoInsertarLugar(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarLugares();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String,String>();
                parametros.put("Nombre", edNombre.getText().toString());
                parametros.put("Latitud", edLatitud.getText().toString());
                parametros.put("Longitud", edLongitud.getText().toString());
                parametros.put("CostoEntrada", edCostoAcceso.getText().toString());
                parametros.put("Calle", edCalle.getText().toString());
                parametros.put("Correo", etCorreo.getText().toString());
                parametros.put("Telefono", etTelefono.getText().toString());
                parametros.put("NoInterior", edNumeroInterior.getText().toString());
                parametros.put("NoExterior", edNumeroExterior.getText().toString());
                parametros.put("Facebook", etFacebook.getText().toString());
                parametros.put("Instagram", etInstagram.getText().toString());
                parametros.put("IdCodPost", CodigoPostal);
                parametros.put("Colonia", Colonia);
                parametros.put("Categoria", spCategorias.getSelectedItem().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoValidarLugarInexistenteModificar (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edNombre.setError("¡Intenta con un nombre distinto!");
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!MetodoValidarCorreo(etCorreo) == false){
                    if (edNombre.getText().toString().isEmpty()) {
                        edNombre.setError("¡Hace falta un nombre!");
                    } else if (!edNombre.getText().toString().isEmpty() && !edLatitud.getText().toString().isEmpty() && !edLongitud.getText().toString().isEmpty() && !edCalle.getText().toString().isEmpty()
                            && !edCostoAcceso.getText().toString().isEmpty() && !edNumeroExterior.getText().toString().isEmpty()) {
                        MetodoActualizarLugar(ActivityAutentificacion.URLPruebas + "OperacionesLugares/EditarPOST.php");
                    } else {
                        Toast.makeText(getApplicationContext(), "¡Faltan campos por llenar!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoActualizarLugar(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarLugares();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String,String>();
                parametros.put("IdLugar", lbIdLugarBusqueda.getText().toString());
                parametros.put("Nombre", edNombre.getText().toString());
                parametros.put("IdCodPost", CodigoPostal);
                parametros.put("Colonia", Colonia);
                parametros.put("Latitud", edLatitud.getText().toString());
                parametros.put("Longitud", edLongitud.getText().toString());
                parametros.put("CostoEntrada", edCostoAcceso.getText().toString());
                parametros.put("Calle", edCalle.getText().toString());
                parametros.put("NoInterior", edNumeroInterior.getText().toString());
                parametros.put("NoExterior", edNumeroExterior.getText().toString());
                parametros.put("Facebook", etFacebook.getText().toString());
                parametros.put("Instagram", etInstagram.getText().toString());
                parametros.put("Telefono", etTelefono.getText().toString());
                parametros.put("Correo", etCorreo.getText().toString());
                parametros.put("Categoria", spCategorias.getSelectedItem().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoEliminarLugar(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String,String>();
                parametros.put("Nombre", edNombre.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }
    
    public void MetodoLimpiarCampos (){
        edNombre.setText("");
        edLatitud.setText("");
        edLongitud.setText("");
        edCostoAcceso.setText("");
        edCalle.setText("");
        edNumeroInterior.setText("");
        edNumeroExterior.setText("");
        lbIdLugarBusqueda.setText("");
        etFacebook.setText("");
        etInstagram.setText("");
        etCorreo.setText("");
        etTelefono.setText("");
        spColonias.setSelection(0);
        spCategorias.setSelection(0);
        spCodigosPostales.setSelection(0);
    }
}