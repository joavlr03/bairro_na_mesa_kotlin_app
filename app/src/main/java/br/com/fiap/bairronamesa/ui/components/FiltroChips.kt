package br.com.fiap.bairronamesa.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.bairronamesa.data.StatusOferta
import br.com.fiap.bairronamesa.ui.theme.Corpo
import br.com.fiap.bairronamesa.ui.theme.Creme100
import br.com.fiap.bairronamesa.ui.theme.Creme200
import br.com.fiap.bairronamesa.ui.theme.LinhaCampo
import br.com.fiap.bairronamesa.ui.theme.Tinta
import br.com.fiap.bairronamesa.ui.theme.VerdeHorta

/** Todas · Disponível · Reservado · Vendido (null = Todas). */
@Composable
fun FiltroChips(selecionado: String?, onSelecionar: (String?) -> Unit) {
    val opcoes = listOf(null, StatusOferta.DISPONIVEL, StatusOferta.RESERVADO, StatusOferta.VENDIDO)
    Row(
        Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opcoes.forEach { status ->
            val ativo = status == selecionado
            FilterChip(
                selected = ativo,
                onClick = { onSelecionar(status) },
                label = {
                    Text(
                        if (status == null) "Todas" else rotuloStatus(status),
                        style = Corpo.copy(fontSize = 15.sp),
                        fontWeight = if (ativo) FontWeight.Bold else FontWeight.Normal
                    )
                },
                leadingIcon = if (ativo) {
                    { Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null,
                modifier = Modifier.height(36.dp),
                shape = RoundedCornerShape(8.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Creme200,
                    labelColor = Tinta,
                    selectedContainerColor = VerdeHorta,
                    selectedLabelColor = Creme100,
                    selectedLeadingIconColor = Creme100,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = ativo,
                    borderColor = LinhaCampo,
                    selectedBorderColor = Color.Transparent,
                    borderWidth = 1.dp,
                ),
            )
        }
    }
}
