package semantic;

import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

public interface Variable {

    /**
     * @return the {@link Type} of this variable
     */
    Type getType();

    /**
     * @return the {@link NameAttribute} of this variable
     */
    NameAttribute getNameAttribute();

}
