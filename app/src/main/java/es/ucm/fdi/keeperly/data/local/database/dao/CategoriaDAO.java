package es.ucm.fdi.keeperly.data.local.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;

@Dao
public interface CategoriaDAO {
    @Insert
    void insert(Categoria categoria);

    @Update
    void update(Categoria categoria);

    @Delete
    void delete(Categoria categoria);

    @Query("SELECT * FROM categorias")
    LiveData<List<Categoria>> getAllCategorias();

    @Query("SELECT * FROM categorias WHERE id_usuario =:id_usu")
    LiveData<List<Categoria>> getAllCategoriasDeUsuario(int id_usu);

    @Query ("SELECT * FROM categorias WHERE id =:id")
    Categoria getCategoriaById(int id);

    @Query("SELECT * FROM categorias WHERE id_usuario =:id_usu AND nombre =:nombre")
    Categoria getCategoriaDeUsuarioPorNombre(int id_usu, String nombre);

    @Query ("SELECT * FROM categorias WHERE nombre =:nombre")
    Categoria getCategoriaByNombre(String nombre);

    @Query("SELECT categorias.nombre FROM categorias JOIN transacciones ON categorias.id = transacciones.idCategoria WHERE transacciones.id = :idTransaccion")
    String getCategoriadeTransaccion(int idTransaccion);
}
