package com.dong.interpreter.data;

import com.dong.interpreter.Type;

public class Char implements Type<Character> {

    public char value;

    public Char(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}
