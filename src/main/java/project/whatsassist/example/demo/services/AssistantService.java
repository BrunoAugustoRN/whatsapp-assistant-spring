package project.whatsassist.example.demo.services;

import project.whatsassist.example.demo.enuns.Status;
import project.whatsassist.example.demo.model.Idea;
import project.whatsassist.example.demo.model.Routine;
import project.whatsassist.example.demo.parser.CommandParser;
import project.whatsassist.example.demo.parser.ParsedCommand;
import project.whatsassist.example.demo.repository.IdeaRepository;
import project.whatsassist.example.demo.repository.ReminderRepository;
import project.whatsassist.example.demo.repository.RoutineRepository;

import java.util.List;

public class AssistantService {

    private CommandParser parser;
    private NotifierService notifier;
    private RoutineRepository routineRepo;
    private IdeaRepository ideaRepo;
    private ReminderRepository reminderRepo;

    public void handleCommand(String from,String body){

    }

    public String scheduleRoutine(args){

    }

    public String saveIdea(String content){ //metodo salvar ideia
           Idea idea = new Idea();
           idea.setContent(content);
           ideaRepo.save(idea);
           return "Ideia anotada"+ content;
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
            return sb.toString();//formatar o objeto para texto e retornar
    }



}
