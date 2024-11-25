package es.ucm.fdi.keeperly.ui.cuenta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class CreateAccountFragment extends Fragment {
    private CreateAccountViewModel createAccountViewModel;
    private EditText nombre;
    private EditText balance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crearCuenta, container, false);
        //Vincula las vistas
        nombre = view.findViewById(R.id.accountNameEditText);
        balance = view.findViewById(R.id.balanceEditText);
        Button crear = view.findViewById(R.id.createAccountButton);
        Button cancelar = view.findViewById(R.id.cancelButton);
        //Inicializacion
        createAccountViewModel = new ViewModelProvider(this, new CreateAccountViewModelFactory()).get(CreateAccountViewModel.class);
        //Cambios en el formulario
        createAccountViewModel.getCuentaFormState().observe(getViewLifecycleOwner(), formState -> {
            if (formState == null) {
                return;
            }
            //Si  los datos mantienen bien el formato se activa el botond e crear
            crear.setEnabled(formState.isDataValid());

            if (formState.getNameError() != null) {
                nombre.setError(getString(formState.getNameError()));
            }
            if (formState.getBalanceError() != null) {
                balance.setError(getString(formState.getBalanceError()));
            }
        });
        //Resultado del formulario
        createAccountViewModel.getCuentaResult().observe(getViewLifecycleOwner(), result -> {

        });
        //Boton crear cuenta
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                String nombreC = nombre.getText().toString();
                String textoBalance = balance.getText().toString();
                double balanceC = textoBalance.isEmpty() ? 0 : Double.parseDouble(textoBalance);
                //Obtiene el id del usuario logeado
                //int idUsuario = LoginRepository.getInstance(RepositoryFactory.getInstance().getUsuarioRepository());
                //createAccountViewModel.createAccount(nombreC, balanceC, idUsuario);

            }
        });
        //Boton cancelar
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                requireActivity().onBackPressed(); //Retrocede a la pantalla anterior
            }
        });

        return view;
    }
}
