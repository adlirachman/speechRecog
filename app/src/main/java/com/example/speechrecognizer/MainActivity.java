package com.example.speechrecognizer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int VR_REQUEST = 999;
    public static final String TAG = null;
    private ListView wordList;
    private final String LOG_TAG = "SpeechRepeatActivity";
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private boolean mIslistening;
    TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button speechBtn = (Button) findViewById(R.id.speech_btn);
//        wordList = (ListView) findViewById(R.id.word_list);
        PackageManager packManager = getPackageManager();
        List<ResolveInfo> intActivities = packManager.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());


        SpeechRecognitionListener listener = new SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);
        requestRecordAudioPermission();

        mText = (TextView) findViewById(R.id.mText);

    }

    public void onClick (View view){
        if (!mIslistening)
        {
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            Toast.makeText(MainActivity.this,"Start Listening",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.destroy();
        }
    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onBeginningOfSpeech()
        {
            Log.d(TAG, "onBeginingOfSpeech");
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {

        }

        @Override
        public void onEndOfSpeech()
        {
            Log.d(TAG, "onEndOfSpeech");
        }

        @Override
        public void onError(int error)
        {
            if(error == 8){
                mSpeechRecognizer.cancel();
                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            }
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

            Log.d(TAG, "error = " + error);

        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {

        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
            Log.d(TAG, "onReadyForSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onResults(Bundle results)
        {
            Log.d(TAG, "onResults" + results); //$NON-NLS-1$
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            // matches are the return values of speech recognition engine
            // Use these values for whatever you wish to do
            String str = new String();
            for (int i=0; i<matches.size(); i++){
                Log.d(TAG,"result: "+matches.get(i));
                str += matches.get(i);
            }
            mText.setText("Results: "+String.valueOf(matches.size()));
        }

        @Override
        public void onRmsChanged(float rmsdB)
        {
        }
    }

    private void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;

            // If the user previously denied this permission then show a message explaining why
            // this permission is needed
            if (checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
    }
}
