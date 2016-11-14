package org.monitoring;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.runner.RunWith;

/**
 * Runs all test within project.
 */
@RunWith(ClasspathSuite.class)
@ClasspathSuite.ClassnameFilters({".*Test"})
public class AllTestsRunner {
}