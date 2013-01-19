package org.drools.planner.examples.mista2013.persistence.parsers.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.io.FileUtils;

public class RawProjectData {

    public static RawProjectData parse(final File source) {
        try {
            final ANTLRStringStream input = new ANTLRStringStream(FileUtils.readFileToString(source));
            final MRCPSPLexer lexer = new MRCPSPLexer(input);
            final CommonTokenStream tokens = new CommonTokenStream(lexer);
            final MRCPSPParser parser = new MRCPSPParser(tokens);
            return parser.parse();
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
        this.validate();
    }

    private List<Request> findRequestsWithJobNumber(final int jobNumber) {
        final List<Request> requests = new ArrayList<Request>();
        for (final Request r : this.requestsAndDurations) {
            if (r.getJobNumber() == jobNumber) {
                requests.add(r);
            }
        }
        return Collections.unmodifiableList(requests);
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

    private void validate() {
        this.validatePrecedence();
        this.validateRequests();
        this.validateResourceCount();
    }

    private void validatePrecedence() {
        final int jobCount = this.situation.getJobCount();
        final int projectJobCount = this.project.getJobCount();
        if (jobCount != projectJobCount) {
            throw new IllegalArgumentException("Situation declares " + jobCount + " jobs and project provides "
                    + projectJobCount + ".");
        }
        // make sure that there are exactly as many precedence relations
        final Set<Integer> jobNumbersFromPrecedence = new HashSet<Integer>();
        for (final Precedence p : this.precedences) {
            jobNumbersFromPrecedence.add(p.getJobNumber());
        }
        final int precedenceDeclarations = jobNumbersFromPrecedence.size();
        if (precedenceDeclarations != projectJobCount) {
            throw new IllegalArgumentException("Situation declares " + jobCount + " but there are only "
                    + precedenceDeclarations + " precedence declarations.");
        }
        // make sure that each successor is a valid job
        for (final Precedence p : this.precedences) {
            for (final Integer sucessor : p.getSuccessors()) {
                if (jobNumbersFromPrecedence.contains(sucessor)) {
                    continue;
                }
                throw new IllegalArgumentException("Job " + p.getJobNumber() + " has an invalid successor: " + sucessor);
            }
        }
    }

    private void validateRequests() {
        for (final Precedence p : this.precedences) {
            final List<Request> requests = this.findRequestsWithJobNumber(p.getJobNumber());
            // every job must be requested
            if (requests.size() == 0) {
                throw new IllegalArgumentException("Job " + p.getJobNumber() + " is never requested.");
            }
            // every job must have the same modes as requested
            final Set<Integer> requiredModes = new HashSet<Integer>();
            for (int i = 1; i < p.getNumberOfModes(); i++) {
                requiredModes.add(i);
            }
            for (final Request r : requests) {
                requiredModes.remove(r.getMode());
            }
            if (requiredModes.size() > 0) {
                throw new IllegalArgumentException("Job " + p.getJobNumber() + " is missing the following modes:"
                        + requiredModes);
            }
        }
    }

    private void validateResourceCount() {
        final int renewables = this.situation.getRenewableResourceCount();
        final int nonrenewables = this.situation.getNonRenewableResourceCount();
        final int doubleConstrained = this.situation.getDoublyConstrainedResourceCount();
        final int total = renewables + nonrenewables + doubleConstrained;
        // validate that each stated resource specifies availability
        final int actuallyAvailable = this.resourceAvailability.size();
        if (actuallyAvailable != total) {
            throw new IllegalStateException("There are supposed to be " + total + " resources, but only "
                    + actuallyAvailable + " state their availability.");
        }
        /*
         * validate that each resource request specifies all the stated
         * resources
         */
        for (final Request r : this.requestsAndDurations) {
            final List<Integer> resources = r.getResources();
            final int available = resources.size();
            if (available != total) {
                throw new IllegalStateException("There are supposed to be " + total + " resources, but " + available
                        + " are available.");
            }
        }
    }

}
