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

import net.pravian.tuxedo.timer.Timer;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import net.pravian.tuxedo.pool.StaticPool;
import net.pravian.tuxedo.timer.Split;

public class Tuxedo {

    private static final Tuxedo instance = new Tuxedo("Main");
    //
    @Getter
    private final String name;
    //
    private final Map<String, Timer> timers = new HashMap<>();
    @Getter
    @Setter
    private Clock clock = Clock.SYSTEM;

    public Tuxedo(String name) {
        this.name = name;
    }

    public Timer timer(String name) {
        Timer timer = timers.get(name);

        if (timer == null) {
            timer = new Timer(this, name, new StaticPool()); // TODO
            timers.put(name, timer);
        }

        return timer;
    }

    public Split start(String name) {
        return timer(name).start();
    }

    public static Tuxedo instance() {
        return instance;
    }

}
