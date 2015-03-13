/*

    ngs-fca  Formal concept analysis for genomics.
    Copyright (c) 2014-2015 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.gnu.org/licenses/lgpl.html

*/
package org.nmdp.ngs.fca;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Formal concepts and their partial ordering.
 */
public final class Concept implements Partial<Concept> {
    private final BitSet extent;
    private final BitSet intent;

    /**
     * Construct a concept with given objects (extent) and attributes (intent).
     *
     * @param extent objects
     * @param intent attributes
     */
    public Concept(final BitSet extent, final BitSet intent) {
        this.extent = extent;
        this.intent = intent;
    }

    /**
     * Retrieve a concept's shared objects.
     *
     * @return extent
     */
    // todo:  shared objects --> bitset?  needs clarification
    public BitSet extent() {
        return extent;
    }

    /**
     * Retrieve a concept's shared attributes.
     *
     * @return intent
     */
    // todo:  shared attributes --> bitset?  needs clarification
    public BitSet intent() {
        return intent;
    }

    /**
     * Decode an object list from its bit membership.
     *
     * @param bits where each set bit represents membership in the given group
     * @param group list of all members
     * @return immutable list of members
     */
    // todo:  lists should be typed
    public static List decode(final BitSet bits, final List group) {
        List members = new ArrayList();

        for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {
            members.add(group.get(i));
        }
        return members;
    }

    /**
     * Encode bit membership from a list of objects.
     *
     * @param members to encode
     * @param group list of all members
     * @return bits where each set bit represents membership in the group
     */
    public static BitSet encode(final List members, final List group) {
        BitSet bits = new BitSet();

        for (Object object : members) {
            int index = group.indexOf(object);
            if (index >= 0) {
                bits.flip(index);
            }
        }
        return bits;
    }

    /**
     * Define the partial ordering of two concepts.
     *
     * @param that that concept
     * @return partial ordering of this and that concept
     */
    @Override
    public Ordering ordering(final Concept that) {
        BitSet meet = (BitSet) this.intent.clone();
        meet.and(that.intent);

        if (this.intent.equals(that.intent)) {
            return Partial.Ordering.EQUAL;
        }
        if (this.intent.equals(meet)) {
            return Partial.Ordering.LESS;
        }
        if (that.intent.equals(meet)) {
            return Partial.Ordering.GREATER;
        }
        return Partial.Ordering.NONCOMPARABLE;
    }

    @Override
    public String toString() {
        return intent.toString();
    }
}
