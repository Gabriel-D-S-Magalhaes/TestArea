package vivacity.com.br.testarea;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.model.Status;
import ai.api.ui.AIDialog;

public class AIDialogSampleActivity extends AppCompatActivity implements AIDialog.AIDialogListener {

    private static final String TAG = AIDialogSampleActivity.class.getSimpleName();

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
        Log.i(TAG, "Speech: " + speech);

        // Get parameters
        final HashMap<String, JsonElement> params = resultado.getParameters();

        if (params != null && !params.isEmpty()) {

            Log.i(TAG, "Parameters: ");

            for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {

                Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
            }
        }

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
    private void handleStatus(final Status status) {

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

    public void buttonListenOnClick(View view) {
        aiDialog.showAndListen();
    }
}
