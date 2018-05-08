package com.dong.interpreter.data;

import com.dong.interpreter.Type;

public class Str implements Type<String> {
    public String value;

    public Str(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
