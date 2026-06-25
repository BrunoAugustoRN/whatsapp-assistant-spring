package project.whatsassist.example.demo.parser;

public class InvalidCommandException extends RuntimeException{
    public InvalidCommandException(String message){
        super(message);
    }
}
