package com.example.livros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.livros.databinding.ActivityLivroBinding;
import com.example.livros.model.Livro;

public class LivroActivity extends AppCompatActivity {
    private ActivityLivroBinding activityLivroBinding;
    private int posicao = -1;
    private Livro livro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLivroBinding = ActivityLivroBinding.inflate(getLayoutInflater());

        setContentView(activityLivroBinding.getRoot());

        activityLivroBinding.salvarBt.setOnClickListener(
                (View view) -> {
                    livro = new Livro(
                           activityLivroBinding.tituloEt.getText().toString(),
                           activityLivroBinding.isbnEt.getText().toString(),
                           activityLivroBinding.primeiroAutorEt.getText().toString(),
                           activityLivroBinding.editoraEt.getText().toString(),
                           Integer.parseInt(activityLivroBinding.edicaoEt.getText().toString()),
                           Integer.parseInt(activityLivroBinding.paginasEt.getText().toString())
                   );

                   Intent resultadoIntent = new Intent();
                   resultadoIntent.putExtra(MainActivity.EXTRA_LIVRO, livro);

                   //Se foi edição, devolver posição também
                    if(posicao!=-1){
                        resultadoIntent.putExtra(MainActivity.EXTRA_POSICAO, posicao);
                    }

                   setResult(RESULT_OK, resultadoIntent);
                   finish();
                }
        );

        //Verificando se é uma edição ou consulta e preenchimento de campos
        posicao = getIntent().getIntExtra(MainActivity.EXTRA_POSICAO, -1);
        livro = getIntent().getParcelableExtra(MainActivity.EXTRA_LIVRO);
        if(livro!=null){
            activityLivroBinding.tituloEt.setEnabled(false);
            activityLivroBinding.tituloEt.setText(livro.getTitulo());
            activityLivroBinding.isbnEt.setText(livro.getIsbn());
            activityLivroBinding.primeiroAutorEt.setText(livro.getPrimeiroAutor());
            activityLivroBinding.editoraEt.setText(livro.getEditora());
            activityLivroBinding.edicaoEt.setText(Integer.toString(livro.getEdicao()));
            activityLivroBinding.paginasEt.setText(Integer.toString(livro.getPaginas()));

            if(posicao==-1){
                for (int i = 0; i < activityLivroBinding.getRoot().getChildCount(); i++){
                    activityLivroBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activityLivroBinding.salvarBt.setVisibility(View.GONE);
            }
        }
    }
}