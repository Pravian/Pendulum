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
package net.pravian.tuxedo.timer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.concurrent.Callable;
import lombok.Getter;
import net.pravian.tuxedo.Clock;
import net.pravian.tuxedo.Tuxedo;
import net.pravian.tuxedo.persistence.Persistable;
import net.pravian.tuxedo.pool.Pool;
import net.pravian.tuxedo.pool.StaticPool;
import net.pravian.tuxedo.snapshot.Snapshot;
import net.pravian.tuxedo.snapshot.Snapshottable;

public class Timer implements Snapshottable, Persistable, Iterable<Long> {

    @Getter
    private final String name;
    @Getter
    private final Clock clock;
    private final Pool pool;

    public Timer(Tuxedo pendulum, String name, Pool pool) {
        this(name, pendulum.getClock(), pool);
    }

    public Timer(String name) {
        this(name, Clock.SYSTEM);
    }

    public Timer(String name, Pool pool) {
        this(name, Clock.SYSTEM, pool);
    }

    public Timer(String name, Clock clock) {
        this(name, clock, new StaticPool());
    }

    public Timer(String name, Clock clock, Pool pool) {
        this.name = name;
        this.clock = clock;
        this.pool = pool;
    }

    public Split start() {
        return Split.start(this);
    }
        
    public long time(Split time) {
        return time(time.getTimeNanos());
    }

    public long time(Runnable runnable) {
        long start = clock.nanos();
        try {
            runnable.run();
            return time(clock.nanos() - start);
        } catch (Throwable throwable) {
            time(clock.nanos() - start);
            throw throwable;
        }
    }
    
    public <T> T time(Callable<T> callable) throws Exception {
        long start = clock.nanos();
        try {
            return callable.call();
        } finally {
            time(clock.nanos() - start);
        }
    }

    public long time(long time) {
        pool.push(time);
        return time;
    }
    
    public int size() {
        return pool.size();
    }

    public void clear() {
        pool.clear();
    }

    @Override
    public Snapshot snapshot() {
        return pool.snapshot();
    }

    @Override
    public Iterator<Long> iterator() {
        return pool.iterator();
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        pool.writeTo(stream);
    }

    @Override
    public void readFrom(InputStream stream) throws IOException {
        pool.readFrom(stream);
    }

}
