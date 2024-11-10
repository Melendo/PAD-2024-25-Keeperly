package es.ucm.fdi.keeperly.integracion.entities;

import androidx.room.*;

@Entity(
        tableName = "presupuestos",
        foreignKeys = {
                @ForeignKey(entity = Usuario.class, parentColumns = "id", childColumns = "idUsuario", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Categoria.class, parentColumns = "id", childColumns = "idCategoria")
        }
)
public class Presupuesto {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idUsuario;
    private String nombre;
    private int idCategoria;
    private double cantidad;
    private double gastado;
    private String fechaInicio;
    private String fechaFin;

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getGastado() {
        return gastado;
    }

    public void setGastado(double gastado) {
        this.gastado = gastado;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}
