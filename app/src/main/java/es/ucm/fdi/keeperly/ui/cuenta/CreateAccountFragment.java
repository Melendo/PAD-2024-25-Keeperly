package es.ucm.fdi.keeperly.ui.cuenta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Usuario;
import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class CreateAccountFragment extends Fragment {
    private CreateAccountViewModel createAccountViewModel;
    private EditText nombre;
    private EditText balance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_cuenta, container, false);
        //Vincula las vistas
        nombre = view.findViewById(R.id.accountNameEditText);
        balance = view.findViewById(R.id.balanceEditText);
        Button crear = view.findViewById(R.id.createAccountButton);
        Button cancelar = view.findViewById(R.id.cancelButton);
        //Inicializacion
        createAccountViewModel = new ViewModelProvider(this, new CreateAccountViewModelFactory()).get(CreateAccountViewModel.class);
        //Cambios en el formulario
        createAccountViewModel.getCuentaFormState().observe(getViewLifecycleOwner(), formState -> {
            //No se ha inicializado
            if (formState == null) {
                return;
            }
            //Si los datos son validos se activa el boton de crear
            crear.setEnabled(formState.isDataValid());
            //Muestra el mensaje de error si el nombre no es valido
            if (formState.getNameError() != null) {
                nombre.setError(getString(R.string.invalid_accountname));
            }
            //Muestra el mensaje de error si el balance no es valido
            if (formState.getBalanceError() != null) {
                balance.setError(getString(R.string.invalid_balance));
            }
        });
        //Resultado del formulario
        createAccountViewModel.getCuentaResult().observe(getViewLifecycleOwner(), result -> {
            //Si no hay resultado
            if (result == null) {
                return;
            }
            //Si el resultado tiene un error
            if (result.getError() != null) {
                Toast.makeText(requireContext(), getString(R.string.create_account_failed), Toast.LENGTH_SHORT).show();
            }
            //Si se crea con exito la cuenta
            if (result.getSuccess() != null) {
                Toast.makeText(requireContext(), getString(R.string.success_account_creation), Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed(); // Regresa a la pantalla anterior
            }
        });
        //Boton crear cuenta
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                String nombreC = nombre.getText().toString();
                String textoBalance = balance.getText().toString();
                double balanceC = textoBalance.isEmpty() ? 0 : Double.parseDouble(textoBalance);
                //Obtiene el id del usuario logeado
                Usuario loggedUser = LoginRepository.getInstance(RepositoryFactory.getInstance().getUsuarioRepository()).getLoggedUser();
                int idUsuario = loggedUser.getId();
                createAccountViewModel.createAccount(nombreC, balanceC, idUsuario);
                getParentFragmentManager().popBackStack(); // Redirige a la vista anterior

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
