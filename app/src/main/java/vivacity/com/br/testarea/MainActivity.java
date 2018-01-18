package vivacity.com.br.testarea;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.beans.OperationResult;
import com.qihancloud.opensdk.function.beans.FaceRecognizeBean;
import com.qihancloud.opensdk.function.beans.SpeakOption;
import com.qihancloud.opensdk.function.beans.speech.Grammar;
import com.qihancloud.opensdk.function.beans.wheelmotion.DistanceWheelMotion;
import com.qihancloud.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.qihancloud.opensdk.function.unit.HardWareManager;
import com.qihancloud.opensdk.function.unit.MediaManager;
import com.qihancloud.opensdk.function.unit.ProjectorManager;
import com.qihancloud.opensdk.function.unit.SpeechManager;
import com.qihancloud.opensdk.function.unit.WheelMotionManager;
import com.qihancloud.opensdk.function.unit.interfaces.hardware.PIRListener;
import com.qihancloud.opensdk.function.unit.interfaces.hardware.VoiceLocateListener;
import com.qihancloud.opensdk.function.unit.interfaces.media.FaceRecognizeListener;
import com.qihancloud.opensdk.function.unit.interfaces.speech.RecognizeListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends TopBaseActivity {

    private HTTPMethods httpMethods;

    //Qihan SDK
    private HardWareManager hardWareManager;
    private WheelMotionManager wheelMotionManager;
    private SpeechManager speechManager;
    private MediaManager mediaManager;

    //Constants
    private static final String TAG = "MainActivity";
    private static final int FULL_ANGLE = 360;

    private boolean heardSanbot = false;
    private int angleToTurn;
    private boolean humanDetected = false;

    Listening listening = new Listening();

    //Info for user
    TextView tv_sound_source_info;
    TextView tv_tts_info;
    TextView tv_turn_sanbot_info;
    TextView tv_human_detected_info;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Keep screen awake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        httpMethods = new HTTPMethods();

        //Qihan SDK
        hardWareManager = (HardWareManager) getUnitManager(FuncConstant.HARDWARE_MANAGER);
        wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);
        mediaManager = (MediaManager) getUnitManager(FuncConstant.MEDIA_MANAGER);

        tv_sound_source_info = (TextView) findViewById(R.id.tv_sound_source_info);
        tv_tts_info = (TextView) findViewById(R.id.tv_tts_info);
        tv_turn_sanbot_info = (TextView) findViewById(R.id.tv_turn_sanbot_info);
        tv_human_detected_info = (TextView) findViewById(R.id.tv_human_detected_info);

        listening.execute();
    }

    @Override
    protected void onMainServiceConnected() {
    }

    public void search(View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    //System.out.println(httpMethods.forecast15(3477));
                    //System.out.println(httpMethods.forecast3(3477));
                    //System.out.println(httpMethods.localeID(3477));
                    //System.out.println(httpMethods.localeNameState(HTTPMethods.SEARCH_CITY_NAME, "São Paulo", null));
                    //System.out.println(httpMethods.localeNameState(HTTPMethods.SEARCH_CITY_STATE, null, "RJ"));
                    //System.out.println(httpMethods.localeNameState(HTTPMethods.SEARCH_CITY_NAME_STATE, "Belo Horizonte", "MG"));
                    WeatherBean weatherBean = new GsonBuilder().create().fromJson(httpMethods.weather(3477), WeatherBean.class);
                    System.out.println("Cidade: " + weatherBean.getName() + "\nTemperatura = " + weatherBean.getData().getTemperature() + "ºC");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * @param view
     */
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.btn_wheel_control:
                //heardSanbot();
                break;
            case R.id.executar_musicas_btn:
                executarMusicas();
                break;

            case R.id.executar_videos_btn:
                startActivity(new Intent(this, ProjetarVideoActivity.class));
                break;
        }
    }

    public void searchSoundSource() {

        // Sound source localization event will be callback when robot has been awaken.
        hardWareManager.setOnHareWareListener(new VoiceLocateListener() {

            /**
             * Localizacão da fonte de som
             * @param i = Angle refers to the angle offset of sound source and straight
             *          ahead of robot by clockwise.
             */
            @Override
            public void voiceLocateResult(int i) {

                // Info para o user
                Log.i(TAG, "Fonte de som localizada a " + i + "º");
                tv_sound_source_info.setText("\nFonte de som localizada a " + i + "º");
                angleToTurn = i;
            }
        });
    }

    public void heardSanbot() {

        speechManager.doWakeUp();

        speechManager.setOnSpeechListener(new RecognizeListener() {

            @Override
            public boolean onRecognizeResult(Grammar grammar) {

                if (!grammar.getText().isEmpty() && grammar.getText().contains("Robot")) {

                    heardSanbot = true;
                    turnSanbot(angleToTurn);

                    // Print info
                    Log.i(TAG, "Ouviu \"Robot\" a " + angleToTurn + "º");

                    Toast.makeText(MainActivity.this,
                            "Ouviu \"Robot\" a " + angleToTurn + "º", Toast.LENGTH_SHORT)
                            .show();

                    tv_tts_info.setText("Sanbot entendeu: " + grammar.getText());

                    return true;
                }

                //Print info
                Log.i(TAG, "Sanbot understood: " + grammar.getText());
                tv_tts_info.setText("Sanbot entendeu: " + grammar.getText());

                return false;
            }

            @Override
            public void onRecognizeVolume(int i) {

            }
        });
    }

    public void turnSanbot(int angle) {

        OperationResult operationResult = new OperationResult();

        tv_turn_sanbot_info.setText("Virando a " + angle + "º");

        // Se o ângulo é até 180
        if (angle >= 0 && angle <= 180) {

            //então viro normalmente (sentido horário), para á direita.
            operationResult = wheelMotionManager.doRelativeAngleMotion(new RelativeAngleWheelMotion(
                    RelativeAngleWheelMotion.TURN_RIGHT, 10, angle));

            tv_turn_sanbot_info.setText(
                    "Description: " + operationResult.getDescription()
                            + " Result: " + operationResult.getResult()
                            + " Error code: " + operationResult.getErrorCode());


        } else if (angle > 180 && angle <= 360) {

            // Se o ângulo vai de 181 a 360 graus, então (para não ficar demorado virando no sentido
            // horário), faço: 360 - angle e viro á esquerda.
            operationResult = wheelMotionManager.doRelativeAngleMotion(new RelativeAngleWheelMotion(
                    RelativeAngleWheelMotion.TURN_LEFT, 10,
                    FULL_ANGLE - angle));

            tv_turn_sanbot_info.setText(
                    "Description: " + operationResult.getDescription()
                            + " Result: " + operationResult.getResult()
                            + " Error code: " + operationResult.getErrorCode());

        }

        if (operationResult.getErrorCode() == 1) {

            new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {

                    tv_turn_sanbot_info.setText("Walking to human in " + millisUntilFinished / 1000);
                }

                public void onFinish() {

                    tv_turn_sanbot_info.setText("Walking...");
                    walkToHuman();
                }
            }.start();
        }
    }

    public void detectHuman() {

        hardWareManager.setOnHareWareListener(new PIRListener() {
            @Override
            public void onPIRCheckResult(boolean b, int i) {

                if (b) {
                    humanDetected = true;
                    switch (i) {
                        case 1:
                            Toast.makeText(MainActivity.this,
                                    "PIR detectou um corpo humano a frente.",
                                    Toast.LENGTH_SHORT).show();

                            Log.i(TAG, "PIR detectou um corpo humano a frente.");
                            break;
                        case 2:
                            Toast.makeText(MainActivity.this,
                                    "PIR detectou um corpo humano atrás.",
                                    Toast.LENGTH_SHORT).show();

                            Log.i(TAG, "PIR detectou um corpo humano atrás");
                            break;
                    }
                } else {
                    humanDetected = false;
                }

                tv_human_detected_info.setText("Humano detectado: " + humanDetected);
            }
        });
    }

    public void walkToHuman() {

        DistanceWheelMotion distanceWheelMotion = new DistanceWheelMotion(
                DistanceWheelMotion.ACTION_FORWARD_RUN, 5, 100);

        wheelMotionManager.doDistanceMotion(distanceWheelMotion);
        Toast.makeText(MainActivity.this, "Walking to human", Toast.LENGTH_SHORT)
                .show();
    }

    public void speak(String text) {

        SpeakOption speakOption = new SpeakOption();
        speakOption.setLanguageType(SpeakOption.LAG_ENGLISH_US);
        if (!text.isEmpty() || !text.equals("") || !text.equals(" "))
            speechManager.startSpeak(text, speakOption);
    }

    public void recognizeFamilyMember() {

        // Note: Internet connection is required for using this function
        FaceRecognizeListener faceRecognizeListener = new FaceRecognizeListener() {

            @Override
            public void recognizeResult(List<FaceRecognizeBean> list) {

                /**
                 * A sintaxe do for each é a seguinte:
                 * for (tipo variavel_do_tipo_do_seuArray : seuArray) {
                 *      //seu código
                 * }
                 */

                for (FaceRecognizeBean faceRecognizeBean : list) {

                    // Maybe useful for the developer
                    Log.i(TAG, new GsonBuilder().setPrettyPrinting().create().toJson(faceRecognizeBean));

                    // Note: No app "Family" o tamanho min do nome é de 2 caracteres
                    if (faceRecognizeBean.getUser().length() >= 2) {
                        speak("Hello" + faceRecognizeBean.getUser());
                    }
                }
            }
        };

        mediaManager.setMediaListener(faceRecognizeListener);
    }

    public void executarMusicas() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(Environment.getExternalStorageDirectory().getPath() +
                "/Documents");// Talvez seja necessário trocar esse caminho

        intent.setDataAndType(Uri.fromFile(file), "audio/*");//Pegue qualquer arquivo de audio do caminho .../Documents
        startActivity(intent);// nome do método auto explicativo.
    }

    class Listening extends AsyncTask<Void, Void, Void> {

        private static final String TAG = "Listening";

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param voids The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground running");
            //searchSoundSource();
            //detectHuman();
            //recognizeFamilyMember();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i(TAG, "onPostExecute running");
        }
    }
}