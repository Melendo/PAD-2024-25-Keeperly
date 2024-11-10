package es.ucm.fdi.keeperly.integracion.daos;

import androidx.room.*;

import java.util.List;

import es.ucm.fdi.keeperly.integracion.entities.Usuario;

@Dao
public interface UsuarioDAO {
    @Insert
    void insert(Usuario usuario);

    @Update
    void update(Usuario usuario);

    @Delete
    void delete(Usuario usuario);

    @Query("SELECT * FROM usuarios")
    List<Usuario> getAllUsuarios();

    @Query("SELECT * FROM usuarios WHERE id = :id")
    Usuario getUsuarioById(int id);
}
