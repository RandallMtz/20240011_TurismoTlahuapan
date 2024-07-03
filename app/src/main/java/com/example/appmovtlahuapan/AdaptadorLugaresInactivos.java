package com.example.appmovtlahuapan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptadorLugaresInactivos extends RecyclerView.Adapter<AdaptadorLugaresInactivos.LugarViewHolder> {

    private Context mCtx;
    private List<AdquirirLugaresInactivos> LugaresList;
    private RequestQueue requestQueue;

    public AdaptadorLugaresInactivos(Context mCtx, List<AdquirirLugaresInactivos> lugares) {
        this.mCtx = mCtx;
        this.LugaresList = lugares;

        MySingleton singleton = MySingleton.getInstance(mCtx.getApplicationContext());
        requestQueue = singleton.getRequestQueue();
    }

    @NonNull
    @Override
    public AdaptadorLugaresInactivos.LugarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_lista_lugares_botones,null);
        return new LugarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorLugaresInactivos.LugarViewHolder holder, int position) {
        AdquirirLugaresInactivos Lugar = LugaresList.get(position);

        holder.Nombre.setText(Lugar.getNombre());

        holder.Informacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(ActivityAutentificacion.URLPruebas + "InformacionLugarRevision.php?Nombre=" + Lugar.getNombre() + "");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                view.getContext().startActivity(intent);

            }
        });

        holder.Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ActivityAutentificacion.URLPruebas + "OperacionesLugares/EditarPOSTRevision.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(view.getContext(), "Sitio aceptado. Recarga la pantalla.", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(),error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map <String, String> parametros = new HashMap<String,String>();
                        parametros.put("Nombre", Lugar.getNombre());
                        return parametros;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });

        holder.Denegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ActivityAutentificacion.URLPruebas + "OperacionesLugares/EliminarPOST.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(view.getContext(), "Sitio eliminado. Recarga la pantalla.", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(),error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map <String, String> parametros = new HashMap<String,String>();
                        parametros.put("Nombre", Lugar.getNombre());
                        return parametros;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return LugaresList.size();
    }

    class LugarViewHolder extends RecyclerView.ViewHolder {

        Button Aceptar, Denegar, Informacion;
        LinearLayout linearLayout;
        TextView Nombre;
        public LugarViewHolder(@NonNull View itemView) {
            super(itemView);

            Informacion = itemView.findViewById(R.id.btInfo);
            Aceptar = itemView.findViewById(R.id.btSi);
            Denegar = itemView.findViewById(R.id.btNo);
            Nombre = itemView.findViewById(R.id.TextVNombreListadoLugaresBotones);
            linearLayout = itemView.findViewById(R.id.LinearLayoutBotones);
        }
    }
}
