package syntactic;

import lexical.LexicalSequence;
import lexical.Token;
import lexical.TokenType;

import java.util.Arrays;
import java.util.Optional;

import static lexical.TokenType.*;

public class MiniJavaSyntacticAnalyzer implements SyntacticAnalyzer {

    private final LexicalSequence sequence;

    public MiniJavaSyntacticAnalyzer(LexicalSequence sequence) {
        this.sequence = sequence;
    }

	@Override
	public void analyze() throws SyntacticException {
		Token next = sequence.peek().get();
		boolean isEof = next.getType() == EOF;
		if (!isEof){
			inicial();
		} else {
			throw new SyntacticException("El archivo se encuentra vacío", next);
		}
	}

	/**
	 * Tries to match the next token of the {@link LexicalSequence} with the given {@link TokenType}. It then
	 * returns the matched token. If both tokens cannot be matched, then an exception will be thrown.
	 *
	 * @return the next {@link Token} of the {@link LexicalSequence} that match the given {@link TokenType}
	 */
    private Token match(TokenType type) {
        Token next = sequence.next().orElseThrow(IllegalStateException::new);
        if (next.getType() == EOF) {
            throw new SyntacticException("Se esperaba (" + type + ") pero se llego al final del archivo", next);
        } else if (next.getType() != type) {
            throw new SyntacticException("Se esperaba (" + type + ") pero se encontro " + next.getType(), next);
        }
        return next;
    }

	/**
	 * Checks if the next token in the {@link LexicalSequence} has one of the {@link TokenType} provided
	 * as argument.
	 *
	 * @return true if the next token {@link TokenType} appears in the given list of types, false otherwise
	 */
    private boolean equalsAny(TokenType... types) {
        Optional<TokenType> nextType = sequence.peek().map(Token::getType);
        return nextType
                .filter(type -> Arrays.asList(types).contains(type))
                .isPresent();
    }

    ///Start
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
		} else if (equalsAny(EOF)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {class} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
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
		if (equalsAny(K_STATIC, K_STRING, K_BOOLEAN, K_CHAR, K_INT, K_DYNAMIC, ID_CLS, K_PRIVATE, K_PUBLIC)) {
			miembro();
			listaMiembros();
		} else if (equalsAny(P_BRCKT_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {private, static, boolean, public, char, dynamic, idClase, String, }, int} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void miembro() {
		if (equalsAny(K_STATIC)) {
			match(K_STATIC);
			atributoOMetodo();
		} else if (equalsAny(K_STRING, K_BOOLEAN, K_CHAR, K_INT)) {
			tipoPrimitivo();
			listaDecAtrs();
			match(P_SEMICOLON);
		} else if (equalsAny(K_DYNAMIC)) {
			match(K_DYNAMIC);
			metodo();
		} else if (equalsAny(ID_CLS)) {
			match(ID_CLS);
			atributoOConstructor();
		} else if (equalsAny(K_PRIVATE, K_PUBLIC)) {
			visibilidad();
			staticOVacio();
			atributo();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (miembro) pero se encontro "+token.getType(), token);});
		}
	}

	private void atributo() {
		tipo();
		listaDecAtrs();
		match(P_SEMICOLON);
	}

	private void staticOVacio() {
		if (equalsAny(K_STATIC)) {
			match(K_STATIC);
		} else if (equalsAny(K_STRING, K_INT, K_BOOLEAN, ID_CLS, K_CHAR)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {static, boolean, char, idClase, String, int} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void visibilidad() {
		if (equalsAny(K_PRIVATE)) {
			match(K_PRIVATE);
		} else if (equalsAny(K_PUBLIC)) {
			match(K_PUBLIC);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (visibilidad) pero se encontro "+token.getType(), token);});
		}
	}

	private void atributoOConstructor() {
		if (equalsAny(OP_LT, ID_MV)) {
			genExplicitaOVacio();
			listaDecAtrs();
			match(P_SEMICOLON);
		} else if (equalsAny(P_PAREN_OPEN)) {
			restoConstructor();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (atributoOConstructor) pero se encontro "+token.getType(), token);});
		}
	}

	private void restoConstructor() {
		argsFormales();
		bloque();
	}

	private void metodo() {
		tipoMetodo();
		match(ID_MV);
		argsFormales();
		bloque();
	}

	private void tipoMetodo() {
		if (equalsAny(K_STRING, K_BOOLEAN, K_CHAR, K_INT, ID_CLS)) {
			tipo();
		} else if (equalsAny(K_VOID)) {
			match(K_VOID);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (tipoMetodo) pero se encontro "+token.getType(), token);});
		}
	}

	private void atributoOMetodo() {
		if (equalsAny(K_STRING, K_BOOLEAN, K_CHAR, K_INT, ID_CLS)) {
			tipo();
			match(ID_MV);
			otrosDecAtrsORestoMetodo();
		} else if (equalsAny(K_VOID)) {
			match(K_VOID);
			match(ID_MV);
			restoMetodo();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (atributoOMetodo) pero se encontro "+token.getType(), token);});
		}
	}

	private void otrosDecAtrsORestoMetodo() {
		if (equalsAny(P_PAREN_OPEN)) {
			restoMetodo();
		} else if (equalsAny(P_COMMA, P_SEMICOLON)) {
			otrosDecAtrs();
			match(P_SEMICOLON);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (otrosDecAtrsORestoMetodo) pero se encontro "+token.getType(), token);});
		}
	}

	private void otrosDecAtrs() {
		if (equalsAny(P_COMMA)) {
			match(P_COMMA);
			listaDecAtrs();
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {;, ,} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void listaDecAtrs() {
		match(ID_MV);
		otrosDecAtrs();
	}

	private void restoMetodo() {
		argsFormales();
		bloque();
	}

	private void bloque() {
		match(P_BRCKT_OPEN);
		listaSentencias();
		match(P_BRCKT_CLOSE);
	}

	private void listaSentencias() {
		if (equalsAny(ID_CLS, K_STRING, K_BOOLEAN, K_CHAR, K_INT, P_SEMICOLON, ID_MV, K_NEW, K_THIS, P_PAREN_OPEN, K_FOR, P_BRCKT_OPEN, K_RETURN, K_IF)) {
			sentencia();
			listaSentencias();
		} else if (equalsAny(P_BRCKT_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {new, this, (, for, String, int, boolean, idMetVar, char, idClase, ;, {, if, }, return} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void sentencia() {
		if (equalsAny(ID_CLS)) {
			match(ID_CLS);
			restoAccesoEstaticoOVarLocalClase();
			match(P_SEMICOLON);
		} else if (equalsAny(K_STRING, K_BOOLEAN, K_CHAR, K_INT)) {
			varLocalPrimitiva();
			match(P_SEMICOLON);
		} else if (equalsAny(P_SEMICOLON)) {
			match(P_SEMICOLON);
		} else if (equalsAny(ID_MV, K_NEW, K_THIS, P_PAREN_OPEN)) {
			asignacionOLlamadaPrimitiva();
			match(P_SEMICOLON);
		} else if (equalsAny(K_FOR)) {
			sentenciaFor();
		} else if (equalsAny(P_BRCKT_OPEN)) {
			bloque();
		} else if (equalsAny(K_RETURN)) {
			sentenciaReturn();
			match(P_SEMICOLON);
		} else if (equalsAny(K_IF)) {
			sentenciaIf();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (sentencia) pero se encontro "+token.getType(), token);});
		}
	}

	private void sentenciaIf() {
		match(K_IF);
		match(P_PAREN_OPEN);
		expresion();
		match(P_PAREN_CLOSE);
		sentencia();
		elseOVacio();
	}

	private void elseOVacio() {
		if (equalsAny(K_ELSE)) {
			match(K_ELSE);
			sentencia();
		} else if (equalsAny(K_NEW, P_SEMICOLON, ID_MV, P_BRCKT_OPEN, K_RETURN, K_THIS, P_BRCKT_CLOSE, K_IF, P_PAREN_OPEN, K_STRING, K_INT, K_BOOLEAN, K_FOR, ID_CLS, K_CHAR)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {new, this, (, for, String, int, idMetVar, boolean, else, char, idClase, ;, {, }, if, return} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void sentenciaReturn() {
		match(K_RETURN);
		expresionOVacio();
	}

	private void expresionOVacio() {
		if (equalsAny(ID_CLS, K_FALSE, STRING, K_NULL, INT, K_TRUE, CHAR, ID_MV, K_NEW, K_THIS, P_PAREN_OPEN, OP_MINUS, OP_PLUS, OP_NOT)) {
			expresion();
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {new, !, false, intLiteral, this, (, charLiteral, +, -, idMetVar, null, stringLiteral, true, idClase, ;} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void sentenciaFor() {
		match(K_FOR);
		match(P_PAREN_OPEN);
		tipo();
		match(ID_MV);
		restoFor();
	}

	private void restoFor() {
		if (equalsAny(P_COLON)) {
			restoForEach();
		} else if (equalsAny(ASSIGN, P_SEMICOLON)) {
			restoForClasico();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (restoFor) pero se encontro "+token.getType(), token);});
		}
	}

	private void restoForClasico() {
		varLocalAsignacionOVacio();
		match(P_SEMICOLON);
		expresion();
		match(P_SEMICOLON);
		asignacion();
		match(P_PAREN_CLOSE);
		sentencia();
	}

	private void asignacion() {
		acceso();
		tipoDeAsignacion();
	}

	private void restoForEach() {
		match(P_COLON);
		expresion();
		match(P_PAREN_CLOSE);
		sentencia();
	}

	private void asignacionOLlamadaPrimitiva() {
		acceso();
		restoAsignacionOVacio();
	}

	private void varLocalPrimitiva() {
		tipoPrimitivo();
		match(ID_MV);
		varLocalAsignacionOVacio();
	}

	private void restoAccesoEstaticoOVarLocalClase() {
		if (equalsAny(P_DOT)) {
			restoAccesoEstatico();
			restoAsignacionOVacio();
		} else if (equalsAny(OP_LT, ID_MV)) {
			genExplicitaOVacio();
			match(ID_MV);
			varLocalAsignacionOVacio();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (restoAccesoEstaticoOVarLocalClase) pero se encontro "+token.getType(), token);});
		}
	}

	private void varLocalAsignacionOVacio() {
		if (equalsAny(ASSIGN)) {
			match(ASSIGN);
			expresion();
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {;, =} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void restoAsignacionOVacio() {
		if (equalsAny(ASSIGN, OP_MINUS, ASSIGN_INCR)) {
			tipoDeAsignacion();
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {++, ;, =, -} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void tipoDeAsignacion() {
		if (equalsAny(ASSIGN)) {
			match(ASSIGN);
			expresion();
		} else if (equalsAny(OP_MINUS)) {
			match(OP_MINUS);
			match(OP_MINUS);
		} else if (equalsAny(ASSIGN_INCR)) {
			match(ASSIGN_INCR);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (tipoDeAsignacion) pero se encontro "+token.getType(), token);});
		}
	}

	private void restoAccesoEstatico() {
		match(P_DOT);
		accesoVarOMetodo();
		encadenado();
	}

	private void accesoVarOMetodo() {
		match(ID_MV);
		argsActualesOVacio();
	}

	private void argsActualesOVacio() {
		if (equalsAny(P_PAREN_OPEN)) {
			argsActuales();
		} else if (equalsAny(OP_MULT, P_SEMICOLON, P_COMMA, OP_MOD, OP_PLUS, P_DOT, ASSIGN, OP_DIV, OP_GT, OP_EQ, OP_GTE, OP_MINUS, OP_LT, P_PAREN_CLOSE, OP_AND, ASSIGN_INCR, OP_LTE, OP_NOTEQ, OP_OR)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {==, &&, ++, ||, <=, %, (, ), *, +, ,, -, ., /, ;, <, !=, =, >, >=} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void argsActuales() {
		match(P_PAREN_OPEN);
		listaExpsOVacio();
		match(P_PAREN_CLOSE);
	}

	private void listaExpsOVacio() {
		if (equalsAny(ID_CLS, K_FALSE, STRING, K_NULL, INT, K_TRUE, CHAR, ID_MV, K_NEW, K_THIS, P_PAREN_OPEN, OP_MINUS, OP_PLUS, OP_NOT)) {
			listaExps();
		} else if (equalsAny(P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {new, !, false, intLiteral, this, (, ), charLiteral, +, -, idMetVar, null, stringLiteral, true, idClase} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
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
		} else if (equalsAny(P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {), ,} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void expresion() {
		expresionUnaria();
		expresionResto();
	}

	private void expresionUnaria() {
		if (equalsAny(ID_CLS)) {
			accesoEstatico();
		} else if (equalsAny(K_FALSE, STRING, K_NULL, INT, K_TRUE, CHAR, ID_MV, K_NEW, K_THIS, P_PAREN_OPEN, OP_MINUS, OP_PLUS, OP_NOT)) {
			expresionUnariaSinStatic();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (expresionUnaria) pero se encontro "+token.getType(), token);});
		}
	}

	private void expresionUnariaSinStatic() {
		if (equalsAny(K_FALSE, STRING, K_NULL, INT, K_TRUE, CHAR, ID_MV, K_NEW, K_THIS, P_PAREN_OPEN)) {
			operando();
		} else if (equalsAny(OP_MINUS, OP_PLUS, OP_NOT)) {
			operadorUnario();
			operando();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (expresionUnariaSinStatic) pero se encontro "+token.getType(), token);});
		}
	}

	private void operadorUnario() {
		if (equalsAny(OP_MINUS)) {
			match(OP_MINUS);
		} else if (equalsAny(OP_PLUS)) {
			match(OP_PLUS);
		} else if (equalsAny(OP_NOT)) {
			match(OP_NOT);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (operadorUnario) pero se encontro "+token.getType(), token);});
		}
	}

	private void operando() {
		if (equalsAny(K_FALSE, STRING, K_NULL, INT, K_TRUE, CHAR)) {
			literal();
		} else if (equalsAny(ID_MV, K_NEW, K_THIS, P_PAREN_OPEN)) {
			acceso();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (operando) pero se encontro "+token.getType(), token);});
		}
	}

	private void acceso() {
		if (equalsAny(ID_MV, K_NEW, K_THIS)) {
			primario();
			encadenado();
		} else if (equalsAny(P_PAREN_OPEN)) {
			match(P_PAREN_OPEN);
			expParenOCasting();
			encadenado();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (acceso) pero se encontro "+token.getType(), token);});
		}
	}

	private void expParenOCasting() {
		if (equalsAny(K_FALSE, STRING, K_NULL, INT, K_TRUE, CHAR, ID_MV, K_NEW, K_THIS, P_PAREN_OPEN, OP_MINUS, OP_PLUS, OP_NOT)) {
			expresionUnariaSinStatic();
			expresionResto();
			match(P_PAREN_CLOSE);
		} else if (equalsAny(ID_CLS)) {
			match(ID_CLS);
			restoCastingORestoExpresion();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (expParenOCasting) pero se encontro "+token.getType(), token);});
		}
	}

	private void restoCastingORestoExpresion() {
		if (equalsAny(OP_LT, P_PAREN_CLOSE)) {
			restoCasting();
			accesoEstaticoOPrimarioOExpParen();
		} else if (equalsAny(P_DOT)) {
			restoAccesoEstatico();
			expresionResto();
			match(P_PAREN_CLOSE);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (restoCastingORestoExpresion) pero se encontro "+token.getType(), token);});
		}
	}

	private void accesoEstaticoOPrimarioOExpParen() {
		if (equalsAny(P_PAREN_OPEN)) {
			expresionParentizada();
		} else if (equalsAny(ID_MV, K_NEW, K_THIS)) {
			primario();
		} else if (equalsAny(ID_CLS)) {
			accesoEstatico();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (accesoEstaticoOPrimarioOExpParen) pero se encontro "+token.getType(), token);});
		}
	}

	private void expresionParentizada() {
		match(P_PAREN_OPEN);
		expresion();
		match(P_PAREN_CLOSE);
	}

	private void restoCasting() {
		genExplicitaOVacio();
		match(P_PAREN_CLOSE);
	}

	private void expresionResto() {
		if (equalsAny(OP_GT, OP_MOD, OP_MINUS, OP_OR, OP_LTE, OP_GTE, OP_EQ, OP_NOTEQ, OP_MULT, OP_AND, OP_PLUS, OP_LT, OP_DIV)) {
			operadorBinario();
			expresionUnaria();
			expresionResto();
		} else if (equalsAny(P_SEMICOLON, P_COMMA, P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {||, ==, &&, <=, %, ), *, +, ,, -, /, ;, !=, <, >, >=} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void operadorBinario() {
		if (equalsAny(OP_GT)) {
			match(OP_GT);
		} else if (equalsAny(OP_MOD)) {
			match(OP_MOD);
		} else if (equalsAny(OP_MINUS)) {
			match(OP_MINUS);
		} else if (equalsAny(OP_OR)) {
			match(OP_OR);
		} else if (equalsAny(OP_LTE)) {
			match(OP_LTE);
		} else if (equalsAny(OP_GTE)) {
			match(OP_GTE);
		} else if (equalsAny(OP_EQ)) {
			match(OP_EQ);
		} else if (equalsAny(OP_NOTEQ)) {
			match(OP_NOTEQ);
		} else if (equalsAny(OP_MULT)) {
			match(OP_MULT);
		} else if (equalsAny(OP_AND)) {
			match(OP_AND);
		} else if (equalsAny(OP_PLUS)) {
			match(OP_PLUS);
		} else if (equalsAny(OP_LT)) {
			match(OP_LT);
		} else if (equalsAny(OP_DIV)) {
			match(OP_DIV);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (operadorBinario) pero se encontro "+token.getType(), token);});
		}
	}

	private void primario() {
		if (equalsAny(ID_MV)) {
			accesoVarOMetodo();
		} else if (equalsAny(K_NEW)) {
			accesoConstructor();
		} else if (equalsAny(K_THIS)) {
			accesoThis();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (primario) pero se encontro "+token.getType(), token);});
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
		} else if (equalsAny(P_PAREN_OPEN)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {(, <} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void restoGenericidad() {
		if (equalsAny(OP_GT)) {
			genRestoImplicito();
		} else if (equalsAny(ID_CLS)) {
			genRestoExplicito();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (restoGenericidad) pero se encontro "+token.getType(), token);});
		}
	}

	private void genRestoExplicito() {
		match(ID_CLS);
		match(OP_GT);
	}

	private void genRestoImplicito() {
		match(OP_GT);
	}

	private void literal() {
		if (equalsAny(K_FALSE)) {
			match(K_FALSE);
		} else if (equalsAny(STRING)) {
			match(STRING);
		} else if (equalsAny(K_NULL)) {
			match(K_NULL);
		} else if (equalsAny(INT)) {
			match(INT);
		} else if (equalsAny(K_TRUE)) {
			match(K_TRUE);
		} else if (equalsAny(CHAR)) {
			match(CHAR);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (literal) pero se encontro "+token.getType(), token);});
		}
	}

	private void accesoEstatico() {
		match(ID_CLS);
		match(P_DOT);
		accesoVarOMetodo();
		encadenado();
	}

	private void encadenado() {
		if (equalsAny(P_DOT)) {
			varOMetodoEncadenado();
			encadenado();
		} else if (equalsAny(OP_MULT, P_SEMICOLON, P_COMMA, OP_MOD, OP_PLUS, ASSIGN, OP_DIV, OP_GT, OP_EQ, OP_GTE, OP_MINUS, OP_LT, P_PAREN_CLOSE, OP_AND, ASSIGN_INCR, OP_LTE, OP_NOTEQ, OP_OR)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {==, &&, ++, ||, <=, %, ), *, +, ,, -, ., /, ;, <, !=, =, >, >=} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void varOMetodoEncadenado() {
		match(P_DOT);
		match(ID_MV);
		argsActualesOVacio();
	}

	private void argsFormales() {
		match(P_PAREN_OPEN);
		listaArgsFormalesOVacio();
		match(P_PAREN_CLOSE);
	}

	private void listaArgsFormalesOVacio() {
		if (equalsAny(K_STRING, K_BOOLEAN, K_CHAR, K_INT, ID_CLS)) {
			listaArgsFormales();
		} else if (equalsAny(P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {boolean, char, ), idClase, String, int} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
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
		} else if (equalsAny(P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {), ,} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void argFormal() {
		tipo();
		match(ID_MV);
	}

	private void tipo() {
		if (equalsAny(K_STRING, K_BOOLEAN, K_CHAR, K_INT)) {
			tipoPrimitivo();
		} else if (equalsAny(ID_CLS)) {
			match(ID_CLS);
			genExplicitaOVacio();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (tipo) pero se encontro "+token.getType(), token);});
		}
	}

	private void tipoPrimitivo() {
		if (equalsAny(K_STRING)) {
			match(K_STRING);
		} else if (equalsAny(K_BOOLEAN)) {
			match(K_BOOLEAN);
		} else if (equalsAny(K_CHAR)) {
			match(K_CHAR);
		} else if (equalsAny(K_INT)) {
			match(K_INT);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (tipoPrimitivo) pero se encontro "+token.getType(), token);});
		}
	}

	private void herencia() {
		if (equalsAny(K_EXTENDS)) {
			match(K_EXTENDS);
			match(ID_CLS);
			genExplicitaOVacio();
		} else if (equalsAny(P_BRCKT_OPEN)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {extends, {} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void genExplicitaOVacio() {
		if (equalsAny(OP_LT)) {
			match(OP_LT);
			match(ID_CLS);
			match(OP_GT);
		} else if (equalsAny(K_EXTENDS, ID_MV, P_BRCKT_OPEN, P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {idMetVar, extends, ), {, <} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

    ///End
}
