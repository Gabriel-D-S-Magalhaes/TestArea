package vivacity.com.br.testarea;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.beans.speech.Grammar;
import com.qihancloud.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.qihancloud.opensdk.function.unit.HardWareManager;
import com.qihancloud.opensdk.function.unit.SpeechManager;
import com.qihancloud.opensdk.function.unit.WheelMotionManager;
import com.qihancloud.opensdk.function.unit.interfaces.hardware.VoiceLocateListener;
import com.qihancloud.opensdk.function.unit.interfaces.speech.RecognizeListener;

import java.io.IOException;

public class MainActivity extends TopBaseActivity {

    private HTTPMethods httpMethods;

    private HardWareManager hardWareManager;
    private WheelMotionManager wheelMotionManager;
    private SpeechManager speechManager;

    private static final String TAG = "MainActivity";
    private static final int FULL_ANGLE = 360;

    private int angleToTurn;

    Listerning listerning = new Listerning();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Keep screen awake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        httpMethods = new HTTPMethods();

        hardWareManager = (HardWareManager) getUnitManager(FuncConstant.HARDWARE_MANAGER);
        wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);

        listerning.execute();
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
     * @param view Responsável por virar o Sanbot na direção da fonte de som.
     */
    public void wheelControl(View view) {
        heardSanbot();
    }

    public void searchSoundSource() {

        // Sound source localization event will be callback when robot has been awaken.
        hardWareManager.setOnHareWareListener(new VoiceLocateListener() {

            /**
             * Resultado da localizacão da fonte de som
             * @param i = Angle refers to the angle offset of sound source and straight
             *          ahead of robot by clockwise.
             */
            @Override
            public void voiceLocateResult(int i) {

                // Info para o user
                //Toast.makeText(MainActivity.this, "Localizando fonte de som... a " + i + "º", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Localizando fonte de som...");
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

                    turnSanbot(angleToTurn);
                    Log.i(TAG, "Ouviu Robot a " + angleToTurn + "º");
                    Toast.makeText(MainActivity.this, "Ouviu Robot a " + angleToTurn + "º", Toast.LENGTH_SHORT).show();
                    return true;
                }

                Log.i(TAG, "Sanbot understood: " + grammar.getText());
                return false;
            }

            @Override
            public void onRecognizeVolume(int i) {

            }
        });
    }

    public void turnSanbot(int angle) {

        // Se o ângulo é até 180
        if (angle >= 0 && angle <= 180) {

            //então viro normalmente (sentido horário), para á direita.
            wheelMotionManager.doRelativeAngleMotion(new RelativeAngleWheelMotion(
                    RelativeAngleWheelMotion.TURN_RIGHT, 10, angle));

        } else if (angle > 180 && angle <= 360) {

            // se o ângulo vai de 181 a 360 graus, então (para não ficar demorado
            // virando no sentido horário), faço: 360 - angle e viro á esquerda.
            wheelMotionManager.doRelativeAngleMotion(new RelativeAngleWheelMotion(
                    RelativeAngleWheelMotion.TURN_LEFT, 10,
                    FULL_ANGLE - angle));
        }

    }

    @Override
    protected void onMainServiceConnected() {

    }


    class Listerning extends AsyncTask<Void, Void, Void> {

        private static final String TAG = "Listerning";

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
            searchSoundSource();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i(TAG, "onPostExecute running");
            //turnSanbot(integer);
        }
    }
}