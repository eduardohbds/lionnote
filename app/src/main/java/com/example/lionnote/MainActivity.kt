package com.example.lionnote

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.example.lionnote.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

//    private lateinit var btnImc:LinearLayout;
    private lateinit var rvMain:RecyclerView;
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

        override fun getItemCount(): Int {
            return mainItems.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemMenu = mainItems[position]
            holder.bind(itemMenu)
        }

    }

    private class MainViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(item: MainItem) {
            val buttonTest: Button = itemView.findViewById(R.id.btn_item)
            buttonTest.setText(item.textStringId)
        }
    }
}