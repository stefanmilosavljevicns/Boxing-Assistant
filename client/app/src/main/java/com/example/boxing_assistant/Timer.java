package com.example.boxing_assistant;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.boxing_assistant.MusicDataBase.MusicDataBase;
import com.example.boxing_assistant.MusicDataBase.MusicEntity;
import com.example.boxing_assistant.databinding.FragmentTimerBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class Timer extends Fragment {

    View view;
    String endtime;
    int timerS, timerM, restS, restM, prepM, prepS,workcolor,restcolor,endcolor;
    private Handler mSeekbarUpdateHandler = new Handler();
    boolean paused;
    boolean isReseted, isMusicPaused, crashMusic;
    boolean musicCheker;
    List<MusicEntity> musicEntities;
    SoundPool soundPool;
    long statPrepDur, statRoundDur, statRestDur,current;
    int rounds, currentRound,soundStart;
    TextView Status,Timer,Rounder;
    String clock;
    ConstraintLayout rl;
    CountDownTimer cdt;
    MusicDataBase musicDataBase;
    ImageButton reset,pause;
    MediaPlayer mediaPlayer;
    int id;
    FragmentTimerBinding binding;
    //Input za muziku
    FloatingActionButton btnplay;
    ImageButton btnnext,btnprev;
    TextView txtsname,txtsstart,txtsstop;
    SeekBar seekmusic;
    boolean exit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTimerBinding.inflate(inflater,container,false);
        view = binding.getRoot();
        mediaPlayer = new MediaPlayer();

        paused = false;
        crashMusic = false;
        initSoundPool();
        rl = binding.bg;
        rounds = TimerArgs.fromBundle(getArguments()).getRoundPicker();
        timerS = TimerArgs.fromBundle(getArguments()).getRoundTimerSec();
        timerM = TimerArgs.fromBundle(getArguments()).getRoundTimerMin();
        restS = TimerArgs.fromBundle(getArguments()).getRestTimerSec();
        restM = TimerArgs.fromBundle(getArguments()).getRestTimerMin();
        prepS = TimerArgs.fromBundle(getArguments()).getPrepareTimerSec();
        prepM = TimerArgs.fromBundle(getArguments()).getPrepareTimerMin();
        musicCheker = TimerArgs.fromBundle(getArguments()).getMusic();
        //MUZIKA
        btnplay = binding.play;
        btnnext = binding.prevRight;
        btnprev = binding.prevLeft;
        txtsname = binding.titleMusic;
        txtsstart = binding.startTime;
        txtsstop = binding.endTime;
        seekmusic = binding.seekbar;
        turnonUi(false);
        if(!musicCheker) {

            btnplay.setVisibility(View.GONE);btnnext.setVisibility(View.GONE);btnprev.setVisibility(View.GONE);txtsname.setVisibility(View.GONE);txtsstart.setVisibility(View.GONE);txtsstop.setVisibility(View.GONE);seekmusic.setVisibility(View.GONE);}
        else{

            musicDataBase = MusicDataBase.getInstance(getContext());
            musicEntities = musicDataBase.mainDao().getAll();
            id = 0;
        }
        workcolor = Color.parseColor("#1cb84a");
        restcolor = Color.parseColor("#c9b816");
        endcolor = Color.parseColor("#000000");
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(!exit){
                    Toast.makeText(getContext(),"PRESS BACK BUTTON AGAIN TO EXIT",Toast.LENGTH_SHORT).show();
                    exit = true;
                    cdt = new CountDownTimer(3000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            exit = false;
                        }
                    }.start();
                }
                else {
                    Navigation.findNavController(view).navigate(R.id.action_timer_to_basic_Timer2);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        currentRound = 0;

        reset = binding.RESETBtn;
        pause = binding.PAUSEBtn;
        Timer = binding.TIMER;
        Status = binding.STATUS;
        Rounder = binding.ROUNDCHECKER;
        pause.setOnClickListener(this::pause);
        reset.setOnClickListener(this::reset);
        Rounder.setText(String.format(Locale.ENGLISH,"ROUND: %02d / %02d", currentRound,rounds));
        statRoundDur = TimeUnit.MINUTES.toMillis(timerM) + TimeUnit.SECONDS.toMillis(timerS);
        statRestDur = TimeUnit.MINUTES.toMillis(restM) + TimeUnit.SECONDS.toMillis(restS);
        reset.setEnabled(false);
        reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.grey_font));
        if(prepS > 0 || prepM > 0 ){
            Status.setText("TIME TO PREPARE");
            clock = String.format(Locale.ENGLISH, "%02d:%02d", prepM, prepS);
            Timer.setText(clock);
            statPrepDur = TimeUnit.MINUTES.toMillis(prepM) + TimeUnit.SECONDS.toMillis(prepS);

            cdt = new CountDownTimer(statPrepDur, 1000){

                @Override
                public void onTick(long millisUntilFinished) {

                    clock = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    Timer.setText(clock);
                    current = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    playStart();
                    changeRound();
                    round();
                    //MUZIKA
                    if(musicCheker) {

                            turnonUi(true);
                            playonstartmusic();


                    }
                }
            }.start();

        }
        else{
            playStart();
            changeRound();
            round();
            //MUZIKA
            if(musicCheker) {
                    turnonUi(true);
                    playonstartmusic();
            }
        }
        return view;
    }
    private Runnable mUpdateSeekbar = new Runnable() {

        @Override
        public void run() {
            if(musicCheker){
                seekmusic.setProgress(mediaPlayer.getCurrentPosition());
                txtsstart.setText(createTime(mediaPlayer.getCurrentPosition()));
                mSeekbarUpdateHandler.postDelayed(this, 1000);
            }

        }
    };
    //muzika
    void pause_play_music(View view){
        if(!isMusicPaused){
            mediaPlayer.pause();
            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
            isMusicPaused = true;
            btnplay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
       else{
           mediaPlayer.start();
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
            isMusicPaused = false;
            btnplay.setImageResource(R.drawable.ic_baseline_pause_24);
        }
    }
    public void turnonUi(boolean decide){
        if(decide){
            btnplay.setVisibility(View.VISIBLE);
            btnnext.setVisibility(View.VISIBLE);
            btnprev.setVisibility(View.VISIBLE);
            txtsname.setVisibility(View.VISIBLE);
            txtsstart.setVisibility(View.VISIBLE);
            txtsstop.setVisibility(View.VISIBLE);
            seekmusic.setVisibility(View.VISIBLE);
        }
        else{
            btnplay.setVisibility(View.INVISIBLE);
            btnnext.setVisibility(View.INVISIBLE);
            btnprev.setVisibility(View.INVISIBLE);
            txtsname.setVisibility(View.INVISIBLE);
            txtsstart.setVisibility(View.INVISIBLE);
            txtsstop.setVisibility(View.INVISIBLE);
            seekmusic.setVisibility(View.INVISIBLE);
        }
    }
    public void checkMusic(MediaPlayer mp){
        Boolean jk;
        jk = true;
        if(!jk){
            if(mp == null){
                musicDataBase.mainDao().delete(musicEntities.get(id));
                if(musicDataBase.mainDao().getAll().size() <= 0){
                    btnnext.setEnabled(false);
                    btnplay.setEnabled(false);
                    btnprev.setEnabled(false);
                    seekmusic.setEnabled(false);
                    crashMusic = true;
                    txtsname.setText("PLEASE ADD MORE MUSIC");

                }
                else{
                    id = ((id+1)%musicEntities.size());
                    Uri uri = Uri.parse(musicEntities.get(id).getMusicurl());
                    mediaPlayer =  MediaPlayer.create(getContext(),uri);
                    checkMusic(mediaPlayer);
                    if(crashMusic) return;
                    seekmusic.setProgress(0);
                    endtime = createTime(mediaPlayer.getDuration());
                    seekmusic.setMax(mediaPlayer.getDuration());
                    txtsstop.setText(endtime);
                    txtsname.setText(musicEntities.get(id).getMusicname());
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            btnnext.performClick();
                        }
                    });
                    mediaPlayer.start();
                }

            }
        }


    }
    //MUZIKA
    public void playonstartmusic(){
        isMusicPaused = false;


        Uri uri = Uri.parse(musicEntities.get(id).getMusicurl());
        try{
            mediaPlayer = MediaPlayer.create(getContext(),uri);
        }
        catch (Exception e){
            Log.d("Greska",e.toString());
        }
        checkMusic(mediaPlayer);
        if(crashMusic) return;
        mediaPlayer.start();
        endtime = createTime(mediaPlayer.getDuration());
        txtsstop.setText(endtime);
        txtsname.setText(musicEntities.get(id).getMusicname());
        btnplay.setOnClickListener(this::pause_play_music);
        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnnext.performClick();
            }
        });
        mediaPlayer.start();
        seekmusic.setMax(mediaPlayer.getDuration());
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                id = ((id+1)%musicEntities.size());
                Uri uri = Uri.parse(musicEntities.get(id).getMusicurl());
                mediaPlayer =  MediaPlayer.create(getContext(),uri);
                checkMusic(mediaPlayer);
                if(crashMusic) return;
                seekmusic.setProgress(0);
                endtime = createTime(mediaPlayer.getDuration());
                seekmusic.setMax(mediaPlayer.getDuration());
                txtsstop.setText(endtime);
                txtsname.setText(musicEntities.get(id).getMusicname());
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        btnnext.performClick();
                    }
                });
                mediaPlayer.start();
            }
        });

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                seekmusic.setProgress(0);
                id = ((id-1)<0)?(musicEntities.size()-1):(id - 1);
                Uri uri = Uri.parse(musicEntities.get(id).getMusicurl());
                mediaPlayer =  MediaPlayer.create(getContext(),uri);
                checkMusic(mediaPlayer);
                if(crashMusic) return;
                endtime = createTime(mediaPlayer.getDuration());
                txtsstop.setText(endtime);
                txtsname.setText(musicEntities.get(id).getMusicname());
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        btnnext.performClick();
                    }
                });
                mediaPlayer.start();
            }
        });
        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }
    public void initSoundPool(){

        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes
                    audioAttributes
                    = new AudioAttributes
                    .Builder()
                    .setUsage(
                            AudioAttributes
                                    .USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(
                            AudioAttributes
                                    .CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool
                    = new SoundPool
                    .Builder()
                    .setMaxStreams(3)
                    .setAudioAttributes(
                            audioAttributes)
                    .build();
        }
        else {
            soundPool
                    = new SoundPool(
                    3,
                    AudioManager.STREAM_MUSIC,
                    0);
        }
        soundStart = soundPool.load(getContext(),R.raw.box,1);

    }
    public void playStart(){
        soundPool.play(soundStart,1,1,0,0,1);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        soundPool.release();
        musicCheker = false;
       if(mediaPlayer!= null) mediaPlayer.release();

        binding = null;
    }

    public void changeRound(){
        currentRound++;
        Rounder.setText(String.format(Locale.ENGLISH,"ROUND: %02d / %02d", currentRound,rounds));
    }
    public void rest(){
       rl.setBackgroundColor(restcolor);
        Status.setText("REST");
        Status.setBackgroundColor(restcolor);
        cdt = new CountDownTimer(statRestDur,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                clock = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                Timer.setText(clock);
                current = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                playStart();
                round();
            }
        }.start();
    }
    public void restWithTimer(long timer){
        cdt = new CountDownTimer(timer,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                clock = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                Timer.setText(clock);
                current = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                playStart();
                round();
            }
        }.start();
    }
    public void round(){
     rl.setBackgroundColor(workcolor);
        Status.setText("FIGHT");
        Status.setBackgroundColor(workcolor);
        startTimer(statRoundDur);

    }
    public void pause(View g){

        if(Status.getText().equals("FIGHT")){
            if(!paused)
            {
                paused = true;
                isReseted =false;
                pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                cdt.cancel();
                reset.setEnabled(true);
                reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
            }
            else{

                paused = false;
                if(isReseted){
                    startTimer(statRoundDur);
                    reset.setEnabled(false);
                    reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.grey_font));
                    pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                }
                else {
                    startTimer(current);
                    reset.setEnabled(false);
                    reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.grey_font));
                    pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                }
            }

        }
        else if (Status.getText().equals("REST")){
            if(!paused)
            {
                paused = true;
                isReseted =false;
                pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                cdt.cancel();
                reset.setEnabled(true);
                reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
            }
            else{

                paused = false;
                if(isReseted){
                   restWithTimer(statRestDur);
                    reset.setEnabled(false);
                    reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.grey_font));
                    pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                }
                else {
                    restWithTimer(current);
                    reset.setEnabled(false);
                    reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.grey_font));
                    pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                }
            }
        }

    }
    @SuppressLint("SuspiciousIndentation")
    public void reset(View g){
      if(Status.getText().equals("FIGHT"))  clock = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(statRoundDur),TimeUnit.MILLISECONDS.toSeconds(statRoundDur) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(statRoundDur)));
        if(Status.getText().equals("REST"))  clock = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(statRestDur),TimeUnit.MILLISECONDS.toSeconds(statRestDur) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(statRestDur)));
        isReseted =true;
        Timer.setText(clock);
        reset.setEnabled(false);
        reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.grey_font));

    }
    public void startTimer(long round_time){
        if(Status.getText().equals("FIGHT")){
            cdt = new CountDownTimer(round_time, 1000){

                @Override
                public void onTick(long millisUntilFinished) {
                        clock = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    Timer.setText(clock);
                    current = millisUntilFinished;
                }
                @Override
                public void onFinish() {
                    playStart();

                    if (currentRound + 1 <= rounds) {
                        changeRound();
                        if (statRestDur > 0) {

                            rest();
                        }
                        else{
                            round();
                        }
                    }

                    else{
                        Status.setText("Done!");

                      rl.setBackgroundColor(endcolor);

                    }
                }
            }.start();
        }
    }
    public String createTime(int duration){
        String time  = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;
        time += min+":" ;
        if (sec < 10){
            time+="0";
        }
        time+= sec;
        return time;
    }

}
