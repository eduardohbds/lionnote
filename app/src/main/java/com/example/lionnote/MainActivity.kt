package com.example.lionnote

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    //    private lateinit var btnImc:LinearLayout;
    private lateinit var rvMain:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItems = mutableListOf<MainItem>()
        mainItems.add(
            MainItem(id = 1,
                drawbleId = R.drawable.ic_imc,
                textStringId = R.string.label_imc,
                color = Color.GRAY)
        )

        val adapter = MainAdapter(mainItems)
        rvMain = findViewById(R.id.rv_main)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(this)

//        btnImc=findViewById(R.id.btn_imc)
//        btnImc.setOnClickListener{
//            var i = Intent(this,imcActivity::class.java)
//            startActivity(i)
//        }
    }

    private inner class MainAdapter(private val mainItems: List<MainItem>) : RecyclerView.Adapter<MainViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val viewHolder = layoutInflater.inflate(R.layout.menu_item,parent,false)
            return MainViewHolder(viewHolder)
        }
        // informar quantos celulas essa listagem terá
        override fun getItemCount(): Int {
            return mainItems.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemMenu = mainItems[position]
            holder.bind(itemMenu)
        }

    }
    //a classe da celula em si
    private class MainViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MainItem) {
            val image: ImageView = itemView.findViewById(R.id.item_img_icon)
            val text: TextView = itemView.findViewById(R.id.item_txt_name)
            val container: LinearLayout = itemView as LinearLayout

            image.setImageResource(item.drawbleId)
            text.setText(item.textStringId)
            container.setBackgroundColor(item.color)
        }
    }
}