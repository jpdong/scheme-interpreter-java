package com.dong.interpreter.data;

import com.dong.interpreter.Type;

public class Int implements Type<Integer> {
    public int value;
    public Int(Integer value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
