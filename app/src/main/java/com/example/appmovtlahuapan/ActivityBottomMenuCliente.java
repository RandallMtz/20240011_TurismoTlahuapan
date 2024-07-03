package com.example.appmovtlahuapan;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appmovtlahuapan.databinding.ActivityBottomMenuClienteBinding;

public class ActivityBottomMenuCliente extends ActivityBasic {

    ActivityBottomMenuClienteBinding bindingCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingCliente = ActivityBottomMenuClienteBinding.inflate(getLayoutInflater());
        setContentView(bindingCliente.getRoot());

        MetodoRemplazarFragmento(new FragmentGenericoSitiosTuristicosV2());

        bindingCliente.bottomNavigationViewCliente.setBackground(null);

        bindingCliente.bottomNavigationViewCliente.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.LugaresCliente){
                MetodoRemplazarFragmento(new FragmentGenericoSitiosTuristicosV2());
            } else if (item.getItemId() == R.id.MiPerfil) {
                MetodoRemplazarFragmento(new FragmentGenericoPerfil());
            } else if (item.getItemId() == R.id.ReferenciaCliente) {
                MetodoRemplazarFragmento(new FragmentGenericoInformacion());
            }
            return true;
        });
    }

    private void MetodoRemplazarFragmento (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_cliente, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityAutentificacion.class);
        startActivity(intent);
    }
}