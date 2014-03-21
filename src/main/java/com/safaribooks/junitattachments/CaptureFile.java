package com.safaribooks.junitattachments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * annotations to easily capture file data, and have it be reported in jenkins.  see RecordArtifactRule
 * 
 * currently only works on public methods and fields
 * 
 * will default to <method or field name>.txt
 * 
 * if the value to be recorded is null, nothing will be recorded
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
//TODO: have this work Only with public members
//TODO: having this work on local variable would be awesome, mabe in java 8 :-(
@Retention(RetentionPolicy.RUNTIME)
public @interface CaptureFile {

    /**
     * the file extension. will default to txt
     * 
     * @return
     */
    String extension() default "txt";

    /**
     * the name of the file to be stored (without file extension), will default to the method or field name
     * 
     * @return
     */
    String name() default "";
}
