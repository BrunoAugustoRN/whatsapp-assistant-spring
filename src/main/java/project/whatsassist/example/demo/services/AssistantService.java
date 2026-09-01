package project.whatsassist.example.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.whatsassist.example.demo.enuns.Status;
import project.whatsassist.example.demo.model.Idea;
import project.whatsassist.example.demo.model.Reminder;
import project.whatsassist.example.demo.model.Routine;
import project.whatsassist.example.demo.parser.CommandParser;
import project.whatsassist.example.demo.parser.InvalidCommandException;
import project.whatsassist.example.demo.parser.ParsedCommand;
import project.whatsassist.example.demo.repository.IdeaRepository;
import project.whatsassist.example.demo.repository.ReminderRepository;
import project.whatsassist.example.demo.repository.RoutineRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AssistantService {

    private final CommandParser parser;
    private final NotifierService notifier;
    private final RoutineRepository routineRepo;
    private final IdeaRepository ideaRepo;
    private final ReminderRepository reminderRepo;


    /*
    * metodo para receber from = numero whatsapp(67199999999) / body  = um id de 1 a 8 para identificacao de qual metodo deve chamar
     *ex: scheduledRoutine() opcao 1 "ID, CONTEUDO" (1, 10/04, 08:00, ESTUDAR JAVA)
    * */
    public void handleCommand(String from,String body){//Recebe numero whatsapp + id-conteudo
            try {
                ParsedCommand pdc = parser.parsedCommand(body);

                switch (pdc.option()) {//switch case com option formatado para int, assim chamado o metodo q se pede na mensagem

                    case 1 -> scheduleRoutine(from, pdc.rawArgs());//anota tarefa a rotina

                    case 2 -> saveIdea(from, pdc.rawArgs());//salvar ideia

                    case 3 -> deleteRecord(from, pdc.rawArgs());//deletar rotina ou ideia

                    case 4 -> deleteIdea(pdc.rawArgs(), from);

                    case 5 -> listActive(from);//listar rotina pendente e todas ideias

                    case 6 -> completeTask(from, pdc.rawArgs());//alterar status da tarefa

                    case 7 -> todaySummary(from, pdc.rawArgs());//listar tarefas do dia

                    case 8 -> setReminder(pdc.rawArgs(), from);//lembrete

                    case 9 -> listHistory(from);//lista todas tarefas concluidas do dia
                }
            }catch (InvalidCommandException e){
                notifier.send(from, "⚠ " + e.getMessage());

            }
    }

    public String scheduleRoutine(String from, String args){
        Routine routine = new Routine();
        String[] parts = args.split(",",3);//quebra msg em tres partes
        String dataComAno = parts[0].trim() + "/" + LocalDate.now().getYear();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");//converte data e hora para LocalDate e LocalTime
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate data = LocalDate.parse(dataComAno, formatter);
        LocalTime time = LocalTime.parse(parts[1].trim(), horaFormatter);
        LocalDateTime dataTime = data.atTime(time);//junta data e hora

        routine.setScheduledAt(dataTime);//preenche atributos
        routine.setDescription(parts[2].trim());
        routine.setStatus(Status.PENDING);

        routineRepo.save(routine);

        StringBuilder sb = new StringBuilder();

        if(!parts[2].isBlank()){
                sb.append("*Tarefa:* ").append(parts[2]).append(" ,*anotado*");
            }

        notifier.send(from, sb.toString());
        return sb.toString();
    }

    public String saveIdea(String from, String content){ //metodo salvar ideia
        Idea idea = new Idea();//objeto da classe Idea
        idea.setContent(content);//preenche com o conteudo recebido
        ideaRepo.save(idea);//salva ideia no db

        StringBuilder sb = new StringBuilder();
        if(!content.isBlank()){
                   sb.append("*Ideia anotada!*");
           }
        notifier.send(from, sb.toString());
        return sb.toString();
    }

    public String deleteRecord(String from, String args) {//metodo deletar ideia ou rotina atraves do ID
        Long id = Long.parseLong(args);//recebe id em forma de string, converte para Long
        StringBuilder sb = new StringBuilder();
        if (routineRepo.existsById(id)) {//identifica se existe alguma rotina com o ID informado, se existir deleta
            routineRepo.deleteById(id);//chama repositorio com metodo deleteById
            sb.append("*Rotina* ").append(id).append(" ,*removida!*");
        } else {
            sb.append("*Rotina* ").append(id).append(" *não encontrada!*");
        }
        notifier.send(from, sb.toString());
        return sb.toString();
    }

    public String deleteIdea(String args, String from) {
        Long id = Long.parseLong(args);
        StringBuilder sb = new StringBuilder();
        if (ideaRepo.existsById(id)) {//mesma coisa para ideias
            ideaRepo.deleteById(id);
            sb.append("*Ideia* ").append(id).append(" ,*removida!*");
        } else {
            sb.append("*Ideia* ").append(id).append(" *não encontrada!*");
        }
        notifier.send(from, sb.toString());
        return sb.toString();
    }

    public String listActive(String from){//metodo listar rotinas pendenter e todas ideias
            List<Routine> routineList = routineRepo.findByStatus(Status.PENDING);//lista de rotinas pendentes
            List<Idea> ideaList = ideaRepo.findAll();//lista de ideias
            StringBuilder sb = new StringBuilder("*Ativos/Pendentes*\n");//stringbuilder para montar a mensagem com a lista de rotinas e ideias

            if(routineList.isEmpty() && ideaList.isEmpty()){//ve se existe algo em rotinas ou ideias primeiramente
                sb.append("*Nada por aqui!*");
            }


            if(!routineList.isEmpty()){//entra aqui apenas se tiver algo
                sb.append("*Rotinas:*\n");//formatacao de mensagem -ID-DESCRICAO-DATA AGENDADA
                for(Routine r : routineList) {
                    sb.append("- ").append(r.getId());
                    sb.append(" - ").append(r.getDescription());
                    sb.append("(").append(r.getScheduledAt()).append(")\n");
                }
                }
                if(!ideaList.isEmpty()){
                sb.append("\n*Ideias:*\n");//ID-DESCRICAO-DATA CRIADA
                for(Idea i : ideaList){
                    sb.append("- ").append(i.getId());
                    sb.append(" - ").append(i.getContent());
                    sb.append("- (").append(i.getCreatedAt()).append(")\n");
                }
            }
            notifier.send(from,sb.toString());
            return sb.toString();
    }

    public String completeTask(String from, String args){//Recebe o id em formato de texto
        Long id = Long.parseLong(args);//converte o id para Long

        String msg = routineRepo.findById(id).map(routine ->{//busca em rotina a tarefa com o id q o usuario informou. .map para verificar se realmete existe a tarefa
                routine.setStatus(Status.DONE);//se existir altera status para DONE
                routine.setCompletedAt(LocalDateTime.now());//e atualiza a data e hora para o momento atual
                routineRepo.save(routine);//salva no db
                return "*Tarefa* " + routine.getDescription() + " *concluída!*";//retorna mensagem de tarefa concluida, se condicao .map for true
                }).orElse("*Tarefa* " + id + " *não encontrada*");//se nao retona mensagem de tarefa nao concluida .orELse

        notifier.send(from, msg.toString());
        return msg;
    }

    public String todaySummary(String from, String args){
        LocalDate today = LocalDate.now();//Dia atual
        LocalDateTime start = today.atStartOfDay();//Data do dia atual + hora do inicio do dia 00:00:00
        LocalDateTime end = today.atTime(23, 59, 59);//Hora final do dia 23:59:59

        List<Routine> routine = routineRepo.findByScheduledAtBetweenAndStatus(start, end, Status.PENDING);//lista de tarefas pendentes do dia, intervalo das 00:00 as 23:59

        StringBuilder sb = new StringBuilder(" *Hoje:* \n");//formatacao da mensagem com as tarefas

        if(routine.isEmpty()){//entra aqui se estiver vazio
            sb.append("*Nada por aqui hoje*");
        }

        for(Routine r : routine){
            sb.append(" ")
                    .append(r.getScheduledAt().toLocalTime())
                    .append(" - ")
                    .append(r.getDescription())
                    .append("\n");
        }
        notifier.send(from, sb.toString());
        return sb.toString();//retorna em texto/string
    }

    public String setReminder(String args, String from){//recebe minutos e descricao, from numero do whatsapp
        String[] parts = args.split(",",2   );//separa a msg por virgula em dois pedacos
        int minutes = Integer.parseInt(parts[0].trim());//converte o minuto enviado em inteiro e tira espacos cm trim
        String description = parts[1].trim();//tirar espacos da descricao

        StringBuilder sb = new StringBuilder();
        LocalDateTime triggerAt = LocalDateTime.now().plusMinutes(minutes);//soma minutos recebido + hora atual

        Reminder reminder = new Reminder();//criando e salvando reminder
        reminder.setDescription(description);
        reminder.setTriggerAt(triggerAt);
        reminder.setPhoneNumber(from);
        reminder.setFired(false);//fired = false sinalizando q lembrete ainda nao foi disparado
        reminderRepo.save(reminder);

        sb.append(" *Te aviso em* ").append(minutes).append(" *min:*").append(description);

        notifier.send(from, sb.toString());
        return sb.toString();

    }

    public String listHistory(String from){
        LocalDate today = LocalDate.now();//dia atual
        LocalDateTime start = today.atStartOfDay();//inicio do dia 00:00
        List<Routine> routineList = routineRepo.findByStatusAndCompletedAtAfterOrderByCompletedAtDesc(Status.DONE, start);//busca no bd status DONE apos 00:00
        StringBuilder sb = new StringBuilder("*Tarefas concluidas:* \n");//formatacao da msg

        if(routineList.isEmpty()){//verifica se a alguma tarefa concluida
            sb.append("*Nenhuma tarefa concluida* ").append(today);
        }

        for(Routine r : routineList){
            sb.append('-')
                    .append(r.getDescription())
                    .append("-").append(r.getCompletedAt().toLocalTime())
                    .append("\n");
        }
        notifier.send(from, sb.toString());
        return sb.toString();
    }

}
