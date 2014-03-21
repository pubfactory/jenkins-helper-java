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
 * records test artifacts to disk, use with
 * https://wiki.jenkins-ci.org/display/JENKINS/JUnit+Attachments+Plugin
 * 
 * @author markl
 * 
 */
public class RecordAttachmentRule extends TestWatcher {

	private final Object testObject;

	/**
	 * the class must be constructed with the object it will later be inspecting
	 */
	public RecordAttachmentRule(final Object testObject) {
		super();
		this.testObject = testObject;
	}

	@Override
	protected void starting(final Description description) {
		description.getTestClass().getMethods();
	}

	@Override
	protected void failed(final Throwable e, final Description description) {

		Map<String, byte[]> fileOut = new HashMap<String, byte[]>();
		// TODO: tostring the objects, and such

		for (Method m : description.getTestClass().getMethods()) {

			CaptureFile cf = m.getAnnotation(CaptureFile.class);
			if (cf != null) {

				String fileKey = m.getName() + "." + cf.extension();

				try {

					Object methodOutput = m.invoke(testObject);

					insertObject(fileOut, fileKey, m.getReturnType(),
							methodOutput);

				} catch (IllegalAccessException e1) {
					System.out.println("unable serialize '" + fileKey + "':"
							+ e1);
				} catch (IllegalArgumentException e1) {
					System.out.println("unable serialize '" + fileKey + "':"
							+ e1);
				} catch (InvocationTargetException e1) {
					System.out.println("unable serialize '" + fileKey + "':"
							+ e1);
				}
			}
		}

		for (Field f : description.getTestClass().getFields()) {

			CaptureFile cf = f.getAnnotation(CaptureFile.class);
			if (cf != null) {

				String fileKey = f.getName() + "." + cf.extension();
				try {

					insertObject(fileOut, fileKey, f.getType(),
							f.get(testObject));

				} catch (IllegalArgumentException e1) {
					System.out.println("unable serialize '" + fileKey + "':"
							+ e1);
				} catch (IllegalAccessException e1) {
					System.out.println("unable serialize '" + fileKey + "':"
							+ e1);
				}
			}
		}

		// we assume jenkins will perform a clean build so there is no need to
		// clean the directory

		String relpath = "target/test-attachments/";

		// TODO: system var for maven's target/ directory?
		String root = relpath + testObject.getClass().getName();
		
		(new File(root)).mkdirs();
		// TODO: new file error checks and stuff

		// Because the plugin is class based not test based
		String pre = description.getMethodName() + ".";

		if (fileOut.size() > 0) {

			for (Entry<String, byte[]> p : fileOut.entrySet()) {
				String fullRelativePath = root + "/" + pre + p.getKey();
				byte[] value = p.getValue();
				File path = new File(fullRelativePath);

				RecordAttachment.record(path, value);
			}
		}
	}

	private void insertObject(Map<String, byte[]> fileOut, String key,
			Class<?> type, Object o) {

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
		// TODO: use reflection to record the classes that arn't
	}

}
