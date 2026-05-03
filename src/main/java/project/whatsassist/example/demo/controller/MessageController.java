package project.whatsassist.example.demo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.whatsassist.example.demo.services.AssistantService;

@RestController
@RequiredArgsConstructor
public class MessageController {

    @Autowired
    private AssistantService assistantService;

    /* Metodo q o Twillo chama toda vez ao mandar mensagem
    * no whatsapp */
    @PostMapping(value = "/webhook")//requisição post com endereço webhook
    public ResponseEntity<Void> receive ( //RespondeEntity para retorno
            @RequestParam("From") String from, //parametros filtrados que devem virao na url, assim podendo pegar a mensagem FROM: numero do whatsapp
            @RequestParam("Body") String body // BODY: conteudo que usuario digitou
    ){
            assistantService.handleCommand(from, body) //chamar metodo passando From e Body
            return ResponseEntity.ok().build(); //retorna 200ok para o twillo
    }

}
