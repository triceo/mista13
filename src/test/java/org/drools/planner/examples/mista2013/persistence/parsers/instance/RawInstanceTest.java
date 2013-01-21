package org.drools.planner.examples.mista2013.persistence.parsers.instance;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

import org.drools.planner.examples.mista2013.Properties;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RawInstanceTest {

    private static final FileFilter filter = new FileFilter() {

        @Override
        public boolean accept(final File arg0) {
            return (!arg0.isDirectory());
        }

    };

    @Parameters(name = "{index} ({0})")
    public static Collection<Object[]> retrieveInstanceFiles() {
        final Collection<Object[]> instanceFiles = new ArrayList<Object[]>();
        for (final File f : Properties.DATA_FOLDER.listFiles(RawInstanceTest.filter)) {
            final Object[] instance = new Object[] { f };
            instanceFiles.add(instance);
        }
        return instanceFiles;
    }

    public final File instanceFileToTest;

    public RawInstanceTest(final File f) {
        this.instanceFileToTest = f;
    }

    /**
     * Tests whether or not competition data can be parsed by the parser.
     * Doesn't check for the validity of the parsed data.
     */
    @Test
    public void testParse() {
        final RawInstance ri = RawInstance.parse(this.instanceFileToTest);
        Assert.assertNotNull(ri);
        Assert.assertTrue("Instance has zero projects.", ri.getNumberOfProjects() > 0);
        Assert.assertTrue("Instance has zero resources.", ri.getNumberOfResources() > 0);
        for (final RawProject rp : ri.getProjects()) {
            try {
                rp.getProjectData();
            } catch (final Exception ex) {
                Assert.fail("Parsing project data failed. Look for errors in the respective tests.");
            }
        }
    }

}
