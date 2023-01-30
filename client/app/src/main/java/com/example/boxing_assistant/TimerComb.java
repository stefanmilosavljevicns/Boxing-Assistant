package com.example.boxing_assistant;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.boxing_assistant.Models.Model;
import com.example.boxing_assistant.MusicDataBase.MusicDataBase;
import com.example.boxing_assistant.MusicDataBase.MusicEntity;
import com.example.boxing_assistant.ViewModel.DialogComboViewModel;
import com.example.boxing_assistant.databinding.FragmentTimerCombBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimerComb extends Fragment {
    View view;
    int timerS, timerM, restS, restM, prepM, prepS, workcolor, restcolor, endcolor, soundStart, soundNextMove, id;
    private final Handler mSeekbarUpdateHandler = new Handler();
    boolean isReseted, firstTime, musicCheker, isMusicPaused, paused, crashMusic;
    SoundPool soundPool;
    long statPrepDur, statRoundDur, statRestDur, current, currentMoves;
    MusicDataBase musicDataBase;
    List<MusicEntity> musicEntities;
    MediaPlayer mediaPlayer;
    Long current_move;
    int rounds, current_round, combo_index, g;
    ConstraintLayout rl;
    TextView Timer, Rounder, Moves, Moves_Dur, Status;
    String clock, clockForMoves;
    CountDownTimer cdt;
    String endtime;
    FloatingActionButton next, previous;
    ImageButton reset, pause;
    ArrayList<Model> kombo;
    Model mod;
    ImageButton btnplay, btnnext, btnprev;
    TextView txtsname, txtsstart, txtsstop;
    SeekBar seekmusic;
    boolean exit = false;
    private FragmentTimerCombBinding binding;
    private DialogComboViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTimerCombBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        initSoundPool();
        rl = binding.bgForcomb;
        paused = false;
        crashMusic = false;
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(DialogComboViewModel.class);
        sharedViewModel.init();
        firstTime = false;
        musicCheker = TimerArgs.fromBundle(getArguments()).getMusic();
        kombo = sharedViewModel.getNameData().getValue();
        combo_index = -1;

        musicCheker = TimerArgs.fromBundle(getArguments()).getMusic();


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (!exit) {
                    Toast.makeText(getContext(), "PRESS BACK BUTTON AGAIN TO EXIT", Toast.LENGTH_SHORT).show();
                    exit = true;
                    cdt = new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            exit = false;
                        }
                    }.start();
                } else {

                    Navigation.findNavController(view).navigate(R.id.action_timer_Comb_to_timer_Sa_Kombinacijama);

                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        rounds = TimerCombArgs.fromBundle(getArguments()).getRoundPicker();
        timerS = TimerCombArgs.fromBundle(getArguments()).getRoundTimerSec();
        timerM = TimerCombArgs.fromBundle(getArguments()).getRoundTimerMin();
        restS = TimerCombArgs.fromBundle(getArguments()).getRestTimerSec();
        restM = TimerCombArgs.fromBundle(getArguments()).getRestTimerMin();
        prepS = TimerCombArgs.fromBundle(getArguments()).getPrepareTimerSec();
        prepM = TimerCombArgs.fromBundle(getArguments()).getPrepareTimerMin();
        workcolor = Color.parseColor("#1cb84a");
        restcolor = Color.parseColor("#c9b816");
        endcolor = Color.parseColor("#000000");
        current_round = 0;
        next = binding.next;
        previous = binding.previous;

        reset = binding.RESETBtnForcomb;
        Moves = binding.listOfMoves;

        Moves_Dur = binding.timeOfMoves;
        pause = binding.PAUSEBtnForcomb;
        Status = binding.STATUSForcomb;
        Timer = binding.TIMERForcomb;

        Rounder = binding.ROUNDCHECKERForcomb;
        next.setOnClickListener(this::changeMoveForw);
        previous.setOnClickListener(this::changeMoveBack);

        pause.setOnClickListener(this::pause);
        reset.setOnClickListener(this::reset);
        Rounder.setText(String.format(Locale.ENGLISH, "%02d / %02d", current_round, rounds));
        statRoundDur = TimeUnit.MINUTES.toMillis(timerM) + TimeUnit.SECONDS.toMillis(timerS);
        statRestDur = TimeUnit.MINUTES.toMillis(restM) + TimeUnit.SECONDS.toMillis(restS);
        btnplay = binding.playComb;
        btnnext = binding.prevRightComb;
        btnprev = binding.prevLeftComb;
        txtsname = binding.titleMusicComb;
        txtsstart = binding.startTimeComb;
        txtsstop = binding.endTimeComb;
        seekmusic = binding.seekbarComb;
        turnonUi(false);
        if (!musicCheker) {
            btnplay.setVisibility(View.GONE);
            btnnext.setVisibility(View.GONE);
            btnprev.setVisibility(View.GONE);
            txtsname.setVisibility(View.GONE);
            txtsstart.setVisibility(View.GONE);
            txtsstop.setVisibility(View.GONE);
            seekmusic.setVisibility(View.GONE);
        } else {
            musicDataBase = MusicDataBase.getInstance(getContext());
            musicEntities = musicDataBase.mainDao().getAll();
            id = 0;
        }
        if (prepS > 0 || prepM > 0) {
            Status.setText("TIME TO PREPARE");
            clock = String.format(Locale.ENGLISH, "%02d:%02d", prepM, prepS);
            Timer.setText(clock);
            statPrepDur = TimeUnit.MINUTES.toMillis(prepM) + TimeUnit.SECONDS.toMillis(prepS);
            cdt = new CountDownTimer(statPrepDur, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    clock = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    Timer.setText(clock);
                    current = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    changeRound();
                    playStart();
                    round();
                    if (musicCheker) {
                        turnonUi(true);
                        playonstartmusic();
                    }

                    potezi();
                }
            }.start();
        } else {
            playStart();
            changeRound();
            round();
            if (musicCheker) {
                turnonUi(true);
                playonstartmusic();
            }
            potezi();
        }
        return view;
    }

    public void turnonUi(boolean decide) {
        if (decide) {
            btnplay.setVisibility(View.VISIBLE);
            btnnext.setVisibility(View.VISIBLE);
            btnprev.setVisibility(View.VISIBLE);
            txtsname.setVisibility(View.VISIBLE);
            txtsstart.setVisibility(View.VISIBLE);
            txtsstop.setVisibility(View.VISIBLE);
            seekmusic.setVisibility(View.VISIBLE);
        } else {
            btnplay.setVisibility(View.INVISIBLE);
            btnnext.setVisibility(View.INVISIBLE);
            btnprev.setVisibility(View.INVISIBLE);
            txtsname.setVisibility(View.INVISIBLE);
            txtsstart.setVisibility(View.INVISIBLE);
            txtsstop.setVisibility(View.INVISIBLE);
            seekmusic.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        soundPool.release();
        musicCheker = false;
        if (mediaPlayer != null) mediaPlayer.release();
        binding = null;
    }

    public void checkMusic(MediaPlayer mp) {
        if (mp == null) {
            musicDataBase.mainDao().delete(musicEntities.get(id));
            if (musicDataBase.mainDao().getAll().size() <= 0) {
                btnnext.setEnabled(false);
                btnplay.setEnabled(false);
                btnprev.setEnabled(false);
                seekmusic.setEnabled(false);
                crashMusic = true;
                txtsname.setText("PLEASE ADD MORE MUSIC");

            } else {
                id = ((id + 1) % musicEntities.size());
                Uri uri = Uri.parse(musicEntities.get(id).getMusicurl());
                mediaPlayer = MediaPlayer.create(getContext(), uri);
                checkMusic(mediaPlayer);
                if (crashMusic) return;
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

    void pausePlayMusic(View view) {
        if (!isMusicPaused) {
            mediaPlayer.pause();
            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
            isMusicPaused = true;
            btnplay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
        } else {
            mediaPlayer.start();
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
            isMusicPaused = false;
            btnplay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
        }
    }

    public void playonstartmusic() {
        isMusicPaused = false;
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                id = ((id + 1) % musicEntities.size());
                Uri uri = Uri.parse(musicEntities.get(id).getMusicurl());
                mediaPlayer = MediaPlayer.create(getContext(), uri);
                checkMusic(mediaPlayer);
                if (crashMusic) return;
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
                id = ((id - 1) < 0) ? (musicEntities.size() - 1) : (id - 1);
                Uri uri = Uri.parse(musicEntities.get(id).getMusicurl());
                mediaPlayer = MediaPlayer.create(getContext(), uri);
                checkMusic(mediaPlayer);
                if (crashMusic) return;
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
        Uri uri = Uri.parse(musicEntities.get(id).getMusicurl());

        mediaPlayer = MediaPlayer.create(getContext(), uri);
        checkMusic(mediaPlayer);
        if (crashMusic) return;
        btnplay.setOnClickListener(this::pausePlayMusic);
        endtime = createTime(mediaPlayer.getDuration());
        txtsstop.setText(endtime);
        txtsname.setText(musicEntities.get(id).getMusicname());

        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnnext.performClick();
            }
        });
        mediaPlayer.start();
        seekmusic.setMax(mediaPlayer.getDuration());
        seekmusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.design_default_color_primary), PorterDuff.Mode.MULTIPLY);
        seekmusic.getThumb().setColorFilter(getResources().getColor(R.color.design_default_color_primary), PorterDuff.Mode.SRC_IN);
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

    private final Runnable mUpdateSeekbar = new Runnable() {

        @Override
        public void run() {
            if (musicCheker) {
                seekmusic.setProgress(mediaPlayer.getCurrentPosition());
                txtsstart.setText(createTime(mediaPlayer.getCurrentPosition()));
                mSeekbarUpdateHandler.postDelayed(this, 1000);
            }

        }
    };

    public void initSoundPool() {

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
        } else {
            soundPool
                    = new SoundPool(
                    3,
                    AudioManager.STREAM_MUSIC,
                    0);
        }
        soundStart = soundPool.load(getContext(), R.raw.box, 1);
        soundNextMove = soundPool.load(getContext(), R.raw.beep, 1);
    }

    public void playStart() {
        soundPool.play(soundStart, 1, 1, 0, 0, 1);
    }

    public void playNext() {
        soundPool.play(soundNextMove, 1, 1, 0, 0, 1);
    }

    public void changeMoveForw(View v) {
        if (combo_index < kombo.size() - 1 & (Status.getText().equals("FIGHT") | Status.getText().equals("REST"))) {
            changeMove(true);
        }
    }

    public void changeMoveBack(View v) {
        if (combo_index > 0 & (Status.getText().equals("FIGHT") | Status.getText().equals("REST"))) {
            changeMove(false);
        }
    }

    public void resetMove() {
        combo_index = -1;

    }

    public void changeMove(boolean add) {
        if (add) {
            combo_index++;
        } else {
            combo_index--;
        }
        if (combo_index == kombo.size()) {
            combo_index = 0;
        }
        mod = kombo.get(combo_index);
        currentMoves = mod.getTime();
        String editor = mod.getTitle();
        editor = editor.replace(" | ", "\n");
        Moves.setText(editor);
        current_move = currentMoves;
        g = current_move.intValue() / 1000;

        clockForMoves = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(currentMoves), TimeUnit.MILLISECONDS.toSeconds(currentMoves) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentMoves)));
        Moves_Dur.setText(clockForMoves);

    }

    public void potezi() {

        currentMoves -= 1000;
        clockForMoves = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(currentMoves), TimeUnit.MILLISECONDS.toSeconds(currentMoves) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentMoves)));
        Moves_Dur.setText(clockForMoves);

        if (currentMoves <= 0) {
            if (!firstTime) {
                firstTime = true;
            } else {
                playNext();
            }

            changeMove(true);

        }

    }

    public void changeRound() {
        current_round++;
        Rounder.setText(String.format(Locale.ENGLISH, "%02d / %02d", current_round, rounds));
    }

    public void rest() {
        firstTime = true;
        rl.setBackgroundColor(restcolor);

        Status.setText("REST");

        cdt = new CountDownTimer(statRestDur, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                clock = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
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

    public void round() {
        rl.setBackgroundColor(workcolor);
        Status.setText("FIGHT");
        startTimer(statRoundDur);
    }

    public void pause(View g) {
        if (Status.getText().equals("FIGHT")) {
            if (!paused) {
                paused = true;
                isReseted = false;
                pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                cdt.cancel();
                reset.setEnabled(true);
                reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
            } else {

                paused = false;
                if (isReseted) {
                    startTimer(statRoundDur);
                    reset.setEnabled(false);
                    reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.grey_font));
                    pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                } else {
                    startTimer(current);
                    reset.setEnabled(false);
                    reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.grey_font));
                    pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                }
            }

        } else if (Status.getText().equals("REST")) {
            if (!paused) {
                paused = true;
                isReseted = false;
                pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                cdt.cancel();
                reset.setEnabled(true);
                reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
            } else {

                paused = false;
                if (isReseted) {
                    restWithTimer(statRestDur);
                    reset.setEnabled(false);
                    reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.grey_font));
                    pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                } else {
                    restWithTimer(current);
                    reset.setEnabled(false);
                    reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.grey_font));
                    pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                }
            }
        }
    }

    public void reset(View g) {
        resetMove();
        if (Status.getText().equals("FIGHT"))
            clock = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(statRoundDur), TimeUnit.MILLISECONDS.toSeconds(statRoundDur) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(statRoundDur)));
        if (Status.getText().equals("REST"))
            clock = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(statRestDur), TimeUnit.MILLISECONDS.toSeconds(statRestDur) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(statRestDur)));
        isReseted = true;
        changeMove(true);
        Timer.setText(clock);
        reset.setEnabled(false);
        reset.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.grey_font));
    }

    public void restWithTimer(long timer) {
        cdt = new CountDownTimer(timer, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                clock = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
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

    public void startTimer(long round_time) {

        if (Status.getText().equals("FIGHT")) {
            cdt = new CountDownTimer(round_time, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    clock = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    potezi();
                    Timer.setText(clock);
                    current = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    playStart();
                    if (current_round + 1 <= rounds) {
                        changeRound();
                        if (statRestDur > 0) {
                            rest();
                        } else {
                            round();
                        }
                    } else {
                        rl.setBackgroundColor(endcolor);
                        Status.setText("Workout is done!");
                    }
                }
            }.start();
        }
    }

    public String createTime(int duration) {
        String time = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;
        time += min + ":";
        if (sec < 10) {
            time += "0";
        }
        time += sec;
        return time;
    }
}