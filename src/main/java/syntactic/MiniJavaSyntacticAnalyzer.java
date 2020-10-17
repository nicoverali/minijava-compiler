package syntactic;

import lexical.Token;
import lexical.TokenType;
import lexical.analyzer.LexicalSequence;

import java.util.Arrays;
import java.util.Optional;

import static lexical.TokenType.*;

public class MiniJavaSyntacticAnalyzer implements SyntacticAnalyzer {

    private final LexicalSequence sequence;

    public MiniJavaSyntacticAnalyzer(LexicalSequence sequence){
        this.sequence = sequence;
    }

    private void match(TokenType type) {
        Token next = sequence.next().orElseThrow(IllegalStateException::new);
        if (next.getType() == EOF){
            throw new SyntacticException("Se esperaba "+type+" pero se llego al final del archivo", next);
        } else if (next.getType() != type){
            throw new SyntacticException("Se esperaba "+type+" pero se encontro"+next.getType(), next);
        }
    }

    private boolean equalsAny(TokenType... types){
        Optional<TokenType> nextType = sequence.peek().map(Token::getType);
        return nextType
                .filter(type -> Arrays.asList(types).contains(type))
                .isPresent();
    }

    @Override
    public void analyze() throws SyntacticException{
        sequence.peek()
                .filter(token -> token.getType() != EOF)
                .ifPresent((t) -> inicial());
    }

    private void inicial() {
        listaClases();
    }

    private void listaClases() {
        clase();
        otrasClases();
    }

    private void otrasClases() {
        if (equalsAny(K_CLASS)) {
            listaClases();
        } else {
            // Nothing
        }
    }

    private void clase() {
        match(K_CLASS);
        match(ID_CLS);
        herencia();
        match(P_BRCKT_OPEN);
        listaMiembros();
        match(P_BRCKT_CLOSE);
    }

    private void listaMiembros() {
        if (equalsAny(K_PRIVATE, K_PUBLIC, ID_CLS, K_STATIC, K_DYNAMIC)) {
            miembro();
            listaMiembros();
        } else {
            // Nothing
        }
    }

    private void miembro() {
        if  (equalsAny(K_PRIVATE, K_PUBLIC)) {
            atributo();
        } else if  (equalsAny(ID_CLS)) {
            constructor();
        } else if  (equalsAny(K_STATIC, K_DYNAMIC)) {
            metodo();
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (miembro) pero se encontro"+next.getType(), next);
        }
    }

    private void metodo() {
        formaMetodo();
        tipoMetodo();
        match(ID_MV);
        argsFormales();
        bloque();
    }

    private void tipoMetodo() {
        if  (equalsAny(K_VOID)) {
            match(K_VOID);
        } else if  (equalsAny(ID_CLS, K_CHAR, K_INT, K_BOOLEAN, K_STRING)) {
            tipo();
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (tipoMetodo) pero se encontro"+next.getType(), next);
        }
    }

    private void formaMetodo() {
        if  (equalsAny(K_STATIC)) {
            match(K_STATIC);
        } else if  (equalsAny(K_DYNAMIC)) {
            match(K_DYNAMIC);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (formaMetodo) pero se encontro"+next.getType(), next);
        }
    }

    private void constructor() {
        match(ID_CLS);
        argsFormales();
        bloque();
    }

    private void bloque() {
        match(P_BRCKT_OPEN);
        listaSentencias();
        match(P_BRCKT_CLOSE);
    }

    private void listaSentencias() {
        if (equalsAny(K_IF, K_RETURN, ID_CLS, K_CHAR, K_INT, K_BOOLEAN, K_STRING, K_WHILE, P_SEMICOLON, P_BRCKT_OPEN, ID_MV, K_STATIC, K_NEW, K_THIS, P_PAREN_OPEN)) {
            sentencia();
            listaSentencias();
        } else {
            // Nothing
        }
    }

    private void sentencia() {
        if  (equalsAny(K_IF)) {
            match(K_IF);
            match(P_PAREN_OPEN);
            expresion();
            match(P_PAREN_CLOSE);
            sentencia();
            elseOVacio();
        } else if  (equalsAny(K_RETURN)) {
            match(K_RETURN);
            expresionOVacio();
            match(P_SEMICOLON);
        } else if  (equalsAny(ID_CLS, K_CHAR, K_INT, K_BOOLEAN, K_STRING)) {
            tipo();
            listaDecVars();
            match(P_SEMICOLON);
        } else if  (equalsAny(K_WHILE)) {
            match(K_WHILE);
            match(P_PAREN_OPEN);
            expresion();
            match(P_PAREN_CLOSE);
            sentencia();
        } else if  (equalsAny(P_SEMICOLON)) {
            match(P_SEMICOLON);
        } else if  (equalsAny(P_BRCKT_OPEN)) {
            bloque();
        } else if  (equalsAny(ID_MV, K_STATIC, K_NEW, K_THIS, P_PAREN_OPEN)) {
            acceso();
            asignacionOVacio();
            match(P_SEMICOLON);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (sentencia) pero se encontro"+next.getType(), next);
        }
    }

    private void asignacionOVacio() {
        if (equalsAny(ASSIGN_PLUS, ASSIGN, ASSIGN_MINUS)) {
            tipoDeAsignacion();
            expresion();
        } else {
            // Nothing
        }
    }

    private void tipoDeAsignacion() {
        if  (equalsAny(ASSIGN_PLUS)) {
            match(ASSIGN_PLUS);
        } else if  (equalsAny(ASSIGN)) {
            match(ASSIGN);
        } else if  (equalsAny(ASSIGN_MINUS)) {
            match(ASSIGN_MINUS);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (tipoDeAsignacion) pero se encontro"+next.getType(), next);
        }
    }

    private void listaDecVars() {
        match(ID_MV);
        otrosDecVars();
    }

    private void otrosDecVars() {
        if (equalsAny(P_COMMA)) {
            match(P_COMMA);
            listaDecVars();
        } else {
            // Nothing
        }
    }

    private void expresionOVacio() {
        if (equalsAny(OP_MINUS, OP_PLUS, OP_NOT, K_NULL, K_FALSE, STRING, CHAR, K_TRUE, INT, ID_MV, K_STATIC, K_NEW, K_THIS, P_PAREN_OPEN)) {
            expresion();
        } else {
            // Nothing
        }
    }

    private void elseOVacio() {
        if (equalsAny(K_ELSE)) {
            match(K_ELSE);
            sentencia();
        } else {
            // Nothing
        }
    }

    private void expresion() {
        expresionUnaria();
        expresionResto();
    }

    private void expresionResto() {
        if (equalsAny(OP_MINUS, OP_GT, OP_GTE, OP_LTE, OP_NOTEQ, OP_PLUS, OP_LT, OP_DIV, OP_OR, OP_AND, OP_MULT, OP_EQ, OP_MOD)) {
            operadorBinario();
            expresionUnaria();
            expresionResto();
        } else {
            // Nothing
        }
    }

    private void operadorBinario() {
        if  (equalsAny(OP_MINUS)) {
            match(OP_MINUS);
        } else if  (equalsAny(OP_GT)) {
            match(OP_GT);
        } else if  (equalsAny(OP_GTE)) {
            match(OP_GTE);
        } else if  (equalsAny(OP_LTE)) {
            match(OP_LTE);
        } else if  (equalsAny(OP_NOTEQ)) {
            match(OP_NOTEQ);
        } else if  (equalsAny(OP_PLUS)) {
            match(OP_PLUS);
        } else if  (equalsAny(OP_LT)) {
            match(OP_LT);
        } else if  (equalsAny(OP_DIV)) {
            match(OP_DIV);
        } else if  (equalsAny(OP_OR)) {
            match(OP_OR);
        } else if  (equalsAny(OP_AND)) {
            match(OP_AND);
        } else if  (equalsAny(OP_MULT)) {
            match(OP_MULT);
        } else if  (equalsAny(OP_EQ)) {
            match(OP_EQ);
        } else if  (equalsAny(OP_MOD)) {
            match(OP_MOD);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (operadorBinario) pero se encontro"+next.getType(), next);
        }
    }

    private void expresionUnaria() {
        if  (equalsAny(OP_MINUS, OP_PLUS, OP_NOT)) {
            operadorUnario();
            operando();
        } else if  (equalsAny(K_NULL, K_FALSE, STRING, CHAR, K_TRUE, INT, ID_MV, K_STATIC, K_NEW, K_THIS, P_PAREN_OPEN)) {
            operando();
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (expresionUnaria) pero se encontro"+next.getType(), next);
        }
    }

    private void operando() {
        if  (equalsAny(K_NULL, K_FALSE, STRING, CHAR, K_TRUE, INT)) {
            literal();
        } else if  (equalsAny(ID_MV, K_STATIC, K_NEW, K_THIS, P_PAREN_OPEN)) {
            acceso();
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (operando) pero se encontro"+next.getType(), next);
        }
    }

    private void acceso() {
        primario();
        encadenado();
    }

    private void encadenado() {
        if (equalsAny(P_DOT)) {
            varOMetodoEncadenado();
            encadenado();
        } else {
            // Nothing
        }
    }

    private void varOMetodoEncadenado() {
        match(P_DOT);
        match(ID_MV);
        argsActualesOVacio();
    }

    private void primario() {
        if  (equalsAny(ID_MV)) {
            accesoVarOMetodo();
        } else if  (equalsAny(K_STATIC)) {
            accesoEstatico();
        } else if  (equalsAny(K_NEW)) {
            accesoConstructor();
        } else if  (equalsAny(K_THIS)) {
            accesoThis();
        } else if  (equalsAny(P_PAREN_OPEN)) {
            match(P_PAREN_OPEN);
            expresion();
            match(P_PAREN_CLOSE);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (primario) pero se encontro"+next.getType(), next);
        }
    }

    private void accesoThis() {
        match(K_THIS);
    }

    private void accesoConstructor() {
        match(K_NEW);
        match(ID_CLS);
        argsActuales();
    }

    private void accesoEstatico() {
        match(K_STATIC);
        match(ID_CLS);
        match(P_DOT);
        accesoMetodo();
    }

    private void accesoMetodo() {
        match(ID_MV);
        argsActuales();
    }

    private void accesoVarOMetodo() {
        match(ID_MV);
        argsActualesOVacio();
    }

    private void argsActualesOVacio() {
        if (equalsAny(P_PAREN_OPEN)) {
            argsActuales();
        } else {
            // Nothing
        }
    }

    private void argsActuales() {
        match(P_PAREN_OPEN);
        listaExpsOVacio();
        match(P_PAREN_CLOSE);
    }

    private void listaExpsOVacio() {
        if (equalsAny(OP_MINUS, OP_PLUS, OP_NOT, K_NULL, K_FALSE, STRING, CHAR, K_TRUE, INT, ID_MV, K_STATIC, K_NEW, K_THIS, P_PAREN_OPEN)) {
            listaExps();
        } else {
            // Nothing
        }
    }

    private void listaExps() {
        expresion();
        otrosExps();
    }

    private void otrosExps() {
        if (equalsAny(P_COMMA)) {
            match(P_COMMA);
            listaExps();
        } else {
            // Nothing
        }
    }

    private void literal() {
        if  (equalsAny(K_NULL)) {
            match(K_NULL);
        } else if  (equalsAny(K_FALSE)) {
            match(K_FALSE);
        } else if  (equalsAny(STRING)) {
            match(STRING);
        } else if  (equalsAny(CHAR)) {
            match(CHAR);
        } else if  (equalsAny(K_TRUE)) {
            match(K_TRUE);
        } else if  (equalsAny(INT)) {
            match(INT);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (literal) pero se encontro"+next.getType(), next);
        }
    }

    private void operadorUnario() {
        if  (equalsAny(OP_MINUS)) {
            match(OP_MINUS);
        } else if  (equalsAny(OP_PLUS)) {
            match(OP_PLUS);
        } else if  (equalsAny(OP_NOT)) {
            match(OP_NOT);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (operadorUnario) pero se encontro"+next.getType(), next);
        }
    }

    private void argsFormales() {
        match(P_PAREN_OPEN);
        listaArgsFormalesOVacio();
        match(P_PAREN_CLOSE);
    }

    private void listaArgsFormalesOVacio() {
        if (equalsAny(ID_CLS, K_CHAR, K_INT, K_BOOLEAN, K_STRING)) {
            listaArgsFormales();
        } else {
            // Nothing
        }
    }

    private void listaArgsFormales() {
        argFormal();
        otrosArgsFormales();
    }

    private void otrosArgsFormales() {
        if (equalsAny(P_COMMA)) {
            match(P_COMMA);
            listaArgsFormales();
        } else {
            // Nothing
        }
    }

    private void argFormal() {
        tipo();
        match(ID_MV);
    }

    private void atributo() {
        visibilidad();
        tipo();
        listaDecAtrs();
        match(P_SEMICOLON);
    }

    private void listaDecAtrs() {
        match(ID_MV);
        otrosDecAtrs();
    }

    private void otrosDecAtrs() {
        if (equalsAny(P_COMMA)) {
            match(P_COMMA);
            listaDecAtrs();
        } else {
            // Nothing
        }
    }

    private void tipo() {
        if  (equalsAny(ID_CLS)) {
            match(ID_CLS);
        } else if  (equalsAny(K_CHAR, K_INT, K_BOOLEAN, K_STRING)) {
            tipoPrimitivo();
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (tipo) pero se encontro"+next.getType(), next);
        }
    }

    private void tipoPrimitivo() {
        if  (equalsAny(K_CHAR)) {
            match(K_CHAR);
        } else if  (equalsAny(K_INT)) {
            match(K_INT);
        } else if  (equalsAny(K_BOOLEAN)) {
            match(K_BOOLEAN);
        } else if  (equalsAny(K_STRING)) {
            match(K_STRING);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (tipoPrimitivo) pero se encontro"+next.getType(), next);
        }
    }

    private void visibilidad() {
        if  (equalsAny(K_PRIVATE)) {
            match(K_PRIVATE);
        } else if  (equalsAny(K_PUBLIC)) {
            match(K_PUBLIC);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (visibilidad) pero se encontro"+next.getType(), next);
        }
    }

    private void herencia() {
        if (equalsAny(K_EXTENDS)) {
            match(K_EXTENDS);
            match(ID_CLS);
        } else {
            // Nothing
        }
    }



}
