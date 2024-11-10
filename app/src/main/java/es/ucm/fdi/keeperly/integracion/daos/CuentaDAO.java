package es.ucm.fdi.keeperly.integracion.daos;

import androidx.room.*;

import java.util.List;

import es.ucm.fdi.keeperly.integracion.entities.Cuenta;

@Dao
public interface CuentaDAO {
    @Insert
    void insert(Cuenta cuenta);

    @Update
    void update(Cuenta cuenta);

    @Delete
    void delete(Cuenta cuenta);

    @Query("SELECT * FROM cuentas WHERE idUsuario = :userId")
    List<Cuenta> getCuentasByUsuario(int userId);

    @Query("SELECT * FROM cuentas WHERE idUsuario = :id")
    Cuenta getCuentaById(int id);
}
