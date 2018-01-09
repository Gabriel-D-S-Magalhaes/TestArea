package vivacity.com.br.testarea;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.qihancloud.opensdk.function.unit.HardWareManager;
import com.qihancloud.opensdk.function.unit.SpeechManager;
import com.qihancloud.opensdk.function.unit.WheelMotionManager;
import com.qihancloud.opensdk.function.unit.interfaces.hardware.VoiceLocateListener;
import com.qihancloud.opensdk.function.unit.interfaces.speech.WakenListener;

import java.io.IOException;

public class MainActivity extends TopBaseActivity {

    private HTTPMethods httpMethods;

    private HardWareManager hardWareManager;
    private WheelMotionManager wheelMotionManager;
    private SpeechManager speechManager;

    private static final String TAG = "MainActivity";
    private static final int FULL_ANGLE = 360;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Keep screen awake
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        httpMethods = new HTTPMethods();

        hardWareManager = (HardWareManager) getUnitManager(FuncConstant.HARDWARE_MANAGER);
        wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);
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
     *
     * Responsável por virar o Sanbot na direção da fonte de som.
     */
    public void wheelControl(View view) {
        searchSoundSource();
    }

    public void searchSoundSource() {
        // Necessário despertar o robô para usar o método VoiceLocateListener.voiceLocateResult
        // @deprecated speechManager.doWakeUp();

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
                Toast.makeText(MainActivity.this, "Localizando fonte de som...", Toast.LENGTH_SHORT).show();

                // Se o ângulo é até 180
                if (i >= 0 && i <= 180) {

                    //então viro normalmente (sentido horário), para á direita.
                    wheelMotionManager.doRelativeAngleMotion(new RelativeAngleWheelMotion(
                            RelativeAngleWheelMotion.TURN_RIGHT, 10, i));

                } else if (i > 180 && i <= 360) {
                    // se o ângulo vai de 181 a 360 graus, então (para não ficar demorado
                    // virando no sentido horário), faço: 360 - i e viro á esquerda.
                    wheelMotionManager.doRelativeAngleMotion(new RelativeAngleWheelMotion(
                            RelativeAngleWheelMotion.TURN_LEFT, 10,
                            FULL_ANGLE - i));
                }
            }
        });

        // Para capturar o momento que o robô foi despertado
        speechManager.setOnSpeechListener(new WakenListener() {

            // Awaken Event Occurred
            @Override
            public void onWakeUp() {

                Log.i(TAG, "Awaken Event Occurred");

                // Info ao user
                Toast.makeText(MainActivity.this, "Awaken Event Occurred", Toast.LENGTH_SHORT).show();
            }

            // Sleep Event Occurred
            @Override
            public void onSleep() {

                Log.i(TAG, "Sleep Event Occurred");

                // Info ao user
                Toast.makeText(MainActivity.this, "Sleep Event Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}