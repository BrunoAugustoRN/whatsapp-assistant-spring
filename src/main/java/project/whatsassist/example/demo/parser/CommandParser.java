package project.whatsassist.example.demo.parser;

import org.springframework.stereotype.Component;

@Component
public class CommandParser {

    //verifica se a mensagem do usuario esta vazia
    public ParsedCommand parsedCommand(String message){
        if(message == null || message.isBlank()){
            throw new InvalidCommandException(
                    "Mensagem vazia. " + menuText()
            );
        }
        //divide a mensagem em duas partes
        String[] parts = message.trim().split(",",2);

        //verifica se a mnesagem comeca com numero
        int option;
        try {
            option = Integer.parseInt(parts[0].trim());
        } catch (NumberFormatException e) {
            throw new InvalidCommandException(
                    "Comece com o número da opção.\n" + menuText()
            );
        }
        //verfica se a opcao existe
        if(option < 1 || option > 8){
            throw new InvalidCommandException(
                    "Opção inválida " + option + "\n" + menuText()
            );
        }
        //verfica se a uma segunda parte apos a virgula
        String args = parts.length > 1 ? parts[1].trim() : "";

        validateArgs(option, args);

        return new ParsedCommand(option, args);
    }

    private void validateArgs(int option, String args){
        switch (option){
            //divide a msg em 3 partes, verifica se a pelo menos 3 partes preenchida para anotar rotina
            case 1 -> {
                String[] p = args.split(",",3);
                if(p.length < 3 || p[2].isBlank()){
                    throw new InvalidCommandException(
                            "Formato: 1, DD/MM, HH:mm, Descrição\n" +
                            "Ex: 1, 10/04, 08:00, Estudar java"
                    );
                }
            }
            case 2 -> {
                //verifica se a algo em args para anotar ideia, precisa ter algo apos a virgula
                if(args.isBlank()){
                    throw new InvalidCommandException(
                            "Formato: 2, sua ideia\n" +
                            "Ex: 2, Ideia de app para finanças"
                    );
                }
            }
            case 3, 5 ->{
                //remover ou concluir
                if(args.isBlank() || !args.matches("\\d+")){
                    throw new InvalidCommandException(
                            "Formato: " + option + ", ID\n" +
                            "Ex: " + option + ", 3"
                    );
                }
            }
            case 4, 6, 8 -> {

            }
            case 7 ->{
                String[] p = args.split(",",2);
                if(p.length < 2 || p[1].isBlank() || !p[0].trim().matches("\\d+")){
                    throw new InvalidCommandException(
                            "Formato: 7, minutos, descrição\n" +
                            "Ex: 7, 15, Tirar café"
                    );
                }
            }
        }
    }
    public String menuText(){
        return """
                Menu do assistente: 
                1 - Agendar Rotina  (1, DD/MM, HH:mm, Desc)
                2 - Anotar Ideia    (2, Sua ideia)
                3 - Remover         (3, ID)
                4 - Listar Ativas
                5 - Concluir        (5, ID)
                6 - Resumo de Hoje
                7 - Lembrete        (7, min, Desc)
                8 - Histórico
                """;
    }

}
