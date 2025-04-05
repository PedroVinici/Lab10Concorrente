package com.example.demo;

import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.ArrayUtils;
import java.util.*;

@Service
public class ProductService {
    //Aqui já pode ter a estrutura para guardar os produtos, não precisa de repositório
    public List<Product> getProducts(){
        List<Product> list = new ArrayList<Product>();
        return list;
    }
}