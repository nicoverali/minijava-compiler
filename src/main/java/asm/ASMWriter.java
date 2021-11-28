package asm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;

public class ASMWriter {

    private int indentation = 0;
    private final FileWriter writer;

    public ASMWriter(String file) throws IOException {
        this.writer = new FileWriter(file);
    }

    public void indent() {
        indentation++;
    }

    public void unindent() {
        indentation--;
    }

    public void writeln(String text) throws UncheckedIOException{
        try {
            String indentationStr = "\t".repeat(indentation);
            writer.write(indentationStr + text + "\n");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void writeln() throws UncheckedIOException {
        writeln("");
    }

    public void writeln(String format, Object... args){
        writeln(String.format(format, args));
    }

    public void flush(){
        try {
            writer.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
