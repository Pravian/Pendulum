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
package net.pravian.tuxedo.pool;

import static com.google.common.truth.Truth.*;
import java.io.IOException;
import net.pravian.tuxedo.persistence.PersistenceTestUtil;

import org.junit.Test;

public class StaticPoolTest {

    @Test
    public void testValues() {
        StaticPool pool = new StaticPool();

        pool.push(1);
        pool.push(2);
        pool.push(3);
        pool.push(7);

        assertThat(pool.size()).isEqualTo(4);

        long[] values = pool.snapshot().getValues();

        assertThat(values[0]).isEqualTo(1);
        assertThat(values[1]).isEqualTo(2);
        assertThat(values[2]).isEqualTo(3);
        assertThat(values[3]).isEqualTo(7);
    }

    @Test
    public void testSize() {
        StaticPool pool = new StaticPool();

        assertThat(pool.size()).isEqualTo(0);

        pool.push(1);
        pool.push(2);
        pool.push(3);
        pool.push(7);

        assertThat(pool.size()).isEqualTo(4);
        pool.clear();
        assertThat(pool.size()).isEqualTo(0);
    }

    @Test
    public void testPersistence() throws IOException {
        PersistenceTestUtil.testPool(new StaticPool());
    }

}
