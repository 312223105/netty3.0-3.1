/*
 * Copyright (C) 2008  Trustin Heuiseung Lee
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, 5th Floor, Boston, MA 02110-1301 USA
 */
package net.gleamynode.netty.handler.codec.frame;

import net.gleamynode.netty.array.ByteArray;
import net.gleamynode.netty.array.HeapByteArray;

/**
 * @author The Netty Project (netty@googlegroups.com)
 * @author Trustin Lee (trustin@gmail.com)
 *
 * @version $Rev$, $Date$
 *
 */
public class Delimiters {

    public static ByteArray[] newNulDelimiter() {
        return new ByteArray[] {
                new HeapByteArray(new byte[] { 0 }) };
    }

    public static ByteArray[] newLineDelimiter() {
        return new ByteArray[] {
                new HeapByteArray(new byte[] { '\r', '\n' }),
                new HeapByteArray(new byte[] { '\n' }),
                new HeapByteArray(new byte[] { '\r' }) };
    }

    private Delimiters() {
        // Unused
    }
}
