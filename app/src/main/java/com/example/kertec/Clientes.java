package com.example.kertec;

public class Clientes {

    String cliente;
    String marca;
    String modelo;
    String serie;

    public Clientes(String cliente, String marca, String modelo, String serie) {
        this.cliente = cliente;
        this.marca = marca;
        this.modelo = modelo;
        this.serie = serie;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        return cliente;
    }
}
