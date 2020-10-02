package lexical;

public enum TokenType {

    ID_CLS("idCls"),
    ID_MV("idMv"),
    INT("int"),
    CHAR("char"),
    STRING("str"),
    P_DOT("punctuation_dot"),
    P_COMMA("punctuation_comma"),
    P_SEMICOLON("punctuation_semicolon"),
    P_PAREN_OPEN("punctuation_parenthesis_open"),
    P_PAREN_CLOSE("punctuation_parenthesis_close"),
    P_BRCKT_OPEN("punctuation_bracket_open"),
    P_BRCKT_CLOSE("punctuation_bracket_close"),
    OP_LT("operator_less"),
    OP_LTE("operator_less_or_equals"),
    OP_GT("operator_greater"),
    OP_GTE("operator_greater_or_equals"),
    OP_EQ("operator_equals"),
    OP_NOTEQ("operator_not_equals"),
    OP_PLUS("operator_plus"),
    OP_MINUS("operator_minus"),
    OP_MULT("operator_multiplication"),
    OP_DIV("operator_division"),
    OP_MOD("operator_module"),
    OP_NOT("operator_not"),
    OP_AND("operator_and"),
    OP_OR("operator_or"),
    ASSIGN("assign"),
    ASSIGN_PLUS("assign_plus"),
    ASSIGN_MINUS("assign_minus"),
    ASSIGN_MULT("assign_multiplication"),
    ASSIGN_DIV("assign_division"),
    ASSIGN_MOD("assign_module");

    private final String tokenName;

    TokenType(String tokenName) {
        this.tokenName = tokenName;
    }

    @Override
    public String toString() {
        return tokenName;
    }
}
