package com.example.livros.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.provider.Settings.Global.getString
import android.util.Log
import com.example.livros.R
import java.sql.SQLException

class LivroSqlite(contexto: Context): LivroDAO {

    companion object {
        private val BD_LIVROS = "livros"
        private val TABELA_LIVRO = "livro"
        private val COLUNA_TITULO = "titulo"
        private val COLUNA_ISBN = "isbn"
        private val COLUNA_PRIMEIRO_AUTOR = "primeiro_autor"
        private val COLUNA_EDITORA = "editora"
        private val COLUNA_EDICAO = "edicao"
        private val COLUNA_PAGINAS = "paginas"

        private val CRIAR_TABELA_LIVRO_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_LIVRO} (" +
                "${COLUNA_TITULO} TEXT NOT NULL PRIMARY KEY, " +
                "${COLUNA_ISBN} TEXT NOT NULL, " +
                "${COLUNA_PRIMEIRO_AUTOR} TEXT NOT NULL, " +
                "${COLUNA_EDITORA} TEXT NOT NULL, " +
                "${COLUNA_EDICAO} INTEGER NOT NULL, " +
                "${COLUNA_PAGINAS} INTEGER NOT NULL );"
    }

    //REFERENCIA PARA O BANCO DE DADOS
    private val livrosBd: SQLiteDatabase
    init {
        livrosBd = contexto.openOrCreateDatabase(BD_LIVROS, MODE_PRIVATE, null)
        try {
            livrosBd.execSQL(CRIAR_TABELA_LIVRO_STMT)
        } catch (se: SQLException) {
         Log.e(contexto.getString(R.string.app_name), se.toString())
        }
    }

    override fun criarLivro(livro: Livro): Long {
        val livroCv = ContentValues()
        livroCv.put(COLUNA_TITULO, livro.titulo)
        livroCv.put(COLUNA_ISBN, livro.isbn)
        livroCv.put(COLUNA_PRIMEIRO_AUTOR, livro.primeiroAutor)
        livroCv.put(COLUNA_EDITORA, livro.editora)
        livroCv.put(COLUNA_EDICAO, livro.edicao)
        livroCv.put(COLUNA_PAGINAS, livro.paginas)

        return livrosBd.insert(TABELA_LIVRO, null, livroCv)
    }


    override fun recuperarLivro(titulo: String): Livro {
        val livroCursor = livrosBd.query(
            true, //distinct
            TABELA_LIVRO, //tabela
            null, //colunas
            "${COLUNA_TITULO} = ?", //where
            arrayOf(titulo), //valores do where
            null,
            null,
            null,
            null
        )

        return if (livroCursor.moveToFirst()) {
            with(livroCursor) {
                Livro (
                    getString(getColumnIndexOrThrow(TABELA_LIVRO)),
                    getString(getColumnIndexOrThrow(COLUNA_ISBN)),
                    getString(getColumnIndexOrThrow(COLUNA_PRIMEIRO_AUTOR)),
                    getString(getColumnIndexOrThrow(COLUNA_EDITORA)),
                    getInt(getColumnIndexOrThrow(COLUNA_EDICAO)),
                    getInt(getColumnIndexOrThrow(COLUNA_PAGINAS))
                )
            }
        }
        else {
            Livro()
        }
    }

    override fun recuperarLivros(): MutableList<Livro> {
        // Tarefa
        val listaLivros: MutableList<Livro> = mutableListOf()

        //Consulta usando raw query
        val livroQuery = "SELECT * FROM ${TABELA_LIVRO};"
        val livroCursor = livrosBd.rawQuery(livroQuery, null)

        while (livroCursor.moveToNext()) {
            with(livroCursor) {
                Livro (
                    getString(getColumnIndexOrThrow(TABELA_LIVRO)),
                    getString(getColumnIndexOrThrow(COLUNA_ISBN)),
                    getString(getColumnIndexOrThrow(COLUNA_PRIMEIRO_AUTOR)),
                    getString(getColumnIndexOrThrow(COLUNA_EDITORA)),
                    getInt(getColumnIndexOrThrow(COLUNA_EDICAO)),
                    getInt(getColumnIndexOrThrow(COLUNA_PAGINAS))
                )
            }
        }
        return listaLivros
    }

    override fun atualizarLivro(livro: Livro): Int {
        val livroCv = ContentValues()
        livroCv.put(COLUNA_TITULO, livro.titulo)
        livroCv.put(COLUNA_ISBN, livro.isbn)
        livroCv.put(COLUNA_PRIMEIRO_AUTOR, livro.primeiroAutor)
        livroCv.put(COLUNA_EDITORA, livro.editora)
        livroCv.put(COLUNA_EDICAO, livro.edicao)
        livroCv.put(COLUNA_PAGINAS, livro.paginas)

        return livrosBd.update(TABELA_LIVRO, livroCv, "${COLUNA_TITULO} = ?", arrayOf(livro.titulo))
    }

    override fun removerLivro(titulo: String): Int {
       // Tarefa
        return livrosBd.delete (
            TABELA_LIVRO,
            "${COLUNA_TITULO} = ?",
             arrayOf(titulo)
        )
    }
}