package semantic.symbol.attribute.type;

import lexical.Token;

public class NullType extends Type{

    public static final String NULL = "null";

    public static NullType NULL(){
        return new NullType(null, NULL);
    }

    public static NullType NULL(Token nullToken){
        return new NullType(nullToken, NULL);
    }

    private NullType(Token token, String name) {
        super(token, name);
    }

    @Override
    public boolean conforms(Type other) {
        return other instanceof ReferenceType;
    }
}
