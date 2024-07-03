package com.example.appmovtlahuapan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class ActivitySubirImagenes extends ActivityBasic {

    Bitmap bitmap;
    private Button btSeleccionarImagen, btSubirImagen, btVerImagenes;
    private EditText edDescripcion;
    private ImageView imImagen;
    
    private int  IdLugarActivityAnterior, PICK_IMAGE_REQUEST = 1;
    private String KEY_IMAGE = "foto", KEY_NOMBRE = "nombre", UPLOAD_URL = ActivityAutentificacion.URLPruebas + "Componentes/SubirImagen.php";

    private RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_imagenes);

        btSeleccionarImagen = findViewById(R.id.ButtonImgImagenesOp);
        btSubirImagen = findViewById(R.id.ButtonSubirImagenesImagenesOp);
        btVerImagenes = findViewById(R.id.ButtonEliminarImagenesImagenesOp);
        
        imImagen = findViewById(R.id.ImageVImagenSeleccionadaImagenesOp);
        
        edDescripcion = findViewById(R.id.editTextDescripcionImagen);

        Intent intent = getIntent();
        IdLugarActivityAnterior = intent.getExtras().getInt("IdLugar");

        MySingleton singleton = MySingleton.getInstance(getApplicationContext());
        requestQueue = singleton.getRequestQueue();

        btSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btSubirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imImagen.getDrawable() == null){
                    Toast.makeText(ActivitySubirImagenes.this, "¡Selecciona una imagen!", Toast.LENGTH_LONG).show();
                } else {
                    uploadImage();
                }
            }
        });

        btVerImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityListadoEliminarImagenes.class);
                intent.putExtra("IdLugar", IdLugarActivityAnterior);
                startActivity(intent);
            }
        });
    }

    private void uploadImage() {
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        imImagen.setImageDrawable(null);
                        edDescripcion.setText("");
                        Toast.makeText(ActivitySubirImagenes.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(ActivitySubirImagenes.this, error.getMessage() + "", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = getStringImagen(bitmap);
                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, imagen);
                params.put(KEY_NOMBRE, "null");
                params.put("IdLugar", String.valueOf(IdLugarActivityAnterior));
                params.put("descripcion", edDescripcion.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imImagen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}