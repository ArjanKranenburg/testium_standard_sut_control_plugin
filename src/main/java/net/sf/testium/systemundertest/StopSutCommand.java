/**
 * 
 */
package net.sf.testium.systemundertest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import net.sf.testium.configuration.SutControlConfiguration;
import net.sf.testium.executor.TestStepCommandExecutor;
import net.sf.testium.executor.general.SpecifiedParameter;

import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.impl.TestStepCommandResultImpl;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStepCommand;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;


/**
 * @author arjan.kranenburg
 *
 * Simple class for stopping the System Under Test.
 */
public final class StopSutCommand implements TestStepCommandExecutor
{
	private static final String myAction = "stop";
	private SutControlConfiguration myConfig;

	/**
	 * @param sutControl
	 */
	public StopSutCommand( SutControlConfiguration aConfig )
	{
		Trace.println( Trace.CONSTRUCTOR );
		myConfig = aConfig;
	}

	/* (non-Javadoc)
	 * @see org.TestToolInterfaces.systemUnderTest.AbstractSingleSutAction#doAction()
	 */
	public boolean doAction(RunTimeData aVariables, File aLogDir)
	{
		Trace.println( Trace.EXEC );
		File command = myConfig.getCommand();
		String cmdParam = myConfig.getStopParameter();
		cmdParam += " " + myConfig.getSettingsParameter();
		File runLog = new File( aLogDir, "sutStart.log" );
		try
		{
			StandardSutControl.execute(command, cmdParam, runLog);
		}
		catch (FileNotFoundException exc)
		{
        	Trace.print(Trace.UTIL, exc );
        	return false;
		}
    	return true;
	}

	public TestStepResult execute( TestStepCommand aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		Trace.println( Trace.EXEC );
		TestStepResult result = new TestStepCommandResultImpl( aStep );

		File command = myConfig.getCommand();
		String cmdParam = myConfig.getStopParameter();
		cmdParam += " " + myConfig.getSettingsParameter();

		File runLog = new File( aLogDir, "sutStop.log" );
		result.addTestLog("sutStart", "sutStop.log");
		
		try
		{
			StandardSutControl.execute(command, cmdParam, runLog);
			result.setResult(VERDICT.PASSED);
		}
		catch (FileNotFoundException exc)
		{
        	Trace.print(Trace.UTIL, exc );
    		result.setResult(VERDICT.FAILED);
    		result.setComment(exc.getMessage());
		}

		return result;
	}

	public String getCommand()
	{
		Trace.println( Trace.GETTER );
		return myAction;
	}

	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		Trace.println( Trace.EXEC_PLUS );
		return true;	
	}

	public String getDescription() {
		return "Stops the System Under Test.";
	}

	public ArrayList<SpecifiedParameter> getParameterSpecs() {
		return new ArrayList<SpecifiedParameter>();
	}
}
