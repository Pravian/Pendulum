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

import static com.google.common.truth.Truth.*;

import org.junit.Test;

public class SimpleSnapshotTest {

    @Test
    public void testCase() {
        // https://en.wikipedia.org/wiki/Standard_deviation
        long[] values = {
            2, 4, 4, 4, 5, 5, 7, 9
        };

        Snapshot snap = SimpleSnapshot.forValues(values);

        assertThat(snap.getMin()).isEqualTo(2L);
        assertThat(snap.getMax()).isEqualTo(9L);
        assertThat(snap.getTotal()).isEqualTo(40L);
        assertThat(snap.getMean()).isEqualTo(5L);
        assertThat(snap.getMedian()).isAnyOf(4L, 5L);
        assertThat(snap.getVariance()).isEqualTo(4L);
        assertThat(snap.getStandardDeviation()).isEqualTo(2L);
    }

}
