/**
 * 
 */
package net.sf.testium.systemundertest;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.testium.configuration.SutControlConfiguration;
import net.sf.testium.executor.TestStepCommandExecutor;

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
 * Simple class for starting the System Under Test.
 */
public final class StartSutCommand implements TestStepCommandExecutor
{
	private static final String myAction = "start";
	private SutControlConfiguration myConfig;

	/**
	 * @param sutControl
	 */
	public StartSutCommand( SutControlConfiguration aConfig )
	{
		Trace.println( Trace.CONSTRUCTOR );
		myConfig = aConfig;
	}

	public TestStepResult execute( TestStepCommand aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		Trace.println( Trace.EXEC );
		TestStepResult result = new TestStepCommandResultImpl( aStep );

		File command = myConfig.getCommand();
		String cmdParam = myConfig.getStartParameter();
		cmdParam += " " + myConfig.getSettingsParameter();

		File runLog = new File( aLogDir, "sutStart.log" );
		result.addTestLog("sutStart", "sutStart.log");
		
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
}
