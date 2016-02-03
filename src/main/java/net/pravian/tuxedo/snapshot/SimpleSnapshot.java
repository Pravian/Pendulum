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

import java.util.Arrays;
import java.util.Collection;
import lombok.Getter;
import net.pravian.tuxedo.MathUtil;
import net.pravian.tuxedo.timer.Split;

public class SimpleSnapshot implements Snapshot {

    @Getter
    private final long[] values;
    @Getter
    private final long total;
    @Getter
    private final long mean;
    @Getter
    private final long min;
    @Getter
    private final long max;
    @Getter
    private final long median;
    @Getter
    private final long variance;

    private SimpleSnapshot(long[] values) {
        Arrays.sort(values);

        this.values = values;

        int index = 0;
        long tempTotal = 0;
        long tempMin = Long.MAX_VALUE;
        long tempMax = Long.MIN_VALUE;
        for (long value : values) {

            values[index] = value;
            tempTotal += value;

            if (value < tempMin) {
                tempMin = value;
            }

            if (value > tempMax) {
                tempMax = value;
            }

            index++;
        }

        total = tempTotal;
        mean = tempTotal / values.length;

        min = tempMin;
        max = tempMax;

        // Median
        int middleIndex = (int) Math.floor((double) values.length / 2.0);
        if (values.length % 2 == 1) {
            this.median = values[middleIndex];
        } else {
            this.median = (values[middleIndex] + values[middleIndex + 1]) / 2;
        }

        // Variance
        long cumVarianceTotal = 0;
        for (long value : values) {
            long diff = (value - mean);
            cumVarianceTotal += diff * diff;
        }
        variance = cumVarianceTotal / values.length;
    }

    @Override
    public long getStandardDeviation() {
        return MathUtil.sqrt(variance);
    }

    public static SimpleSnapshot forValues(long[] values) {
        return new SimpleSnapshot(Arrays.copyOf(values, values.length));
    }

    public static SimpleSnapshot forCollection(Collection<Long> coll) {
        long[] values = new long[coll.size()];

        int index = 0;
        for (Long value : coll) {
            values[index] = value;
            index++;
        }

        return new SimpleSnapshot(values);
    }

}
