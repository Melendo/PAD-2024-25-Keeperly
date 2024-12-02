package es.ucm.fdi.keeperly.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.PresupuestoDAO;
import es.ucm.fdi.keeperly.data.local.database.dao.TransaccionDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;
import es.ucm.fdi.keeperly.data.local.database.entities.Transaccion;

public class PresupuestoRepository {

    private final PresupuestoDAO presupuestoDao;
    private final TransaccionDAO transaccionDAO;

    private final ExecutorService executorService;
    private final MutableLiveData<Integer> operationStatus = new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleteStatus = new MutableLiveData<>();
    private final MutableLiveData<Integer> updateStatus = new MutableLiveData<>();

    public LiveData<Integer> getOperationStatus() {
        return operationStatus;
    }

    public PresupuestoRepository() {
        presupuestoDao = KeeperlyDB.getInstance().presupuestoDao();
        transaccionDAO = KeeperlyDB.getInstance().transaccionDao();

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
        if (presupuesto.getCantidad() > 0) {
            if (presupuesto.getFechaInicio().before(presupuesto.getFechaFin())) {
                executorService.execute(() -> {
                    try {
                        executorService.execute(() -> presupuestoDao.update(presupuesto));
                        updateStatus.postValue(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        updateStatus.postValue(-1);
                    }
                });
            } else {
                updateStatus.postValue(-3);
            }

        } else {
            updateStatus.postValue(-2);
        }

    }

    public void delete(Presupuesto presupuesto) {
        try {
            executorService.execute(() -> presupuestoDao.delete(presupuesto));
            deleteStatus.postValue(true);
        } catch (Exception e) {
            e.printStackTrace();
            deleteStatus.postValue(false);
        }
    }

    public LiveData<List<Presupuesto>> getAllPresupuestos(int id_usuario) {
        return presupuestoDao.getPresupuestosByUsuario(id_usuario);
    }

    public Presupuesto getPresupuestoById(int id) {
        return presupuestoDao.getPresupuestoById(id);
    }

    public double getTotalGastado(Presupuesto presupuesto) {
        double totalGastado = 0.0;

        List<Transaccion> transacciones = transaccionDAO.obtenerTransaccionesEntreFechasDirect(presupuesto.getFechaInicio(), presupuesto.getFechaFin(), presupuesto.getIdCategoria());

        for (Transaccion transaccion : transacciones) {
            totalGastado += transaccion.getCantidad();
        }

        Presupuesto presupuesto1 = presupuestoDao.getPresupuestoById(presupuesto.getId());
        if(presupuesto1.getGastado() != totalGastado){
            presupuesto1.setGastado(totalGastado);
            presupuestoDao.update(presupuesto1);
        }

        return totalGastado != 0 ? totalGastado * (-1) : totalGastado;
    }

    public LiveData<List<Transaccion>> getTransaccionesDePresupuesto(Presupuesto presupuesto) {
        return transaccionDAO.obtenerTransaccionesEntreFechas(presupuesto.getFechaInicio(), presupuesto.getFechaFin(), presupuesto.getIdCategoria());
    }

    public LiveData<Boolean> getDeleteStatus() {
        return deleteStatus;
    }

    public LiveData<Integer> getUpdateStatus() {
        return updateStatus;
    }
}
