package br.com.cardapia.company.acessmap;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;

import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.cardapia.company.acessmap.bean.CadastroRetornoBean;
import br.com.cardapia.company.acessmap.bean.LocationBean;
import br.com.cardapia.company.acessmap.bean.MarkerBean;
import br.com.cardapia.company.acessmap.bean.SidewalkBean;
import br.com.cardapia.company.acessmap.bean.UsuarioBean;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.LatLongUtils;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import mt.slider.helper.SliderHelper;
import mt.slider.interfaces.OnSliderIndexChangeListener;
import mt.slider.model.SliderItem;

public class SidewalkTypeFragment extends Fragment implements
        View.OnClickListener,
        OnSliderIndexChangeListener,
        MoovDisContants,
        SeekBar.OnSeekBarChangeListener,
        OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleMap mMap;
    private MapView mMapView;
    private View mView;

    private SliderHelper mSliderHelper;
    private TextView mTextViewLabel;
    private ArrayList<SliderItem> mSliderItemList;
    private OnFragmentInteractionListener mListener;
    private SeekBar seekBar;
    private TextView textDificulade;
    private TextView txtDistanceRsult;

    private LatLng currentLocation;
    private String idUser;

    private String[] dificultLevel = null;
    private int[] colorsDificultLevel = null;
    private int[] customDificultLevel = null;
    private int[] imageResourceIds = null;

    private List<LatLng> lstLatLngs = new ArrayList<>();

    SidewalkTypeFragmentListener fragListener;

    final Fragment me = this;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SidewalkTypeFragment newInstance(String param1, String param2) {
        SidewalkTypeFragment fragment = new SidewalkTypeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.sidewalk_type_fragment, container, false);

        if (this.getActivity() != null) {
            MapsInitializer.initialize(this.getActivity());

            if (BuildConfig.enableCrashlytics) {
                Fabric.with(getContext(), new Crashlytics());
            }

            ButterKnife.bind(getActivity());
        }


        seekBar = mView.findViewById(R.id.seek_bar_dificuldade);
        seekBar.setOnSeekBarChangeListener(this);
        textDificulade = mView.findViewById(R.id.txt_grau_dificuldade);
        txtDistanceRsult = mView.findViewById(R.id.txt_sidewalk_distance);

        initSlider(mView);

        Button btnCancel = (Button) mView.findViewById(R.id.btn_cancelar_sidewalk);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(me).commit();
                    fragListener.onDialogNegativeClick(SidewalkTypeFragment.this);
                }
            }
        });

        ImageButton btnProsseguir = (ImageButton) mView.findViewById(R.id.btn_ok_1_sidewalk);
        btnProsseguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextPage(mView);
            }
        });

        Button btnOK = (Button) mView.findViewById(R.id.btn_ok_2_sidewalk);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lstLatLngs.size() < 2) {
                    MoovDisUtil.showErrorMessage(getContext(), getString(R.string.ops), getString(R.string.sidewalk_mark_validadtion));
                    return;
                }

                SidewalkBean bean = new SidewalkBean();

                LocationBean locationBeanStart = new LocationBean();
                locationBeanStart.setType(LOCATION_TYPE_POINT);
                locationBeanStart.setCoordinates(new double[]{lstLatLngs.get(0).longitude, lstLatLngs.get(0).latitude});

                LocationBean locationBeanEnd = new LocationBean();
                locationBeanEnd.setType(LOCATION_TYPE_POINT);
                locationBeanEnd.setCoordinates(new double[]{lstLatLngs.get(1).longitude, lstLatLngs.get(1).latitude});

                UsuarioBean usuarioBean = new UsuarioBean();
                usuarioBean.set_id(getIdUser());
                bean.setLocationStart(locationBeanStart);
                bean.setLocationEnd(locationBeanEnd);
                bean.setSidewalkType(SidewalkBean.SidewalkTypeEnum.fromId(mSliderHelper.getSliderPositionIndex()));
                bean.setDateCreated(new Date());
                bean.setUser(usuarioBean);
                bean.setDificultLevel(seekBar.getProgress());

                new SidewalkAddRequestTask().execute(bean);

            }
        });

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // INIT THE MAPS
        // Inflate the layout for this fragment
        mMapView = (MapView) mView.findViewById(R.id.map_detail_sidewalk);
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }


        mMapView.setVisibility(View.GONE);
    }

    /**
     * Go to the next Page
     * @param rootView
     */
    private void goToNextPage(final View rootView) {

        final int widthScreen = getDisplaySize()[0];
        final float ratio = 1.2f;
        final int intImageSize = (int) (widthScreen / ratio);

        mTextViewLabel.setVisibility(View.GONE);
        rootView.findViewById(R.id.layout_result_sidewalk).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.card_grau_dificuldade).setVisibility(View.GONE);
        rootView.findViewById(R.id.card_next).setVisibility(View.GONE);
        rootView.findViewById(R.id.slider).setVisibility(View.GONE);
        ((ImageView) rootView.findViewById(R.id.img_indicator_page)).setImageDrawable(getResources().getDrawable(R.drawable.page_2_of_2));
        rootView.findViewById(R.id.btn_ok_2_sidewalk).setVisibility(View.VISIBLE);
        ((ImageView) rootView.findViewById(R.id.img_sidewalk_selected)).setImageDrawable(getResources().getDrawable(imageResourceIds[mSliderHelper.getSliderPositionIndex()]));
        ((TextView)rootView.findViewById(R.id.txt_sidewalk_selected)).setText(mTextViewLabel.getText());
        txtDistanceRsult.setVisibility(View.VISIBLE);
        txtDistanceRsult.setText(getString(R.string.distance_metros, "0"));

        TextView txtToque = rootView.findViewById(R.id.txt_toque);
        if (txtToque.getTextSize() >= 42.0f) {
            txtToque.setTextSize(14.0f); // AO ATRIBUIR TEXTO A UNIDADE MUDA
        }

        mMapView.setVisibility(View.VISIBLE);

    }

    private void initSlider(View rootView) {
        mSliderItemList = generateItems();
        mTextViewLabel = (TextView) rootView.findViewById(R.id.textViewCategoryLabel);
        View slider = rootView.findViewById(R.id.slider);
        updateLabel(0);
        // Helper (add slider view items)
        mSliderHelper = new SliderHelper(getContext(), mSliderItemList, false, slider);

        mSliderHelper.setOnSliderIndexChangeListener(this);
        mSliderHelper.setSlideButtonResources(R.drawable.ic_button_left, R.drawable.ic_button_right);

    }

    private ArrayList<SliderItem> generateItems() {
        ArrayList<SliderItem> items = new ArrayList<>();

        dificultLevel = new String[] {
                getString(R.string.grau_0),
                getString(R.string.grau_1),
                getString(R.string.grau_2),
                getString(R.string.grau_3),
                getString(R.string.grau_4),
                getString(R.string.grau_5)
        };

        colorsDificultLevel = new int[] {
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_light,
                android.R.color.holo_red_dark
        };

        customDificultLevel = new int[] {
                2,
                4,
                3,
                4,
                3,
                1,
                5,
                1,
                3
        };

        int[] colorResourceIds = {
                android.R.color.holo_green_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_green_dark
        };
        imageResourceIds = new int[] {
                R.drawable.calc_1,
                R.drawable.calc_2,
                R.drawable.calc_3,
                R.drawable.calc_4,
                R.drawable.calc_5,
                R.drawable.calc_6,
                R.drawable.calc_7,
                R.drawable.calc_8,
                R.drawable.calc_9
        };

        String[] imageLabels = {
                getString(R.string.calc_type_1),
                getString(R.string.calc_type_2),
                getString(R.string.calc_type_3),
                getString(R.string.calc_type_4),
                getString(R.string.calc_type_5),
                getString(R.string.calc_type_6),
                getString(R.string.calc_type_7),
                getString(R.string.calc_type_8),
                getString(R.string.calc_type_9)
        };

        final int widthScreen = getDisplaySize()[0];
        final float ratio = 2.5f;

        for (int i = 0; i < imageResourceIds.length; i++) {
            items.add(new SliderItem(
                    imageLabels[i],
                    imageResourceIds[i],
                    colorResourceIds[i],
                    (int) (widthScreen / (ratio * 2)),
                    (int) (widthScreen / ratio)
            ));
        }
        return items;
    }

    private void updateLabel(int newIndex) {
        mTextViewLabel.setText(mSliderItemList.get(newIndex).getLabel());
        mTextViewLabel.setTextColor(getResources().getColor(mSliderItemList.get(newIndex).getColorID()));

        // UPDTE LABEL DIFFICUT
        if (customDificultLevel != null) {
            updateDifficultLevel(customDificultLevel[newIndex]);
        }

    }

    private void updateDifficultLevel(int newIndex) {
        textDificulade.setText(dificultLevel[newIndex]);
        if (getContext() != null) {
            textDificulade.setTextColor(ContextCompat.getColor(getContext(), colorsDificultLevel[newIndex]));
        }
        seekBar.setProgress(newIndex);

    }

    @Override
    public void OnSliderIndexChanged(int newIndex) {
        Log.d(MainActivity.class.getSimpleName(), "OnSliderIndexChanged newIndex: " + newIndex);
        updateLabel(newIndex);
    }

    @Override
    public void onClick(View v) {
        // get selected slider index
        int index = mSliderHelper.getSliderPositionIndex();
        Log.d("onSlideClick", String.format("Clicou no item %s", index));
        switch (index) {
            case 0:
                break;
            case 1:
                break;
            default:
                break;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            fragListener = (SidewalkTypeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
            fragListener = (SidewalkTypeFragmentListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*
        Fragment fragment = (getChildFragmentManager().findFragmentById(R.id.map_detail_sidewalk));
        if (fragment != null){
            getChildFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
        */

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        Log.d("onProgressChanged", String.format("%s %s", i, b));
        updateDifficultLevel(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("onMapReady", "Carregou o Mapa");
        mMap = googleMap;

        if (getContext() != null) {

            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getContext(), R.raw.style_json));

            } catch (Resources.NotFoundException e) {
            }
        }

        LatLng posicao = new LatLng(-25.4405,-49.274);

        if (getCurrentLocation() != null) {
            posicao = getCurrentLocation();
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(posicao));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                lstLatLngs.add(point);
                if (lstLatLngs.size() > 2) {
                    lstLatLngs.remove(0);
                }
                drawLines();
            }
        });
    }

    private void drawLines() {

        mMap.clear();

        for (int i = 0; i < lstLatLngs.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(lstLatLngs.get(i)).draggable(true));
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {

                }
            });
        }

        if (lstLatLngs.size() > 1) {
            int indexTipo = mSliderHelper.getSliderPositionIndex();
            SidewalkBean.SidewalkTypeEnum typeEnum = SidewalkBean.SidewalkTypeEnum.fromId(indexTipo);

            mMap.addPolyline(MoovDisUtil.makeSidewalkLineColorPrincipal(typeEnum, lstLatLngs));
            mMap.addPolyline(MoovDisUtil.makeSidewalkLineColorSecundary(typeEnum, lstLatLngs));

            double distance = LatLongUtils.distance(lstLatLngs.get(0).latitude,
                    lstLatLngs.get(0).longitude,
                    lstLatLngs.get(1).latitude,
                    lstLatLngs.get(1).longitude,
                    LatLongUtils.METERS);

            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(1);
            txtDistanceRsult.setText(getString(R.string.distance_metros, nf.format(distance)));
            if (getActivity() != null) {
                Toast.makeText(getActivity(), txtDistanceRsult.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    private int[] getDisplaySize() {
        int deviceWidth = 480;
        int deviceHeight = 640;

        if (getContext() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowmanager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
            deviceWidth = displayMetrics.widthPixels;
            deviceHeight = displayMetrics.heightPixels;
        }

        return new int[] {deviceWidth, deviceHeight};
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface SidewalkTypeFragmentListener {
        void onDialogPositiveClick(SidewalkTypeFragment fragment);
        void onDialogNegativeClick(SidewalkTypeFragment fragment);
    }


    @Override
    public void onResume() {
        if (mMapView != null) {
            mMapView.onResume();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null) {
            mMapView.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }



    /**
     * Rest to Sidewalk add
     */
    private class SidewalkAddRequestTask extends AsyncTask<SidewalkBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(SidewalkBean... params) {
            try {
                Log.d("SidewalkAddRequestTask", params.toString());
                MoovDisUtil.showProgress(getContext(), getString(R.string.saving));
                final String url = MoovdisServices.getApiPostSidewalkAddUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                return restTemplate.postForObject(url, params[0], CadastroRetornoBean.class);
            } catch (Exception e) {
                Log.e("SidewalkAddRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(getContext(), getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(CadastroRetornoBean retorno) {
            if (retorno != null) {
                Toast.makeText(getContext(), getString(R.string.sidewalk_sucesso), Toast.LENGTH_SHORT).show();

                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(me).commit();
                    fragListener.onDialogPositiveClick(SidewalkTypeFragment.this);
                }

            }
        }
    }


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

}
