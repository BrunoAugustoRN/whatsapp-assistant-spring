# WhatsApp Assistant

Assistente pessoal via WhatsApp para anotar rotinas, ideias e lembretes, construído com Spring Boot. O usuário manda mensagens no WhatsApp e a aplicação salva, lista, atualiza e dispara lembretes automaticamente.

## Como funciona

O Twilio recebe as mensagens do WhatsApp e as encaminha via webhook (`POST /webhook`) para a aplicação, que interpreta o comando, executa a ação (salvar, remover, listar, concluir) e responde de volta pelo próprio WhatsApp.

Um scheduler roda em segundo plano a cada minuto verificando lembretes pendentes e disparando o aviso no horário certo.

### Comandos disponíveis hoje

```
1 - Agendar Rotina  (1, DD/MM, HH:mm, Descrição)
2 - Anotar Ideia    (2, Sua ideia)
3 - Remover Rotina  (3, ID)
4 - Remover Ideia   (4, ID)
5 - Listar Ativas
6 - Concluir Tarefa (6, ID)
7 - Resumo de Hoje
8 - Lembrete        (8, minutos, Descrição)
9 - Histórico
```

## Stack

- Java 17 + Spring Boot
- Spring Data JPA + H2 (banco em memória)
- Twilio API (integração com WhatsApp)
- Spring Scheduling (`@Scheduled`) para disparo de lembretes
- Lombok

## Rodando localmente

1. Clone o repositório e configure as variáveis de ambiente da Twilio:
   ```
   TWILIO_ACCOUNT_SID=seu_account_sid
   TWILIO_AUTH_TOKEN=seu_auth_token
   ```
2. Suba a aplicação:
   ```
   ./mvnw spring-boot:run
   ```
3. Exponha a porta local (ex: via ngrok) e configure a URL pública + `/webhook` no [Twilio Sandbox for WhatsApp](https://www.twilio.com/docs/whatsapp/sandbox).
4. Envie uma mensagem para o número do sandbox seguindo o formato dos comandos acima.
5. O console do banco H2 fica disponível em `localhost:8080/h2-console`.

## Roadmap

- [ ] Interpretação de linguagem natural com IA (Gemini), substituindo o menu numérico por comandos em texto livre
- [ ] Integração com Google Calendar para sincronizar rotinas agendadas
- [ ] Migração do transporte de mensagens de WhatsApp (Twilio) para Telegram
