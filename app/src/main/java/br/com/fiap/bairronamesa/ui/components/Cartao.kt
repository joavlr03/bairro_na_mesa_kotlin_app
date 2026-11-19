package br.com.fiap.bairronamesa.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.fiap.bairronamesa.ui.theme.Creme200
import br.com.fiap.bairronamesa.ui.theme.Linha
import br.com.fiap.bairronamesa.ui.theme.Tinta

/** Card branco padrão: canto 14 dp, borda 1 dp Linha, elevação baixa. */
@Composable
fun Cartao(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Creme200, contentColor = Tinta),
        border = BorderStroke(1.dp, Linha),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        content = content
    )
}
