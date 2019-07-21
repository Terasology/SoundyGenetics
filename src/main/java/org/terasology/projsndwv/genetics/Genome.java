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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Handles genetic combination, including mutations.
 */
public class Genome {
    private MersenneRandom random;
    private final int size;

    private List<Map<AllelePair, List<Mutation>>> mutations;

    /**
     * Constructs a {@code Genome} with a seed and given number of loci.
     * @param size Number of loci in this genome.
     * @param seed Seed for the combination RNG.
     */
    public Genome(int size, long seed) {
        this.size = size;
        random = new MersenneRandom(seed);

        mutations = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            mutations.add(new HashMap<>());
        }
    }

    /**
     * Combines two genomes, evaluating mutations.
     * @param g0 First genome to combine.
     * @param g1 Second genome to combine.
     * @return An iterator returning genomes that are random, mutated combinations of the input genomes, or null if
     * the input genomes are of different sizes or are invalid.
     */
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

                GeneticsComponent g0mutated = mutate(g0, possibleMutations);
                GeneticsComponent g1mutated = mutate(g1, possibleMutations);

                for (int i = 0; i < size; i++) {
                    result.activeGenes.add(random.nextBoolean() ? g0mutated.activeGenes.get(i) : g0mutated.inactiveGenes.get(i));
                    result.inactiveGenes.add(random.nextBoolean() ? g1mutated.activeGenes.get(i) : g1mutated.inactiveGenes.get(i));
                }

                return result;
            }
        };
    }

    private GeneticsComponent mutate(GeneticsComponent component, List<Mutation> mutations) {
        List<Mutation> mutationsCopy = new ArrayList<>(mutations);
        Collections.shuffle(mutationsCopy);

        for (Mutation mutation : mutationsCopy) {
            if (random.nextFloat() < mutation.chance) {
                return mutation.component;
            }
        }

        return component;
    }

    // TODO: GeneticsComponents should probably consist of two separated genomes. This will allow for non-binary genetics and less data in these overrides later on.

    /**
     * Registers an override mutation to be factored into combinations performed with this {@code Genome}. An override
     * mutation mutates the entire genome of a parent if a given pair of alleles is expressed by the parents at a given
     * locus.
     *
     * For example, consider a mutation registered at locus 1 with alleles '1' and '2', and override genetics '3 3 3'.
     *
     * The following parents could trigger it, as they have a 1-2 pair at locus 1:<br/>
     * <br/>
     * Parent 1: 1 2 1<br/>
     * Parent 2: 1 1 1<br/>
     * <br/>
     * The following parents could not, as they only have the specified pair at locus 2:<br/>
     * <br/>
     * Parent 1: 1 1 1<br/>
     * Parent 2: 1 1 2<br/>
     * <br/>
     *
     * Suppose the mutation triggered in the first case, the chance was evaluated and a mutation occurred for parent 2. As
     * this is an override mutation, parent 2 will now contribute to combination as if it had the genetics '3 3 3', and
     * offspring for that combination will be as if the parents were '1 1 1' and '3 3 3'.
     * @param locus Locus this mutation is triggered by.
     * @param allele0 First allele that must be present at the given locus.
     * @param allele1 Second allele that must be present at the given locus.
     * @param override Genome resulting from this mutation.
     * @param chance Probability of this mutation occurring if the trigger is met.
     */
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
