package grammar.generator;

import com.google.common.base.Verify;
import grammar.GrammarTerm;
import lexical.TokenType;

import java.util.HashMap;
import java.util.Map;

import static lexical.TokenType.*;

public class GrammarTokenMapper {

    private static final Map<String, TokenType> map = new HashMap<>();
    static {
        map.put("idClase", ID_CLS);
        map.put("idMetVar", ID_MV);

        map.put("intLiteral", INT);
        map.put("charLiteral", CHAR);
        map.put("stringLiteral", STRING);

        map.put(".", P_DOT);
        map.put(",", P_COMMA);
        map.put(";", P_SEMICOLON);
        map.put("(", P_PAREN_OPEN);
        map.put(")", P_PAREN_CLOSE);
        map.put("{", P_BRCKT_OPEN);
        map.put("}", P_BRCKT_CLOSE);

        map.put("<", OP_LT);
        map.put("<=", OP_LTE);
        map.put(">", OP_GT);
        map.put(">=", OP_GTE);
        map.put("==", OP_EQ);
        map.put("!=", OP_NOTEQ);
        map.put("+", OP_PLUS);
        map.put("-", OP_MINUS);
        map.put("*", OP_MULT);
        map.put("/", OP_DIV);
        map.put("%", OP_MOD);
        map.put("!", OP_NOT);
        map.put("&&", OP_AND);
        map.put("||", OP_OR);

        map.put("=", ASSIGN);
        map.put("++", ASSIGN_INCR);
        map.put("--", ASSIGN_DECR);

        map.put("class", K_CLASS);
        map.put("extends", K_EXTENDS);
        map.put("static", K_STATIC);
        map.put("dynamic", K_DYNAMIC);
        map.put("void", K_VOID);
        map.put("boolean", K_BOOLEAN);
        map.put("char", K_CHAR);
        map.put("int", K_INT);
        map.put("String", K_STRING);
        map.put("public", K_PUBLIC);
        map.put("private", K_PRIVATE);
        map.put("if", K_IF);
        map.put("else", K_ELSE);
        map.put("for", K_FOR);
        map.put("return", K_RETURN);
        map.put("this", K_THIS);
        map.put("new", K_NEW);
        map.put("null", K_NULL);
        map.put("true", K_TRUE);
        map.put("false", K_FALSE);





        map.put("EOF", EOF);
    }

    public static TokenType map(String term){
        return Verify.verifyNotNull(map.get(term), "The given term ("+term+") does not exist in this map");
    }

    public static TokenType map(GrammarTerm term){
        if (term.isNonTerminal()) throw new IllegalArgumentException("A non terminal can't be mapped to a Token");
        return Verify.verifyNotNull(map.get(term.toString()), "The given term ("+term.getName()+") does not exist in this map");
    }

}
