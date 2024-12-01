package es.ucm.fdi.keeperly.ui.transaccion;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.databinding.FragmentTransaccionBinding;

public class TransaccionFragment extends Fragment {

    private TransaccionViewModel transaccionViewModel;
    private FragmentTransaccionBinding binding;
    private TransaccionAdapter transaccionAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaccion, container, false);

        FloatingActionButton fabCreateTransaccion = view.findViewById(R.id.fab_add_transaction);
        fabCreateTransaccion.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_nav_transaccion_to_createTransaccionFragment);
        });

        transaccionViewModel =  new ViewModelProvider(this, new TransaccionViewModelFactory()).get(TransaccionViewModel.class);

        binding = FragmentTransaccionBinding.inflate(inflater, container, false);

        RecyclerView recyclerView = binding.recyclerViewTransacciones;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        transaccionAdapter = new TransaccionAdapter();
        recyclerView.setAdapter(transaccionAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        transaccionViewModel = new ViewModelProvider(this).get(TransaccionViewModel.class);
        // TODO: Use the ViewModel
    }

}