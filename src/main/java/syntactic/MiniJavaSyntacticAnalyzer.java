package syntactic;

import lexical.Token;
import lexical.TokenType;
import lexical.LexicalSequence;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static lexical.TokenType.*;

public class MiniJavaSyntacticAnalyzer implements SyntacticAnalyzer {

    private final LexicalSequence sequence;
	private List<SyntacticException> exceptionList;

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
            throw new SyntacticException("Se esperaba " + type + " pero se llego al final del archivo", next);
        } else if (next.getType() != type) {
            throw new SyntacticException("Se esperaba " + type + " pero se encontro " + next.getType(), next);
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
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (miembro) pero se encontro "+token.getType(), token);});
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
		} else if  (equalsAny(K_INT, K_STRING, K_CHAR, K_BOOLEAN, ID_CLS)) {
			tipo();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (tipoMetodo) pero se encontro "+token.getType(), token);});
		}
	}

	private void formaMetodo() {
		if  (equalsAny(K_STATIC)) {
			match(K_STATIC);
		} else if  (equalsAny(K_DYNAMIC)) {
			match(K_DYNAMIC);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (formaMetodo) pero se encontro "+token.getType(), token);});
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
		if (equalsAny(K_INT, K_STRING, K_CHAR, K_BOOLEAN, ID_CLS, K_FOR, K_IF, P_BRCKT_OPEN, ID_MV, K_THIS, K_NEW, P_PAREN_OPEN, P_SEMICOLON, K_RETURN)) {
			sentencia();
			listaSentencias();
		}
	}

	private void sentencia() {
		if  (equalsAny(K_INT, K_STRING, K_CHAR, K_BOOLEAN, ID_CLS)) {
			varLocal();
			match(P_SEMICOLON);
		} else if  (equalsAny(K_FOR)) {
			sentenciaFor();
		} else if  (equalsAny(K_IF)) {
			sentenciaIf();
		} else if  (equalsAny(P_BRCKT_OPEN)) {
			bloque();
		} else if  (equalsAny(ID_MV, K_THIS, K_NEW, P_PAREN_OPEN)) {
			asignacionOLlamada();
			match(P_SEMICOLON);
		} else if  (equalsAny(P_SEMICOLON)) {
			match(P_SEMICOLON);
		} else if  (equalsAny(K_RETURN)) {
			sentenciaReturn();
			match(P_SEMICOLON);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (sentencia) pero se encontro "+token.getType(), token);});
		}
	}

	private void sentenciaReturn() {
		match(K_RETURN);
		expresionOVacio();
	}

	private void expresionOVacio() {
		if (equalsAny(OP_NOT, OP_MINUS, OP_PLUS, ID_MV, K_THIS, K_NEW, P_PAREN_OPEN, INT, STRING, K_NULL, K_FALSE, K_TRUE, CHAR)) {
			expresion();
		}
	}

	private void asignacionOLlamada() {
		acceso();
		restoAsignacionOVacio();
	}

	private void restoAsignacionOVacio() {
		if (equalsAny(ASSIGN_INCR, OP_MINUS, ASSIGN)) {
			tipoDeAsignacion();
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
		}
	}

	private void sentenciaFor() {
		match(K_FOR);
		match(P_PAREN_OPEN);
		varLocal();
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

	private void tipoDeAsignacion() {
		if  (equalsAny(ASSIGN_INCR)) {
			match(ASSIGN_INCR);
		} else if  (equalsAny(OP_MINUS)) {
			match(OP_MINUS);
			match(OP_MINUS);
		} else if  (equalsAny(ASSIGN)) {
			match(ASSIGN);
			expresion();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (tipoDeAsignacion) pero se encontro "+token.getType(), token);});
		}
	}

	private void varLocal() {
		tipo();
		match(ID_MV);
		varLocalAsignacionOVacio();
	}

	private void varLocalAsignacionOVacio() {
		if (equalsAny(ASSIGN)) {
			match(ASSIGN);
			expresion();
		}
	}

	private void expresion() {
		expresionUnaria();
		expresionResto();
	}

	private void expresionResto() {
		if (equalsAny(OP_PLUS, OP_OR, OP_DIV, OP_LTE, OP_GTE, OP_EQ, OP_AND, OP_LT, OP_MOD, OP_MINUS, OP_MULT, OP_NOTEQ, OP_GT)) {
			operadorBinario();
			expresionUnaria();
			expresionResto();
		}
	}

	private void operadorBinario() {
		if  (equalsAny(OP_PLUS)) {
			match(OP_PLUS);
		} else if  (equalsAny(OP_OR)) {
			match(OP_OR);
		} else if  (equalsAny(OP_DIV)) {
			match(OP_DIV);
		} else if  (equalsAny(OP_LTE)) {
			match(OP_LTE);
		} else if  (equalsAny(OP_GTE)) {
			match(OP_GTE);
		} else if  (equalsAny(OP_EQ)) {
			match(OP_EQ);
		} else if  (equalsAny(OP_AND)) {
			match(OP_AND);
		} else if  (equalsAny(OP_LT)) {
			match(OP_LT);
		} else if  (equalsAny(OP_MOD)) {
			match(OP_MOD);
		} else if  (equalsAny(OP_MINUS)) {
			match(OP_MINUS);
		} else if  (equalsAny(OP_MULT)) {
			match(OP_MULT);
		} else if  (equalsAny(OP_NOTEQ)) {
			match(OP_NOTEQ);
		} else if  (equalsAny(OP_GT)) {
			match(OP_GT);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (operadorBinario) pero se encontro "+token.getType(), token);});
		}
	}

	private void expresionUnaria() {
		if  (equalsAny(OP_NOT, OP_MINUS, OP_PLUS)) {
			operadorUnario();
			operando();
		} else if  (equalsAny(ID_MV, K_THIS, K_NEW, P_PAREN_OPEN, INT, STRING, K_NULL, K_FALSE, K_TRUE, CHAR)) {
			operando();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (expresionUnaria) pero se encontro "+token.getType(), token);});
		}
	}

	private void operando() {
		if  (equalsAny(ID_MV, K_THIS, K_NEW, P_PAREN_OPEN)) {
			acceso();
		} else if  (equalsAny(INT, STRING, K_NULL, K_FALSE, K_TRUE, CHAR)) {
			literal();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (operando) pero se encontro "+token.getType(), token);});
		}
	}

	private void literal() {
		if  (equalsAny(INT)) {
			match(INT);
		} else if  (equalsAny(STRING)) {
			match(STRING);
		} else if  (equalsAny(K_NULL)) {
			match(K_NULL);
		} else if  (equalsAny(K_FALSE)) {
			match(K_FALSE);
		} else if  (equalsAny(K_TRUE)) {
			match(K_TRUE);
		} else if  (equalsAny(CHAR)) {
			match(CHAR);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (literal) pero se encontro "+token.getType(), token);});
		}
	}

	private void acceso() {
		if  (equalsAny(ID_MV, K_THIS, K_NEW)) {
			primario();
			encadenado();
		} else if  (equalsAny(P_PAREN_OPEN)) {
			match(P_PAREN_OPEN);
			accesoEPoAccesoCasting();
			encadenado();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (acceso) pero se encontro "+token.getType(), token);});
		}
	}

	private void accesoEPoAccesoCasting() {
		if  (equalsAny(ID_CLS)) {
			restoCasting();
			primarioOExpParen();
		} else if  (equalsAny(OP_NOT, OP_MINUS, OP_PLUS, ID_MV, K_THIS, K_NEW, P_PAREN_OPEN, INT, STRING, K_NULL, K_FALSE, K_TRUE, CHAR)) {
			restoExpresionParentizada();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (accesoEPoAccesoCasting) pero se encontro "+token.getType(), token);});
		}
	}

	private void restoExpresionParentizada() {
		expresion();
		match(P_PAREN_CLOSE);
	}

	private void primarioOExpParen() {
		if  (equalsAny(ID_MV, K_THIS, K_NEW)) {
			primario();
		} else if  (equalsAny(P_PAREN_OPEN)) {
			expresionParentizada();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (primarioOExpParen) pero se encontro "+token.getType(), token);});
		}
	}

	private void expresionParentizada() {
		match(P_PAREN_OPEN);
		expresion();
		match(P_PAREN_CLOSE);
	}

	private void restoCasting() {
		match(ID_CLS);
		match(P_PAREN_CLOSE);
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
		if  (equalsAny(ID_MV)) {
			accesoVarOMetodo();
		} else if  (equalsAny(K_THIS)) {
			accesoThis();
		} else if  (equalsAny(K_NEW)) {
			accesoConstructor();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (primario) pero se encontro "+token.getType(), token);});
		}
	}

	private void accesoConstructor() {
		match(K_NEW);
		match(ID_CLS);
		argsActuales();
	}

	private void accesoThis() {
		match(K_THIS);
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
		if (equalsAny(OP_NOT, OP_MINUS, OP_PLUS, ID_MV, K_THIS, K_NEW, P_PAREN_OPEN, INT, STRING, K_NULL, K_FALSE, K_TRUE, CHAR)) {
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

	private void operadorUnario() {
		if  (equalsAny(OP_NOT)) {
			match(OP_NOT);
		} else if  (equalsAny(OP_MINUS)) {
			match(OP_MINUS);
		} else if  (equalsAny(OP_PLUS)) {
			match(OP_PLUS);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (operadorUnario) pero se encontro "+token.getType(), token);});
		}
	}

	private void argsFormales() {
		match(P_PAREN_OPEN);
		listaArgsFormalesOVacio();
		match(P_PAREN_CLOSE);
	}

	private void listaArgsFormalesOVacio() {
		if (equalsAny(K_INT, K_STRING, K_CHAR, K_BOOLEAN, ID_CLS)) {
			listaArgsFormales();
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
		}
	}

	private void tipo() {
		if  (equalsAny(K_INT, K_STRING, K_CHAR, K_BOOLEAN)) {
			tipoPrimitivo();
		} else if  (equalsAny(ID_CLS)) {
			match(ID_CLS);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (tipo) pero se encontro "+token.getType(), token);});
		}
	}

	private void tipoPrimitivo() {
		if  (equalsAny(K_INT)) {
			match(K_INT);
		} else if  (equalsAny(K_STRING)) {
			match(K_STRING);
		} else if  (equalsAny(K_CHAR)) {
			match(K_CHAR);
		} else if  (equalsAny(K_BOOLEAN)) {
			match(K_BOOLEAN);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (tipoPrimitivo) pero se encontro "+token.getType(), token);});
		}
	}

	private void visibilidad() {
		if  (equalsAny(K_PRIVATE)) {
			match(K_PRIVATE);
		} else if  (equalsAny(K_PUBLIC)) {
			match(K_PUBLIC);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (visibilidad) pero se encontro "+token.getType(), token);});
		}
	}

	private void herencia() {
		if (equalsAny(K_EXTENDS)) {
			match(K_EXTENDS);
			match(ID_CLS);
		}
	}

    ///End
}
