package com.dong.interpreter;

import com.dong.interpreter.data.EnvFrame;
import com.dong.interpreter.data.Int;
import com.dong.interpreter.data.Node;
import com.dong.interpreter.data.Symbol;
import com.dong.interpreter.func.AddProc;
import com.dong.interpreter.func.Func;
import com.dong.interpreter.func.Procedure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Interpreter {

    EnvFrame globalEnvironment;


    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();
        interpreter.init();
        interpreter.start();
    }

    private void start() {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        Type value;
        String line;
        try {
            while (true) {
                Tool.print("> ");
                line = reader.readLine();
                if (line != null) {
                    Type ast = parse(line);
                    Tool.print("ast : " + listStr(ast) + "\n");
                    value = eval(ast,globalEnvironment);
                    if (value != null) {
                        Tool.print(listStr(value) + "\n");
                    }
                }
            }
        } catch (IOException e) {
            Tool.print(e.toString()+"\n");
            e.printStackTrace();
        }
    }

    private String listStr(Type value) {
        String result = null;
        StringBuilder stringBuilder = new StringBuilder();
        if (value instanceof Node) {
            stringBuilder.append("(");
            for (Type type : ((Node) value).typeList) {
                stringBuilder.append(listStr(type) + " ");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append(")");
            result = stringBuilder.toString();
        } else if (value instanceof Int){
            result = String.valueOf(((Int)value).getValue());
        } else if (value instanceof Symbol) {
            result = ((Symbol) value).getValue();
        }
        return result;
    }

    public Type eval(Type ast,EnvFrame env) {
        if (ast instanceof Symbol) {
            return env.lookupSymbol(((Symbol) ast).getValue());
        } else if (!(ast instanceof Node)) {
            return ast;
        }
        List<Type> list = ((Node) ast).typeList;
        Symbol symbol = (Symbol) list.get(0);
        if ("quote".equals(symbol.id)) {
            return list.get(1);
        } else if ("if".equals(symbol.id)) {
            Node test = (Node) list.get(1);
            Node trueBody = (Node) list.get(2);
            Node falseBody = (Node) list.get(3);
            Symbol result = (Symbol) eval(test, env);
            if ("true".equals(result.id)) {
                return eval(trueBody, env);
            } else {
                return eval(falseBody, env);
            }
        } else if ("define".equals(symbol.id)) {
            Symbol s = (Symbol) list.get(1);
            Type type = list.get(2);
            env.frameMap.put(s.id, eval(type, env));
        } else if ("lambda".equals(symbol.id)) {
            Node parms = (Node) list.get(1);
            Node body = (Node) list.get(2);
            return new Procedure(parms, body, env,this);
        } else {
            Func<Type> proc = (Func) eval(list.get(0), env);
            List<Type> arguments = getArguments(list, env);
            return proc.accept(arguments);
        }
        return null;
    }

    private List<Type> getArguments(List<Type> list, EnvFrame env) {
        List<Type> result = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            result.add(eval(list.get(i), env));
        }
        return result;
    }

    private Type parse(String program) {
        return readFromTokens(tokenize(program));
    }

    private Queue<String> tokenize(String s) {
        String string = s.replace("(", " ( ").replace(")", " ) ");
        String[] strings = string.split("\\s+");
        Queue<String> queue = new LinkedList<>();
        for (int i = 0; i < strings.length; i++) {
            if (!"".equals(strings[i])) {
                queue.offer(strings[i]);
            }
        }
        return queue;
    }

    private Type readFromTokens(Queue<String> tokens) {
        if (tokens.isEmpty()) {
            throw new RuntimeException("unexpected EOF while reading");
        } else {
            String s = tokens.poll();
            if (s.equals("(")) {
                Node node = new Node();
                while (!")".equals(tokens.peek())) {

                    node.typeList.add(readFromTokens(tokens));
                }
                tokens.poll();
                return node;
            } else if (")".equals(s)) {
                throw new RuntimeException("unexpected ) while reading");
            } else {
                return atom(s);
            }
        }
    }

    private Type atom(String s) {
        try {
            return new Int(Integer.valueOf(s));
        } catch (NumberFormatException e) {
            return new Symbol(s);
        }
    }

    private void init() {
        globalEnvironment = makeEnvironment();
    }

    private EnvFrame makeEnvironment() {
        EnvFrame frame = new EnvFrame();
        initEnvironment(frame);
        return frame;
    }

    private void initEnvironment(EnvFrame env) {
        Func addProc = new AddProc();
        env.frameMap.put("+", addProc);
    }







    /*static class Float extends Type {
        public float value;

        public Float(float value) {
            this.value = value;
        }

        @Override
        public Float getValue() {
            return new Float(value);
        }
    }*/
}
