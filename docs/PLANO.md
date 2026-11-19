# Plano de Desenvolvimento — Bairro na Mesa (App do Produtor · Android)

> **Uso deste documento:** especificação completa para desenvolver o app Android da disciplina *Android Kotlin Developer* (GS 2026.2 – CitizenTech). Pode ser entregue diretamente ao Claude com o pedido: *"Desenvolva o app seguindo este plano"*.
> **Prazo:** 19/11/2026, 23h59 (horário de Brasília).
> **Ambiente:** Windows 11 · Android Studio 2024.2 (Ladybug) ou mais novo · emulador Android.

---

## 1. Contexto e posicionamento

O **Bairro na Mesa** transforma praças da periferia em pontos semanais de comida acessível e cidadania. O app Flutter já desenvolvido atende o **perfil morador** (catálogo, cesta, pré-pedido, QR code, mutirão, painel de impacto).

Este app Android cobre o **outro lado da feira: o produtor**. Ele tira do roadmap o item *"painel do produtor com lista de separação"*.

| Item | Definição |
|---|---|
| Nome do app | **Bairro na Mesa** (subtítulo na tela inicial: *"Área do Produtor"*) |
| Pacote | `br.com.fiap.bairronamesa` |
| Objetivo | Permitir que o agricultor familiar publique a oferta da semana, acompanhe o que foi reservado e vendido e separe só o necessário para a feira |
| Público beneficiado | Agricultores familiares e hortas comunitárias da Grande SP (persona: **Seu José**, de Mogi das Cruzes); indiretamente, as famílias que compram na praça |
| Problema | O pequeno produtor vende sem previsibilidade, depende de atravessadores e perde parte da colheita |
| ODS | **ODS 2 – Fome Zero (meta 2.3):** renda do pequeno produtor com venda direta. **ODS 12 – Consumo Responsável (meta 12.3):** colher só o reservado reduz o desperdício |

**Regra de honestidade (exigida pelo enunciado):** o app é um protótipo acadêmico com dados locais. Ele **não** está integrado à API nem ao app Flutter. O README deve dizer isso explicitamente.

---

## 2. Decisões já tomadas

| Decisão | Escolha | Motivo |
|---|---|---|
| Arquitetura | MVVM simples (data / viewmodel / ui / navigation) | Atende o Req. 6 (separação de camadas e ViewModel) sem complexidade extra |
| Persistência | Room com KSP | Obrigatório (Req. 5); o KSP é mais rápido e é o padrão atual (o kapt está em manutenção) |
| Estado | `Flow` no DAO → `StateFlow` no ViewModel → `collectAsStateWithLifecycle` | A tela se atualiza sozinha quando o banco muda (Req. 4 e 6) |
| Injeção de dependência | Manual (`ViewModelProvider.Factory` + `Application`) | Hilt não vale ponto e aumenta o risco de erro de build |
| Idioma do código | Português (`Oferta`, `quantidadeKg`, `precoKg`) | Igual às entidades do resumo do projeto |
| Status | `String` com constantes em um `object StatusOferta` | Dispensa `TypeConverter` e evita erro de compilação |
| Dados iniciais | Banco criado com **3 ofertas de exemplo** (seed no `RoomDatabase.Callback.onCreate`) | A lista já aparece preenchida; o seed roda só na criação do banco, então não sobrescreve os dados do usuário |
| Feiras e categorias | Listas fixas em Kotlin (`DadosMock.kt`) | São dados de referência, não registros do usuário |
| Fora do escopo | Login, backend, Retrofit, mapa, notificações, Pix, Hilt | Opcionais sem pontuação (Regras de escopo do enunciado) |

---

## 3. Stack e versões

Versões compatíveis entre si e com o Android Studio Ladybug+. **Não aceitar o "Upgrade Assistant" do Android Studio antes da entrega**, porque atualizar versões no meio do desenvolvimento costuma quebrar o build.

| Item | Versão |
|---|---|
| compileSdk / targetSdk | 35 |
| minSdk | 26 (Android 8.0) |
| JDK | 17 (o embutido no Android Studio) |
| Android Gradle Plugin | 8.7.3 |
| Kotlin | 2.0.21 (com plugin `org.jetbrains.kotlin.plugin.compose`) |
| KSP | 2.0.21-1.0.28 |
| Compose BOM | 2024.12.01 (Material 3) |
| Navigation Compose | 2.8.5 |
| Lifecycle (ViewModel / runtime-compose) | 2.8.7 |
| Room (runtime, ktx, compiler) | 2.6.1 |
| Material Icons Extended | via BOM |

Configuração em `gradle/libs.versions.toml` (version catalog) e `build.gradle.kts` (Kotlin DSL), como o template *Empty Activity* atual do Android Studio já gera.

---

## 4. Modelo de dados

```kotlin
@Entity(tableName = "ofertas")
data class Oferta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val produto: String,        // obrigatório, 2 a 40 caracteres
    val categoria: String,      // uma de DadosMock.categorias
    val quantidadeKg: Double,   // > 0 e <= 1000
    val precoKg: Double,        // > 0 e <= 500
    val feira: String,          // uma de DadosMock.feiras
    val status: String = StatusOferta.DISPONIVEL,
    val criadaEm: Long = System.currentTimeMillis()
) {
    val valorTotal: Double get() = quantidadeKg * precoKg
}

object StatusOferta {
    const val DISPONIVEL = "DISPONIVEL"
    const val RESERVADO  = "RESERVADO"
    const val VENDIDO    = "VENDIDO"
    fun proximo(atual: String) = when (atual) {
        DISPONIVEL -> RESERVADO
        RESERVADO  -> VENDIDO
        else       -> VENDIDO
    }
}
```

**Dados mock (`DadosMock.kt`).** Se o app Flutter usar outros nomes, substituir para manter os dois apps coerentes no vídeo.
- **Feiras:** "Praça São Mateus · Sábado", "Praça Cidade Tiradentes · Sábado", "Praça do Itaim Paulista · Domingo"
- **Categorias:** Verduras, Legumes, Frutas, Ovos, Outros

**Seed (3 ofertas):**
| Produto | Categoria | kg | R$/kg | Feira | Status |
|---|---|---|---|---|---|
| Alface crespa | Verduras | 12 | 8,00 | Praça São Mateus | DISPONIVEL |
| Tomate | Legumes | 30 | 6,50 | Praça São Mateus | RESERVADO |
| Banana prata | Frutas | 20 | 5,00 | Praça Cidade Tiradentes | VENDIDO |

**Por que um seed com status diferentes:** a tela inicial já abre com os três cards de impacto preenchidos e a lista de separação já tem conteúdo para demonstrar.

---

## 5. Camada de dados

**`OfertaDao`**
| Função | Query / tipo |
|---|---|
| `listarTodas(): Flow<List<Oferta>>` | `SELECT * FROM ofertas ORDER BY criadaEm DESC` |
| `buscarPorId(id): Flow<Oferta?>` | `WHERE id = :id` |
| `listarReservadas(): Flow<List<Oferta>>` | `WHERE status = 'RESERVADO' ORDER BY feira, produto` |
| `inserir(oferta)` | `@Insert` (suspend) |
| `atualizar(oferta)` | `@Update` (suspend) |
| `excluir(oferta)` | `@Delete` (suspend) |

**`AppDatabase`:** `version = 1`, `exportSchema = false`, singleton com `Room.databaseBuilder(...)`, callback de seed no `onCreate` (inserção via coroutine em `Dispatchers.IO`).

**`OfertaRepository`:** encapsula o DAO. É criado na classe `BairroNaMesaApp : Application` e registrado no `AndroidManifest` (`android:name=".BairroNaMesaApp"`).

---

## 6. ViewModel e estado

**`OfertaViewModel(repository)`**, com Factory própria.

| Exposição | Tipo | Uso |
|---|---|---|
| `ofertas` | `StateFlow<List<Oferta>>` (via `stateIn`, `WhileSubscribed(5000)`) | Lista |
| `filtroStatus` | `StateFlow<String?>` (null = todos) | Chips de filtro |
| `ofertasFiltradas` | `combine(ofertas, filtroStatus)` | Lista exibida |
| `resumo` | `StateFlow<ResumoImpacto>` | Cards da tela inicial |
| `reservadasPorFeira` | `StateFlow<Map<String, List<Oferta>>>` | Lista de separação |
| `formState` | `MutableStateFlow<FormOfertaState>` | Campos, erros e modo criar/editar |
| `eventos` | `Channel<String>` → `Flow` | Mensagens de Snackbar (disparo único) |

```kotlin
data class ResumoImpacto(
    val kgOfertados: Double,      // soma de todas as ofertas
    val kgVendidos: Double,       // soma das VENDIDO
    val rendaEstimada: Double,    // soma de valorTotal das VENDIDO
    val totalReservadas: Int
)
```

**Ações:** `salvar()`, `carregarParaEdicao(id)`, `limparFormulario()`, `avancarStatus(oferta)`, `excluir(oferta)`, `definirFiltro(status)`.

**Por que o Snackbar passa por `Channel`:** mensagens são eventos de uma única vez. Se ficassem em um `StateFlow`, reapareceriam ao girar a tela.

---

## 7. Telas e navegação

**Rotas (Navigation Compose):**
| Rota | Tela |
|---|---|
| `inicio` | InicioScreen (start destination) |
| `ofertas` | ListaOfertasScreen |
| `cadastro?id={id}` | CadastroOfertaScreen (sem id = nova; com id = edição) |
| `separacao` | SeparacaoScreen |

Linguagem simples e textos curtos em todas as telas, porque o público tem pouca familiaridade digital.

Todas as telas, exceto a inicial, têm `TopAppBar` com seta de voltar (`navController.popBackStack()`). Depois de salvar, a tela de cadastro volta para a lista.

### 7.1 InicioScreen
- Cabeçalho: "Bairro na Mesa" e subtítulo "Área do Produtor".
- Texto curto: *"Publique sua colheita da semana, acompanhe as reservas e leve para a praça só o necessário."*
- Card **"Para quem é"**: agricultores familiares e hortas comunitárias.
- **Cards de impacto** (Row/Grid): kg ofertados · kg vendidos · renda estimada (R$) · ofertas reservadas.
- Indicação dos ODS: "ODS 2 · Fome Zero" e "ODS 12 · Consumo Responsável".
- Botões: **"Minhas ofertas"**, **"Nova oferta"** (primário) e **"Lista de separação"**.

### 7.2 ListaOfertasScreen
- `Scaffold` + `FloatingActionButton` (+) → `cadastro`.
- `FilterChip`s: Todas · Disponível · Reservado · Vendido.
- `LazyColumn` de **OfertaCard**: produto (título), categoria, `12 kg · R$ 8,00/kg`, total `R$ 96,00`, feira, **StatusChip**.
- Ações no card:
  - Botão **"Marcar como reservado"** ou **"Marcar como vendido"** (texto conforme o status; some quando VENDIDO) → Snackbar "Status atualizado".
  - Ícone **Editar** → `cadastro?id=X`.
  - Ícone **Excluir** → `AlertDialog` "Excluir a oferta de Tomate?" (Cancelar / Excluir) → Snackbar "Oferta excluída".
- **Estado vazio** (componente `EstadoVazio`): ícone + *"Nenhuma oferta por aqui ainda."* + *"Toque em + para publicar sua colheita."* Com filtro ativo: *"Nenhuma oferta com o status Vendido."*

### 7.3 CadastroOfertaScreen
- Título: "Nova oferta" ou "Editar oferta".
- Campos:
  | Campo | Componente | Validação | Mensagem de erro |
  |---|---|---|---|
  | Produto | `OutlinedTextField` | obrigatório, 2–40 caracteres | "Informe o nome do produto" |
  | Categoria | `ExposedDropdownMenuBox` | obrigatório | "Escolha uma categoria" |
  | Quantidade (kg) | `OutlinedTextField` numérico | > 0 e ≤ 1000 | "Informe uma quantidade maior que zero" |
  | Preço por kg (R$) | `OutlinedTextField` numérico | > 0 e ≤ 500 | "Informe um preço válido" |
  | Feira | `ExposedDropdownMenuBox` | obrigatório | "Escolha a feira" |
- Converter números com `replace(',', '.').toDoubleOrNull()`, porque o teclado brasileiro usa vírgula.
- Prévia ao vivo: *"Valor total estimado: R$ 96,00"*.
- Botão **Salvar**: valida tudo; se houver erro, mostra a mensagem abaixo do campo (`isError` + `supportingText`) e **não grava**. Se estiver válido, grava, mostra o Snackbar "Oferta salva" e volta para a lista.
- Na edição, o status atual é mantido.

### 7.4 SeparacaoScreen
- Ofertas **RESERVADO** agrupadas por feira (cabeçalho por feira com `stickyHeader` ou um `Text` em destaque).
- Cada item: produto e kg a separar; rodapé por feira com o total em kg.
- Botão em cada item: **"Marcar como vendido"**.
- Estado vazio: *"Nenhuma reserva para separar. Quando os moradores reservarem, os itens aparecem aqui."*

**Por que essa tela existe:** reproduz a jornada do resumo (*"na sexta recebe a lista de separação e colhe só o necessário"*) e liga o app ao ODS 12.3.

---

## 8. Estrutura de pastas

```
app/src/main/java/br/com/fiap/bairronamesa/
├── BairroNaMesaApp.kt            # Application: cria Database e Repository
├── MainActivity.kt               # ponto de entrada; chama AppNavHost()
├── data/
│   ├── Oferta.kt                 # @Entity + StatusOferta
│   ├── OfertaDao.kt
│   ├── AppDatabase.kt            # singleton + seed
│   ├── OfertaRepository.kt
│   └── DadosMock.kt              # feiras e categorias
├── viewmodel/
│   ├── OfertaViewModel.kt        # + ResumoImpacto, FormOfertaState
│   └── OfertaViewModelFactory.kt
├── navigation/
│   ├── Rotas.kt
│   └── AppNavHost.kt
└── ui/
    ├── components/ (OfertaCard.kt, StatusChip.kt, ResumoCard.kt, EstadoVazio.kt, CampoDropdown.kt)
    └── screens/ (InicioScreen.kt, ListaOfertasScreen.kt, CadastroOfertaScreen.kt, SeparacaoScreen.kt)
```

---

## 9. Mapa: requisito da rubrica → onde está atendido

| Req. | Pontos | Atendido por |
|---|---|---|
| 1. Contextualização e tela inicial | 1,0 | InicioScreen + seção "Problema/ODS" do README |
| 2. Navegação | 1,5 | 4 rotas no NavHost, voltar em todas as telas |
| 3. Interface e UX | 1,5 | Scaffold, Card, LazyColumn, TextField, Button, Snackbar, estados vazios |
| 4. Dados e interação | 2,0 | Oferta (7 atributos), cadastro, edição, avançar status, exclusão, validação |
| 5. Room | 1,5 | AppDatabase + teste de fechar/reabrir |
| 6. Código e estado | 1,5 | Pacotes separados, ViewModel com StateFlow, Flow do Room |
| 7. Documentação | 1,0 | README + prints da seção 11 |

---

## 10. README.md (estrutura obrigatória)

1. **Bairro na Mesa – App do Produtor** (badge Kotlin/Compose)
2. **Integrantes:** nome completo + RM de cada um *(PENDENTE: preencher)*
3. **Problema** e **público beneficiado** (Seu José; agricultores familiares)
4. **Proposta da solução** e relação com o app Flutter do morador (ecossistema)
5. **ODS 2 (meta 2.3) e ODS 12 (meta 12.3)**, com justificativa
6. **Funcionalidades** (F1 a F6)
7. **Tecnologias** (tabela da seção 3)
8. **Arquitetura** (diagrama simples: UI → ViewModel → Repository → DAO → Room)
9. **Como executar no Windows 11:** abrir no Android Studio (File > Open), aguardar o Gradle Sync, criar um emulador (Device Manager > Pixel, API 35) e clicar em Run ▶
10. **Fluxo principal** + **capturas de tela**
11. **Evidências:** criação, ação adicional e persistência após reabrir (roteiro da seção 11)
12. **Limitações (protótipo acadêmico):** dados simulados, sem integração com API, Flutter ou serviços públicos
13. **Roadmap:** integração com a API Spring do Bairro na Mesa, login de produtor, notificação de nova reserva

---

## 11. Roteiro de testes e evidências (prints para o README)

Fazer os passos no emulador, salvando cada print em `docs/prints/`:

1. `01-inicio.png`: tela inicial com os cards de impacto (do seed).
2. `02-lista.png`: lista com as 3 ofertas de exemplo.
3. `03-validacao.png`: tentar salvar com o produto vazio e kg = 0, mostrando os erros.
4. `04-cadastro.png`: formulário preenchido ("Mandioca, Legumes, 25 kg, R$ 4,00").
5. `05-criada.png`: lista com a nova oferta + Snackbar "Oferta salva".
6. `06-status.png`: Mandioca marcada como Reservado.
7. `07-separacao.png`: lista de separação mostrando a Mandioca.
8. `08-excluir.png`: diálogo de confirmação de exclusão.
9. **Persistência:** fechar o app pelo botão de multitarefa (arrastar para cima), reabrir e fazer `09-reaberto.png` mostrando a Mandioca ainda Reservada.
10. `10-vazio.png`: filtro sem resultado mostrando o estado vazio.

**Por que fechar pelo multitarefa e não só pelo "voltar":** isso encerra o processo e prova que o dado veio do Room, não da memória.

---

## 12. Entrega (Windows 11)

1. No Android Studio: **Build > Clean Project**.
2. Fechar o Android Studio.
3. No Explorador de Arquivos, apagar dentro do projeto: `.gradle\`, `.idea\`, `build\`, `app\build\` e `local.properties`.
   **Por quê:** o enunciado proíbe pastas de build, e o `local.properties` tem o caminho do SDK da sua máquina, o que quebra a importação no PC do professor.
4. Botão direito na pasta do projeto → **Compactar para arquivo ZIP**.
5. Testar: extrair o zip em outra pasta, abrir no Android Studio e rodar.
6. Enviar o zip (com o README dentro) + o vídeo pitch até **23h59**.

---

## 13. Pendências antes de desenvolver

| Pendência | Responsável | Se não houver resposta |
|---|---|---|
| Nome completo e RM de cada integrante | Equipe | README com campos `[PREENCHER]` |
| Nomes das feiras e categorias usados no app Flutter | João | Usar os da seção 4 |

---

## 14. Ordem de implementação (para o Claude ou a equipe)

1. Configurar o Gradle (version catalog, plugins Compose e KSP, dependências) e confirmar que compila.
2. `data/` completo + `BairroNaMesaApp`.
3. `OfertaViewModel` + Factory.
4. Componentes.
5. Telas, na ordem: Lista → Cadastro → Início → Separação.
6. NavHost + MainActivity.
7. Rodar o roteiro da seção 11.
8. README.

**Critério de pronto:** compila sem erros; todos os passos da seção 11 funcionam; nenhum texto do app ou do README afirma integração real com API, prefeitura, CRAS ou o app Flutter.
