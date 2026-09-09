package project.whatsassist.example.demo.ia;

import com.google.genai.types.Content;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConversationMemory {

    private static final int MAX_TURNOS = 20;
    private final Map<String, List<Content>> historico = new ConcurrentHashMap<>();//contato -> lista de mensagens

    //retorna o historico do contato from ou lista vazia
    public List<Content> get(String from){
        return historico.getOrDefault(from,List.of());
    }

    //registrar nova mensagem(turno)
    public void adicionar(String from, Content turno){
        List<Content> lista = historico.computeIfAbsent(from, k -> new ArrayList<>());//busca lista de mensagens do contato from
        lista.add(turno);
        while (lista.size() > MAX_TURNOS){//remove a mensagem mais antiga
            lista.remove(0);
        }
    }






}
