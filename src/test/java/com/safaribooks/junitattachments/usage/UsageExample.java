package com.safaribooks.junitattachments.usage;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import com.safaribooks.junitattachments.CaptureFile;
import com.safaribooks.junitattachments.RecordAttachmentRule;

/**
 * example of how to record artifacts automatically. Not a test in the strict
 * sense.
 */
public class UsageExample {

	// First include the recordArtifactRule in your junit test.
	@Rule
	public RecordAttachmentRule recordArtifactRule = new RecordAttachmentRule(this);

	// create anything you would need for your tests, in this case an
	// authentication system
	Sam sam = new Sam();

	// you can record artifacts by using the @CaptureFile annotation on either
	// public fields, or public methods public methods will be called after the
	// test has failed.

	// by default the the string will be saved to <method or field name>.txt,
	// but this can be changed with the name and extension parameter

	// will go to authentication.txt
	@CaptureFile
	public String authentication;

	// will go to why.html
	@CaptureFile(name = "why", extension = "html")
	public String getHtml() {
		return sam.getWhy();
	}

	@Test
	public void failingGreenEggsAndHamTest() {
		authentication = "Would you, could you, on a boat?";

		assertTrue("all is not well", sam.accept(authentication));
	}

	@Test
	public void passingGreenEggsAndHamTest() {
		authentication = "You do not like them.\n" + "So you say.\n"
				+ "Try them! Try them!\n" + "And you may.\n"
				+ "Try them and you may I say.\n";

		assertTrue("all is well", sam.accept(authentication));
	}
}
