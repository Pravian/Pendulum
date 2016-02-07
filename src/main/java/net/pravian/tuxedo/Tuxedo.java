/*
 * Copyright 2015 Jerom van der Sar.
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
package net.pravian.tuxedo;

import java.util.Collections;
import net.pravian.tuxedo.timer.Timer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import net.pravian.tuxedo.pool.Pool;
import net.pravian.tuxedo.pool.PoolFactory;
import net.pravian.tuxedo.pool.StaticPool;
import net.pravian.tuxedo.timer.Split;

public class Tuxedo implements Iterable<Timer> {

    private static final Tuxedo instance = new Tuxedo("Main");
    //
    @Getter
    private final String name;
    //
    private final Map<String, Timer> timers = new HashMap<>();
    @Getter
    @Setter
    private Clock clock = Clock.SYSTEM;
    @Getter
    @Setter
    private PoolFactory poolFactory = PoolFactory.DEFAULT;

    public Tuxedo(String name) {
        this.name = name;
    }

    public Set<Timer> getTimers() {
        return Collections.unmodifiableSet(new HashSet<>(timers.values()));
    }

    public void clear() {
        timers.clear();
    }

    public int size() {
        return timers.size();
    }

    public Timer timer(String name) {
        return timer(name, poolFactory.createPool());
    }

    public Timer timer(String name, Pool pool) {
        Timer timer = timers.get(name);

        if (timer == null) {
            timer = new Timer(this, name, pool);
            timers.put(name, timer);
        }

        return timer;
    }

    public Split start(String name) {
        return timer(name).start();
    }

    @Override
    public Iterator<Timer> iterator() {
        return timers.values().iterator();
    }

    public static Tuxedo instance() {
        return instance;
    }

}
