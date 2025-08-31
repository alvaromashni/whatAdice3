# whatAdice — Desafio técnico (Android/Kotlin)

Desafio técnico com **Splash**, **Login**, **Cadastro** e **Home**, seguindo o Material design 3 com modificações próprias, validações básicas e **persistência local** simples.

---

##  Stack utilizada

| Camada | Tecnologia | Por quê |
|---|---|---|
| Linguagem | **Kotlin** | Gostei muito da simplicidade da sintaxe do Kotlin. |
| UI | **Material 3**, **ConstraintLayout**, **MaterialButton/TextInputLayout** | Visual consistente (outlined), alinhado ao protótipo no Figma e responsividade simples sem Navigation. |
| Ciclo de Vida | **Lifecycle KTX (`lifecycle-runtime-ktx`)** | `lifecycleScope.launch {}` para delays seguros na Splash. |
| Concorrência | **Kotlin coroutines (`kotlinx-coroutines-android`)** | `delay(1500)` sem bloquear a UI. |
| Persistência | **SharedPreferences** (+ `org.json` para serializar usuários) | Simples, depois penso em mudar para DataStore. |

**SDK:** minSdk 23+, target/compile SDK 34.  
**Gradle (módulo app):**
```gradle
dependencies {
    implementation "androidx.appcompat:appcompat:1.7.0"
    implementation "com.google.android.material:material:1.12.0"
    implementation "androidx.constraintlayout:constraintlayout:2.1.4"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.9.2"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2"
}
```
Tema base:
```xml
<style name="Theme.whatAdice" parent="Theme.Material3.Light.NoActionBar"/>
```

---

## Decisões e motivos

1. **Activities simples (Splash, Login, Register, Home)**  
   - Clareza de leitura.  
   - Navegação direta via `startActivity()` + `finish()`; sem depender de Navigation/Fragments.

2. **Persistência com SharedPreferences + JSON**  
   - Atende ao requisito de *persistência local* sem sobrecarga de Room/DataStore.  
   - Fácil de revisar: JSON legível e poucas linhas de código.

3. **Normalização de e-mail em um único lugar**  
   - Evita bugs (maiúsculas, espaços invisíveis/NBSP).  
   - Implementado em `util/EmailUtils.kt` e usado tanto no **Register** quanto no **Login** e no **UserStorage** para a chave de índice.

4. **Material 3 + estilos reutilizáveis**  
   - Inputs outlined e botões “pílula” com **radius** e **cores** centralizados em `styles.xml`.  
   - Facilita manter o design consistente entre telas.

5. **Sem ViewModel/Repository (por enquanto)**  
   - Mantém o projeto enxuto e direto ao ponto.  
   - Fácil evoluir depois (trocar SharedPreferences por DataStore/Room sem reescrever UI).

---

## Desafios que surgiram no caminho (e como resolvi)

- **Inputs sem borda (ficando sublinhados)**  
  Causa: tema Material incorreto.  
  Solução: `Theme.Material3.*` + `TextInputLayout` com `OutlinedBox`.

- **Splash não reconhecia `onCreate`/`lifecycleScope`**  
  Causa: herança sem parênteses (`: AppCompatActivity`), falta de deps KTX.  
  Solução: `: AppCompatActivity()` + `lifecycle-runtime-ktx` e `kotlinx-coroutines-android`.

- **Crashes ao tocar em Cadastrar (volta sozinho pro Login)**  
  Causa: `lateinit` de views não inicializadas / cast incorreto (`MaterialButton` vs `<Button>`).  
  Solução: obter views após `setContentView`.

- **Credenciais inválidas após cadastrar**  
  Causa: e-mail indexado de forma diferente entre cadastro e login; espaços/maiúsculas.  
  Solução: `normalizeEmail()` + `cleanPassword()` + `UserStorage.key()`; usei o método `debugDump()` para inspecionar no Logcat.

- **Abre e fecha após Splash**  
  Causa: `setContentView` apontando para layout inexistente / IDs divergentes / duas Activities com `LAUNCHER`.  
  Solução: Manifest com **apenas a Splash** como `LAUNCHER`, checagem de IDs e layout corretos, exclui o MainActivity.

---

## Requisitos do projeto

- **Splash:** exibe logo e decide rota (logado → Home; não logado → Login).  
- **Login:** campos de e-mail e senha; validações; botões **Entrar** e **Cadastre-se**.  
- **Cadastro:** nome, e-mail, senha; validações (e-mail válido, senha ≥ 6, usuário não duplicado).  
- **Home:** exibe **nome** e **e-mail** do usuário logado, com botão **Sair**.  
- **Persistência & sessão:** usuários e sessão salvos em `SharedPreferences`; ao reabrir o app, mantém login até **Logout**.

---

## Sobre a ideia **whatAdice**

**whatAdice** é o começo de um app voltado a rolagem de dados para jogadores de RPG que jogam de forma remota - esse fluxo de login é importante para fazer com que o app funcione de forma remota, não apenas local, como é por enquanto. A funcionalidade principal seria a possibilidade dos jogadores rolarem o dado e compartilharem o resultado mesmo online.
Neste MVP, o foco está somente no **fluxo de autenticação e base de telas** (Splash → Login/Register → Home). Os próximos passos planejados:

- Perfil e party com outros usuários.  
- Sessão online com rolagem de dados de todos os tipos e memória.  
- Sincronização remota (futuro backend).

> O nome “whatAdice” é simples, existe a possibilidade de pensar em outro nome quando se tornar um produto de verdade.

---

## Licença

Projeto voltado exclusivamente a avaliação técnica para processo de estágio.
