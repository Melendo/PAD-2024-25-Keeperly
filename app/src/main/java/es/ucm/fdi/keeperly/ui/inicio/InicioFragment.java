package es.ucm.fdi.keeperly.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private PresupuestosInicioAdapter presupuestosInicioAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Declaración del ViewModel
        inicioViewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        //Binding del fragment
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //RecyclerView de cuentas
        RecyclerView recyclerViewCuentas = root.findViewById(R.id.recyclerViewCuentas);
        recyclerViewCuentas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCuentas.setHasFixedSize(true);

        cuentasInicioAdapter = new CuentasInicioAdapter();
        recyclerViewCuentas.setAdapter(cuentasInicioAdapter);

        //RecyclerView de presupuestos
        RecyclerView recyclerViewPresupuestos = root.findViewById(R.id.recyclerViewPresupuestos);
        recyclerViewPresupuestos.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPresupuestos.setHasFixedSize(true);

        presupuestosInicioAdapter = new PresupuestosInicioAdapter();
        recyclerViewPresupuestos.setAdapter(presupuestosInicioAdapter);

        //Observadores del ViewModel
        inicioViewModel.getCuentas().observe(getViewLifecycleOwner(), cuentasInicioAdapter::setCuentas);
        inicioViewModel.getPresupuestos().observe(getViewLifecycleOwner(), presupuestosInicioAdapter::setPresupuestos);


        //Observadores de los campos del fragment
        final TextView welcomeText = binding.welcomeText;
        inicioViewModel.getWelcomeText().observe(getViewLifecycleOwner(), welcomeText::setText);

        final TextView priceThisMonth = binding.numDineroTotal;
        inicioViewModel.getNumDineroTotal().observe(getViewLifecycleOwner(), priceThisMonth::setText);

        final TextView priceLastMonth = binding.numGastadoTotal;
        inicioViewModel.getNumTotalGastado().observe(getViewLifecycleOwner(), priceLastMonth::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}