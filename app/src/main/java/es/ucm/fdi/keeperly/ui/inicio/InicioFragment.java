package es.ucm.fdi.keeperly.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import es.ucm.fdi.keeperly.databinding.FragmentInicioBinding;

public class InicioFragment extends Fragment {

    private FragmentInicioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        InicioViewModel inicioViewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView welcomeText = binding.welcomeText;
        inicioViewModel.getWelcomeText().observe(getViewLifecycleOwner(), welcomeText::setText);
        final TextView priceThisMonth = binding.numDineroTotal;
        inicioViewModel.getPriceThisMonth().observe(getViewLifecycleOwner(), priceThisMonth::setText);
        final TextView priceLastMonth = binding.numGastadoTotal;
        inicioViewModel.getPriceLastMonth().observe(getViewLifecycleOwner(), priceLastMonth::setText);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}