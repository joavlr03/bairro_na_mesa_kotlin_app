package br.com.fiap.bairronamesa.ui.screens

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.fiap.bairronamesa.ui.components.ResumoCard
import br.com.fiap.bairronamesa.ui.components.dinheiro
import br.com.fiap.bairronamesa.ui.components.kg
import br.com.fiap.bairronamesa.ui.theme.Corpo
import br.com.fiap.bairronamesa.ui.theme.Creme100
import br.com.fiap.bairronamesa.ui.theme.Legenda
import br.com.fiap.bairronamesa.ui.theme.Milho
import br.com.fiap.bairronamesa.ui.theme.MilhoClaro
import br.com.fiap.bairronamesa.ui.theme.Rotulo
import br.com.fiap.bairronamesa.ui.theme.Tinta
import br.com.fiap.bairronamesa.ui.theme.Titulo1
import br.com.fiap.bairronamesa.ui.theme.VerdeBroto
import br.com.fiap.bairronamesa.ui.theme.VerdeHorta
import br.com.fiap.bairronamesa.ui.theme.VerdeHortaForte
import br.com.fiap.bairronamesa.ui.theme.VerdeMata
import br.com.fiap.bairronamesa.viewmodel.OfertaViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InicioScreen(vm: OfertaViewModel, onNovaOferta: () -> Unit, onMinhasOfertas: () -> Unit, onSeparacao: () -> Unit) {
    val resumo by vm.resumo.collectAsStateWithLifecycle()
    IconesClarosNaBarraDeStatus()

    Column(Modifier.fillMaxSize().background(Creme100)) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            // Cabeçalho
            Column(
                Modifier.fillMaxWidth().background(VerdeMata).statusBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 22.dp)
            ) {
                Row(
                    Modifier.background(Milho, CircleShape).padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Eco, contentDescription = null, tint = Tinta, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Área do Produtor", style = Legenda, fontWeight = FontWeight.Bold, color = Tinta)
                }
                Spacer(Modifier.height(12.dp))
                Text("Bairro na Mesa", style = Titulo1, color = Creme100, modifier = Modifier.semantics { heading() })
                Spacer(Modifier.height(8.dp))
                Text(
                    "Publique sua colheita da semana, acompanhe as reservas e leve para a praça só o necessário.",
                    style = Corpo, color = Creme100
                )
            }

            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    Modifier.fillMaxWidth().background(VerdeBroto, RoundedCornerShape(14.dp)).padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Rounded.Eco, contentDescription = null, tint = VerdeHortaForte, modifier = Modifier.padding(top = 2.dp))
                    Column {
                        Text("Para quem é", style = Corpo, fontWeight = FontWeight.Bold)
                        Text("Agricultores familiares e hortas comunitárias da Grande SP.", style = Corpo)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ResumoCard("Kg ofertados", kg(resumo.kgOfertados), Modifier.weight(1f))
                        ResumoCard("Kg vendidos", kg(resumo.kgVendidos), Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ResumoCard("Renda estimada", dinheiro(resumo.rendaEstimada), Modifier.weight(1f), destaque = true)
                        ResumoCard("Ofertas reservadas", resumo.totalReservadas.toString(), Modifier.weight(1f))
                    }
                }

                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("ODS 2 · Fome Zero", "ODS 12 · Consumo Responsável").forEach {
                        Text(
                            it, style = Legenda, fontWeight = FontWeight.Bold,
                            modifier = Modifier.background(MilhoClaro, CircleShape).padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Rodapé fixo com as ações
        Column(Modifier.navigationBarsPadding().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onNovaOferta, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Icon(Icons.Rounded.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Nova oferta", style = Rotulo)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                BotaoContornado("Minhas ofertas", onMinhasOfertas, Modifier.weight(1f))
                BotaoContornado("Lista de separação", onSeparacao, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun BotaoContornado(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = 48.dp),
        border = BorderStroke(2.dp, VerdeHorta),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeHortaForte),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Text(texto, style = Rotulo, textAlign = TextAlign.Center)
    }
}

/** O cabeçalho é verde-escuro: enquanto a Início está aberta, os ícones da barra de status ficam claros. */
@Composable
private fun IconesClarosNaBarraDeStatus() {
    val view = LocalView.current
    if (view.isInEditMode) return
    DisposableEffect(Unit) {
        val controle = WindowCompat.getInsetsController((view.context as Activity).window, view)
        controle.isAppearanceLightStatusBars = false
        onDispose { controle.isAppearanceLightStatusBars = true }
    }
}
