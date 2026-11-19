# Especificação visual das telas — Bairro na Mesa (App do Produtor)

Este arquivo descreve o mockup aprovado. O app em Jetpack Compose deve ficar **visualmente igual** a ele.
Fontes de verdade, nesta ordem:
1. Este arquivo (cores, medidas, textos, componentes).
2. `prints/*.png` (aparência final).
3. `telas/*.dc.html` (HTML do mockup: valores exatos de px, cores e textos). É só referência: **não** é para usar WebView nem copiar HTML; tudo vira Compose nativo.

A lógica, os dados, a arquitetura e as rotas seguem `docs/PLANO.md`. Se houver conflito: comportamento → PLANO.md; aparência → este arquivo.

---

## 1. Tema (criar em `ui/theme/`)

### Cores (`Color.kt`)
| Nome | Hex | Uso |
|---|---|---|
| VerdeHorta | #2F7D4F | `primary`; botões primários, chip selecionado, valores em R$ |
| VerdeHortaForte | #246640 | texto verde pequeno, botão contornado (texto) |
| VerdeMata | #173D28 | cabeçalho da Início, cabeçalho de feira na Separação |
| VerdeBroto | #E3F1E7 | card "Para quem é", prévia do valor, chip Disponível, botão tonal |
| TerraTexto | #B4461C | `secondary` |
| Milho | #F2B630 | `tertiary`; FAB, selo "Área do Produtor" |
| MilhoClaro | #FDF0CC | chips de ODS, chip Reservado, aviso da Separação |
| Creme100 | #FBF5EA | `background` / `surface` (fundo de todas as telas) |
| Creme200 | #FFFFFF | cards, campos, diálogo |
| Creme300 | #F1E8D8 | chip Vendido, rodapé de total, círculo do estado vazio |
| Tinta | #1E2B23 | `onBackground` / `onSurface` (texto principal) |
| TintaSuave | #5C665F | texto secundário/legendas |
| Linha | #E2D7C4 | borda de cards e divisória da TopAppBar |
| LinhaCampo | #8A8F84 | borda de campo e de chip não selecionado |
| Erro | #B42318 | `error`; borda e mensagem de erro, botão Excluir |
| ErroFundo | #FDECEA | círculo do ícone no diálogo de exclusão |

Usar só tema claro (`dynamicColor = false`).

### Tipografia (`Type.kt`)
Baixar e colocar em `app/src/main/res/font/`:
- `bricolage_grotesque_bold.ttf` (700) e `bricolage_grotesque_extrabold.ttf` (800) — títulos e números
- `atkinson_hyperlegible_regular.ttf` (400) e `atkinson_hyperlegible_bold.ttf` (700) — todo o resto

Se a equipe não tiver os .ttf, usar `FontFamily.Default` e deixar um TODO (não usar downloadable fonts).

| Estilo | Fonte | Tamanho / altura | Peso | Uso |
|---|---|---|---|---|
| titulo1 | Bricolage | 34 / 40 sp, letterSpacing -0.5 | 800 | "Bairro na Mesa" na Início |
| numero | Bricolage | 28 / 32 sp | 800 | valores dos cards de impacto |
| tituloTopBar | Bricolage | 22 / 28 sp | 700 | título da TopAppBar |
| titulo3 | Bricolage | 21 / 28 sp | 700 | nome do produto no card |
| corpo | Atkinson | 17 / 24 sp | 400 | texto padrão, campos |
| rotulo | Atkinson | 16 / 20 sp | 700 | botões, rótulos de campo |
| legenda | Atkinson | 14 / 20 sp | 400 | metadados, mensagens de erro |

Nunca usar texto abaixo de 14 sp.

### Formas e medidas
- Card: canto 14 dp, borda 1 dp `Linha`, elevação baixa (1–2 dp).
- Botões: formato pílula (`RoundedCornerShape(50)`), altura 52 dp (primário) ou 48/44 dp (secundário/no card).
- Campos e chips de filtro: canto 8 dp.
- Diálogo: canto 24 dp. FAB: 64 dp, canto 20 dp, cor `Milho`, ícone `Tinta`.
- Margem lateral das telas: 16 dp. Espaço entre cards: 14 dp.
- Área de toque mínima: 44–48 dp (o público tem pouca prática com celular).

### Ícones (Material Icons Extended, versão Rounded)
Voltar `ArrowBack`, adicionar `Add`, editar `Edit`, excluir `Delete`, check `Check`, dropdown `ExpandMore`, reservado `Schedule`, erro `Error`/`Info`, feira `Place`, lista `Checklist`, broto `Eco`, estado vazio `ShoppingBasket`.
Regra: **status e erro nunca só por cor** — sempre ícone + texto.

---

## 2. Componentes reutilizáveis (`ui/components/`)

**TopBarVoltar(titulo, onVoltar)** — altura 64 dp, fundo `Creme100`, divisória inferior 1 dp `Linha`, `IconButton` 48 dp com `ArrowBack` e título em `tituloTopBar`.

**StatusChip(status)** — pílula, padding 10×4 dp, texto `legenda` em negrito, ícone 14 dp:
| Status | Fundo | Texto | Ícone | Rótulo |
|---|---|---|---|---|
| DISPONIVEL | VerdeBroto | VerdeHortaForte | círculo (`Circle` outline) | Disponível |
| RESERVADO | MilhoClaro | Tinta | `Schedule` | Reservado |
| VENDIDO | Creme300 | Tinta | `Check` | Vendido |

**OfertaCard(oferta, onAvancar, onEditar, onExcluir)** — Card branco, padding 16×14 dp, coluna com espaço 6 dp:
1. Linha: produto (`titulo3`) à esquerda, `StatusChip` à direita.
2. `"{categoria} · {feira}"` em `legenda` `TintaSuave`.
3. Linha: `"12 kg · R$ 8,00/kg"` (`corpo`) à esquerda; total `"R$ 96,00"` em `corpo` negrito `VerdeHorta` à direita.
4. Linha de ações (topo 4 dp): botão que ocupa o espaço (`weight(1f)`, 44 dp) + `IconButton` Editar (Tinta) + `IconButton` Excluir (Erro).
   - DISPONIVEL → botão **tonal** (fundo VerdeBroto, texto VerdeHortaForte) "Marcar como reservado".
   - RESERVADO → botão **primário** (VerdeHorta / Creme100) "Marcar como vendido".
   - VENDIDO → sem botão; os dois ícones alinhados à direita.

**ResumoCard(rotulo, valor, destaque)** — Card branco, padding 14×12 dp; rótulo em `legenda` `TintaSuave`, valor em `numero` (cor `VerdeHorta` só na renda).

**CampoTexto / CampoDropdown** — rótulo **acima** do campo (Text `rotulo`, não label flutuante), campo 52 dp, fundo branco, borda 1 dp `LinhaCampo` (2 dp `VerdeHorta` com foco; 2 dp `Erro` com erro). Erro abaixo: ícone `Error` 16 dp + texto `legenda` cor `Erro`. Dropdown usa `ExposedDropdownMenuBox` com `ExpandMore`; vazio mostra "Escolha" em `TintaSuave`.

**EstadoVazio(titulo, texto)** — centralizado: círculo 96 dp `Creme300` com `ShoppingBasket` 48 dp `TintaSuave`, título Bricolage 22 sp, texto `corpo` `TintaSuave`.

**FiltroChips** — `FilterChip` altura 36 dp, canto 8 dp. Selecionado: fundo VerdeHorta, texto Creme100 negrito, ícone `Check`. Não selecionado: fundo branco, borda LinhaCampo.

---

## 3. Telas

Formatação de dinheiro: `"R$ %.2f"` com vírgula (`Locale("pt","BR")`), ex.: `R$ 96,00`. Kg sem casas quando inteiro (`12 kg`).

### 3.1 InicioScreen — `prints/01-inicio.png`, `telas/Main.dc.html`
Sem TopAppBar. Coluna em tela cheia, fundo Creme100:
1. **Cabeçalho** fundo `VerdeMata`, padding 16 dp lateral, 28 dp topo, 22 dp base:
   - Selo pílula `Milho` com ícone `Eco` + "Área do Produtor" (Tinta, legenda negrito).
   - "Bairro na Mesa" (`titulo1`, Creme100).
   - "Publique sua colheita da semana, acompanhe as reservas e leve para a praça só o necessário." (`corpo`, Creme100).
2. Conteúdo (padding 16 dp, espaço 16 dp):
   - Card `VerdeBroto` canto 14: ícone `Eco` VerdeHortaForte + "Para quem é" (negrito) / "Agricultores familiares e hortas comunitárias da Grande SP."
   - Grade 2×2 de `ResumoCard` (espaço 12 dp): Kg ofertados · Kg vendidos · Renda estimada (verde) · Ofertas reservadas. Valores vêm de `resumo` do ViewModel (seed: 62 kg, 20 kg, R$ 100,00, 1).
   - Linha de chips `MilhoClaro`: "ODS 2 · Fome Zero" e "ODS 12 · Consumo Responsável".
   - No rodapé (empurrado com `Spacer(weight(1f))`): botão primário largura total "Nova oferta" com ícone `Add`; abaixo, dois botões contornados lado a lado (borda 2 dp VerdeHorta, texto VerdeHortaForte): "Minhas ofertas" e "Lista de separação".

### 3.2 ListaOfertasScreen — `prints/02-ofertas.png`, `telas/Ofertas.dc.html`
- `Scaffold` com `TopBarVoltar("Minhas ofertas")`, `SnackbarHost` e FAB (`Add`, Milho) no canto inferior direito.
- `FiltroChips` em linha rolável horizontal: Todas · Disponível · Reservado · Vendido.
- `LazyColumn` de `OfertaCard` (padding 16 dp lateral, 96 dp no fim para o FAB não cobrir o último card).
- Lista vazia: `EstadoVazio` ("Nenhuma oferta por aqui ainda." / "Toque em + para publicar sua colheita."). Com filtro ativo: ver 3.7.

### 3.3 CadastroOfertaScreen — `prints/03-cadastro.png`, `telas/Cadastro.dc.html`
- `TopBarVoltar("Nova oferta")` ou "Editar oferta".
- Coluna rolável, padding 16 dp, espaço 14 dp: Produto · Categoria (dropdown) · **Quantidade (kg) e Preço por kg (R$) lado a lado** (2 colunas, espaço 12 dp, teclado `KeyboardType.Decimal`) · Feira (dropdown).
- Card `VerdeBroto` canto 14: "Valor total estimado" à esquerda e o valor (Bricolage 24 sp 800, VerdeHortaForte) à direita, atualizado ao vivo.
- Legenda: "A oferta entra como Disponível. Os moradores reservam pelo app."
- No rodapé: botão primário largura total "Salvar oferta" com ícone `Check`.

### 3.4 Cadastro com erros — `prints/05-validacao.png`, `telas/Validacao.dc.html`
Mesma tela 3.3 depois de tocar em Salvar com dados inválidos: campos com borda `Erro` 2 dp e mensagem abaixo (ícone + texto). Mensagens exatas: "Informe o nome do produto", "Escolha uma categoria", "Informe uma quantidade maior que zero", "Informe um preço válido", "Escolha a feira". Sem prévia de valor quando a conta não é válida.

### 3.5 SeparacaoScreen — `prints/04-separacao.png`, `telas/Separacao.dc.html`
- `TopBarVoltar("Lista de separação")`.
- Aviso `MilhoClaro` canto 14 com ícone `Checklist`: "Colha só o que foi reservado. Assim nada se perde na praça."
- Para cada feira (de `reservadasPorFeira`), um Card branco canto 14 com:
  - Cabeçalho fundo `VerdeMata`: ícone `Place` (Milho) + nome da feira (Bricolage 19 sp, Creme100).
  - Uma linha por oferta (padding 16×14, divisória `Linha`): produto (`titulo3`) à esquerda e kg (19 sp negrito) à direita; abaixo, botão contornado largura total "Marcar como vendido".
  - Rodapé fundo `Creme300`: "Total para separar" e o total em kg (Bricolage 22 sp 800, VerdeHortaForte).
- Legenda centralizada: "Aparecem aqui só as ofertas com status Reservado."
- Sem reservas: `EstadoVazio` ("Nenhuma reserva para separar." / "Quando os moradores reservarem, os itens aparecem aqui.").

### 3.6 Diálogo de exclusão — `prints/06-excluir.png`, `telas/Excluir.dc.html`
`AlertDialog` sobre a lista (canto 24 dp, fundo branco):
- Ícone: círculo 48 dp `ErroFundo` com `Delete` em `Erro`.
- Título: "Excluir a oferta de {produto}?" (Bricolage 24 sp).
- Texto: "A oferta some da lista e da feira. Não dá para desfazer."
- Botões à direita: "Cancelar" (TextButton, VerdeHortaForte) e "Excluir" (botão pílula fundo `Erro`, texto branco).

### 3.7 Filtro sem resultado — `prints/07-vazio.png`, `telas/Vazio.dc.html`
Lista com o chip do filtro selecionado e `EstadoVazio`: título "Nenhuma oferta com o status {Status}." e texto "Toque em Todas para ver a lista inteira ou em + para publicar sua colheita." O FAB continua visível.

---

## 4. Navegação entre telas (igual ao protótipo)
- Início → "Nova oferta" → Cadastro; "Minhas ofertas" → Lista; "Lista de separação" → Separação.
- Lista → FAB → Cadastro; ícone Editar → `cadastro?id=X`; ícone Excluir → diálogo.
- Cadastro → Salvar válido → volta para a Lista + Snackbar "Oferta salva".
- Toda seta de voltar → `popBackStack()`.
