package es.ucm.fdi.keeperly.ui.transaccion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.ucm.fdi.keeperly.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateTransaccionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTransaccionFragment extends Fragment {

    public CreateTransaccionFragment() {
        // Required empty public constructor
    }

    public static CreateTransaccionFragment newInstance() { return new CreateTransaccionFragment(); }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_transaccion, container, false);
        return view;
    }
}