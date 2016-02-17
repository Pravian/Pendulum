/*
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import net.pravian.tuxedo.Clock;
import net.pravian.tuxedo.persistence.PersistenceUtil;
import net.pravian.tuxedo.snapshot.SimpleSnapshot;
import net.pravian.tuxedo.snapshot.Snapshot;

/**
 * https://github.com/dropwizard/metrics/blob/master/metrics-core/src/main/java/io/dropwizard/metrics/SlidingTimeWindowReservoir.java
 */
public class SlidingTimeWindowPool implements Pool {

    // allow for this many duplicate ticks before overwriting measurements
    private static final int COLLISION_BUFFER = 256;
    // only trim on updating once every N
    private static final int TRIM_THRESHOLD = 256;

    private final Clock clock;
    private final ConcurrentSkipListMap<Long, Long> values; // Tick -> Value
    private final long window;
    private final AtomicLong lastTick;
    private final AtomicLong count;

    public SlidingTimeWindowPool(long window, TimeUnit windowUnit) {
        this(window, windowUnit, Clock.SYSTEM);
    }

    public SlidingTimeWindowPool(long window, TimeUnit windowUnit, Clock clock) {
        this.clock = clock;
        this.values = new ConcurrentSkipListMap<>();
        this.window = windowUnit.toNanos(window) * COLLISION_BUFFER;
        this.lastTick = new AtomicLong(clock.nanos() * COLLISION_BUFFER);
        this.count = new AtomicLong();
    }

    @Override
    public int size() {
        trim();
        return values.size();
    }

    @Override
    public void push(long value) {
        if (count.incrementAndGet() % TRIM_THRESHOLD == 0) {
            trim();
        }
        values.put(getTick(), value);
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public Snapshot snapshot() {
        trim();
        return SimpleSnapshot.forCollection(values.values());
    }

    @Override
    public Iterator<Long> iterator() {
        return values.values().iterator();
    }

    private long getTick() {
        for (;;) {
            final long oldTick = lastTick.get();
            final long tick = clock.nanos() * COLLISION_BUFFER;
            // ensure the tick is strictly incrementing even if there are duplicate ticks
            final long newTick = tick - oldTick > 0 ? tick : oldTick + 1;
            if (lastTick.compareAndSet(oldTick, newTick)) {
                return newTick;
            }
        }
    }

    private void trim() {
        values.headMap(getTick() - window).clear();
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {

        long[] longValues = new long[values.size() * 2];
        int i = 0;
        for (Long tick : values.keySet()) {
            longValues[i++] = tick;
            longValues[i++] = values.get(tick);
        }

        PersistenceUtil.writeValues(stream, longValues);
    }

    @Override
    public void readFrom(InputStream stream) throws IOException {

        long[] longValues = PersistenceUtil.readValues(stream);

        if (longValues.length % 2 != 0) {
            throw new IOException("Invalid stream format. Amount of values is not even! " + longValues.length + " values.");
        }

        clear();
        for (int i = 0; i < longValues.length; i += 2) {
            values.put(longValues[i], longValues[i + 1]);
        }
    }

}
