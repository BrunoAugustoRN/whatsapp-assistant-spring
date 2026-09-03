package project.whatsassist.example.demo.ia;

import com.google.genai.gaos.models.interactions.Function;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Schema;
import com.google.genai.types.Tool;
import com.google.genai.types.Type;
import project.whatsassist.example.demo.enuns.Status;
import project.whatsassist.example.demo.model.Routine;
import project.whatsassist.example.demo.repository.RoutineRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class AssistantFunctions {

    //FunctionDeclaration = classe SDK Gemini
    FunctionDeclaration removerRotina = FunctionDeclaration.builder()//builder gerar objetos por parte

            .name("remover_rotina")//Identificador para a IA saber quando se referir a essa funcao

            .description("Remove uma rotina/tarefa agendada pelo ID. Use quando o usuário pedir para apagar, cancelar ou remover uma rotina específica.")//Descricao para IA decidir se essa e a funcao certa para o momento

            .parameters(//dados q a funcao precisa receber para funcionar

                    Schema.builder()//Schema formato para descrever a estrutura de dados dos parametros

                            .type(Type.Known.OBJECT)//"Type.Known.OBJECT" diz Os parametros ao todos formam um objeto

                            .properties(Map.of(//"Properties" lista quais campos esse objeto de parametros tem. "Map.of" cria um mapa (chave - valor) onde a chave e o nome do parametro "id", valor outro Schema descrevedno esse parametro especificamente

                                    "id", Schema.builder()
                                                    .type(Type.Known.INTEGER)//"Type.Known.INTEGER" diz q o campo "id" precisa ser um inteiro

                                                    .description("O ID numérico da rotina a ser removia.")//Descricao para IA entender o q esse campo significa

                                                    .build()//fecha e constroi o o objeto schema dos parametros

                            ))
                            .required("id")//campo "id" e obrigatorio

                            .build()//fecha os parameters
            )
            .build();//constroi o objeto
    /*
    * O bloco inteiro nao executa nenhuma acao, monta um formato estruturado, um JSON
    */

    FunctionDeclaration agendarRotina = FunctionDeclaration.builder()
            .name("agendar_rotina")
            .description("Agenda uma rotina/tarefa/compromisso para uma data e hora específicas. Use quando o usuário quiser marcar, agendar ou lembrar de algo em um momento futuro")
            .parameters(
                    Schema.builder()
                            .type(Type.Known.OBJECT)
                            .properties(Map.of(
                                    "data", Schema.builder()
                                                    .type(Type.Known.STRING)
                                                    .description("Data no formato ISO AAAA-MM-DD. Resolva expressões relativas como 'amanhã' ou 'sexta que vem' usando a data atual informada no contexto")
                                            .build(),
                                    "hora", Schema.builder()
                                                    .type(Type.Known.STRING)
                                                    .description("Horario em formato HH:mm (24 horas).")
                                            .build(),
                                    "descricao", Schema.builder()
                                                    .type(Type.Known.STRING)
                                                    .description("Descricao curta da tarefa, ex: 'Estudar java'.")
                                            .build()
                            ))
                            .required("data", "hora", "descricao")
                            .build()
            )
            .build();

    FunctionDeclaration criarLembrete = FunctionDeclaration.builder()
            .name("criar_lembrete")
            .description("Cria um lembrete avulso que dispara depois de um número de minutos. Use quando o usuário pedir para ser avisado/lembrado de algo em X minutos.")
            .parameters(
                    Schema.builder()
                            .type(Type.Known.OBJECT)
                            .properties(Map.of(
                                    "minutos", Schema.builder()
                                            .type(Type.Known.INTEGER)
                                            .description("Quantidade de minutos a partir de agora até o lembrete disparar.")
                                            .build(),
                                    "descricao", Schema.builder()
                                            .type(Type.Known.STRING)
                                            .description("Texto curto do que o lembrete deve avisar, ex: 'tirar café'.")
                                            .build()
                            ))
                            .required("minutos", "descricao")
                            .build()
            )
            .build();

    FunctionDeclaration anotarIdeia = FunctionDeclaration.builder()
            .name("anotar_ideia")
            .description("Salva uma ideia solta do usuário, sem data ou hora associada. Use quando o usuário quiser guardar uma ideia, insight ou algo pra lembrar depois, sem compromisso de horário.")
            .parameters(
                    Schema.builder()
                            .type(Type.Known.OBJECT)
                            .properties(Map.of(
                                    "conteudo", Schema.builder()
                                            .type(Type.Known.STRING)
                                            .description("O texto da ideia a ser salva.")
                                            .build()
                            ))
                            .required("conteudo")
                            .build()
            )
            .build();

    FunctionDeclaration removerIdeia = FunctionDeclaration.builder()
            .name("remover_ideia")
            .description("Remove uma ideia salva pelo ID. Use quando o usuário pedir para apagar ou remover uma ideia específica.")
            .parameters(
                    Schema.builder()
                            .type(Type.Known.OBJECT)
                            .properties(Map.of(
                                    "id", Schema.builder()
                                                    .type(Type.Known.INTEGER)
                                                    .description("O ID numérico da ideia a ser removida.")
                                                    .build()
                            ))
                            .required("id")
                            .build()
            )
            .build();

    FunctionDeclaration concluirTarefa = FunctionDeclaration.builder()
            .name("concluir_tarefa")
            .description("Marca uma rotina/tarefa como concluída pelo ID. Use quando o usuário disser que terminou, concluiu ou fez uma tarefa.")
            .parameters(
                    Schema.builder()
                            .type(Type.Known.OBJECT)
                            .properties(Map.of(
                                    "id", Schema.builder()
                                                    .type(Type.Known.INTEGER)
                                                    .description("O ID numérico da tarefa concluída.")
                                                    .build()
                            ))
                            .required("id")
                            .build()
            )
            .build();

    FunctionDeclaration listarAtivos = FunctionDeclaration.builder()
            .name("listar_ativos")
            .description("Lista todas rotinas pendentes e todas ideias salvas. Use para quando o usuario pedir para ver o que esta pendente, ativo ou anotado no momento")
            .build();

    FunctionDeclaration resumoHoje = FunctionDeclaration.builder()
            .name("resumo_hoje")
            .description("Lista as rotinas/tarefas pendentes agendadas para o dia de hoje. Use quando o usuário perguntar o que tem pra hoje ou pedir um resumo do dia.")
            .build();

    FunctionDeclaration historico = FunctionDeclaration.builder()
            .name("historico")
            .description("Lista as tarefas já concluídas no dia de hoje. Use quando o usuário pedir o histórico ou o que já foi feito hoje.")
            .build();


    Tool tool = Tool.builder()
            .functionDeclarations(removerRotina, removerIdeia, resumoHoje, agendarRotina, anotarIdeia, criarLembrete, concluirTarefa, historico, listarAtivos)
            .build();

}
