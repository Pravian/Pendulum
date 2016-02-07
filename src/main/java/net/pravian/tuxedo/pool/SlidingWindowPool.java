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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import net.pravian.tuxedo.persistence.PersistenceUtil;
import net.pravian.tuxedo.snapshot.SimpleSnapshot;
import net.pravian.tuxedo.snapshot.Snapshot;

public class SlidingWindowPool implements Pool {

    private final long[] values;
    private int current;

    public SlidingWindowPool(int size) {
        this.values = new long[size];
    }

    @Override
    public synchronized int size() {
        return Math.min(current, values.length);
    }

    @Override
    public void push(long value) {
        values[(current++ % values.length)] = value;
    }

    @Override
    public void clear() {
        for (int i = 0; i < values.length; i++) {
            values[i] = 0;
        }
        current = 0;
    }

    @Override
    public Snapshot snapshot() {
        return SimpleSnapshot.forDirectValues(values());
    }

    @Override
    public Iterator<Long> iterator() {
        return snapshot().iterator();
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        PersistenceUtil.writeValues(stream, values());
    }

    @Override
    public void readFrom(InputStream stream) throws IOException {
        long[] readValues = PersistenceUtil.readValues(stream);

        clear();
        for (long value : readValues) {
            push(value);
        }
    }

    private long[] values() {
        final long[] snapValues = new long[size()];
        System.arraycopy(values, 0, snapValues, 0, snapValues.length);
        return snapValues;
    }

}
