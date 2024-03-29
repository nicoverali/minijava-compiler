package lexical.analyzer;

import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.AutomataToken;
import lexical.automata.LexicalNode;
import lexical.automata.branch.KeywordBranch;
import lexical.automata.filter.*;
import lexical.automata.node.builder.OmitterNodeBuilder;
import lexical.automata.node.builder.TokenizerAcceptorBuilder;
import lexical.automata.node.builder.TokenizerNodeBuilder;
import lexical.automata.node.strategy.TokenizeInitialStrategy;

import java.util.Optional;

import static lexical.TokenType.*;

public class MiniJavaLexicalAnalyzer implements LexicalAnalyzer{


    private final LexicalAnalyzerInitialNode initialNode;

    public MiniJavaLexicalAnalyzer(SourceCodeReader reader) {
        initialNode = new LexicalAnalyzerInitialNode(reader, omitterInitialNode, tokenizerInitialNode);
    }

    @Override
    public Optional<Token> getNextToken() throws LexicalException {
        return initialNode.getNextToken();
    }


    // ---------- PUNCTUATION ------------ //

    private static final LexicalNode<AutomataToken> dotAcceptor = new TokenizerAcceptorBuilder(P_DOT).build();

    private static final LexicalNode<AutomataToken> commaAcceptor = new TokenizerAcceptorBuilder(P_COMMA).build();

    private static final LexicalNode<AutomataToken> semicolonAcceptor = new TokenizerAcceptorBuilder(P_SEMICOLON).build();

    private static final LexicalNode<AutomataToken> parenOpenAcceptor = new TokenizerAcceptorBuilder(P_PAREN_OPEN).build();

    private static final LexicalNode<AutomataToken> parenCloseAcceptor = new TokenizerAcceptorBuilder(P_PAREN_CLOSE).build();

    private static final LexicalNode<AutomataToken> bracketOpenAcceptor = new TokenizerAcceptorBuilder(P_BRCKT_OPEN).build();

    private static final LexicalNode<AutomataToken> bracketCloseAcceptor = new TokenizerAcceptorBuilder(P_BRCKT_CLOSE).build();

    // -------- //

    // ---------- ASSIGNMENT ------------ //

    private static final LexicalNode<AutomataToken> assignPlusAcceptor = new TokenizerAcceptorBuilder(ASSIGN_PLUS).build();

    private static final LexicalNode<AutomataToken> assignMinusAcceptor = new TokenizerAcceptorBuilder(ASSIGN_MINUS).build();

    // -------- //

    // ---------- ASSIGNMENT AND EQUALS ------------ //

    private static final LexicalNode<AutomataToken> equalAcceptor = new TokenizerAcceptorBuilder(OP_EQ).build();

    private static final LexicalNode<AutomataToken> assignAcceptor =
            new TokenizerNodeBuilder("Tries to match equal token or else returns assign token")
                .ifEquals('=').storeInLexeme().thenMoveTo(equalAcceptor)
                .orElseReturnToken(ASSIGN);

    // -------- //

    // ---------- OPERATORS ------------ //

    private static final LexicalNode<AutomataToken> plusAcceptor =
            new TokenizerNodeBuilder("Tries to match plus_assign token or else return plus token")
                .ifEquals('=').storeInLexeme().thenMoveTo(assignPlusAcceptor)
                .orElseReturnToken(OP_PLUS);

    private static final LexicalNode<AutomataToken> minusAcceptor =
            new TokenizerNodeBuilder("Tries to match minus_assign token or else return plus token")
                .ifEquals('=').storeInLexeme().thenMoveTo(assignMinusAcceptor)
                .orElseReturnToken(OP_MINUS);

    private static final LexicalNode<AutomataToken> multiplicationAcceptor = new TokenizerAcceptorBuilder(OP_MULT).build();

    private static final LexicalNode<AutomataToken> divisionAcceptor = new TokenizerAcceptorBuilder(OP_DIV).build();

    private static final LexicalNode<AutomataToken> moduleAcceptor = new TokenizerAcceptorBuilder(OP_MOD).build();

    private static final LexicalNode<AutomataToken> andAcceptor = new TokenizerAcceptorBuilder(OP_AND).build();

    private static final LexicalNode<AutomataToken> andOpMiddle =
            new TokenizerNodeBuilder("Tries to match the second & of AND token")
                .ifEquals('&').storeInLexeme().thenMoveTo(andAcceptor)
                .orElseThrow("Operador AND mal formado. Falta un caracter '&'");

    private static final LexicalNode<AutomataToken> orAcceptor = new TokenizerAcceptorBuilder(OP_OR).build();

    private static final LexicalNode<AutomataToken> orOpMiddle =
            new TokenizerNodeBuilder("Tries to match the second | of OR token")
                .ifEquals('|').storeInLexeme().thenMoveTo(orAcceptor)
                .orElseThrow("Operador OR mal formado. Falta un caracter '|'");

    private static final LexicalNode<AutomataToken> notEqualAcceptor = new TokenizerAcceptorBuilder(OP_NOTEQ).build();

    private static final LexicalNode<AutomataToken> notOperatorAcceptor =
            new TokenizerNodeBuilder("Tries to match not_equal token or else return NOT token")
                .ifEquals('=').storeInLexeme().thenMoveTo(notEqualAcceptor)
                .orElseReturnToken(OP_NOT);

    private static final LexicalNode<AutomataToken> lessOrEqAcceptor = new TokenizerAcceptorBuilder(OP_LTE).build();

    private static final LexicalNode<AutomataToken> lessAcceptor =
            new TokenizerNodeBuilder("Tries to match less_or_equal token or else return LESS token")
                .ifEquals('=').storeInLexeme().thenMoveTo(lessOrEqAcceptor)
                .orElseReturnToken(OP_LT);

    private static final LexicalNode<AutomataToken> greaterOrEqAcceptor = new TokenizerAcceptorBuilder(OP_GTE).build();

    private static final LexicalNode<AutomataToken> greaterAcceptor =
            new TokenizerNodeBuilder("Tries to match grater_or_equal token or else return GREATER token")
                .ifEquals('=').storeInLexeme().thenMoveTo(greaterOrEqAcceptor)
                .orElseReturnToken(OP_GT);

    // -------- //

    // ---------- IDENTIFIERS ------------ //

    private static final LexicalNode<AutomataToken> methodVariableIdentifierAcceptor =
            new TokenizerNodeBuilder("Matches any letter, digit or underscore, then return ID_MV token")
                .ifPass(new DigitFilter()).storeInLexeme().thenRepeat()
                .ifPass(new LetterFilter()).storeInLexeme().thenRepeat()
                .ifEquals('_').storeInLexeme().thenRepeat()
                .orElseReturnToken(ID_MV );

    private static final LexicalNode<AutomataToken> classIdentifierAcceptor =
            new TokenizerNodeBuilder("Matches any letter, digit or underscore, then return ID_CLS token")
                .ifPass(new DigitFilter()).storeInLexeme().thenRepeat()
                .ifPass(new LetterFilter()).storeInLexeme().thenRepeat()
                .ifEquals('_').storeInLexeme().thenRepeat()
                .orElseReturnToken(ID_CLS);

    // -------- //

    // ---------- LITERALS ------------ //


    private static final LexicalNode<AutomataToken> digitAcceptor =
            new TokenizerNodeBuilder("Stores all digits and then return INT token")
                .ifPass(new DigitFilter()).storeInLexeme().thenRepeat()
                .orElseReturnToken(INT);

    // ------ Strings -------- //

    private static final LexicalNode<AutomataToken> stringAcceptor = new TokenizerAcceptorBuilder(STRING).build();

    private static final LexicalNode<AutomataToken> stringMultilineClosingThirdQuote =
            new TokenizerNodeBuilder("Tries to match the first closing double quote")
                .ifEquals('"').thenMoveTo(stringAcceptor)
                .orElseReject();

    private static final LexicalNode<AutomataToken> stringMultilineClosingSecondQuote =
            new TokenizerNodeBuilder("Tries to match the second closing double quote")
                .ifEquals('"').thenMoveTo(stringMultilineClosingThirdQuote)
                .orElseReject();

    private static final LexicalNode<AutomataToken> stringMultilineClosingFirstQuote =
            new TokenizerNodeBuilder("Tries to match the first closing double quote")
                .ifEquals('"').thenMoveTo(stringMultilineClosingSecondQuote)
                .orElseReject();

    private static final LexicalNode<AutomataToken> stringMultilineChars =
            new TokenizerNodeBuilder("Stores every character but tries to close String on new line character")
                .ifEquals('\n').thenTry(stringMultilineClosingFirstQuote, 4)
                .ifAny().storeInLexeme().thenRepeat()
                .orElseThrow("Literal String no cerrado");

    private static final LexicalNode<AutomataToken> stringMultilineOpeningNewLine =
            new TokenizerNodeBuilder("Tries to match the opening new line of multiline Strings")
                .ifEquals('\n').thenMoveTo(stringMultilineChars)
                .orElseReject();

    private static final LexicalNode<AutomataToken> emptyStringOrMultiline =
            new TokenizerNodeBuilder("Tries to match a multiline String, else, it accepts an empty String")
                .ifEquals('"').thenTry(stringMultilineOpeningNewLine, 2)
                .orElseReturnToken(STRING);

    private static final LexicalNode<AutomataToken> lineStringNode =
            new TokenizerNodeBuilder("Assumes this is a line String, stores all characters except invalid ones")
                .ifEquals('"').thenMoveTo(stringAcceptor)
                .ifAnyExcept('"', '\n').storeInLexeme().thenRepeat()
                .ifEquals('\n').thenThrow("El caracter de salto de linea no es valido dentro de una String en linea")
                .orElseThrow("Literal String no cerrado");

    private static final LexicalNode<AutomataToken> stringInitialNode =
            new TokenizerNodeBuilder("On a closing double quote will try to match multiline String, else, stores characters")
                .ifEquals('"').thenMoveTo(emptyStringOrMultiline)
                .ifAnyExcept('"', '\n').storeInLexeme().thenMoveTo(lineStringNode)
                .ifEquals('\n').thenThrow("El caracter de salto de linea no es valido dentro de un literal String")
                .orElseThrow("Literal String no cerrado");

    // ---- //

    private static final LexicalNode<AutomataToken> charAcceptor = new TokenizerAcceptorBuilder(CHAR).build();

    private static final LexicalNode<AutomataToken> charClosingNode =
            new TokenizerNodeBuilder("Tries to match the closing character quote")
                .ifEquals('\'').thenMoveTo(charAcceptor)
                .orElseThrow("Literal char no cerrado");

    private static final LexicalNode<AutomataToken> charSpecialCharacterOpen =
            new TokenizerNodeBuilder("Stores the next character representing a special character")
                .ifEquals('\n').thenThrow("El caracter de salto de linea no es un literal char valido")
                .ifAnyExcept('\n').storeInLexeme().thenMoveTo(charClosingNode)
                .orElseThrow("Literal char mal formado, falta caracter despues de barra invertida.");

    private static final LexicalNode<AutomataToken> charInitialNode =
            new TokenizerNodeBuilder("Tries to match a valid character value")
                .ifEquals('\\').storeInLexeme().thenMoveTo(charSpecialCharacterOpen)
                .ifAnyExcept('\\', '\n', '\'').storeInLexeme().thenMoveTo(charClosingNode)
                .ifEquals('\n').thenThrow("El caracter de salto de linea no es un literal char valido")
                .orElseThrow("Literal char no válido.");

    // -------- //

    // ---------- COMMENTS ------------ //

    private static final LexicalNode<Boolean> singleLineCommentAcceptor =
            new OmitterNodeBuilder("Skips all characters and returns upon new line character")
                .ifAnyExcept('\n').thenRepeat()
                .orElseAccept();

    private static final LexicalNode<Boolean> multiLineCommentAcceptor =
            new OmitterNodeBuilder("Accepts the multi-line comment and simply returns").orElseAccept();

    private static final LexicalNode<Boolean> multiLineCommentClosingNode =
            new OmitterNodeBuilder("Tries to match the final comment character, or else throws exception")
                .ifEquals('/').thenMoveTo(multiLineCommentAcceptor)
                .orElseReject();

    private static final LexicalNode<Boolean> multiLineCommentInitialNode =
            new OmitterNodeBuilder("Skips all characters except '*' that may mark the end of comment")
                .ifEquals('*').thenTry(multiLineCommentClosingNode,2)
                .ifAny().thenRepeat()
                .orElseThrow("Comentario multi-linea no cerrado.");

    private static final LexicalNode<Boolean> commentsInitialNode =
            new OmitterNodeBuilder("Tries to match a single or multi-line comment, or else just return")
                .ifEquals('/').thenMoveTo(singleLineCommentAcceptor)
                .ifEquals('*').thenMoveTo(multiLineCommentInitialNode)
                .orElseReject();

    // -------- //

    // ---------- INITIAL NODES ------------ //

    private static final LexicalNode<Boolean> omitterInitialNode =
            new OmitterNodeBuilder("Skips comments and whitespaces")
                .ifPass(new WhitespaceFilter()).thenRepeat()
                .ifEquals('/').thenTry(commentsInitialNode, 2)
                .orElseAccept();

    private static final LexicalNode<AutomataToken> tokenizerInitialNode =
            new TokenizerNodeBuilder("Tokenize next characters")
                .ifEquals('.').storeInLexeme().thenMoveTo(dotAcceptor)
                .ifEquals(',').storeInLexeme().thenMoveTo(commaAcceptor)
                .ifEquals(';').storeInLexeme().thenMoveTo(semicolonAcceptor)
                .ifEquals('(').storeInLexeme().thenMoveTo(parenOpenAcceptor)
                .ifEquals(')').storeInLexeme().thenMoveTo(parenCloseAcceptor)
                .ifEquals('{').storeInLexeme().thenMoveTo(bracketOpenAcceptor)
                .ifEquals('}').storeInLexeme().thenMoveTo(bracketCloseAcceptor)

                .ifEquals('+').storeInLexeme().thenMoveTo(plusAcceptor)
                .ifEquals('-').storeInLexeme().thenMoveTo(minusAcceptor)
                .ifEquals('*').storeInLexeme().thenMoveTo(multiplicationAcceptor)
                .ifEquals('/').storeInLexeme().thenMoveTo(divisionAcceptor)
                .ifEquals('%').storeInLexeme().thenMoveTo(moduleAcceptor)
                .ifEquals('|').storeInLexeme().thenMoveTo(orOpMiddle)
                .ifEquals('&').storeInLexeme().thenMoveTo(andOpMiddle)
                .ifEquals('<').storeInLexeme().thenMoveTo(lessAcceptor)
                .ifEquals('>').storeInLexeme().thenMoveTo(greaterAcceptor)
                .ifEquals('!').storeInLexeme().thenMoveTo(notOperatorAcceptor)
                .ifEquals('=').storeInLexeme().thenMoveTo(assignAcceptor)

                .ifPass(new LowercaseLetterFilter()).storeInLexeme().decorate(new KeywordBranch()).thenMoveTo(methodVariableIdentifierAcceptor)
                .ifPass(new UppercaseLetterFilter()).storeInLexeme().decorate(new KeywordBranch()).thenMoveTo(classIdentifierAcceptor)

                .ifPass(new DigitFilter()).storeInLexeme().thenMoveTo(digitAcceptor)
                .ifEquals('"').thenMoveTo(stringInitialNode)
                .ifEquals('\'').thenMoveTo(charInitialNode)
                .orElse(new TokenizeInitialStrategy("Caracter ilegal"));

    // -------- //


}
