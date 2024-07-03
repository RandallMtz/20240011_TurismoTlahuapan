package com.example.appmovtlahuapan;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appmovtlahuapan.databinding.ActivityBottomMenuAdministrativoBinding;

import java.io.File;

public class ActivityBottomMenuAdministrativo extends ActivityBasic {
    ActivityBottomMenuAdministrativoBinding bindingAdministrativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingAdministrativo = ActivityBottomMenuAdministrativoBinding.inflate(getLayoutInflater());
        setContentView(bindingAdministrativo.getRoot());

        MetodoRemplazarFragmento(new FragmentAdministrativoUsuarios());

        MetodoLimpiarCache();

        bindingAdministrativo.bottomNavigationViewAdministrativo.setBackground(null);

        bindingAdministrativo.bottomNavigationViewAdministrativo.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.UsuariosAdministrador){
                MetodoRemplazarFragmento(new FragmentAdministrativoUsuarios());
            } else if (item.getItemId() == R.id.ServiciosAdministrador){
                MetodoRemplazarFragmento(new FragmentAdministrativoSitiosTuristicos());
            } else if (item.getItemId() == R.id.CensosAdministrador){
                MetodoRemplazarFragmento(new FragmentAdministrativoReportes());
            }
            return true;
        });
    }

    private void MetodoRemplazarFragmento (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_administrativo,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityAutentificacion.class);
        startActivity(intent);
    }
}