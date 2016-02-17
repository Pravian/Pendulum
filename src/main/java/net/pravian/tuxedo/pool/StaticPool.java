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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.pravian.tuxedo.persistence.PersistenceUtil;
import net.pravian.tuxedo.snapshot.SimpleSnapshot;
import net.pravian.tuxedo.snapshot.Snapshot;

public class StaticPool implements Pool {

    private final List<Long> values = Collections.synchronizedList(new ArrayList<Long>());

    @Override
    public void push(long val) {
        values.add(val);
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public Snapshot snapshot() {
        synchronized (values) {
            return SimpleSnapshot.forCollection(values);
        }
    }

    @Override
    public Iterator<Long> iterator() {
        synchronized (values) {
            return values.iterator();
        }
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        synchronized (values) {
            PersistenceUtil.writeValues(stream, values.toArray(new Long[0]));
        }
    }

    @Override
    public void readFrom(InputStream stream) throws IOException {
        long[] readValues = PersistenceUtil.readValues(stream);

        clear();
        for (long value : readValues) {
            push(value);
        }
    }

}
