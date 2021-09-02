package com.example.proyectose_2021is.ui.voz;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectose_2021is.MainActivity;
import com.example.proyectose_2021is.R;
import com.example.proyectose_2021is.databinding.FragmentVozBinding;

import java.util.ArrayList;
import java.util.Locale;

public class vozFragment extends Fragment {

    private vozViewModel vozViewModel;
    private FragmentVozBinding binding;

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private ImageView micButton;
    String comandos_on[] = {"prender","encender","on","prende","ilumina","enciende"};
    String comandos_off[] = {"apagar","apaga","off","quita","quitar","oscurece"};
    String comandos_open[] = {"abrir","open","abre","ábrete","saca","sacar","quitar","quita"};
    String comandos_close[] = {"cerrar","cierra","pon","ponle"};
    String funciones_luz[] = {"luz", "foco","iluminacion","alumbrado"};
    String funciones_seg[] = {"cerrojo", "cerradura","puerta","seguro","llave","door"};
    String lugares_sala[] = {"sala","salón","recibidor","estancia","living"};
    String lugares_cocina[] = {"cocina","comedor","kitchen","cook","cocinar"};
    String lugares_dormitorio[] = {"dormitorio","cuarto","habitación","alcoba","recámara","aposento","cama"};
    String lugares_bano[] = {"baño","bañera","tina","servicio","tocador","ducha","aseo"};
    String lugares_exterior[] = {"afuera","puerta","exterior","fuera"};
    String todo[] = {"todo","todos","casa","hogar","luces"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vozViewModel =
                new ViewModelProvider(this).get(vozViewModel.class);

        binding = FragmentVozBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        editText = root.findViewById(R.id.text);
        micButton = root.findViewById(R.id.button);
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
                Toast.makeText(getContext(), "Escuchando...",Toast.LENGTH_SHORT).show();
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
                Log.e("APP",data.get(0));
                funcion(data.get(0).toLowerCase());
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

    public void funcion(String voiceMessage){
        String topic = "nuGWuCsEaAPeld7/"+lugar(voiceMessage)+"/"+tipo(voiceMessage);
        if(lugar(voiceMessage)!=null && tipo(voiceMessage)!=null){
            try{
                MainActivity.client.publish(topic,comando(voiceMessage).getBytes(),2,false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getContext(), "Repita, por favor.",Toast.LENGTH_SHORT).show();
        }

    }

    public boolean comparar(String[] lista, String mensaje){
        for(String elemento: lista){
            if(mensaje.contains(elemento)){
                return true;
            }
        }
        return false;
    }

    public String lugar(String mensaje){
        if(comparar(lugares_bano,mensaje)){
            return "bano";
        }else if(comparar(lugares_cocina,mensaje)){
            return "cocina";
        }else if(comparar(lugares_sala,mensaje)){
            return "sala";
        }else if(comparar(lugares_dormitorio,mensaje)){
            return "dormitorio";
        }else if(comparar(lugares_exterior,mensaje)){
            return "exterior";
        }else if(comparar(todo,mensaje)){
            return "todo";
        }
        return null;
    }

    public String tipo(String mensaje){
        if(comparar(funciones_luz,mensaje)||comparar(comandos_on,mensaje)||comparar(comandos_off,mensaje)){
            return "luz";
        }else if(comparar(funciones_seg,mensaje)||comparar(comandos_open,mensaje)||comparar(comandos_close,mensaje)){
            return "motor";
        }
        return null;
    }

    public String comando(String mensaje){
        if(comparar(comandos_on,mensaje)){
            return "encender";
        }else if(comparar(comandos_off,mensaje)){
            return "apagar";
        }else if(comparar(comandos_open,mensaje)){
            return "abrir";
        }else if(comparar(comandos_close,mensaje)){
            return "cerrar";
        }
        return null;
    }
}