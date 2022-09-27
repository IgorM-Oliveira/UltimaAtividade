package br.unigran.ultimotrabalho;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.unigran.ultimotrabalho.bancoDeDados.DBHelper;
import br.unigran.ultimotrabalho.bancoDeDados.DB_Telefone;
import br.unigran.ultimotrabalho.entidades.Telefone;

public class MainActivity extends AppCompatActivity {

    EditText editNome;
    EditText editDataNascimento;
    EditText editTelefone;
    Button botaoSalvar;
    ListView listDados;
    List<Telefone> listTelefone;
    ArrayAdapter adapter;
    DB_Telefone DB_telefone;
    Telefone telefone;
    Boolean validarEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper db = new DBHelper(this);
        DB_telefone = new DB_Telefone(db);

        editNome = findViewById(R.id.editNome);
        editTelefone = findViewById(R.id.editTelefone);
        editDataNascimento = findViewById(R.id.editDataNascimento);
        botaoSalvar = findViewById(R.id.botaoSalvar);
        listDados = findViewById(R.id.listDados);

        listTelefone = new ArrayList<>();
        adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listTelefone);

        listDados.setAdapter(adapter);
        DB_telefone.listar(listTelefone);

        validarEdicao = false;

        acao();
    }

    private void acao() {
        listDados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Selecione uma Opção:")
                        .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                validarEdicao = true;

                                telefone = new Telefone();
                                telefone.setId(listTelefone.get(i).getId());

                                editNome.setText(listTelefone.get(i).getNome());
                                editTelefone.setText(listTelefone.get(i).getTelefone());
                                editDataNascimento.setText(listTelefone.get(i).getDataNascimento());
                            }
                        })
                        .setNegativeButton("Remover", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                new AlertDialog.Builder(view.getContext())
                                        .setMessage("Deseja realmente remover?")
                                        .setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int k) {
                                                DB_telefone.remover(listTelefone.get(i).getId());
                                                DB_telefone.listar(listTelefone);
                                                adapter.notifyDataSetChanged();

                                                Toast.makeText(MainActivity.this, "Removido com Sucesso!", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .setNegativeButton("Cancelar", null)
                                        .create().show();
                            }
                        })
                        .create().show();
                return (false);
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editNome.getText().toString().isEmpty() || editTelefone.getText().toString().isEmpty() || editDataNascimento.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Dados Inválidos!", Toast.LENGTH_SHORT).show();
                } else {
                    if (validarEdicao == false) {
                        telefone = new Telefone();
                    }

                    telefone.setNome(editNome.getText().toString());
                    telefone.setTelefone(editTelefone.getText().toString());
                    telefone.setDataNascimento(editDataNascimento.getText().toString());

                    if (validarEdicao) {
                        DB_telefone.editar(telefone);

                        Toast.makeText(MainActivity.this, "Editado com Sucesso!", Toast.LENGTH_LONG).show();
                    } else {
                        DB_telefone.inserir(telefone);

                        Toast.makeText(MainActivity.this, "Salvo com Sucesso!", Toast.LENGTH_LONG).show();
                    }

                    DB_telefone.listar(listTelefone);
                    adapter.notifyDataSetChanged();

                    telefone = null;
                    validarEdicao = false;
                    editNome.setText("");
                    editTelefone.setText("");
                    editDataNascimento.setText("");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        telefone = null;
        validarEdicao = false;
        editNome.setText("");
        editTelefone.setText("");
        editDataNascimento.setText("");

        Toast.makeText(MainActivity.this, "Cancelado com Sucesso!", Toast.LENGTH_LONG).show();
    }
}