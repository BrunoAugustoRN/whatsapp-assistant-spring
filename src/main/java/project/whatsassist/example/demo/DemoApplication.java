package project.whatsassist.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import project.whatsassist.example.demo.ia.IaService;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// teste temporário de conexão com a Gemini - remover depois de confirmar que funciona
	@Bean
	public CommandLineRunner testarGemini(IaService iaService) {
		return args -> {
			System.out.println("=== Testando conexão com a Gemini ===");
			System.out.println(iaService.testarConexao());
			System.out.println("=== Fim do teste ===");
		};
	}

}
