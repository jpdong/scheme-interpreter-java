package com.dong.interpreter;

public interface Function {

    public SchemeObject accept(SchemeObject arguments);
}
