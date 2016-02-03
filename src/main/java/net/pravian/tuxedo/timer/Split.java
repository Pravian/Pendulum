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

import lombok.Getter;
import net.pravian.tuxedo.Timable;

public class Split implements Timable {

    @Getter
    private final Timer timer;
    @Getter
    private final long start;
    @Getter
    private long stop = 0;

    private Split(Timer timer) {
        this.timer = timer;
        this.start = timer.getClock().nanos();
    }

    public boolean isTiming() {
        return stop == 0;
    }

    public void stop() {
        if (!isTiming()) {
            return;
        }
        stop = timer.getClock().nanos();

        timer.add(this);
    }

    @Override
    public long getTimeNanos() {
        return stop - start;
    }

    public static Split start(Timer timer) {
        return new Split(timer);
    }

}
