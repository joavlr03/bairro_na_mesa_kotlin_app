package br.com.fiap.bairronamesa.ui.components

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val BR = DecimalFormatSymbols(Locale.forLanguageTag("pt-BR"))

/** R$ 1.234,50 */
fun dinheiro(valor: Double) = "R$ " + DecimalFormat("#,##0.00", BR).format(valor)

/** 12 kg · 12,5 kg (sem casas quando inteiro) */
fun kg(valor: Double) = DecimalFormat("#,##0.##", BR).format(valor) + " kg"

/** Valores para preencher os campos na edição (sem separador de milhar, que o campo não entende) */
fun numeroKg(valor: Double): String = DecimalFormat("0.##", BR).format(valor)
fun numeroPreco(valor: Double): String = DecimalFormat("0.00", BR).format(valor)
