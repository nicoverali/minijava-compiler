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
		herencia();
		match(P_BRCKT_OPEN);
		listaMiembros();
		match(P_BRCKT_CLOSE);
	}

	private void listaMiembros() {
		if (equalsAny(K_STRING, K_CHAR, K_INT, K_BOOLEAN, K_PRIVATE, K_PUBLIC, K_STATIC, K_DYNAMIC, ID_CLS)) {
			miembro();
			listaMiembros();
		} else if (equalsAny(P_BRCKT_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {private,boolean,static,public,char,dynamic,idClase,String,int} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void miembro() {
		if (equalsAny(K_STRING, K_CHAR, K_INT, K_BOOLEAN, K_PRIVATE, K_PUBLIC)) {
			atributo();
		} else if (equalsAny(K_STATIC, K_DYNAMIC)) {
			metodo();
		} else if (equalsAny(ID_CLS)) {
			match(ID_CLS);
			listaAtrsOConstructor();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (miembro) pero se encontro "+token.getType(), token);});
		}
	}

	private void listaAtrsOConstructor() {
		if (equalsAny(P_PAREN_OPEN)) {
			restoConstructor();
		} else if (equalsAny(ID_MV)) {
			listaDecAtrs();
			match(P_SEMICOLON);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (listaAtrsOConstructor) pero se encontro "+token.getType(), token);});
		}
	}

	private void restoConstructor() {
		argsFormales();
		bloque();
	}

	private void metodo() {
		formaMetodo();
		tipoMetodo();
		match(ID_MV);
		argsFormales();
		bloque();
	}

	private void bloque() {
		match(P_BRCKT_OPEN);
		listaSentencias();
		match(P_BRCKT_CLOSE);
	}

	private void listaSentencias() {
		if (equalsAny(P_SEMICOLON, P_PAREN_OPEN, K_THIS, K_NEW, ID_MV, K_RETURN, P_BRCKT_OPEN, K_STRING, K_CHAR, K_INT, K_BOOLEAN, ID_CLS, K_IF, K_FOR)) {
			sentencia();
			listaSentencias();
		} else if (equalsAny(P_BRCKT_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {new,(,this,for,String,int,idMetVar,boolean,char,idClase,;,{,if,return} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void sentencia() {
		if (equalsAny(P_SEMICOLON)) {
			match(P_SEMICOLON);
		} else if (equalsAny(P_PAREN_OPEN, K_THIS, K_NEW, ID_MV)) {
			asignacionOLlamada();
			match(P_SEMICOLON);
		} else if (equalsAny(K_RETURN)) {
			sentenciaReturn();
			match(P_SEMICOLON);
		} else if (equalsAny(P_BRCKT_OPEN)) {
			bloque();
		} else if (equalsAny(K_STRING, K_CHAR, K_INT, K_BOOLEAN, ID_CLS)) {
			varLocal();
			match(P_SEMICOLON);
		} else if (equalsAny(K_IF)) {
			sentenciaIf();
		} else if (equalsAny(K_FOR)) {
			sentenciaFor();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (sentencia) pero se encontro "+token.getType(), token);});
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
		if (equalsAny(ASSIGN, P_SEMICOLON)) {
			restoForClasico();
		} else if (equalsAny(P_COLON)) {
			restoForEach();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (restoFor) pero se encontro "+token.getType(), token);});
		}
	}

	private void restoForEach() {
		match(P_COLON);
		expresion();
		match(P_PAREN_CLOSE);
		sentencia();
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
				{throw new SyntacticException("Se esperaba {else} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
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
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {=} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void sentenciaReturn() {
		match(K_RETURN);
		expresionOVacio();
	}

	private void expresionOVacio() {
		if (equalsAny(OP_NOT, OP_PLUS, OP_MINUS, P_PAREN_OPEN, K_THIS, K_NEW, ID_MV, K_FALSE, K_TRUE, INT, STRING, K_NULL, CHAR)) {
			expresion();
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {!,new,(,this,false,intLiteral,charLiteral,+,-,idMetVar,null,stringLiteral,true} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void asignacionOLlamada() {
		acceso();
		restoAsignacionOVacio();
	}

	private void restoAsignacionOVacio() {
		if (equalsAny(ASSIGN_INCR, ASSIGN, OP_MINUS)) {
			tipoDeAsignacion();
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {++,=,-} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void tipoDeAsignacion() {
		if (equalsAny(ASSIGN_INCR)) {
			match(ASSIGN_INCR);
		} else if (equalsAny(ASSIGN)) {
			match(ASSIGN);
			expresion();
		} else if (equalsAny(OP_MINUS)) {
			match(OP_MINUS);
			match(OP_MINUS);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (tipoDeAsignacion) pero se encontro "+token.getType(), token);});
		}
	}

	private void acceso() {
		if (equalsAny(P_PAREN_OPEN)) {
			match(P_PAREN_OPEN);
			accesoEPoAccesoCasting();
			encadenado();
		} else if (equalsAny(K_THIS, K_NEW, ID_MV)) {
			primario();
			encadenado();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (acceso) pero se encontro "+token.getType(), token);});
		}
	}

	private void encadenado() {
		if (equalsAny(P_DOT)) {
			varOMetodoEncadenado();
			encadenado();
		} else if (equalsAny(OP_MULT, P_SEMICOLON, P_COMMA, OP_MOD, OP_PLUS, ASSIGN, OP_DIV, OP_GT, OP_EQ, OP_GTE, OP_MINUS, OP_LT, P_PAREN_CLOSE, OP_AND, ASSIGN_INCR, OP_NOTEQ, OP_LTE, OP_OR)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {.} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void varOMetodoEncadenado() {
		match(P_DOT);
		match(ID_MV);
		argsActualesOVacio();
	}

	private void accesoEPoAccesoCasting() {
		if (equalsAny(ID_CLS)) {
			restoCasting();
			primarioOExpParen();
		} else if (equalsAny(OP_NOT, OP_PLUS, OP_MINUS, P_PAREN_OPEN, K_THIS, K_NEW, ID_MV, K_FALSE, K_TRUE, INT, STRING, K_NULL, CHAR)) {
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
		if (equalsAny(P_PAREN_OPEN)) {
			expresionParentizada();
		} else if (equalsAny(K_THIS, K_NEW, ID_MV)) {
			primario();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (primarioOExpParen) pero se encontro "+token.getType(), token);});
		}
	}

	private void primario() {
		if (equalsAny(K_THIS)) {
			accesoThis();
		} else if (equalsAny(K_NEW)) {
			accesoConstructor();
		} else if (equalsAny(ID_MV)) {
			accesoVarOMetodo();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (primario) pero se encontro "+token.getType(), token);});
		}
	}

	private void accesoVarOMetodo() {
		match(ID_MV);
		argsActualesOVacio();
	}

	private void argsActualesOVacio() {
		if (equalsAny(P_PAREN_OPEN)) {
			argsActuales();
		} else if (equalsAny(OP_MULT, P_SEMICOLON, P_COMMA, OP_MOD, OP_PLUS, P_DOT, ASSIGN, OP_DIV, OP_GT, OP_EQ, OP_GTE, OP_MINUS, OP_LT, P_PAREN_CLOSE, OP_AND, ASSIGN_INCR, OP_NOTEQ, OP_LTE, OP_OR)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {(} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void accesoConstructor() {
		match(K_NEW);
		match(ID_CLS);
		argsActuales();
	}

	private void argsActuales() {
		match(P_PAREN_OPEN);
		listaExpsOVacio();
		match(P_PAREN_CLOSE);
	}

	private void listaExpsOVacio() {
		if (equalsAny(OP_NOT, OP_PLUS, OP_MINUS, P_PAREN_OPEN, K_THIS, K_NEW, ID_MV, K_FALSE, K_TRUE, INT, STRING, K_NULL, CHAR)) {
			listaExps();
		} else if (equalsAny(P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {!,new,(,this,false,intLiteral,charLiteral,+,-,idMetVar,null,stringLiteral,true} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
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
				{throw new SyntacticException("Se esperaba {,} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void accesoThis() {
		match(K_THIS);
	}

	private void expresionParentizada() {
		match(P_PAREN_OPEN);
		expresion();
		match(P_PAREN_CLOSE);
	}

	private void expresion() {
		expresionUnaria();
		expresionResto();
	}

	private void expresionResto() {
		if (equalsAny(OP_DIV, OP_GT, OP_MULT, OP_EQ, OP_MINUS, OP_PLUS, OP_OR, OP_GTE, OP_NOTEQ, OP_AND, OP_MOD, OP_LTE, OP_LT)) {
			operadorBinario();
			expresionUnaria();
			expresionResto();
		} else if (equalsAny(P_SEMICOLON, P_COMMA, P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {==,||,&&,<=,%,*,+,-,/,!=,<,>,>=} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void operadorBinario() {
		if (equalsAny(OP_DIV)) {
			match(OP_DIV);
		} else if (equalsAny(OP_GT)) {
			match(OP_GT);
		} else if (equalsAny(OP_MULT)) {
			match(OP_MULT);
		} else if (equalsAny(OP_EQ)) {
			match(OP_EQ);
		} else if (equalsAny(OP_MINUS)) {
			match(OP_MINUS);
		} else if (equalsAny(OP_PLUS)) {
			match(OP_PLUS);
		} else if (equalsAny(OP_OR)) {
			match(OP_OR);
		} else if (equalsAny(OP_GTE)) {
			match(OP_GTE);
		} else if (equalsAny(OP_NOTEQ)) {
			match(OP_NOTEQ);
		} else if (equalsAny(OP_AND)) {
			match(OP_AND);
		} else if (equalsAny(OP_MOD)) {
			match(OP_MOD);
		} else if (equalsAny(OP_LTE)) {
			match(OP_LTE);
		} else if (equalsAny(OP_LT)) {
			match(OP_LT);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (operadorBinario) pero se encontro "+token.getType(), token);});
		}
	}

	private void expresionUnaria() {
		if (equalsAny(OP_NOT, OP_PLUS, OP_MINUS)) {
			operadorUnario();
			operando();
		} else if (equalsAny(P_PAREN_OPEN, K_THIS, K_NEW, ID_MV, K_FALSE, K_TRUE, INT, STRING, K_NULL, CHAR)) {
			operando();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (expresionUnaria) pero se encontro "+token.getType(), token);});
		}
	}

	private void operando() {
		if (equalsAny(P_PAREN_OPEN, K_THIS, K_NEW, ID_MV)) {
			acceso();
		} else if (equalsAny(K_FALSE, K_TRUE, INT, STRING, K_NULL, CHAR)) {
			literal();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (operando) pero se encontro "+token.getType(), token);});
		}
	}

	private void literal() {
		if (equalsAny(K_FALSE)) {
			match(K_FALSE);
		} else if (equalsAny(K_TRUE)) {
			match(K_TRUE);
		} else if (equalsAny(INT)) {
			match(INT);
		} else if (equalsAny(STRING)) {
			match(STRING);
		} else if (equalsAny(K_NULL)) {
			match(K_NULL);
		} else if (equalsAny(CHAR)) {
			match(CHAR);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (literal) pero se encontro "+token.getType(), token);});
		}
	}

	private void operadorUnario() {
		if (equalsAny(OP_NOT)) {
			match(OP_NOT);
		} else if (equalsAny(OP_PLUS)) {
			match(OP_PLUS);
		} else if (equalsAny(OP_MINUS)) {
			match(OP_MINUS);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (operadorUnario) pero se encontro "+token.getType(), token);});
		}
	}

	private void restoCasting() {
		match(ID_CLS);
		match(P_PAREN_CLOSE);
	}

	private void argsFormales() {
		match(P_PAREN_OPEN);
		listaArgsFormalesOVacio();
		match(P_PAREN_CLOSE);
	}

	private void listaArgsFormalesOVacio() {
		if (equalsAny(K_STRING, K_CHAR, K_INT, K_BOOLEAN, ID_CLS)) {
			listaArgsFormales();
		} else if (equalsAny(P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {boolean,char,idClase,String,int} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
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
				{throw new SyntacticException("Se esperaba {,} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void argFormal() {
		tipo();
		match(ID_MV);
	}

	private void tipoMetodo() {
		if (equalsAny(K_VOID)) {
			match(K_VOID);
		} else if (equalsAny(K_STRING, K_CHAR, K_INT, K_BOOLEAN, ID_CLS)) {
			tipo();
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (tipoMetodo) pero se encontro "+token.getType(), token);});
		}
	}

	private void formaMetodo() {
		if (equalsAny(K_STATIC)) {
			match(K_STATIC);
		} else if (equalsAny(K_DYNAMIC)) {
			match(K_DYNAMIC);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (formaMetodo) pero se encontro "+token.getType(), token);});
		}
	}

	private void atributo() {
		if (equalsAny(K_STRING, K_CHAR, K_INT, K_BOOLEAN)) {
			tipoPrimitivo();
			listaDecAtrs();
			match(P_SEMICOLON);
		} else if (equalsAny(K_PRIVATE, K_PUBLIC)) {
			visibilidad();
			tipo();
			listaDecAtrs();
			match(P_SEMICOLON);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (atributo) pero se encontro "+token.getType(), token);});
		}
	}

	private void tipo() {
		if (equalsAny(K_STRING, K_CHAR, K_INT, K_BOOLEAN)) {
			tipoPrimitivo();
		} else if (equalsAny(ID_CLS)) {
			match(ID_CLS);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (tipo) pero se encontro "+token.getType(), token);});
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

	private void listaDecAtrs() {
		match(ID_MV);
		otrosDecAtrs();
	}

	private void otrosDecAtrs() {
		if (equalsAny(P_COMMA)) {
			match(P_COMMA);
			listaDecAtrs();
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {,} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

	private void tipoPrimitivo() {
		if (equalsAny(K_STRING)) {
			match(K_STRING);
		} else if (equalsAny(K_CHAR)) {
			match(K_CHAR);
		} else if (equalsAny(K_INT)) {
			match(K_INT);
		} else if (equalsAny(K_BOOLEAN)) {
			match(K_BOOLEAN);
		} else {
			sequence.next().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba (tipoPrimitivo) pero se encontro "+token.getType(), token);});
		}
	}

	private void herencia() {
		if (equalsAny(K_EXTENDS)) {
			match(K_EXTENDS);
			match(ID_CLS);
		} else if (equalsAny(P_BRCKT_OPEN)) { // Check for follow
			// Nothing for now
		} else {
			sequence.peek().ifPresent(token -> 
				{throw new SyntacticException("Se esperaba {extends} pero se encontro "+token.getLexeme()+" ("+token.getType()+")", token);});
		}
	}

    ///End
}
