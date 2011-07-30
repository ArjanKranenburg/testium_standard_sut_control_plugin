/**
 * 
 */
package org.testium.systemundertest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.testium.configuration.SutControlConfiguration;
import org.testium.executor.TestStepCommandExecutor;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.testsuite.TestStepSimple;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;
import org.testtoolinterfaces.utils.Trace;


/**
 * @author arjan.kranenburg
 *
 * Simple class for starting the System Under Test.
 */
public final class GetVersionLongSutCommand implements TestStepCommandExecutor
{
	private static final String myAction = "getLongVersion";
	public static final String myVersionLogParameter = "versionLongLog";
	private SutControlConfiguration myConfig;

	/**
	 * Constructor
	 * 
	 * @param aConfig
	 */
	public GetVersionLongSutCommand( SutControlConfiguration aConfig )
	{
		Trace.println( Trace.CONSTRUCTOR );
		myConfig = aConfig;
	}

	public ArrayList<Parameter> getParameters()
	{
		Trace.println( Trace.GETTER );
		ArrayList<Parameter> params = new ArrayList<Parameter>();
		Parameter versionLongLogParameter = new Parameter(myVersionLogParameter, File.class );
		params.add( versionLongLogParameter );

		return params;
	}

	@Override
	public TestStepResult execute( TestStepSimple aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		Trace.println( Trace.EXEC, "execute( " + aStep.getId() + ", "
		               						   + "aVariables, "
		               						   + aLogDir.getName() + " )", true );
		// verifyParameters( aStep.getParameters() ); // Not needed

		// TODO is this correct? Why not directly using the constants?
		ArrayList<Parameter> params = getParameters();
		for (Parameter param : params)
		{
			RunTimeVariable var = aVariables.get( param.getName() );
			if ( var == null ||
				 var.getType() != param.getValueType() )
			{
				throw new TestSuiteException( param.getName()
				                              + " is not set or is not of type "
				                              + param.getValueType().getName(),
				                              aStep );
			}
		}

		TestStepResult result = new TestStepResult( aStep );

		File command = myConfig.getCommand();
		String cmdParam = myConfig.getLongVersionParameter();
		cmdParam += " " + myConfig.getSettingsParameter();

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		File runLog = new File( aLogDir, "sutVersionLong.log" );
		result.addTestLog("sutVersionLong", "sutVersionLong.log");
		
		try
		{
			StandardSutControl.execute( command, cmdParam, output, runLog );
			result.setResult(VERDICT.PASSED);
		}
		catch (Exception exc)
		{
        	Trace.print(Trace.UTIL, exc );
    		result.setResult(VERDICT.FAILED);
    		result.setComment(exc.getMessage());
		}

		PrintWriter pw;
		try
		{
			pw = new PrintWriter(runLog);
			pw.println(output.toString());
	        pw.flush();
		}
		catch (FileNotFoundException exc)
		{
        	Trace.print(Trace.UTIL, exc );
		}

		RunTimeVariable logVar = aVariables.get( myVersionLogParameter );
		logVar.setValue( runLog );
        return result;
	}

	@Override
	public String getCommand()
	{
		Trace.println( Trace.GETTER );
		return myAction;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		Trace.println( Trace.EXEC_PLUS );
		return true;
	}
}
