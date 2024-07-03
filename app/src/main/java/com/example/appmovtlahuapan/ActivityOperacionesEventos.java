package com.example.appmovtlahuapan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ActivityOperacionesEventos extends ActivityBasic {

    Bitmap bitmap;
    private AsyncHttpClient cliente;
    private Button btBuscarEvento, btEliminarEvento, btInsertarEvento, btLimpiarCampos, btModificarEvento, btSeleccionarImagen;
    private EditText edDescripcion, edFechaFinal, edFechaRealizacion, edHoraFinal, edHoraRealizacion, edNombre;
    private ImageView imFotografiaPromocional;
    private RequestQueue requestQueue;
    private SearchableSpinner spClavesEventos, spLugares;
    private TextView lbIdEventoBusqueda;
    
    private int PICK_IMAGE_REQUEST = 1, selectedHour, selectedMinute;
    private String LugarBusqueda, LugarCodificado, NombreBusqueda, NombreFotografiaBusqueda, SeleccionImagen = "hola", URLLugares = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerLugares.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operaciones_eventos);
        
        cliente = new AsyncHttpClient();

        btBuscarEvento = findViewById(R.id.ButtonEventosBuscarOp); btEliminarEvento = findViewById(R.id.ButtonEliminarEventosOp);
        btInsertarEvento = findViewById(R.id.ButtonInsertarEventosOp); btLimpiarCampos = findViewById(R.id.ButtonLimpiarCamposEventosOp);
        btModificarEvento = findViewById(R.id.ButtonModificarEventosOp); btSeleccionarImagen = findViewById(R.id.BtSeleccionarImagenOperacionesEventos);
        
        edDescripcion = findViewById(R.id.EditTDescripcionEventosOp);
        edFechaRealizacion = findViewById(R.id.EditTFechaEventosOp); edFechaFinal = findViewById(R.id.EditTFechaFinalEventosOp);
        edHoraRealizacion = findViewById(R.id.EditTHoraEventosOp); edHoraFinal = findViewById(R.id.EditTHoraFinalEventosOp);
        edNombre = findViewById(R.id.EditTNombreEventosOp);

        imFotografiaPromocional = findViewById(R.id.ImgVFotografiaOperacionesEventos);

        lbIdEventoBusqueda = findViewById(R.id.TextVEventosBuscarOp);

        spClavesEventos = findViewById(R.id.SpinnerClavesEventosBuscar);
        spLugares = findViewById(R.id.spBamosYa);

        spLugares.setTitle("Selecciona un sitio turístico");
        spLugares.setPositiveButton("Cerrar");
        
        spClavesEventos.setTitle("Selecciona un evento");
        spClavesEventos.setPositiveButton("Cerrar");

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        MetodoPoblarLugares();

        spLugares.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MetodoPoblarEventos();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        btBuscarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoBuscarEvento(ActivityAutentificacion.URLPruebas + "OperacionesEventos/BuscarGETNuevaInterfaz.php?Nombre=" + spClavesEventos.getSelectedItem() + "&Lugar=" + spLugares.getSelectedItem() + "");
            }
        });

        btLimpiarCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoLimpiarCampos();
            }
        });

        btSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        edFechaRealizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                Locale locale = new Locale("es", "ES");
                Locale.setDefault(locale);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ActivityOperacionesEventos.this, R.style.Theme_AppMovTlahuapan,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                                edFechaRealizacion.setText(selectedDate);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        edFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                Locale locale = new Locale("es", "ES");
                Locale.setDefault(locale);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ActivityOperacionesEventos.this, R.style.Theme_AppMovTlahuapan,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                                edFechaFinal.setText(selectedDate);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        edHoraRealizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                selectedHour = c.get(Calendar.HOUR_OF_DAY);
                selectedMinute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityOperacionesEventos.this, R.style.Theme_AppMovTlahuapan,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                edHoraRealizacion.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, selectedHour, selectedMinute, false);
                timePickerDialog.show();
            }
        });

        edHoraFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                selectedHour = c.get(Calendar.HOUR_OF_DAY);
                selectedMinute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityOperacionesEventos.this, R.style.Theme_AppMovTlahuapan,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                edHoraFinal.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, selectedHour, selectedMinute, false);
                timePickerDialog.show();
            }
        });

        btInsertarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edFechaRealizacion.getText().toString().isEmpty() || edFechaFinal.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "¡Aun faltan campos por llenar!", Toast.LENGTH_SHORT).show();
                } else {
                    MetodoValidarEventoInexistenteInsertar(ActivityAutentificacion.URLPruebas + "OperacionesEventos/ValidarEventoInexistenteNuevaInterfaz.php?Nombre=" + edNombre.getText().toString() + "&NombreLugar=" + spLugares.getSelectedItem() + "");
                }
            }
        });

        btModificarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edNombre.getText().toString().isEmpty()){
                    edNombre.setError("¡Se necesita una clave!");
                } else {
                    MetodoValidarEventoInexistenteModificar(ActivityAutentificacion.URLPruebas + "OperacionesEventos/ValidarEventoInexistenteModificarNuevaInterfaz.php?Nombre=" + edNombre.getText().toString() + "&Nombre1=" + NombreBusqueda + "&NombreLugar=" + LugarBusqueda +"");
                }
            }
        });

        btEliminarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edNombre.getText().toString().isEmpty()) {
                    MetodoEliminarEvento(ActivityAutentificacion.URLPruebas + "OperacionesEventos/EliminarPOST.php");
                } else {
                    edNombre.setError("¡Se necesita una clave!");
                }
                MetodoPoblarEventos();
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

    private void MetodoPoblarEventos() {
        ActivityOperacionesActividades ActActividades = new ActivityOperacionesActividades();
        LugarCodificado = ActActividades.MetodoCodificarTexto(spLugares.getSelectedItem().toString());
        String url = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerEventosPruebas.php?NombreLugar=" + LugarCodificado + "";
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    MetodoCargarEventos(new String((responseBody)));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });
    }

    private void MetodoCargarEventos(String respuesta) {
        ArrayList<AdquirirEventosProximos> listaC = new ArrayList<AdquirirEventosProximos>();
        try {
            JSONArray jsonArreglo = new JSONArray(respuesta);
            for (int i = 0; i < jsonArreglo.length(); i++) {
                AdquirirEventosProximos nv = new AdquirirEventosProximos();
                nv.setNombre(jsonArreglo.getJSONObject(i).getString("Nombre"));
                listaC.add(nv);
            }
            ArrayAdapter<AdquirirEventosProximos> a = new ArrayAdapter<AdquirirEventosProximos>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, listaC);
            spClavesEventos.setAdapter(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void MetodoBuscarEvento(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        SeleccionImagen = "";
                        NombreBusqueda = jsonObject.getString("Nombre");
                        lbIdEventoBusqueda.setText(jsonObject.getString("IdEvento"));
                        edNombre.setText(jsonObject.getString("Nombre"));
                        edDescripcion.setText(jsonObject.getString("Descripcion"));
                        edFechaRealizacion.setText(jsonObject.getString("Fecha"));
                        edFechaFinal.setText(jsonObject.getString("FechaFinal"));
                        edHoraRealizacion.setText(jsonObject.getString("Hora"));
                        edHoraFinal.setText(jsonObject.getString("HoraFinal"));
                        NombreFotografiaBusqueda = jsonObject.getString("FotoNombre");
                        Glide.with(getApplicationContext())
                                .load(jsonObject.getString("Foto") + "?timestamp=" + System.currentTimeMillis())
                                .into(imFotografiaPromocional);
                        LugarBusqueda = jsonObject.getString("NombreLug");
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Evento inexistente", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoValidarEventoInexistenteInsertar(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Toast.makeText(getApplicationContext(), "Evento ya registrado en el sitio.", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MetodoInsertarEvento(ActivityAutentificacion.URLPruebas + "OperacionesEventos/InsertarPOSTNuevaInterfaz.php");
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoInsertarEvento(String url) {
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarEventos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("Nombre", edNombre.getText().toString());
                parametros.put("Descripcion", edDescripcion.getText().toString());
                parametros.put("Fecha", edFechaRealizacion.getText().toString());
                parametros.put("FechaFinal", edFechaFinal.getText().toString());
                parametros.put("Hora", edHoraRealizacion.getText().toString());
                parametros.put("HoraFinal", edHoraFinal.getText().toString());
                parametros.put("NombreLugar", spLugares.getSelectedItem().toString());
                parametros.put("Fotografia", getStringImagen(bitmap));
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoValidarEventoInexistenteModificar(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Toast.makeText(getApplicationContext(), "Evento ya registrado en el sitio.", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (SeleccionImagen.equals("")) {
                    MetodoActualizarEventoSinImagen(ActivityAutentificacion.URLPruebas + "OperacionesEventos/EditarPOSTNuevaInterfaz.php");
                } else {
                    MetodoActualizarEventoConImagen(ActivityAutentificacion.URLPruebas + "OperacionesEventos/EditarPOSTNuevaInterfazImagen.php");
                }
                SeleccionImagen = "";
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoActualizarEventoSinImagen(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCache(); MetodoLimpiarCampos(); MetodoPoblarEventos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("IdEvento", lbIdEventoBusqueda.getText().toString());
                parametros.put("Nombre", edNombre.getText().toString());
                parametros.put("Descripcion", edDescripcion.getText().toString());
                parametros.put("Fecha", edFechaRealizacion.getText().toString());
                parametros.put("FechaFinal", edFechaFinal.getText().toString());
                parametros.put("Hora", edHoraRealizacion.getText().toString());
                parametros.put("HoraFinal", edHoraFinal.getText().toString());
                parametros.put("NombreLugar", spLugares.getSelectedItem().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoActualizarEventoConImagen(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCache(); MetodoLimpiarCampos(); MetodoPoblarEventos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("IdEvento", lbIdEventoBusqueda.getText().toString());
                parametros.put("Nombre", edNombre.getText().toString());
                parametros.put("Descripcion", edDescripcion.getText().toString());
                parametros.put("Fecha", edFechaRealizacion.getText().toString());
                parametros.put("FechaFinal", edFechaFinal.getText().toString());
                parametros.put("Hora", edHoraRealizacion.getText().toString());
                parametros.put("HoraFinal", edHoraFinal.getText().toString());
                parametros.put("NombreLugar", spLugares.getSelectedItem().toString());
                parametros.put("Fotografia", getStringImagen(bitmap));
                parametros.put("FotoNombre", NombreFotografiaBusqueda);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoEliminarEvento(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarEventos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("IdEvento", lbIdEventoBusqueda.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen"), PICK_IMAGE_REQUEST);
        SeleccionImagen = "hola";
    }

    public String getStringImagen(Bitmap bmp) {
        if (bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            SeleccionImagen = "hola";
            return encodedImage;
        } else {
            SeleccionImagen = "";
            return "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imFotografiaPromocional.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void MetodoLimpiarCampos(){
        edDescripcion.setText("");
        edFechaRealizacion.setText("");
        edFechaFinal.setText("");
        edHoraRealizacion.setText("");
        edHoraFinal.setText("");
        edNombre.setText("");
        imFotografiaPromocional.setImageBitmap(null);
        lbIdEventoBusqueda.setText("");
        NombreFotografiaBusqueda = "";
    }
}