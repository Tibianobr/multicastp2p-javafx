package sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    Map<Integer,Recurso> recursos;
    Integer size;


    public Manager(List<Recurso> lista) {
        recursos = new HashMap<>();
        for (size = 0; size < lista.size() ; size++)
            recursos.put(size,lista.get(size));
        size = recursos.size();
    }

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
