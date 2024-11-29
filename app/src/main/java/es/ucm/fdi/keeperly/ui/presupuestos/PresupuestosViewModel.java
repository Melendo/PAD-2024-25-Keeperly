package es.ucm.fdi.keeperly.ui.presupuestos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;
import java.util.List;

import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;
import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.repository.PresupuestoRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class PresupuestosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final PresupuestoRepository presupuestoRepository;
    private final LoginRepository loginRepository;
    private final LiveData<List<Presupuesto>> presupuestos;

    public PresupuestosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is budgets fragment");
        this.loginRepository = LoginRepository.getInstance(RepositoryFactory.getInstance().getUsuarioRepository());
        this.presupuestoRepository = RepositoryFactory.getInstance().getPresupuestoRepository();
        this.presupuestos = presupuestoRepository.getAllPresupuestos(loginRepository.getLoggedUser().getId());
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Integer> getOperationStatus() {
        return presupuestoRepository.getOperationStatus();
    }
    public LiveData<List<Presupuesto>> getPresupuestos() {
        return presupuestos;
    }


    // Método para crear un nuevo presupuesto
    public void crearPresupuesto(String nombre, int categoria, double cantidad, Date fechaInicio, Date fechaFin) {
        LoginRepository loginRepository = LoginRepository.getInstance(RepositoryFactory.getInstance().getUsuarioRepository());

        presupuestoRepository.insert(presupuestoRepository.construirPresupuesto(nombre, loginRepository.getLoggedUser().getId() , categoria, cantidad, fechaInicio, fechaFin)); // Insertamos el presupuesto en la base de datos
    }
}