package es.ucm.fdi.keeperly.data.local.database.entities;

import androidx.room.*;

import java.util.Date;

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
    private String concepto;
    private double cantidad;
    private Date fecha;

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

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
