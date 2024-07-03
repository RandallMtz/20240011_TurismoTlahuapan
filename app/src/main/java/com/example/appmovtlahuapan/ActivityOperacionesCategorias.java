package com.example.appmovtlahuapan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ActivityOperacionesCategorias extends AppCompatActivity {

    Bitmap bitmap;
    private AsyncHttpClient cliente;
    private Button btBuscarCategoria, btEliminarCategoria, btInsertarCategoria, btLimpiarCampos, btModificarCategoria, btSeleccionarImagen;
    private EditText edCategoria;
    private ImageView imFotografiaPromocional;
    private RequestQueue requestQueue;
    private SearchableSpinner spCategorias;
    
    private int PICK_IMAGE_REQUEST = 1;
    private String CategoriaBusqueda, SeleccionImagen = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operaciones_categorias);

        cliente = new AsyncHttpClient();

        btBuscarCategoria = findViewById(R.id.ButtonBuscarCategoriasOp); btEliminarCategoria = findViewById(R.id.ButtonEliminarCategoriasOp);
        btInsertarCategoria = findViewById(R.id.ButtonInsertarCategoriasOp); btLimpiarCampos = findViewById(R.id.ButtonLimpiarCamposCategoriasOp);
        btModificarCategoria = findViewById(R.id.ButtonModificarCategoriasOp); btSeleccionarImagen = findViewById(R.id.BtSeleccionarImagenOperacionesCategorias);

        edCategoria = findViewById(R.id.EditTCategoriaOp);

        imFotografiaPromocional = findViewById(R.id.ImgVFotografiaOperacionesCategorias);

        spCategorias = findViewById(R.id.SpinnerCategoriasBuscar);
        spCategorias.setTitle("Selecciona una categoría");
        spCategorias.setPositiveButton("Cerrar");

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        MetodoPoblarCategorias();

        btBuscarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodoBuscarCategoria(ActivityAutentificacion.URLPruebas + "OperacionesCategorias/BuscarGET.php?Categoria=" + spCategorias.getSelectedItem() + "");
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

        btInsertarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edCategoria.getText().toString().isEmpty()){
                    MetodoValidarCategoriaInexistenteInsertar(ActivityAutentificacion.URLPruebas + "OperacionesCategorias/ValidarCategoriaInexistente.php?Categoria=" + edCategoria.getText() + "");
                } else {
                    edCategoria.setError("¡Es necesario un nombre!");
                }
            }
        });

        btModificarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edCategoria.getText().toString().isEmpty()){
                    MetodoValidarCategoriaInexistenteModificar(ActivityAutentificacion.URLPruebas + "OperacionesCategorias/ValidarCategoriaInexistenteModificar.php?Categoria=" + edCategoria.getText() + "&Categoria1=" + CategoriaBusqueda + "");
                } else {
                    edCategoria.setError("¡Es necesario un nombre!");
                }
            }
        });

        btEliminarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edCategoria.getText().toString().isEmpty()){
                    MetodoEliminarCategoria(ActivityAutentificacion.URLPruebas + "OperacionesCategorias/EliminarPOST.php");
                } else {
                    edCategoria.setError("¡Es necesario un nombre!");
                }
            }
        });
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
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
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

    private void MetodoBuscarCategoria (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        SeleccionImagen = "";
                        edCategoria.setText(jsonObject.getString("Categoria"));
                        CategoriaBusqueda = jsonObject.getString("Categoria");
                        Glide.with(getApplicationContext())
                                .load(jsonObject.getString("FotoPromocional") + "?timestamp=" + System.currentTimeMillis())
                                .into(imFotografiaPromocional);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Categoría inexistente", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoValidarCategoriaInexistenteInsertar (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edCategoria.setError("¡Intenta con una categoria distinta!");
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (edCategoria.getText().toString().isEmpty() || !SeleccionImagen.equals("hola")) {
                    Toast.makeText(getApplicationContext(), "¡Debes completar la información!", Toast.LENGTH_SHORT).show();
                } else if (!edCategoria.getText().toString().isEmpty() && SeleccionImagen.equals("hola")) {
                    MetodoInsertarCategoria(ActivityAutentificacion.URLPruebas + "OperacionesCategorias/InsertarPOST.php");
                } else {
                    Toast.makeText(getApplicationContext(), "¡Faltan campos por llenar!", Toast.LENGTH_SHORT).show();
                }
                SeleccionImagen = "";
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoInsertarCategoria(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarCategorias();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String,String>();
                parametros.put("Categoria", edCategoria.getText().toString());
                parametros.put("Fotografia", getStringImagen(bitmap));
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoValidarCategoriaInexistenteModificar(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Toast.makeText(getApplicationContext(), "Categoria ya registrada", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (SeleccionImagen.equals("")) {
                    MetodoActualizarCategoriaSinImagen(ActivityAutentificacion.URLPruebas + "OperacionesCategorias/EditarPOST.php");
                } else {
                    MetodoActualizarCategoriaConImagen(ActivityAutentificacion.URLPruebas + "OperacionesCategorias/EditarPOSTImagen.php");
                }
                SeleccionImagen = "";
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void MetodoActualizarCategoriaSinImagen(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarCategorias();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("CategoriaBus", CategoriaBusqueda);
                parametros.put("Categoria", edCategoria.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoActualizarCategoriaConImagen(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarCategorias();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("CategoriaBus", CategoriaBusqueda);
                parametros.put("Categoria", edCategoria.getText().toString());
                parametros.put("Fotografia", getStringImagen(bitmap));
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void MetodoEliminarCategoria(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡Operación exitosa!", Toast.LENGTH_SHORT).show();
                MetodoLimpiarCampos(); MetodoPoblarCategorias();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parametros = new HashMap<String,String>();
                parametros.put("Categoria", edCategoria.getText().toString());
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

    public void MetodoLimpiarCampos (){
        SeleccionImagen = "";
        edCategoria.setText("");
        imFotografiaPromocional.setImageBitmap(null);
        spCategorias.setSelection(0);
    }
}