package org.drools.planner.examples.mista2013.persistence.parsers.project;

import java.io.File;
import java.io.IOException;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.io.FileUtils;

public class RawProjectData {

    public static void parse(final File source) {
        try {
            final ANTLRStringStream input = new ANTLRStringStream(FileUtils.readFileToString(source));
            final MRCPSPLexer lexer = new MRCPSPLexer(input);
            final CommonTokenStream tokens = new CommonTokenStream(lexer);
            final MRCPSPParser parser = new MRCPSPParser(tokens);
            parser.parse();
        } catch (final IOException e) {
            throw new IllegalArgumentException("Cannot read parser input file: " + source, e);
        } catch (final RecognitionException e) {
            throw new IllegalArgumentException("Cannot parse input file: " + source, e);
        }
    }

}
