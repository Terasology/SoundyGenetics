/*
 * Copyright 2019 MovingBlocks
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
package org.terasology.projsndwv.genetics;

import org.terasology.projsndwv.genetics.components.GeneticsComponent;
import org.terasology.utilities.random.MersenneRandom;

import java.util.Iterator;

public class Genome {
    private MersenneRandom random;
    private final int size;

    public Genome(int size, long seed) {
        this.size = size;
        random = new MersenneRandom(seed);
    }

    public Iterator<GeneticsComponent> combine(GeneticsComponent g0, GeneticsComponent g1) {
        if (!g0.isValid() || !g1.isValid()) {
            return null; // TODO: Error handle properly
        }
        else if (g0.size != size || g1.size != size) {
            return null;
        }

        return new Iterator<GeneticsComponent>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public GeneticsComponent next() {
                GeneticsComponent result = new GeneticsComponent(size);

                for (int i = 0; i < size; i++) {
                    result.activeGenes.add(random.nextBoolean() ? g0.activeGenes.get(i) : g0.inactiveGenes.get(i));
                    result.inactiveGenes.add(random.nextBoolean() ? g1.activeGenes.get(i) : g1.inactiveGenes.get(i));
                }

                return result;
            }
        };
    }
}
