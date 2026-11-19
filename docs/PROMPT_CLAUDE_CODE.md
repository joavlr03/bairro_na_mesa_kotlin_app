Desenvolva o app Android seguindo `docs/PLANO.md` (arquitetura, dados, rotas, validações e ordem de implementação).

A aparência deve ser igual ao mockup aprovado:
- `docs/design/TELAS.md` → especificação de tema, componentes e telas (siga à risca: cores, medidas, textos).
- `docs/design/prints/*.png` → imagens de cada tela. Abra e compare antes de criar cada tela.
- `docs/design/telas/*.dc.html` → HTML do mockup, só para conferir valores exatos. Não use WebView: tudo em Jetpack Compose nativo.

Regras:
1. Siga a ordem da seção 14 do plano. Depois de cada etapa, rode `gradlew.bat assembleDebug` e só avance se compilar.
2. Crie primeiro `ui/theme/` (Color.kt, Type.kt, Theme.kt) conforme a seção 1 do TELAS.md.
3. Não aceite atualizações de versão: use exatamente as versões da seção 3 do plano.
4. Ao terminar cada tela, liste o que ficou diferente do print e por quê.
5. Ambiente: Windows 11 (use `gradlew.bat` e caminhos com `\` em instruções para a equipe).
