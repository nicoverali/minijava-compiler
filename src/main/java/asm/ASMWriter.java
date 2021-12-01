package asm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;

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

    public void writelnLabeled(String label, String text) throws UncheckedIOException{
        try {
            String indentationStr = "\t".repeat(indentation);
            writer.write(label + ":" + indentationStr + text + "\n");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void writelnLabeled(String label) throws UncheckedIOException {
        writelnLabeled(label, "");
    }

    public void writelnLabeled(String label, String format, Object... args){
        writelnLabeled(label, String.format(format, args));
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
