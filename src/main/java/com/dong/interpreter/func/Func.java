package com.dong.interpreter.func;

import com.dong.interpreter.Type;
import com.dong.interpreter.data.EnvFrame;
import com.dong.interpreter.data.Node;

import java.util.List;

public abstract class Func<T> implements Type {
    public T accept(List<Type> arguments){return null;}

    public void accept(List<Type> arguments, Result result, EnvFrame env){}
}
