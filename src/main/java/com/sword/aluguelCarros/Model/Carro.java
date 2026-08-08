package com.sword.aluguelCarros.Model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Getter;

@NoArgsConstructor
@AllArgsConstructor
public class Carro {
    private Integer id;
    private String nome;
    private String marca;
    private Float valorDiaria;
    private String disponivel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Float getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(Float valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public String getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(String disponivel) {
        this.disponivel = disponivel;
    }

}
