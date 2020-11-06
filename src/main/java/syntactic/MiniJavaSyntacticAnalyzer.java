package syntactic;

import lexical.Token;
import lexical.TokenType;
import lexical.analyzer.LexicalSequence;
import semantic.symbol.*;
import semantic.symbol.attribute.GenericityAttribute;
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

public class MiniJavaSyntacticAnalyzer implements SyntacticAnalyzer {

    private final SymbolTable st = SymbolTable.getInstance();
    private final LexicalSequence sequence;

    public MiniJavaSyntacticAnalyzer(LexicalSequence sequence) {
        this.sequence = sequence;
    }

    private Token match(TokenType type) {
        Token next = sequence.next().orElseThrow(IllegalStateException::new);
        if (next.getType() == EOF) {
            throw new SyntacticException("Se esperaba " + type + " pero se llego al final del archivo", next);
        } else if (next.getType() != type) {
            throw new SyntacticException("Se esperaba " + type + " pero se encontro " + next.getType(), next);
        }
        return next;
    }

    private boolean equalsAny(TokenType... types) {
        Optional<TokenType> nextType = sequence.peek().map(Token::getType);
        return nextType
                .filter(type -> Arrays.asList(types).contains(type))
                .isPresent();
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
        NameAttribute name = NameAttribute.of(match(ID_CLS));
        st.currentInterface = new InterfaceSymbol(name);

        // Check for generic interface
        genExplicitaOVacio().ifPresent(st.currentInterface::add);

        // Check if this interface extends another ones
        herenciaInterfaz();

        match(P_BRCKT_OPEN);
        listaMiembrosInterfaz();
        match(P_BRCKT_CLOSE);

        // Add the current interface to symbol table
        st.add(st.currentInterface);
        st.currentInterface = null;
    }

    private void herenciaInterfaz() {
        if (equalsAny(K_EXTENDS)) {
            match(K_EXTENDS);
            ReferenceType extendInter = new ReferenceType(match(ID_CLS));

            // Check for extending from generic interface
            genExplicitaOVacio().ifPresent(extendInter::addGeneric);

            // Add the interface to the current interface on symbol table
            st.currentInterface.addExtends(extendInter);

            // Keep extending interfaces if exist
            otrosHerenciaInterfaz();
        }
    }

    private void otrosHerenciaInterfaz() {
        if (equalsAny(P_COMMA)) {
            match(P_COMMA);
            ReferenceType extendInter = new ReferenceType(match(ID_CLS));

            // Check for extending from generic interface
            genExplicitaOVacio().ifPresent(extendInter::addGeneric);

            // Add the interface to the current interface on symbol table
            st.currentInterface.addExtends(extendInter);

            // Keep extending interfaces if exist
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
        IsStaticAttribute isStatic = formaMetodo();
        Type type = tipoMetodo();
        NameAttribute name = NameAttribute.of(match(ID_MV));
        List<ParameterSymbol> parameters = argsFormales();
        match(P_SEMICOLON);
        st.currentInterface.add(new UserMethodSymbol(isStatic, type, name, parameters));
    }

    private void clase() {
        match(K_CLASS);
        NameAttribute name = NameAttribute.of(match(ID_CLS));
        st.currentClass = new UserClassSymbol(name);

        // Check if class is generic
        genExplicitaOVacio().ifPresent(st.currentClass::add);

        // Check if extends from class or implements interface
        herenciaClase();

        // Add all members
        match(P_BRCKT_OPEN);
        listaMiembrosClase();
        match(P_BRCKT_CLOSE);

        // Add final class to symbol table
        st.add(st.currentClass);
        st.currentClass = null;
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
            ReferenceType extendsType = new ReferenceType(match(ID_CLS));
            genExplicitaOVacio().ifPresent(extendsType::addGeneric);
            st.currentClass.setParent(extendsType);
        }
    }

    private void implementsOVacio() {
        if (equalsAny(K_IMPLEMENTS)) {
            match(K_IMPLEMENTS);
            ReferenceType implementsType = new ReferenceType(match(ID_CLS));
            genExplicitaOVacio().ifPresent(implementsType::addGeneric);
            st.currentClass.addImplements(implementsType);
            otrosImplements();
        }
    }

    private void otrosImplements() {
        if (equalsAny(P_COMMA)) {
            match(P_COMMA);
            ReferenceType implementsType = new ReferenceType(match(ID_CLS));
            genExplicitaOVacio().ifPresent(implementsType::addGeneric);
            st.currentClass.addImplements(implementsType);
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
            metodo(IsStaticAttribute.createDynamic(match(K_DYNAMIC)));
        } else if (equalsAny(K_STATIC)) {
            atributoOMetodo(IsStaticAttribute.createStatic(match(K_STATIC)));
        } else if (equalsAny(K_CHAR, K_INT, K_BOOLEAN, K_STRING)) {
            PrimitiveType type = tipoPrimitivo();
            listaDecAtrs(type);
            match(P_SEMICOLON);
        } else if (equalsAny(K_PUBLIC, K_PRIVATE)) {
            IsPublicAttribute isPublic = visibilidad();
            IsStaticAttribute isStatic = staticOVacio();
            atributo(isPublic, isStatic);
        } else if (equalsAny(ID_CLS)) {
            ReferenceType classReference = new ReferenceType(match(ID_CLS));
            atributoOConstructor(classReference);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (miembroClase) pero se encontro " + token.getType(), token);
            });
        }
    }

    /**
     * Creates a new method in the current class of the {@link SymbolTable}
     *
     * @param isStatic a {@link IsStaticAttribute} that determines if the new method is static or not
     */
    private void metodo(IsStaticAttribute isStatic) {
        Type type = tipoMetodo();
        NameAttribute name = NameAttribute.of(match(ID_MV));
        List<ParameterSymbol> parameters = argsFormales();
        bloque();

        // Add method to current class
        st.currentClass.add(new UserMethodSymbol(isStatic, type, name, parameters));
    }

    private void atributoOConstructor(ReferenceType classReference) {
        if (equalsAny(OP_LT, ID_MV)) {
            genExplicitaOVacio().ifPresent(classReference::addGeneric);
            listaDecAtrs(classReference);
            match(P_SEMICOLON);
        } else if (equalsAny(P_PAREN_OPEN)) {
            constructor(classReference);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (atributoOConstructor) pero se encontro " + token.getType(), token);
            });
        }
    }

    private void constructor(ReferenceType classReference) {
        List<ParameterSymbol> parameters = argsFormales();
        bloque();
        st.currentClass.add(new ConstructorSymbol(classReference, parameters));
    }

    private void atributo(IsPublicAttribute isPublic, IsStaticAttribute isStatic) {
        Type type = tipo();
        listaDecAtrs(isPublic, isStatic, type);
        match(P_SEMICOLON);
    }

    private IsStaticAttribute staticOVacio() {
        if (equalsAny(K_STATIC)) {
            return IsStaticAttribute.createStatic(match(K_STATIC));
        }
        return IsStaticAttribute.emptyDynamic();
    }

    private IsPublicAttribute visibilidad() {
        if (equalsAny(K_PUBLIC)) {
            return IsPublicAttribute.createPublic(match(K_PUBLIC));
        } else if (equalsAny(K_PRIVATE)) {
            return IsPublicAttribute.createPrivate(match(K_PRIVATE));
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (visibilidad) pero se encontro " + token.getType(), token);
            });
            throw new IllegalStateException("No hay mas tokens para analizar");
        }
    }

    /**
     * @return a {@link IsStaticAttribute} that determines if the method is static or dynamic
     */
    private IsStaticAttribute formaMetodo() {
        if (equalsAny(K_DYNAMIC)) {
            return IsStaticAttribute.createDynamic(match(K_DYNAMIC));
        } else if (equalsAny(K_STATIC)) {
            return IsStaticAttribute.createStatic(match(K_STATIC));
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (formaMetodo) pero se encontro " + token.getType(), token);
            });
            throw new IllegalStateException("No hay mas tokens para analizar");
        }
    }

    /**
     * Depending of what's read from the sour code, adds a new {@link AttributeSymbol} or {@link MethodSymbol}
     * to the current class of the {@link SymbolTable}
     *
     * @param isStatic a {@link IsStaticAttribute} that determines if the new attribute or method is static or not
     */
    private void atributoOMetodo(IsStaticAttribute isStatic) {
        if (equalsAny(K_CHAR, K_INT, K_BOOLEAN, K_STRING, ID_CLS)) {
            Type type = tipo();
            NameAttribute name = NameAttribute.of(match(ID_MV));
            restoDecAtrsORestoMetodo(isStatic, type, name);
        } else if (equalsAny(K_VOID)) {
            VoidType type = VoidType.VOID(match(K_VOID));
            NameAttribute name = NameAttribute.of(match(ID_MV));
            restoMetodo(isStatic, type, name);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (atributoOMetodo) pero se encontro " + token.getType(), token);
            });
        }
    }

    /**
     * Depending of what's read from the sour code, adds a new {@link AttributeSymbol} or {@link MethodSymbol}
     * to the current class of the {@link SymbolTable}
     *
     * @param isStatic a {@link IsStaticAttribute} that determines if the new attribute or method is static or not
     * @param type a {@link Type} that determines the type of the attribute or the return type of the method
     * @param name a {@link NameAttribute} that contains the name of either the new attribute or new method
     */
    private void restoDecAtrsORestoMetodo(IsStaticAttribute isStatic, Type type, NameAttribute name) {
        if (equalsAny(ASSIGN_PLUS, ASSIGN, ASSIGN_MINUS, P_COMMA, P_SEMICOLON)) {
            // Add the first attribute to the class
            st.currentClass.add(new AttributeSymbol(isStatic, type, name));

            // They may be more inline attribute declarations
            restoDecAtrs(isStatic, type);
        } else if (equalsAny(P_PAREN_OPEN)) {
            restoMetodo(isStatic, type, name);
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (restoDecAtrsORestoMetodo) pero se encontro " + token.getType(), token);
            });
        }
    }

    /**
     * Adds a new {@link MethodSymbol} to the current class of the {@link SymbolTable}
     *
     * @param isStatic a {@link IsStaticAttribute} that determines if the new method is static or not
     * @param type a {@link Type} that determines the return type of the method
     * @param name a {@link NameAttribute} that contains the name of the new method
     */
    private void restoMetodo(IsStaticAttribute isStatic, Type type, NameAttribute name) {
        List<ParameterSymbol> parameters = argsFormales();
        bloque();
        st.currentClass.add(new UserMethodSymbol(isStatic, type, name, parameters));
    }

    /**
     * Adds other {@link AttributeSymbol} that may be declared inline to the current class of the {@link SymbolTable}.
     *
     * @param isStatic a {@link IsStaticAttribute} that determines if the new attributes are static or not
     * @param type the {@link Type} of the new attributes declared inline
     */
    private void restoDecAtrs(IsStaticAttribute isStatic, Type type) {
        asignacionOVacio();
        otrosDecAtrs(isStatic, type);
        match(P_SEMICOLON);
    }

    /**
     * Adds other {@link AttributeSymbol} that may be declared inline to the current class of the {@link SymbolTable}.
     *
     * @param isPublic a {@link IsPublicAttribute} that determines if the new attributes are public or not
     * @param isStatic a {@link IsStaticAttribute} that determines if the new attributes are static or not
     * @param type the {@link Type} of the new attributes declared inline
     */
    private void listaDecAtrs(IsPublicAttribute isPublic, IsStaticAttribute isStatic, Type type) {
        NameAttribute name = NameAttribute.of(match(ID_MV));
        st.currentClass.add(new AttributeSymbol(isPublic, isStatic, type, name));
        asignacionOVacio();
        otrosDecAtrs(isPublic, isStatic, type);
    }

    /**
     * Adds other {@link AttributeSymbol} that may be declared inline to the current class of the {@link SymbolTable}.
     *
     * @param isPublic a {@link IsPublicAttribute} that determines if the new attributes are public or not
     * @param isStatic a {@link IsStaticAttribute} that determines if the new attributes are static or not
     * @param type the {@link Type} of the new attributes declared inline
     */
    private void otrosDecAtrs(IsPublicAttribute isPublic, IsStaticAttribute isStatic, Type type) {
        if (equalsAny(P_COMMA)) {
            match(P_COMMA);
            listaDecAtrs(isPublic, isStatic, type);
        }
    }

    /**
     * Adds other {@link AttributeSymbol} that may be declared inline to the current class of the {@link SymbolTable}.
     * All attributes visibility will be set by default.
     *
     * @param isStatic a {@link IsStaticAttribute} that determines if the new attributes are static or not
     * @param type the {@link Type} of the new attributes declared inline
     */
    private void listaDecAtrs(IsStaticAttribute isStatic, Type type) {
        NameAttribute name = NameAttribute.of(match(ID_MV));
        st.currentClass.add(new AttributeSymbol(isStatic, type, name));
        asignacionOVacio();
        otrosDecAtrs(isStatic, type);
    }

    /**
     * Adds other {@link AttributeSymbol} that may be declared inline to the current class of the {@link SymbolTable}.
     * All attributes visibility will be set by default.
     *
     * @param isStatic a {@link IsStaticAttribute} that determines if the new attributes are static or not
     * @param type the {@link Type} of the new attributes declared inline
     */
    private void otrosDecAtrs(IsStaticAttribute isStatic, Type type) {
        if (equalsAny(P_COMMA)) {
            match(P_COMMA);
            listaDecAtrs(isStatic, type);
        }
    }

    /**
     * Adds other {@link AttributeSymbol} that may be declared inline to the current class of the {@link SymbolTable}.
     * All attributes visibility will be set by default, and so will be the static aspect of them.
     *
     * @param type the {@link Type} of the new attributes declared inline
     */
    private void listaDecAtrs(Type type) {
        NameAttribute name = NameAttribute.of(match(ID_MV));
        st.currentClass.add(new AttributeSymbol(type, name));
        asignacionOVacio();
        otrosDecAtrs(type);
    }

    /**
     * Adds other {@link AttributeSymbol} that may be declared inline to the current class of the {@link SymbolTable}.
     * All attributes visibility will be set by default, and so will be their static aspect.
     *
     * @param type the {@link Type} of the new attributes declared inline
     */
    private void otrosDecAtrs(Type type) {
        if (equalsAny(P_COMMA)) {
            match(P_COMMA);
            listaDecAtrs(type);
        }
    }

    /**
     * @return a {@link Type} read from the source code
     */
    private Type tipo() {
        if (equalsAny(K_CHAR, K_INT, K_BOOLEAN, K_STRING)) {
            return tipoPrimitivo();
        } else if (equalsAny(ID_CLS)) {
            return tipoClaseGen();
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (tipo) pero se encontro " + token.getType(), token);
            });
            throw new IllegalStateException("No hay mas tokens para analizar");
        }
    }

    /**
     * @return return a {@link Type} containing the type of the method
     */
    private Type tipoMetodo() {
        if (equalsAny(K_VOID)) {
            return VoidType.VOID(match(K_VOID));
        } else if (equalsAny(K_CHAR, K_INT, K_BOOLEAN, K_STRING, ID_CLS)) {
            return tipo();
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (tipoMetodo) pero se encontro " + token.getType(), token);
            });
            throw new IllegalStateException("No hay mas tokens para analizar");
        }
    }

    /**
     * @return a {@link ReferenceType} read from the source code. It may also contain a {@link GenericityAttribute}
     */
    private ReferenceType tipoClaseGen() {
        ReferenceType type = new ReferenceType(match(ID_CLS));
        genExplicitaOVacio().ifPresent(type::addGeneric);
        return type;
    }

    /**
     * @return a {@link PrimitiveType} read from the source code
     */
    private PrimitiveType tipoPrimitivo() {
        if (equalsAny(K_CHAR)) {
            return PrimitiveType.CHAR(match(K_CHAR));
        } else if (equalsAny(K_INT)) {
            return PrimitiveType.INT(match(K_INT));
        } else if (equalsAny(K_BOOLEAN)) {
            return PrimitiveType.BOOLEAN(match(K_BOOLEAN));
        } else if (equalsAny(K_STRING)) {
            return PrimitiveType.STRING(match(K_STRING));
        } else {
            sequence.next().ifPresent(token ->
            {
                throw new SyntacticException("Se esperaba (tipoPrimitivo) pero se encontro " + token.getType(), token);
            });
            throw new IllegalStateException("No hay mas tokens para analizar");
        }
    }

    /**
     * @return a list of all the consecutive {@link ParameterSymbol} read from the source code
     */
    private List<ParameterSymbol> listaArgsFormales() {
        ParameterSymbol parameter = argFormal();
        List<ParameterSymbol> list = otrosArgsFormales();
        list.add(0, parameter);
        return list;
    }

    /**
     * @return a list of all the consecutive {@link ParameterSymbol} read from the source code
     */
    private List<ParameterSymbol> otrosArgsFormales() {
        if (equalsAny(P_COMMA)) {
            match(P_COMMA);
            return listaArgsFormales();
        }
        return new ArrayList<>();
    }

    /**
     * @return a list of all the consecutive {@link ParameterSymbol} read from the source code.
     * If there are no parameters then an empty list gets returned
     */
    private List<ParameterSymbol> listaArgsFormalesOVacio() {
        if (equalsAny(K_CHAR, K_INT, K_BOOLEAN, K_STRING, ID_CLS)) {
            return listaArgsFormales();
        }
        return new ArrayList<>();
    }

    /**
     * @return a list of all the consecutive {@link ParameterSymbol} read from the source code
     */
    private List<ParameterSymbol> argsFormales() {
        match(P_PAREN_OPEN);
        List<ParameterSymbol> parameters = listaArgsFormalesOVacio();
        match(P_PAREN_CLOSE);
        return parameters;
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

    /**
     * @return an {@link Optional} wrapping the {@link GenericityAttribute} read from source code.
     * The optional will be empty if no genericty has been read
     */
    private Optional<GenericityAttribute> genExplicitaOVacio() {
        if (equalsAny(OP_LT)) {
            match(OP_LT);
            Token token = match(ID_CLS);
            match(OP_GT);
            return Optional.of(new GenericityAttribute(token));
        }
        return Optional.empty();
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

    /**
     * @return a {@link ParameterSymbol} read from the source code
     */
    private ParameterSymbol argFormal() {
        Type type = tipo();
        NameAttribute name = NameAttribute.of(match(ID_MV));
        return new UserParameterSymbol(type, name);
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
