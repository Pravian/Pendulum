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
import java.util.List;
import net.pravian.tuxedo.pool.Pool;
import net.pravian.tuxedo.snapshot.Snapshot;
import net.pravian.tuxedo.timer.Timer;

public class PersistenceTestUtil {

    private static final long[] values = {
        153030229,
        234110223,
        945511402,
        420499952,
        233123055,
        23,
        123662
    };

    private static final byte[] data;

    static {
        ByteArrayOutputStream valuesBaos = new ByteArrayOutputStream();
        DataOutputStream valuesDos = new DataOutputStream(valuesBaos);

        try {

            valuesDos.writeInt(values.length);
            for (long value : values) {
                valuesDos.writeLong(value);
            }
            valuesDos.flush();
        } catch (Exception ex) {
            assertWithMessage("Could not serialise data!").fail();
        }
        //

        data = valuesBaos.toByteArray();
        assertThat(data.length).isEqualTo(values.length * 8 + 4);
    }

    public static void testPool(Pool pool) throws IOException {
        assertThat(pool.size()).isEqualTo(0);

        writeValues(pool);

        assertThat(pool.size()).isEqualTo(values.length);

        readAndAssertValues(pool);

    }

    public static void testTimer(Timer timer) throws IOException {
        assertThat(timer.size()).isEqualTo(0);

        writeValues(timer);

        assertThat(timer.size()).isEqualTo(values.length);

        assertValues(timer.snapshot());
        readAndAssertValues(timer);
    }

    private static void writeValues(Persistable pers) throws IOException {
        pers.readFrom(new ByteArrayInputStream(data));
    }

    private static void assertValues(Snapshot shot) {
        List<Long> valuesList = new ArrayList<>(values.length);

        for (long value : values) {
            valuesList.add(value);
        }

        assertThat(shot.getValues())
                .asList()
                .containsExactlyElementsIn(valuesList);
    }

    private static void readAndAssertValues(Persistable pers) throws IOException {
        ByteArrayOutputStream resultBaos = new ByteArrayOutputStream();
        DataOutputStream resultDos = new DataOutputStream(resultBaos);
 
        pers.writeTo(resultDos);
        resultDos.flush();

        assertThat(resultBaos.toByteArray()).isEqualTo(data);

    }

}
