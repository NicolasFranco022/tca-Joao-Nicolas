package com.tca.Models;

import java.time.LocalDate;


public class Agendamento {
    private int id;
    private Pet pet;
    private LocalDate dataAgendamento;
    private String motivoConsulta;
    private Dono dono;

    public Agendamento() {}

    public Agendamento(int id, String motivoConsulta, LocalDate dataAgendamento) {
        this.id = id;
        this.motivoConsulta = motivoConsulta;
        this.dataAgendamento = dataAgendamento;
    }

    public Agendamento(int id, Pet pet, LocalDate dataAgendamento, String motivoConsulta) {
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

    public Dono getDono() {
        return dono;
    }

    public void setDono(Dono dono) {
        this.dono = dono;
    }
}