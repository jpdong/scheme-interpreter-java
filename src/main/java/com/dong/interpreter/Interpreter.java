package com.dong.interpreter;


import com.dong.interpreter.data.SchemeData;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;

import static com.dong.interpreter.data.SchemeType.Type.*;

public class Interpreter {

    static class EnvFrame {
        HashMap<String, SchemeObject> frameMap;
        EnvFrame nextFrame;

        public EnvFrame() {
            this.frameMap = new HashMap<>();
        }
    }

    SchemeObject emptyList;
    SchemeObject falses;
    SchemeObject trues;
    SchemeObject symbolTable;
    SchemeObject quoteSymbol;
    SchemeObject defineSymbol;
    SchemeObject setSymbol;
    SchemeObject okSymbol;
    SchemeObject ifSymbol;
    SchemeObject lambdaSymbol;
    SchemeObject beginSymbol;
    SchemeObject condSymbol;
    SchemeObject elseSymbol;
    SchemeObject letSymbol;
    SchemeObject andSymbol;
    SchemeObject orSymbol;
    SchemeObject eofObject;
    SchemeObject emptyEnvironment;
    EnvFrame globalEnvironment;

    public void init() {
        emptyList = new SchemeObject();
        emptyList.type = THE_EMPTY_LIST;
        falses = new SchemeObject();
        falses.type = BOOL;
        falses.data = new SchemeData(falses.type);
        falses.data.bool.value = 0;
        trues = new SchemeObject();
        trues.type = BOOL;
        trues.data = new SchemeData(trues.type);
        trues.data.bool.value = 1;
        symbolTable = emptyList;
        quoteSymbol = makeSymbol("quote");
        defineSymbol = makeSymbol("define");
        setSymbol = makeSymbol("set!");
        okSymbol = makeSymbol("ok");
        ifSymbol = makeSymbol("if");
        lambdaSymbol = makeSymbol("lambda");
        beginSymbol = makeSymbol("begin");
        condSymbol = makeSymbol("cond");
        elseSymbol = makeSymbol("else");
        letSymbol = makeSymbol("let");
        andSymbol = makeSymbol("and");
        orSymbol = makeSymbol("or");

        eofObject = new SchemeObject();
        eofObject.type = EOF_OBJECT;

        emptyEnvironment = emptyList;

        globalEnvironment = makeEnvironment();
    }

    /*private SchemeObject makeEnvironment() {
        SchemeObject env = setupEnvironment();
        populateEnvironment(env);
        return env;
    }*/

    private EnvFrame makeEnvironment() {
        EnvFrame frame = new EnvFrame();
        populateEnvironment(frame);
        return frame;
    }

    Function isNullProc;
    Function addProc;

    class isNullProc implements Function {
        @Override
        public SchemeObject accept(SchemeObject arguments) {
            return isEmptyList(car(arguments)) ? trues:falses;
        }
    }
    
    class addProc implements Function {

        @Override
        public SchemeObject accept(SchemeObject arguments) {
            long result = 0;
            while (!isEmptyList(arguments)) {
                result += car(arguments).data.num.value;
                arguments = cdr(arguments);
            }
            return makeNum(result);
        }

        private SchemeObject makeNum(long value) {
            SchemeObject object = new SchemeObject();
            object.type = NUM;
            object.data.num.value = value;
            return object;
        }
    }

    private void populateEnvironment(EnvFrame env) {
        addProcedure(env,"null?"      , isNullProc);
        /*
        addProcedure(env,"boolean?"   , isBooleanProc);
        addProcedure(env,"symbol?"    , isSymbolProc);
        addProcedure(env,"integer?"   , isIntegerProc);
        addProcedure(env,"char?"      , isCharProc);
        addProcedure(env,"string?"    , isStringProc);
        addProcedure(env,"pair?"      , isPairProc);
        addProcedure(env,"procedure?" , isProcedureProc);

        addProcedure(env,"char->integer" , charToIntegerProc);
        addProcedure(env,"integer->char" , integerToCharProc);
        addProcedure(env,"number->string", numberToStringProc);
        addProcedure(env,"string->number", stringToNumberProc);
        addProcedure(env,"symbol->string", symbolToStringProc);
        addProcedure(env,"string->symbol", stringToSymbolProc);
        */
        addProcedure(env,"+"        , addProc);
        /*
        addProcedure(env,"-"        , subProc);
        addProcedure(env,"*"        , mulProc);
        addProcedure(env,"quotient" , quotientProc);
        addProcedure(env,"remainder", remainderProc);
        addProcedure(env,"="        , isNumberEqualProc);
        addProcedure(env,"<"        , isLessThanProc);
        addProcedure(env,">"        , isGreaterThanProc);

        addProcedure(env,"cons"    , consProc);
        addProcedure(env,"car"     , carProc);
        addProcedure(env,"cdr"     , cdrProc);
        addProcedure(env,"set-car!", setCarProc);
        addProcedure(env,"set-cdr!", setCdrProc);
        addProcedure(env,"list"    , listProc);

        addProcedure(env,"eq?", isEqProc);

        addProcedure(env,"apply", applyProc);

        addProcedure(env,"interaction-environment",
                interactionEnvironmentProc);
        addProcedure(env,"null-environment", nullEnvironmentProc);
        addProcedure(env,"environment"     , environmentProc);
        addProcedure(env,"eval"            , evalProc);

        addProcedure(env,"load"             , loadProc);
        addProcedure(env,"open-input-port"  , openInputPortProc);
        addProcedure(env,"close-input-port" , closeInputPortProc);
        addProcedure(env,"input-port?"      , isInputPortProc);
        addProcedure(env,"read"             , readProc);
        addProcedure(env,"read-char"        , readCharProc);
        addProcedure(env,"peek-char"        , peekCharProc);
        addProcedure(env,"eof-object?"      , isEofObjectProc);
        addProcedure(env,"open-output-port" , openOutputPortProc);
        addProcedure(env,"close-output-port", closeOutputPortProc);
        addProcedure(env,"output-port?"     , isOutputPortProc);
        addProcedure(env,"write-char"       , writeCharProc);
        addProcedure(env,"write"            , writeProc);

        addProcedure(env,"error", errorProc);
        */
    }

    private void addProcedure(EnvFrame env,String schemeName, Function function) {
        defineVariable(schemeName, makePrimitiveProc(function), env);
    }

    private void defineVariable(String symbol, SchemeObject val, EnvFrame env) {
        String key = symbol;
        env.frameMap.put(key, val);
    }

    private void addToFrame(SchemeObject var, SchemeObject val, SchemeObject frame) {
        setCar(frame,var);
        setCdr(frame, val);
    }

    private void setCdr(SchemeObject object, SchemeObject val) {
        object.data.pair.cdr = val;
    }

    private void setCar(SchemeObject object, SchemeObject val) {
        object.data.pair.car = val;
    }

    private SchemeObject frameVals(SchemeObject frame) {
        return cdr(frame);
    }

    private SchemeObject frameVars(SchemeObject frame) {
        return car(frame);
    }

    private SchemeObject firstFrame(SchemeObject env) {
        return car(env);
    }

    private SchemeObject makePrimitiveProc(Function function) {
        SchemeObject object = new SchemeObject();
        object.type = PRIMITIVE_PROC;
        object.data = new SchemeData(object.type);
        object.data.primitiveProc.fu = function;
        return object;
    }



    private EnvFrame extendEnvironment(SchemeObject vars, SchemeObject vals, EnvFrame baseEnvironment) {
        EnvFrame newFrame = new EnvFrame();
        newFrame.nextFrame = baseEnvironment;
        return newFrame;
    }

    private SchemeObject makeFrame(SchemeObject vars, SchemeObject vals) {
        return cons(vars,vals);
    }

    private SchemeObject makeSymbol(String s) {
        SchemeObject element = symbolTable;
        while (!isEmptyList(element)) {
            if (element.data != null) {
                if (s.equals(car(element).data.symbol.value)) {
                    return car(element);
                } else {
                    element = cdr(element);
                }
            }
        }
        SchemeObject object = new SchemeObject();
        object.type = SYMBOL;
        object.data = new SchemeData(object.type);
        object.data.symbol.value = s;
        symbolTable = cons(object, symbolTable);
        return symbolTable;
    }

    private SchemeObject cons(SchemeObject car, SchemeObject cdr) {
        SchemeObject newTable = new SchemeObject();
        newTable.type = PAIR;
        newTable.data = new SchemeData(newTable.type);
        newTable.data.pair.car = car;
        newTable.data.pair.cdr = cdr;
        return newTable;
    }

    private SchemeObject cdr(SchemeObject element) {
        return element.data == null? null:element.data.pair.cdr;
    }

    private SchemeObject car(SchemeObject element) {
        return element.data.pair.car;
    }

    private boolean isEmptyList(SchemeObject object) {
        return object.type == THE_EMPTY_LIST;
    }

    public static void main(String[] args) {
        Tool.print("Welcome to SchemeInterpreter. Use ctrl-c to exit.\n");
        Interpreter interpreter = new Interpreter();
        interpreter.init();
        interpreter.start();
    }

    private void start() {
        SchemeObject exp;
        while (true) {
            Tool.print("> ");
            exp = readInput(new PushbackInputStream(System.in));
            if (exp == null) {
                break;
            } else {
                Tool.print(eval(exp, globalEnvironment));
                Tool.print("\n");
            }
        }
    }

    private SchemeObject eval(SchemeObject exp, EnvFrame env) {
        SchemeObject procedure;
        SchemeObject arguments;
        if (isSymbol(exp)) {
            return lookupVariableValue(exp, env);
        } else if (isPair(exp)) {
            procedure = eval(operator(exp), env);
            arguments = listOfValues(operand(exp), env);
            if (isPrimitiveProc(procedure)) {
                return  procedure.data.primitiveProc.fu.accept(arguments);
            }
        } else if (isSelfEvaluation(exp)) {
            return exp;
        }
        return null;
    }

    private boolean isSelfEvaluation(SchemeObject exp) {
        return isBoolean(exp)   ||
                isNum(exp)    ||
                isCharacters(exp) ||
                isStrings(exp);
    }

    private boolean isBoolean(SchemeObject exp) {
        return exp.type == BOOL;
    }

    private boolean isNum(SchemeObject exp) {
        return exp.type == NUM;
    }

    private boolean isCharacters(SchemeObject exp) {
        return exp.type == CHARACTER;
    }

    private boolean isStrings(SchemeObject exp) {
        return exp.type == STRING;
    }

    private SchemeObject listOfValues(SchemeObject exps, EnvFrame env) {
        if (isEmptyList(exps)) {
            return emptyList;
        } else {
            return cons(eval(car(exps), env), listOfValues(cdr(exps), env));
        }
    }

    private SchemeObject lookupVariableValue(SchemeObject object, EnvFrame env) {
        String key = object.data.symbol.value;
        while (env != null) {
            if (env.frameMap.containsKey(key)) {
                return env.frameMap.get(key);
            }
            env = env.nextFrame;
        }
        return null;
    }

    private SchemeObject operand(SchemeObject object) {
        return cdr(object);
    }

    private SchemeObject operator(SchemeObject object) {
        return car(object);
    }

    private boolean isPrimitiveProc(SchemeObject object) {
        return object.type == PRIMITIVE_PROC;
    }

    private boolean isPair(SchemeObject object) {
        return object.type == PAIR;
    }

    private boolean isSymbol(SchemeObject object) {
        return object.type == SYMBOL;
    }

    private SchemeObject readInput(PushbackInputStream inputStream) {
        short sign = 1;
        int index = 0;
        int num = 0;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            eatWhitespace(inputStream);
            char c = (char) inputStream.read();
            if (c == '(') {
                return readPair(inputStream);
            } else if (isInitial(c) || ((c == '+' || c == '-') && isDelimiter(peek(inputStream)))) {
                index = 0;
                while (isInitial(c) || Character.isDigit(c) || c == '+' || c == '-') {
                    if (index < 1000) {
                        stringBuilder.append(c);
                    } else {
                        Tool.print("symbol too long.");
                        return null;
                    }
                    c = (char) inputStream.read();
                }
                if (isDelimiter(c)) {
                    inputStream.unread(c);
                    return makeSymbol(stringBuilder.toString());
                } else {
                    Tool.print("symbol not followed by delimiter");
                    return null;
                }
            } else if (Character.isDigit(c) || (c == '-' && Character.isDigit(peek(inputStream)))) {
                if (c == '-') {
                    sign = -1;
                } else {
                    inputStream.unread(c);
                }
                while (Character.isDigit(c = (char) inputStream.read())) {
                    num = num * 10 + (c - '0');
                }
                num *= sign;
                if (isDelimiter(c)) {
                    inputStream.unread(c);
                    return makeNum(num);
                }
            } else if (c == '\n') {
                return null;
            }
        } catch (IOException e) {
            Tool.print(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    private SchemeObject makeNum(int num) {
        SchemeObject object = new SchemeObject();
        object.type = NUM;
        object.data = new SchemeData(object.type);
        object.data.num.value = num;
        return object;
    }

    private boolean isDelimiter(char c) {
        return c == ' ' || c == -1 ||
                c == '('   || c == ')' ||
                c == '"'   || c == ';';
    }

    private char peek(PushbackInputStream inputStream) throws IOException {
        char c = (char) inputStream.read();
        inputStream.unread(c);
        return c;
    }

    private boolean isInitial(char c) {
        //char[] chars = new char[]{'*','/','>','<','=','?','!'};
        return Character.isAlphabetic(c) || c == '*' || c == '/' || c == '>' ||
                c == '<' || c == '=' || c == '?' || c == '!';
    }

    private SchemeObject readPair(PushbackInputStream inputStream) {
        SchemeObject carObj = null;
        SchemeObject cdrObj;
        try {
            eatWhitespace(inputStream);
            char c = (char) inputStream.read();
            if (c == ')') {
                return emptyList;
            }
            inputStream.unread(c);
            carObj = readInput(inputStream);
            eatWhitespace(inputStream);
        } catch (IOException e) {
            Tool.print(e.toString());
            e.printStackTrace();
        }
        //c = inputStream.read();
        cdrObj = readPair(inputStream);
        return cons(carObj,cdrObj);
    }

    private void eatWhitespace(PushbackInputStream inputStream) throws IOException {
        char c;
        while ((c = (char) inputStream.read()) != '\n') {
            if (c == ' ') {
                continue;
            } else if (c == ';') {
                while ((c = (char) inputStream.read()) != '\n' && c != '\n') {
                    continue;
                }
            }
            inputStream.unread(c);
            break;
        }

    }
}
