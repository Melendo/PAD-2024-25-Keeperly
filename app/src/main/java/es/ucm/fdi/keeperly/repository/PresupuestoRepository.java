package es.ucm.fdi.keeperly.repository;

import android.content.Context;

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

    public PresupuestoRepository() {
        presupuestoDao = KeeperlyDB.getInstance().presupuestoDao();
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

    public Presupuesto construirPresupuesto(String nombre,int usuario, int categoria, double cantidad, Date fechaInicio, Date fechaFin){
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
