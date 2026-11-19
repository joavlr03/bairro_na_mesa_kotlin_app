package br.com.fiap.bairronamesa.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ofertas")
data class Oferta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val produto: String,
    val categoria: String,
    val quantidadeKg: Double,
    val precoKg: Double,
    val feira: String,
    val status: String = StatusOferta.DISPONIVEL,
    val criadaEm: Long = System.currentTimeMillis()
) {
    val valorTotal: Double get() = quantidadeKg * precoKg
}

object StatusOferta {
    const val DISPONIVEL = "DISPONIVEL"
    const val RESERVADO = "RESERVADO"
    const val VENDIDO = "VENDIDO"
    fun proximo(atual: String) = when (atual) {
        DISPONIVEL -> RESERVADO
        RESERVADO -> VENDIDO
        else -> VENDIDO
    }
}
