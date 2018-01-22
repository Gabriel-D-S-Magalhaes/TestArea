package vivacity.com.br.testarea;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIConfiguration.RecognitionEngine;
import ai.api.AIDataService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.model.Status;

public class AITextSampleActivity extends AppCompatActivity {

    private static final String TAG = AITextSampleActivity.class.getSimpleName();

    private EditText query_editText;
    private TextView resultTextView;

    private AIDataService aiDataService;
    private AIRequest aiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aitext_sample);

        //Keep screen awake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        query_editText = (EditText) findViewById(R.id.query_editText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        // Set up the configuration. Replace CLIENT_ACCESS_TOKEN with your client access token.
        final AIConfiguration aiConfiguration = new AIConfiguration(
                getString(R.string.client_access_token),
                ai.api.AIConfiguration.SupportedLanguages.PortugueseBrazil,
                RecognitionEngine.System);

        aiDataService = new AIDataService(aiConfiguration);
        aiRequest = new AIRequest();
    }

    public void onClickedView(View view) {

        switch (view.getId()) {

            case R.id.send_btn:
                sendRequest();
                break;
        }
    }

    private void sendRequest() {
        final String queryString = query_editText.getText().toString();

        // If the string is null or 0-length.
        if (TextUtils.isEmpty(queryString)) {
            onError(new AIError("Query should not empty"));
            return;
        }

        aiRequest.setQuery(queryString);

        new AsyncTask<AIRequest, Void, AIResponse>() {

            private AIError error;

            /**
             * Override this method to perform a computation on a background thread. The
             * specified parameters are the parameters passed to {@link #execute}
             * by the caller of this task.
             * <p>
             * This method can call {@link #publishProgress} to publish updates
             * on the UI thread.
             *
             * @param aiRequests The parameters of the task.
             * @return A result, defined by the subclass of this task.
             * @see #onPreExecute()
             * @see #onPostExecute
             * @see #publishProgress
             */
            @Override
            protected AIResponse doInBackground(AIRequest... aiRequests) {

                AIRequest aiRequest = aiRequests[0];

                try {

                    AIResponse aiResponse = aiDataService.request(aiRequest);
                    return aiResponse;
                } catch (AIServiceException e) {
                    error = new AIError(e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(AIResponse aiResponse) {
                super.onPostExecute(aiResponse);

                if (aiResponse != null) {
                    // process aiResponse here
                    onResult(aiResponse);
                } else {
                    onError(error);
                }
            }
        }.execute(aiRequest);
    }

    private void onResult(final AIResponse response) {
        // Use the response object to get all the results

        // Get and handle the status
        final Status status = response.getStatus();
        handleStatus(status);

        // Result
        final Result result = response.getResult();

        // Get resolved query
        Log.i(TAG, "Resolved query: " + result.getResolvedQuery());

        // Get action
        Log.i(TAG, "Action: " + result.getAction());

        // Get parameters
        final HashMap<String, JsonElement> parameters = result.getParameters();
        if (parameters != null && !parameters.isEmpty()) {

            Log.i(TAG, "Parameters: ");

            Map.Entry<String, JsonElement> map;

            for (final Map.Entry<String, JsonElement> entry : parameters.entrySet()) {
                Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
            }
        }

        // Get speech
        String speech = result.getFulfillment().getSpeech();
        //resultTextView.setText("VitaconAgent say: " + speech);
        Log.i(TAG, "Speech: " + speech);

        resultTextView.setText(new GsonBuilder().setPrettyPrinting().create().toJson(response));
    }

    private void onError(final AIError error) {
        Log.e(TAG, error.getMessage());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultTextView.setText(error.getMessage());
            }
        });
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
}
