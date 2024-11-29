package es.ucm.fdi.keeperly.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.PresupuestoDAO;
import es.ucm.fdi.keeperly.data.local.database.dao.TransaccionDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;
import es.ucm.fdi.keeperly.data.local.database.entities.Transaccion;

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

    public LiveData<List<Presupuesto>> getAllPresupuestos(int id_usuario) {
        return presupuestoDao.getPresupuestosByUsuario(id_usuario);
    }

    public Presupuesto getPresupuestoById(int id) {
        return presupuestoDao.getPresupuestoById(id);
    }

    public double getTotalGastado(Presupuesto presupuesto) {
        double totalGastado = 0.0;
        TransaccionDAO transaccionDAO = KeeperlyDB.getInstance().transaccionDao();
        List<Transaccion> transacciones = new ArrayList<>();
        //transacciones = transaccionDAO.getTransaccionesEntreDosFechas(presupuesto.getFechaInicio(), presupuesto.getFechaFin());

        for(Transaccion transaccion : transacciones) {
            totalGastado += transaccion.getCantidad();
        }

        return totalGastado;
    }
}
