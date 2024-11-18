package es.ucm.fdi.keeperly.data.local.database.entities;

import androidx.room.*;

@Entity(
        tableName = "transacciones",
        foreignKeys = {
                @ForeignKey(entity = Categoria.class, parentColumns = "id", childColumns = "idCategoria"),
                @ForeignKey(entity = Cuenta.class, parentColumns = "id", childColumns = "idCuenta")
        }
)
public class Transaccion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idCategoria;
    private int idCuenta;
    private boolean tipo; // true: ingreso, false: gasto
    private double cantidad;
    private String fecha;

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
