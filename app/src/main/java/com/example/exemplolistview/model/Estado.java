package com.example.exemplolistview.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "estado")
public class Estado {

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    //para passar uma string como id seta id= true  no databaseField e apaga tudo que tem no parenteses
    private Integer id;

    @DatabaseField(canBeNull = false, columnName = "nome", width = 60)
    private String nome;

    @DatabaseField(canBeNull = false, width = 2) //useGetSet true antes de colocar no banco pega do getSigla - pode ter um metodo no getSigla que retorna o metodo
    private String sigla;

    public Estado() {
    }

    public Estado(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public Estado(Integer id, String nome, String sigla) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Override
    public String toString() {
        return nome + " (" + sigla + ")";
    }
}
