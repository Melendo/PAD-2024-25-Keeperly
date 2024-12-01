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
        transaccionViewModel =  new ViewModelProvider(this, new TransaccionViewModelFactory()).get(TransaccionViewModel.class);

        binding = FragmentTransaccionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton fabCreateTransaccion = root.findViewById(R.id.fab_add_transaction);
        fabCreateTransaccion.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_nav_transaccion_to_createTransaccionFragment);
        });

        RecyclerView recyclerView = binding.recyclerViewTransacciones;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (transaccionAdapter == null) { // Prevent re-initialization
            transaccionAdapter = new TransaccionAdapter();
        }
        recyclerView.setAdapter(transaccionAdapter);

        transaccionViewModel.getTransacciones().observe(getViewLifecycleOwner(), transaccionAdapter::setTransacciones);
        //On Click Listeners

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}