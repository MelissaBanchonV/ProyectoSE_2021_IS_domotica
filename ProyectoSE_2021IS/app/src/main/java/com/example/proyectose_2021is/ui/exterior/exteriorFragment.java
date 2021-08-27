package com.example.proyectose_2021is.ui.exterior;

import static com.example.proyectose_2021is.ui.voz.vozFragment.RecordAudioRequestCode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectose_2021is.MainActivity;
import com.example.proyectose_2021is.R;
import com.example.proyectose_2021is.databinding.FragmentExteriorBinding;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.Locale;

public class exteriorFragment extends Fragment {

    private exteriorViewModel exteriorViewModel;
    private FragmentExteriorBinding binding;

    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private ImageView micButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exteriorViewModel =
                new ViewModelProvider(this).get(exteriorViewModel.class);

        binding = FragmentExteriorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        WebView myWebView = root.findViewById(R.id.camara);
        myWebView.loadUrl("http://192.168.200.22:8080/?action=stream");
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    Intent intent=new Intent(getContext(),camera.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        ImageView encendido = root.findViewById(R.id.lightON);
        encendido.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 Toast.makeText(getContext(),"Encendido",Toast.LENGTH_SHORT).show();
                 try{
                     MainActivity.client.publish("nuGWuCsEaAPeld7/exterior/luz","encender".getBytes(),2,false);
                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
        });
        ImageView apagado = root.findViewById(R.id.lightOFF);
        apagado.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(), "Apagado",Toast.LENGTH_SHORT).show();
                try{
                    MainActivity.client.publish("nuGWuCsEaAPeld7/exterior/luz","apagar".getBytes(),2,false);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        ImageView abierto = root.findViewById(R.id.openDoor);
        abierto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(),"Abierto",Toast.LENGTH_SHORT).show();
                try{
                    MainActivity.client.publish("nuGWuCsEaAPeld7/exterior/motor","abrir".getBytes(),2,false);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        ImageView cerrado = root.findViewById(R.id.closeDoor);
        cerrado.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(), "Cerrado",Toast.LENGTH_SHORT).show();
                try{
                    MainActivity.client.publish("nuGWuCsEaAPeld7/exterior/motor","cerrar".getBytes(),2,false);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        micButton = root.findViewById(R.id.record);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                Toast.makeText(getContext(), "Escuchando...",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                micButton.setImageResource(R.drawable.recording);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                try {
                    MainActivity.client.publish("nuGWuCsEaAPeld7/exterior/audio",data.get(0).getBytes(),2,false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    micButton.setImageResource(R.drawable.record_stop);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions((Activity) getContext(),new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText((Activity) getContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }
}
