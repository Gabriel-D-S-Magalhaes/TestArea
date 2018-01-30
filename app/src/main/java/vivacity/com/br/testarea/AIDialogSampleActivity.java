package vivacity.com.br.testarea;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.model.Status;
import ai.api.ui.AIDialog;

public class AIDialogSampleActivity extends AppCompatActivity implements AIDialog.AIDialogListener {

    private static final String TAG = AIDialogSampleActivity.class.getSimpleName();

    private TextToSpeech textToSpeech;
    private static final int MAX_SPEECH_INPUT_LENGTH = TextToSpeech.getMaxSpeechInputLength();

    private TextView resultTextView;

    private AIDialog aiDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidialog_sample);

        //Keep screen awake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        resultTextView = (TextView) findViewById(R.id.resultTextView);

        final AIConfiguration aiConfiguration = new AIConfiguration(
                getString(R.string.client_access_token),
                AIConfiguration.SupportedLanguages.PortugueseBrazil,
                AIConfiguration.RecognitionEngine.System);

        aiDialog = new AIDialog(this, aiConfiguration);
        aiDialog.setResultsListener(this);
    }

    @Override
    public void onResult(AIResponse result) {
        // Use the result object to get all the results

        // Get and handle the status
        final Status status = result.getStatus();
        handleStatus(status);

        // Get resolved query
        final Result resultado = result.getResult();
        Log.i(TAG, "Resolved query: " + resultado.getResolvedQuery());

        // Get action
        Log.i(TAG, "Action: " + resultado.getAction());

        // Get speech
        final String speech = resultado.getFulfillment().getSpeech();
        speak(speech);
        Log.i(TAG, "Speech: " + speech);

        // Get parameters
        final HashMap<String, JsonElement> params = resultado.getParameters();
        handleParameters(params);

        resultTextView.setTextColor(Color.BLACK);
        resultTextView.setText(new GsonBuilder().setPrettyPrinting().create().toJson(result));
    }

    @Override
    public void onError(AIError error) {
        Log.e(TAG, error.getMessage());
        resultTextView.setTextColor(Color.RED);
        resultTextView.setText(error.getMessage());
    }

    @Override
    public void onCancelled() {
        Log.i(TAG, "onCancelled");
    }

    /**
     * Método responsável por lidar com o status e códigos de erros retornados pelo Dialogflow.
     *
     * @param status objeto {@link Status}
     *               To know more: https://dialogflow.com/docs/reference/agent/#status_object
     */
    private void handleStatus(@NonNull final Status status) {

        Log.i(TAG, "Status code: " + status.getCode());
        Log.i(TAG, "Status type: " + status.getErrorType());
        Log.i(TAG, "Status errorId: " + status.getErrorID());
        Log.i(TAG, "Status errorDetails: " + status.getErrorDetails());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (status.getCode()) {
                    case 400:

                        Toast.makeText(getApplicationContext(), "Erro 400!",
                                Toast.LENGTH_LONG).show();
                        break;
                    case 401:

                        Toast.makeText(getApplicationContext(),
                                "Erro 401! Credenciais faltantes ou erradas.",
                                Toast.LENGTH_LONG).show();
                        break;
                    case 404:

                        Toast.makeText(getApplicationContext(), "Erro 404!",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 405:

                        Toast.makeText(getApplicationContext(), "Erro 405!",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 406:

                        Toast.makeText(getApplicationContext(), "Erro 406!",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 409:

                        Toast.makeText(getApplicationContext(),
                                "Erro 409!", Toast.LENGTH_SHORT).show();
                        break;
                    case 429:

                        Toast.makeText(getApplicationContext(),
                                "Muitos requests foram enviados no curto período de tempo!",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    private void handleParameters(final HashMap<String, JsonElement> parameters) {

        // Verifica se o argumento passado para o método não é null
        if (parameters != null && !parameters.isEmpty()) {

            // pega o nome do app a ser iniciado
            String app = parameters.get("app").getAsString();

            // Verifica qual app deve ser iniciado
            if (Objects.equals(app, "Music")) {

                //usa o player do robô
                executarMusicas();
            } else if (Objects.equals(app, "Movie")) {

                //projetar usando a activity ProjetarVideoActivity
                projetarVideos(1);
            }

            // Show info
            Log.i(TAG, "Parameters: ");
            /*O método entrySet() da classe Map nos devolve um Set<Map.Entry<K, V>>, onde K e V são
             os tipos da chave e do valor respectivamente do nosso mapa. Esse retorno nada mais do
             que uma entrada contendo a chave e seu respectivo valor.*/
            for (final Map.Entry<String, JsonElement> entry : parameters.entrySet()) {
                Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
            }
        }
    }

    public void buttonListenOnClick(View view) {
        aiDialog.showAndListen();
    }

    /**
     * Método que faz o robô falar. Usando o Mecanismo de Convesão de Texto em Voz do Google.
     *
     * @param text = Texto para ser convertido em voz.
     */
    private void speak(@NonNull final String text) {

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {

                    if (textToSpeech.isLanguageAvailable(new Locale("pt",
                            "BR")) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {

                        // The specified language as represented by the Locale is available and supported.
                        textToSpeech.setLanguage(new Locale("pt", "BR"));

                        if (!TextUtils.isEmpty(text)) {

                            if (text.length() <= MAX_SPEECH_INPUT_LENGTH) {

                                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                            } else {

                                Toast.makeText(getApplicationContext(),
                                        "Tamanho máx. do texto = " + MAX_SPEECH_INPUT_LENGTH,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(getApplicationContext(),
                                    "Sem texto a ser convertido.", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "Idioma pt-BR não disponível.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(getApplicationContext(),
                            "Your device don't support Speech output",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Método responsável por reproduzir as músicas através do player padrão do robô.
     */
    private void executarMusicas() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(Environment.getExternalStorageDirectory().getPath() +
                "/Documents");// Talvez seja necessário trocar esse caminho

        intent.setDataAndType(Uri.fromFile(file), "audio/*");//Pegue qualquer arquivo de audio do caminho .../Documents
        startActivity(intent);// nome do método auto explicativo.
    }

    /**
     * Método responsável pela reprodução e projeção dos vídeos.
     *
     * @param app representa qual aplicação será usada para reproduzir as músicas.
     *            1 - Aplicativo atual: Usa a {@link ProjetarVideoActivity}.
     *            2 - Aplicativo Movie: Usa o player de vídeo do Sanbot.
     */
    private void projetarVideos(int app) {

        switch (app) {
            case 1:

                startActivity(new Intent(this, ProjetarVideoActivity.class));
                break;
            case 2:

                Intent intent = new Intent(Intent.ACTION_VIEW);
                File file = new File(Environment.getExternalStorageDirectory().getPath() +
                        "/Documents");// Talvez seja necessário trocar esse caminho

                intent.setDataAndType(Uri.fromFile(file), "video/*");//Pegue qualquer arquivo de video do caminho .../Documents
                startActivity(intent);// nome do método auto explicativo.
                break;
        }
    }

    @Override
    protected void onResume() {
        if (aiDialog != null) {
            aiDialog.resume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (aiDialog != null) {
            aiDialog.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            Log.i(TAG, "Método shutdown chamado no método onDestroy.");
        }
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
}
