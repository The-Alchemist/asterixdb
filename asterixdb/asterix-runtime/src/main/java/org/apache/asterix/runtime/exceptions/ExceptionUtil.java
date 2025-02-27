/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.asterix.runtime.exceptions;

import org.apache.asterix.om.types.EnumDeserializer;

public class ExceptionUtil {
    private ExceptionUtil() {
    }

    public static String toExpectedTypeString(byte... expectedTypeTags) {
        StringBuilder expectedTypes = new StringBuilder();
        int numCandidateTypes = expectedTypeTags.length;
        for (int index = 0; index < numCandidateTypes; ++index) {
            if (index > 0) {
                if (index == numCandidateTypes - 1) {
                    expectedTypes.append(" or ");
                } else {
                    expectedTypes.append(", ");
                }
            }
            expectedTypes.append(EnumDeserializer.ATYPETAGDESERIALIZER.deserialize(expectedTypeTags[index]));
        }
        return expectedTypes.toString();
    }

    public static String indexToPosition(int index) {
        int i = index + 1;
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                switch (i % 10) {
                    case 1:
                        return i + "st";
                    case 2:
                        return i + "nd";
                    case 3:
                        return i + "rd";
                    default:
                        return i + "th";
                }
        }
    }
}
