package br.com.fiap.bairronamesa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val Cores = lightColorScheme(
    primary = VerdeHorta,
    onPrimary = Creme100,
    secondary = TerraTexto,
    onSecondary = Creme200,
    secondaryContainer = VerdeBroto, // botão tonal
    onSecondaryContainer = VerdeHortaForte,
    tertiary = Milho,
    onTertiary = Tinta,
    background = Creme100,
    onBackground = Tinta,
    surface = Creme100,
    onSurface = Tinta,
    surfaceContainerLowest = Creme200,
    onSurfaceVariant = TintaSuave,
    outline = LinhaCampo,
    outlineVariant = Linha,
    error = Erro,
    onError = Creme200,
)

// Só tema claro, sem cores dinâmicas: a identidade visual é fixa
@Composable
fun BairroNaMesaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Cores, typography = AppTypography, content = content)
}
