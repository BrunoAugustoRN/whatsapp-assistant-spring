package project.whatsassist.example.demo.services;

import org.springframework.stereotype.Service;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;


@Service
public class IaService {


    Client client = new Client();

    public String testarConexao() {
        GenerateContentResponse response = client.models.generateContent(
                "gemini-3.6-flash",
                "Explain how AI works in a few sentences.",
                null
        );

        return response.text();
    }

}
