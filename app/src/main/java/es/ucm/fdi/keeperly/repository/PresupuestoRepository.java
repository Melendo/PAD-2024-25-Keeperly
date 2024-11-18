package es.ucm.fdi.keeperly.repository;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.PresupuestoDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;

public class PresupuestoRepository {
    private static volatile PresupuestoRepository instance;

    private final PresupuestoDAO presupuestoDao;
    private final ExecutorService executorService;

    private PresupuestoRepository() {
        presupuestoDao = KeeperlyDB.getInstance().presupuestoDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public static PresupuestoRepository getInstance() {
        if (instance == null)
            instance = new PresupuestoRepository();
        return instance;
    }

    public void insert(Presupuesto presupuesto) {
        executorService.execute(() -> presupuestoDao.insert(presupuesto));
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
}
