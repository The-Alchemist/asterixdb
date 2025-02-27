/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.asterix.api.common;

import java.util.Set;

import org.apache.asterix.translator.SessionConfig;
import org.apache.hyracks.api.exceptions.Warning;
import org.apache.hyracks.api.result.IResultMetadata;

public class ResultMetadata implements IResultMetadata {

    private final SessionConfig.OutputFormat format;
    private long jobDuration;
    private long processedObjects;
    private Set<Warning> warnings;

    public ResultMetadata(SessionConfig.OutputFormat format) {
        this.format = format;
    }

    public SessionConfig.OutputFormat getFormat() {
        return format;
    }

    public long getProcessedObjects() {
        return processedObjects;
    }

    public void setProcessedObjects(long processedObjects) {
        this.processedObjects = processedObjects;
    }

    public void setJobDuration(long jobDuration) {
        this.jobDuration = jobDuration;
    }

    public void setWarnings(Set<Warning> warnings) {
        this.warnings = warnings;
    }

    public long getJobDuration() {
        return jobDuration;
    }

    public Set<Warning> getWarnings() {
        return warnings;
    }

    @Override
    public String toString() {
        return "ResultMetadata{" + "format=" + format + ", jobDuration=" + jobDuration + ", processedObjects="
                + processedObjects + '}';
    }
}
