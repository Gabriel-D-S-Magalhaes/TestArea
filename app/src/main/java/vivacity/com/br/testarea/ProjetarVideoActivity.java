package vivacity.com.br.testarea;

import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.ProjectorManager;

import java.io.File;
import java.io.FileFilter;

public class ProjetarVideoActivity extends TopBaseActivity {

    private static final String TAG = "ProjetarVideoActivity";
    private int qtdVideos;
    private int videoAtual = 0;
    private File[] videos;

    //Referência para a VideoView
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

        searchVideos();
        turnOnProjector();
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
     * Faz uma pesquisa por todos os arquivos .mp4 (vídeos) no diretório Documents
     */
    private void searchVideos() {

        String documents = Environment.getExternalStorageDirectory().toString() + "/Documents";
        Log.d(TAG, "Directory Path Documents: " + documents);

        File directory = new File(documents);

        // Filtro para pegar do diretório Documents somente os arquivos .mp4
        FileFilter filter = new FileFilter() {
            /**
             * Tests whether or not the specified abstract pathname should be
             * included in a pathname list.
             *
             * @param pathname The abstract pathname to be tested
             * @return <code>true</code> if and only if <code>pathname</code>
             * should be included
             */
            @Override
            public boolean accept(File pathname) {

                // Se é um arquivo normal e, em seu nome contém ".mp4" (extensão), então
                if (pathname.isFile() && pathname.getName().contains(".mp4")) {

                    // o arquivo pode ser adicionado na lista
                    return true;
                }

                // do contrário o mesmo não é adicionado.
                return false;
            }
        };

        // Lista de arquivos, filtrados, contidos no diretório Documents.
        final File[] videos = directory.listFiles(filter);
        Log.d(TAG, "Qtd de arquivos .mp4: " + videos.length);

        // Laço for que percorre toda a lista de videos
        for (File video : videos) {
            // Mostrar os nomes dos arquivos
            Log.d(TAG, "File name: " + video.getName());
        }

        setQtdVideos(videos.length);// Armazena a qtd de vídeos encontrados
        setVideos(videos);// Armazena uma lista de File
    }

    /**
     * Responsável pela reprodução do vídeo
     */
    private void executarVideo() {

        // Cria uma Uri para o vídeo armazenado no dispositivo;
        //Uri file = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sanbot_mps);
        // NOTE: A Uri informada para a VideoView pode se referir tanto a um arquivo local como a um
        // arquivo remoto.

        // Atribui a uri para a VideoView
        //videoView.setVideoURI(file);

        final File sdcard = Environment.getExternalStorageDirectory();

        File file = new File(sdcard, "/Documents/mps_innovative_solutions_for_future_retai.mp4");
        videoView.setVideoPath(getVideos()[getVideoAtual()].getAbsolutePath());

        // Criação de um objeto dos controles de execução
        MediaController controller = new MediaController(this);

        /**
         * The "previous" and "next" buttons are hidden until setPrevNextListeners() has been called
         * The "previous" and "next" buttons are visible but disabled if setPrevNextListeners() was
         * called with null listeners
         */
        // next button
        View.OnClickListener next = new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                videoView.setVideoPath(getVideos()[nextVideo()].getAbsolutePath());
            }
        };
        // previous button
        View.OnClickListener prev = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.setVideoPath(getVideos()[prevVideo()].getAbsolutePath());
            }
        };

        controller.setPrevNextListeners(next, prev);

        // Adicionar os controles de execução, que são implementados pela classe MediaController;
        videoView.setMediaController(controller);

        // e, por fim, iniciar a reprodução, invocando o método VideoView.start.
        videoView.start();
    }

    /**
     * To turn on the projector. Note: Wait at least 12 second between each switch, otherwise
     * the next command will not be executed.
     */
    private void turnOnProjector() {

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
     */
    private boolean isProjectorOn() {
        return projectorOn;
    }

    /**
     * @param projectorOn O projetor está ligado(true) / desligado (false).
     */
    private void setProjectorOn(boolean projectorOn) {
        this.projectorOn = projectorOn;
    }

    /**
     * @return Quantidade de vídeos (.mp4) encontrados.
     */
    private int getQtdVideos() {
        return qtdVideos;
    }

    /**
     * @param qtdVideos Quantidade de vídeos (.mp4) encontrados.
     */
    private void setQtdVideos(int qtdVideos) {
        this.qtdVideos = qtdVideos;
    }

    /**
     * @return Index do vídeo, sendo reproduzido.
     */
    private int getVideoAtual() {
        return videoAtual;
    }

    /**
     * ERRO DE LÓGICA AQUI!
     * */
    private int prevVideo() {

        if (getVideoAtual() == 0) {
            return getQtdVideos();
        } else {
            this.videoAtual -= 1;
            return getVideoAtual();
        }
    }

    /**
     * ERRO DE LÓGICA AQUI!
     * */
    private int nextVideo() {

        if (getVideoAtual() < getQtdVideos()) {
            this.videoAtual += 1;
            return getVideoAtual();
        } else {
            return 0;
        }
    }

    /**
     * @param videos Todos os vídeos (.mp4) encontrados no diretório Documents
     * */
    private void setVideos(File[] videos) {
        this.videos = videos;
    }

    /**
     * @return Array de arquivos .mp4
     * */
    private File[] getVideos() {
        return videos;
    }

    @Override
    protected void onMainServiceConnected() {

    }
}
