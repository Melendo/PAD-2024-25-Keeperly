package es.ucm.fdi.keeperly.ui.presupuestos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;
import java.util.List;

import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;
import es.ucm.fdi.keeperly.repository.CategoriaRepository;
import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.repository.PresupuestoRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class PresupuestosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final PresupuestoRepository presupuestoRepository;
    private final CategoriaRepository categoriaRepository;
    private final LoginRepository loginRepository;
    private final LiveData<List<Presupuesto>> presupuestos;

    public PresupuestosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is budgets fragment");
        this.loginRepository = LoginRepository.getInstance(RepositoryFactory.getInstance().getUsuarioRepository());
        this.presupuestoRepository = RepositoryFactory.getInstance().getPresupuestoRepository();
        this.presupuestos = presupuestoRepository.getAllPresupuestos(loginRepository.getLoggedUser().getId());
        this.categoriaRepository = RepositoryFactory.getInstance().getCategoriaRepository();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Integer> getOperationStatus() {
        return presupuestoRepository.getOperationStatus();
    }

    public LiveData<Boolean> getDeleteStatus() {
        return presupuestoRepository.getDeleteStatus();
    }

    public LiveData<Integer> getUpdateStatus() {
        return presupuestoRepository.getUpdateStatus();
    }

    public LiveData<List<Presupuesto>> getPresupuestos() {
        return presupuestos;
    }

    public double calcularGastado(Presupuesto presupuesto) {
        return presupuestoRepository.getTotalGastado(presupuesto);
    }
    // Método para crear un nuevo presupuesto
    public void crearPresupuesto(String nombre, int categoria, double cantidad, Date fechaInicio, Date fechaFin) {
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setNombre(nombre);
        presupuesto.setIdUsuario(loginRepository.getLoggedUser().getId());
        presupuesto.setIdCategoria(categoria);
        presupuesto.setCantidad(cantidad);
        presupuesto.setFechaInicio(fechaInicio);
        presupuesto.setFechaFin(fechaFin);
        presupuesto.setGastado(0.0);

        presupuestoRepository.insert(presupuesto);
    }

    public String getNombreCategoria(int id_categoria) {
        return categoriaRepository.getCategoriaById(id_categoria).getNombre();
    }

    public double getTotalGastadoEnPresupuesto(Presupuesto presupuesto) {
        return presupuestoRepository.getTotalGastado(presupuesto);
    }

    public void eliminarPresupuesto(Presupuesto presupuesto) {
        presupuestoRepository.delete(presupuesto);
    }

    public void update(Presupuesto presupuesto) {
        presupuesto.setIdUsuario(loginRepository.getLoggedUser().getId());
        presupuestoRepository.update(presupuesto);
    }
}