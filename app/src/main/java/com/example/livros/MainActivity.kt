package com.example.livros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.livros.adapter.LivrosRvAdapter
import com.example.livros.adapter.livrosAdapter
import com.example.livros.controller.LivroController
import com.example.livros.databinding.ActivityMainBinding
import com.example.livros.model.Livro
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), OnLivroClickListener {
    companion object Extras {
        const val EXTRA_LIVRO = "EXTRA_LIVRO"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }

    private val activityMainBinding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var livroActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarLivroActivityResultLauncher: ActivityResultLauncher<Intent>

    //Data Source
    private val livrosList: MutableList<Livro> by lazy {
        livroController.buscarLivros()
    }

    //Controller
    private val livroController: LivroController by lazy {
        LivroController(this)
    }


    //LayoutManager
    private val livrosLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val livrosAdapter: LivrosRvAdapter by lazy {
        LivrosRvAdapter(this, livrosList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        //Associar Adapter e LayoutManager ao RecycleView
        activityMainBinding.livrosRv.adapter = livrosAdapter //Kotlin
        activityMainBinding.livrosRv.layoutManager = livrosLayoutManager
        //activityMainBinding.livrosLv.(livrosAdapter); Java


        //Adicionar um livro
        livroActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Livro>(EXTRA_LIVRO)?.apply {
                    livroController.inserirLivro(this)
                    livrosList.add(this)
                    livrosAdapter.notifyDataSetChanged()
                }
            }
        }

        editarLivroActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO, -1)
                val livro = resultado.data?.getParcelableExtra<Livro>(EXTRA_LIVRO)?.apply {
                    if(posicao!=null && posicao!=-1) {
                        livroController.modificarLivro(this) // modificando no banco de dados
                        livrosList[posicao] = this
                        livrosAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainBinding.adicionarLivroFab.setOnClickListener {
            livroActivityResultLauncher.launch(Intent(this, LivroActivity::class.java))
        }
    }

    /*
    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when (item.itemId) {

        R.id.adicionarLivroMi -> {
            livroActivityResultLauncher.launch(Intent(this, LivroActivity::class.java))
            true
        } else ->  {
            false;
        }
    }
*/
    override fun onContextItemSelected(item: MenuItem): Boolean {
       // val posicao = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        val posicao = livrosAdapter.posicao
        val livro = livrosList[posicao]

        return when (item.itemId) {
            R.id.editarLivroMi -> {
                //Editar livro
                val editarLivroIntent = Intent(this, LivroActivity::class.java)
                editarLivroIntent.putExtra(EXTRA_LIVRO, livro)
                editarLivroIntent.putExtra(EXTRA_POSICAO, posicao)
                editarLivroActivityResultLauncher.launch(editarLivroIntent)

                true
            }
            R.id.removerLivroMi -> {
                //Remover livro
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirma a remoção?")
                    setPositiveButton("Sim"){_,_, ->
                        livroController.apagarLivro(livro.titulo)
                        livrosList.removeAt(posicao)
                        livrosAdapter.notifyDataSetChanged()
                        Snackbar.make(activityMainBinding.root, "Livro removido!", Snackbar.LENGTH_SHORT).show()
                    }

                    setNegativeButton("Não") { _, _, ->
                        Snackbar.make(activityMainBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()

                true
            }
            else -> {
                false
            }
        }
    }

    override fun onLivroClick(posicao: Int) {
        val livro = livrosList[posicao]
        val consultarLivroIntent = Intent (this, LivroActivity::class.java)
        consultarLivroIntent.putExtra(EXTRA_LIVRO, livro)
        startActivity(consultarLivroIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when (item.itemId) {
        R.id.atualizarMi -> {
            livrosAdapter.notifyDataSetChanged()
            true
        }
        R.id.sairMi -> {
            AutenticacaoFirebase.firebaseAuth.signOut()
            finish()
            true
        }else ->  {
            false;
        }
    }

    override fun onStart() {
        super.onStart()
        if(AutenticacaoFirebase.firebaseAuth.currentUser==null){
            finish()
        }
    }
}
