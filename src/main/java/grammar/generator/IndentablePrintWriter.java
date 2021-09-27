package grammar.generator;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Locale;

/**
 * A simple wrapper of PrintWriter that adds the ability of indent every call to print methods
 */
public class IndentablePrintWriter extends PrintWriter {

    private int indentation = 0;

    public IndentablePrintWriter(Writer out) {
        super(out);
    }

    public IndentablePrintWriter(Writer out, int baseIndentation){
        super(out);
        if (baseIndentation < 0) throw new IllegalArgumentException("Indentation cannot be negative");
        indentation = baseIndentation;
    }

    /**
     * Indents the following calls to print methods. This adds to the current indentation level.
     * Beware that this will not affect other methods like {@link #write(String)}.
     */
    public void indent(){
        indentation++;
    }

    /**
     * Unindents the following calls to print methods. This lowers the current indentation level.
     * If the current indentation level is zero, then this won't have any effect, since indentation cannot be negative.
     * Beware that this will not affect other methods like {@link #write(String)}.
     */
    public void unindent(){
        indentation = Math.max(0, indentation-1);
    }
    
    private String printIndentation(){
        char[] out = new char[indentation];
        Arrays.fill(out, '\t');
        return new String(out);
    }

    @Override
    public void print(boolean b) {
        super.print(printIndentation() +  b);
    }

    @Override
    public void print(char c) {
        super.print(printIndentation() +  c);
    }

    @Override
    public void print(int i) {
        super.print(printIndentation() +  i);
    }

    @Override
    public void print(long l) {
        super.print(printIndentation() +  l);
    }

    @Override
    public void print(float f) {
        super.print(printIndentation() +  f);
    }

    @Override
    public void print(double d) {
        super.print(printIndentation() +  d);
    }

    @Override
    public void print(char[] s) {
        super.print(printIndentation() + Arrays.toString(s));
    }

    @Override
    public void print(String s) {
        super.print(printIndentation() +  s);
    }

    @Override
    public void print(Object obj) {
        super.print(printIndentation() +  obj);
    }

    @Override
    public PrintWriter printf(String format, Object... args) {
        return super.printf(printIndentation() +  format, args);
    }

    @Override
    public PrintWriter printf(Locale l, String format, Object... args) {
        return super.printf(l, printIndentation() + format, args);
    }
}
