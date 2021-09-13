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
package org.netbeans.lib.profiler.heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

enum Progress {
    COMPUTE_INSTANCES,
    COMPUTE_REFERENCES,
    FILL_HEAP_TAG_BOUNDS,
    COMPUTE_GC_ROOTS;

    Handle start() {
        return new Handle(this);
    }

    private static List<Listener> listeners = Collections.emptyList();
    synchronized static void register(Listener onChange) {
        if (listeners.isEmpty()) {
            listeners = Collections.singletonList(onChange);
        } else {
            List<Listener> copy = new ArrayList<>(listeners);
            copy.add(onChange);
            listeners = copy;
        }
    }

    synchronized static void notifyUpdates(Handle h, int type) {
        for (Listener onChange : listeners) {
            switch (type) {
                case 1: onChange.started(h); break;
                case 2: onChange.progress(h); break;
                default: onChange.finished(h);
            }
        }
    }

    static interface Listener {
        void started(Handle h);
        void progress(Handle h);
        void finished(Handle h);
    }

    static final class Handle implements AutoCloseable {
        final Progress type;
        private long value;
        private long startOffset;
        private long endOffset;

        private Handle(Progress type) {
            this.type = type;
            notifyUpdates(this, 1);
        }

        void progress(long value, long endValue) {
            progress(value, 0, value, endValue);
        }

        void progress(long counter, long startOffset, long value, long endOffset) {
            // keep this method short so that it can be inlined
            if (counter % 100000 == 0) {
                doProgress(value, startOffset, endOffset);
            }
        }

        @Override
        public void close() {
            notifyUpdates(this, 2);
        }

        private void doProgress(long value, long startOffset, long endOffset) {
            this.value = value;
            this.endOffset = endOffset;
            this.startOffset = startOffset;
            notifyUpdates(this, 1);
        }

        long getValue() {
            return value;
        }

        long getStartOffset() {
            return startOffset;
        }

        long getEndOffset() {
            return endOffset;
        }
    }
}
