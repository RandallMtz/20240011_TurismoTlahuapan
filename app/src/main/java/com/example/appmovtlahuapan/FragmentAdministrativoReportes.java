package com.example.appmovtlahuapan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FragmentAdministrativoReportes extends Fragment {

    private AsyncHttpClient cliente;
    private CardView cdEliminarComentarios, cdReporteClientes, cdReporteComentarios, cdReporteComentariosUno, cdRevisiones;
    private SearchableSpinner spLugares;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_administrativo_reportes, container, false);

        cliente = new AsyncHttpClient();

        cdEliminarComentarios = view.findViewById(R.id.EliminarComentariosUnoCardAdministrador); cdReporteClientes = view.findViewById(R.id.ReporteClientesCardAdministrador);
        cdReporteComentarios = view.findViewById(R.id.ReporteComentariosCardAdministrador); cdReporteComentariosUno = view.findViewById(R.id.ReporteComentariosUnoCardAdministrador);
        cdRevisiones = view.findViewById(R.id.RevisionesCardAdministrador);

        spLugares = view.findViewById(R.id.SpinnerLugaresReportes);
        spLugares.setTitle("Selecciona un sitio turistico");
        spLugares.setPositiveButton("Cerrar");

        if (ActivityAutentificacion.NivelAdministracionUsuario.equals("Ayuntamiento")){
            ViewGroup.LayoutParams layoutParams = cdReporteClientes.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;;
            cdReporteClientes.setLayoutParams(layoutParams);
            cdReporteComentarios.setEnabled(false);
            cdReporteComentariosUno.setEnabled(false);
            spLugares.setEnabled(false);
            cdEliminarComentarios.setEnabled(false);
            cdReporteComentarios.setVisibility(ViewGroup.INVISIBLE);
            cdReporteComentariosUno.setVisibility(ViewGroup.INVISIBLE);
            spLugares.setVisibility(ViewGroup.INVISIBLE);
            cdEliminarComentarios.setVisibility(ViewGroup.INVISIBLE);
        }

        MetodoPoblarLugares();

        cdRevisiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityListadoLugaresBotones.class);
                startActivity(intent);

            }
        });

        cdReporteClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(ActivityAutentificacion.URLPruebas + "ReporteClientes.php");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        cdReporteComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(ActivityAutentificacion.URLPruebas + "ReporteComentariosTodos.php");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        cdReporteComentariosUno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(ActivityAutentificacion.URLPruebas + "ReporteComentariosUno.php?Nombre=" + spLugares.getSelectedItem() + "");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        cdEliminarComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityListadoEliminarComentarios.class);
                intent.putExtra("Nombre",spLugares.getSelectedItem().toString());
                startActivity(intent);

            }
        });

        return view;
    }

    private void MetodoPoblarLugares(){
        cliente.post(ActivityAutentificacion.URLPruebas + "Componentes/SpinnerLugares.php", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    MetodoCargarLugares(new String((responseBody)));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });
    }

    private void MetodoCargarLugares(String respuesta){
        ArrayList<AdquirirLugares> lista = new ArrayList<AdquirirLugares>();
        try {
            JSONArray jsonArreglo = new JSONArray(respuesta);
            for (int i = 0; i < jsonArreglo.length(); i++){
                AdquirirLugares rz = new AdquirirLugares();
                rz.setNombre(jsonArreglo.getJSONObject(i).getString("Nombre"));
                lista.add(rz);
            }
            ArrayAdapter<AdquirirLugares> a = new ArrayAdapter <AdquirirLugares> (getContext(), android.R.layout.simple_dropdown_item_1line, lista);
            spLugares.setAdapter(a);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}