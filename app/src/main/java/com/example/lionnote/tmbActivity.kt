package com.example.lionnote

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.lionnote.model.Calc

class tmbActivity : AppCompatActivity() {
    private lateinit var lifestyleAuto: AutoCompleteTextView
    private lateinit var editHeight: EditText;
    private lateinit var editWeight: EditText;
    private lateinit var editAge: EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmb)
        editHeight = findViewById(R.id.edit_tmb_height)
        editWeight = findViewById(R.id.edit_tmb_weight)
        val buttonSend: Button = findViewById(R.id.btn_tmb_send)

        lifestyleAuto = findViewById(R.id.auto_lifestyle)
        val items = resources.getStringArray(R.array.tmb_lifestyle)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        lifestyleAuto.setAdapter(adapter)

        buttonSend.setOnClickListener {
            if (!validate()) {
                Toast.makeText(this, R.string.field_messages, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val weight = editWeight.text.toString().toInt()
            val height = editHeight.text.toString().toInt()
            val age = editAge.text.toString().toInt()

            val result = calculateTmb(weight, height, age)
            val response = tmbRequest(result)

            AlertDialog.Builder(this)
                .setMessage(getString(R.string.tmb_response, response))
                .setPositiveButton(
                    android.R.string.ok
                ) { dialog, which -> }
                .setNegativeButton(
                    R.string.save
                ) { dialog, which ->
                    //Processo paralelo para não travar a main thread UI
                    Thread {
                        val app = application as App
                        val dao = app.db.calcDao()
                        dao.insert(Calc(type = "tmb", res = response))
                        runOnUiThread {//usado para escrever na thread principal
                            openListActivity()
                        }
                    }.start()
                }
                .create()
                .show()

            val service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            service.hideSoftInputFromWindow(currentFocus?.windowToken, 0)


        }
    }

    private fun tmbRequest(tmb: Double): Double {
        val items = resources.getStringArray(R.array.tmb_lifestyle)
        return when {
            lifestyleAuto.text.toString() == items[0] -> tmb * 1.2
            lifestyleAuto.text.toString() == items[1] -> tmb * 1.375
            lifestyleAuto.text.toString() == items[2] -> tmb * 1.55
            lifestyleAuto.text.toString() == items[3] -> tmb * 1.725
            lifestyleAuto.text.toString() == items[4] -> tmb * 1.9
            else -> 0.0
        }
    }

    private fun calculateTmb(weight: Int, height: Int, age: Int): Double {
        return 66 + (13.8 * weight) + (5 * height) - (6.8 * age)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search) {
            finish()
            openListActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openListActivity() {
        val intent = Intent(this, ListCalcActivity::class.java)
        intent.putExtra("type", "tmb")
        startActivity(intent)
    }

    private fun validate(): Boolean {
        return (editHeight.text.toString().isNotEmpty()
                && editAge.text.toString().isNotEmpty()
                && editWeight.text.toString().isNotEmpty()
                && !editHeight.text.toString().startsWith("0")
                && !editWeight.text.toString().startsWith("0")
                && !editAge.text.toString().startsWith("0"))
    }
}