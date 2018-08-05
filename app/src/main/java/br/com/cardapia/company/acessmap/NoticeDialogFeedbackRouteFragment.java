package br.com.cardapia.company.acessmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.ByteArrayOutputStream;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

/**
 * Created by tvtios-01 on 17/12/17.
 */

public class NoticeDialogFeedbackRouteFragment extends DialogFragment {


    /* The activity that creates an instance of this dialog fragment must
         * implement this interface in order to receive event callbacks.
         * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    private TextView txtComments;
    private RatingBar ratingBar;
    private Switch feedbackPublic;
    private static final float GOOD_RATING = 3.0f;

    private String idRota;

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
        // Build the dialog and set up the button click handlers

        if (BuildConfig.enableCrashlytics) {
            if (getActivity() != null) {
                Fabric.with(getActivity(), new Crashlytics());
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_feedback_rota, null);
        builder.setView(rootView);

        ButterKnife.bind(getActivity());

        txtComments = (TextView) rootView.findViewById(R.id.txtFeedbackRoute);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        feedbackPublic = (Switch) rootView.findViewById(R.id.switchPublico);

        txtComments.setImeOptions(EditorInfo.IME_ACTION_DONE);
        txtComments.setRawInputType(InputType.TYPE_CLASS_TEXT);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                LayerDrawable layers = (LayerDrawable) ratingBar.getProgressDrawable();
                if (rating >= GOOD_RATING) {
                    DrawableCompat.setTint(layers.getDrawable(0), 0x33000000); // The background tint
                    DrawableCompat.setTint(layers.getDrawable(1), 0x00000000); // The gap tint (Transparent in this case so the gap doesnt seem darker than the background)
                    DrawableCompat.setTint(layers.getDrawable(2), 0xff249c24); // The star tint
                } else {
                    DrawableCompat.setTint(layers.getDrawable(0), 0x33000000); // The background tint
                    DrawableCompat.setTint(layers.getDrawable(1), 0x00000000); // The gap tint (Transparent in this case so the gap doesnt seem darker than the background)
                    DrawableCompat.setTint(layers.getDrawable(2), Color.RED); // The star tint
                }

            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(NoticeDialogFeedbackRouteFragment.this);
                    }
                });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(NoticeDialogFeedbackRouteFragment.this);
                    }
                });
        return builder.create();
    }


    /**
     * Return the Comments.
     * @return
     */
    public String getComments() {
        return txtComments.getText().toString();
    }

    /**
     *
     * @return
     */
    public boolean isLiked() {
        return ratingBar.getRating() >= GOOD_RATING;
    }

    /**
     *
     * @return
     */
    public float getRating() {
        return ratingBar.getRating();
    }

    /**
     *
     * @return
     */
    public String getIdRota() {
        return idRota;
    }

    /**
     *
     * @param idRota
     */
    public void setIdRota(String idRota) {
        this.idRota = idRota;
    }

    /**
     * I
     * @return
     */
    public boolean isPublicFeedback() {
        return feedbackPublic.isChecked();
    }


}
