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

import java.util.*;

public class Genome {
    private MersenneRandom random;
    private final int size;

    private List<Map<AllelePair, List<Mutation>>> mutations;

    public Genome(int size, long seed) {
        this.size = size;
        random = new MersenneRandom(seed);

        mutations = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            mutations.add(new HashMap<>());
        }
    }

    public Iterator<GeneticsComponent> combine(GeneticsComponent g0, GeneticsComponent g1) {
        if (!g0.isValid() || !g1.isValid()) {
            return null;
        }
        else if (g0.size != size || g1.size != size) {
            return null;
        }

        List<Mutation> possibleMutations = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Map<AllelePair, List<Mutation>> mutationsForLocus = mutations.get(i);
            AllelePair ap = new AllelePair(g0.activeGenes.get(i), g1.activeGenes.get(i)); // TODO: Remove dependence on active genes
            if (mutationsForLocus.containsKey(ap)) {
                possibleMutations.addAll(mutationsForLocus.get(ap));
            }
        }

        return new Iterator<GeneticsComponent>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public GeneticsComponent next() {
                GeneticsComponent result = new GeneticsComponent(size);

                GeneticsComponent g0mutated = g0;
                GeneticsComponent g1mutated = g1;

                Collections.shuffle(possibleMutations);

                for (Mutation mutation : possibleMutations) {
                    if (random.nextFloat() < mutation.chance) {
                        g0mutated = mutation.component;
                        break;
                    }
                }

                Collections.shuffle(possibleMutations);

                for (Mutation mutation : possibleMutations) {
                    if (random.nextFloat() < mutation.chance) {
                        g1mutated = mutation.component;
                        break;
                    }
                }

                for (int i = 0; i < size; i++) {
                    result.activeGenes.add(random.nextBoolean() ? g0mutated.activeGenes.get(i) : g0mutated.inactiveGenes.get(i));
                    result.inactiveGenes.add(random.nextBoolean() ? g1mutated.activeGenes.get(i) : g1mutated.inactiveGenes.get(i));
                }

                return result;
            }
        };
    }

    // TODO: GeneticsComponents should probably consist of two separated genomes. This will allow for non-binary genetics and less data in these overrides later on.
    public void registerMutation(int locus, int allele0, int allele1, GeneticsComponent override, float chance) {
        AllelePair ap = new AllelePair(allele0, allele1);
        Map<AllelePair, List<Mutation>> mutationsForLocus = mutations.get(locus);
        List<Mutation> mutationList = mutationsForLocus.get(ap);
        if (mutationList != null) {
            mutationList.add(new Mutation(chance, override));
        }
        else {
            mutationsForLocus.put(ap, new ArrayList<>(Collections.singletonList(new Mutation(chance, override))));
        }
    }

    private final class AllelePair {
        private int allele0;
        private int allele1;

        public AllelePair(int a0, int a1) {
            if (a0 <= a1) {
                allele0 = a0;
                allele1 = a1;
            }
            else {
                allele0 = a1;
                allele1 = a0;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof  AllelePair)) {
                return false;
            }
            AllelePair ap = (AllelePair)o;
            return allele0 == ap.allele0 && allele1 == ap.allele1;
        }

        @Override
        public int hashCode() {
            return 31 * allele0 + allele1;
        }
    }

    private final class Mutation {
        public float chance;
        public GeneticsComponent component;

        public Mutation(float chance, GeneticsComponent component) {
            this.chance = chance;
            this.component = component;
        }
    }
}
