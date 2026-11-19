package br.com.fiap.bairronamesa.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.bairronamesa.data.Oferta
import br.com.fiap.bairronamesa.data.StatusOferta
import br.com.fiap.bairronamesa.ui.theme.Corpo
import br.com.fiap.bairronamesa.ui.theme.Erro
import br.com.fiap.bairronamesa.ui.theme.Legenda
import br.com.fiap.bairronamesa.ui.theme.Rotulo
import br.com.fiap.bairronamesa.ui.theme.Tinta
import br.com.fiap.bairronamesa.ui.theme.TintaSuave
import br.com.fiap.bairronamesa.ui.theme.Titulo3
import br.com.fiap.bairronamesa.ui.theme.VerdeHorta

@Composable
fun OfertaCard(oferta: Oferta, onAvancar: () -> Unit, onEditar: () -> Unit, onExcluir: () -> Unit) {
    Cartao(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(oferta.produto, style = Titulo3, modifier = Modifier.weight(1f))
                StatusChip(oferta.status)
            }
            Text("${oferta.categoria} · ${oferta.feira}", style = Legenda, color = TintaSuave)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${kg(oferta.quantidadeKg)} · ${dinheiro(oferta.precoKg)}/kg", style = Corpo, modifier = Modifier.weight(1f))
                Text(dinheiro(oferta.valorTotal), style = Corpo, fontWeight = FontWeight.Bold, color = VerdeHorta)
            }
            Row(
                Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val botao = Modifier.weight(1f).heightIn(min = 44.dp)
                val texto = Rotulo.copy(fontSize = 15.sp)
                when (oferta.status) {
                    StatusOferta.DISPONIVEL -> FilledTonalButton(onClick = onAvancar, modifier = botao) {
                        Text("Marcar como reservado", style = texto)
                    }
                    StatusOferta.RESERVADO -> Button(onClick = onAvancar, modifier = botao) {
                        Text("Marcar como vendido", style = texto)
                    }
                    else -> Spacer(Modifier.weight(1f))
                }
                IconButton(onClick = onEditar) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Editar ${oferta.produto}", tint = Tinta)
                }
                IconButton(onClick = onExcluir) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Excluir ${oferta.produto}", tint = Erro)
                }
            }
        }
    }
}
