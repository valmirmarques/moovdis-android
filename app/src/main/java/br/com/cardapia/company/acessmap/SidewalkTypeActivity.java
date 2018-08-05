package br.com.cardapia.company.acessmap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.model.LatLng;

import br.com.cardapia.company.acessmap.util.MoovDisContants;
import io.fabric.sdk.android.Fabric;


public class SidewalkTypeActivity extends FragmentActivity implements SidewalkTypeFragment.OnFragmentInteractionListener,
        SidewalkTypeFragment.SidewalkTypeFragmentListener, MoovDisContants {

    /**
     * Criacao da Tela.
     *
     * @param savedInstanceState param
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidewalk_type);

        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
        }

        double[] currentLocation = getIntent().getDoubleArrayExtra(CURRENT_LOCATION);
        String idUser = getIntent().getStringExtra(ID_USUARIO);

        SidewalkTypeFragment fragment = new SidewalkTypeFragment();
        if (currentLocation != null && currentLocation.length >= 2) {
            fragment.setCurrentLocation(new LatLng(currentLocation[0], currentLocation[1]));
        } else {
            fragment.setCurrentLocation(new LatLng(LAT_UTFR, LONG_UTFPR));
        }

        fragment.setIdUser(idUser);

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.add(R.id.item_detail_container_activity, fragment).commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDialogPositiveClick(SidewalkTypeFragment fragment) {
        Intent intent = new Intent();
        intent.putExtra(INSERT_WIDEWALK, true);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDialogNegativeClick(SidewalkTypeFragment fragment) {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
