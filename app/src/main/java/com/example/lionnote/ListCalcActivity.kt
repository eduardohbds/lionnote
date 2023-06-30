package com.example.lionnote

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lionnote.model.Calc
import java.text.SimpleDateFormat
import java.util.*

class ListCalcActivity : AppCompatActivity() {
    private lateinit var rv : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)
        val result = mutableListOf<Calc>()
        val adapter = ListCalcAdapter(result)
        rv = findViewById(R.id.rv_list)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter


        //se não chegar nada joga um erro
        val type = intent?.extras?.getString("type") ?: throw IllegalStateException("Type not found")
        Thread {
            val app = application as App
            val dao = app.db.calcDao()
            val response = dao.getRegisterByType(type)
            runOnUiThread {//usado para escrever na thread principal
                result.addAll(response)
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    private inner class ListCalcAdapter(
        private val listCalc: List<Calc>,
    ) : RecyclerView.Adapter<ListCalcAdapter.ListCalcHolder>() {
        //Qual é o layout XML da celula
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCalcHolder {
            val viewHolder = layoutInflater.inflate(R.layout.menu_item, parent, false)
            return ListCalcHolder(viewHolder)
        }

        // informar quantos celulas essa listagem terá
        override fun getItemCount(): Int {
            return listCalc.size
        }
        // disparado toda vez que houver uma rolagem na tela e for necessario trocar o conteudo da celula
        override fun onBindViewHolder(holder: ListCalcHolder, position: Int) {
            val itemMenu = listCalc[position]
            holder.bind(itemMenu)
        }
        //classe da celula em si
        private inner class ListCalcHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: Calc) {
                val tv = itemView as TextView

                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt"))
                val data = sdf.format(item.createdDate)
                val res = item.res

                tv.text = getString(R.string.list_response,res,data)
            }
        }
    }
}