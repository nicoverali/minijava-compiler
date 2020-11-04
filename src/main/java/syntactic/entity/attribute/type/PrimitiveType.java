package syntactic.entity.attribute.type;

import lexical.Token;

public class PrimitiveType extends Type{


    public static PrimitiveType INT(Token token){
        return new PrimitiveType(token, "int");
    }

    public static PrimitiveType INT(){
        return new PrimitiveType(null, "int");
    }

    public static PrimitiveType STRING(Token token){
        return new PrimitiveType(token, "String");
    }

    public static PrimitiveType STRING(){
        return new PrimitiveType(null, "String");
    }

    public static PrimitiveType BOOLEAN(Token token){
        return new PrimitiveType(token, "boolean");
    }

    public static PrimitiveType BOOLEAN(){
        return new PrimitiveType(null, "boolean");
    }

    public static PrimitiveType CHAR(Token token){
        return new PrimitiveType(token, "char");
    }

    public static PrimitiveType CHAR(){
        return new PrimitiveType(null, "char");
    }

    private PrimitiveType(Token token, String name) {
        super(token, name);
    }
}
