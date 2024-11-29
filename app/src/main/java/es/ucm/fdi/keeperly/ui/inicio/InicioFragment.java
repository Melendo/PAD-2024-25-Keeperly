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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.databinding.FragmentInicioBinding;

public class InicioFragment extends Fragment {

    private InicioViewModel inicioViewModel;
    private FragmentInicioBinding binding;
    private CuentasInicioAdapter cuentasInicioAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerViewCuentas = root.findViewById(R.id.recyclerViewCuentas);
        recyclerViewCuentas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCuentas.setHasFixedSize(true);

        cuentasInicioAdapter = new CuentasInicioAdapter();
        recyclerViewCuentas.setAdapter(cuentasInicioAdapter);

        inicioViewModel.getCuentas().observe(getViewLifecycleOwner(), cuentasInicioAdapter::setCuentas);


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