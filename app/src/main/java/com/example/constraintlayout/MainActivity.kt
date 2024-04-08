package com.example.constraintlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var edtPessoas: EditText
    private lateinit var btnFalar: Button
    private lateinit var btnCompartilhamento: FloatingActionButton
    private lateinit var valorfinal: TextView
    private var ttsSucess: Boolean = false
    private lateinit var textoTTS: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtConta = findViewById(R.id.edtConta)
        edtPessoas = findViewById(R.id.edtPessoas)
        btnFalar = findViewById(R.id.btFalar)
        btnCompartilhamento = findViewById(R.id.floatingActionButton)
        valorfinal = findViewById(R.id.valorFinal)
        // Initialize TTS engine
        tts = TextToSpeech(this, this)

        edtConta.addTextChangedListener(this)
        edtPessoas.addTextChangedListener(this)
        btnCompartilhamento.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain2"
            intent.putExtra(Intent.EXTRA_TEXT, textoTTS)
            startActivity(intent)
        }
        btnFalar.setOnClickListener {
            tts.speak(textoTTS, TextToSpeech.QUEUE_FLUSH, null, "ID1")
        }

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       Log.d("PDM24","Antes de mudar")

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM24","Mudando")
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d ("PDM24", "Depois de mudar")
        val valorUnico = edtConta.text.toString()
        val numerodePessoas = edtPessoas.text.toString()

        if (valorUnico != "" && numerodePessoas != "") {
            val valorUnicoDouble = valorUnico.toDoubleOrNull() ?: 0.0
            val numerodePessoasInt = numerodePessoas.toIntOrNull() ?: 1

            val valorporPessoa = valorUnicoDouble / numerodePessoasInt
            val resultadoDoTexto = String.format(Locale.getDefault(), "Por pessoa: R$%.2f", valorporPessoa)

            valorfinal.text = resultadoDoTexto

            textoTTS = "A conta fica $valorporPessoa reais para cada pessoa!"
        } else {
            valorfinal.text = "R$:0,00"
        }
    }

    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                ttsSucess=true
                Log.d("PDM24","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM24", "Failed to initialize TTS engine.")
                ttsSucess=false
            }
        }


}

