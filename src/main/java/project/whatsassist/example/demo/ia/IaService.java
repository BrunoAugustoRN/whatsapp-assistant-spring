package project.whatsassist.example.demo.ia;

import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Part;
import org.springframework.stereotype.Service;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class IaService {

    Client client = new Client();//conexao com gemini assim que a classe e istanciada
    AssistantFunctions functions = new AssistantFunctions();//instancia AssistantFunctions para acesso ao Tool

    public GenerateContentResponse interpretar(String mensagemUsuario, List<Content> historico, String contextoItens){//receber mensagem crua que o usuario enviou do whatsapp/telegram, devolve o objeto de resposta inteiro da API para examinar no .functionsCalls() quanto .text

        GenerateContentConfig config = GenerateContentConfig.builder()//Construir a configuracao dessa chama especifica
                .tools(functions.tool)//diz ao gemini "essas sao as acoes que voce pode escolher chamar" Tool cm 9 funcoes
                .systemInstruction(
                        Content.fromParts(//agrupa 1 ou mais Part em um Content
                                Part.fromText(
                                        "Hoje é " + LocalDate.now() + ". Você é um assistente pessoal que interpreta mensagens do usuário e decide qual função chamar.\n\n"
                                                + "Itens ativos do usuário agora (use o ID certo quando ele se referir a um pelo nome):\n"
                                                + contextoItens
                                )
                        )
                )
                .build();

        List<Content> contents = new ArrayList<>(historico);//copia o historico da conversa
        contents.add(Content.builder()//adiciona a mensagem atual do usuario no final
                .role("user")
                .parts(List.of(Part.fromText(mensagemUsuario)))
                .build());

        return client.models.generateContent(//chama da API do Gemini
                "gemini-3.6-flash",
                contents,
                config
        );

    }

}
