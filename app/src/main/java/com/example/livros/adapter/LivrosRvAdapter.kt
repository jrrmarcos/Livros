package com.example.livros.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.livros.OnLivroClickListener
import com.example.livros.R
import com.example.livros.databinding.LayoutLivroBinding
import com.example.livros.model.Livro

class LivrosRvAdapter(
    private val onLivroClickListener: OnLivroClickListener,
    private val livrosList: MutableList<Livro>
): RecyclerView.Adapter<LivrosRvAdapter.LivroLayoutHolder>() {
    //Posição que será recuperada pelo menu de contexto
    var posicao: Int = -1

    //ViewHolder
    inner class LivroLayoutHolder(layoutLivroBinding: LayoutLivroBinding): RecyclerView.ViewHolder(layoutLivroBinding.root),
        View.OnCreateContextMenuListener {
        val tituloTv: TextView = layoutLivroBinding.tituloTv
        val primeiroAutorTv: TextView = layoutLivroBinding.primeiroAutorTv
        val editoraTv: TextView = layoutLivroBinding.editoraTv
        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.context_manu_main, menu)
        }
    }

    //Quando uma nova célula precisar ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroLayoutHolder {
        //Criar uma nova célula
        val layoutLivroBinding = LayoutLivroBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //Criar um holder associado a nova célula
        val viewHolder: LivroLayoutHolder = LivroLayoutHolder(layoutLivroBinding)
        return viewHolder
        //return LivroLayoutHolder(layoutLivroBinding)
    }

    //Quando for necessário atualizar os valores de uma célula
    override fun onBindViewHolder(holder: LivroLayoutHolder, position: Int) {
        //Busco o livro
        val livro = livrosList[position]

        //Atualizar os valores do viewHolder
        with(holder) {
            tituloTv.text = livro.titulo
            primeiroAutorTv.text = livro.primeiroAutor
            editoraTv.text = livro.editora
            itemView.setOnClickListener {
                onLivroClickListener.onLivroClick(position)
            }
            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }
    }

    override fun getItemCount(): Int = livrosList.size
}