/**
 * @author markl
 */
package com.safaribooks.junitattachments;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;


/**
 * 
 * 
 * @author markl
 * 
 */
public class RecordArtifactRule extends TestWatcher {

	// TOOD: use with
	// https://wiki.jenkins-ci.org/display/JENKINS/JUnit+Attachments+Plugin

	// TODO: check that everything in the class that uses the CaptureFile
	// annotation is set to public, fail the test otherwise.

	// /**
	// * A logger... so if you use System.out, or System.err... you will be
	// shot!
	// */
	// private static transient final Logger logger =
	// LoggerFactory.getLogger(RecordArtifactRule.class);

	private final Object testObject;

	/**
     *
     */
	public RecordArtifactRule(final Object testObject) {
		super();
		this.testObject = testObject;
	}

	@Override
	protected void starting(final Description description) {
		// description.
		description.getTestClass().getMethods();
	}
	
	@Override
	protected void failed(final Throwable e, final Description description) {

		Map<String, byte[]> fileOut = new HashMap<String, byte[]>();
		// TODO: null checks and such
		// TODO: tostring the objects, and such

		for (Method m : description.getTestClass().getMethods()) {

			// System.out.println(m.getName());
			CaptureFile cf = m.getAnnotation(CaptureFile.class);
			if (cf != null) {

				String fileKey = m.getName() + "." + cf.extention();
				
				try {

					Object methodOutput = m.invoke(testObject);

					insertObject(fileOut, fileKey, m.getReturnType(),methodOutput);

				} catch (IllegalAccessException e1) {
					System.out.println("unable serialize '" + fileKey + "':" + e1);
				} catch (IllegalArgumentException e1) {
					System.out.println("unable serialize '" + fileKey + "':" + e1);
				} catch (InvocationTargetException e1) {
					System.out.println("unable serialize '" + fileKey + "':" + e1);
				}
			}
		}

		for (Field f : description.getTestClass().getFields()) {

			CaptureFile cf = f.getAnnotation(CaptureFile.class);
			if (cf != null) {

				String fileKey = f.getName() + "." + cf.extention();
				try {
					
					insertObject(fileOut, fileKey, f.getType(),f.get(testObject));

				} catch (IllegalArgumentException e1) {
					System.out.println("unable serialize '" + fileKey + "':" + e1);
				} catch (IllegalAccessException e1) {
					System.out.println("unable serialize '" + fileKey + "':" + e1);
				}
			}
		}

		// TODO: first clean this directory

		// String relpath = "target/surefire-reports/";
		String relpath = "target/testArtifacts/";

		// TODO: system var for maven target?
		(new File(relpath)).mkdirs();// + System.getProperty("user.dir");
		String root = relpath + testObject.getClass().getName();// + "." +
																// description.getMethodName();
		(new File(root)).mkdirs();
		// TODO: new file error checks and stuff

		// becuase the plugin is class based not test based
		String pre = description.getMethodName() + ".";

		if (fileOut.size() > 0) {

			for (Entry<String, byte[]> p : fileOut.entrySet()) {
				String fullRelativePath = root + "/" + pre + p.getKey();
				byte[] value = p.getValue();
				File path = new File(fullRelativePath);
				
				RecordArtifact.record(path, value);
				
			}
		}

	}

	// TODO: handle Content
	
	private void insertObject(Map<String, byte[]> fileOut, String key, Class<?> type, Object o){
		
		if (o == null) {
			System.out.println("unable serialize '" + o + "': it was null");

		} else if (String.class.equals(type)) {
			String str = (String) o;
			fileOut.put(key, str.getBytes());
			
		} else {// TODO: reflect and insure this is the output return type
			byte[] byt = (byte[]) o;
			fileOut.put(key, byt);
		}
		

		// TODO: handle tostringable classes
		//TODO: use reflection to record the classes that arn't
	}
	
}
