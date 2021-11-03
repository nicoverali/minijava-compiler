package syntactic;

import lexical.LexicalSequence;
import lexical.Token;
import lexical.TokenType;
import lexical.automata.AutomataToken;
import semantic.ast.block.BlockNode;
import semantic.ast.block.LocalVariable;
import semantic.ast.expression.NullNode;
import semantic.ast.expression.access.CastAccessNode;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.expression.LiteralNode;
import semantic.ast.expression.OperandNode;
import semantic.ast.expression.access.AccessNode;
import semantic.ast.expression.access.ConstructorAccessNode;
import semantic.ast.expression.access.MethodAccessNode;
import semantic.ast.expression.access.ParenthesizedExpressionNode;
import semantic.ast.expression.access.StaticMethodAccessNode;
import semantic.ast.expression.access.StaticVarAccessNode;
import semantic.ast.expression.access.ThisAccessNode;
import semantic.ast.expression.access.VarAccessNode;
import semantic.ast.expression.access.chain.ChainNode;
import semantic.ast.expression.access.chain.ChainedAttrNode;
import semantic.ast.expression.access.chain.ChainedMethodNode;
import semantic.ast.expression.binary.ArithmeticBinaryExpression;
import semantic.ast.expression.binary.ComparisonBinaryExpression;
import semantic.ast.expression.binary.EqualityBinaryExpression;
import semantic.ast.expression.binary.LogicalBinaryExpression;
import semantic.ast.expression.unary.IntUnaryExpression;
import semantic.ast.expression.unary.NotUnaryExpression;
import semantic.ast.sentence.*;
import semantic.ast.sentence.assignment.AssignmentNode;
import semantic.ast.sentence.assignment.AssignmentSentenceNode;
import semantic.ast.sentence.assignment.IncrDecrSentenceNode;
import semantic.symbol.AttributeSymbol;
import semantic.symbol.ConstructorSymbol;
import semantic.symbol.ParameterSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.IsPublicAttribute;
import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.*;
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
import static semantic.symbol.attribute.type.NullType.NULL;
import static semantic.symbol.attribute.type.ReferenceType.of;

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
		BlockNode block = bloque();
		ST.currentClass.add(new ConstructorSymbol(classReference, parameters, block));
	}

	/**
	 * It builds a new method with the given staticness and the type, name and parameters it founds
	 * @param isStatic whether the method is static or not
	 */
	private void metodo(IsStaticAttribute isStatic) {
		Type type = tipoMetodo();
		NameAttribute name = NameAttribute.of(match(ID_MV));
		List<ParameterSymbol> parameters = argsFormales();
		BlockNode block = bloque();

		ST.currentClass.add(new UserMethodSymbol(isStatic, type, name, parameters, block));
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
		BlockNode block = bloque();
		ST.currentClass.add(new UserMethodSymbol(isStatic, type, name, parameters, block));
	}

	/**
	 * Builds a {@link BlockNode} and returns it
	 */
	private BlockNode bloque() {
		Token openBracket = match(P_BRCKT_OPEN);
		List<SentenceNode> sentences = listaSentencias();
		Token closeBracket = match(P_BRCKT_CLOSE);
		return new BlockNode(openBracket, sentences, closeBracket);
	}

	/**
	 * Returns a list of {@link SentenceNode}
	 */
	private List<SentenceNode> listaSentencias() {
		if (equalsAny(P_SEMICOLON, ID_CLS, K_RETURN, K_FOR, K_NEW, K_THIS, ID_MV, P_PAREN_OPEN, P_BRCKT_OPEN, K_IF, K_BOOLEAN, K_STRING, K_CHAR, K_INT)) {
			SentenceNode sentence = sentencia();
			List<SentenceNode> list = listaSentencias();
			list.add(0, sentence); // Add it to the beginning
			return list;
		} else if (equalsAny(P_BRCKT_CLOSE)) { // Check for follow
			// Nothing for now
			return new ArrayList<>();
		} else {
			throw createSyntacticException("Se esperaba {new, for, this, (, String, int, idMetVar, boolean, char, idClase, ;, {, if, }, return} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Builds and returns a {@link SentenceNode}
	 */
	private SentenceNode sentencia() {
		if (equalsAny(P_SEMICOLON)) {
			Token semicolon = match(P_SEMICOLON);
			return new EmptySentenceNode(semicolon);
		} else if (equalsAny(ID_CLS)) {
			ReferenceType classRef = of(match(ID_CLS));
			SentenceNode sentence = restoAccesoEstaticoOVarLocalClase(classRef);
			match(P_SEMICOLON);
			return sentence;
		} else if (equalsAny(K_RETURN)) {
			ReturnSentenceNode sentence = sentenciaReturn();
			match(P_SEMICOLON);
			return sentence;
		} else if (equalsAny(K_FOR)) {
			return sentenciaFor();
		} else if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN)) {
			SentenceNode sentence = asignacionOLlamadaPrimitiva();
			match(P_SEMICOLON);
			return sentence;
		} else if (equalsAny(P_BRCKT_OPEN)) {
			BlockNode block = bloque();
			return new BlockSentenceNode(block);
		} else if (equalsAny(K_IF)) {
			return sentenciaIf();
		} else if (equalsAny(K_BOOLEAN, K_STRING, K_CHAR, K_INT)) {
			DeclarationSentenceNode sentence = varLocalPrimitiva();
			match(P_SEMICOLON);
			return sentence;
		} else {
			throw createSyntacticException("Se esperaba (sentencia) pero se encontro %tokenType%");
		}
	}

	/**
	 * Returns a {@link DeclarationSentenceNode} that has a {@link LocalVariable} of {@link PrimitiveType}
	 */
	private DeclarationSentenceNode varLocalPrimitiva() {
		PrimitiveType type = tipoPrimitivo();
		NameAttribute name = NameAttribute.of(match(ID_MV));

		return varLocalAsignacionOVacio(new LocalVariable(type, name));
	}

	/**
	 * Returns a {@link IfSentenceNode}
	 */
	private IfSentenceNode sentenciaIf() {
		Token ifToken = match(K_IF);
		match(P_PAREN_OPEN);
		ExpressionNode ifExpression = expresion();
		match(P_PAREN_CLOSE);
		SentenceNode sentence = sentencia();
		Optional<SentenceNode> elseSentence = elseOVacio();

		return elseSentence.isPresent()
				? new IfSentenceNode(ifToken, ifExpression, sentence, elseSentence.get())
				: new IfSentenceNode(ifToken, ifExpression, sentence);
	}

	/**
	 * Returns an {@link Optional} wrapping an else sentence
	 */
	private Optional<SentenceNode> elseOVacio() {
		if (equalsAny(K_ELSE)) {
			match(K_ELSE);
			return Optional.of(sentencia());
		} else if (equalsAny(K_NEW, P_SEMICOLON, ID_MV, P_BRCKT_OPEN, K_RETURN, K_THIS, P_BRCKT_CLOSE, K_IF, P_PAREN_OPEN, K_STRING, K_INT, K_BOOLEAN, K_FOR, ID_CLS, K_CHAR)) { // Check for follow
			// Nothing for now
			return Optional.empty();
		} else {
			throw createSyntacticException("Se esperaba {new, this, (, for, String, int, idMetVar, boolean, else, char, idClase, ;, {, }, if, return} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Returns a {@link SentenceNode} that is either a {@link AssignmentSentenceNode} or a {@link CallSentenceNode}
	 */
	private SentenceNode asignacionOLlamadaPrimitiva() {
		AccessNode leftAccess = acceso();
		return restoAsignacionOVacio(leftAccess);
	}

	/**
	 * Returns a {@link SentenceNode} that is a {@link ForSentenceNode}.
	 * In cases where the source code uses for-each statements they get mapped to
	 * an {@link EmptySentenceNode}
	 */
	private SentenceNode sentenciaFor() {
		Token forToken = match(K_FOR);
		match(P_PAREN_OPEN);
		Type varType = tipo();
		NameAttribute varName = NameAttribute.of(match(ID_MV));
		return restoFor(forToken, new LocalVariable(varType, varName));
	}

	/**
	 * Returns a {@link SentenceNode} that has to be a {@link ForSentenceNode}.
	 * We won't support for-each at this stage. If we get to compile a source code that
	 * contains a for-each, then we simply map the for-each sentence to an {@link EmptySentenceNode}
	 */
	private SentenceNode restoFor(Token forToken, LocalVariable var) {
		if (equalsAny(P_COLON)) {
			restoForEach();
			return new EmptySentenceNode(new AutomataToken(null, P_SEMICOLON, null, null));
		} else if (equalsAny(ASSIGN, P_SEMICOLON)) {
			return restoForClasico(forToken, var);
		} else {
			throw createSyntacticException("Se esperaba (restoFor) pero se encontro %tokenType%");
		}
	}

	/**
	 * Returns a {@link ForSentenceNode}
	 */
	private ForSentenceNode restoForClasico(Token forToken, LocalVariable var) {
		DeclarationSentenceNode declaration = varLocalAsignacionOVacio(var);
		match(P_SEMICOLON);
		ExpressionNode expression = expresion();
		match(P_SEMICOLON);
		AssignmentNode assignment = asignacion();
		match(P_PAREN_CLOSE);
		SentenceNode sentence = sentencia();
		return new ForSentenceNode(forToken, declaration, expression, assignment, sentence);
	}

	/**
	 * Returns a {@link AssignmentNode} that has to be one of the available assignments
	 */
	private AssignmentNode asignacion() {
		AccessNode leftAccess = acceso();
		return tipoDeAsignacion(leftAccess);
	}

	private void restoForEach() {
		match(P_COLON);
		expresion();
		match(P_PAREN_CLOSE);
		sentencia();
	}

	/**
	 * Returns a {@link ReturnSentenceNode}
	 */
	private ReturnSentenceNode sentenciaReturn() {
		Token returnToken = match(K_RETURN);
		Optional<ExpressionNode> exp = expresionOVacio();
		return exp.isPresent()
				? new ReturnSentenceNode(returnToken, exp.get())
				: new ReturnSentenceNode(returnToken);
	}

	/**
	 * Returns an {@link Optional} wrapping an {@link ExpressionNode}
	 */
	private Optional<ExpressionNode> expresionOVacio() {
		if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN, CHAR, STRING, K_NULL, K_TRUE, INT, K_FALSE, OP_NOT, OP_PLUS, OP_MINUS, ID_CLS)) {
			return Optional.of(expresion());
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
			return Optional.empty();
		} else {
			throw createSyntacticException("Se esperaba {new, !, this, (, intLiteral, false, charLiteral, +, -, idMetVar, null, stringLiteral, true, idClase, ;} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Returns a {@link SentenceNode} that is either a {@link AccessNode} or a {@link LocalVariable} with {@link ReferenceType}.
	 * It uses a {@link ReferenceType} to build the sentence.
	 */
	private SentenceNode restoAccesoEstaticoOVarLocalClase(ReferenceType classRef) {
		if (equalsAny(P_DOT)) {
			AccessNode leftAccess = restoAccesoEstatico(classRef);
			return restoAsignacionOVacio(leftAccess);
		} else if (equalsAny(OP_LT, ID_MV)) {
			genExplicitaOVacio();
			NameAttribute varName = NameAttribute.of(match(ID_MV));
			return varLocalAsignacionOVacio(new LocalVariable(classRef, varName));
		} else {
			throw createSyntacticException("Se esperaba (restoAccesoEstaticoOVarLocalClase) pero se encontro %tokenType%");
		}
	}

	/**
	 * Builds a {@link DeclarationSentenceNode} with the given {@link LocalVariable}. It may add an assignment to the declaration
	 */
	private DeclarationSentenceNode varLocalAsignacionOVacio(LocalVariable variable) {
		if (equalsAny(ASSIGN)) {
			Token assignToken = match(ASSIGN);
			ExpressionNode expression = expresion();
			return new DeclarationSentenceNode(variable, assignToken, expression);
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
			return new DeclarationSentenceNode(variable);
		} else {
			throw createSyntacticException("Se esperaba {;, =} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Receives an {@link AccessNode} and returns either an assignment sentences or a
	 * call sentences depending on the rest of the sentences.
	 * If the rest contains and assignment operator the the return sentences could be
	 * a {@link AssignmentSentenceNode} or a {@link IncrDecrSentenceNode}.
	 * If the rest is empty, then the given access is actually a {@link CallSentenceNode}
	 */
	private SentenceNode restoAsignacionOVacio(AccessNode leftAccess) {
		if (equalsAny(ASSIGN, ASSIGN_INCR, ASSIGN_DECR)) {
			return tipoDeAsignacion(leftAccess);
		} else if (equalsAny(P_SEMICOLON)) { // Check for follow
			// Nothing for now
			//noinspection OptionalGetWithoutIsPresent
			return new CallSentenceNode(leftAccess, sequence.peek().get());
		} else {
			throw createSyntacticException("Se esperaba {++, ;, =, -} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Builds and returns an assignment sentences with the given {@link AccessNode}
	 */
	private AssignmentNode tipoDeAsignacion(AccessNode leftAccess) {
		if (equalsAny(ASSIGN)) {
			Token assignToken = match(ASSIGN);
			ExpressionNode expression = expresion();
			return new AssignmentSentenceNode(leftAccess, assignToken, expression);
		} else if (equalsAny(ASSIGN_INCR)) {
			Token incrToken = match(ASSIGN_INCR);
			return new IncrDecrSentenceNode(leftAccess, incrToken);
		} else if (equalsAny(ASSIGN_DECR)) {
			Token decrToken = match(ASSIGN_DECR);
			return new IncrDecrSentenceNode(leftAccess, decrToken);
		} else {
			throw createSyntacticException("Se esperaba (tipoDeAsignacion) pero se encontro %tokenType%");
		}
	}

	/**
	 * Builds a {@link AccessNode} with the given {@link ReferenceType}
	 */
	private AccessNode restoAccesoEstatico(ReferenceType classRef) {
		Token dotToken = match(P_DOT);
		AccessNode access = accesoVarOMetodoEstatico(classRef);
		access = encadenado(access);
		return access;
	}

	/**
	 * Builds a {@link AccessNode} that's either a {@link VarAccessNode} or a {@link MethodAccessNode}
	 */
	private AccessNode accesoVarOMetodo() {
		NameAttribute name = NameAttribute.of(match(ID_MV));
		Optional<List<ExpressionNode>> arguments = argsActualesOVacio();
		return arguments.isPresent()
				? new MethodAccessNode(name, arguments.get())
				: new VarAccessNode(name);
	}

	/**
	 * Builds a {@link AccessNode} that's either a {@link StaticVarAccessNode} or a {@link StaticMethodAccessNode}.
	 * It uses the given {@link ReferenceType} to build the access
	 */
	private AccessNode accesoVarOMetodoEstatico(ReferenceType classRef) {
		NameAttribute name = NameAttribute.of(match(ID_MV));
		Optional<List<ExpressionNode>> arguments = argsActualesOVacio();

		return arguments.isPresent()
				? new StaticMethodAccessNode(classRef, name, arguments.get())
				: new StaticVarAccessNode(classRef, name);
	}

	/**
	 * Returns an {@link Optional} wrapping a list of {@link ExpressionNode} that represents actual parameters.
	 * Although returning an {@link Optional} wrapping a list is not recommended, in this case an empty list will mean
	 * that we have call but without arguments, instead of not call at all
	 */
	private Optional<List<ExpressionNode>> argsActualesOVacio() {
		if (equalsAny(P_PAREN_OPEN)) {
			return Optional.of(argsActuales());
		} else if (equalsAny(OP_MULT, P_SEMICOLON, P_COMMA, OP_MOD, OP_PLUS, P_DOT, ASSIGN, OP_DIV, OP_GT, OP_EQ, OP_GTE, OP_LT, OP_MINUS, P_PAREN_CLOSE, OP_AND, ASSIGN_INCR, ASSIGN_DECR, OP_LTE, OP_NOTEQ, OP_OR)) { // Check for follow
			// Nothing for now
			return Optional.empty();
		} else {
			throw createSyntacticException("Se esperaba {==, &&, ++, ||, <=, %, (, ), *, +, ,, -, ., /, ;, <, !=, =, >, >=} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Returns a list of {@link ExpressionNode} that represent actual parameters
	 */
	private List<ExpressionNode> argsActuales() {
		match(P_PAREN_OPEN);
		List<ExpressionNode> exps = listaExpsOVacio();
		match(P_PAREN_CLOSE);
		return exps;
	}

	/**
	 * Returns a list of {@link ExpressionNode}
	 */
	private List<ExpressionNode> listaExpsOVacio() {
		if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN, CHAR, STRING, K_NULL, K_TRUE, INT, K_FALSE, OP_NOT, OP_PLUS, OP_MINUS, ID_CLS)) {
			return listaExps();
		} else if (equalsAny(P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
			return List.of();
		} else {
			throw createSyntacticException("Se esperaba {new, !, this, (, intLiteral, false, ), charLiteral, +, -, idMetVar, null, stringLiteral, true, idClase} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Returns a list of {@link ExpressionNode}
	 */
	private List<ExpressionNode> listaExps() {
		ExpressionNode expression = expresion();
		List<ExpressionNode> list = otrosExps();
		list.add(0, expression); // Add it to the beginning
		return list;
	}

	/**
	 * Returns a list of {@link ExpressionNode}
	 */
	private List<ExpressionNode> otrosExps() {
		if (equalsAny(P_COMMA)) {
			match(P_COMMA);
			return listaExps();
		} else if (equalsAny(P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
			return new ArrayList<>();
		} else {
			throw createSyntacticException("Se esperaba {), ,} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Builds an {@link ExpressionNode} and returns it
	 */
	private ExpressionNode expresion() {
		ExpressionNode leftExp = expresionUnaria();
		return expresionResto(leftExp);
	}

	/**
	 * Builds and returns an {@link ExpressionNode}
	 */
	private ExpressionNode expresionUnaria() {
		if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN, CHAR, STRING, K_NULL, K_TRUE, INT, K_FALSE, OP_NOT, OP_PLUS, OP_MINUS)) {
			return expresionUnariaSinStatic();
		} else if (equalsAny(ID_CLS)) {
			return accesoEstatico();
		} else {
			throw createSyntacticException("Se esperaba (expresionUnaria) pero se encontro %tokenType%");
		}
	}

	/**
	 * Builds and returns an {@link ExpressionNode}
	 */
	private ExpressionNode expresionUnariaSinStatic() {
		if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN, CHAR, STRING, K_NULL, K_TRUE, INT, K_FALSE)) {
			return operando();
		} else if (equalsAny(OP_NOT, OP_PLUS, OP_MINUS)) {
			Token operator = operadorUnario();
			OperandNode operand = operando();
			switch (operator.getType()){
				case OP_PLUS: case OP_MINUS:
					return new IntUnaryExpression(operand, operator);
				case OP_NOT:
					return new NotUnaryExpression(operand, operator);
				default: throw new RuntimeException("Operator should be one of the given above");
			}
		} else {
			throw createSyntacticException("Se esperaba (expresionUnariaSinStatic) pero se encontro %tokenType%");
		}
	}

	/**
	 * Returns a {@link Token} that represents a unary <b>operator</b>
	 */
	private Token operadorUnario() {
		if (equalsAny(OP_NOT)) {
			return match(OP_NOT);
		} else if (equalsAny(OP_PLUS)) {
			return match(OP_PLUS);
		} else if (equalsAny(OP_MINUS)) {
			return match(OP_MINUS);
		} else {
			throw createSyntacticException("Se esperaba (operadorUnario) pero se encontro %tokenType%");
		}
	}

	/**
	 * Returns a {@link OperandNode}
	 */
	private OperandNode operando() {
		if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN)) {
			return acceso();
		} else if (equalsAny(CHAR, STRING, K_NULL, K_TRUE, INT, K_FALSE)) {
			return literal();
		} else {
			throw createSyntacticException("Se esperaba (operando) pero se encontro %tokenType%");
		}
	}

	/**
	 * Returns a {@link OperandNode}
	 */
	private OperandNode literal() {
		if (equalsAny(CHAR)) {
			return new LiteralNode(PrimitiveType.CHAR(match(CHAR)));
		} else if (equalsAny(STRING)) {
			return new LiteralNode(PrimitiveType.STRING(match(STRING)));
		} else if (equalsAny(K_NULL)) {
			return new NullNode(NULL(match(K_NULL)));
		} else if (equalsAny(K_TRUE)) {
			return new LiteralNode(PrimitiveType.BOOLEAN(match(K_TRUE)));
		} else if (equalsAny(INT)) {
			return new LiteralNode(PrimitiveType.INT(match(INT)));
		} else if (equalsAny(K_FALSE)) {
			return new LiteralNode(PrimitiveType.BOOLEAN(match(K_FALSE)));
		} else {
			throw createSyntacticException("Se esperaba (literal) pero se encontro %tokenType%");
		}
	}

	/**
	 * Builds and returns an {@link AccessNode}
	 */
	private AccessNode acceso() {
		if (equalsAny(K_NEW, K_THIS, ID_MV)) {
			AccessNode access = primario();
			access = encadenado(access);
			return access;
		} else if (equalsAny(P_PAREN_OPEN)) {
			match(P_PAREN_OPEN);
			AccessNode leftAccess = expParenOCasting();
			return encadenado(leftAccess);
		} else {
			throw createSyntacticException("Se esperaba (acceso) pero se encontro %tokenType%");
		}
	}

	/**
	 * Builds and returns an {@link AccessNode} that can be a {@link CastAccessNode}
	 * or a {@link ParenthesizedExpressionNode}
	 */
	private AccessNode expParenOCasting() {
		if (equalsAny(K_NEW, K_THIS, ID_MV, P_PAREN_OPEN, CHAR, STRING, K_NULL, K_TRUE, INT, K_FALSE, OP_NOT, OP_PLUS, OP_MINUS)) {
			ExpressionNode exp = expresionUnariaSinStatic();
			exp = expresionResto(exp);
			match(P_PAREN_CLOSE);
			return new ParenthesizedExpressionNode(exp);
		} else if (equalsAny(ID_CLS)) {
			ReferenceType classRef = of(match(ID_CLS));
			return restoCastingORestoExpresion(classRef);
		} else {
			throw createSyntacticException("Se esperaba (expParenOCasting) pero se encontro %tokenType%");
		}
	}

	/**
	 * Builds and returns an {@link AccessNode} that can be a {@link CastAccessNode}
	 * or a {@link ParenthesizedExpressionNode}
	 * It uses the given {@link ReferenceType} to build the expression
	 */
	private AccessNode restoCastingORestoExpresion(ReferenceType classRef) {
		if (equalsAny(OP_LT, P_PAREN_CLOSE)) {
			restoCasting();
			AccessNode access = accesoEstaticoOPrimarioOExpParen();
			AccessNode finalAccess = encadenado(access);
			return new CastAccessNode(classRef, finalAccess);
		} else if (equalsAny(P_DOT)) {
			AccessNode leftAccess = restoAccesoEstatico(classRef);
			ExpressionNode expression = expresionResto(leftAccess);
			match(P_PAREN_CLOSE);
			return new ParenthesizedExpressionNode(expression);
		} else {
			throw createSyntacticException("Se esperaba (restoCastingORestoExpresion) pero se encontro %tokenType%");
		}
	}

	/**
	 * Builds and returns an {@link AccessNode}
	 */
	private AccessNode accesoEstaticoOPrimarioOExpParen() {
		if (equalsAny(P_PAREN_OPEN)) {
			return expresionParentizada();
		} else if (equalsAny(K_NEW, K_THIS, ID_MV)) {
			return primario();
		} else if (equalsAny(ID_CLS)) {
			return accesoEstatico();
		} else {
			throw createSyntacticException("Se esperaba (accesoEstaticoOPrimarioOExpParen) pero se encontro %tokenType%");
		}
	}

	/**
	 * Returns an {@link AccessNode} that's either a {@link StaticMethodAccessNode} or a {@link StaticVarAccessNode}
	 */
	private AccessNode accesoEstatico() {
		ReferenceType classRef = of(match(ID_CLS));
		match(P_DOT);
		AccessNode leftAccess = accesoVarOMetodoEstatico(classRef);
		return encadenado(leftAccess);
	}

	/**
	 * Returns an {@link AccessNode}
	 */
	private AccessNode expresionParentizada() {
		match(P_PAREN_OPEN);
		ExpressionNode expression = expresion();
		match(P_PAREN_CLOSE);
		return new ParenthesizedExpressionNode(expression);
	}

	private void restoCasting() {
		genExplicitaOVacio();
		match(P_PAREN_CLOSE);
	}

	/**
	 * Using the given {@link ExpressionNode} it builds either a binary expression or returns the same
	 * {@link ExpressionNode}
	 */
	private ExpressionNode expresionResto(ExpressionNode leftExpression) {
		if (equalsAny(OP_MULT, OP_LT, OP_GTE, OP_EQ, OP_AND, OP_LTE, OP_NOTEQ, OP_OR, OP_PLUS, OP_MINUS, OP_DIV, OP_MOD, OP_GT)) {
			Token operator = operadorBinario();
			ExpressionNode exp = expresionUnaria();
			switch (operator.getType()){
				case OP_MULT: case OP_PLUS: case OP_MINUS: case OP_DIV: case OP_MOD:
					exp = new ArithmeticBinaryExpression(leftExpression, exp, operator); break;
				case OP_AND: case OP_OR:
					exp = new LogicalBinaryExpression(leftExpression, exp, operator); break;
				case OP_EQ: case OP_NOTEQ:
					exp = new EqualityBinaryExpression(leftExpression, exp, operator); break;
				case OP_GT: case OP_GTE: case OP_LT: case OP_LTE:
					exp = new ComparisonBinaryExpression(leftExpression, exp, operator); break;
			}
			return expresionResto(exp);
		} else if (equalsAny(P_SEMICOLON, P_COMMA, P_PAREN_CLOSE)) { // Check for follow
			// Nothing for now
			return leftExpression;
		} else {
			throw createSyntacticException("Se esperaba {==, &&, ||, <=, %, ), *, +, ,, -, /, ;, <, !=, >, >=} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Returns a {@link Token} that represents a binary oeprator
	 */
	private Token operadorBinario() {
		if (equalsAny(OP_MULT)) {
			return match(OP_MULT);
		} else if (equalsAny(OP_LT)) {
			return match(OP_LT);
		} else if (equalsAny(OP_GTE)) {
			return match(OP_GTE);
		} else if (equalsAny(OP_EQ)) {
			return match(OP_EQ);
		} else if (equalsAny(OP_AND)) {
			return match(OP_AND);
		} else if (equalsAny(OP_LTE)) {
			return match(OP_LTE);
		} else if (equalsAny(OP_NOTEQ)) {
			return match(OP_NOTEQ);
		} else if (equalsAny(OP_OR)) {
			return match(OP_OR);
		} else if (equalsAny(OP_PLUS)) {
			return match(OP_PLUS);
		} else if (equalsAny(OP_MINUS)) {
			return match(OP_MINUS);
		} else if (equalsAny(OP_DIV)) {
			return match(OP_DIV);
		} else if (equalsAny(OP_MOD)) {
			return match(OP_MOD);
		} else if (equalsAny(OP_GT)) {
			return match(OP_GT);
		} else {
			throw createSyntacticException("Se esperaba (operadorBinario) pero se encontro %tokenType%");
		}
	}

	/**
	 * Using the given {@link AccessNode} it chains to it a {@link ChainNode} if present. Else it returns the
	 * same {@link AccessNode}
	 */
	private AccessNode encadenado(AccessNode leftAccess) {
		if (equalsAny(P_DOT)) {
			ChainNode chainNode = varOMetodoEncadenado(leftAccess);
			leftAccess.setChain(chainNode);
			encadenado(chainNode);
			return leftAccess;
		} else if (equalsAny(OP_MULT, P_SEMICOLON, P_COMMA, OP_MOD, OP_PLUS, ASSIGN, OP_DIV, OP_GT, OP_EQ, OP_GTE, OP_LT, OP_MINUS, P_PAREN_CLOSE, OP_AND, ASSIGN_INCR, ASSIGN_DECR, OP_LTE, OP_NOTEQ, OP_OR)) { // Check for follow
			// Nothing for now
			return leftAccess;
		} else {
			throw createSyntacticException("Se esperaba {==, &&, ++, ||, <=, %, ), *, +, ,, -, ., /, ;, <, !=, =, >, >=} pero se encontro %lexeme% (%tokenType%)");
		}
	}

	/**
	 * Builds and returns either a {@link ChainedAttrNode} or a {@link ChainedMethodNode}.
	 * It uses the given {@link AccessNode} to set the chained nodes their left access
	 */
	private ChainNode varOMetodoEncadenado(AccessNode leftAccess) {
		Token dotToken = match(P_DOT);
		NameAttribute name = NameAttribute.of(match(ID_MV));
		Optional<List<ExpressionNode>> arguments = argsActualesOVacio();
		return arguments.isPresent()
				? new ChainedMethodNode(leftAccess, dotToken, name, arguments.get())
				: new ChainedAttrNode(leftAccess, dotToken, name);
	}

	/**
	 * Builds and returns a {@link AccessNode}
	 */
	private AccessNode primario() {
		if (equalsAny(K_NEW)) {
			return accesoConstructor();
		} else if (equalsAny(K_THIS)) {
			return accesoThis();
		} else if (equalsAny(ID_MV)) {
			return accesoVarOMetodo();
		} else {
			throw createSyntacticException("Se esperaba (primario) pero se encontro %tokenType%");
		}
	}

	/**
	 * Returns a {@link ThisAccessNode}
	 */
	private ThisAccessNode accesoThis() {
		return new ThisAccessNode(match(K_THIS));
	}

	/**
	 * Returns a {@link ConstructorAccessNode}
	 */
	private ConstructorAccessNode accesoConstructor() {
		match(K_NEW);
		ReferenceType classRef = of(match(ID_CLS));
		genericidadOVacio();
		List<ExpressionNode> arguments = argsActuales();
		return new ConstructorAccessNode(classRef, arguments);
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
