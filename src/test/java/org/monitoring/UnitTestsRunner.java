package org.monitoring;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.runner.RunWith;

/**
 * Runs unit tests, the ones marked with Test suffix.
 */
@RunWith(ClasspathSuite.class)
@ClasspathSuite.ClassnameFilters({".*Test", "!.*ITest"})
public class UnitTestsRunner {

}