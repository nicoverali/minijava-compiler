package semantic.symbol.finder;

import semantic.symbol.attribute.type.Type;

import java.util.List;

public class ParametersValidation {

    public static boolean conforms(List<Type> ourParameters, List<Type> theirParameters){
        if (ourParameters.size() != theirParameters.size()) return false;
        for (int i = 0; i < ourParameters.size(); i++) {
            if (!ourParameters.get(i).conforms(theirParameters.get(i))){
                return false;
            }
        }
        return true;
    }

}
