package com.example.skynav.POJO;

public class VehiculoAereo {
    private String nombre;
    private String precio;
    private String plaza;
    private String origen;
    private String destino;

    public VehiculoAereo(String nombre, String precio, String plaza, String origen, String destino) {
        this.nombre = nombre;
        this.precio = precio;
        this.plaza = plaza;
        this.origen = origen;
        this.destino = destino;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getPlaza() {
        return plaza;
    }

    public void setPlaza(String plaza) {
        this.plaza = plaza;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}
