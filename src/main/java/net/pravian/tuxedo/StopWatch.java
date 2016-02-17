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
package net.pravian.tuxedo;

public class StopWatch implements Timable {

    protected final Clock clock;
    //
    protected boolean running;
    protected long start;
    protected long stop;

    public StopWatch() {
        this(Clock.SYSTEM);
    }

    public StopWatch(Clock clock) {
        this.clock = clock;
        reset();
    }

    public synchronized final boolean isRunning() {
        return running;
    }

    public synchronized final Clock getClock() {
        return clock;
    }

    public synchronized final void reset() {
        running = false;
        start = 0;
        stop = 0;
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        start = clock.nanos();
    }

    public synchronized long stop() {
        if (!running) {
            return stop - start;
        }
        running = false;
        return (stop = clock.nanos()) - start;
    }

    public synchronized long current() {
        return start - clock.nanos();
    }

    @Override
    public synchronized long getTimeNanos() {
        if (isRunning()) {
            throw new IllegalStateException("StopWatch is still running!");
        }

        return stop - start;
    }

}
