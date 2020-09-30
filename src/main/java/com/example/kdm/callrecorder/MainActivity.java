package com.example.kdm.callrecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    Button btnStart, btnStop, btnPlayRecorded, btnStopRecorded;
    File file;

    String fileExtension = ".3gp",pathSave;
    private static final String TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 100;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 200;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askForPermissions();
        setId();
        setListener();
        btnStop.setEnabled(false);
        btnPlayRecorded.setEnabled(false);
        btnStopRecorded.setEnabled(false);
    }

    public void setId(){
        btnStart = findViewById(R.id.start_recording);
        btnStop = findViewById(R.id.stop_recording);
        btnPlayRecorded = findViewById(R.id.start_recorded);
        btnStopRecorded = findViewById(R.id.stop_recorded);
    }

    public void setListener(){
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnPlayRecorded.setOnClickListener(this);
        btnStopRecorded.setOnClickListener(this);
    }


    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_recording:
                startRecording();
                break;

            case R.id.stop_recording:
                stopRecording();
                break;

            case R.id.start_recorded :
                playRecorded();
                break;

            case R.id.stop_recorded:
                stopRecorded();
                break;
        }
    }

    public void startRecording(){
        getFilePath();
        setMediaRecorder();
        try{
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e){
            e.printStackTrace();
            Log.e(TAG, "Start Recording: Failed Due to ", e.getCause());
        } catch (IllegalStateException ise){

        }
        btnStart.setEnabled(false);
        btnPlayRecorded.setEnabled(false);
        btnStopRecorded.setEnabled(false);
        btnStop.setEnabled(true);

        btnStart.setText("Recording...");
        btnStart.setTextColor(Color.GREEN);

        btnStopRecorded.setTextColor(Color.BLACK);
        btnStopRecorded.setText("STOP RECORDED");

        btnStop.setTextColor(Color.BLACK);
        btnStop.setText("STOP RECORDING");

        Toast.makeText(this, "Recording Started",Toast.LENGTH_SHORT).show();
    }

    public void stopRecording(){

        try{
            mediaRecorder.stop();
        }catch (RuntimeException stopException){

        }

        btnStop.setText("Stopped");
        btnStop.setTextColor(Color.GREEN);

        btnStart.setText("START RECORDING");
        btnStart.setTextColor(Color.BLACK);

        btnStop.setEnabled(false);
        btnStopRecorded.setEnabled(false);
        btnPlayRecorded.setEnabled(true);
        btnStart.setEnabled(true);

        Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show();
    }

    public void playRecorded(){
        btnStart.setEnabled(false);
        btnPlayRecorded.setEnabled(false);
        btnStopRecorded.setEnabled(true);
        btnStop.setEnabled(false);

        btnPlayRecorded.setText("Playing...");
        btnPlayRecorded.setTextColor(Color.GREEN);

        btnStop.setTextColor(Color.BLACK);
        btnStop.setText("STOP RECORDING");

        btnStopRecorded.setTextColor(Color.BLACK);
        btnStopRecorded.setText("STOP RECORDED");

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(pathSave);
            mediaPlayer.prepare();
        } catch (IOException ioe){

        }
        mediaPlayer.start();

        Toast.makeText(this, "Playing this recorded audio", Toast.LENGTH_SHORT).show();
    }

    public void stopRecorded(){
        btnStart.setEnabled(true);
        btnPlayRecorded.setEnabled(true);
        btnStop.setEnabled(false);
        btnStopRecorded.setEnabled(false);

        btnStopRecorded.setText("Stopped");
        btnStopRecorded.setTextColor(Color.GREEN);

        btnPlayRecorded.setText("PLAY RECORDED");
        btnPlayRecorded.setTextColor(Color.BLACK);

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            setMediaRecorder();
        }
    }

    public String getFilePath(){
        String filePath = Environment.getExternalStorageDirectory().getPath();
        file = new File(filePath,"CallRecorder");
        if (!file.exists()) {
            file.mkdirs();
        }
        pathSave=file.getAbsolutePath()+"/"+fileExtension;
        return (pathSave);
    }

    private void setMediaRecorder(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(getFilePath());
    }

    public void askForPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_RECORD_AUDIO_PERMISSION);
        }
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
        }
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
    }
}
