// Copyright 2020 Ciaran O'Reilly
// Copyright 2011-2020, Institute of Cybernetics at Tallinn University of Technology
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
package dk.nordfalk.esperanto.parolrekono;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

import cat.oreilly.localstt.R;
import cat.oreilly.localstt.VoskRecognitionService;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = MainActivity.class.getSimpleName();

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private Button button;
    private TextView resultsTextView;

    protected void toast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        resultsTextView = findViewById(R.id.resultsTextView);

        // new ComponentName("dk.nordfalk.esperanto.parolrekono", "cat.oreilly.localstt.VoskRecognitionService"));
        ComponentName serviceComponent = new ComponentName(getPackageName(), VoskRecognitionService.class.getName());
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this, serviceComponent);

        button.setOnClickListener(v -> {
            //final Locale esperanto = Locale.forLanguageTag("eo");
            //speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, esperanto);
            //speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Parolu nun");
            // speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
            final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
            speechRecognizer.startListening(speechRecognizerIntent);
        });


        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.i(TAG, "onReadyForSpeech "+bundle);
            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setText("");
                editText.setHint(R.string.speaknow);
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                speechRecognizer.stopListening();
            }

            @Override
            public void onError(int error) {
                Log.i(TAG, "onError "+error);
                toast("Okazis eraro dum rekono ("+error+")");
            }

            @Override
            public void onResults(Bundle bundle) {
                Log.i(TAG, "onResults");
                ArrayList<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Log.i(TAG, results.toString());
                editText.setText(results.get(0));
                resultsTextView.setText(String.join("\n", results));
                toast(String.format(getString(R.string.recognized), results.get(0)));
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.i(TAG, "onPartialResults");
                ArrayList<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Log.i(TAG, results.toString());
                editText.setText(results.get(0));
                resultsTextView.setText(String.join("\n", results));
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.d(TAG, bundle.toString());
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO }, RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }
}
