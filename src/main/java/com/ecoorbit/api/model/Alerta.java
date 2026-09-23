package com.ecoorbit.api.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "alertas")
public class Alerta {

    @Id
    private String id;

    @NotBlank
    private String codigo;

    @NotBlank
    private String areaCodigo;

    private String tipo;
    private String nivelRisco;
    private Instant data;
    private String descricao;
    private Double areaAfetadaHectares;
    private String status;

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

    public String getAreaCodigo() {
        return areaCodigo;
    }

    public void setAreaCodigo(String areaCodigo) {
        this.areaCodigo = areaCodigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNivelRisco() {
        return nivelRisco;
    }

    public void setNivelRisco(String nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public Instant getData() {
        return data;
    }

    public void setData(Instant data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getAreaAfetadaHectares() {
        return areaAfetadaHectares;
    }

    public void setAreaAfetadaHectares(Double areaAfetadaHectares) {
        this.areaAfetadaHectares = areaAfetadaHectares;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
