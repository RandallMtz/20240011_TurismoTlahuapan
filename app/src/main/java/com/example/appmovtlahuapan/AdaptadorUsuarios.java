package com.example.appmovtlahuapan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.UsuarioViewHolder> {

    private Context mCtx;
    private List<AdquirirUsuarios> UsuarioList;

    public AdaptadorUsuarios(Context mCtx, List<AdquirirUsuarios> usuarios) {
        this.mCtx = mCtx;
        this.UsuarioList = usuarios;
    }

    @NonNull
    @Override
    public AdaptadorUsuarios.UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_lista_empleados,null);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorUsuarios.UsuarioViewHolder holder, int position) {
        AdquirirUsuarios Usuario = UsuarioList.get(position);

        holder.Usuario.setText(Usuario.getUsuario());
        holder.Nombre.setText(Usuario.getNombre());
        holder.ApellidoPaterno.setText(Usuario.getApePater());
        holder.ApellidoMaterno.setText(Usuario.getApeMater());
        holder.Correo.setText(Usuario.getCorreo());
        holder.Telefono.setText(Usuario.getTelefono());

    }

    @Override
    public int getItemCount() {
        return UsuarioList.size();
    }

    class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView ApellidoPaterno, ApellidoMaterno, Correo, Nombre, Telefono, Usuario;
        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);

            ApellidoMaterno = itemView.findViewById(R.id.TextVApeMaterListadoUsuarios);
            ApellidoPaterno = itemView.findViewById(R.id.TextVApePaterListadoUsuarios);
            Correo = itemView.findViewById(R.id.TextVCorreoListadoUsuarios);
            Nombre = itemView.findViewById(R.id.TextVNombreListadoUsuarios);
            Telefono = itemView.findViewById(R.id.TextVTelefonoListadoUsuarios);
            Usuario = itemView.findViewById(R.id.TextVUsuarioListadoUsuarios);
        }
    }
}
