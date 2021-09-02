package com.example.proyectose_2021is.ui.luces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectose_2021is.MainActivity;
import com.example.proyectose_2021is.R;
import com.example.proyectose_2021is.databinding.FragmentLucesBinding;

public class lucesFragment extends Fragment {

    private lucesViewModel lucesViewModel;
    private FragmentLucesBinding binding;
    /*MqttAndroidClient client;*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        lucesViewModel =
                new ViewModelProvider(this).get(lucesViewModel.class);

        binding = FragmentLucesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageView OFFSala = root.findViewById(R.id.lightOFFSala);
        ImageView ONSala = root.findViewById(R.id.lightONSala);
        controlLuces(ONSala, OFFSala, "nuGWuCsEaAPeld7/sala/luz");

        ImageView OFFCocina = root.findViewById(R.id.lightOFFCocina);
        ImageView ONCocina = root.findViewById(R.id.lightONCocina);
        controlLuces(ONCocina, OFFCocina, "nuGWuCsEaAPeld7/cocina/luz");

        ImageView OFFDorm = root.findViewById(R.id.lightOFFDorm);
        ImageView ONDorm = root.findViewById(R.id.lightONDorm);
        controlLuces(ONDorm, OFFDorm, "nuGWuCsEaAPeld7/dormitorio/luz");

        ImageView OFFBath = root.findViewById(R.id.lightOFFBath);
        ImageView ONBath= root.findViewById(R.id.lightONBath);
        controlLuces(ONBath, OFFBath, "nuGWuCsEaAPeld7/bano/luz");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void controlLuces(ImageView btnON, ImageView btnOFF, String sub_topic){
        String final_topic = MainActivity.TOPIC + sub_topic;
        btnON.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(),"Encendido",Toast.LENGTH_LONG).show();
                try{
                    MainActivity.client.publish(sub_topic,"encender".getBytes(),2,false);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        btnOFF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(), "Apagado",Toast.LENGTH_LONG).show();
                try{
                    MainActivity.client.publish(sub_topic,"apagar".getBytes(),2,false);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}