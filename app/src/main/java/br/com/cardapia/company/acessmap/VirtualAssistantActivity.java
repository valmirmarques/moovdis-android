package br.com.cardapia.company.acessmap;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.appolica.flubber.Flubber;
import com.crashlytics.android.Crashlytics;
import com.dragankrstic.autotypetextview.AutoTypeTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ibm.watson.developer_cloud.conversation.v1.model.Context;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.RuntimeEntity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import br.com.cardapia.company.acessmap.bean.BotaoReportBean;
import br.com.cardapia.company.acessmap.bean.CadastroRetornoBean;
import br.com.cardapia.company.acessmap.bean.DescricaoFotoBean;
import br.com.cardapia.company.acessmap.bean.FacesBean;
import br.com.cardapia.company.acessmap.bean.LocationBean;
import br.com.cardapia.company.acessmap.bean.MarkerBean;
import br.com.cardapia.company.acessmap.bean.MarkerDistanceBean;
import br.com.cardapia.company.acessmap.bean.MessageRequestBean;
import br.com.cardapia.company.acessmap.bean.UsuarioBean;
import br.com.cardapia.company.acessmap.parser.MicrooftJsonParser;
import br.com.cardapia.company.acessmap.services.MicrosoftCognitiveServices;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.services.ServiceInvoker;
import br.com.cardapia.company.acessmap.util.LocationHelper;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class VirtualAssistantActivity extends AppCompatActivity implements MoovDisContants, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback {


    private static final int TAKE_PICTURE = 1;

    private static final String AUTHORITY =
            BuildConfig.APPLICATION_ID + ".provider";
    private static final String FILENAME = "CameraContentDemo";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String mCurrentPhotoPath;
    private TextToSpeech tts;
    private AutoTypeTextView textView;
    private Context lastContext = null;
    private MessageResponse lastMessage = null;

    private Location latLng;
    private String pendentAudio = StringUtils.EMPTY;
    private LocationHelper locationHelper;

    private SharedPreferences sharedPref;
    private String nomeUsuario = StringUtils.EMPTY;
    private String idUsuario = StringUtils.EMPTY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
        }

        setContentView(R.layout.activity_virtual_assistant);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.tool_bar));

        ImageButton btnMic = (ImageButton) findViewById(R.id.btn_mic_assistant);

        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        Flubber.with()
                .animation(Flubber.AnimationPreset.ROTATION)
                .interpolator(Flubber.Curve.BZR_EASE_IN)
                .repeatCount(5)
                .duration(1000)                              // Last for 1000 milliseconds(1 second)
                .createFor(btnMic)                           // Apply it to the view
                .start();                                    // Start it now

        textView = (AutoTypeTextView) findViewById(R.id.lbl_assist_saudacao);

        locationHelper = new LocationHelper(this);
        locationHelper.checkpermission();
        ButterKnife.bind(this);

        // check availability of play services
        if (locationHelper.checkPlayServices()) {
            // Building the GoogleApi client
            Log.d("buildGoogleApiClient", "Connecting");
            locationHelper.buildGoogleApiClient();
        }


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("Location", "Location Permission not Enabled");
            pendentAudio += getString(R.string.allow_location);
        } else {
            latLng = locationHelper.getLocation();
            if (latLng == null) {
                Log.i("Location", "Location Permission not avaiable");
            }
        }

        // Verifica se o usuário já salvou as preferências uma vez
        sharedPref = this.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);
        nomeUsuario = sharedPref.getString(NAME_USER, StringUtils.EMPTY);
        idUsuario = sharedPref.getString(ID_USUARIO, StringUtils.EMPTY);

        setVolumeSound();

        if (MoovDisUtil.isInternetConnected(this)) {
            // CHECK IF IS IN PORTUGUESE YEAT
            if (!Locale.getDefault().getDisplayLanguage().equalsIgnoreCase(LANG_PORTUGUESE)) {
                initTTS(new String[]{ getString(R.string.assistant_saudation), getString(R.string.sorry_language)}, null);
            } else {
                new WatsonConversationRequestTask().execute(StringUtils.EMPTY);
            }


        } else {
            initTTS(new String[]{getString(R.string.no_network)}, null);
        }


    }

    private void setVolumeSound() {
        try {
            AudioManager am =
                    (AudioManager) getSystemService(AUDIO_SERVICE);

            am.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    0);
            Log.d("setVolumeSound", "Volume ajustado com sucesso");

        } catch (Exception e) {
            Log.e("setVolumeSound", e.getMessage(), e );
        }
    }

    /**
     * Start TTS
     * @param speak speak
     * @param utteranceId utteranceId
     */
    protected void initTTS(final String[] speak, final String utteranceId) {

        //falando = true;

        if (StringUtils.isNotBlank(pendentAudio)) {
            speak[speak.length - 1] += String.format(". %s", pendentAudio);
            pendentAudio = StringUtils.EMPTY;
        }

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.d("Audio->onInit", "init");
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result != TextToSpeech.ERROR) {
                        Log.d("TTS", "Audio Inicializado com Sucesso");
                        for (String aSpeak : speak) {
                            setAutoTextTyping(aSpeak);
                            speak(aSpeak, TextToSpeech.QUEUE_ADD, utteranceId);
                        }
                    } else {
                        Log.e("TTS", "This Language is not supported");
                    }
                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                Log.d("TTS", "START: " + s);
            }

            @Override
            public void onDone(String s) {
                Log.d("TTS", "DONE: " + s);
                String[] arrS = s.split("@");

                if (arrS.length == 3 && arrS[1].equals(arrS[2])) {

                    final String currentWatsonAction = arrS[0];

                    // ME MOSTRE O QUE TEM AQUI
                    if (WATSON_ACTION_SHOW_ME_HERE.equalsIgnoreCase(currentWatsonAction)) {
                        startImageCapture();
                    }

                    // TEM UM PROBLEMA AQUI - MARCAR OCORRENCIA
                    if (WATSON_ACTION_REPORT_INACCESSIBILITY.equalsIgnoreCase(currentWatsonAction)) {
                        reportInacessibility();
                    }

                    if (WATSON_ACTION_OCURRENCIES_IN_REGION.equalsIgnoreCase(currentWatsonAction)) {
                        showOcurrenciesInRegion();
                    }
                }

            }

            @Override
            public void onError(String s) {
                Log.d("TTS", "ERROR: " + s);
            }
        });
    }


    /**
     *  speak
     * @param fala fala
     * @param queueType queueType
     * @param utteranceId utteranceId
     */
    private void speak(String fala, final int queueType, final String utteranceId) {
        //String[] text = fala.split("\\.");
        String[] text = { fala };
        for (int i = 0; i < text.length; i++) {
            final String id = (utteranceId == null ? UTTERANCE_ID : utteranceId) + "@" + i + "@" + (text.length - 1);

            HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, id);

            tts.speak(text[i], queueType, MoovDisUtil.mapStringToBundle(params), id);
            tts.playSilentUtterance(300, TextToSpeech.QUEUE_ADD, null);
        }
    }


    public void onPause() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }


    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Locale pt_BR = new Locale("pt", "BR");
        if (!Locale.getDefault().getDisplayLanguage().equalsIgnoreCase(LANG_PORTUGUESE)) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_country),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, pt_BR.getDisplayLanguage());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, pt_BR);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.touch_to_speak));
        //need to have a calling package for it to work
        if (!intent.hasExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE)) {
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, VirtualAssistantActivity.this.getPackageName());
        }
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    for (String s : result) {
                        Log.d("Text Result:", s);
                    }
                    if (MoovDisUtil.isInternetConnected(this)) {
                        new WatsonConversationRequestTask().execute(result.get(0));
                    }
                }
                break;
            }
            case TAKE_PICTURE: {
                if (resultCode == RESULT_OK) {
                    initTTS(new String[]{getString(R.string.please_wait_analizyng)}, null);

                    File f = new File(mCurrentPhotoPath);
                    byte[] photo = uriToByteArray(f);
                    callMicrosoftVisualAnalitics(photo);
                }
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "JPEG_" + FILENAME + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Inicia a captura da Imagem
     */
    private void startImageCapture() {
        new ImageCaptureTask().execute();
    }

    /**
     * Report an Inacesebility
     */
    private void reportInacessibility() {
        if (lastContext != null && lastMessage != null) {

            MarkerBean bean = new MarkerBean();
            bean.setDateCreated(new Date());
            bean.setLabel(getString(R.string.danger_obstacule));
            bean.setMarkerType(MARKER_BAD);
            bean.setMarkerIcon(BUTTON_NAME_NO_BLIND);
            bean.setComment(lastMessage.getInput().getText());
            LocationBean locationBean = new LocationBean();
            locationBean.setType(LOCATION_TYPE_POINT);
            locationBean.setCoordinates(new double[]{latLng.getLongitude(), latLng.getLatitude()});
            bean.setLocation(locationBean);
            UsuarioBean usuarioBean = new UsuarioBean();
            usuarioBean.set_id(sharedPref.getString(MoovDisContants.ID_USUARIO, StringUtils.EMPTY));
            bean.setUser(usuarioBean);
            // PRIMEIRO PEGA AS ENTIDADES
            List<RuntimeEntity> listEntities = lastMessage.getEntities();
            if (listEntities != null && !listEntities.isEmpty()) {

                for (RuntimeEntity entity : listEntities) {
                    if (WATSON_ENTITY_MARKER_TYPE.equalsIgnoreCase(entity.getEntity())) {
                        String entityValue = entity.getValue();

                        for (BotaoReportBean botaoGood : MapsActivity.BOTAO_GOOD) {
                            if (entityValue.equalsIgnoreCase(botaoGood.getButtonName())) {
                                bean.setLabel(botaoGood.getText());
                                bean.setMarkerType(MARKER_GOOD);
                                bean.setMarkerIcon(botaoGood.getButtonName());
                                break;
                            }
                        }

                        for (BotaoReportBean botaoBad : MapsActivity.BOTAO_BAD) {
                            if (entityValue.equalsIgnoreCase(botaoBad.getButtonName())) {
                                bean.setLabel(botaoBad.getText());
                                bean.setMarkerType(MARKER_BAD);
                                bean.setMarkerIcon(botaoBad.getButtonName());
                                break;
                            }
                        }

                        break;
                    }
                }

            }
            new MarkerAddRequestTask().execute(bean);
        }


    }

    /**
     * Show Ocorrencies in Region
     */
    private void showOcurrenciesInRegion() {
        new AlertsGetRequestTask().execute();
    }

    /**
     * Call Microsoft APi to Recognize Photo
     *
     * @param photo
     */
    protected void callMicrosoftVisualAnalitics(byte[] photo) {
        // Getting URL to the Google Directions API
        String url = MicrosoftCognitiveServices.getApiUrl();
        Log.d("recognizePhoto", url);
        String b64Image = this.getBase64String(photo);
        MicrosoftCognitiveServicesFetchUrlTask fetchUrl = new MicrosoftCognitiveServicesFetchUrlTask();
        fetchUrl.execute(url, b64Image);
    }

    /**
     * TTs Destroy
     */
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            Log.d("TAG", "TTS Destroyed");
        }
        super.onDestroy();
    }

    /**
     * Comprimi e retorna para Base64
     *
     * @param bitmap
     * @return
     */
    private String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    /**
     * Comprimi e retorna para Base64
     *
     * @param bitmap
     * @return
     */
    private String getBase64String(byte[] bitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bmp = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length, options);
        Bitmap scaled = Bitmap.createScaledBitmap(bmp, (bmp.getWidth() / 5), (bmp.getHeight() / 5), true);
        return getBase64String(scaled);
    }

    /**
     * retorna Byte array de uma URI
     *
     * @param f
     */
    private byte[] uriToByteArray(File f) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        byte[] buf = new byte[1024];
        int n;
        try {
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Start Assistance
     *
     * @param view
     */
    public void startAssistance(View view) {
        promptSpeechInput();
    }

    /**
     * Update Text Auto Type Assincronuous
     */
    private void setAutoTextTyping(final String s) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                textView.setTextAutoTyping(s);
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Once connected with google api, get the location
        latLng = locationHelper.getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        locationHelper.connectApiClient();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Connection failed:", " ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    private class ImageCaptureTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Intent cameraIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
            } else {
                cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            }
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                return null;
            }

            Uri outputUri = FileProvider.getUriForFile(VirtualAssistantActivity.this, AUTHORITY, photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(cameraIntent, TAKE_PICTURE);
            return null;
        }
    }

    // Fetches data from url passed
    private class MicrosoftCognitiveServicesFetchUrlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                // Fetching the data from web service
                String b64Image = url[1];
                byte[] img = org.apache.commons.codec.binary.Base64.decodeBase64(b64Image.getBytes());
                data = ServiceInvoker.downloadUrl(url[0], MicrosoftCognitiveServices.getApiHeaders(), "POST", img);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (StringUtils.isBlank(result)) {
                initTTS(new String[]{getString(R.string.sorry_analizyng)}, null);
            } else {
                ParserTask parserTask = new ParserTask();
                parserTask.execute(result);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////// LOCATION SERVICES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * A class to parse the Microsoft Image in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, DescricaoFotoBean> {

        // Parsing the data in non-ui thread
        @Override
        protected DescricaoFotoBean doInBackground(String... jsonData) {

            JSONObject jObject;
            DescricaoFotoBean imagemDescription = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                MicrooftJsonParser parser = new MicrooftJsonParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                imagemDescription = parser.parse(jObject);
                Log.d("ParserTask", "Executing Image Description");
                Log.d("ParserTask", imagemDescription.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return imagemDescription;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(DescricaoFotoBean result) {
            // Se nao encontrou a rota
            if (result == null) {
                initTTS(new String[]{getString(R.string.sorry_analizyng)}, null);
            } else {

                Set<String> captionResults = result.getCaptions().keySet();
                String adulto = "A imagem " + (result.getAdult().isAdultContent() ? "" : "não ") + "tem conteúdo adulto, " + (result.getAdult().isRacyContent() ? "mas contém" : " não contém ") + " conteúdo picante";

                StringBuilder rostos = new StringBuilder(StringUtils.EMPTY);
                int i = 0;
                for (FacesBean faces : result.getFaces()) {
                    if (i == 0) {
                        rostos.append("Vejo um rosto ");
                    } else {
                        rostos.append("Mais um rosto ");
                    }
                    String genero = (faces.getGender().equalsIgnoreCase("Male") ? "Masculino" : "Feminino");
                    if (faces.getAge() < 12) {
                        genero = "Infantil";
                    }
                    rostos.append(genero).append(" de aproximadamente ").append(faces.getAge()).append(" anos.");
                    i++;
                }

                for (String s : captionResults) {
                    Double confidence = result.getCaptions().get(s);
                    FetchUrlTranslator fetchUrlTranslator = new FetchUrlTranslator();
                    fetchUrlTranslator.execute(MicrosoftCognitiveServices.getApiTranslatorUrl(s), String.valueOf(confidence), adulto, rostos.toString());
                }


            }
        }

    }

    // Fetches data from url passed
    private class FetchUrlTranslator extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... url) {
            String data = "";
            try {
                // Fetching the data from web service
                data = ServiceInvoker.downloadUrl(url[0], MicrosoftCognitiveServices.getApiTranslateHeaders(), "GET", null);
                Log.d("Background Task data", data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return new String[]{data, url[1], url[2], url[3]};
        }

        @Override
        protected void onPostExecute(String result[]) {
            super.onPostExecute(result);
            if (result == null) {
                initTTS(new String[]{getString(R.string.sorry_analizyng)}, null);
            } else {
                // Read XML
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                try {
                    ByteArrayInputStream bios = new ByteArrayInputStream(result[0].getBytes());
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(bios);
                    doc.getDocumentElement().normalize();
                    String traducao = doc.getDocumentElement().getTextContent();
                    String confidenceStr = result[1];
                    Double confidence = Double.parseDouble(confidenceStr) * 100;
                    Integer confidenceInt = confidence.intValue();
                    String frase1 = "Tenho " + confidenceInt + "% de certeza de que vejo ";

                    initTTS(new String[]{frase1, traducao, result[2], result[3]}, null);

                } catch (ParserConfigurationException | IOException | SAXException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Rest to Mark add General
     */
    private class WatsonConversationRequestTask extends AsyncTask<String, Void, MessageResponse> {

        @Override
        protected MessageResponse doInBackground(String... params) {
            try {
                final String url = MoovdisServices.getApiWatsonUrl();
                MessageRequestBean request = new MessageRequestBean();
                request.setText(params[0]);

                // SET CONTEXT
                if (lastContext == null) {
                    lastContext = new Context();
                    lastContext.put(TIMEZONE, AMERICA_SAO_PAULO);
                }
                lastContext.put(WATSON_ACTION, null);
                lastContext.put(NAME_USER, nomeUsuario);
                lastContext.put(ID_USUARIO, idUsuario);
                if (latLng != null) {
                    lastContext.put(LATITUDE, latLng.getLatitude());
                    lastContext.put(LONGITUDE, latLng.getLongitude());
                    lastContext.put(ALTITUDE, latLng.getAltitude());
                }
                //

                request.setContext(lastContext);
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                MessageResponse retorno = restTemplate.postForObject(url, request, MessageResponse.class);
                return retorno;
            } catch (Exception e) {
                Log.e("WatsonConversation", e.getMessage(), e);
            }
            return null;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void onPostExecute(MessageResponse retorno) {
            if (retorno != null) {
                lastContext = retorno.getContext();
                lastMessage = retorno;

                HashMap retornoMap = retorno;
                List<String> text = (List<String>) retornoMap.get(KEY_OUTPUT);
                String utteranceId = null;
                if (lastContext != null && lastContext.get(WATSON_ACTION) != null) {
                    utteranceId = (String) lastContext.get(WATSON_ACTION);
                }
                for (String s : text) {
                    Log.d("WatsonOutput", s);
                    initTTS(new String[]{s}, utteranceId);
                }


            } else {
                initTTS(new String[]{getString(R.string.sorry_watson)}, null);
            }

        }
    }


    /// SERVICES

    /**
     * Rest to Mark add General
     */
    private class MarkerAddRequestTask extends AsyncTask<MarkerBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(MarkerBean... params) {
            try {
                Log.d("MarkerAddRequestTask", Arrays.toString(params));
                MoovDisUtil.showProgress(VirtualAssistantActivity.this, "Salvando ocorrência");
                final String url = MoovdisServices.getApiPostMarkerAddUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                CadastroRetornoBean retorno = restTemplate.postForObject(url, params[0], CadastroRetornoBean.class);
                return retorno;
            } catch (Exception e) {
                Log.e("MarkerAddRequestTask", e.getMessage(), e);
                MoovDisUtil.showErrorMessage(VirtualAssistantActivity.this, getString(R.string.ops), e.getMessage());
            } finally {
                MoovDisUtil.hideProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(CadastroRetornoBean retorno) {
            Toast.makeText(VirtualAssistantActivity.this, getString(R.string.ocorrencia_sucesso), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Rest do add user
     */
    private class AlertsGetRequestTask extends AsyncTask<String, Void, List<MarkerDistanceBean>> {
        @Override
        protected List<MarkerDistanceBean> doInBackground(String... params) {
            try {
                Log.i("AlertsGetRequestTask", "Init");

                final String url = MoovdisServices.getApiGetMarkersUrl(String.valueOf(latLng.getLatitude()),
                        String.valueOf(latLng.getLongitude()));
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                MarkerDistanceBean[] retorno = restTemplate.getForObject(url, MarkerDistanceBean[].class);
                return Arrays.asList(retorno);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        // POPULATE THE MAPS
        @Override
        protected void onPostExecute(List<MarkerDistanceBean> retorno) {
            if (retorno == null || retorno.isEmpty()) {
                initTTS(new String[]{getString(R.string.info_not_found)}, null);
                return;
            }

            Collections.sort(retorno);
            int sizeList = retorno.size() < LIMIT_OCURRENCIES_TO_SHOW ? retorno.size() : LIMIT_OCURRENCIES_TO_SHOW;
            String[] textToSpeak = new String[sizeList];

            for (int i = 0; i < sizeList; i++) {
                MarkerDistanceBean marker = retorno.get(i);
                textToSpeak[i] = String.format("%s reportado à %s metros de distância ", marker.getLabel(), (int) marker.getDistance());
                if (StringUtils.isNotBlank(marker.getAddressDescription())) {
                    textToSpeak[i] += String.format("na %s", marker.getAddressDescription());
                }
                if (StringUtils.isNotBlank(marker.getComment())) {
                    textToSpeak[i] += String.format(". \"%s\"", marker.getComment());
                }
            }
            initTTS(textToSpeak, null);

        }
    }


}
