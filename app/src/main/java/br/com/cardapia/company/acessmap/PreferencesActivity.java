package br.com.cardapia.company.acessmap;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appnirman.vaidationutils.ValidationUtils;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.cardapia.company.acessmap.bean.CadastroRetornoBean;
import br.com.cardapia.company.acessmap.bean.UsuarioBean;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.BitmapHelper;
import br.com.cardapia.company.acessmap.util.CameraIntentHelper;
import br.com.cardapia.company.acessmap.util.CameraIntentHelperCallback;
import br.com.cardapia.company.acessmap.util.Mask;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import io.karim.MaterialTabs;


public class PreferencesActivity extends AppCompatActivity implements MoovDisContants {


    private static final int FIRST_PAGE = 1;
    private static final int SECOND_PAGE = 2;
    //private static final int TAKE_PICTURE = 1;
    private static final int MIN_AGE = -7;
    private static CameraIntentHelper mCameraIntentHelper;
    private static EditText TXT_NOME;
    private static EditText TXT_EMAIL;
    private static EditText TXT_DATA_NASC;
    private static Switch SWITCH_CADEIRA;
    private static Switch SWITCH_DEF_VISUAL;
    private static Switch SWITCH_OUTROS;
    private static Switch SWITCH_NO_DEF;
    private static EditText TXT_LARG_CADEIRA;
    private static MaterialEditText TXT_TIPO_OUTRA_DEF;
    private static ImageButton btnTackPic;
    private static CircleImageView ivThumbnailPhoto;
    private static boolean enablePhoto = true;

    private static String TAG = "PrefsActivity";
    private static SharedPreferences sharedPref;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Context mContext;
    private ValidationUtils validationUtils;
    private MaterialTabs tabSwipeableView;

    /**
     * POPULA DADOS DO USUARIO
     *
     * @param retorno
     */
    private static void populateUserDate(final UsuarioBean retorno) {
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(NAME_USER, retorno.getName());
        if (StringUtils.isNotBlank(retorno.get_id())) {
            editor.putString(ID_USUARIO, retorno.get_id());
        }

        TXT_NOME.setText(retorno.getName());
        TXT_EMAIL.setText(retorno.getEmail());
        SWITCH_CADEIRA.setChecked(retorno.isWeelchair());
        SWITCH_DEF_VISUAL.setChecked(retorno.isBlind());
        if (retorno.isWeelchair() || retorno.isBlind()) {
            SWITCH_NO_DEF.setChecked(false);
        } else {
            SWITCH_NO_DEF.setChecked(true);
        }
        TXT_LARG_CADEIRA.setText(String.valueOf(retorno.getWeelchairWidht()));
        TXT_DATA_NASC.setText(MoovDisUtil.formatShortDate(retorno.getBirthDate()));
        SWITCH_OUTROS.setChecked(retorno.isOthersDisabilities());
        TXT_TIPO_OUTRA_DEF.setText(retorno.getOthersDisabilitiesName());

        // SETA A IMAGEM DO PROFILE E DO MENU
        if (retorno.getImage() != null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(retorno.getImage(), 0, retorno.getImage().length);
            ivThumbnailPhoto.setImageBitmap(bMap);
            editor.putString(IMG_PROFILE, Base64.encodeToString(retorno.getImage(), Base64.DEFAULT));

        } else {
            ivThumbnailPhoto.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.profile_icon));
        }
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
        }

        setContentView(R.layout.activity_preferences);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.tool_bar));
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>" + actionBar.getTitle().toString() + " </font>"));
        Drawable backArrow = getResources().getDrawable(R.drawable.baseline_arrow_back_24);
        backArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(backArrow);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container_prefs);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabSwipeableView = (MaterialTabs) this.findViewById(R.id.tabsSwipeableView);
        tabSwipeableView.setViewPager(mViewPager);


        //final PagerTabStrip pageTab = findViewById(R.id.pager_header);
        //pageTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22.0f);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // DO Nothing
            }

            @Override
            public void onPageSelected(int position) {
                // DO Nothing
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Do Nothing
            }
        });

        // init the validator
        validationUtils = new ValidationUtils(mContext);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidData()) {

                    UsuarioBean user = new UsuarioBean();
                    user.set_id(sharedPref.getString(ID_USUARIO, null));
                    user.setName(TXT_NOME.getText().toString());
                    user.setEmail(TXT_EMAIL.getText().toString());
                    if (StringUtils.isBlank(user.get_id())) {
                        user.setDateCreated(new Timestamp(new Date().getTime()));
                    }
                    user.setBirthDate(MoovDisUtil.parseShortDate(TXT_DATA_NASC.getText().toString()));
                    user.setBlind(SWITCH_DEF_VISUAL.isChecked());
                    user.setWeelchair(SWITCH_CADEIRA.isChecked());
                    if (StringUtils.isNotBlank(TXT_LARG_CADEIRA.getText().toString())) {
                        user.setWeelchairWidht(Integer.parseInt(TXT_LARG_CADEIRA.getText().toString()));
                    } else {
                        user.setWeelchairWidht(0);
                    }
                    user.setOthersDisabilities(SWITCH_OUTROS.isChecked());
                    user.setOthersDisabilitiesName(TXT_TIPO_OUTRA_DEF.getText().toString());
                    // Profile Image
                    Bitmap bitmap = ((BitmapDrawable) ivThumbnailPhoto.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    byte[] image = stream.toByteArray();
                    user.setImage(image);
                    user.setPushId(FirebaseInstanceId.getInstance().getToken());
                    user.setLang(Locale.getDefault().getLanguage());

                    new UserAddRequestTask().execute(user);
                } else {
                    mViewPager.setCurrentItem(SECOND_PAGE);
                }
            }
        });


        // Verifica se o usuário já salvou as preferências uma vez
        sharedPref = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // SE JA EXISTE UM USUARIO CADASTRADO
        if (sharedPref.contains(ID_USUARIO)) {
            // OBTEM OS DADOS DO USUARIO NO BANCO DE DADOS
            new UserGetByIdRequestTask().execute(sharedPref.getString(ID_USUARIO, StringUtils.EMPTY));
        }

        // VERIFICA PERMISSAO PARA ACESSAR A CAMERA
        enablePhoto = checkPermissionPhoto();

        setupCameraIntentHelper();
    }

    private boolean checkPermissionPhoto() {

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(
                        this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
            }
            return false;
        } else {
            return true;
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
                    btnTackPic.setVisibility(View.VISIBLE);
                } else {
                    btnTackPic.setVisibility(View.INVISIBLE);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Show Date Picker
     *
     * @param v
     */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        /*
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) intent.getExtras().get("data");
            ivThumbnailPhoto.setImageBitmap(photo);
            ivThumbnailPhoto.setVisibility(View.VISIBLE);
        } else {
        }
        */
        mCameraIntentHelper.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * Valida os campos do Formulario
     *
     * @return true or false
     */
    private boolean isValidData() {

        if (!validationUtils.isValidEmail(TXT_EMAIL.getText().toString())) {
            TXT_EMAIL.setError(getString(R.string.campo_invalido, getString(R.string.email)));
            TXT_EMAIL.requestFocus();
            return false;
        }

        if (!validationUtils.isEmptyEditText(TXT_NOME.getText().toString())) {
            TXT_NOME.setError(getString(R.string.campo_obrigatorio, getString(R.string.name)));
            TXT_NOME.requestFocus();
            return false;
        }

        if (!validationUtils.isEmptyEditText(TXT_DATA_NASC.getText().toString())) {
            TXT_DATA_NASC.setError(getString(R.string.campo_obrigatorio, getString(R.string.data_nasc)));
            TXT_DATA_NASC.requestFocus();
            return false;
        }

        if (!MoovDisUtil.isDateValid(TXT_DATA_NASC.getText().toString())) {
            TXT_DATA_NASC.setError(getString(R.string.campo_invalido, getString(R.string.data_nasc)));
            TXT_DATA_NASC.requestFocus();
            return false;
        }

        Date data = MoovDisUtil.parseShortDate(TXT_DATA_NASC.getText().toString());

        Calendar minAge = Calendar.getInstance();
        minAge.add(Calendar.YEAR, MIN_AGE);

        if (data.after(minAge.getTime())) {
            TXT_DATA_NASC.setError(getString(R.string.campo_invalido, getString(R.string.data_nasc)));
            TXT_DATA_NASC.requestFocus();
            return false;
        }


        return true;
    }

    private void setupCameraIntentHelper() {
        mCameraIntentHelper = new CameraIntentHelper(this, new CameraIntentHelperCallback() {

            @Override
            public void onPhotoUriFound(Date dateCameraIntentStarted, Uri photoUri, int rotateXDegrees) {
                Bitmap photo = BitmapHelper.readBitmap(PreferencesActivity.this, photoUri);
                if (photo != null) {
                    photo = BitmapHelper.shrinkBitmap(photo, 300, rotateXDegrees);
                    ivThumbnailPhoto.setImageBitmap(photo);
                    ivThumbnailPhoto.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, PreferencesActivity.this);
            }

            @Override
            public void onSdCardNotMounted() {
                Toast.makeText(getApplicationContext(), getString(R.string.error_sd_card_not_mounted), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCanceled() {
                Toast.makeText(getApplicationContext(), getString(R.string.warning_camera_intent_canceled), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCouldNotTakePhoto() {
                Toast.makeText(getApplicationContext(), getString(R.string.error_could_not_take_photo), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPhotoUriNotFound() {
                Toast.makeText(getApplicationContext(), getString(R.string.activity_camera_intent_photo_uri_not_found), Toast.LENGTH_LONG).show();
            }

            @Override
            public void logException(Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_sth_went_wrong), Toast.LENGTH_LONG).show();
                Log.d(getClass().getName(), e.getMessage());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (mCameraIntentHelper != null) {
            mCameraIntentHelper.onSaveInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mCameraIntentHelper != null) {
            mCameraIntentHelper.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraIntentHelper = null;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            if (getArguments().getInt(ARG_SECTION_NUMBER) == FIRST_PAGE) {
                rootView = inflater.inflate(R.layout.fragment_main, container, false);

                SWITCH_DEF_VISUAL = (Switch) rootView.findViewById(R.id.switchDefVisual);
                final TextView txtDetVisual = (TextView) rootView.findViewById(R.id.txtDetVisual);

                SWITCH_CADEIRA = (Switch) rootView.findViewById(R.id.switchCadeira);
                final TextView txtDetCadeira = (TextView) rootView.findViewById(R.id.txtDetCadeira);

                SWITCH_OUTROS = (Switch) rootView.findViewById(R.id.switchOutros);
                TXT_TIPO_OUTRA_DEF = rootView.findViewById(R.id.txtInformeDecifiencia);

                SWITCH_NO_DEF = (Switch) rootView.findViewById(R.id.switchNoDef);
                final TextView txtNoDef = (TextView) rootView.findViewById(R.id.txtDetNoDef);

                // Campos a ocultar ou Exibir
                final TextView lblLarguraCadeira = (TextView) rootView.findViewById(R.id.lblLarguraCadeira);
                TXT_LARG_CADEIRA = (EditText) rootView.findViewById(R.id.txtLargCadeira);
                lblLarguraCadeira.setVisibility(View.GONE);
                TXT_LARG_CADEIRA.setVisibility(View.GONE);
                TXT_TIPO_OUTRA_DEF.setVisibility(View.GONE);


                SWITCH_CADEIRA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Toast.makeText(getActivity(), (getString(R.string.uso_cadeira) + " "
                                        + (isChecked ? getString(R.string.habilitado) :
                                        getString(R.string.desabilitado))),
                                Toast.LENGTH_SHORT).show();
                        if (isChecked) {
                            lblLarguraCadeira.setVisibility(View.VISIBLE);
                            TXT_LARG_CADEIRA.setVisibility(View.VISIBLE);
                            txtDetCadeira.setVisibility(View.VISIBLE);
                            SWITCH_NO_DEF.setChecked(false);
                            SWITCH_OUTROS.setChecked(false);
                        } else {
                            lblLarguraCadeira.setVisibility(View.GONE);
                            TXT_LARG_CADEIRA.setVisibility(View.GONE);
                            txtDetCadeira.setVisibility(View.GONE);

                            if (!SWITCH_DEF_VISUAL.isChecked() && !SWITCH_OUTROS.isChecked()) {
                                SWITCH_NO_DEF.setChecked(true);
                            }
                        }
                    }
                });

                TXT_LARG_CADEIRA.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            EditText txt = (EditText) view;
                            if (StringUtils.isBlank(txt.getText().toString())) {
                                txt.setText(String.valueOf(DEFAULT_WEELCHAIR_WIDTH));
                            }
                        }
                    }
                });

                SWITCH_DEF_VISUAL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Toast.makeText(getActivity(), (getString(R.string.sou_def_visual) + " "
                                        + (isChecked ? getString(R.string.habilitado) :
                                        getString(R.string.desabilitado))),
                                Toast.LENGTH_SHORT).show();
                        if (isChecked) {
                            txtDetVisual.setVisibility(View.VISIBLE);
                            SWITCH_NO_DEF.setChecked(false);


                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            //define o titulo
                            builder.setTitle(getString(R.string.acessibilidade));
                            //define a mensagem
                            builder.setMessage(getString(R.string.acessibilidade_ativar));
                            //define um botão como positivo
                            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                    startActivity(intent);
                                }
                            });
                            //define um botão como negativo.
                            builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                            //cria o AlertDialog
                            builder.create().show();


                        } else {
                            txtDetVisual.setVisibility(View.GONE);

                            if (!SWITCH_CADEIRA.isChecked() && !SWITCH_OUTROS.isChecked()) {
                                SWITCH_NO_DEF.setChecked(true);
                            }

                        }
                    }
                });

                SWITCH_NO_DEF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            txtNoDef.setVisibility(View.VISIBLE);
                            SWITCH_CADEIRA.setChecked(false);
                            SWITCH_DEF_VISUAL.setChecked(false);
                            SWITCH_OUTROS.setChecked(false);
                        } else {
                            txtNoDef.setVisibility(View.GONE);
                        }
                    }
                });

                SWITCH_OUTROS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Toast.makeText(getActivity(), (getString(R.string.outras_defs) + " "
                                        + (isChecked ? getString(R.string.habilitado) :
                                        getString(R.string.desabilitado))),
                                Toast.LENGTH_SHORT).show();
                        if (isChecked) {
                            SWITCH_NO_DEF.setChecked(false);
                            SWITCH_CADEIRA.setChecked(false);
                            TXT_TIPO_OUTRA_DEF.setVisibility(View.VISIBLE);
                            TXT_TIPO_OUTRA_DEF.requestFocus();
                        } else {
                            TXT_TIPO_OUTRA_DEF.setVisibility(View.GONE);
                            TXT_TIPO_OUTRA_DEF.setText(StringUtils.EMPTY);

                            if (!SWITCH_DEF_VISUAL.isChecked() && !SWITCH_CADEIRA.isChecked()) {
                                SWITCH_NO_DEF.setChecked(true);
                            }
                        }
                    }
                });


            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == SECOND_PAGE) {

                // SEGUNDA ABA
                rootView = inflater.inflate(R.layout.fragment_main_sobre, container, false);
                btnTackPic = (ImageButton) rootView.findViewById(R.id.btnImgProfile);

                ivThumbnailPhoto = (CircleImageView) rootView.findViewById(R.id.imgProfile);

                // Coloca validarores nas caixas de texto do cadastro do usuário
                TXT_NOME = (EditText) rootView.findViewById(R.id.txtNome);
                TXT_EMAIL = (EditText) rootView.findViewById(R.id.txtEmail);
                TXT_DATA_NASC = (EditText) rootView.findViewById(R.id.txtDataNasc);
                TXT_DATA_NASC.addTextChangedListener(Mask.insert(Mask.MASK_DATE, TXT_DATA_NASC));


                btnTackPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCameraIntentHelper != null) {
                            mCameraIntentHelper.startCameraIntent();
                        }
                    }

                });
                btnTackPic.setVisibility(enablePhoto ? View.VISIBLE : View.INVISIBLE);

                TXT_EMAIL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            new UserGetByIdEmailTask().execute(TXT_EMAIL.getText().toString());
                        }
                    }
                });

            }

            return rootView;
        }
    }

    /**
     * A {@link DatePickerFragment} to select the date
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String data = StringUtils.leftPad(String.valueOf(day), 2, '0')
                    + "/"
                    + StringUtils.leftPad(String.valueOf(month + 1), 2, '0')
                    + "/"
                    + year;
            ((EditText) getActivity().findViewById(R.id.txtDataNasc)).setText(data);
        }
    }

    /**
     * Rest do get user
     */
    private static class UserGetByIdEmailTask extends AsyncTask<String, Void, UsuarioBean> {
        @Override
        protected UsuarioBean doInBackground(String... params) {
            try {
                Log.d("UserGetByIdEmailTask", params.toString());
                Map<String, String> paramsQuery = new HashMap<>();
                paramsQuery.put("email", params[0]);
                final String url = MoovdisServices.getApiGetUserUrl(paramsQuery);
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                UsuarioBean[] retorno = restTemplate.getForObject(url, UsuarioBean[].class);
                List<UsuarioBean> retornoList = Arrays.asList(retorno);

                if (!retornoList.isEmpty()) {
                    return retornoList.get(0);
                } else {
                    return null;
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(UsuarioBean retorno) {
            if (retorno == null) {
                return;
            }

            populateUserDate(retorno);

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.lets_start);
                case 1:
                    return getString(R.string.about_you);
            }
            return null;
        }
    }

    /**
     * Rest do add user
     */
    private class UserAddRequestTask extends AsyncTask<UsuarioBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(UsuarioBean... params) {
            try {
                Log.d("UserAddRequestTask", params.toString());
                MoovDisUtil.showProgress(PreferencesActivity.this, getString(R.string.save_user));
                final String url = MoovdisServices.getApiAddUserUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                UsuarioBean usuario = params[0];
                CadastroRetornoBean retorno = restTemplate.postForObject(url, usuario, CadastroRetornoBean.class);
                if (retorno != null) {
                    retorno.setUsuario(usuario);
                }
                return retorno;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                MoovDisUtil.showErrorMessage(PreferencesActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }

            return null;
        }

        @Override
        protected void onPostExecute(CadastroRetornoBean retorno) {
            if (retorno == null) {
                return;
            }

            // PERSIT DE USER ID ON KEY
            SharedPreferences.Editor editor = sharedPref.edit();
            if (StringUtils.isNotBlank(retorno.get_id())) {
                editor.putString(ID_USUARIO, retorno.get_id());
            }

            if (retorno.getError().getCode() == 0) {
                if (retorno.getUsuario().getImage() != null) {
                    editor.putString(IMG_PROFILE, Base64.encodeToString(retorno.getUsuario().getImage(), Base64.DEFAULT));
                }
                editor.putString(NAME_USER, retorno.getUsuario().getName());
                editor.putInt(WEELCHAIR_WIDTH, Integer.parseInt(TXT_LARG_CADEIRA.getText().toString()));
                editor.putBoolean(USE_WEELCHAIR, SWITCH_CADEIRA.isChecked());

            }
            editor.commit();

            // set the code on preferences.
            // Direct do Maps.
            Snackbar.make(TXT_NOME.getRootView(), getString(R.string.config_sucesso), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Intent intent = new Intent(PreferencesActivity.this, MapsActivity.class);
            startActivity(intent);
            finish();

        }

    }

    /**
     * Rest do get user
     */
    private class UserGetByIdRequestTask extends AsyncTask<String, Void, UsuarioBean> {
        @Override
        protected UsuarioBean doInBackground(String... params) {
            try {
                Log.d("UserGetByIdRequestTask", params.toString());
                MoovDisUtil.showProgress(PreferencesActivity.this, getString(R.string.recover_user));
                final String url = MoovdisServices.getApiGetUserUrl(null) + "/" + params[0];
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                UsuarioBean retorno = restTemplate.getForObject(url, UsuarioBean.class);
                return retorno;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                MoovDisUtil.showErrorMessage(PreferencesActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(UsuarioBean retorno) {
            if (retorno == null) {
                return;
            }

            populateUserDate(retorno);

            if (mViewPager.getCurrentItem() == SECOND_PAGE) {
                TXT_NOME.requestFocus();
            }

        }
    }

}