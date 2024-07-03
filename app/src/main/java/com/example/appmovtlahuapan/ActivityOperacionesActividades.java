package com.example.appmovtlahuapan;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ActivityOperacionesActividades extends ActivityBasic {

    private AsyncHttpClient cliente;
    private Button btMetodoBuscarActividad, btEliminarActividad, btInsertarActividad, btLimpiarCampos, btModificarActividad;
    private EditText edCosto, edDescripcion, edNombre;
    private TextView lbIdActividadBusqueda;
    private RequestQueue requestQueue;
    private SearchableSpinner spClavesActividades, spLugares;
    
    private String NombreBusqueda, LugarCodificado, LugarBusqueda, URLActividades, URLIdsTemporales, URLLugares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operaciones_actividades);

        cliente = new AsyncHttpClient();

        btMetodoBuscarActividad = findViewById(R.id.ButtonActividadesBuscarOp); btEliminarActividad = findViewById(R.id.ButtonEliminarActividadesOp);
        btInsertarActividad = findViewById(R.id.ButtonInsertarActividadesOp); btLimpiarCampos = findViewById(R.id.ButtonLimpiarCamposActividadesOp);
        btModificarActividad = findViewById(R.id.ButtonModificarActividadesOp);

        edCosto = findViewById(R.id.EditTCostoActividadesOp);
        edDescripcion = findViewById(R.id.EditTDescripcionActividadesOp);
        edNombre = findViewById(R.id.EditTNombreActividadesOp);
        
        lbIdActividadBusqueda = findViewById(R.id.TextVActividadesBuscarOp);

        spClavesActividades = findViewById(R.id.SpinnerClavesActividadesBuscar);
        spLugares = findViewById(R.id.SpinnerIdLugarActividadesOp);
        
        spClavesActividades.setTitle("Selecciona un servicio");
        spClavesActividades.setPositiveButton("Cerrar");
        spLugares.setTitle("Selecciona un sitio turístico");
        spLugares.setPositiveButton("Cerrar");

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        if (ActivityAutentificacion.NivelAdministracionUsuario.equals("Administrador")){
            URLIdsTemporales = ActivityAutentificacion.URLPruebas + "OperacionesActividades/IdTemporales.php";
            URLActividades = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerClavesActividadesPruebas.php?NombreLugar=" + LugarCodificado + "";
            URLLugares = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerLugares.php" ;
        } else if (ActivityAutentificacion.NivelAdministracionUsuario.equals("Ayuntamiento")){
            URLIdsTemporales = ActivityAutentificacion.URLPruebas + "OperacionesActividades/IdTemporales.php";
            URLActividades = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerClavesActividadesPruebas.php?NombreLugar=" + LugarCodificado + "";
            URLLugares = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerLugares.php";
        }

        MetodoPoblarLugares();

        spLugares.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MetodoPoblarActividades();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        btMetodoBuscarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoBuscarActividad(ActivityAutentificacion.URLPruebas + "OperacionesActividades/BuscarGETNuevaInterfaz.php?Nombre=" + spClavesActividades.getSelectedItem() + "&Lugar=" + spLugares.getSelectedItem() + "");
            }
        });

        btLimpiarCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoLimpiarCampos();
            }
        });

        btInsertarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoValidarActividadInexistenteInsertar(ActivityAutentificacion.URLPruebas + "OperacionesActividades/ValidarActividadInexistenteNuevaInterfaz.php?Nombre=" + edNombre.getText().toString() + "&NombreLugar=" + spLugares.getSelectedItem() + "");
                MetodoPoblarActividades();
            }
        });

        btModificarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edNombre.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "¡Se necesita un Nombre!", Toast.LENGTH_SHORT).show();
                } else {
                    MetodoValidarActividadInexistenteModificar(ActivityAutentificacion.URLPruebas + "OperacionesActividades/ValidarActividadInexistenteModificarNuevaInterfaz.php?Nombre=" + edNombre.getText().toString() + "&Nombre1=" + NombreBusqueda + "&NombreLug=" + LugarBusqueda + "");
                    MetodoPoblarActividades();
                }
            }
        });

        btEliminarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edNombre.getText().toString().isEmpty()) {
                    MetodoEliminarActividad(ActivityAutentificacion.URLPruebas + "OperacionesActividades/EliminarPOST.php");
                } else {
                    Toast.makeText(getApplicationContext(), "¡Se necesita un Nombre!", Toast.LENGTH_SHORT).show();
                }
                spLugares.setSelection(0);
                MetodoPoblarActividades();
            }
        });
    }

    private void MetodoPoblarLugares() {
        String url = URLLugares;
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    MetodoCargarLugares(new String((responseBody)));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });
    }

    private void MetodoCargarLugares(String respuesta) {
        ArrayList<AdquirirLugares> listaC = new ArrayList<AdquirirLugares>();
        try {
            JSONArray jsonArreglo = new JSONArray(respuesta);
            for (int i = 0; i < jsonArreglo.length(); i++) {
                AdquirirLugares nv = new AdquirirLugares();
                nv.setNombre(jsonArreglo.getJSONObject(i).getString("Nombre"));
                listaC.add(nv);
            }
            ArrayAdapter<AdquirirLugares> a = new ArrayAdapter<AdquirirLugares>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, listaC);
            spLugares.setAdapter(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void MetodoPoblarActividades() {
        LugarCodificado = MetodoCodificarTexto(spLugares.getSelectedItem().toString());
        String url = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerClavesActividadesPruebas.php?NombreLugar=" + LugarCodificado + "";
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    MetodoCargarActividades(new String((responseBody)));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });
    }

    private void MetodoCargarActividades(String respuesta) {
        ArrayList<AdquirirSitioTuristicoActividades> listaC = new ArrayList<AdquirirSitioTuristicoActividades>();
        try {
            JSONArray jsonArreglo = new JSONArray(respuesta);
            for (int i = 0; i < jsonArreglo.length(); i++) {
                AdquirirSitioTuristicoActividades nv = new AdquirirSitioTuristicoActividades();
                nv.setNombre(jsonArreglo.getJSONObject(i).getString("Nombre"));
                listaC.add(nv);
            }
            ArrayAdapter<AdquirirSitioTuristicoActividades> a = new ArrayAdapter<AdquirirSitioTuristicoActividades>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, listaC);
            spClavesActividades.setAdapter(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void MetodoBuscarActividad(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        lbIdActividadBusqueda.setText(jsonObject.getString("IdActividad"));
                        edNombre.setText(jsonObject.getString("Nombre"));
                        edDescripcion.setText(jsonObject.getString("Descripcion"));
                        edCosto.setText(jsonObject.getString("Costo"));
                        NombreBusqueda = jsonObject.getString("Nombre");
                        LugarBusqueda = jsonObject.getString("NombreLug");
                        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(URLIdsTemporales, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                JSONObject jsonObject2 = null;
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        jsonObject2 = response.getJSONObject(i);
                                        if ((jsonObject2.getString("Nombre")).equals(LugarBusqueda)){
                                            spLugares.setSelection(Integer.parseInt(jsonObject2.getString("id_temporal"))-1);
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
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Actividad inexistente", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoValidarActividadInexistenteInsertar(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Toast.makeText(getApplicationContext(), "Actividad ya registrada en el sitio.", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MetodoInsertarActividad(ActivityAutentificacion.URLPruebas + "OperacionesActividades/InsertarPOSTNuevaInterfaz.php");
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoInsertarActividad(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarActividades();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("Nombre", edNombre.getText().toString());
                parametros.put("Descripcion", edDescripcion.getText().toString());
                parametros.put("Costo", edCosto.getText().toString());
                parametros.put("NombreLugar", spLugares.getSelectedItem().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoValidarActividadInexistenteModificar(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Toast.makeText(getApplicationContext(), "Actividad ya registrada en el sitio.", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MetodoActualizarActividad(ActivityAutentificacion.URLPruebas + "OperacionesActividades/EditarPOSTNuevaInterfaz.php");
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoActualizarActividad(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarActividades();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("IdActividad", lbIdActividadBusqueda.getText().toString());
                parametros.put("Nombre", edNombre.getText().toString());
                parametros.put("Descripcion", edDescripcion.getText().toString());
                parametros.put("Costo", edCosto.getText().toString());
                parametros.put("NombreLugar", spLugares.getSelectedItem().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoEliminarActividad(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarActividades();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("IdActividad", lbIdActividadBusqueda.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoLimpiarCampos (){
        edCosto.setText("");
        edDescripcion.setText("");
        edNombre.setText("");
        lbIdActividadBusqueda.setText("");
    }
}