package org.drools.planner.examples.mista2013.persistence.parsers.project;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RawProjectDataTest {

    private static final File dataFolder = new File("data/mista2013/input");

    private static final FileFilter fileFilter = new FileFilter() {

        @Override
        public boolean accept(final File arg0) {
            return (!arg0.isDirectory());
        }

    };

    private static final FileFilter folderFilter = new FileFilter() {

        @Override
        public boolean accept(final File arg0) {
            return arg0.isDirectory();
        }

    };

    @Parameters(name = "{index} ({0})")
    public static Collection<Object[]> retrieveInstanceFiles() {
        final Collection<Object[]> instanceFiles = new ArrayList<Object[]>();
        for (final File folder : RawProjectDataTest.dataFolder.listFiles(RawProjectDataTest.folderFilter)) {
            for (final File f : folder.listFiles(RawProjectDataTest.fileFilter)) {
                final Object[] instance = new Object[] { f };
                instanceFiles.add(instance);
            }
        }
        return instanceFiles;
    }

    public final File instanceFileToTest;

    public RawProjectDataTest(final File f) {
        this.instanceFileToTest = f;
    }

    /**
     * Tests whether or not the MRCPSP data can be parsed by the parser. Doesn't
     * check for the validity of the parsed data.
     */
    @Test
    public void testParse() {
        RawProjectData.parse(this.instanceFileToTest);
    }

}
