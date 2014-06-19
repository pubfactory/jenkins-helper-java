jenkins-helper-java
===================

This project interfaces with [JUnit Attachments Plugin](https://wiki.jenkins-ci.org/display/JENKINS/JUnit+Attachments+Plugin) to record and display files easily in [Jenkins](jenkins-ci.org). 

Configure Jenkins
-----------------

To use the Jenkins helper first configure you Jenkins instance to use the JUnit Attachments Plugin

 * go to ```[jenkins]/pluginManager/``` where ```jenkins``` is your ```jenkins``` url
 * Click on under the ```Available``` tab select ```JUnit Attachments Plugin```
 * click ```Download now and install after restart```
 * You will need to restart Jenkins.

Then configure your Jenkins build to use the Plugin.

 * Click the ```Configure``` link in your project
 * Under ```Post-build Actions```
 * “Add post-build action” and add  ```Archive the artifacts``` if you have not already
 * next to ```Files to archive``` put ```target/test-attachments/**```
 * Again if you have not already added ```Publish JUnit test result report``` add it with ```Add post-build action```
 * Select the ```Publish test attachments``` check-box.

Configure Maven
---------------

Include jenkins-helper-java in your pom as a test dependency

```xml
<dependency>
    <groupId>com.safaribooks</groupId>
    <artifactId>jenkins-helper-java</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

Configuring your tests
----------------------

You will need to include ```RecordAttachmentRule``` as a rule in your junit test

```java
    @Rule
    public RecordAttachmentRule recordArtifactRule = new RecordAttachmentRule(this);
```

Then annotate public fields or methods you would like recorded on test failure
```java
    @CaptureFile(extension = "txt")
    public String impotantText;

    @CaptureFile(extension = "xml")
    public String getImportantXml() {
        return getXml()
    }
```

The interface is simple, artifacts will only be recorded on test failure, and nothing will be recorded if they are null.
