package project.whatsassist.example.demo.ia;

import com.google.genai.gaos.models.interactions.Function;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;

import java.util.Map;

public class AssistantFunctions {

    //FunctionDeclaration = classe SDK Gemini
    FunctionDeclaration removerRotina = FunctionDeclaration.builder()//builder gerar objetos por parte

            .name("remover_rotina")//Identificador para a IA saber quando se referir a essa funcao

            .description("Remove uma rotina/tarefa agendada pelo ID. Use quando o usuário pedir para apagar, cancelar ou remover uma rotina específica.")//Descricao para IA decidir se essa e a funcao certa para o momento

            .parameters(//dados q a funcao precisa receber para funcionar

                    Schema.builder()//Schema formato para descrever a estrutura de dados dos parametros

                            .type(Type.Known.OBJECT)//"Type.Known.OBJECT" diz Os parametros ao todos formam um objeto

                            .properties(Map.of(//"Properties" lista quais campos esse objeto de parametros tem. "Map.of" cria um mapa (chave - valor) onde a chave e o nome do parametro "id", valor outro Schema descrevedno esse parametro especificamente

                                    "id", Schema.builder()
                                                    .type(Type.Known.INTEGER)//"Type.Known.INTEGER" diz q o campo "id" precisa ser um inteiro

                                                    .description("O ID numérico da rotina a ser removia.")//Descricao para IA entender o q esse campo significa

                                                    .build()//fecha e constroi o o objeto schema dos parametros

                            ))
                            .required("id")//campo "id" e obrigatorio

                            .build()//fecha os parameters
            )
            .build();//constroi o objeto
    /*
    * O bloco inteiro nao executa nenhuma acao, monta um formato estruturado, um JSON
    */


}
