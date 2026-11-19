package br.com.fiap.bairronamesa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.fiap.bairronamesa.data.StatusOferta
import br.com.fiap.bairronamesa.ui.theme.Creme300
import br.com.fiap.bairronamesa.ui.theme.Legenda
import br.com.fiap.bairronamesa.ui.theme.MilhoClaro
import br.com.fiap.bairronamesa.ui.theme.Tinta
import br.com.fiap.bairronamesa.ui.theme.VerdeBroto
import br.com.fiap.bairronamesa.ui.theme.VerdeHortaForte

fun rotuloStatus(status: String) = when (status) {
    StatusOferta.DISPONIVEL -> "Disponível"
    StatusOferta.RESERVADO -> "Reservado"
    else -> "Vendido"
}

/** Status sempre com ícone + texto (nunca só cor). */
@Composable
fun StatusChip(status: String) {
    val (fundo, cor, icone) = when (status) {
        StatusOferta.DISPONIVEL -> Triple(VerdeBroto, VerdeHortaForte, Icons.Rounded.RadioButtonUnchecked)
        StatusOferta.RESERVADO -> Triple(MilhoClaro, Tinta, Icons.Rounded.Schedule)
        else -> Triple(Creme300, Tinta, Icons.Rounded.Check)
    }
    Row(
        Modifier.background(fundo, CircleShape).padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icone, contentDescription = null, tint = cor, modifier = Modifier.size(14.dp))
        Text(rotuloStatus(status), style = Legenda, fontWeight = FontWeight.Bold, color = cor)
    }
}
