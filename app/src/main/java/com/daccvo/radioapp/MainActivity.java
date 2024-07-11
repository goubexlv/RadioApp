package com.daccvo.radioapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

public class MainActivity extends AppCompatActivity {

    private SimpleExoPlayer player;
    private ImageButton btnPlayStop;
    private SeekBar seekBar;
    private ProgressBar progressBar;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;
    private boolean isPlaying = false;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlayStop = findViewById(R.id.btnPlayStop);
        seekBar = findViewById(R.id.seekBar);
        progressBar = findViewById(R.id.progressBar);

        // Initialisation du lecteur ExoPlayer
        player = new SimpleExoPlayer.Builder(this).build();

        // Gestion du clic sur le bouton Play/Stop
        btnPlayStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    stopPlaying();
                } else {
                    startLoading();
                }
            }
        });

        // Mise à jour de la barre de progression de la lecture
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                    seekBar.setProgress((int) player.getCurrentPosition());
                    seekBar.setMax((int) player.getDuration());
                    handler.postDelayed(this, 1000);
                }
            }
        };

        // Initialisation du SeekBar pour la navigation dans la lecture
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && player != null) {
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Démarrage de la lecture automatique lors de l'ouverture de l'application
        startLoading();
    }

    // Méthode pour commencer le chargement du flux radio
    private void startLoading() {
        try {
            String radioUrl = "https://streaming.brol.tech/jamendolounge"; // URL du flux radio (à remplacer par votre propre URL)
            MediaItem mediaItem = MediaItem.fromUri(radioUrl);
            progressBar.setVisibility(View.VISIBLE); // Affichage de la barre de progression
            btnPlayStop.setEnabled(false); // Désactivation du bouton pendant le chargement

            // Chargement du média dans le lecteur ExoPlayer
            player.setMediaItem(mediaItem);
            player.prepare(); // Préparation du lecteur de manière asynchrone
            player.addListener(new ExoPlayer.Listener() {
                @Override
                public void onPlaybackStateChanged(int state) {
                    if (state == ExoPlayer.STATE_READY) {
                        progressBar.setVisibility(View.GONE); // Masquage de la barre de progression une fois le chargement terminé
                        btnPlayStop.setEnabled(true); // Réactivation du bouton une fois le chargement terminé
                        startPlaying(); // Démarrage de la lecture
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error starting playback", e);
        }
    }

    // Méthode pour démarrer la lecture
    private void startPlaying() {
        try {
            player.play(); // Démarrage de la lecture
            handler.post(updateSeekBar); // Mise à jour de la barre de progression
            isPlaying = true; // Indication que la lecture est en cours
            btnPlayStop.setImageResource(R.drawable.ic_stop); // Changement de l'icône du bouton pour afficher l'arrêt
            Log.d(TAG, "Playing started");
        } catch (Exception e) {
            Log.e(TAG, "Error starting playback", e);
        }
    }

    // Méthode pour arrêter la lecture
    private void stopPlaying() {
        player.stop(); // Arrêt de la lecture
        handler.removeCallbacks(updateSeekBar); // Arrêt de la mise à jour de la barre de progression
        seekBar.setProgress(0); // Réinitialisation de la position de la barre de progression
        isPlaying = false; // Indication que la lecture est arrêtée
        btnPlayStop.setImageResource(R.drawable.ic_play); // Changement de l'icône du bouton pour afficher la lecture
        Log.d(TAG, "Playing stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release(); // Libération des ressources du lecteur ExoPlayer
        handler.removeCallbacks(updateSeekBar); // Arrêt de la mise à jour de la barre de progression
    }
}
