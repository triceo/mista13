package org.drools.planner.examples.mista2013.persistence;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.planner.examples.common.persistence.AbstractTxtSolutionImporter;
import org.drools.planner.examples.mista2013.domain.Job;
import org.drools.planner.examples.mista2013.domain.Job.JobType;
import org.drools.planner.examples.mista2013.domain.JobMode;
import org.drools.planner.examples.mista2013.domain.Mista2013;
import org.drools.planner.examples.mista2013.domain.ProblemInstance;
import org.drools.planner.examples.mista2013.domain.Project;
import org.drools.planner.examples.mista2013.domain.Resource;
import org.drools.planner.examples.mista2013.domain.Resource.ResourceType;
import org.drools.planner.examples.mista2013.persistence.parsers.instance.RawInstance;
import org.drools.planner.examples.mista2013.persistence.parsers.instance.RawProject;
import org.drools.planner.examples.mista2013.persistence.parsers.project.Precedence;
import org.drools.planner.examples.mista2013.persistence.parsers.project.RawProjectData;
import org.drools.planner.examples.mista2013.persistence.parsers.project.Request;

public class Mista2013SolutionImporter extends AbstractTxtSolutionImporter {

    private class Mista2013TxtInputBuilder extends TxtInputBuilder {

        private List<Job> buildJobs(final RawProjectData data, final List<Resource> resources) {
            final List<Request> requests = data.getRequestsAndDurations();
            final List<Precedence> precedence = data.getPrecedences();
            // traverse the jobs backwards; successors need to be created first
            final Map<Integer, Job> jobCache = new HashMap<Integer, Job>();
            for (int jobId = precedence.size(); jobId > 0; jobId--) {
                // gather successors
                final List<Job> successors = new ArrayList<Job>();
                for (final Integer successorId : precedence.get(jobId - 1).getSuccessors()) {
                    successors.add(jobCache.get(successorId));
                }
                // prepare job modes
                final List<JobMode> modes = new ArrayList<JobMode>();
                for (final Request r : requests) {
                    if (r.getJobNumber() != jobId) {
                        continue;
                    }
                    // prepare resource consumption data
                    int resourceId = 0;
                    final TObjectIntMap<Resource> resourceConsumption = new TObjectIntHashMap<Resource>();
                    for (final Resource resource : resources) {
                        resourceConsumption.put(resource, r.getResources().get(resourceId));
                        resourceId++;
                    }
                    modes.add(new JobMode(r.getMode(), r.getDuration(), resourceConsumption));
                }
                JobType jt = null;
                if (jobId == precedence.size()) {
                    jt = JobType.SINK;
                } else if (jobId == 1) {
                    jt = JobType.SOURCE;
                } else {
                    jt = JobType.STANDARD;
                }
                final Job job = new Job(jobId, modes, successors, jt);
                jobCache.put(jobId, job);
            }
            return new ArrayList<Job>(jobCache.values());
        }

        private Project buildProject(final RawProject raw, final List<Resource> globalResources) {
            final RawProjectData data = raw.getProjectData();
            // set resource capacities
            final List<Resource> resultingResources = new ArrayList<Resource>();
            for (int i = 0; i < globalResources.size(); i++) {
                Resource r = globalResources.get(i);
                if (r == null) {
                    if (i < 2) {
                        r = new Resource(i + 1, ResourceType.RENEWABLE);
                        r.setCapacity(data.getResourceAvailability().get(i));
                    } else if (i < 4) {
                        r = new Resource(i + 1, ResourceType.NONRENEWABLE);
                        r.setCapacity(data.getResourceAvailability().get(i));
                    } else {
                        r = new Resource(i + 1, ResourceType.DOUBLE_CONSTRAINED);
                        r.setCapacity(data.getResourceAvailability().get(i));
                    }
                }
                resultingResources.add(r);
            }
            // build jobs and project
            final List<Job> jobs = this.buildJobs(data, resultingResources);
            final Project p = new Project(raw.getId(), raw.getCriticalPathDuration(), raw.getReleaseDate(),
                    resultingResources, jobs);
            return p;
        }

        @Override
        public Mista2013 readSolution() throws IOException {
            final RawInstance instance = RawInstance.parse(this.inputFile);
            // prepare global resources
            final List<Resource> globalResources = new ArrayList<Resource>();
            int i = 1;
            for (final Integer capacity : instance.getResourceCapacities()) {
                if (capacity < 0) {
                    globalResources.add(null);
                } else {
                    final Resource r = new Resource(i);
                    r.setCapacity(capacity);
                    globalResources.add(r);
                }
                i++;
            }
            // build projects from raw data
            final List<Project> projects = new ArrayList<Project>();
            for (final RawProject rp : instance.getProjects()) {
                final Project p = this.buildProject(rp, globalResources);
                projects.add(p);
            }
            return new Mista2013(new ProblemInstance(projects));
        }

    }

    public Mista2013SolutionImporter() {
        super(new Mista2013DaoImpl());
    }

    @Override
    public TxtInputBuilder createTxtInputBuilder() {
        return new Mista2013TxtInputBuilder();
    }

}
