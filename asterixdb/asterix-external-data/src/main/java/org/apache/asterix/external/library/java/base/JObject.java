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
package org.apache.asterix.external.library.java.base;

import java.io.DataOutput;
import java.io.IOException;

import org.apache.asterix.external.api.IJObject;
import org.apache.asterix.om.base.IAObject;
import org.apache.asterix.om.types.ATypeTag;
import org.apache.hyracks.api.exceptions.HyracksDataException;

public abstract class JObject implements IJObject {

    protected IAObject value;
    protected byte[] bytes;

    protected JObject() {
    }

    protected JObject(IAObject value) {
        this.value = value;
    }

    @Override
    public IAObject getIAObject() {
        return value;
    }

    public void serializeTypeTag(boolean writeTypeTag, DataOutput dataOutput, ATypeTag typeTag)
            throws HyracksDataException {
        if (writeTypeTag) {
            try {
                dataOutput.writeByte(typeTag.serialize());
            } catch (IOException e) {
                throw HyracksDataException.create(e);
            }
        }
    }
}
