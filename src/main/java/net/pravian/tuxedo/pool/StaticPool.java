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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.pravian.tuxedo.snapshot.SimpleSnapshot;
import net.pravian.tuxedo.snapshot.Snapshot;

public class StaticPool implements Pool {

    private final List<Long> values = new ArrayList<>();

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
        return SimpleSnapshot.forCollection(values);
    }

    @Override
    public Iterator<Long> iterator() {
        return values.iterator();
    }

}
