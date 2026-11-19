package br.com.fiap.bairronamesa.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.com.fiap.bairronamesa.R

val Bricolage = FontFamily(
    Font(R.font.bricolage_grotesque_bold, FontWeight.Bold),
    Font(R.font.bricolage_grotesque_extrabold, FontWeight.ExtraBold),
)
val Atkinson = FontFamily(
    Font(R.font.atkinson_hyperlegible_regular, FontWeight.Normal),
    Font(R.font.atkinson_hyperlegible_bold, FontWeight.Bold),
)

val Titulo1 = TextStyle(fontFamily = Bricolage, fontWeight = FontWeight.ExtraBold, fontSize = 34.sp, lineHeight = 40.sp, letterSpacing = (-0.5).sp)
val Numero = TextStyle(fontFamily = Bricolage, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, lineHeight = 32.sp)
val TituloTopBar = TextStyle(fontFamily = Bricolage, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp)
val Titulo3 = TextStyle(fontFamily = Bricolage, fontWeight = FontWeight.Bold, fontSize = 21.sp, lineHeight = 28.sp)
val Corpo = TextStyle(fontFamily = Atkinson, fontWeight = FontWeight.Normal, fontSize = 17.sp, lineHeight = 24.sp)
val Rotulo = TextStyle(fontFamily = Atkinson, fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 20.sp)
val Legenda = TextStyle(fontFamily = Atkinson, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp)

// Estilos padrão do Material apontam para os da especificação (nunca abaixo de 14 sp)
val AppTypography = Typography(
    displaySmall = Titulo1,
    headlineMedium = Numero,
    titleLarge = TituloTopBar,
    titleMedium = Titulo3,
    titleSmall = Rotulo,
    bodyLarge = Corpo,
    bodyMedium = Corpo,
    bodySmall = Legenda,
    labelLarge = Rotulo,
    labelMedium = Legenda,
    labelSmall = Legenda,
)
