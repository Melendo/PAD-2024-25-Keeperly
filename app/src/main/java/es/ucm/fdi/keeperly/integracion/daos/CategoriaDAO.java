package es.ucm.fdi.keeperly.integracion.daos;

import androidx.room.*;

import java.util.List;

import es.ucm.fdi.keeperly.integracion.entities.Categoria;

@Dao
public interface CategoriaDAO {
    @Insert
    void insert(Categoria categoria);

    @Update
    void update(Categoria categoria);

    @Delete
    void delete(Categoria categoria);

    @Query("SELECT * FROM categorias")
    List<Categoria> getAllCategorias();

    @Query ("SELECT * FROM categorias WHERE id =:id")
    Categoria getCategoriaById(int id);
}
