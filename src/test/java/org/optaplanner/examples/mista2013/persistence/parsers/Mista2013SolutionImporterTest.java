package org.optaplanner.examples.mista2013.persistence.parsers;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.optaplanner.examples.mista2013.Properties;
import org.optaplanner.examples.mista2013.persistence.Mista2013SolutionImporter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class Mista2013SolutionImporterTest {

    private static final FileFilter filter = new FileFilter() {

        @Override
        public boolean accept(final File arg0) {
            return (!arg0.isDirectory());
        }

    };

    @Parameters(name = "{index} ({0})")
    public static Collection<Object[]> retrieveInstanceFiles() {
        final Collection<Object[]> instanceFiles = new ArrayList<Object[]>();
        for (final File f : Properties.DATA_FOLDER.listFiles(Mista2013SolutionImporterTest.filter)) {
            final Object[] instance = new Object[] { f };
            instanceFiles.add(instance);
        }
        return instanceFiles;
    }

    public final File instanceFileToTest;

    public Mista2013SolutionImporterTest(final File f) {
        this.instanceFileToTest = f;
    }

    @Test
    public void test() throws IOException {
        final Mista2013SolutionImporter importer = new Mista2013SolutionImporter();
        importer.readSolution(this.instanceFileToTest);
    }

}
