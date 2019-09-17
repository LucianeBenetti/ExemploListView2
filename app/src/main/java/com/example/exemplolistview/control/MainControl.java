package com.example.exemplolistview.control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.exemplolistview.R;
import com.example.exemplolistview.dao.db.EstadoDao;
import com.example.exemplolistview.model.Estado;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainControl {

    private Activity activity;
    private EditText editNome;
    private EditText editSigla;
    private TextView tvContagem;
    private ListView lvEstado;
    private List<Estado> listEstado;
    private ArrayAdapter<Estado> adapterEstado;
    private Estado estado;
    private EstadoDao estadoDao;

    public MainControl(Activity activity) {
        this.activity = activity;
        estadoDao = new EstadoDao(activity);
        initComponents();
    }

    private void initComponents() {
        editNome = activity.findViewById(R.id.editNomeEstado);
        editSigla = activity.findViewById(R.id.editSiglaEstado);
        tvContagem = activity.findViewById(R.id.tvContagem);
        lvEstado = activity.findViewById(R.id.lvEstado);
        configListView();
    }

    private void configListView() {
        try {
            listEstado = estadoDao.getDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //listEstado.add(new Estado("Acre", "AC"));
        adapterEstado = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, listEstado);
        //para spinner tem um R.layout.spinner.
        lvEstado.setAdapter(adapterEstado);
        cliqueCurto();
        cliqueLongo();
        atualizarContador();
    }


    private void cliqueLongo() {
        lvEstado.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                estado = adapterEstado.getItem(i);
                dialogExcluirEstado(estado);
                //return false = excuta o clique curto junto
              //  return false;
                return true; //executa somente clique longo
            }
        });
    }

    private void cliqueCurto() {
        lvEstado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                estado = adapterEstado.getItem(i);
                dialogEditarEstado(estado);
            }
        });
    }

    private void addEstadoLv(Estado e) {
        adapterEstado.add(e);
    }

    private void excluirEstadoLv(Estado e) {
        adapterEstado.remove(e);
    }

    private void atualizarEstado(Estado e) {
        estado.setNome(e.getNome());//estado dos atributos globais
        estado.setSigla(e.getSigla());
        adapterEstado.notifyDataSetChanged();
    }

    private Estado getDadosForm() {
        Estado e = new Estado();
        e.setNome(editNome.getText().toString());
        e.setSigla(editSigla.getText().toString());
        return e;
    }

    private void carregarForm(Estado e) {
        editNome.setText(e.getNome());
        editSigla.setText(e.getSigla());
    }

    private void atualizarContador() {
        tvContagem.setText("Contagem: " + adapterEstado.getCount());
    }

    private void dialogExcluirEstado(final Estado e) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
        alerta.setTitle("Exclindo Estado...");
        alerta.setMessage(e.toString());
        alerta.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                estado = null;
            }
        });
        alerta.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                try {
                    if(estadoDao.getDao().delete(estado)>0) {
                        excluirEstadoLv(e);
                        atualizarContador();
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                estado = null;
            }
        });
        alerta.show();
    }

    private void dialogEditarEstado (final Estado e){
        AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
        alerta.setTitle("Mostrando dados");
        alerta.setMessage(e.toString());
        alerta.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                estado = null;
            }
        });
        alerta.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                carregarForm(e);
            }
        });
        alerta.show();
    }

    private void ordenarLista(){
        adapterEstado.sort(new Comparator<Estado>() {
            @Override
            public int compare(Estado estado1, Estado estado2) {
                return estado1.getNome().compareTo(estado2.getNome());
            }
        });
    }

    public void salvarAction(){

        if(estado==null){
            estado = getDadosForm();
        }else {
            Estado e = getDadosForm();
              estado.setNome(e.getNome());
              estado.setSigla(e.getSigla());
        }
       Dao.CreateOrUpdateStatus res;
            try {
                res = estadoDao.getDao().createOrUpdate(estado);

                if(res.isCreated()){
                    addEstadoLv(estado);
                    atualizarContador();
                }else if(res.isUpdated()){

                    atualizarEstado(estado);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }


//            if(estado== null){
//            Estado e = getDadosForm();
//
//            try {
//                if(estadoDao.getDao().create(e)>0) {
//                    addEstadoLv(e);
//                    atualizarContador();
//                    ordenarLista();
//                }
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        }else{
//            Estado e = getDadosForm();
//            estado.setNome(e.getNome());
//            estado.setSigla(e.getSigla());
//            try {
//                if(estadoDao.getDao().update(estado)>0) {
//                    atualizarEstado(e);
//                    ordenarLista();
//                }
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        }
        estado = null;
    }
}