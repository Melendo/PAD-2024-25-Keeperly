package es.ucm.fdi.keeperly.ui.presupuestos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

import es.ucm.fdi.keeperly.repository.PresupuestoRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class PresupuestosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final PresupuestoRepository presupuestoRepository;

    public PresupuestosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is budgets fragment");
        this.presupuestoRepository = RepositoryFactory.getInstance().getPresupuestoRepository();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Integer> getOperationStatus() {
        return presupuestoRepository.getOperationStatus();
    }

    // Método para crear un nuevo presupuesto
    public void crearPresupuesto(String nombre,int usuario, int categoria, double cantidad, Date fechaInicio, Date fechaFin) {

        presupuestoRepository.insert(presupuestoRepository.construirPresupuesto(nombre, usuario , categoria, cantidad, fechaInicio, fechaFin)); // Insertamos el presupuesto en la base de datos
    }
}