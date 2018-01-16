package vivacity.com.br.testarea;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.ProjectorManager;

public class ProjetarVideoActivity extends TopBaseActivity {

    private static final String TAG = "ProjetarVideoActivity";

    //Referência para a VideoVies
    private VideoView videoView;

    // Qihan SDK
    private ProjectorManager projectorManager;

    // Para saber se o projetor está ligado
    private boolean projectorOn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate invoked.");
        setContentView(R.layout.activity_projetar_video);

        //Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Para manipular a VideoView
        videoView = (VideoView) findViewById(R.id.videoView);

        // Para manipular o projetor
        projectorManager = (ProjectorManager) getUnitManager(FuncConstant.PROJECTOR_MANAGER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart invoked.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart invoked.");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume invoked.");
        turnOnProjetor();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause invoked.");
        turnOffProjector();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop invoked.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy invoked.");
        turnOffProjector();
    }

    /**
     * Responsável pela reprodução do vídeo
     * */
    private void executarVideo() {

        // Cria uma Uri para o vídeo armazenado no dispositivo;
        Uri file = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sanbot_mps);
        // NOTE: A Uri informada para a VideoView pode se referir tanto a um arquivo local como a um
        // arquivo remoto.

        // Atribui a uri para a VideoView
        videoView.setVideoURI(file);

        // Adicionar os controles de execução, que são implementados pela classe MediaController;
        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);

        // e, por fim, iniciar a reprodução, invocando o método VideoView.start.
        videoView.start();
    }

    /**
     * To turn on the projector. Note: Wait at least 12 second between each switch, otherwise
     * the next command will not be executed.
     */
    private void turnOnProjetor() {

        // Verifica se o projetor está ligado
        if (isProjectorOn()) {

            //Projetor já estava ligado
            Toast.makeText(getApplicationContext(), "Projetor já está ligado.",
                    Toast.LENGTH_SHORT).show();
        } else {

            // Projetor não estava ligado. Em 12s ele será ligado.
            new CountDownTimer(12000, 4000) {
                /**
                 * Callback fired on regular interval.
                 *
                 * @param millisUntilFinished The amount of time until finished.
                 */
                @Override
                public void onTick(long millisUntilFinished) {

                    //Feedback para o usuário
                    Toast.makeText(getApplicationContext(), "Aguarde...", Toast.LENGTH_SHORT)
                            .show();
                }

                /**
                 * Callback fired when the time is up.
                 */
                @Override
                public void onFinish() {

                    //Ligando...
                    if (projectorManager.switchProjector(true).getErrorCode() == 1) {

                        setProjectorOn(true);

                        projectorManager.setMode(ProjectorManager.MODE_WALL);

                        // Em 14s o vídeo será reproduzido
                        new CountDownTimer(14000, 7000) {

                            /**
                             * Callback fired on regular interval.
                             *
                             * @param millisUntilFinished The amount of time until finished.
                             */
                            @Override
                            public void onTick(long millisUntilFinished) {

                                // Feedback para o usuário
                                Toast.makeText(getApplicationContext(), "Projetando em "
                                        + millisUntilFinished / 1000 + "s.", Toast.LENGTH_SHORT).show();
                            }

                            /**
                             * Callback fired when the time is up.
                             */
                            @Override
                            public void onFinish() {

                                // Começa a reprodução do vídeo
                                executarVideo();
                                // Vídeo reproduzindo
                            }
                        }.start();
                    }
                }
            }.start();
        }
    }

    /**
     * To turn off the projector. Note: Wait at least 12 second between each switch, otherwise
     * the next command will not be executed.
     */
    private void turnOffProjector() {

        // Verifica se o projetor está ligado
        if (isProjectorOn()) {

            // Projetor estava desligado. Desligando...
            if (projectorManager.switchProjector(false).getErrorCode() == 1) {

                // Projetor desligado
                setProjectorOn(false);
            }
        }
    }

    /**
     * @return boolean = O projetor está ligado?
     * */
    private boolean isProjectorOn() {
        return projectorOn;
    }

    /**
     * @param projectorOn
     * O projetor está ligado(true) / desligado (false).
     * */
    private void setProjectorOn(boolean projectorOn) {
        this.projectorOn = projectorOn;
    }

    @Override
    protected void onMainServiceConnected() {

    }
}
