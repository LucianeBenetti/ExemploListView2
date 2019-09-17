package com.example.exemplolistview.dao.db;

import android.content.Context;
import com.example.exemplolistview.dao.helpers.DaoHelper;
import com.example.exemplolistview.model.Estado;

import java.sql.SQLException;
import java.util.List;

public class EstadoDao extends DaoHelper <Estado>{

    public EstadoDao(Context c) {
        super(c, Estado.class);
    }

    public Estado getEstadosBySigla(String sigla){

        try {
            List<Estado> listEstado=  this.getDao().queryBuilder().where().eq("sigla", sigla).or().like("nome", sigla).query();
            if(listEstado.size()>0){
                return listEstado.get(0);
            }else{
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
