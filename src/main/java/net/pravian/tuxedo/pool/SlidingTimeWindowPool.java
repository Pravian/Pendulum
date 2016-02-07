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


import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import net.pravian.tuxedo.Clock;
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
    private final ConcurrentSkipListMap<Long, Long> measurements;
    private final long window;
    private final AtomicLong lastTick;
    private final AtomicLong count;

    public SlidingTimeWindowPool(long window, TimeUnit windowUnit) {
        this(window, windowUnit, Clock.SYSTEM);
    }

    public SlidingTimeWindowPool(long window, TimeUnit windowUnit, Clock clock) {
        this.clock = clock;
        this.measurements = new ConcurrentSkipListMap<>();
        this.window = windowUnit.toNanos(window) * COLLISION_BUFFER;
        this.lastTick = new AtomicLong(clock.nanos() * COLLISION_BUFFER);
        this.count = new AtomicLong();
    }

    @Override
    public int size() {
        trim();
        return measurements.size();
    }
    
    @Override
    public void push(long value) {
        if (count.incrementAndGet() % TRIM_THRESHOLD == 0) {
            trim();
        }
        measurements.put(getTick(), value);
    }

    @Override
    public void clear() {
        measurements.clear();
    }

    @Override
    public Snapshot snapshot() {
        trim();
        return SimpleSnapshot.forCollection(measurements.values());
    }

    @Override
    public Iterator<Long> iterator() {
        return measurements.values().iterator();
    }

    private long getTick() {
        for (; ; ) {
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
        measurements.headMap(getTick() - window).clear();
    }
}
