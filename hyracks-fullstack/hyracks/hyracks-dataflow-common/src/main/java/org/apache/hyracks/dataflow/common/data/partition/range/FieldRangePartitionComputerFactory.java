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
package org.apache.hyracks.dataflow.common.data.partition.range;

import org.apache.hyracks.api.comm.IFrameTupleAccessor;
import org.apache.hyracks.api.context.IHyracksTaskContext;
import org.apache.hyracks.api.dataflow.value.IBinaryComparator;
import org.apache.hyracks.api.dataflow.value.IBinaryComparatorFactory;
import org.apache.hyracks.api.dataflow.value.ITuplePartitionComputer;
import org.apache.hyracks.api.dataflow.value.ITuplePartitionComputerFactory;
import org.apache.hyracks.api.exceptions.HyracksDataException;

public abstract class FieldRangePartitionComputerFactory implements ITuplePartitionComputerFactory {
    private static final long serialVersionUID = 1L;
    private final int[] rangeFields;
    private IBinaryComparatorFactory[] comparatorFactories;

    protected FieldRangePartitionComputerFactory(int[] rangeFields, IBinaryComparatorFactory[] comparatorFactories) {
        this.rangeFields = rangeFields;
        this.comparatorFactories = comparatorFactories;
    }

    protected abstract RangeMap getRangeMap(IHyracksTaskContext hyracksTaskContext) throws HyracksDataException;

    @Override
    public ITuplePartitionComputer createPartitioner(IHyracksTaskContext hyracksTaskContext) {
        final IBinaryComparator[] comparators = new IBinaryComparator[comparatorFactories.length];
        for (int i = 0; i < comparatorFactories.length; ++i) {
            comparators[i] = comparatorFactories[i].createBinaryComparator();
        }

        return new ITuplePartitionComputer() {
            private RangeMap rangeMap;

            @Override
            public void initialize() throws HyracksDataException {
                rangeMap = getRangeMap(hyracksTaskContext);
            }

            @Override
            public int partition(IFrameTupleAccessor accessor, int tIndex, int nParts) throws HyracksDataException {
                if (nParts == 1) {
                    return 0;
                }
                int slotIndex = getRangePartition(accessor, tIndex);
                // Map range partition to node partitions.
                double rangesPerPart = 1;
                if (rangeMap.getSplitCount() + 1 > nParts) {
                    rangesPerPart = ((double) rangeMap.getSplitCount() + 1) / nParts;
                }
                return (int) Math.floor(slotIndex / rangesPerPart);
            }

            private int getRangePartition(IFrameTupleAccessor accessor, int tIndex) throws HyracksDataException {
                int slotIndex = 0;
                for (int slotNumber = 0; slotNumber < rangeMap.getSplitCount(); ++slotNumber) {
                    int c = compareSlotAndFields(accessor, tIndex, slotNumber);
                    if (c < 0) {
                        return slotIndex;
                    }
                    slotIndex++;
                }
                return slotIndex;
            }

            private int compareSlotAndFields(IFrameTupleAccessor accessor, int tIndex, int slotNumber)
                    throws HyracksDataException {
                int c = 0;
                int startOffset = accessor.getTupleStartOffset(tIndex);
                int slotLength = accessor.getFieldSlotsLength();
                for (int fieldNum = 0; fieldNum < comparators.length; ++fieldNum) {
                    int fIdx = rangeFields[fieldNum];
                    int fStart = accessor.getFieldStartOffset(tIndex, fIdx);
                    int fEnd = accessor.getFieldEndOffset(tIndex, fIdx);
                    c = comparators[fieldNum].compare(accessor.getBuffer().array(), startOffset + slotLength + fStart,
                            fEnd - fStart, rangeMap.getByteArray(), rangeMap.getStartOffset(fieldNum, slotNumber),
                            rangeMap.getLength(fieldNum, slotNumber));
                    if (c != 0) {
                        return c;
                    }
                }
                return c;
            }

        };
    }
}
