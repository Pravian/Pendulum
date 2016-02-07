/*
 * Copyright 2016 Jerom van der Sar
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
package net.pravian.tuxedo.persistence;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PersistenceUtil {

    private PersistenceUtil() {
    }

    public static void writeValues(OutputStream stream, Long[] values) throws IOException {
        long[] lValues = new long[values.length];

        int size = 0;
        for (Long value : values) {
            if (value != null) {
                lValues[size++] = value;
            }
        }

        writeValues(stream, lValues, size);
    }

    public static void writeValues(OutputStream stream, long[] values) throws IOException {
        writeValues(stream, values, values.length);
    }

    private static void writeValues(OutputStream stream, long[] values, int length) throws IOException {
        DataOutputStream dOut = stream instanceof DataOutputStream ? (DataOutputStream) stream : new DataOutputStream(stream);

        dOut.writeInt(length);
        for (long value : values) {
            dOut.writeLong(value);
        }
    }

    public static long[] readValues(InputStream stream) throws IOException {
        DataInputStream dIn = stream instanceof DataInputStream ? (DataInputStream) stream : new DataInputStream(stream);

        int length = dIn.readInt();

        if (length < 0) {
            throw new IOException("Invalid length: " + length);
        }

        long[] values = new long[length];

        for (int i = 0; i < values.length; i++) {
            values[i] = dIn.readLong();
        }

        return values;
    }
}
