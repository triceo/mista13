package org.drools.planner.examples.mista2013.persistence.parsers.project;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

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

    private final SituationMetadata situation;
    private final ProjectMetadata project;
    private final List<Precedence> precedences;
    private final List<Request> requestsAndDurations;
    private final List<Integer> resourceAvailability;

    public RawProjectData(final SituationMetadata situation, final ProjectMetadata project,
            final List<Precedence> precendences, final List<Request> requests, final List<Integer> resourceAvailability) {
        this.situation = situation;
        this.project = project;
        this.precedences = Collections.unmodifiableList(precendences);
        this.requestsAndDurations = Collections.unmodifiableList(requests);
        this.resourceAvailability = Collections.unmodifiableList(resourceAvailability);
    }

    public List<Precedence> getPrecedences() {
        return this.precedences;
    }

    public ProjectMetadata getProject() {
        return this.project;
    }

    public List<Request> getRequestsAndDurations() {
        return this.requestsAndDurations;
    }

    public List<Integer> getResourceAvailability() {
        return this.resourceAvailability;
    }

    public SituationMetadata getSituation() {
        return this.situation;
    }

}
