package vivacity.com.br.testarea;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mac on 27/12/17.
 * @author Gabriel Dos santos Magalhães
 */

public class WebClient {

    protected String get(String url) {

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        final String[] json = new String[1];

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try (Response response = client.newCall(request).execute()) {
                    json[0] = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        return json[0];
    }
}
