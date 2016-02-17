/*
 * Copyright 2016 Jerom van der Sar
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
package net.pravian.tuxedo.persistence;

import static com.google.common.truth.Truth.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.pravian.tuxedo.pool.Pool;
import net.pravian.tuxedo.snapshot.Snapshot;
import net.pravian.tuxedo.timer.Timer;

public class PersistenceTestUtil {

    private static final long[] values = {
        153030229,
        234110223,
        72,
        945511402,
        420499952,
        932,
        233123055,
        23,
        123662,
        15,
        3,};

    private static final List<Long> valuesList;

    static {
        List<Long> tempValuesList = new ArrayList<>(values.length);

        for (long value : values) {
            tempValuesList.add(value);
        }

        valuesList = Collections.unmodifiableList(tempValuesList);

        assertWithMessage("Could not compile values list!").that(values).asList().containsExactlyElementsIn(valuesList).inOrder();
    }

    public static void testPool(Pool pool) throws IOException {
        assertThat(pool.size()).isEqualTo(0);

        // Push values
        for (long value : values) {
            pool.push(value);
        }

        // Assert contents
        assertThat(pool.size()).isEqualTo(values.length);
        assertThat(pool.snapshot().getValues()).asList().containsExactlyElementsIn(valuesList);

        // Write values to a byte array
        byte[] poolBytes = writeBytes(pool);

        // Clear
        pool.clear();
        assertThat(pool.size()).isEqualTo(0);

        // Read the values back again
        readBytes(pool, poolBytes);

        // Assert contents
        assertThat(pool.size()).isEqualTo(values.length);
        assertThat(pool.snapshot().getValues()).asList().containsExactlyElementsIn(valuesList);
    }

    public static void testTimer(Timer timer) throws IOException {
        assertThat(timer.size()).isEqualTo(0);

        // Push values
        for (long value : values) {
            timer.time(value);
        }

        // Assert contents
        assertThat(timer.size()).isEqualTo(values.length);
        assertThat(timer.snapshot().getValues()).asList().containsExactlyElementsIn(valuesList);

        // Write values to a byte array
        byte[] timerBytes = writeBytes(timer);

        // Clear
        timer.clear();
        assertThat(timer.size()).isEqualTo(0);

        // Read the values back again
        readBytes(timer, timerBytes);

        // Assert contents
        assertThat(timer.size()).isEqualTo(values.length);
        assertThat(timer.snapshot().getValues()).asList().containsExactlyElementsIn(valuesList);
    }

    private static byte[] writeBytes(Persistable pers) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            pers.writeTo(baos);
            return baos.toByteArray();
        }
    }

    private static void readBytes(Persistable pers, byte[] bytes) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            pers.readFrom(bais);
        }
    }

}
