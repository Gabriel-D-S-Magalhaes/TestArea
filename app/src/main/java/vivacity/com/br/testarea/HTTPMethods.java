package vivacity.com.br.testarea;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mac on 27/12/17.
 *
 * @author Gabriel Dos Santos Magalhães
 */

public class HTTPMethods {

    private final String TOKEN = "YOUR-TOKEN-HERE";
    final static public int SEARCH_CITY_NAME = 1;
    final static public int SEARCH_CITY_STATE = 2;
    final static public int SEARCH_CITY_NAME_STATE = 3;
    final static public int SAO_PAULO_ID = 3477;

    /**
     * Previsão 15 dias por ID da cidade.
     *
     * @param city_id ID da cidade (e. g. 3477 - São Paulo)
     * @return String = json representando a resposta do servidor.
     */
    public String forecast15(int city_id) throws IOException {
        String url = "http://apiadvisor.climatempo.com.br/api/v1/forecast/locale/" + city_id + "/days/15?token=" + TOKEN;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * Previsão 72 horas (3 dias) por ID da cidade.
     *
     * @param city_id ID da cidade (e.g 3477 - São Paulo)
     * @return String = json representando a resposta do servidor.
     */
    public String forecast3(int city_id) throws IOException {
        String url = "http://apiadvisor.climatempo.com.br/api/v1/forecast/locale/" + city_id + "/hours/72?token=" + TOKEN;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * Busca dados de cidade por ID.
     * @param city_id = ID da cidade (e. g. 3477 - São Paulo).
     * @return String = = JSON representando a resposta do server
     * */
    public String localeID(int city_id) throws IOException {
        String url = "http://apiadvisor.climatempo.com.br/api/v1/locale/city/" + city_id + "?token=" + TOKEN;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * Busca dados de cidades por Nome e/ou Estado.
     * @param i = SEARCH_CITY_NAME, SEARCH_CITY_STATE, SEARCH_CITY_NAME_STATE.
     *          SEARCH_CITY_NAME = Buscar dados de cidade por Nome.
     *          SEARCH_CITY_STATE = Buscar dados de cidade por Estado
     *          SEARCH_CITY_NAME_STATE = Busca dados de cidades por Nome e Estado.
     * @param city_name  Nome da cidade (e. g. São Paulo).
     * @param city_state Estado da cidade (e. g. SP).
     * @return String = JSON representando a resposta do server
     * @see ://apiadvisor.climatempo.com.br/doc/index.html#api-Locale
     */
    public String localeNameState(int i, @Nullable String city_name, @Nullable String city_state) throws IOException {
        String url = null;

        switch (i) {
            case 1:
                if (!TextUtils.isEmpty(city_name))
                    url = "http://apiadvisor.climatempo.com.br/api/v1/locale/city?name=" + city_name + "&token=" + TOKEN;
                break;
            case 2:
                if (!TextUtils.isEmpty(city_state))
                    url = "http://apiadvisor.climatempo.com.br/api/v1/locale/city?state=" + city_state + "&token=" + TOKEN;
                break;
            case 3:
                if (!TextUtils.isEmpty(city_name) && !TextUtils.isEmpty(city_state))
                    url = "http://apiadvisor.climatempo.com.br/api/v1/locale/city?name=" + city_name + "&state=" + city_state + "&token=" + TOKEN;
                break;
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * Tempo no momento por ID da cidade.
     *
     * @param city_id = ID da cidade (e. g. 3477 - São Paulo)
     * @return String = JSON representando a resposta do server
     */
    public String weather(int city_id) throws IOException {
        String url = "http://apiadvisor.climatempo.com.br/api/v1/weather/locale/" + city_id + "/current?token=" + TOKEN;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
