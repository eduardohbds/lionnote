package com.example.lionnote

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.lionnote.model.Calc

class imcActivity : AppCompatActivity() {
    private lateinit var editHeight: EditText;
    private lateinit var editWeight: EditText;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc)
        editHeight = findViewById(R.id.edit_imc_height)
        editWeight = findViewById(R.id.edit_imc_weight)
        val buttonSend: Button = findViewById(R.id.btn_imc_send)
        buttonSend.setOnClickListener {
            if (!validate()) {
                Toast.makeText(this, R.string.field_messages, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val weight = editWeight.text.toString().toInt()
            val height = editHeight.text.toString().toInt()

            val result = calculateImc(weight, height)
            Log.d("Teste", "resultado: $result")

            val imcResponseId = imcResponse(result)
            Toast.makeText(this, imcResponseId, Toast.LENGTH_LONG).show()

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.imc_response, result))
                .setMessage(imcResponseId)
                .setPositiveButton(
                    android.R.string.ok
                ) { dialog, which -> TODO("Not yet implemented") }
                .setNegativeButton(
                    R.string.save
                ) { dialog, which ->
                    //Processo paralelo para não travar a main thread UI
                    Thread {
                        val app = application as App
                        val dao = app.db.calcDao()
                        dao.insert(Calc(type = "imc", res = result))
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
    // se for true->visivel else invisivel
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search){
            finish()
            openListActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openListActivity() {
        val intent = Intent(this@imcActivity,ListCalcActivity::class.java)
        intent.putExtra("type","imc")
        startActivity(intent)
    }

    @StringRes
    private fun imcResponse(value: Double): Int {
        return when {
            value < 15.0 -> R.string.imc_severely_low_weight
            value < 16.0 -> R.string.imc_very_low_weight
            value < 18.5 -> R.string.imc_low_weight
            value < 25.0 -> R.string.normal
            value < 30.0 -> R.string.imc_high_weight
            value < 35.0 -> R.string.imc_so_high_weight
            value < 40.0 -> R.string.imc_severely_high_weight
            else -> R.string.imc_extreme_weight
        }
    }

    private fun calculateImc(weight: Int, height: Int): Double {
        return (weight / ((height / 100.0) * (height / 100.0)))
    }

    private fun validate(): Boolean {
        return (editHeight.text.toString().isNotEmpty()
                && !editHeight.text.toString().startsWith("0")
                && editWeight.text.toString().isNotEmpty()
                && !editWeight.text.toString().startsWith("0"))
    }
}