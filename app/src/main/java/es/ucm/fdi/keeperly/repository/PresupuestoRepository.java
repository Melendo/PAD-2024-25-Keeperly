package es.ucm.fdi.keeperly.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.PresupuestoDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;

public class PresupuestoRepository {

    private final PresupuestoDAO presupuestoDao;
    private final ExecutorService executorService;
    private final MutableLiveData<Integer> operationStatus = new MutableLiveData<>();

    public LiveData<Integer> getOperationStatus() {
        return operationStatus;
    }

    public PresupuestoRepository() {
        presupuestoDao = KeeperlyDB.getInstance().presupuestoDao();
        executorService = Executors.newSingleThreadExecutor();
    }


    public void insert(Presupuesto presupuesto) {

        if (presupuesto.getCantidad() > 0) {
            if (presupuesto.getFechaInicio().before(presupuesto.getFechaFin())) {
                executorService.execute(() -> {
                    try {
                        presupuestoDao.insert(presupuesto);
                        operationStatus.postValue(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        operationStatus.postValue(-1);
                    }
                });
            } else {
                operationStatus.postValue(-3);
            }

        } else {
            operationStatus.postValue(-2);
        }
    }

    public void update(Presupuesto presupuesto) {
        executorService.execute(() -> presupuestoDao.update(presupuesto));
    }

    public void delete(Presupuesto presupuesto) {
        executorService.execute(() -> presupuestoDao.delete(presupuesto));
    }

    public List<Presupuesto> getAllPresupuestos(int id_usuario) {
        return presupuestoDao.getPresupuestosByUsuario(id_usuario);
    }

    public Presupuesto getPresupuestoById(int id) {
        return presupuestoDao.getPresupuestoById(id);
    }

    public Presupuesto construirPresupuesto(String nombre, int usuario, int categoria, double cantidad, Date fechaInicio, Date fechaFin) {
        // Aquí puedes procesar los datos y pasarlos al repositorio para que se guarden en la base de datos
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setNombre(nombre);
        presupuesto.setIdUsuario(usuario);
        presupuesto.setIdCategoria(categoria);
        presupuesto.setCantidad(cantidad);
        presupuesto.setFechaInicio(fechaInicio);
        presupuesto.setFechaFin(fechaFin);
        presupuesto.setGastado(0.0);

        return presupuesto;
    }
}
