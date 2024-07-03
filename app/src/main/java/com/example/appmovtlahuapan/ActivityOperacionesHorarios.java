package com.example.appmovtlahuapan;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ActivityOperacionesHorarios extends ActivityBasic {

    private AsyncHttpClient cliente;
    private Button btBuscarHorario, btEliminarHorario, btInsertarHorario,  btLimpiarCampos, btModificarHorario;
    private EditText edHoraFin, edHoraInicio;
    private RequestQueue requestQueue;
    private SearchableSpinner spLugares;
    private Spinner spDias;
    private TextView lbIdHorarioBusqueda;
    
    private int selectedHour, selectedMinute;
    private String Dia, URLLugares = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerLugares.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operaciones_horarios);

        cliente = new AsyncHttpClient();
        
        btBuscarHorario = findViewById(R.id.ButtonHorarioBuscarOp); btEliminarHorario = findViewById(R.id.ButtonEliminarHorarioOp);
        btInsertarHorario = findViewById(R.id.ButtonInsertarHorarioOp); btLimpiarCampos = findViewById(R.id.ButtonLimpiarCamposHorarioOp);
        btModificarHorario = findViewById(R.id.ButtonModificarHorarioOp);

        edHoraFin = findViewById(R.id.EditTHoraFinHorariosOp); edHoraInicio = findViewById(R.id.EditTHoraInHorarioOp);
        
        lbIdHorarioBusqueda = findViewById(R.id.TextVHorarioBuscarOp);

        spDias = findViewById(R.id.SpinnerDiaHorariosOp);
        spLugares = findViewById(R.id.SpinnerIdLugarHorariosOp);
        spLugares.setTitle("Selecciona una clave");
        spLugares.setPositiveButton("Cerrar");

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        if (ActivityAutentificacion.NivelAdministracionUsuario.equals("Amanuense")){
            URLLugares = ActivityAutentificacion.URLPruebas + "Componentes/SpinnerLugaresAmanuense.php";
        }

        ArrayAdapter<CharSequence> adapterDias = ArrayAdapter.createFromResource(this, R.array.spinnerDias, android.R.layout.simple_dropdown_item_1line);
        spDias.setAdapter(adapterDias);

        MetodoPoblarLugares();

        spDias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Dia = adapterView.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        btBuscarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoBuscarHorario(ActivityAutentificacion.URLPruebas + "OperacionesHorarios/BuscarGETNuevaInterfaz.php?Dia=" + spDias.getSelectedItem() + "&Nombre=" + MetodoCodificarTexto(spLugares.getSelectedItem().toString()) + "");
            }
        });

        btLimpiarCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoLimpiarCampos();
            }
        });

        edHoraInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                selectedHour = c.get(Calendar.HOUR_OF_DAY);
                selectedMinute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityOperacionesHorarios.this, R.style.Theme_AppMovTlahuapan,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                edHoraInicio.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, selectedHour, selectedMinute, false);
                timePickerDialog.show();
            }
        });

        edHoraFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                selectedHour = c.get(Calendar.HOUR_OF_DAY);
                selectedMinute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityOperacionesHorarios.this,  R.style.Theme_AppMovTlahuapan,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                edHoraFin.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, selectedHour, selectedMinute, false);
                timePickerDialog.show();
            }
        });

        btInsertarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edHoraFin.getText().toString().isEmpty() || edHoraInicio.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"¡Faltan campos por llenar!", Toast.LENGTH_SHORT).show();
                } else {
                    MetodoValidarHorarioInexistenteInsertar(ActivityAutentificacion.URLPruebas + "OperacionesHorarios/ValidarHorarioInexistenteNuevaInterfaz.php?Dia=" + Dia + "&NombreLugar=" + MetodoCodificarTexto(spLugares.getSelectedItem().toString()) + "");
                }
            }
        });

        btModificarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edHoraFin.getText().toString().isEmpty() || edHoraInicio.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"¡Faltan campos por llenar!", Toast.LENGTH_SHORT).show();
                } else {
                    MetodoActualizarHorario(ActivityAutentificacion.URLPruebas + "OperacionesHorarios/EditarPOSTNuevaInterfaz.php");
                }
            }
        });

        btEliminarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoEliminarHorario(ActivityAutentificacion.URLPruebas + "OperacionesHorarios/EliminarPOST.php");
                MetodoLimpiarCampos();
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

    private void MetodoBuscarHorario(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        lbIdHorarioBusqueda.setText(jsonObject.getString("IdHorario"));
                        edHoraInicio.setText(jsonObject.getString("HoraInicio"));
                        edHoraFin.setText(jsonObject.getString("HoraFin"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Horario aun no asignado.", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoValidarHorarioInexistenteInsertar(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Toast.makeText(getApplicationContext(), "Horario ya asignado", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MetodoInsertarHorario(ActivityAutentificacion.URLPruebas + "OperacionesHorarios/InsertarPOSTNuevaInterfaz.php");
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoInsertarHorario(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("Dia", Dia);
                parametros.put("HoraIn", edHoraInicio.getText().toString());
                parametros.put("HoraFn", edHoraFin.getText().toString());
                parametros.put("NombreLugar", spLugares.getSelectedItem().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoActualizarHorario(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("Dia", Dia);
                parametros.put("HoraIn", edHoraInicio.getText().toString());
                parametros.put("HoraFn", edHoraFin.getText().toString());
                parametros.put("NombreLugar", spLugares.getSelectedItem().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoEliminarHorario(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("IdHorario", lbIdHorarioBusqueda.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }
    
    public void MetodoLimpiarCampos (){
        edHoraFin.setText("");
        edHoraInicio.setText("");
        lbIdHorarioBusqueda.setText("");
    }
}