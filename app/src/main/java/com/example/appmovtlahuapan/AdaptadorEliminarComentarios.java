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

public class AdaptadorEliminarComentarios extends RecyclerView.Adapter<AdaptadorEliminarComentarios.ComentariosViewHolder> {

    private Context mCtx;
    private List<AdquirirComentarios> ComentariosList;

    private RequestQueue requestQueue;

    public AdaptadorEliminarComentarios(Context mCtx, List<AdquirirComentarios> ComentariosLista) {
        this.mCtx = mCtx;
        this.ComentariosList = ComentariosLista;

        MySingleton singleton = MySingleton.getInstance(mCtx.getApplicationContext());
        requestQueue = singleton.getRequestQueue();
    }

    @NonNull
    @Override
    public AdaptadorEliminarComentarios.ComentariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_lista_eliminar_comentarios,null);
        return new ComentariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorEliminarComentarios.ComentariosViewHolder holder, int position) {
        AdquirirComentarios comentarios = ComentariosList.get(position);

        holder.Titulo.setText(comentarios.getNombre());
        holder.Comentario.setText(comentarios.getComentario());

        holder.btEliminarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ActivityAutentificacion.URLPruebas + "Componentes/EliminarComentarioPOST.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(view.getContext(), "Comentario eliminado. Recarga la pantalla.", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(),error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map <String, String> parametros = new HashMap<String,String>();
                        parametros.put("IdComentario", comentarios.getIdComentario());
                        return parametros;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ComentariosList.size();
    }

    class ComentariosViewHolder extends RecyclerView.ViewHolder {

        Button btEliminarComentario;
        TextView Comentario, Titulo;
        public ComentariosViewHolder(@NonNull View itemView) {
            super(itemView);

            btEliminarComentario = itemView.findViewById(R.id.btEliminarComentariosElim);
            Comentario = itemView.findViewById(R.id.TextVComentarioEliminarComentariosElim);
            Titulo = itemView.findViewById(R.id.TextVLugarEliminarComentariosElim);
        }
    }
}
