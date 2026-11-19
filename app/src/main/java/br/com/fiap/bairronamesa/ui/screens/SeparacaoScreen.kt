package br.com.fiap.bairronamesa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.fiap.bairronamesa.ui.components.Cartao
import br.com.fiap.bairronamesa.ui.components.EstadoVazio
import br.com.fiap.bairronamesa.ui.components.TopBarVoltar
import br.com.fiap.bairronamesa.ui.components.kg
import br.com.fiap.bairronamesa.ui.theme.Atkinson
import br.com.fiap.bairronamesa.ui.theme.Bricolage
import br.com.fiap.bairronamesa.ui.theme.Corpo
import br.com.fiap.bairronamesa.ui.theme.Creme100
import br.com.fiap.bairronamesa.ui.theme.Creme300
import br.com.fiap.bairronamesa.ui.theme.Legenda
import br.com.fiap.bairronamesa.ui.theme.Linha
import br.com.fiap.bairronamesa.ui.theme.Milho
import br.com.fiap.bairronamesa.ui.theme.MilhoClaro
import br.com.fiap.bairronamesa.ui.theme.Rotulo
import br.com.fiap.bairronamesa.ui.theme.TintaSuave
import br.com.fiap.bairronamesa.ui.theme.Titulo3
import br.com.fiap.bairronamesa.ui.theme.VerdeHortaForte
import br.com.fiap.bairronamesa.ui.theme.VerdeMata
import br.com.fiap.bairronamesa.viewmodel.OfertaViewModel

@Composable
fun SeparacaoScreen(vm: OfertaViewModel, onVoltar: () -> Unit) {
    val porFeira by vm.reservadasPorFeira.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(Unit) { vm.eventos.collect { snackbar.showSnackbar(it) } }

    Scaffold(
        topBar = { TopBarVoltar("Lista de separação", onVoltar) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        val grupos = porFeira ?: return@Scaffold // carregando
        if (grupos.isEmpty()) {
            EstadoVazio(
                "Nenhuma reserva para separar.",
                "Quando os moradores reservarem, os itens aparecem aqui.",
                Modifier.padding(padding).fillMaxSize()
            )
            return@Scaffold
        }
        LazyColumn(
            Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    Modifier.fillMaxWidth().background(MilhoClaro, RoundedCornerShape(14.dp)).padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Rounded.Checklist, contentDescription = null)
                    Text("Colha só o que foi reservado. Assim nada se perde na praça.", style = Corpo)
                }
            }
            grupos.forEach { (feira, ofertas) ->
                item(key = feira) {
                    Cartao(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier.fillMaxWidth().background(VerdeMata).padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Outlined.Place, contentDescription = null, tint = Milho, modifier = Modifier.size(22.dp))
                            Text(feira, fontFamily = Bricolage, fontWeight = FontWeight.Bold, fontSize = 19.sp, lineHeight = 24.sp, color = Creme100)
                        }
                        ofertas.forEachIndexed { i, oferta ->
                            if (i > 0) HorizontalDivider(color = Linha)
                            Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(oferta.produto, style = Titulo3, modifier = Modifier.weight(1f))
                                    Text(kg(oferta.quantidadeKg), fontFamily = Atkinson, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                                }
                                BotaoContornado("Marcar como vendido", { vm.avancarStatus(oferta) }, Modifier.fillMaxWidth())
                            }
                        }
                        Row(
                            Modifier.fillMaxWidth().background(Creme300).padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total para separar", style = Rotulo, modifier = Modifier.weight(1f))
                            Text(
                                kg(ofertas.sumOf { it.quantidadeKg }),
                                fontFamily = Bricolage, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = VerdeHortaForte
                            )
                        }
                    }
                }
            }
            item {
                Text(
                    "Aparecem aqui só as ofertas com status Reservado.",
                    style = Legenda, color = TintaSuave, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
