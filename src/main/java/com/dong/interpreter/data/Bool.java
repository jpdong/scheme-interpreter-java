package com.dong.interpreter.data;

import com.dong.interpreter.Type;

public class Bool implements Type<Boolean> {

    public boolean value;

    public Bool(Boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
