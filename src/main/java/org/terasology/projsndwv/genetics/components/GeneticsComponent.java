/*
 * Copyright 2018 MovingBlocks
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
package org.terasology.projsndwv.genetics.components;

import org.terasology.entitySystem.Component;

import java.util.ArrayList;
import java.util.List;

/***
 * Stores a genome where one allele at a locus is expressed and the other is repressed.
 */
public final class GeneticsComponent implements Component {
    /**
     * Number of loci in this genome.
     */
    public int size;

    /**
     * List of expressed alleles.
     */
    public List<Integer> activeGenes;
    /**
     * List of repressed alleles.
     */
    public List<Integer> inactiveGenes;

    /**
     * Constructs a {@code GeneticsComponent} with no size. Intended only for loading.
     */
    public GeneticsComponent() {
        activeGenes = new ArrayList<>();
        inactiveGenes = new ArrayList<>();
    }

    /**
     * Constructs a {@code GeneticsComponent} of a given size.
     * @param size Number of loci in the genome for this component
     */
    public GeneticsComponent(int size) {
        this.size = size;
        activeGenes = new ArrayList<>(size);
        inactiveGenes = new ArrayList<>(size);
    }

    /**
     * Determines if this component is valid.
     * @return True if and only if there is the same number of active and inactive alleles, and the number of each
     * matches the number of loci.
     */
    public boolean isValid() {
        return activeGenes.size() == inactiveGenes.size() && activeGenes.size() == size;
    }
}
