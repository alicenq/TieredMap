/*
 * The MIT License
 *
 * Copyright 2014 Rogue <Alice Q.>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package rogue.util.test;

import rogue.util.*;

/**
 * Main method for running test cases
 *
 * @author Rogue <Alice Q.>
 */
public class TestProgram {

    /**
     * Main method
     *
     * @param args unused
     */
    public static void main(String[] args) {
        TieredMap root = new TieredMap();

        TieredMap a0 = root.child();
        TieredMap b0 = root.child();
        TieredMap c0 = root.child();

        TieredMap c1 = c0.child();
        TieredMap c2 = c1.child();

        TieredMap c3 = c1.sibling();

        TieredMap d1 = c3.child();
        TieredMap e1 = d1.child();

        a0.put("a0", 0);
        b0.put("b0", 0);
        c0.put("c0", 0);
        c1.put("c1", 1);
        c2.put("c2", 2);
        c3.put("c3", 3);
        d1.put("d1", 1);
        e1.inherit("d1");
        e1.inherit("a0");

        TieredMap a1 = a0.child();

        System.out.println(TieredMap.toGraph(c2));

        c0.detach();
        System.out.println(c0.getChildren());
    }
}
