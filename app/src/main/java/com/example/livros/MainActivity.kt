package com.example.livros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.livros.databinding.ActivityMainBinding
import com.example.livros.model.Livro

class MainActivity : AppCompatActivity() {
    private val activityMainBinding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Data Source
    private val livrosList: MutableList<Livro> = mutableListOf()

    //Adapter
    private val livrosAdapter: ArrayAdapter<String> by lazy{
        val livrosStringList = mutableListOf<String>()
        livrosList.forEach{ livro -> livrosStringList.add(livro.toString())}
        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, livrosStringList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        //Inicializando lista de livros
        inicializarLivrosList()

        //Associar Adapter ao ListView
        activityMainBinding.livrosLv.adapter = livrosAdapter
    }

    private fun inicializarLivrosList(){
        for (indice in 1..10) {
            livrosList.add(
                Livro(
                    "Título ${indice}",
                    "Isbn ${indice}",
                    "Primeiro Autor ${indice}",
                    "Editora ${indice}",
                    indice,
                    indice
                )
            )
        }
    }
}