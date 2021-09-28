package lexical.analyzer;

import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.Lexeme;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.branch.DefaultNodeBranch;
import lexical.automata.branch.filter.*;
import lexical.automata.builder.LexicalNodeBuilder;
import lexical.automata.node.IntegerLexicalNode;
import lexical.automata.node.strategy.AcceptorStrategy;
import lexical.automata.node.strategy.InitialNodeStrategy;
import lexical.automata.node.strategy.KeywordDecorator;
import lexical.automata.node.strategy.SubLexemeStrategy;

import java.util.Optional;

import static lexical.TokenType.*;

public class MiniJavaLexicalAnalyzer implements LexicalAnalyzer{


    private final SourceCodeReader reader;
    private boolean didReachEOF = false;

    public MiniJavaLexicalAnalyzer(SourceCodeReader reader) {
        this.reader = reader;
        buildCircularReferences();
    }

    @Override
    public Optional<Token> getNextToken() throws LexicalException {
        if (didReachEOF) return Optional.empty();

        Token token;
        do {
            token = initialNode.process(reader, Lexeme.empty());
        } while (token == null);

        if (token.getType() == EOF){
            didReachEOF = true;
        }

        return Optional.of(token);
    }

    // ---------- ASSIGNMENT AND EQUALS ------------ //

    private static final LexicalNode assignAcceptor =
            new LexicalNodeBuilder("Tries to match equal token or else returns assign token")
                .ifEquals('=').thenReturn(OP_EQ)
                .orElseReturn(ASSIGN);

    // -------- //

    // ---------- OPERATORS ------------ //

    private static final LexicalNode plusAcceptor =
            new LexicalNodeBuilder("Tries to match plus_assign token or else return plus token")
                .ifEquals('+').thenReturn(ASSIGN_INCR)
                .orElseReturn(OP_PLUS);

    private static final LexicalNode minusAcceptor =
            new LexicalNodeBuilder("Tries to match minus_assign token or else return plus token")
                .ifEquals('-').thenReturn(ASSIGN_DECR)
                .orElseReturn(OP_MINUS);

    private static final LexicalNode andOpMiddle =
            new LexicalNodeBuilder("Tries to match the second & of AND token")
                .ifEquals('&').thenReturn(OP_AND)
                .orElseThrow("Operador AND mal formado. Falta un caracter '&'");

    private static final LexicalNode orOpMiddle =
            new LexicalNodeBuilder("Tries to match the second | of OR token")
                .ifEquals('|').thenReturn(OP_OR)
                .orElseThrow("Operador OR mal formado. Falta un caracter '|'");

    private static final LexicalNode notOperatorAcceptor =
            new LexicalNodeBuilder("Tries to match not_equal token or else return NOT token")
                .ifEquals('=').thenReturn(OP_NOTEQ)
                .orElseReturn(OP_NOT);

    private static final LexicalNode lessAcceptor =
            new LexicalNodeBuilder("Tries to match less_or_equal token or else return LESS token")
                .ifEquals('=').thenReturn(OP_LTE)
                .orElseReturn(OP_LT);

    private static final LexicalNode greaterAcceptor =
            new LexicalNodeBuilder("Tries to match grater_or_equal token or else return GREATER token")
                .ifEquals('=').thenReturn(OP_GTE)
                .orElseReturn(OP_GT);

    // -------- //

    // ---------- IDENTIFIERS ------------ //

    private static final LexicalNode methodVariableIdentifierAcceptor =
            new LexicalNodeBuilder("Matches any letter, digit or underscore, then return ID_MV token")
                .ifPass(new DigitFilter()).thenRepeat()
                .ifPass(new LetterFilter()).thenRepeat()
                .ifEquals('_').thenRepeat()
                .orElse(new KeywordDecorator(new AcceptorStrategy(ID_MV)));

    private static final LexicalNode classIdentifierAcceptor =
            new LexicalNodeBuilder("Matches any letter, digit or underscore, then return ID_CLS token")
                .ifPass(new DigitFilter()).thenRepeat()
                .ifPass(new LetterFilter()).thenRepeat()
                .ifEquals('_').thenRepeat()
                .orElse(new KeywordDecorator(new AcceptorStrategy(ID_CLS)));

    // -------- //

    // ---------- LITERALS ------------ //


    private static final LexicalNode digitAcceptor =
            new LexicalNodeBuilder("Stores all digits and then return INT token")
                .ifPass(new DigitFilter()).thenRepeat()
                .orElseReturn(INT);

    // ------ Strings -------- //

    private static final LexicalNode stringSingleLineEscape =
            new LexicalNodeBuilder("Escapes any character and goes back")
                .orElseThrow("Literal String no cerrado");

    private static final LexicalNode stringMultiLineEscape =
            new LexicalNodeBuilder("Escapes any character and goes back")
                .orElseThrow("Literal String no cerrado");


    private static final LexicalNode stringMultilineClosingThirdQuote =
            new LexicalNodeBuilder("Tries to match the third closing double quote")
                .ifEquals('"').thenMoveToNodeWith(new SubLexemeStrategy(STRING, 3))
                .orElseThrow("Literal String no cerrado");

    private static final LexicalNode stringMultilineClosingSecondQuote =
            new LexicalNodeBuilder("Tries to match the second closing double quote")
                .ifEquals('"').thenMoveTo(stringMultilineClosingThirdQuote)
                .orElseThrow("Literal String no cerrado");

    private static final LexicalNode stringMultilineChars =
            new LexicalNodeBuilder("Stores every character but tries to close String on double quote character")
                .ifEquals('"').thenMoveTo(stringMultilineClosingSecondQuote)
                .ifEquals('\\').thenMoveTo(stringMultiLineEscape)
                .ifAnyExcept('"').thenRepeat()
                .orElseThrow("Literal String no cerrado");

    private static final LexicalNode emptyStringOrMultiline =
            new LexicalNodeBuilder("Tries to match a multiline String, else, it accepts an empty String")
                .ifEquals('"').thenMoveTo(stringMultilineChars)
                .orElse(new SubLexemeStrategy(STRING, 1));

    private static final LexicalNode lineStringNode =
            new LexicalNodeBuilder("Assumes this is a line String, stores all characters except invalid ones")
                .ifEquals('"').thenMoveToNodeWith(new SubLexemeStrategy(STRING, 1))
                .ifEquals('\\').thenMoveTo(stringSingleLineEscape)
                .ifAnyExcept('"', '\n').thenRepeat()
                .ifEquals('\n').thenThrow("El caracter de salto de linea no es valido dentro de una String en linea")
                .orElseThrow("Literal String no cerrado");

    private static final LexicalNode stringInitialNode =
            new LexicalNodeBuilder("On a closing double quote will try to match multiline String, else, stores characters")
                .ifEquals('"').thenMoveTo(emptyStringOrMultiline)
                .ifEquals('\\').thenMoveTo(stringSingleLineEscape)
                .ifAnyExcept('"', '\n').thenMoveTo(lineStringNode)
                .ifEquals('\n').thenThrow("El caracter de salto de linea no es valido dentro de un literal String")
                .orElseThrow("Literal String no cerrado");

    // ---- //

    private static final LexicalNode charClosingNode =
            new LexicalNodeBuilder("Tries to match the closing character quote")
                .ifEquals('\'').thenMoveToNodeWith(new SubLexemeStrategy(CHAR, 1))
                .orElseThrow("Literal char no cerrado");

    private static final LexicalNode charSpecialCharacterOpen =
            new LexicalNodeBuilder("Stores the next character representing a special character")
                .ifEquals('\n').thenThrow("El caracter de salto de linea no es un literal char valido")
                .ifAnyExcept('\n').thenMoveTo(charClosingNode)
                .orElseThrow("Literal char mal formado, falta caracter despues de barra invertida.");

    private static final LexicalNode charInitialNode =
            new LexicalNodeBuilder("Tries to match a valid character value")
                .ifEquals('\\').thenMoveTo(charSpecialCharacterOpen)
                .ifAnyExcept('\\', '\n', '\'').thenMoveTo(charClosingNode)
                .ifEquals('\'').thenThrow("El caracter ' no es un caracter valido")
                .ifEquals('\n').thenThrow("El caracter de salto de linea no es un literal char valido")
                .orElseThrow("Literal char no válido.");

    // -------- //

    // ---------- COMMENTS ------------ //

    private static final LexicalNode singleLineCommentAcceptor =
            new LexicalNodeBuilder("Skips all characters and returns upon new line character")
                .ifAnyExcept('\n').thenRepeat()
                .orElseAccept();

    private static final LexicalNode multiLineCommentClosingNode =
            new LexicalNodeBuilder("Tries to match the final comment character, or else returns to multiline comment node")
                .ifEquals('/').thenAccept()
                .orElseThrow("Comentario multi-linea no cerrado");

    private static final LexicalNode multiLineCommentInitialNode =
            new LexicalNodeBuilder("Skips all characters except '*' that may mark the end of comment")
                .ifEquals('*').thenMoveTo(multiLineCommentClosingNode)
                .ifAnyExcept('*').thenRepeat()
                .orElseThrow("Comentario multi-linea no cerrado");

    private static final LexicalNode commentsOrDivision =
            new LexicalNodeBuilder("Tries to match a single or multi-line comment, or else match division operator")
                .ifEquals('/').thenMoveTo(singleLineCommentAcceptor)
                .ifEquals('*').thenMoveTo(multiLineCommentInitialNode)
                .orElseReturn(OP_DIV);

    // -------- //

    // ---------- INITIAL NODES ------------ //

    private static final LexicalNode initialNode = new LexicalNodeBuilder("Initial node")
                // Punctuation
                .ifEquals('.').thenReturn(P_DOT)
                .ifEquals(',').thenReturn(P_COMMA)
                .ifEquals(':').thenReturn(P_COLON)
                .ifEquals(';').thenReturn(P_SEMICOLON)
                .ifEquals('(').thenReturn(P_PAREN_OPEN)
                .ifEquals(')').thenReturn(P_PAREN_CLOSE)
                .ifEquals('{').thenReturn(P_BRCKT_OPEN)
                .ifEquals('}').thenReturn(P_BRCKT_CLOSE)

                // Operators
                .ifEquals('+').thenMoveTo(plusAcceptor)
                .ifEquals('-').thenMoveTo(minusAcceptor)
                .ifEquals('*').thenReturn(OP_MULT)
                .ifEquals('%').thenReturn(OP_MOD)
                .ifEquals('|').thenMoveTo(orOpMiddle)
                .ifEquals('&').thenMoveTo(andOpMiddle)
                .ifEquals('<').thenMoveTo(lessAcceptor)
                .ifEquals('>').thenMoveTo(greaterAcceptor)
                .ifEquals('!').thenMoveTo(notOperatorAcceptor)
                .ifEquals('=').thenMoveTo(assignAcceptor)

                // Identifiers
                .ifPass(new LowercaseLetterFilter()).thenMoveTo(methodVariableIdentifierAcceptor)
                .ifPass(new UppercaseLetterFilter()).thenMoveTo(classIdentifierAcceptor)

                // Literals
                .ifPass(new DigitFilter()).thenMoveTo(new IntegerLexicalNode("Entero demasiado largo"))
                .ifEquals('"').thenMoveTo(stringInitialNode)
                .ifEquals('\'').thenMoveTo(charInitialNode)

                // Comments, whitespace and division operator
                .ifPass(new WhitespaceFilter()).thenAccept()
                .ifEquals('/').thenMoveTo(commentsOrDivision)

                .orElse(new InitialNodeStrategy("Caracter ilegal"));

    // -------- //

    private void buildCircularReferences(){
        NodeBranch backToChars = new DefaultNodeBranch(new AnyCharacterExceptFilter('"'));
        backToChars.setNextNode(stringMultilineChars);
        stringMultilineClosingThirdQuote.addBranch(backToChars);
        stringMultilineClosingSecondQuote.addBranch(backToChars);

        NodeBranch backToCommentsChars = new DefaultNodeBranch(new AnyCharacterExceptFilter('/'));
        backToCommentsChars.setNextNode(multiLineCommentInitialNode);
        multiLineCommentClosingNode.addBranch(backToCommentsChars);

        NodeBranch backToSingleLineString = new DefaultNodeBranch(new AnyCharacterFilter());
        backToSingleLineString.setNextNode(lineStringNode);
        stringSingleLineEscape.addBranch(backToSingleLineString);

        NodeBranch backToMultiLineString = new DefaultNodeBranch(new AnyCharacterFilter());
        backToMultiLineString.setNextNode(stringMultilineChars);
        stringMultiLineEscape.addBranch(backToMultiLineString);

    }

}
