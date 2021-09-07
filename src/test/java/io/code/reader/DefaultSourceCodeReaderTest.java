package io.code.reader;

import io.code.CodeLine;
import io.code.CodeLineFactory;
import io.code.DefaultCodeLineFactory;
import util.Iterables;

import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

class DefaultSourceCodeReaderTest implements SourceCodeReaderTest<DefaultSourceCodeReader> {


    @Override
    public DefaultSourceCodeReader createSourceCodeReader(String... lines) {
        return new DefaultSourceCodeReader(new CodeLineReaderMock(lines));
    }

    /**
     * We assume <code>DefaultCodeLine</code> works correctly because its tested in {@link io.code.DefaultCodeLineTest}
     *
     * @see io.code.DefaultCodeLineTest
     */
    class CodeLineReaderMock implements CodeLinesReader{

        private final Queue<CodeLine> lines = new LinkedList<>();

        public CodeLineReaderMock(String... lines) {
            CodeLineFactory lineFactory = new DefaultCodeLineFactory();
            for (int i = 0; i < lines.length; i++){
                String lineString = i+1 == lines.length
                        ? lines[i]
                        : lines[i]+'\n';
                CodeLine line = lineFactory.create(lineString, i);
                this.lines.add(line);
            }
        }

        @Override
        public Optional<CodeLine> next() throws UncheckedIOException {
            return Optional.ofNullable(lines.poll());
        }

        @Override
        public boolean hasNext() throws UncheckedIOException {
            return !lines.isEmpty();
        }

        @Override
        public Optional<CodeLine> peek() throws UncheckedIOException {
            return Optional.ofNullable(lines.peek());
        }
    }
}