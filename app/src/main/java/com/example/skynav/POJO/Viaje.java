package com.example.skynav.POJO;

public class Viaje {
    private String vehiculo;
    private String fecha;
    private String origen;
    private String destino;
    private int id;

    public Viaje(String vehiculo, String fecha, String origen, String destino, int id) {
        this.vehiculo = vehiculo;
        this.fecha = fecha;
        this.origen = origen;
        this.destino = destino;
        this.id = id;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
