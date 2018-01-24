package vivacity.com.br.testarea;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.AudioFormat;
import android.text.TextUtils;
import android.widget.TextView;

public class SpeechActivity extends AppCompatActivity {

    private TextView mText;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;

    private SpeechService mSpeechService;
    private VoiceRecorder mVoiceRecorder;
    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {
        /**
         * Called when the recorder starts hearing voice.
         */
        @Override
        public void onVoiceStart() {
            super.onVoiceStart();
            if (mSpeechService != null) {

                mSpeechService.startRecognizing(mVoiceRecorder.getSampleRate());
            }
        }

        /**
         * Called when the recorder is hearing voice.
         *
         * @param data The audio data in {@link AudioFormat#ENCODING_PCM_16BIT}.
         * @param size The size of the actual data in {@code data}.
         */
        @Override
        public void onVoice(byte[] data, int size) {
            super.onVoice(data, size);
            if (mSpeechService != null) {
                mSpeechService.recognize(data, size);
            }
        }

        /**
         * Called when the recorder stops hearing voice.
         */
        @Override
        public void onVoiceEnd() {
            super.onVoiceEnd();
            if (mSpeechService != null) {
                mSpeechService.finishRecognizing();
            }
        }
    };


    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSpeechService = SpeechService.from(service);
            mSpeechService.addListener(mSpeechServiceListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSpeechService = null;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        mText = (TextView) findViewById(R.id.mText);
    }

    @Override
    protected void onStart() {
        super.onStart();


        // Prepare Cloud Speech API
        bindService(new Intent(this, SpeechService.class), mServiceConnection, BIND_AUTO_CREATE);

        // Start listening to voices
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecorder();
        }
    }

    @Override
    protected void onStop() {
        // Stop listening to voice
        stopVoiceRecorder();

        // Stop Cloud Speech API
        mSpeechService.removeListener(mSpeechServiceListener);
        unbindService(mServiceConnection);
        mSpeechService = null;

        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {

            if (permissions.length == 1 && grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startVoiceRecorder();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
        }
        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();
    }

    private void stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }

    private final SpeechService.Listener mSpeechServiceListener = new SpeechService.Listener() {

        @Override
        public void onSpeechRecognized(final String text, final boolean isFinal) {

            if (isFinal) {
                mVoiceRecorder.dismiss();
            }

            if (mText != null && !TextUtils.isEmpty(text)) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (isFinal) {
                            mText.setText(text + "isFinal");
                        } else {
                            mText.setText(text);
                        }
                    }
                });
            }
        }
    };
}
