package com.dong.interpreter.data;

import com.dong.interpreter.Type;

import java.util.ArrayList;
import java.util.List;

public class Node implements Type<List<Type>> {

    public List<Type> typeList;

    public Node() {
        this.typeList = new ArrayList<>();
    }

}