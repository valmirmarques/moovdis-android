package br.com.cardapia.company.acessmap.services;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tvtios-01 on 24/10/17.
 */

public class ServiceInvoker implements Serializable {

    /**
     * A method to download json data from url
     */
    public static String downloadUrl(String strUrl, Map<String, String> headers, String method, byte[] dataToPost) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);

            // ADD HEADERS
            if (headers != null) {
                Set<String> headersKey = headers.keySet();
                for (String s : headersKey) {
                    urlConnection.setRequestProperty(s, headers.get(s));
                }
            }
            if (dataToPost != null) {
                urlConnection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                outputStream.write(dataToPost);
                outputStream.flush();
            }
            // Connecting to url
            urlConnection.connect();

            if (200 != urlConnection.getResponseCode()) {
                throw new Exception("Failed to Invoke Service:" + urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());
            }

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            if (iStream != null) {
                iStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }


    /**
     * A method to download json data from url
     */
    public static byte[] downloadUrltoByteArray(String strUrl, Map<String, String> headers, String method, byte[] dataToPost) throws IOException {
        byte data[] = null;
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);

            // ADD HEADERS
            if (headers != null) {
                Set<String> headersKey = headers.keySet();
                for (String s : headersKey) {
                    urlConnection.setRequestProperty(s, headers.get(s));
                }
            }
            if (dataToPost != null) {
                //urlConnection.setRequestProperty("Content-Length", String.valueOf(dataToPost.length));
                urlConnection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                outputStream.write(dataToPost);
                outputStream.flush();
            }
            // Connecting to url
            urlConnection.connect();

            if (200 != urlConnection.getResponseCode()) {
                throw new Exception("Failed to Invoke Service:" + urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());
            }

            // Reading data from url
            iStream = urlConnection.getInputStream();
            data = org.apache.commons.io.IOUtils.toByteArray( iStream );

            Log.d("downloadUrl", data.toString());

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            if (iStream != null) {
                iStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }
}
