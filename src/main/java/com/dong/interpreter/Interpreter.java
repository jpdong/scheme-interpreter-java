package com.dong.interpreter;


import java.io.InputStream;

import static com.dong.interpreter.SchemeObject.SchemeType.*;

public class Interpreter {

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
    SchemeObject globalEnvironment;

    public void init() {
        emptyList = new SchemeObject();
        emptyList.type = THE_EMPTY_LIST;
        falses = new SchemeObject();
        falses.type = BOOL;
        falses.data.bool.value = 0;
        trues = new SchemeObject();
        trues.type = BOOL;
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

    private SchemeObject makeEnvironment() {
        SchemeObject env = setupEnvironment();
        populateEnvironment(env);
        return env;
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

    private void populateEnvironment(SchemeObject env) {
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

    private void addProcedure(SchemeObject env,String schemeName, Function function) {
        defineVariable(makeSymbol(schemeName), makePrimitiveProc(function), env);
    }

    private void defineVariable(SchemeObject var, SchemeObject val, SchemeObject env) {
        SchemeObject frame = firstFrame(env);
        SchemeObject vars = frameVars(frame);
        SchemeObject vals = frameVals(frame);
        while (!isEmptyList(vars)) {
            if (var.equals(car(vars))) {
                setCar(vals, val);
                return;
            }
            vars = cdr(vars);
            vals = cdr(vars);
        }
        addToFrame(var,val,frame);
    }

    private void addToFrame(SchemeObject var, SchemeObject val, SchemeObject frame) {
        setCar(frame,cons(var,car(frame)));
        setCdr(frame, cons(val, cdr(frame)));
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
        object.data.primitiveProc.fu = function;

        return object;
    }

    private SchemeObject setupEnvironment() {
        SchemeObject initEnv = extendEnvironment(emptyList, emptyList, emptyEnvironment);
        return initEnv;
    }

    private SchemeObject extendEnvironment(SchemeObject vars, SchemeObject vals, SchemeObject baseEnvironment) {

        return cons(makeFrame(vars,vals), baseEnvironment);
    }

    private SchemeObject makeFrame(SchemeObject vars, SchemeObject vals) {
        return cons(vars,vals);
    }

    private SchemeObject makeSymbol(String s) {
        SchemeObject element = symbolTable;
        while (!isEmptyList(element)) {
            if (s.equals(car(element).data.symbol.value)) {
                return car(element);
            }
            element = cdr(element);
        }
        SchemeObject object = new SchemeObject();
        object.type = SYMBOL;
        object.data.symbol.value = s;
        symbolTable = cons(object, symbolTable);
        return null;
    }

    private SchemeObject cons(SchemeObject object, SchemeObject symbolTable) {
        SchemeObject newTable = new SchemeObject();
        newTable.type = PAIR;
        newTable.data.pair.car = object;
        newTable.data.pair.cdr = symbolTable;
        return newTable;
    }

    private SchemeObject cdr(SchemeObject element) {
        return element.data.pair.cdr;
    }

    private SchemeObject car(SchemeObject element) {
        return element.data.pair.car;
    }

    private boolean isEmptyList(SchemeObject element) {
        return emptyList.equals(element);
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
            exp = readInput(System.in);
            if (exp == null) {
                continue;
            } else {
                Tool.print(eval(exp, globalEnvironment));
                Tool.print("\n");
            }
        }
    }

    private String eval(SchemeObject exp, SchemeObject globalEnvironment) {
        return null;
    }

    private SchemeObject readInput(InputStream in) {
        return null;
    }
}
