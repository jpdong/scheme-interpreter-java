package com.dong.interpreter;

import com.dong.interpreter.data.*;
import com.dong.interpreter.func.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Interpreter {

    EnvFrame globalEnvironment;

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();
        interpreter.init();
        if (args == null || args.length == 0) {
            interpreter.start();
        } else {
            interpreter.readCodeFromFile(args[0]);
        }
    }

    private String readCodeFromFile(String name) {
        File file = new File(name);
        String result = null;
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                result = stringBuilder.toString();
            } catch (IOException e) {
                Tool.print(e.toString() + "\n");
                e.printStackTrace();
            }
        } else {
            Tool.print("no such file\n");
        }
        List<Type> asts = parse(result);
        Type value;
        for (Type ast : asts) {
            value = eval(ast, globalEnvironment);
            if (value != null) {
                Tool.print(listStr(value) + "\n");
            }
        }
        return result;
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
                    /*Type ast = parseSingle(line);
                    Tool.print("ast : " + listStr(ast) + "\n");
                    value = eval(ast, globalEnvironment);
                    if (value != null) {
                        Tool.print(listStr(value) + "\n");
                    }*/
                    List<Type> asts = parse(line);
                    for (Type ast : asts) {
                        value = eval(ast, globalEnvironment);
                        if (value != null) {
                            Tool.print(listStr(value) + "\n");
                        }
                    }
                }
            }
        } catch (IOException e) {
            Tool.print(e.toString() + "\n");
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
        } else if (value instanceof Int) {
            result = String.valueOf(((Int) value).getValue());
        } else if (value instanceof Symbol) {
            result = ((Symbol) value).getValue();
        } else if (value instanceof Bool) {
            result = ((Bool) value).getValue() ? "#t" : "#f";
        }
        return result;
    }

    public Type eval(Type ast, EnvFrame env) {
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
            Type trueBody = list.get(2);
            Type falseBody = list.get(3);
            Bool result = (Bool) eval(test, env);
            if (result.value) {
                return eval(trueBody, env);
            } else {
                return eval(falseBody, env);
            }
        } else if ("let".equals(symbol.id)) {
            Node field = (Node) list.get(1);
            Type body = list.get(2);
            EnvFrame extEnv = new EnvFrame(env);
            for (int i = 0; i < field.typeList.size(); i++) {
                Node pair = (Node) field.typeList.get(i);
                Symbol s = (Symbol) pair.typeList.get(0);
                Type result = eval(pair.typeList.get(1), extEnv);
                extEnv.put(s.id, result);
            }
            return eval(body, extEnv);
        } else if ("define".equals(symbol.id)) {
            Symbol s = (Symbol) list.get(1);
            Type type = list.get(2);
            env.frameMap.put(s.id, eval(type, env));
        } else if ("lambda".equals(symbol.id)) {
            Node parms = (Node) list.get(1);
            Node body = (Node) list.get(2);
            return new Procedure(parms, body, env, this);
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

    private Type parseSingle(String program) {
        return readFromTokens(tokenize(program));
    }

    private List<Type> parse(String program) {
        Queue<String> queue = tokenize(program);
        List<Type> astList = new ArrayList<>();
        while (!queue.isEmpty()) {
            astList.add(readFromTokens(queue));
        }
        return astList;
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

    public void init() {
        globalEnvironment = makeEnvironment();
    }

    private EnvFrame makeEnvironment() {
        EnvFrame frame = new EnvFrame();
        initEnvironment(frame);
        return frame;
    }

    private void initEnvironment(EnvFrame env) {
        Func addProc = new AddProc();
        env.put("+", addProc);
        Func lessProc = new LessProc();
        env.put("<", lessProc);
        Func subProc = new SubProc();
        env.put("-", subProc);
        Func multiProc = new MultiProc();
        env.put("*", multiProc);
        Func divisionProc = new DivisionProc();
        env.put("/", divisionProc);
    }

    public String testInput(String s) {
        return listStr(eval(parseSingle(s), globalEnvironment));
    }
}
