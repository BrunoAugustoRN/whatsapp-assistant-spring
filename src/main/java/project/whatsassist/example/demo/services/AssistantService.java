package project.whatsassist.example.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.whatsassist.example.demo.enuns.Status;
import project.whatsassist.example.demo.model.Idea;
import project.whatsassist.example.demo.model.Reminder;
import project.whatsassist.example.demo.model.Routine;
import project.whatsassist.example.demo.parser.CommandParser;
import project.whatsassist.example.demo.parser.ParsedCommand;
import project.whatsassist.example.demo.repository.IdeaRepository;
import project.whatsassist.example.demo.repository.ReminderRepository;
import project.whatsassist.example.demo.repository.RoutineRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            String[] parts = body.split(",",2);//separa a mensagem em no maximo dois pedacos, utilizando o split
            int option = Integer.parseInt(parts[0].trim());//option recebe um id de 1 a 8, parseInt para converter de string para int, na posicao 0, onde esta o id, trim para tirar espacos
            String args = parts.length > 1 ? parts[1].trim() : "";//args recebe conteudo se mensagem for maior q 1, se nao fica vazio

            switch (option){//switch case com option formatado para int, assim chamado o metodo q se pede na mensagem
                case 2 -> saveIdea(args);//salvar ideia
                case 3 -> deleteRecord(args);//deletar rotina ou ideia
                case 4 -> listActive();//listar rotina pendente e todas ideias
                case 5 -> completeTask(args);//alterar status da tarefa
                case 6 -> todaySummary();//listar tarefas do dia
                case 7 -> setReminder(args, from);//lembrete
            }
    }
/*
    public String scheduleRoutine(args){

    }
*/
    public String saveIdea(String content){ //metodo salvar ideia
           Idea idea = new Idea();//objeto da classe Idea
           idea.setContent(content);//preenche com o conteudo recebido por parametro
           ideaRepo.save(idea);//salva ideia no db
           return "Ideia anotada"+ content;//retorna mensagem q foi salvo
    }

    public String deleteRecord(String args){//metodo deletar ideia ou rotina atraves do ID
            Long id = Long.parseLong(args);//recebe id em forma de string, converte para Long
            if(routineRepo.existsById(id)){//identifica se existe alguma rotina com o ID informado, se existir deleta
                routineRepo.deleteById(id);//chama repositorio com metodo deleteById
                return "Rotina " + id + " removida";//retorna msg q rotina foi removida
            }

            if(ideaRepo.existsById(id)){//mesma coisa para ideias
                ideaRepo.deleteById(id);
                return  "Ideia " + id + " removida";
            }

            return "Nenhuma ideia ou rotina com id: " + id + " encontrado";//so retorna isso caso nao encontrar ID informado

    }

    public String listActive(){//metodo listar rotinas pendenter e todas ideias
            List<Routine> routineList = routineRepo.findByStatus(Status.PENDING);//lista de rotinas pendentes
            List<Idea> ideaList = ideaRepo.findAll();//lista de ideias

            if(routineList.isEmpty() && ideaList.isEmpty()){//ve se existe algo em rotinas ou ideias primeiramente
                return "Nada por aqui";
            }

            StringBuilder sb = new StringBuilder("*Ativos/Pendentes*\n");//stringbuilder para montar a mensagem com a lista de rotinas e ideias

            if(!routineList.isEmpty()){//entra aqui apenas se tiver algo
                sb.append("*Rotinas*\n");//formatacao de mensagem -ID-DESCRICAO-DATA AGENDADA
                for(Routine r : routineList){
                    sb.append("-").append(r.getId());
                    sb.append("-").append(r.getDescription());
                    sb.append("(").append(r.getScheduledAt()).append(")");

                }
                for(Idea i : ideaList){
                    sb.append("*Ideias:*\n");//ID-DESCRICAO-DATA CRIADA
                    sb.append("-").append(i.getId());
                    sb.append("-").append(i.getContent());
                    sb.append("-").append(i.getCreatedAt());
                }
            }
            return sb.toString();//formatar o objeto para texto/string e retornar
    }

    public String completeTask(String args){//Recebe o id em formato de texto
        Long id = Long.parseLong(args);//converte o id para Long

        return routineRepo.findById(id).map(routine ->{//busca em rotina a tarefa com o id q o usuario informou. .map para verificar se realmete existe a tarefa
                routine.setStatus(Status.DONE);//se existir altera status para DONE
                routine.setCompletedAt(LocalDateTime.now());//e atualiza a data e hora para o momento atual
                routineRepo.save(routine);//salva no db
                return "Tarefa* " + routine.getDescription() + " *concluida";//retorna mensagem de tarefa concluida, se condicao .map for true
                }).orElse("Tarefa* " + id + " *não encontrada");//se nao retona mensagem de tarefa nao concluida .orELse
    }

    public String todaySummary(){
        LocalDate today = LocalDate.now();//Dia atual
        LocalDateTime start = today.atStartOfDay();//Data do dia atual + hora do inicio do dia 00:00:00
        LocalDateTime end = today.atTime(23, 59, 59);//Hora final do dia 23:59:59

        List<Routine> routine = routineRepo.findByScheduledAtBetweenAndStatus(start, end, Status.PENDING);//lista de tarefas pendentes do dia, intervalo das 00:00 as 23:59

        if(routine.isEmpty()){//entra aqui se estiver vazio
            return "Nada por aqui hoje";
        }

        StringBuilder sb = new StringBuilder(" *Hoje: *\n");//formatacao da mensagem com as tarefas
        for(Routine r : routine){
            sb.append(" ")
                    .append(r.getScheduledAt().toLocalTime())
                    .append(" - ")
                    .append(r.getDescription())
                    .append("\n");
        }
        return sb.toString();//retorna em texto/string
    }

    public String setReminder(String args, String from){//recebe minutos e descricao, from numero do whatsapp
        String[] parts = args.split(",",2   );//separa a msg por virgula em dois pedacos
        int minutes = Integer.parseInt(parts[0].trim());//converte o minuto enviado em inteiro e tira espacos cm trim
        String description = parts[1].trim();//tirar espacos da descricao

        LocalDateTime triggerAt = LocalDateTime.now().plusMinutes(minutes);//soma minutos recebido + hora atual

        Reminder reminder = new Reminder();//criando e salvando reminder
        reminder.setDescription(description);
        reminder.setTriggerAt(triggerAt);
        reminder.setPhoneNumber(from);
        reminder.setFired(false);//fired = false sinalizando q lembrete ainda nao foi disparado
        reminderRepo.save(reminder);

        return " Te aviso em " + minutes + " min: " + description;

    }

}
