package vivacity.com.br.testarea;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.qihancloud.opensdk.function.unit.HardWareManager;
import com.qihancloud.opensdk.function.unit.SpeechManager;
import com.qihancloud.opensdk.function.unit.WheelMotionManager;
import com.qihancloud.opensdk.function.unit.interfaces.hardware.VoiceLocateListener;
import com.qihancloud.opensdk.function.unit.interfaces.speech.WakenListener;

/**
 * Rever essa class
 */
public class SearchSoundSourceActivity extends TopBaseActivity {

    private SpeechManager speechManager;
    private HardWareManager hardWareManager;
    private WheelMotionManager wheelMotionManager;

    private static final String TAG = "SearchSoundSourceActivity";
    private static final int FULL_ANGLE = 360;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);
        hardWareManager = (HardWareManager) getUnitManager(FuncConstant.HARDWARE_MANAGER);
        wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
    }

    public void searchSoundSource() {
        // Necessário despertar o robô para usar o método VoiceLocateListener.voiceLocateResult
        speechManager.doWakeUp();

        // Para capturar o momento que o robô foi despertado
        speechManager.setOnSpeechListener(new WakenListener() {

            // Awaken Event Occurred
            @Override
            public void onWakeUp() {

                //Log.i(TAG, "Awaken Event Occurred");

                // Info ao user
                //Toast.makeText(WheelControl.this, "Awaken Event Occurred", Toast.LENGTH_SHORT).show();

                hardWareManager.setOnHareWareListener(new VoiceLocateListener() {

                    /**
                     * Resultado da localizacão da fonte de som
                     * @param i = Angle refers to the angle offset of sound source and straight
                     *          ahead of robot by clockwise.
                     */
                    @Override
                    public void voiceLocateResult(int i) {

                        // Info para o user
                        //Toast.makeText(WheelControl.this, "Localizando fonte de som...", Toast.LENGTH_SHORT).show();

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
            }

            // Sleep Event Occurred
            @Override
            public void onSleep() {

                //Log.i(TAG, "Sleep Event Occurred");

                //Toast.makeText(WheelControl.this, "Sleep Event Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onMainServiceConnected() {

    }
}
