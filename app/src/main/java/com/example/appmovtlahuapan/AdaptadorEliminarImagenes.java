package com.example.appmovtlahuapan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptadorEliminarImagenes extends RecyclerView.Adapter<AdaptadorEliminarImagenes.EventosViewHolder> {

    private Context mCtx;
    private List<AdquirirSitioTuristicoImagenes> ImagenList;
    private RequestQueue requestQueue;

    public AdaptadorEliminarImagenes(Context mCtx, List<AdquirirSitioTuristicoImagenes> imagenes) {
        this.mCtx = mCtx;
        this.ImagenList = imagenes;

        MySingleton singleton = MySingleton.getInstance(mCtx.getApplicationContext());
        requestQueue = singleton.getRequestQueue();
    }

    @NonNull
    @Override
    public AdaptadorEliminarImagenes.EventosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_lista_eliminar_imagenes,null);
        return new EventosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorEliminarImagenes.EventosViewHolder holder, int position) {
        AdquirirSitioTuristicoImagenes imagen = ImagenList.get(position);

        Glide.with(mCtx)
                .load(imagen.getFoto() + "?timestamp=" + System.currentTimeMillis())
                .into(holder.Imagen);


        holder.btEliminarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ActivityAutentificacion.URLPruebas + "Componentes/EliminarImagenPOST.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(view.getContext(), "Imagen eliminada. Recarga la pantalla.", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(),error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map <String, String> parametros = new HashMap<String,String>();
                        parametros.put("IdImagen", imagen.getIdImagen());
                        return parametros;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ImagenList.size();
    }

    class EventosViewHolder extends RecyclerView.ViewHolder {

        Button btEliminarImagen;
        ImageView Imagen;
        public EventosViewHolder(@NonNull View itemView) {
            super(itemView);

            btEliminarImagen = itemView.findViewById(R.id.btEliminarImagenesElim);
            Imagen = itemView.findViewById(R.id.ImageVImagenListadoImagenesElim);
        }
    }
}
