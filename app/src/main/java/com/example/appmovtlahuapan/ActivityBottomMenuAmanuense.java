package com.example.appmovtlahuapan;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appmovtlahuapan.databinding.ActivityBottomMenuAdministrativoBinding;
import com.example.appmovtlahuapan.databinding.ActivityBottomMenuAmanuenseBinding;

public class ActivityBottomMenuAmanuense extends ActivityBasic {

    ActivityBottomMenuAmanuenseBinding bindingAmanuense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingAmanuense = ActivityBottomMenuAmanuenseBinding.inflate(getLayoutInflater());
        setContentView(bindingAmanuense.getRoot());

        MetodoRemplazarFragmento(new FragmentAdministrativoSitiosTuristicos());

        bindingAmanuense.bottomNavigationViewAmanuense.setBackground(null);

        bindingAmanuense.bottomNavigationViewAmanuense.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.ServiciosAmanuense){
                MetodoRemplazarFragmento(new FragmentAdministrativoSitiosTuristicos());
            }
            return true;
        });
    }

    private void MetodoRemplazarFragmento (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_amanuense,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityAutentificacion.class);
        startActivity(intent);
    }
}