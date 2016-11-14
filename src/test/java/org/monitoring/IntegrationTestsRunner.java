package org.monitoring;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.runner.RunWith;

/**
 * Runs integration tests, the ones marked with ITest suffix.
 */
@RunWith(ClasspathSuite.class)
@ClasspathSuite.ClassnameFilters({".*ITest"})
public class IntegrationTestsRunner {

}