package semantic.symbol.finder;

import semantic.symbol.ClassSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.Optional;

public class AncestorFinder {

    private static final SymbolTable ST = SymbolTable.getInstance();

    /**
     * Checks whether the first reference or class is ancestor of the second one. That means
     * that the second one inherits from the first.
     *
     * @param ancestorRef ancestor reference or class
     * @param classRef class that inherits from ancestor
     * @return true if the first reference or class is ancestor of the second one, false otherwise
     * @throws IllegalArgumentException if any of the given references point to undeclared classes
     * @throws IllegalStateException if any class has a parent that is undeclared
     */
    public static boolean isAncestor(ReferenceType ancestorRef, ReferenceType classRef) throws IllegalArgumentException, IllegalStateException{
        Optional<ClassSymbol> ancestorSym = ST.getClass(ancestorRef);
        Optional<ClassSymbol> classSym = ST.getClass(classRef);

        if (ancestorSym.isEmpty()) throw new IllegalArgumentException("Undeclared referenced class");
        if (classSym.isEmpty()) throw new IllegalArgumentException("Undeclared referenced class");

        boolean isAncestor = false;
        Optional<ReferenceType> currentAncestor = classSym.get().getParentClass();
        while (currentAncestor.isPresent()){
            if (currentAncestor.get().equals(ancestorRef)){
                isAncestor = true;
                break;
            }

            Optional<ClassSymbol> currentAncestorSym = ST.getClass(currentAncestor.get());
            if (currentAncestorSym.isEmpty()) throw new IllegalStateException("Undeclared parent");
            currentAncestor = currentAncestorSym.get().getParentClass();
        }

        return isAncestor;
    }

    /**
     * Checks if the two given types are compatible or not. Two types will be compatible if one of them is
     * an ancestor of the other one.
     *
     * @param typeA type A
     * @param typeB type B
     * @return true if the types are compatible, false otherwise
     * @see #isAncestor(ReferenceType, ReferenceType)
     */
    public static boolean areTypesCompatible(ReferenceType typeA, ReferenceType typeB) {
        return isAncestor(typeA, typeB) || isAncestor(typeB, typeA);
    }

}
