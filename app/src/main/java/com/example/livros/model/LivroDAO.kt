package com.example.livros.model

interface LivroDAO {
    fun criarLivro(livro: Livro): Long
    fun recuperarLivro(titul: String): Livro
    fun recuperarLivros(): MutableList<Livro>
    fun atualizarLivro(livro: Livro): Int
    fun removerLivro(titulo: String): Int
}