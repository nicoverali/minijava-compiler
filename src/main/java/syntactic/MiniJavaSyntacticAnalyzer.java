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
            throw new SyntacticException("Se esperaba " + type + " pero se encontro " + next.getType(), next);
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
        listaClasesInterfaces();
    }

    private void listaClasesInterfaces() {
        if (equalsAny(K_INTERFACE)) {
            interfaz();
            otrasClasesInterfaces();
        } else if (equalsAny(K_CLASS)) {
            clase();
            otrasClasesInterfaces();
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (listaClasesInterfaces) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void otrasClasesInterfaces() {
        if (equalsAny(K_INTERFACE, K_CLASS)) {
            listaClasesInterfaces();
        }
    }

    private void interfaz() {
        match(K_INTERFACE);
        match(ID_CLS);
        genExplicitaOVacio();
        herenciaInterfaz();
        match(P_BRCKT_OPEN);
        listaMiembrosInterfaz();
        match(P_BRCKT_CLOSE);
    }

    private void herenciaInterfaz() {
        if (equalsAny(K_EXTENDS)) {
            match(K_EXTENDS);
            match(ID_CLS);
            genExplicitaOVacio();
            otrosHerenciaInterfaz();
        }
    }

    private void otrosHerenciaInterfaz() {
        if (equalsAny(P_COMMA)) {
            match(P_COMMA);
            match(ID_CLS);
            genExplicitaOVacio();
            otrosHerenciaInterfaz();
        }
    }

    private void listaMiembrosInterfaz() {
        if (equalsAny(K_DYNAMIC, K_STATIC)) {
            metodoInterfaz();
            listaMiembrosInterfaz();
        }
    }

    private void metodoInterfaz() {
        formaMetodo();
        tipoMetodo();
        match(ID_MV);
        argsFormales();
        match(P_SEMICOLON);
    }

    private void clase() {
        match(K_CLASS);
        match(ID_CLS);
        genExplicitaOVacio();
        herenciaClase();
        match(P_BRCKT_OPEN);
        listaMiembrosClase();
        match(P_BRCKT_CLOSE);
    }

    private void herenciaClase() {
        if (equalsAny(K_EXTENDS, K_IMPLEMENTS)) {
            extendsOVacio();
            implementsOVacio();
        }
    }

    private void extendsOVacio() {
        if (equalsAny(K_EXTENDS)) {
            match(K_EXTENDS);
            match(ID_CLS);
            genExplicitaOVacio();
        }
    }

    private void implementsOVacio() {
        if (equalsAny(K_IMPLEMENTS)) {
            match(K_IMPLEMENTS);
            match(ID_CLS);
            genExplicitaOVacio();
            otrosImplements();
        }
    }

    private void otrosImplements() {
        if (equalsAny(P_COMMA)) {
            match(P_COMMA);
            match(ID_CLS);
            genExplicitaOVacio();
            otrosImplements();
        }
    }

    private void listaMiembrosClase() {
        if (equalsAny(K_DYNAMIC, K_STATIC, K_CHAR, K_INT, K_BOOLEAN, K_STRING, K_PUBLIC, K_PRIVATE, ID_CLS)) {
            miembroClase();
            listaMiembrosClase();
        }
    }

    private void miembroClase() {
        if (equalsAny(K_DYNAMIC)) {
            match(K_DYNAMIC);
            metodo();
        } else if (equalsAny(K_STATIC)) {
            match(K_STATIC);
            atributoOMetodo();
        } else if (equalsAny(K_CHAR, K_INT, K_BOOLEAN, K_STRING)) {
            tipoPrimitivo();
            listaDecAtrs();
            match(P_SEMICOLON);
        } else if (equalsAny(K_PUBLIC, K_PRIVATE)) {
            visibilidad();
            staticOVacio();
            atributo();
        } else if (equalsAny(ID_CLS)) {
            match(ID_CLS);
            atributoOConstructor();
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (miembroClase) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void metodo() {
        tipoMetodo();
        match(ID_MV);
        argsFormales();
        bloque();
    }

    private void atributoOConstructor() {
        if (equalsAny(OP_LT, ID_MV)) {
            genExplicitaOVacio();
            listaDecAtrs();
            match(P_SEMICOLON);
        } else if (equalsAny(P_PAREN_OPEN)) {
            constructor();
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (atributoOConstructor) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void constructor() {
        argsFormales();
        bloque();
    }

    private void atributo() {
        tipo();
        listaDecAtrs();
        match(P_SEMICOLON);
    }

    private void staticOVacio() {
        if (equalsAny(K_STATIC)) {
            match(K_STATIC);
        }
    }

    private void visibilidad() {
        if (equalsAny(K_PUBLIC)) {
            match(K_PUBLIC);
        } else if (equalsAny(K_PRIVATE)) {
            match(K_PRIVATE);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (visibilidad) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void formaMetodo() {
        if (equalsAny(K_DYNAMIC)) {
            match(K_DYNAMIC);
        } else if (equalsAny(K_STATIC)) {
            match(K_STATIC);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (formaMetodo) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void atributoOMetodo() {
        if (equalsAny(K_CHAR, K_INT, K_BOOLEAN, K_STRING, ID_CLS)) {
            tipo();
            match(ID_MV);
            restoDecAtrsORestoMetodo();
        } else if (equalsAny(K_VOID)) {
            match(K_VOID);
            match(ID_MV);
            restoMetodo();
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (atributoOMetodo) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void restoDecAtrsORestoMetodo() {
        if (equalsAny(ASSIGN_PLUS, ASSIGN, ASSIGN_MINUS, P_COMMA, P_SEMICOLON)) {
            restoDecAtrs();
        } else if (equalsAny(P_PAREN_OPEN)) {
            restoMetodo();
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (restoDecAtrsORestoMetodo) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void restoMetodo() {
        argsFormales();
        bloque();
    }

    private void restoDecAtrs() {
        asignacionOVacio();
        otrosDecAtrs();
        match(P_SEMICOLON);
    }

    private void otrosDecAtrs() {
        if (equalsAny(P_COMMA)) {
            match(P_COMMA);
            listaDecAtrs();
        }
    }

    private void listaDecAtrs() {
        match(ID_MV);
        asignacionOVacio();
        otrosDecAtrs();
    }

    private void tipo() {
        if (equalsAny(K_CHAR, K_INT, K_BOOLEAN, K_STRING)) {
            tipoPrimitivo();
        } else if (equalsAny(ID_CLS)) {
            tipoClaseGen();
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (tipo) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void tipoMetodo() {
        if (equalsAny(K_VOID)) {
            match(K_VOID);
        } else if (equalsAny(K_CHAR, K_INT, K_BOOLEAN, K_STRING, ID_CLS)) {
            tipo();
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (tipoMetodo) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void tipoClaseGen() {
        match(ID_CLS);
        genExplicitaOVacio();
    }

    private void tipoPrimitivo() {
        if (equalsAny(K_CHAR)) {
            match(K_CHAR);
        } else if (equalsAny(K_INT)) {
            match(K_INT);
        } else if (equalsAny(K_BOOLEAN)) {
            match(K_BOOLEAN);
        } else if (equalsAny(K_STRING)) {
            match(K_STRING);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (tipoPrimitivo) pero se encontro " + token.getType(), token);
            });
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
        }
    }

    private void listaArgsFormalesOVacio() {
        if (equalsAny(K_CHAR, K_INT, K_BOOLEAN, K_STRING, ID_CLS)) {
            listaArgsFormales();
        }
    }

    private void argsFormales() {
        match(P_PAREN_OPEN);
        listaArgsFormalesOVacio();
        match(P_PAREN_CLOSE);
    }

    private void bloque() {
        match(P_BRCKT_OPEN);
        listaSentencias();
        match(P_BRCKT_CLOSE);
    }

    private void listaSentencias() {
        if (equalsAny(P_BRCKT_OPEN, K_RETURN, P_SEMICOLON, ID_CLS, K_CHAR, K_INT, K_BOOLEAN, K_STRING, K_IF, K_WHILE, ID_MV, K_STATIC, K_NEW, K_THIS, P_PAREN_OPEN)) {
            sentencia();
            listaSentencias();
        }
    }

    private void sentencia() {
        if (equalsAny(P_BRCKT_OPEN)) {
            bloque();
        } else if (equalsAny(K_RETURN)) {
            match(K_RETURN);
            expresionOVacio();
            match(P_SEMICOLON);
        } else if (equalsAny(P_SEMICOLON)) {
            match(P_SEMICOLON);
        } else if (equalsAny(ID_CLS)) {
            match(ID_CLS);
            accesoEstaticoOVarClase();
        } else if (equalsAny(K_CHAR, K_INT, K_BOOLEAN, K_STRING)) {
            tipoPrimitivo();
            listaDecVars();
            match(P_SEMICOLON);
        } else if (equalsAny(K_IF)) {
            match(K_IF);
            match(P_PAREN_OPEN);
            expresion();
            match(P_PAREN_CLOSE);
            sentencia();
            elseOVacio();
        } else if (equalsAny(K_WHILE)) {
            match(K_WHILE);
            match(P_PAREN_OPEN);
            expresion();
            match(P_PAREN_CLOSE);
            sentencia();
        } else if (equalsAny(ID_MV, K_STATIC, K_NEW, K_THIS, P_PAREN_OPEN)) {
            acceso();
            asignacionOVacio();
            match(P_SEMICOLON);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (sentencia) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void elseOVacio() {
        if (equalsAny(K_ELSE)) {
            match(K_ELSE);
            sentencia();
        }
    }

    private void accesoEstaticoOVarClase() {
        if (equalsAny(P_DOT)) {
            match(P_DOT);
            accesoVarOMetodo();
            encadenado();
            asignacionOVacio();
            match(P_SEMICOLON);
        } else if (equalsAny(OP_LT, ID_MV)) {
            genExplicitaOVacio();
            listaDecVars();
            match(P_SEMICOLON);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (accesoEstaticoOVarClase) pero se encontro " + token.getType(), token);
            });
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
        }
    }

    private void asignacionOVacio() {
        if (equalsAny(ASSIGN_PLUS, ASSIGN, ASSIGN_MINUS)) {
            tipoDeAsignacion();
            expresion();
        }
    }

    private void tipoDeAsignacion() {
        if (equalsAny(ASSIGN_PLUS)) {
            match(ASSIGN_PLUS);
        } else if (equalsAny(ASSIGN)) {
            match(ASSIGN);
        } else if (equalsAny(ASSIGN_MINUS)) {
            match(ASSIGN_MINUS);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (tipoDeAsignacion) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void expresionOVacio() {
        if (equalsAny(OP_PLUS, OP_MINUS, OP_NOT, ID_MV, K_STATIC, K_NEW, K_THIS, P_PAREN_OPEN, K_FALSE, K_NULL, K_TRUE, INT, CHAR, STRING)) {
            expresion();
        }
    }

    private void expresion() {
        expNivel1();
    }

    private void operando() {
        if (equalsAny(ID_MV, K_STATIC, K_NEW, K_THIS, P_PAREN_OPEN)) {
            acceso();
        } else if (equalsAny(K_FALSE, K_NULL, K_TRUE, INT, CHAR, STRING)) {
            literal();
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (operando) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void literal() {
        if (equalsAny(K_FALSE)) {
            match(K_FALSE);
        } else if (equalsAny(K_NULL)) {
            match(K_NULL);
        } else if (equalsAny(K_TRUE)) {
            match(K_TRUE);
        } else if (equalsAny(INT)) {
            match(INT);
        } else if (equalsAny(CHAR)) {
            match(CHAR);
        } else if (equalsAny(STRING)) {
            match(STRING);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (literal) pero se encontro " + token.getType(), token);
            });
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
        } else if (equalsAny(K_NEW)) {
            accesoConstructor();
        } else if (equalsAny(K_THIS)) {
            accesoThis();
        } else if (equalsAny(P_PAREN_OPEN)) {
            match(P_PAREN_OPEN);
            expresion();
            match(P_PAREN_CLOSE);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (primario) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void accesoThis() {
        match(K_THIS);
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
        }
    }

    private void restoGenericidad() {
        if (equalsAny(ID_CLS)) {
            genRestoExplicito();
        } else if (equalsAny(OP_GT)) {
            genRestoImplicito();
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (restoGenericidad) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void genRestoImplicito() {
        match(OP_GT);
    }

    private void genRestoExplicito() {
        match(ID_CLS);
        match(OP_GT);
    }

    private void genExplicitaOVacio() {
        if (equalsAny(OP_LT)) {
            match(OP_LT);
            match(ID_CLS);
            match(OP_GT);
        }
    }

    private void accesoEstatico() {
        match(K_STATIC);
        match(ID_CLS);
        match(P_DOT);
        accesoVarOMetodo();
    }

    private void accesoVarOMetodo() {
        match(ID_MV);
        argsActualesOVacio();
    }

    private void argsActualesOVacio() {
        if (equalsAny(P_PAREN_OPEN)) {
            argsActuales();
        }
    }

    private void argsActuales() {
        match(P_PAREN_OPEN);
        listaExpsOVacio();
        match(P_PAREN_CLOSE);
    }

    private void listaExpsOVacio() {
        if (equalsAny(OP_PLUS, OP_MINUS, OP_NOT, ID_MV, K_STATIC, K_NEW, K_THIS, P_PAREN_OPEN, K_FALSE, K_NULL, K_TRUE, INT, CHAR, STRING)) {
            listaExps();
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
        }
    }

    private void argFormal() {
        tipo();
        match(ID_MV);
    }

    private void expNivel1() {
        expNivel2();
        expNivel1Resto();
    }

    private void expNivel1Resto() {
        if (equalsAny(OP_EQ, OP_NOTEQ)) {
            opNivel1();
            expNivel2();
            expNivel1Resto();
        }
    }

    private void opNivel1() {
        if (equalsAny(OP_EQ)) {
            match(OP_EQ);
        } else if (equalsAny(OP_NOTEQ)) {
            match(OP_NOTEQ);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (opNivel1) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void expNivel2() {
        expNivel3();
        expNivel2Resto();
    }

    private void expNivel2Resto() {
        if (equalsAny(OP_AND)) {
            opNivel2();
            expNivel3();
            expNivel2Resto();
        }
    }

    private void opNivel2() {
        match(OP_AND);
    }

    private void expNivel3() {
        expNivel4();
        expNivel3Resto();
    }

    private void expNivel3Resto() {
        if (equalsAny(OP_OR)) {
            opNivel3();
            expNivel4();
            expNivel3Resto();
        }
    }

    private void opNivel3() {
        match(OP_OR);
    }

    private void expNivel4() {
        expNivel5();
        expNivel4Resto();
    }

    private void expNivel4Resto() {
        if (equalsAny(OP_GTE, OP_LT, OP_GT, OP_LTE)) {
            opNivel4();
            expNivel5();
            expNivel4Resto();
        }
    }

    private void opNivel4() {
        if (equalsAny(OP_GTE)) {
            match(OP_GTE);
        } else if (equalsAny(OP_LT)) {
            match(OP_LT);
        } else if (equalsAny(OP_GT)) {
            match(OP_GT);
        } else if (equalsAny(OP_LTE)) {
            match(OP_LTE);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (opNivel4) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void expNivel5() {
        expNivel6();
        expNivel5Resto();
    }

    private void expNivel5Resto() {
        if (equalsAny(OP_MINUS, OP_PLUS)) {
            opNivel5();
            expNivel6();
            expNivel5Resto();
        }
    }

    private void opNivel5() {
        if (equalsAny(OP_MINUS)) {
            match(OP_MINUS);
        } else if (equalsAny(OP_PLUS)) {
            match(OP_PLUS);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (opNivel5) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void expNivel6() {
        expresionUnaria();
        expNivel6Resto();
    }

    private void expNivel6Resto() {
        if (equalsAny(OP_MULT, OP_DIV, OP_MOD)) {
            opNivel6();
            expresionUnaria();
            expNivel6Resto();
        }
    }

    private void opNivel6() {
        if (equalsAny(OP_MULT)) {
            match(OP_MULT);
        } else if (equalsAny(OP_DIV)) {
            match(OP_DIV);
        } else if (equalsAny(OP_MOD)) {
            match(OP_MOD);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (opNivel6) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void expresionUnaria() {
        if (equalsAny(OP_PLUS, OP_MINUS, OP_NOT)) {
            operadorUnario();
            operando();
        } else if (equalsAny(ID_MV, K_STATIC, K_NEW, K_THIS, P_PAREN_OPEN, K_FALSE, K_NULL, K_TRUE, INT, CHAR, STRING)) {
            operando();
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (expresionUnaria) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void operadorUnario() {
        if (equalsAny(OP_PLUS)) {
            match(OP_PLUS);
        } else if (equalsAny(OP_MINUS)) {
            match(OP_MINUS);
        } else if (equalsAny(OP_NOT)) {
            match(OP_NOT);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (operadorUnario) pero se encontro " + token.getType(), token);
            });
        }
    }
}
