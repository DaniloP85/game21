package com.example.game21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.domain.model.Card
import com.example.mobcomponents.customtoast.CustomToast
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var btRecomecar: Button
    private lateinit var btProximaCard: Button
    private lateinit var tvPontuacao: TextView
    private lateinit var ivCard: ImageView

    private var cartas: MutableList<Card> = mutableListOf()
    private val gerador = Random()
    private lateinit var containerPropaganda: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpView()
        setListeners()
        iniciarPartida()
        showBanner()
    }

    private fun showBanner() {
        containerPropaganda.visibility = if (isGratuito())
            View.VISIBLE else View.GONE
    }
    private fun isGratuito(): Boolean {
        return packageName == "com.example.game21.gratuito"
    }


    private fun setUpView() {
        btRecomecar = findViewById(R.id.btRecomecar)
        btProximaCard = findViewById(R.id.btProximaCarta)
        tvPontuacao = findViewById(R.id.tvPontuacao)
        ivCard = findViewById(R.id.ivCarta)
        containerPropaganda = findViewById(R.id.containerPropaganda)

    }

    private fun setListeners() {
        btProximaCard.setOnClickListener {
            realizarJogada()
        }
        btRecomecar.setOnClickListener {
            iniciarPartida()
        }
    }

    private fun iniciarPartida() {
        tvPontuacao.text = "0"
        cartas = getBaralho()
        ivCard.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.logo))
    }

    private fun realizarJogada() {
        val posicaoCartaSelecionada = gerador.nextInt(cartas.size)
        val cartaSelecionada = cartas.get(posicaoCartaSelecionada)
        val pontuacaoAtualizada = tvPontuacao.text.toString().toInt() + cartaSelecionada.points
        tvPontuacao.text = pontuacaoAtualizada.toString()
        exibeMensagem(pontuacaoAtualizada)

        if (pontuacaoAtualizada > 21) {
            iniciarPartida()
        } else {
            cartas.removeAt(posicaoCartaSelecionada)
            ivCard.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    cartaSelecionada.resourceId
                )
            )
        }
    }

    private fun getBaralho(): MutableList<Card> {
        return mutableListOf<Card>(
            Card(R.drawable.as_de_espada, 1),
            Card(R.drawable.dois_de_espada, 2),
            Card(R.drawable.tres_de_espada, 3),
            Card(R.drawable.quatro_de_espada, 4),
            Card(R.drawable.cinco_de_espada, 5),
            Card(R.drawable.seis_de_espada, 6),
            Card(R.drawable.sete_de_espada, 7),
            Card(R.drawable.oito_de_espada, 8),
            Card(R.drawable.nove_de_espada, 9),
            Card(R.drawable.dez_de_espada, 10),
            Card(R.drawable.valete_de_espada, 10),
            Card(R.drawable.dama_de_espada, 10),
            Card(R.drawable.rei_de_espada, 10)
        )
    }

    private fun controlarBotoes() {
        val time = Toast.LENGTH_LONG
        habilitarBotoes(false)
        Handler().postDelayed({
            habilitarBotoes(true)
        }, 2500)
    }

    private fun habilitarBotoes(habilitar: Boolean) {
        btProximaCard.isEnabled = habilitar
        btRecomecar.isEnabled = habilitar
    }

    private fun exibeMensagem(pontuacao: Int) {
        when {
            pontuacao == 21 -> {
                CustomToast.success(this, "Voc?? atingiu a melhor pontua????o. Hora de parar :)")
            }
            pontuacao > 21 -> {
                CustomToast.error(this, "Voc?? perdeu fez $pontuacao e perdeu")
            }
            pontuacao > 11 -> {
                CustomToast.warning(
                    this,
                    "Cuidado, dependendo da carta que comprar voc?? poder?? perder"
                )
            } else -> {
                CustomToast.info(this, "Voc?? ainda pode jogar com seguran??a")
            }
        }
        controlarBotoes()
    }
}

