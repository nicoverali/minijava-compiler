package semantic.symbol.attribute.type;

import lexical.Token;

public class VoidType extends Type{

    public static final String VOID = "void";

    public static VoidType VOID(Token token){
        return new VoidType(token);
    }

    public static VoidType VOID(){
        return new VoidType(null);
    }

    public VoidType(Token token) {
        super(token, VOID);
    }

    @Override
    public boolean conforms(Type other) {
        return other.equals(VOID());
    }
}
