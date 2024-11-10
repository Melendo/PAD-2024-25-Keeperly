package es.ucm.fdi.keeperly.integracion.daos;

import androidx.room.*;

import java.util.List;

import es.ucm.fdi.keeperly.integracion.entities.Presupuesto;

@Dao
public interface PresupuestoDAO {
    @Insert
    void insert(Presupuesto presupuesto);

    @Update
    void update(Presupuesto presupuesto);

    @Delete
    void delete(Presupuesto presupuesto);

    @Query("SELECT * FROM presupuestos WHERE idUsuario = :userId")
    List<Presupuesto> getPresupuestosByUsuario(int userId);

    @Query("SELECT * FROM presupuestos WHERE id = :id")
    Presupuesto getPresupuestoById(int id);
}
