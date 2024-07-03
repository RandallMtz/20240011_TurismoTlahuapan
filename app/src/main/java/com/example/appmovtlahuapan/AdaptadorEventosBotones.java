package com.example.appmovtlahuapan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class AdaptadorEventosBotones extends RecyclerView.Adapter<AdaptadorEventosBotones.EventosViewHolder> {

    private Context mCtx;
    private List<AdquirirEventosProximos> EventosList;
    private RequestQueue requestQueue;

    public AdaptadorEventosBotones(Context mCtx, List<AdquirirEventosProximos> eventos) {
        this.mCtx = mCtx;
        this.EventosList = eventos;

        MySingleton singleton = MySingleton.getInstance(mCtx.getApplicationContext());
        requestQueue = singleton.getRequestQueue();
    }

    @NonNull
    @Override
    public AdaptadorEventosBotones.EventosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_lista_eventos_botones,null);
        return new EventosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorEventosBotones.EventosViewHolder holder, int position) {
        AdquirirEventosProximos eventos = EventosList.get(position);

        holder.Nombre.setText(eventos.getNombre());
        holder.Descripcion.setText(eventos.getDescripcion());
        holder.Fecha.setText(eventos.getFecha());
        holder.FechaFinal.setText(eventos.getFechaFinal());
        holder.Hora.setText(eventos.getHora());
        holder.HoraFinal.setText(eventos.getHoraFinal());
        holder.Lugar.setText(eventos.getLugar());
        Glide.with(mCtx)
                .load(eventos.getFoto() + "?timestamp=" + System.currentTimeMillis())
                .into(holder.Imagen);

        holder.btEliminarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ActivityAutentificacion.URLPruebas + "OperacionesEventos/EliminarPOST.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(view.getContext(), "Evento eliminado. Recarga la pantalla.", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(),error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map <String, String> parametros = new HashMap<String,String>();
                        parametros.put("IdEvento", eventos.getIdEvento());
                        return parametros;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return EventosList.size();
    }

    class EventosViewHolder extends RecyclerView.ViewHolder {

        Button btEliminarEvento;
        ImageView Imagen;
        TextView Descripcion, Fecha, FechaFinal, Hora, HoraFinal, Lugar, Nombre;
        public EventosViewHolder(@NonNull View itemView) {
            super(itemView);

            btEliminarEvento = itemView.findViewById(R.id.eliminarEventoCustomEventos);
            Imagen = itemView.findViewById(R.id.imageView2Botones);
            Nombre = itemView.findViewById(R.id.TextVNombreListadoEventosBotones);
            Descripcion = itemView.findViewById(R.id.TextVDescripcionListadoEventosBotones);
            Fecha = itemView.findViewById(R.id.TextVFechaListadoEventosBotones);
            FechaFinal = itemView.findViewById(R.id.TextVFechaFinalListadoEventosBotones);
            Hora = itemView.findViewById(R.id.TextVHoraListadoEventosBotones);
            HoraFinal = itemView.findViewById(R.id.TextVHoraFinalListadoEventosBotones);
            Lugar = itemView.findViewById(R.id.TextVLugarListadoEventosBotones);
        }
    }
}
