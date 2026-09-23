package com.ecoorbit.api.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "leituras_ambientais")
public class LeituraAmbiental {

    @Id
    private String id;

    @NotBlank
    private String codigo;

    @NotBlank
    private String areaCodigo;

    private Instant data;
    private Double temperatura;
    private Double umidade;
    private Double co2Ppm;
    private Double indiceVegetacao;
    private String riscoIncendio;
    private Double particulasPm25;
    private Double precipitacaoMm;
    private Double velocidadeVentoKmh;
    private Double nivelRioM;
    private Double radiacaoSolar;

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

    public Instant getData() {
        return data;
    }

    public void setData(Instant data) {
        this.data = data;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getUmidade() {
        return umidade;
    }

    public void setUmidade(Double umidade) {
        this.umidade = umidade;
    }

    public Double getCo2Ppm() {
        return co2Ppm;
    }

    public void setCo2Ppm(Double co2Ppm) {
        this.co2Ppm = co2Ppm;
    }

    public Double getIndiceVegetacao() {
        return indiceVegetacao;
    }

    public void setIndiceVegetacao(Double indiceVegetacao) {
        this.indiceVegetacao = indiceVegetacao;
    }

    public String getRiscoIncendio() {
        return riscoIncendio;
    }

    public void setRiscoIncendio(String riscoIncendio) {
        this.riscoIncendio = riscoIncendio;
    }

    public Double getParticulasPm25() {
        return particulasPm25;
    }

    public void setParticulasPm25(Double particulasPm25) {
        this.particulasPm25 = particulasPm25;
    }

    public Double getPrecipitacaoMm() {
        return precipitacaoMm;
    }

    public void setPrecipitacaoMm(Double precipitacaoMm) {
        this.precipitacaoMm = precipitacaoMm;
    }

    public Double getVelocidadeVentoKmh() {
        return velocidadeVentoKmh;
    }

    public void setVelocidadeVentoKmh(Double velocidadeVentoKmh) {
        this.velocidadeVentoKmh = velocidadeVentoKmh;
    }

    public Double getNivelRioM() {
        return nivelRioM;
    }

    public void setNivelRioM(Double nivelRioM) {
        this.nivelRioM = nivelRioM;
    }

    public Double getRadiacaoSolar() {
        return radiacaoSolar;
    }

    public void setRadiacaoSolar(Double radiacaoSolar) {
        this.radiacaoSolar = radiacaoSolar;
    }
}
