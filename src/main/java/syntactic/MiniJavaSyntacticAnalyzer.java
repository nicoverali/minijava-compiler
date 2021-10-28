package syntactic;

import lexical.LexicalSequence;
import lexical.Token;
import lexical.TokenType;
import semantic.symbol.AttributeSymbol;
import semantic.symbol.ConstructorSymbol;
import semantic.symbol.ParameterSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.IsPublicAttribute;
import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.PrimitiveType;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;
import semantic.symbol.attribute.type.VoidType;
import semantic.symbol.user.UserClassSymbol;
import semantic.symbol.user.UserMethodSymbol;
import semantic.symbol.user.UserParameterSymbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static lexical.TokenType.*;
import static semantic.symbol.attribute.IsStaticAttribute.createDynamic;
import static semantic.symbol.attribute.IsStaticAttribute.createStatic;

public class MiniJavaSyntacticAnalyzer implements SyntacticAnalyzer {

	private static final SymbolTable ST = SymbolTable.getInstance();
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

	/**
	 * Returns a new {@link SyntacticException} with the given message and the next {@link Token} of the
	 * {@link LexicalSequence}
	 * Inside the message you can use both <i>{tokenType}</i> and <i>{lexeme}</i>, they will be replaced with the correct
	 * value at the moment the exception gets thrown
	 * @param message the message of the exception
	 * @return a {@link SyntacticException} with the given message
	 */
	private SyntacticException createSyntacticException(String message) {
		return sequence.peek().map(token -> {
			String formattedMessage = message;
			formattedMessage = formattedMessage.replaceAll("%tokenType%", token.getType().toString());
			formattedMessage = formattedMessage.replaceAll("%lexeme%", token.getLexeme());
			return new SyntacticException(formattedMessage, token);
		}).orElseThrow();

	}

    ///[Blocked]Start
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
			throw createSyntacticException("Se esperaba {class} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	private void clase() {
		match(K_CLASS);
		NameAttribute name = NameAttribute.of(match(ID_CLS));
		ST.currentClass = new UserClassSymbol(name);

		genExplicitaOVacio();

		// Check if extends from class
		herencia();

		// Add all members
		match(P_BRCKT_OPEN);
		listaMiembros();
		match(P_BRCKT_CLOSE);

		// Add final class to symbol table
		ST.add(ST.currentClass);
		ST.currentClass = null;
	}

	private void listaMiembros() {
		if (equalsAny(K_STATIC, K_DYNAMIC, ID_CLS, K_PUBLIC, K_PRIVATE, K_BOOLEAN, K_STRING, K_CHAR, K_INT)) {
			miembro();
			listaMiembros();
		} else if (equalsAny(P_BRCKT_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {private, static, boolean, public, char, dynamic, idClase, String, }, int} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	private void miembro() {
		if (equalsAny(K_STATIC)) {
			// Is a method or attribute and we know is static, we assume public
			IsStaticAttribute isStatic = createStatic(match(K_STATIC));
			atributoOMetodo(isStatic);
		} else if (equalsAny(K_DYNAMIC)) {
			// Is a method and we know is dynamic
			IsStaticAttribute isStatic = createDynamic(match(K_DYNAMIC));
			metodo(isStatic);
		} else if (equalsAny(ID_CLS)) {
			// Is an attribute or a constructor with a given class reference
			ReferenceType classReference = new ReferenceType(match(ID_CLS));
			atributoOConstructor(classReference);
		} else if (equalsAny(K_PUBLIC, K_PRIVATE)) {
			// Is an attribute with a given visibility and staticness
			IsPublicAttribute isPublic = visibilidad();
			IsStaticAttribute isStatic = staticOVacio();
			atributo(isPublic, isStatic);
		} else if (equalsAny(K_BOOLEAN, K_STRING, K_CHAR, K_INT)) {
			// Is a primitive attribute, we assume public and non-static
			PrimitiveType type = tipoPrimitivo();
			listaDecAtrs(type);
			match(P_SEMICOLON);
		} else {
			throw createSyntacticException("Se esperaba (miembro) pero se encontro %tokenType%");
		}
	}

	private void atributo(IsPublicAttribute isPublic, IsStaticAttribute isStatic) {
		Type type = tipo();
		listaDecAtrs(isPublic, isStatic, type);
		match(P_SEMICOLON);
	}

	/**
	 * Checks whether something is static or not, if there's no static token then we assume is
	 * dynamic.
	 * @return a {@link IsStaticAttribute}
	 */
	private IsStaticAttribute staticOVacio() {
		if (equalsAny(K_STATIC)) {
			return IsStaticAttribute.createStatic(match(K_STATIC));
		} else if (equalsAny(K_STRING, K_INT, K_BOOLEAN, ID_CLS, K_CHAR)) { // Check for follow
			// Nothing for now
			return IsStaticAttribute.emptyDynamic();
		} else {
			throw createSyntacticException("Se esperaba {static, boolean, char, idClase, String, int} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Checks whether something is public or not. It fails if neither <i>public</i> nor <i>private</i> tokens
	 * are present
	 * @return a {@link IsPublicAttribute}
	 */
	private IsPublicAttribute visibilidad() {
		if (equalsAny(K_PUBLIC)) {
			return IsPublicAttribute.createPublic(match(K_PUBLIC));
		} else if (equalsAny(K_PRIVATE)) {
			return IsPublicAttribute.createPrivate(match(K_PRIVATE));
		} else {
			throw createSyntacticException("Se esperaba (visibilidad) pero se encontro %tokenType%");
		}
	}

	/**
	 * Given a {@link ReferenceType} of a class, it builds either an attribute or a constructor
	 * @param classReference the class of the constructor or the type of the attribute
	 */
	private void atributoOConstructor(ReferenceType classReference) {
		if (equalsAny(OP_LT, ID_MV)) {
			genExplicitaOVacio();
			listaDecAtrs(classReference);
			match(P_SEMICOLON);
		} else if (equalsAny(P_PAREN_OPEN)) {
			restoConstructor(classReference);
		} else {
			throw createSyntacticException("Se esperaba (atributoOConstructor) pero se encontro %tokenType%");
		}
	}

	/**
	 * It builds a new constructor that belongs to the class referenced by the given {@link ReferenceType}
	 * @param classReference a reference to the class the constructor belongs
	 */
	private void restoConstructor(ReferenceType classReference) {
		List<ParameterSymbol> parameters  = argsFormales();
		bloque();
		ST.currentClass.add(new ConstructorSymbol(classReference, parameters));
	}

	/**
	 * It builds a new method with the given staticness and the type, name and parameters it founds
	 * @param isStatic whether the method is static or not
	 */
	private void metodo(IsStaticAttribute isStatic) {
		Type type = tipoMetodo();
		NameAttribute name = NameAttribute.of(match(ID_MV));
		List<ParameterSymbol> parameters = argsFormales();
		bloque();

		ST.currentClass.add(new UserMethodSymbol(isStatic, type, name, parameters));
	}

	/**
	 * Returns the type of a method
	 * @return a {@link Type}
	 */
	private Type tipoMetodo() {
		if (equalsAny(K_VOID)) {
			return VoidType.VOID(match(K_VOID));
		} else if (equalsAny(K_BOOLEAN, K_STRING, K_CHAR, K_INT, ID_CLS)) {
			return tipo();
		} else {
			throw createSyntacticException("Se esperaba (tipoMetodo) pero se encontro %tokenType%");
		}
	}

	/**
	 * Builds either an attribute or a method with the given staticness
	 */
	private void atributoOMetodo(IsStaticAttribute isStatic) {
		if (equalsAny(K_BOOLEAN, K_STRING, K_CHAR, K_INT, ID_CLS)) {
			Type type = tipo();
			NameAttribute name = NameAttribute.of(match(ID_MV));
			otrosDecAtrsORestoMetodo(isStatic, type, name);
		} else if (equalsAny(K_VOID)) {
			VoidType type = VoidType.VOID(match(K_VOID));
			NameAttribute name = NameAttribute.of(match(ID_MV));
			restoMetodo(isStatic, type, name);
		} else {
			throw createSyntacticException("Se esperaba (atributoOMetodo) pero se encontro %tokenType%");
		}
	}

	/**
	 * Build either an attribute or a method with the given staticness, type, and name
	 */
	private void otrosDecAtrsORestoMetodo(IsStaticAttribute isStatic, Type type, NameAttribute name) {
		if (equalsAny(P_PAREN_OPEN)) {
			restoMetodo(isStatic, type, name);
		} else if (equalsAny(P_COMMA, P_SEMICOLON)) {
			ST.currentClass.add(new AttributeSymbol(isStatic, type, name));
			otrosDecAtrs(isStatic, type);
			match(P_SEMICOLON);
		} else {
			throw createSyntacticException("Se esperaba (otrosDecAtrsORestoMetodo) pero se encontro %tokenType%");
		}
	}

	/**
	 * Builds attributes with the given visibility, staticness and type
	 */
	private void otrosDecAtrs(IsPublicAttribute isPublic, IsStaticAttribute isStatic, Type type) {
		if (equalsAny(P_COMMA)) {
			match(P_COMMA);
			listaDecAtrs(isPublic, isStatic, type);
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {;, ,} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Builds attributes with the given staticness and type
	 */
	private void otrosDecAtrs(IsStaticAttribute isStatic, Type type) {
		if (equalsAny(P_COMMA)) {
			match(P_COMMA);
			listaDecAtrs(isStatic, type);
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {;, ,} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Builds attributes with the given type
	 */
	private void otrosDecAtrs(Type type) {
		if (equalsAny(P_COMMA)) {
			match(P_COMMA);
			listaDecAtrs(type);
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {;, ,} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Builds an attribute with the given staticness and type
	 */
	private void listaDecAtrs(IsStaticAttribute isStatic, Type type) {
		NameAttribute name = NameAttribute.of(match(ID_MV));
		ST.currentClass.add(new AttributeSymbol(isStatic, type, name));
		otrosDecAtrs(isStatic, type);
	}

	/**
	 * Builds an attribute with the given type
	 */
	private void listaDecAtrs(Type type) {
		NameAttribute name = NameAttribute.of(match(ID_MV));
		ST.currentClass.add(new AttributeSymbol(type, name));
		otrosDecAtrs(type);
	}

	/**
	 * Builds an attribute with the given visibility, staticness and type
	 */
	private void listaDecAtrs(IsPublicAttribute isPublic, IsStaticAttribute isStatic, Type type) {
		NameAttribute name = NameAttribute.of(match(ID_MV));
		ST.currentClass.add(new AttributeSymbol(isPublic, isStatic, type, name));
		otrosDecAtrs(isPublic, isStatic, type);
	}

	/**
	 * Builds a method with the given staticness, return type and name
	 */
	private void restoMetodo(IsStaticAttribute isStatic, Type type, NameAttribute name) {
		List<ParameterSymbol> parameters = argsFormales();
		bloque();
		ST.currentClass.add(new UserMethodSymbol(isStatic, type, name, parameters));
	}

	private void bloque() {
		match(P_BRCKT_OPEN);
		listaSentencias();
		match(P_BRCKT_CLOSE);
	}

	private void listaSentencias() {
		if (equalsAny(P_SEMICOLON, ID_CLS, K_RETURN, K_FOR, K_NEW, K_THIS, ID_MV, P_PAREN_OPEN, P_BRCKT_OPEN, K_IF, K_BOOLEAN, K_STRING, K_CHAR, K_INT)) {
			sentencia();
			listaSentencias();
		} else if (equalsAny(P_BRCKT_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {new, for, this, (, String, int, idMetVar, boolean, char, idClase, ;, {, if, }, return} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	private void sentencia() {
		if (equalsAny(P_SEMICOLON)) {
			match(P_SEMICOLON);
		} else if (equalsAny(ID_CLS)) {
			match(ID_CLS);
			restoAccesoEstaticoOVarLocalClase();
			match(P_SEMICOLON);
		} else if (equalsAny(K_RETURN)) {
			sentenciaReturn();
			match(P_SEMICOLON);
		} else if (equalsAny(K_FOR)) {
			sentenciaFor();
		} else if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN)) {
			asignacionOLlamadaPrimitiva();
			match(P_SEMICOLON);
		} else if (equalsAny(P_BRCKT_OPEN)) {
			bloque();
		} else if (equalsAny(K_IF)) {
			sentenciaIf();
		} else if (equalsAny(K_BOOLEAN, K_STRING, K_CHAR, K_INT)) {
			varLocalPrimitiva();
			match(P_SEMICOLON);
		} else {
			throw createSyntacticException("Se esperaba (sentencia) pero se encontro %tokenType%");
		}
	}

	private void varLocalPrimitiva() {
		tipoPrimitivo();
		match(ID_MV);
		varLocalAsignacionOVacio();
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
			throw createSyntacticException("Se esperaba {new, this, (, for, String, int, idMetVar, boolean, else, char, idClase, ;, {, }, if, return} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	private void asignacionOLlamadaPrimitiva() {
		acceso();
		restoAsignacionOVacio();
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
			throw createSyntacticException("Se esperaba (restoFor) pero se encontro %tokenType%");
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

	private void sentenciaReturn() {
		match(K_RETURN);
		expresionOVacio();
	}

	private void expresionOVacio() {
		if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN, CHAR, STRING, K_NULL, K_TRUE, INT, K_FALSE, OP_NOT, OP_PLUS, OP_MINUS, ID_CLS)) {
			expresion();
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {new, !, this, (, intLiteral, false, charLiteral, +, -, idMetVar, null, stringLiteral, true, idClase, ;} pero se encontro %lexeme% (%tokenType%)");
		}
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
			throw createSyntacticException("Se esperaba (restoAccesoEstaticoOVarLocalClase) pero se encontro %tokenType%");
		}
	}

	private void varLocalAsignacionOVacio() {
		if (equalsAny(ASSIGN)) {
			match(ASSIGN);
			expresion();
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {;, =} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	private void restoAsignacionOVacio() {
		if (equalsAny(ASSIGN, ASSIGN_INCR, ASSIGN_DECR)) {
			tipoDeAsignacion();
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {++, ;, =, -} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	private void tipoDeAsignacion() {
		if (equalsAny(ASSIGN)) {
			match(ASSIGN);
			expresion();
		} else if (equalsAny(ASSIGN_INCR)) {
			match(ASSIGN_INCR);
		} else if (equalsAny(ASSIGN_DECR)) {
			match(ASSIGN_DECR);
		} else {
			throw createSyntacticException("Se esperaba (tipoDeAsignacion) pero se encontro %tokenType%");
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
		} else if (equalsAny(OP_MULT, P_SEMICOLON, P_COMMA, OP_MOD, OP_PLUS, P_DOT, ASSIGN, OP_DIV, OP_GT, OP_EQ, OP_GTE, OP_LT, OP_MINUS, P_PAREN_CLOSE, OP_AND, ASSIGN_INCR, ASSIGN_DECR, OP_LTE, OP_NOTEQ, OP_OR)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {==, &&, ++, ||, <=, %, (, ), *, +, ,, -, ., /, ;, <, !=, =, >, >=} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	private void argsActuales() {
		match(P_PAREN_OPEN);
		listaExpsOVacio();
		match(P_PAREN_CLOSE);
	}

	private void listaExpsOVacio() {
		if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN, CHAR, STRING, K_NULL, K_TRUE, INT, K_FALSE, OP_NOT, OP_PLUS, OP_MINUS, ID_CLS)) {
			listaExps();
		} else if (equalsAny(P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {new, !, this, (, intLiteral, false, ), charLiteral, +, -, idMetVar, null, stringLiteral, true, idClase} pero se encontro %lexeme% (%tokenType%)");
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
			throw createSyntacticException("Se esperaba {), ,} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	private void expresion() {
		expresionUnaria();
		expresionResto();
	}

	private void expresionUnaria() {
		if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN, CHAR, STRING, K_NULL, K_TRUE, INT, K_FALSE, OP_NOT, OP_PLUS, OP_MINUS)) {
			expresionUnariaSinStatic();
		} else if (equalsAny(ID_CLS)) {
			accesoEstatico();
		} else {
			throw createSyntacticException("Se esperaba (expresionUnaria) pero se encontro %tokenType%");
		}
	}

	private void expresionUnariaSinStatic() {
		if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN, CHAR, STRING, K_NULL, K_TRUE, INT, K_FALSE)) {
			operando();
		} else if (equalsAny(OP_NOT, OP_PLUS, OP_MINUS)) {
			operadorUnario();
			operando();
		} else {
			throw createSyntacticException("Se esperaba (expresionUnariaSinStatic) pero se encontro %tokenType%");
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
			throw createSyntacticException("Se esperaba (operadorUnario) pero se encontro %tokenType%");
		}
	}

	private void operando() {
		if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN)) {
			acceso();
		} else if (equalsAny(CHAR, STRING, K_NULL, K_TRUE, INT, K_FALSE)) {
			literal();
		} else {
			throw createSyntacticException("Se esperaba (operando) pero se encontro %tokenType%");
		}
	}

	private void literal() {
		if (equalsAny(CHAR)) {
			match(CHAR);
		} else if (equalsAny(STRING)) {
			match(STRING);
		} else if (equalsAny(K_NULL)) {
			match(K_NULL);
		} else if (equalsAny(K_TRUE)) {
			match(K_TRUE);
		} else if (equalsAny(INT)) {
			match(INT);
		} else if (equalsAny(K_FALSE)) {
			match(K_FALSE);
		} else {
			throw createSyntacticException("Se esperaba (literal) pero se encontro %tokenType%");
		}
	}

	private void acceso() {
		if (equalsAny(K_NEW, K_THIS, ID_MV)) {
			primario();
			encadenado();
		} else if (equalsAny(P_PAREN_OPEN)) {
			match(P_PAREN_OPEN);
			expParenOCasting();
			encadenado();
		} else {
			throw createSyntacticException("Se esperaba (acceso) pero se encontro %tokenType%");
		}
	}

	private void expParenOCasting() {
		if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN, CHAR, STRING, K_NULL, K_TRUE, INT, K_FALSE, OP_NOT, OP_PLUS, OP_MINUS)) {
			expresionUnariaSinStatic();
			expresionResto();
			match(P_PAREN_CLOSE);
		} else if (equalsAny(ID_CLS)) {
			match(ID_CLS);
			restoCastingORestoExpresion();
		} else {
			throw createSyntacticException("Se esperaba (expParenOCasting) pero se encontro %tokenType%");
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
			throw createSyntacticException("Se esperaba (restoCastingORestoExpresion) pero se encontro %tokenType%");
		}
	}

	private void accesoEstaticoOPrimarioOExpParen() {
		if (equalsAny(P_PAREN_OPEN)) {
			expresionParentizada();
		} else if (equalsAny(K_NEW, K_THIS, ID_MV)) {
			primario();
		} else if (equalsAny(ID_CLS)) {
			accesoEstatico();
		} else {
			throw createSyntacticException("Se esperaba (accesoEstaticoOPrimarioOExpParen) pero se encontro %tokenType%");
		}
	}

	private void accesoEstatico() {
		match(ID_CLS);
		match(P_DOT);
		accesoVarOMetodo();
		encadenado();
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
		if (equalsAny(OP_MULT, OP_LT, OP_GTE, OP_EQ, OP_AND, OP_LTE, OP_NOTEQ, OP_OR, OP_PLUS, OP_MINUS, OP_DIV, OP_MOD, OP_GT)) {
			operadorBinario();
			expresionUnaria();
			expresionResto();
		} else if (equalsAny(P_SEMICOLON, P_COMMA, P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {==, &&, ||, <=, %, ), *, +, ,, -, /, ;, <, !=, >, >=} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	private void operadorBinario() {
		if (equalsAny(OP_MULT)) {
			match(OP_MULT);
		} else if (equalsAny(OP_LT)) {
			match(OP_LT);
		} else if (equalsAny(OP_GTE)) {
			match(OP_GTE);
		} else if (equalsAny(OP_EQ)) {
			match(OP_EQ);
		} else if (equalsAny(OP_AND)) {
			match(OP_AND);
		} else if (equalsAny(OP_LTE)) {
			match(OP_LTE);
		} else if (equalsAny(OP_NOTEQ)) {
			match(OP_NOTEQ);
		} else if (equalsAny(OP_OR)) {
			match(OP_OR);
		} else if (equalsAny(OP_PLUS)) {
			match(OP_PLUS);
		} else if (equalsAny(OP_MINUS)) {
			match(OP_MINUS);
		} else if (equalsAny(OP_DIV)) {
			match(OP_DIV);
		} else if (equalsAny(OP_MOD)) {
			match(OP_MOD);
		} else if (equalsAny(OP_GT)) {
			match(OP_GT);
		} else {
			throw createSyntacticException("Se esperaba (operadorBinario) pero se encontro %tokenType%");
		}
	}

	private void encadenado() {
		if (equalsAny(P_DOT)) {
			varOMetodoEncadenado();
			encadenado();
		} else if (equalsAny(OP_MULT, P_SEMICOLON, P_COMMA, OP_MOD, OP_PLUS, ASSIGN, OP_DIV, OP_GT, OP_EQ, OP_GTE, OP_LT, OP_MINUS, P_PAREN_CLOSE, OP_AND, ASSIGN_INCR, ASSIGN_DECR, OP_LTE, OP_NOTEQ, OP_OR)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {==, &&, ++, ||, <=, %, ), *, +, ,, -, ., /, ;, <, !=, =, >, >=} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	private void varOMetodoEncadenado() {
		match(P_DOT);
		match(ID_MV);
		argsActualesOVacio();
	}

	private void primario() {
		if (equalsAny(K_NEW)) {
			accesoConstructor();
		} else if (equalsAny(K_THIS)) {
			accesoThis();
		} else if (equalsAny(ID_MV)) {
			accesoVarOMetodo();
		} else {
			throw createSyntacticException("Se esperaba (primario) pero se encontro %tokenType%");
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
			throw createSyntacticException("Se esperaba {(, <} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	private void restoGenericidad() {
		if (equalsAny(ID_CLS)) {
			genRestoExplicito();
		} else if (equalsAny(OP_GT)) {
			genRestoImplicito();
		} else {
			throw createSyntacticException("Se esperaba (restoGenericidad) pero se encontro %tokenType%");
		}
	}

	private void genRestoImplicito() {
		match(OP_GT);
	}

	private void genRestoExplicito() {
		match(ID_CLS);
		genExplicitaOVacio();
		otrosGen();
		match(OP_GT);
	}

	/**
	 * Returns a list of {@link ParameterSymbol}
	 */
	private List<ParameterSymbol> argsFormales() {
		match(P_PAREN_OPEN);
		List<ParameterSymbol> parameters = listaArgsFormalesOVacio();
		match(P_PAREN_CLOSE);
		return parameters;
	}

	/**
	 * Returns a list of {@link ParameterSymbol}
	 */
	private List<ParameterSymbol> listaArgsFormalesOVacio() {
		if (equalsAny(K_BOOLEAN, K_STRING, K_CHAR, K_INT, ID_CLS)) {
			return listaArgsFormales();
		} else if (equalsAny(P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
			return new ArrayList<>();
		} else {
			throw createSyntacticException("Se esperaba {boolean, char, ), idClase, String, int} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Returns a list of {@link ParameterSymbol}
	 */
	private List<ParameterSymbol> listaArgsFormales() {
		ParameterSymbol parameter = argFormal();
		List<ParameterSymbol> list = otrosArgsFormales();
		list.add(0, parameter);
		return list;
	}

	/**
	 * Returns a list of {@link ParameterSymbol}
	 */
	private List<ParameterSymbol> otrosArgsFormales() {
		if (equalsAny(P_COMMA)) {
			match(P_COMMA);
			return listaArgsFormales();
		} else if (equalsAny(P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
			return new ArrayList<>();
		} else {
			throw createSyntacticException("Se esperaba {), ,} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Returns a single {@link ParameterSymbol}
	 */
	private ParameterSymbol argFormal() {
		Type type = tipo();
		NameAttribute name = NameAttribute.of(match(ID_MV));
		return new UserParameterSymbol(type, name);
	}

	/**
	 * Returns a {@link Type}
	 */
	private Type tipo() {
		if (equalsAny(K_BOOLEAN, K_STRING, K_CHAR, K_INT)) {
			return tipoPrimitivo();
		} else if (equalsAny(ID_CLS)) {
			ReferenceType type = new ReferenceType(match(ID_CLS));
			genExplicitaOVacio();
			return type;
		} else {
			throw createSyntacticException("Se esperaba (tipo) pero se encontro %tokenType%");
		}
	}

	/**
	 * Returns a {@link PrimitiveType}
	 */
	private PrimitiveType tipoPrimitivo() {
		if (equalsAny(K_BOOLEAN)) {
			return PrimitiveType.BOOLEAN(match(K_BOOLEAN));
		} else if (equalsAny(K_STRING)) {
			return PrimitiveType.STRING(match(K_STRING));
		} else if (equalsAny(K_CHAR)) {
			return PrimitiveType.CHAR(match(K_CHAR));
		} else if (equalsAny(K_INT)) {
			return PrimitiveType.INT(match(K_INT));
		} else {
			throw createSyntacticException("Se esperaba (tipoPrimitivo) pero se encontro %tokenType%");
		}
	}

	private void herencia() {
		if (equalsAny(K_EXTENDS)) {
			match(K_EXTENDS);
			ReferenceType extendsType = new ReferenceType(match(ID_CLS));
			genExplicitaOVacio();
			ST.currentClass.setParent(extendsType);
		} else if (equalsAny(P_BRCKT_OPEN)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {extends, {} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	private void genExplicitaOVacio() {
		if (equalsAny(OP_LT)) {
			match(OP_LT);
			match(ID_CLS);
			genExplicitaOVacio();
			otrosGen();
			match(OP_GT);
		} else if (equalsAny(K_EXTENDS, P_COMMA, OP_GT, ID_MV, P_BRCKT_OPEN, P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {idMetVar, extends, ), {, <, ,, >} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	private void otrosGen() {
		if (equalsAny(P_COMMA)) {
			match(P_COMMA);
			match(ID_CLS);
			genExplicitaOVacio();
			otrosGen();
		} else if (equalsAny(OP_GT)) { // Check for follow
			// Nothing for now
		} else {
			throw createSyntacticException("Se esperaba {,, >} pero se encontro %lexeme% (%tokenType%)");
		}
	}

    ///End
}
