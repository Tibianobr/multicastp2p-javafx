package sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    [MANAGER] Gerenciadora de recursos do grupo
    Para evitar que os clientes acessem diretamente os recursos e também facilitar identificação dos mesmos
    essa classe consegue assemelhar o primeiro recurso livre já que tratamos eles como iguais e devolver para o cliente
    que está apto a usar
 */

public class Manager {
    Map<Integer,Recurso> recursos;
    Integer size;


    // Inicialização dos recursos no manager e configuração de variaveis básicas como tamanho
    public Manager(List<Recurso> lista) {
        recursos = new HashMap<>();
        for (size = 0; size < lista.size() ; size++)
            recursos.put(size,lista.get(size));
        size = recursos.size();
    }

    // Retorna o primeiro recurso livre da lista de recursos
    public Recurso getfirstFree()
    {
        int i;
        for (i = 0; i < recursos.size();i++)
        {
            if (recursos.get(i).getStatus() == 1)
            {
                return recursos.get(i);
            }
        }
        return null;
    }

}
