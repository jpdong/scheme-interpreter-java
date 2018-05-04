package com.dong.interpreter.func;

import com.dong.interpreter.Interpreter;
import com.dong.interpreter.Type;
import com.dong.interpreter.data.EnvFrame;
import com.dong.interpreter.data.Node;

import java.util.List;

public class Procedure implements Func<Type> {
    Node parms;
    Node body;
    EnvFrame env;
    Interpreter interpreter;

    public Procedure(Node parms, Node body, EnvFrame env,Interpreter interpreter) {
        this.parms = parms;
        this.body = body;
        this.env = env;
        this.interpreter = interpreter;
    }

    @Override
    public Type accept(List arguments) {
        return interpreter.eval(body,new EnvFrame(parms, arguments,env));
    }
}