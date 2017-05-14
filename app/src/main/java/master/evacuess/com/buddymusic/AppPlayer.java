package master.evacuess.com.buddymusic;

import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Dilwar Singh on 3/8/2017.
 */

public class AppPlayer extends Fragment {


    ToggleButton playPause;
    Button stop;
    SeekBar bar;
    ImageView coverPic;
    MediaPlayer player;
    Thread thread;
    TextView currentTime, finalTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        playPause = (ToggleButton) rootView.findViewById(R.id.playStop);
        stop = (Button) rootView.findViewById(R.id.stopBt);
        bar = (SeekBar) rootView.findViewById(R.id.seekBar);
        coverPic = (ImageView) rootView.findViewById(R.id.songImageView);
        currentTime = (TextView) rootView.findViewById(R.id.currentTime);
        finalTime = (TextView) rootView.findViewById(R.id.finalTime);


        player = MediaPlayer.create(getContext(), R.raw.my_song);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                player.stop();

               /* player.pause();
                playPause.setChecked(false);
                currentTime.setText("0:0");
                player.seekTo(0);
                bar.setProgress(0);*/


            }
        });

        playPause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    startTheSong();
                } else if (player.isPlaying()) {
                    player.pause();
                }
            }
        });

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    player.pause();
                    player.seekTo(i);
                    player.start();
                    playPause.setChecked(true);


                }
                currentTime.setText(getActualTime(player.getCurrentPosition()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return rootView;
    }

    private void startTheSong() {

        int duration = player.getDuration();
        final int currentDuration = player.getCurrentPosition();
        System.out.println(duration + "              " + currentDuration);

        bar.setMax(duration);

        finalTime.setText(getActualTime(duration));

        player.start();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (player.isPlaying()) {

                    long timePosition = player.getCurrentPosition();

                    // currentTime.setText(getActualTime(timePosition));

                    bar.setProgress((int) timePosition);


                }
            }
        });

        player.start();

        thread.start();


    }


    private String getActualTime(long currentPosition) {

        long time = currentPosition / 1000;

        String s = time / 60 + ":" + time % 60;
        return s;
    }
}
