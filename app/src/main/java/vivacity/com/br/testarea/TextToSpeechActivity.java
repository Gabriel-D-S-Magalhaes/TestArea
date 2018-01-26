package vivacity.com.br.testarea;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

public class TextToSpeechActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String TAG = TextToSpeechActivity.class.getSimpleName();
    private static final int TTS_REQUEST_CODE = 1;

    private TextToSpeech textToSpeech;
    private static int MAX_SPEECH_INPUT_LENGTH = TextToSpeech.getMaxSpeechInputLength(); // 4000

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        Log.i(TAG, "Limit of length of input string passed to speak and synthesizeToFile: "
                + MAX_SPEECH_INPUT_LENGTH);

    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     *
     * @param status {@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {


            if (textToSpeech.isLanguageAvailable(new Locale("pt", "BR")) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {

                // The specified language as represented by the Locale is available and supported.
                textToSpeech.setLanguage(new Locale("pt", "BR"));

                final String text = "Testando Mecanismo de conversão de texto em voz do Google.";

                if (!TextUtils.isEmpty(text) && text.length() <= TextToSpeech.getMaxSpeechInputLength()) {

                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    Toast.makeText(getApplicationContext(), "Erro!", Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            Log.e(TAG, "TextToSpeech engine initialization error.");
            Toast.makeText(getApplicationContext(), "Your device don't support Speech output",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickedView(View view) {
        switch (view.getId()) {
            case R.id.speech_btn:

                Intent checkIntent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
                startActivityForResult(checkIntent, TTS_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TTS_REQUEST_CODE:
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

                    Log.i(TAG, "Success, create the TTS instance!");
                    textToSpeech = new TextToSpeech(this, this);

                } else {
                    Log.i(TAG, "Missing data, install it.");
                    Intent installIntent = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            System.out.println("shutdown() invoked in method onDestroy()");
        }
        super.onDestroy();
    }
}
