package es.ucm.fdi.keeperly.data.local.database.dao;

import androidx.room.*;

import java.util.Collection;
import java.util.List;

import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;

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

    @Query("SELECT * FROM presupuestos WHERE idCategoria = :id")
    List<Categoria> getPresupuestosPorCategoria(int id);
}
