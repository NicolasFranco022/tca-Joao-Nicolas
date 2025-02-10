package com.tca.Models;

import java.time.LocalDate;

public class Agendamento {
    private int id;
    private Pet pet;
    private LocalDate dataAgendamento;
    private String motivoConsulta;

    public Agendamento() {}

    // Construtor atualizado para incluir Dono automaticamente do Pet
    public Agendamento(int id, Pet pet, LocalDate dataAgendamento, String motivoConsulta) {
        if (pet == null || pet.getDono() == null) {
            throw new IllegalArgumentException("Pet e Dono não podem ser nulos.");
        }

        this.id = id;
        this.pet = pet;
        this.dataAgendamento = dataAgendamento;
        this.motivoConsulta = motivoConsulta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    // Dono agora é obtido diretamente do Pet
    public Dono getDono() {
        return (pet != null) ? pet.getDono() : null;
    }
}