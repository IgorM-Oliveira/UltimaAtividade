package br.unigran.ultimotrabalho.bancoDeDados;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import br.unigran.ultimotrabalho.entidades.Telefone;

public class DB_Telefone {
    private DBHelper db;
    private SQLiteDatabase conexao;

    public DB_Telefone(DBHelper db) {
        this.db = db;
    }

    public void inserir(Telefone telefone) {
        conexao = db.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("nome", telefone.getNome());
        valores.put("telefone", telefone.getTelefone());
        valores.put("dataNascimento", telefone.getDataNascimento());

        conexao.insertOrThrow("listaTelefonica", null, valores);

        conexao.close();
    }

    public void remover(Integer id) {
        conexao = db.getWritableDatabase();

        conexao.delete("listaTelefonica", "id=?", new String[]{id + ""});

        conexao.close();
    }

    public void editar(Telefone telefone) {
        conexao = db.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("nome", telefone.getNome());
        valores.put("telefone", telefone.getTelefone());
        valores.put("dataNascimento", telefone.getDataNascimento());

        conexao.update("listaTelefonica", valores, "id=?", new String[]{telefone.getId().toString()});

        conexao.close();
    }

    public void listar(List listaTelefonica) {
        listaTelefonica.clear();
        conexao = db.getReadableDatabase();

        String colunas[] = {"id", "nome", "telefone", "dataNascimento"};
        Cursor query = conexao.query("listaTelefonica", colunas, null, null, null, null, "nome");

        while (query.moveToNext()) {
            Telefone telefone = new Telefone();

            telefone.setId(Integer.parseInt(query.getString(0)));
            telefone.setNome(query.getString(1));
            telefone.setTelefone(query.getString(2));
            telefone.setDataNascimento(query.getString(3));

            listaTelefonica.add(telefone);
        }

        conexao.close();
    }
}