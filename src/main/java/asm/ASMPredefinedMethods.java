package asm;

import semantic.symbol.predefined.PredefinedMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ASMPredefinedMethods {

    private static final Map<String, List<String>> methods = new HashMap();

    static {
        methods.put("debugPrint$int@Object", List.of(
                "LOAD 3\t;\tCargar parametro",
                "IPRINT\t;\tImprime el numero",
                "PRNLN\t;\tFinaliza la linea"
        ));
        methods.put("read$@System", List.of(
                "READ\t;\tLeer de STDIN",
                "STORE 3\t;\tAlmacenar en valor de retorno",
                "STOREFP\t;\tReestablecer FP a RA anterior",
                "RET 0\t;\tSubir la stack n posiciones para volver al RA anterior"
        ));
        methods.put("printB$boolean@System", List.of(
                "LOAD 3\t;\tCargar parametro booleano",
                "BPRINT\t;\tImprimir valor booleano"
        ));
        methods.put("printC$char@System", List.of(
                "LOAD 3\t;\tCargar parametro character",
                "CPRINT\t;\tImprimir valor character"
        ));
        methods.put("printI$int@System", List.of(
                "LOAD 3\t;\tCargar parametro int",
                "IPRINT\t;\tImprimir valor int"
        ));
        methods.put("printS$String@System", List.of(
                "LOAD 3\t;\tCargar parametro String",
                "SPRINT\t;\tImprimir valor String"
        ));
        methods.put("println$@System", List.of(
                "PRNLN\t;\tImprimir salto de linea"
        ));
        methods.put("printBln$boolean@System", List.of(
                "LOAD 3\t;\tCargar parametro booleano",
                "BPRINT\t;\tImprimir valor booleano",
                "PRNLN\t;\tImprimir salto de linea"
        ));
        methods.put("printCln$char@System", List.of(
                "LOAD 3\t;\tCargar parametro character",
                "CPRINT\t;\tImprimir valor character",
                "PRNLN\t;\tImprimir salto de linea"
        ));
        methods.put("printIln$int@System", List.of(
                "LOAD 3\t;\tCargar parametro int",
                "IPRINT\t;\tImprimir valor int",
                "PRNLN\t;\tImprimir salto de linea"
        ));
        methods.put("printSln$String@System", List.of(
                "LOAD 3\t;\tCargar parametro String",
                "SPRINT\t;\tImprimir valor String",
                "PRNLN\t;\tImprimir salto de linea"
        ));
    }

    /**
     * Receives a predefined method and returns the ASM code of it.
     * An error will be thrown if no method is found.
     *
     * @param method a {@link PredefinedMethod}
     * @return the code of the given {@link PredefinedMethod}
     * @throws IllegalArgumentException if the given label does not belong to any predefined method
     */
    public static List<String> getCodeOf(PredefinedMethod method) throws IllegalArgumentException{
        List<String> code = methods.get(ASMLabeler.label(method));
        if (code == null) throw new IllegalArgumentException("Predefined method " + method.getName() + " undefined");
        return code;
    }

}
