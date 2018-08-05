/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.cardapia.company.acessmap.firebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

import br.com.cardapia.company.acessmap.MapsActivity;
import br.com.cardapia.company.acessmap.PreferencesActivity;
import br.com.cardapia.company.acessmap.R;
import br.com.cardapia.company.acessmap.bean.CadastroRetornoBean;
import br.com.cardapia.company.acessmap.bean.UsuarioBean;
import br.com.cardapia.company.acessmap.services.MoovdisServices;
import br.com.cardapia.company.acessmap.services.RestTemplateFactory;
import br.com.cardapia.company.acessmap.util.MoovDisContants;
import br.com.cardapia.company.acessmap.util.MoovDisUtil;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private SharedPreferences sharedPref;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        if (sharedPref == null) {
            // Verifica se o usuário já salvou as preferências uma vez
            sharedPref = this.getSharedPreferences(MoovDisContants.PREFS_NAME, Context.MODE_PRIVATE);
        }

        String idUsuario = sharedPref.getString(MoovDisContants.ID_USUARIO, StringUtils.EMPTY);
        if (StringUtils.isNotBlank(idUsuario)) {
            UsuarioBean bean = new UsuarioBean();
            bean.setPushId(token);
            bean.set_id(idUsuario);
            bean.setLang(Locale.getDefault().getLanguage());
            new UserPushUpdateRequestTask().execute(bean);
        }
    }


    /**
     * Rest do add user
     */
    private class UserPushUpdateRequestTask extends AsyncTask<UsuarioBean, Void, CadastroRetornoBean> {
        @Override
        protected CadastroRetornoBean doInBackground(UsuarioBean... params) {
            try {
                Log.d(TAG, params.toString());
                final String url = MoovdisServices.getApiUserFrmUpdateUrl();
                RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
                UsuarioBean usuario = params[0];
                return restTemplate.postForObject(url, usuario, CadastroRetornoBean.class);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(CadastroRetornoBean retorno) {
            if (retorno == null) {
                return;
            }
            Log.d(TAG, "SUCCESS");

        }

    }


}
