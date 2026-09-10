# WhatsApp Assistant

Assistente pessoal via WhatsApp que interpreta mensagens em linguagem natural usando function calling do Gemini, construído com Spring Boot. Em vez de comandos fixos, o usuário escreve o que precisa e a IA decide sozinha qual ação executar.

## Como funciona

O Twilio recebe as mensagens do WhatsApp e as encaminha via webhook (`POST /webhook`) para a aplicação. A mensagem, junto com o histórico recente da conversa, é enviada ao Gemini, que decide qual função chamar (agendar uma rotina, anotar uma ideia, criar um lembrete, listar pendências, entre outras) e com quais parâmetros. A aplicação executa a ação, salva no banco e responde de volta pelo próprio WhatsApp.

Cada usuário só enxerga e mexe nas próprias rotinas e ideias — tudo isolado por número de telefone.

Um scheduler roda em segundo plano a cada minuto verificando lembretes pendentes e disparando o aviso no horário certo.

### O que dá pra pedir

Não existe mais um menu fixo — a interpretação é livre. Alguns exemplos do que a IA entende:

```
"agenda uma reunião amanhã às 14h pra estudar java"
"quero anotar uma ideia: app pra dividir contas"
"remove a tarefa de estudar java"
"liste minhas tarefas ativas"
"o que eu tenho pra hoje?"
"me lembra em 20 minutos de beber água"
"concluí a tarefa 3"
"qual meu histórico de hoje?"
```

A IA resolve o item certo mesmo quando você se refere a ele pelo nome, sem precisar informar o ID.

## Stack

- Java 17 + Spring Boot
- Spring Data JPA + PostgreSQL, schema versionado com Flyway
- Google Gemini (function calling) para interpretação de linguagem natural
- Twilio API (integração com WhatsApp)
- Spring Scheduling (`@Scheduled`) para disparo de lembretes
- Lombok

## Rodando localmente

1. Clone o repositório.
2. Suba um Postgres local e crie um banco e um usuário pro projeto.
3. Configure as variáveis de ambiente:
   ```
   TWILIO_ACCOUNT_SID=seu_account_sid
   TWILIO_AUTH_TOKEN=seu_auth_token
   GOOGLE_API_KEY=sua_chave_do_gemini
   DB_PASSWORD=senha_do_seu_postgres
   ```
4. Ajuste `spring.datasource.url`/`username` em `application.properties` caso o nome do banco ou do usuário seja diferente do padrão.
5. Suba a aplicação:
   ```
   ./mvnw spring-boot:run
   ```
   O Flyway roda as migrations automaticamente na inicialização, criando as tabelas.
6. Exponha a porta local (por exemplo, via ngrok) e configure a URL pública + `/webhook` no [Twilio Sandbox for WhatsApp](https://www.twilio.com/docs/whatsapp/sandbox).
7. Envie uma mensagem em linguagem natural para o número do sandbox.

## Roadmap

- [ ] Migrar o transporte de mensagens de WhatsApp (Twilio) para Telegram
- [ ] Integração com Google Calendar para sincronizar rotinas agendadas
- [ ] Reduzir o histórico de conversa enviado por chamada, para melhorar o tempo de resposta
