package br.com.cardapia.company.acessmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import br.com.cardapia.company.acessmap.util.BitmapHelper;
import br.com.cardapia.company.acessmap.util.CameraIntentHelper;
import br.com.cardapia.company.acessmap.util.CameraIntentHelperCallback;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

/**
 * Created by tvtios-01 on 17/12/17.
 */

public class NoticeDialogFragment extends DialogFragment implements
        OnMapReadyCallback, MoovDisContants {


    private static CameraIntentHelper mCameraIntentHelper;
    private final String TAG = "CommentDialog";
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    //private static final int TAKE_PICTURE = 200;
    private byte[] markerImage;
    private GoogleMap mMap;
    private LatLng location;

    //public NoticeDialogFragment() {}
    private Button takePicture;
    private TextView txtComments;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Carregou o Mapa");
        mMap = googleMap;

        // ADD EVENTS MAP
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                setLocation(marker.getPosition());

                Toast.makeText(getContext(), R.string.adjusted_location, Toast.LENGTH_LONG).show();
            }
        });

        mMap.addMarker(new MarkerOptions().position(getLocation())
                .title(getString(R.string.arrastar))
                .snippet(getString(R.string.touch_arrastar))
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getContext();

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLng(getLocation()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(19), 2000, null);

        Toast.makeText(getContext(), R.string.can_arrastar, Toast.LENGTH_LONG).show();

    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        if (BuildConfig.enableCrashlytics) {
            if (getActivity() != null) {
                Fabric.with(getActivity(), new Crashlytics());
            }
        }

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), getTheme());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_comment_picture, null);
        builder.setView(rootView);

        ButterKnife.bind(getActivity());

        // Inflate the layout for this fragment
        Fragment fragment = getFragmentManager().findFragmentById(R.id.map_detail);
        SupportMapFragment mapFragment = (SupportMapFragment) fragment;
        mapFragment.getMapAsync(this);

        txtComments = (TextView) rootView.findViewById(R.id.txt_comment);
        txtComments.setImeOptions(EditorInfo.IME_ACTION_DONE);
        txtComments.setRawInputType(InputType.TYPE_CLASS_TEXT);

        takePicture = (Button) rootView.findViewById(R.id.btn_img_ocorrencia);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick", "" + v.getId());
                markerImage = null;

                if (mCameraIntentHelper == null) {
                    setupCameraIntentHelper();
                }

                mCameraIntentHelper.startCameraIntent();
            }
        });
        boolean enablePhoto = checkPermissionPhoto();
        takePicture.setVisibility(enablePhoto ? View.VISIBLE : View.INVISIBLE);

        builder.setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(NoticeDialogFragment.this);

                    }
                });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(NoticeDialogFragment.this);
            }
        });
        return builder.create();
    }

    /**
     * Receiving speech input
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult", String.format("%s, %s", requestCode, resultCode));

        super.onActivityResult(requestCode, resultCode, data);
        mCameraIntentHelper.onActivityResult(requestCode, resultCode, data);

        /*
        switch (requestCode) {
            case TAKE_PICTURE: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    markerImage = stream.toByteArray();
                    break;
                }
            }
        }
        */
    }

    /**
     * Return the Comments.
     * @return
     */
    public String getComments() {
        return txtComments.getText().toString();
    }

    /**
     * Return the Comments.
     * @return
     */
    public byte[] getMarkerImage() {
        return markerImage;
    }

    private void setupCameraIntentHelper() {
        mCameraIntentHelper = new CameraIntentHelper( getActivity(), new CameraIntentHelperCallback() {

            @Override
            public void onPhotoUriFound(Date dateCameraIntentStarted, Uri photoUri, int rotateXDegrees) {
                Bitmap photo = BitmapHelper.readBitmap( getActivity(), photoUri);
                if (photo != null) {

                    photo = BitmapHelper.shrinkBitmap(photo, 600, rotateXDegrees);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    markerImage = stream.toByteArray();

                    BitmapHelper.clearBitmap(photo);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, getActivity());
            }

            @Override
            public void onSdCardNotMounted() {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_sd_card_not_mounted), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCanceled() {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.warning_camera_intent_canceled), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCouldNotTakePhoto() {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_could_not_take_photo), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPhotoUriNotFound() {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.activity_camera_intent_photo_uri_not_found), Toast.LENGTH_LONG).show();
            }

            @Override
            public void logException(Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_sth_went_wrong), Toast.LENGTH_LONG).show();
                Log.d(getClass().getName(), e.getMessage());
            }
        });
    }

    private boolean checkPermissionPhoto() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(
                        getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCameraIntentHelper = null;

        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map_detail));

        if (fragment != null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    takePicture.setVisibility(View.VISIBLE);
                } else {
                    takePicture.setVisibility(View.INVISIBLE);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }
}
