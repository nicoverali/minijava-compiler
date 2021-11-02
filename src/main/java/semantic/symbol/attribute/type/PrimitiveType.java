package semantic.symbol.attribute.type;

import lexical.Token;

public class PrimitiveType extends Type{

    public static final String INT = "int";
    public static final String STRING = "String";
    public static final String BOOLEAN = "boolean";
    public static final String CHAR = "char";

    public static PrimitiveType INT(Token token){
        return new PrimitiveType(token, INT);
    }

    public static PrimitiveType INT(){
        return new PrimitiveType(null, INT);
    }

    public static PrimitiveType STRING(Token token){
        return new PrimitiveType(token, STRING);
    }

    public static PrimitiveType STRING(){
        return new PrimitiveType(null, STRING);
    }

    public static PrimitiveType BOOLEAN(Token token){
        return new PrimitiveType(token, BOOLEAN);
    }

    public static PrimitiveType BOOLEAN(){
        return new PrimitiveType(null, BOOLEAN);
    }

    public static PrimitiveType CHAR(Token token){
        return new PrimitiveType(token, CHAR);
    }

    public static PrimitiveType CHAR(){
        return new PrimitiveType(null, CHAR);
    }

    private PrimitiveType(Token token, String name) {
        super(token, name);
    }

    @Override
    public boolean conforms(Type other) {
        return other.getValue().equals(this.getValue());
    }
}
