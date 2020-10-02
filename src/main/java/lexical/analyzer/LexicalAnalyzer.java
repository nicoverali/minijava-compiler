package lexical.analyzer;

import lexical.LexicalException;
import lexical.Token;

public interface LexicalAnalyzer {

    Token getNextToken() throws LexicalException;

}
