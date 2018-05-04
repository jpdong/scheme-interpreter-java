package com.dong.interpreter.data;

import com.dong.interpreter.Type;

public class Symbol implements Type<String> {
    public String id;

    public Symbol(String id) {
        this.id = id;
    }

    public String getValue() {
        return id;
    }
}
