package br.com.cardapia.company.acessmap;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;

import org.springframework.web.client.RestTemplate;

import java.util.Date;

import br.com.cardapia.company.acessmap.bean.CadastroRetornoBean;
import br.com.cardapia.company.acessmap.bean.MarkerBean;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.refactor.lib.colordialog.ColorDialog;
import io.fabric.sdk.android.Fabric;

public class BottomSheetFragmentMain extends BottomSheetDialogFragment implements MoovDisContants {

    private final String TAG = BottomSheetFragmentMain.class.getSimpleName();
    private MarkerBean markerBean = null;
    private Unbinder unbinder;
    private MarkersListener callback;

    public BottomSheetFragmentMain() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.enableCrashlytics) {
            if (getActivity() != null) {
                Fabric.with(getActivity(), new Crashlytics());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.bottom_sheet_main, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.bottom_button_view_on_map)
    public void clickViewOnMap(final View view) {
        Log.d(TAG, "clickViewOnMap");
        if (getActivity() != null) {

            Intent intent = new Intent(getActivity(), MapsActivity.class);
            Location location = new Location(markerBean.getAddressDescription());
            location.setLatitude(markerBean.getLocation().getCoordinates()[1]);
            location.setLongitude(markerBean.getLocation().getCoordinates()[0]);
            location.setTime(new Date().getTime()); //Set time as current Date

            intent.putExtra(CURRENT_LOCATION, location);
            startActivity(intent);
        }
        hideBottomSheet();
    }

    @OnClick(R.id.bottom_button_like)
    public void clickLike(final View view) {
        Log.d(TAG, "clickLike");
        new MarkerLikeRequestTask().execute(markerBean);
    }

    @OnClick(R.id.bottom_button_ocur_not_exists)
    public void clickNotExists(final View view) {
        Log.d(TAG, "clickNotExists");


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {

                    ColorDialog dialog = new ColorDialog(getActivity());
                    dialog.setAnimationEnable(true);
                    dialog.setTitle(getString(R.string.delete_marker));
                    dialog.setContentText(getString(R.string.delete_marker_info));
                    dialog.setPositiveListener(getString(R.string.sim_lets_go), new ColorDialog.OnPositiveListener() {
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    dialog.dismiss();
                                    if (markerBean != null) {
                                        new MarkerDeleteRequestTask().execute(markerBean.get_id());
                                    }
                                }
                            });
                    dialog.setNegativeListener(getString(R.string.not_now), new ColorDialog.OnNegativeListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            dialog.dismiss();
                            hideBottomSheet();
                        }
                    });

                    dialog.show();
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        setMarkerBean(null);
        unbinder.unbind();
    }


    private void hideBottomSheet() {
        closefragment(this);
    }

    public void closefragment(Fragment fragment){
        if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    public MarkerBean getMarkerBean() {
        return markerBean;
    }

    public void setMarkerBean(MarkerBean markerBean) {
        this.markerBean = markerBean;
    }

    public MarkersListener getCallback() {
        return callback;
    }

    public void setCallback(MarkersListener callback) {
        this.callback = callback;
    }


    /**
     * Rest to Mark add General
     */
    private class MarkerLikeRequestTask extends AsyncTask<MarkerBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(MarkerBean... params) {
            try {
                Log.d("MarkerLikeRequestTask", params[0].get_id());
                final String url = MoovdisServices.getApiPostMarkerLikeUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                CadastroRetornoBean retorno = restTemplate.postForObject(url, params[0], CadastroRetornoBean.class);
                return retorno;
            } catch (Exception e) {
                Log.e("MarkerLikeRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(getActivity(), getString(R.string.ops), e.getMessage());
            } finally {
                closefragment(BottomSheetFragmentMain.this);
            }
            return null;
        }

        @Override
        protected void onPostExecute(CadastroRetornoBean retorno) {
            // DO NOTHING
        }
    }

    /**
     * Rest to Delete Marker
     */
    private class MarkerDeleteRequestTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                Log.d("MarkerDeleteRequestTask", params[0]);
                final String url = MoovdisServices.getApiDeleteMarkerUrl(params[0], YES);
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                restTemplate.delete(url);
            } catch (Exception e) {
                Log.e("MarkerDeleteRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(getActivity(), getString(R.string.ops), e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void retorno) {
            if (callback != null) {
                callback.markerNeedsUpdate();
            }
            closefragment(BottomSheetFragmentMain.this);
        }
    }

    /**
     * Invoke Listener to reaload Markers on Caller
     */
    public interface MarkersListener {
        void markerNeedsUpdate();
    }


}
