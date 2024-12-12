package com.example.skynav.POJO;

public class Viaje {
    private String avion;
    private String fecha;
    private String origen;
    private String destino;
    private int id;

    public Viaje(String avion, String fecha, String origen, String destino, int id) {
        this.avion = avion;
        this.fecha = fecha;
        this.origen = origen;
        this.destino = destino;
        this.id = id;
    }

    public String getAvion() {
        return avion;
    }

    public void setAvion(String avion) {
        this.avion = avion;
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
