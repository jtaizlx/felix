/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.felix.karaf.jpm.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.karaf.jpm.Process;

public class ProcessImpl implements Process {

    private int pid;
    //private File input;
    //private File output;
    //private File error;

    public ProcessImpl(int pid/*, File input, File output, File error*/) {
        this.pid = pid;
        //this.input = input;
        //this.output = output;
        //this.error = error;
    }

    public int getPid() {
        return pid;
    }

    public boolean isRunning() throws IOException {
        if (ScriptUtils.isWindows()) {
            Map<String, String> props = new HashMap<String, String>();
            props.put("${pid}", Integer.toString(pid));
            int ret = ScriptUtils.execute("running", props);
            return ret == 0;
        } else {
            try {
                java.lang.Process process = new java.lang.ProcessBuilder("ps", "-p", Integer.toString(pid)).start();
                BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
                r.readLine(); // skip headers
                String s = r.readLine();
                boolean running = s != null && s.length() > 0;
                int ret = process.waitFor();
                return running;
            } catch (InterruptedException e) {
                throw new InterruptedIOException();
            }
        }
    }

    public void destroy() throws IOException {
        int ret;
        if (ScriptUtils.isWindows()) {
            Map<String, String> props = new HashMap<String, String>();
            props.put("${pid}", Integer.toString(pid));
            ret = ScriptUtils.execute("destroy", props);
        } else {
            ret = ScriptUtils.executeProcess(new java.lang.ProcessBuilder("kill", "-9", Integer.toString(pid)));
        }
        if (ret != 0) {
            throw new IOException("Unable to destroy proces, it may be already terminated");
        }
    }

    /*
    public OutputStream getInputStream() throws FileNotFoundException {
        return new FileOutputStream(input);
    }

    public InputStream getOutputStream() throws FileNotFoundException {
        return new FileInputStream(output);
    }

    public InputStream getErrorStream() throws FileNotFoundException {
        return new FileInputStream(error);
    }
    */

    public int waitFor() throws InterruptedException {
        return 0;
    }

    public int exitValue() {
        return 0;
    }

    public static org.apache.felix.karaf.jpm.Process create(File dir, String command) throws IOException {
        //File input = File.createTempFile("jpm.", ".input");
        //File output = File.createTempFile("jpm.", ".output");
        //File error = File.createTempFile("jpm.", ".error");
        File pidFile = File.createTempFile("jpm.", ".pid");
        try {
            Map<String, String> props = new HashMap<String, String>();
            //props.put("${in.file}", input.getCanonicalPath());
            //props.put("${out.file}", output.getCanonicalPath());
            //props.put("${err.file}", error.getCanonicalPath());
            props.put("${pid.file}", pidFile.getCanonicalPath());
            props.put("${dir}", dir != null ? dir.getCanonicalPath() : "");
            if (ScriptUtils.isWindows()) {
                command = command.replaceAll("\"", "\"\"");
            }
            props.put("${command}", command);
            int ret = ScriptUtils.execute("start", props);
            if (ret != 0) {
                throw new IOException("Unable to create process (error code: " + ret + ")");
            }
            int pid = readPid(pidFile);
            return new ProcessImpl(pid/*, input, output, error*/);
        } finally {
            pidFile.delete();
        }
    }

    public static Process attach(int pid) throws IOException {
        return new ProcessImpl(pid);
    }

    private static int readPid(File pidFile) throws IOException {
        InputStream is = new FileInputStream(pidFile);
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String pidString = r.readLine();
            return Integer.valueOf(pidString);
        } finally {
            try {
                is.close();
            } catch (IOException e) {}
        }
    }

}
