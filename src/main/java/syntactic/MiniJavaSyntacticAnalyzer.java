package syntactic;

import lexical.Token;
import lexical.TokenType;
import lexical.analyzer.LexicalSequence;

import java.util.Arrays;
import java.util.Optional;

import static lexical.TokenType.*;

public class MiniJavaSyntacticAnalyzer implements SyntacticAnalyzer {

    private final LexicalSequence sequence;

    public MiniJavaSyntacticAnalyzer(LexicalSequence sequence) {
        this.sequence = sequence;
    }

    private void match(TokenType type) {
        Token next = sequence.next().orElseThrow(IllegalStateException::new);
        if (next.getType() == EOF) {
            throw new SyntacticException("Se esperaba " + type + " pero se llego al final del archivo", next);
        } else if (next.getType() != type) {
            throw new SyntacticException("Se esperaba " + type + " pero se encontro" + next.getType(), next);
        }
    }

    private boolean equalsAny(TokenType... types) {
        Optional<TokenType> nextType = sequence.peek().map(Token::getType);
        return nextType
                .filter(type -> Arrays.asList(types).contains(type))
                .isPresent();
    }

    @Override
    public void analyze() throws SyntacticException {
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
        genExplicitaOVacio();
        herencia();
        match(P_BRCKT_OPEN);
        listaMiembros();
        match(P_BRCKT_CLOSE);
    }

    private void listaMiembros() {
        if (equalsAny(K_PUBLIC, K_PRIVATE, K_STRING, K_INT, K_BOOLEAN, K_CHAR, ID_CLS, K_STATIC, K_DYNAMIC)) {
            miembro();
            listaMiembros();
        } else {
            // Nothing
        }
    }

    private void miembro() {
        if (equalsAny(K_PUBLIC, K_PRIVATE, K_STRING, K_INT, K_BOOLEAN, K_CHAR)) {
            atributo();
        } else if (equalsAny(ID_CLS)) {
            match(ID_CLS);
            genYListaAtrsOCons();
        } else if (equalsAny(K_STATIC, K_DYNAMIC)) {
            metodo();
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (miembro) pero se encontro" + next.getType(), next);
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
        if (equalsAny(K_STRING, K_INT, K_BOOLEAN, K_CHAR, ID_CLS)) {
            tipo();
        } else if (equalsAny(K_VOID)) {
            match(K_VOID);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (tipoMetodo) pero se encontro" + next.getType(), next);
        }
    }

    private void formaMetodo() {
        if (equalsAny(K_STATIC)) {
            match(K_STATIC);
        } else if (equalsAny(K_DYNAMIC)) {
            match(K_DYNAMIC);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (formaMetodo) pero se encontro" + next.getType(), next);
        }
    }

    private void genYListaAtrsOCons() {
        if (equalsAny(OP_LT, ID_MV)) {
            genExplicitaOVacio();
            listaDecAtrs();
            match(P_SEMICOLON);
        } else if (equalsAny(P_PAREN_OPEN)) {
            constructor();
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (genYListaAtrsOCons) pero se encontro" + next.getType(), next);
        }
    }

    private void constructor() {
        argsFormales();
        bloque();
    }

    private void bloque() {
        match(P_BRCKT_OPEN);
        listaSentencias();
        match(P_BRCKT_CLOSE);
    }

    private void listaSentencias() {
        if (equalsAny(K_RETURN, ID_MV, K_STATIC, K_THIS, K_NEW, P_PAREN_OPEN, K_IF, K_STRING, K_INT, K_BOOLEAN, K_CHAR, ID_CLS, K_WHILE, P_SEMICOLON, P_BRCKT_OPEN)) {
            sentencia();
            listaSentencias();
        } else {
            // Nothing
        }
    }

    private void sentencia() {
        if (equalsAny(K_RETURN)) {
            match(K_RETURN);
            expresionOVacio();
            match(P_SEMICOLON);
        } else if (equalsAny(ID_MV, K_STATIC, K_THIS, K_NEW, P_PAREN_OPEN)) {
            acceso();
            asignacionOVacio();
            match(P_SEMICOLON);
        } else if (equalsAny(K_IF)) {
            match(K_IF);
            match(P_PAREN_OPEN);
            expresion();
            match(P_PAREN_CLOSE);
            sentencia();
            elseOVacio();
        } else if (equalsAny(K_STRING, K_INT, K_BOOLEAN, K_CHAR, ID_CLS)) {
            tipo();
            listaDecVars();
            match(P_SEMICOLON);
        } else if (equalsAny(K_WHILE)) {
            match(K_WHILE);
            match(P_PAREN_OPEN);
            expresion();
            match(P_PAREN_CLOSE);
            sentencia();
        } else if (equalsAny(P_SEMICOLON)) {
            match(P_SEMICOLON);
        } else if (equalsAny(P_BRCKT_OPEN)) {
            bloque();
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (sentencia) pero se encontro" + next.getType(), next);
        }
    }

    private void listaDecVars() {
        match(ID_MV);
        asignacionOVacio();
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

    private void elseOVacio() {
        if (equalsAny(K_ELSE)) {
            match(K_ELSE);
            sentencia();
        } else {
            // Nothing
        }
    }

    private void expresionOVacio() {
        if (equalsAny(OP_PLUS, OP_NOT, OP_MINUS, K_TRUE, STRING, INT, CHAR, K_NULL, K_FALSE, ID_MV, K_STATIC, K_THIS, K_NEW, P_PAREN_OPEN)) {
            expresion();
        } else {
            // Nothing
        }
    }

    private void argsFormales() {
        match(P_PAREN_OPEN);
        listaArgsFormalesOVacio();
        match(P_PAREN_CLOSE);
    }

    private void listaArgsFormalesOVacio() {
        if (equalsAny(K_STRING, K_INT, K_BOOLEAN, K_CHAR, ID_CLS)) {
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
        if (equalsAny(K_PUBLIC, K_PRIVATE)) {
            visibilidad();
            tipo();
            listaDecAtrs();
            match(P_SEMICOLON);
        } else if (equalsAny(K_STRING, K_INT, K_BOOLEAN, K_CHAR)) {
            tipoPrimitivo();
            listaDecAtrs();
            match(P_SEMICOLON);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (atributo) pero se encontro" + next.getType(), next);
        }
    }

    private void listaDecAtrs() {
        match(ID_MV);
        asignacionOVacio();
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

    private void asignacionOVacio() {
        if (equalsAny(ASSIGN, ASSIGN_PLUS, ASSIGN_MINUS)) {
            tipoDeAsignacion();
            expresion();
        } else {
            // Nothing
        }
    }

    private void expresion() {
        expresionUnaria();
        expresionResto();
    }

    private void expresionResto() {
        if (equalsAny(OP_DIV, OP_EQ, OP_MINUS, OP_LTE, OP_PLUS, OP_MULT, OP_LT, OP_OR, OP_MOD, OP_NOTEQ, OP_AND, OP_GT, OP_GTE)) {
            operadorBinario();
            expresionUnaria();
            expresionResto();
        } else {
            // Nothing
        }
    }

    private void operadorBinario() {
        if (equalsAny(OP_DIV)) {
            match(OP_DIV);
        } else if (equalsAny(OP_EQ)) {
            match(OP_EQ);
        } else if (equalsAny(OP_MINUS)) {
            match(OP_MINUS);
        } else if (equalsAny(OP_LTE)) {
            match(OP_LTE);
        } else if (equalsAny(OP_PLUS)) {
            match(OP_PLUS);
        } else if (equalsAny(OP_MULT)) {
            match(OP_MULT);
        } else if (equalsAny(OP_LT)) {
            match(OP_LT);
        } else if (equalsAny(OP_OR)) {
            match(OP_OR);
        } else if (equalsAny(OP_MOD)) {
            match(OP_MOD);
        } else if (equalsAny(OP_NOTEQ)) {
            match(OP_NOTEQ);
        } else if (equalsAny(OP_AND)) {
            match(OP_AND);
        } else if (equalsAny(OP_GT)) {
            match(OP_GT);
        } else if (equalsAny(OP_GTE)) {
            match(OP_GTE);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (operadorBinario) pero se encontro" + next.getType(), next);
        }
    }

    private void expresionUnaria() {
        if (equalsAny(OP_PLUS, OP_NOT, OP_MINUS)) {
            operadorUnario();
            operando();
        } else if (equalsAny(K_TRUE, STRING, INT, CHAR, K_NULL, K_FALSE, ID_MV, K_STATIC, K_THIS, K_NEW, P_PAREN_OPEN)) {
            operando();
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (expresionUnaria) pero se encontro" + next.getType(), next);
        }
    }

    private void operando() {
        if (equalsAny(K_TRUE, STRING, INT, CHAR, K_NULL, K_FALSE)) {
            literal();
        } else if (equalsAny(ID_MV, K_STATIC, K_THIS, K_NEW, P_PAREN_OPEN)) {
            acceso();
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (operando) pero se encontro" + next.getType(), next);
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
        if (equalsAny(ID_MV)) {
            accesoVarOMetodo();
        } else if (equalsAny(K_STATIC)) {
            accesoEstatico();
        } else if (equalsAny(K_THIS)) {
            accesoThis();
        } else if (equalsAny(K_NEW)) {
            accesoConstructor();
        } else if (equalsAny(P_PAREN_OPEN)) {
            match(P_PAREN_OPEN);
            expresion();
            match(P_PAREN_CLOSE);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (primario) pero se encontro" + next.getType(), next);
        }
    }

    private void accesoConstructor() {
        match(K_NEW);
        match(ID_CLS);
        genericidadOVacio();
        argsActuales();
    }

    private void genericidadOVacio() {
        if (equalsAny(OP_LT)) {
            match(OP_LT);
            restoGenericidad();
        } else {
            // Nothing
        }
    }

    private void restoGenericidad() {
        if (equalsAny(ID_CLS)) {
            genRestoExplicito();
        } else if (equalsAny(OP_GT)) {
            genRestoImplicito();
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (restoGenericidad) pero se encontro" + next.getType(), next);
        }
    }

    private void genRestoImplicito() {
        match(OP_GT);
    }

    private void genRestoExplicito() {
        match(ID_CLS);
        match(OP_GT);
    }

    private void accesoThis() {
        match(K_THIS);
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
        if (equalsAny(OP_PLUS, OP_NOT, OP_MINUS, K_TRUE, STRING, INT, CHAR, K_NULL, K_FALSE, ID_MV, K_STATIC, K_THIS, K_NEW, P_PAREN_OPEN)) {
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
        if (equalsAny(K_TRUE)) {
            match(K_TRUE);
        } else if (equalsAny(STRING)) {
            match(STRING);
        } else if (equalsAny(INT)) {
            match(INT);
        } else if (equalsAny(CHAR)) {
            match(CHAR);
        } else if (equalsAny(K_NULL)) {
            match(K_NULL);
        } else if (equalsAny(K_FALSE)) {
            match(K_FALSE);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (literal) pero se encontro" + next.getType(), next);
        }
    }

    private void operadorUnario() {
        if (equalsAny(OP_PLUS)) {
            match(OP_PLUS);
        } else if (equalsAny(OP_NOT)) {
            match(OP_NOT);
        } else if (equalsAny(OP_MINUS)) {
            match(OP_MINUS);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (operadorUnario) pero se encontro" + next.getType(), next);
        }
    }

    private void tipoDeAsignacion() {
        if (equalsAny(ASSIGN)) {
            match(ASSIGN);
        } else if (equalsAny(ASSIGN_PLUS)) {
            match(ASSIGN_PLUS);
        } else if (equalsAny(ASSIGN_MINUS)) {
            match(ASSIGN_MINUS);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (tipoDeAsignacion) pero se encontro" + next.getType(), next);
        }
    }

    private void tipo() {
        if (equalsAny(K_STRING, K_INT, K_BOOLEAN, K_CHAR)) {
            tipoPrimitivo();
        } else if (equalsAny(ID_CLS)) {
            match(ID_CLS);
            genExplicitaOVacio();
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (tipo) pero se encontro" + next.getType(), next);
        }
    }

    private void tipoPrimitivo() {
        if (equalsAny(K_STRING)) {
            match(K_STRING);
        } else if (equalsAny(K_INT)) {
            match(K_INT);
        } else if (equalsAny(K_BOOLEAN)) {
            match(K_BOOLEAN);
        } else if (equalsAny(K_CHAR)) {
            match(K_CHAR);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (tipoPrimitivo) pero se encontro" + next.getType(), next);
        }
    }

    private void visibilidad() {
        if (equalsAny(K_PUBLIC)) {
            match(K_PUBLIC);
        } else if (equalsAny(K_PRIVATE)) {
            match(K_PRIVATE);
        } else {
            Token next = sequence.next().orElse(null);
            throw new SyntacticException("Se esperaba (visibilidad) pero se encontro" + next.getType(), next);
        }
    }

    private void herencia() {
        if (equalsAny(K_EXTENDS)) {
            match(K_EXTENDS);
            match(ID_CLS);
            genExplicitaOVacio();
        } else {
            // Nothing
        }
    }

    private void genExplicitaOVacio() {
        if (equalsAny(OP_LT)) {
            match(OP_LT);
            match(ID_CLS);
            match(OP_GT);
        } else {
            // Nothing
        }
    }


}
