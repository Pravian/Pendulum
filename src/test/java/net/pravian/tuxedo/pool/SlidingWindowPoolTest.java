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

import com.google.common.truth.Truth;
import static com.google.common.truth.Truth.*;
import java.io.IOException;
import junit.framework.Assert;
import net.pravian.tuxedo.persistence.PersistenceTestUtil;

import org.junit.Test;

public class SlidingWindowPoolTest {

    @Test
    public void testSlidingWindow() {

        SlidingWindowPool pool = new SlidingWindowPool(3);

        assertThat(pool.size()).isEqualTo(0);

        pool.push(4L);

        assertThat(pool.size()).isEqualTo(1);
        assertThat(pool.snapshot().getValues()).asList().containsExactly(4L);

        pool.push(15L);

        assertThat(pool.size()).isEqualTo(2);
        assertThat(pool.snapshot().getValues()).asList().containsExactly(4L, 15L);

        pool.push(7L);

        assertThat(pool.size()).isEqualTo(3);
        assertThat(pool.snapshot().getValues()).asList().containsExactly(4L, 15L, 7L);

        pool.push(9L);

        assertThat(pool.size()).isEqualTo(3);
        assertThat(pool.snapshot().getValues()).asList().containsExactly(15L, 7L, 9L);

        pool.push(21L);

        assertThat(pool.size()).isEqualTo(3);
        assertThat(pool.snapshot().getValues()).asList().containsExactly(7L, 9L, 21L);
    }

    @Test
    public void testClear() {

        SlidingWindowPool pool = new SlidingWindowPool(3);

        assertThat(pool.size()).isEqualTo(0);

        pool.push(4L);
        pool.push(15L);
        pool.push(7L);
        pool.push(9L);
        pool.push(21L);

        assertThat(pool.size()).isEqualTo(3);
        assertThat(pool.snapshot().getValues()).asList().containsExactly(7L, 9L, 21L);

        pool.clear();

        assertThat(pool.size()).isEqualTo(0);
        assertThat(pool.snapshot().getValues()).asList().isEmpty();
    }

    @Test
    public void testPersistence() throws IOException {
        PersistenceTestUtil.testPool(new SlidingWindowPool(10));

        try {
            PersistenceTestUtil.testPool(new SlidingWindowPool(2));
            Truth.assertWithMessage("Did not write all values!").fail();
        } catch (Throwable t) {
        }
    }

}
