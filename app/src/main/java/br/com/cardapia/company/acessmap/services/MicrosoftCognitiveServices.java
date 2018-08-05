package br.com.cardapia.company.acessmap.services;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tvtios-01 on 24/10/17.
 */
public class MicrosoftCognitiveServices implements Serializable {


    protected static final String ENCODING = "UTF-8";

    /**
     * Obtem a URL da API
     * @return
     */
    public static String getApiUrl() {
        String visualFeatures = "?visualFeatures=Categories,Tags,Description,Faces,ImageType,Color,Adult";
        String details = "&details=Celebrities,Landmarks";
        String url = "https://brazilsouth.api.cognitive.microsoft.com/vision/v1.0/analyze" + visualFeatures + details;
        return url;
    }

    public static Map<String, String> getApiTranslateHeaders() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Ocp-Apim-Subscription-Key","");
        return map;
    }


    public static Map<String, String> getApiHeaders() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Ocp-Apim-Subscription-Key","");
        map.put("Content-Type", "application/octet-stream");
        return map;
    }

    /**
     * Obtem a URL da API
     * @return
     */
    public static String getApiTranslatorUrl(final String textToTranslate) {
        String langTo = null;
        String langFrom = null;
        String text = null;
        try {
            langTo = "to=" + URLEncoder.encode("pt-BR",ENCODING);
            langFrom = "from=" + URLEncoder.encode("en",ENCODING);
            text = "text=" + URLEncoder.encode(textToTranslate,ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "https://api.microsofttranslator.com/V2/Http.svc/Translate?" + langFrom + "&" + langTo + "&"+ text;
        return url;
    }

}
