package es.ucm.fdi.keeperly.service;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.PresupuestoDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;

public class PresupuestoSA {
    private final PresupuestoDAO presupuestoDao;
    private final ExecutorService executorService;

    public PresupuestoSA(Context context) {
        KeeperlyDB db = KeeperlyDB.getInstance(context);
        presupuestoDao = db.presupuestoDao();
        executorService = Executors.newSingleThreadExecutor();
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
