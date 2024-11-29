package es.ucm.fdi.keeperly.ui.transaccion;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.icu.text.DateFormat;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.databinding.FragmentCreateTransaccionBinding;
import es.ucm.fdi.keeperly.ui.categorias.CategoriasViewModel;
import es.ucm.fdi.keeperly.ui.cuenta.CuentaSpinnerAdapter;
import es.ucm.fdi.keeperly.ui.cuenta.CuentasViewModelFactory;
import es.ucm.fdi.keeperly.ui.cuenta.CuentasViewModel;

public class CreateTransaccionFragment extends Fragment {

    private TransaccionViewModel transaccionViewModel;
    private CategoriasViewModel categoriasViewModel;
    private CuentasViewModel cuentasViewModel;

    private String categoriaSeleccionada;
    private int cuentaSeleccionadaID;

    private List<Cuenta> cuentasLista = new ArrayList<>();

    public CreateTransaccionFragment() {
        // Required empty public constructor
    }

    public static CreateTransaccionFragment newInstance() { return new CreateTransaccionFragment(); }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_transaccion, container, false);

        // Inicializar el ViewModel
        transaccionViewModel = new ViewModelProvider(this, new TransaccionViewModelFactory()).get(TransaccionViewModel.class);

        // Obtener referencias a los elementos de la interfaz de usuario
        final EditText conceptoEditText = view.findViewById(R.id.conceptoEditText);
        final EditText cantidadEditText = view.findViewById(R.id.cantidadEditText);
        final Spinner cuentaSpinner = view.findViewById(R.id.cuentaSpinner);
        final Spinner categoriaSpinner = view.findViewById(R.id.categoriaSpinner);
        final EditText fechaField = view.findViewById(R.id.fechaField);
        final Button crearButton = view.findViewById(R.id.saveTransactionButton);
        final Button cancelarButton = view.findViewById(R.id.cancelTransactionButton);

        // Configurar el ViewModel de Categorías
        categoriasViewModel = new ViewModelProvider(this).get(CategoriasViewModel.class);
        categoriasViewModel.getCategorias().observe(getViewLifecycleOwner(), categorias -> {
            // Configurar el adaptador para el Spinner de Categorías
            if (categorias != null) {
                // Crea una lista de nombres de categorías
                List<String> nombresCategorias = new ArrayList<>();
                nombresCategorias.add(0, "Selecciona una categoría");
                for (Categoria categoria : categorias) {
                    nombresCategorias.add(categoria.getNombre());
                }

                // Configura el adaptador
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        nombresCategorias
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categoriaSpinner.setAdapter(adapter);
            }
        });

        categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    categoriaSeleccionada = adapterView.getItemAtPosition(i).toString();
                } else {
                    categoriaSeleccionada = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //ignore
            }
        });

        // Configurar el ViewModel de Cuentas
        cuentasViewModel = new ViewModelProvider(this, new CuentasViewModelFactory())
                .get(CuentasViewModel.class);
        cuentasViewModel.getCuentas().observe(getViewLifecycleOwner(), cuentas -> {
            // Crea una lista de nombre de cuentas
            Cuenta aux = new Cuenta();
            aux.setBalance(0.0);
            aux.setNombre("Selecciona una cuenta");
            aux.setIdUsuario(-1);

            cuentasLista.add(0, aux);
            cuentasLista.addAll(cuentas);

            //Configurar el adaptador
            CuentaSpinnerAdapter adapter = new CuentaSpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter.setData(cuentasLista);
            cuentaSpinner.setAdapter(adapter);
        });

        cuentaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCuentaName = adapterView.getItemAtPosition(i).toString();
                Cuenta selectedCuenta = null;
                for (Cuenta cuenta : cuentasLista) {
                    if (cuenta.getNombre().equals(selectedCuentaName)) {
                        selectedCuenta = cuenta;
                        break;
                    }
                }

                if (selectedCuenta != null && selectedCuenta.getId() != -1)
                    cuentaSeleccionadaID = selectedCuenta.getId();
                else
                    cuentaSeleccionadaID = -1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //ignore
            }
        });

        // Fecha
        fechaField.setOnClickListener(v -> showDatePickerDialog(fechaField));

        // Boton de crear transaccion
        crearButton.setOnClickListener(v -> {
            if (conceptoEditText.getText().toString().isEmpty()) {
                conceptoEditText.setError("El concepto es obligatorio");
            } else if (cantidadEditText.getText().toString().isEmpty()) {
                cantidadEditText.setError("La cantidad es obligatoria");
            } else if (fechaField.getText().toString().isEmpty()) {
                fechaField.setError("La fecha es obligatoria");
            } else {
                // Obtener los datos del formulario
                String concepto = conceptoEditText.getText().toString();
                double cantidad = Double.parseDouble(cantidadEditText.getText().toString());

                int idcategoria = categoriasViewModel.getCategoriaByNombre(categoriaSeleccionada).getId();
                int idcuenta = cuentaSeleccionadaID;

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    Date fecha = dateFormat.parse(fechaField.getText().toString());
                    transaccionViewModel.createTransaccion(concepto, cantidad, idcuenta, idcategoria, fecha);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        cancelarButton.setOnClickListener(v -> {
            conceptoEditText.setText("");
            cantidadEditText.setText("");
            cuentaSpinner.setSelection(0);
            categoriaSpinner.setSelection(0);
            fechaField.setText("");
            Toast.makeText(requireContext(), "Operacion cancelada", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        });

        // Observar el estado del formulario
        transaccionViewModel.getCreateTransaccionFormState().observe(getViewLifecycleOwner(), createTransaccionFormState -> {
           if (createTransaccionFormState == null)
               return;
           crearButton.setEnabled(createTransaccionFormState.isDataValid());
           if(createTransaccionFormState.isDataValid())
               fechaField.setError(null);
           if (createTransaccionFormState.getConceptoError() != null) {
               conceptoEditText.setError(getString(createTransaccionFormState.getConceptoError()));
           }
           if (createTransaccionFormState.getCantidadError() != null) {
               cantidadEditText.setError(getString(createTransaccionFormState.getCantidadError()));
           }
           if (createTransaccionFormState.getFechaError() != null) {
               fechaField.setError(getString(createTransaccionFormState.getFechaError()));
           }
        });

        transaccionViewModel.getCreateTransaccionResult().observe(getViewLifecycleOwner(), createTransaccionResult -> {
            if (createTransaccionResult == null) {
                return;
            }
            if (createTransaccionResult.getError() != null) {
                Toast.makeText(requireContext(), "Error al crear la transaccion", Toast.LENGTH_SHORT).show();
            }
            if (createTransaccionResult.getSuccess() != null) {
                Toast.makeText(requireContext(), "Transaccion creada", Toast.LENGTH_SHORT).show();
                conceptoEditText.setText("");
                cantidadEditText.setText("");
                cuentaSpinner.setSelection(0);
                categoriaSpinner.setSelection(0);
                fechaField.setText("");
                getParentFragmentManager().popBackStack();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int idCuenta = cuentaSeleccionadaID;
                int idCategoria;
                if (!Objects.equals(categoriaSeleccionada, ""))
                    idCategoria = categoriasViewModel.getCategoriaByNombre(categoriaSeleccionada).getId();
                else
                    idCategoria = -1;

                double cantidad;
                if (cantidadEditText.getText().toString().isEmpty() || cantidadEditText.getText().toString().equals("-"))
                    cantidad = -1;
                else
                    cantidad = Double.parseDouble(cantidadEditText.getText().toString());

                Date fecha;
                if (fechaField.getText().toString().isEmpty() || fechaField.getText().toString().equals("0"))
                    fecha = null;
                else {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        fecha = dateFormat.parse(fechaField.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }

                transaccionViewModel.createTransaccionDataChanged(conceptoEditText.getText().toString(),
                        cantidad,
                        idCuenta,
                        idCategoria,
                        fecha);
            }
        };

        conceptoEditText.addTextChangedListener(afterTextChangedListener);
        cantidadEditText.addTextChangedListener(afterTextChangedListener);
        fechaField.addTextChangedListener(afterTextChangedListener);

        return view;
    }

    private void showDatePickerDialog(EditText editText) {
        // Obtener la fecha actual
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, month1, dayOfMonth) -> {
                    // Formatear la fecha seleccionada
                    String fechaSeleccionada = String.format("%02d-%02d-%04d", dayOfMonth, month1 + 1, year1);
                    editText.setText(fechaSeleccionada);
                },
                year, month, day
        );


        // Mostrar el diálogo
        datePickerDialog.show();
    }
}