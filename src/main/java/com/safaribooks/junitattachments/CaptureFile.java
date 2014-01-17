/**
 * @author markl
 */
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
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
//TODO: have this work Only with public members
//TODO: having this work on local variable would be awesome, mabe in java 8 :-(
@Retention(RetentionPolicy.RUNTIME)
public @interface CaptureFile {//TODO: capture String, just record a bunch of string paramiters

    /**
     * the file extention. will defualt to txt
     * 
     * @return
     */
    String extention() default "txt";

    /**
     * the name of the file to be stored (without file extension), will default to the method or field name
     * 
     * @return
     */
    String name() default "";
}
