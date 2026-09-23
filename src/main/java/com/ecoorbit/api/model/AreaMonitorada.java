package com.ecoorbit.api.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "areas_monitoradas")
public class AreaMonitorada {

    @Id
    private String id;

    @NotBlank
    private String codigo;

    @NotBlank
    private String nome;

    private String bioma;
    private String estado;
    private String municipio;
    private Double areaKm2;
    private String nivelRisco;
    private Boolean protegida;
    private Coordenadas coordenadas;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBioma() {
        return bioma;
    }

    public void setBioma(String bioma) {
        this.bioma = bioma;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public Double getAreaKm2() {
        return areaKm2;
    }

    public void setAreaKm2(Double areaKm2) {
        this.areaKm2 = areaKm2;
    }

    public String getNivelRisco() {
        return nivelRisco;
    }

    public void setNivelRisco(String nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public Boolean getProtegida() {
        return protegida;
    }

    public void setProtegida(Boolean protegida) {
        this.protegida = protegida;
    }

    public Coordenadas getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(Coordenadas coordenadas) {
        this.coordenadas = coordenadas;
    }
}
