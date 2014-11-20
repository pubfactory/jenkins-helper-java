package com.safaribooks.junitattachments;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class RecordAttachment {

    public static void record(File path, byte[] value) {
        try {
            FileUtils.writeByteArrayToFile(path, value);

            System.out.println();
            // according to
            // https://wiki.jenkins-ci.org/display/JENKINS/JUnit+Attachments+Plugin
            // format must be [[ATTACHMENT|/absolute/path/to/some/file]]
            System.out.println("[[ATTACHMENT|" + path.getAbsolutePath() + "]]");
            System.out.println();

        } catch (IOException e1) {
            System.out.println("unable to write'" + path + "':" + e1);
        }
    }
}
