package io.code.reader;

import io.code.ScannerSourceCodeReader;

import java.util.*;

class ScannerSourceCodeReaderTest implements SourceCodeReaderTest<ScannerSourceCodeReader> {


    @Override
    public ScannerSourceCodeReader createSourceCodeReader(String... lines) {
        String source = String.join("\n", lines);
        return new ScannerSourceCodeReader(new Scanner(source));
    }
}