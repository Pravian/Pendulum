/*
 * Copyright 2016 Jerom van der Sar.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.pravian.tuxedo.snapshot;

import java.util.Iterator;
import lombok.Getter;

public class EmptySnapShot implements Snapshot {

    @Getter
    private final long[] values = new long[0];

    @Override
    public long getTotal() {
        return 0;
    }

    @Override
    public long getMean() {
        return 0;
    }

    @Override
    public long getMin() {
        return 0;
    }

    @Override
    public long getMax() {
        return 0;
    }

    @Override
    public long getMedian() {
        return 0;
    }

    @Override
    public long getVariance() {
        return 0;
    }

    @Override
    public long getStandardDeviation() {
        return 0;
    }

    @Override
    public Iterator<Long> iterator() {
        return new LongIterator(values);
    }

}
