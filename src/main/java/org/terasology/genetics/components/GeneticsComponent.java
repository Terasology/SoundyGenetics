// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.genetics.components;

import com.google.common.collect.Lists;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.module.inventory.components.ItemDifferentiating;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/***
 * Stores a genome where one allele at a locus is expressed and the other is repressed.
 *
 * A genome consists of a number of loci (singular: locus). In a simple genome, each locus is associated with a
 * particular trait (called a phenotype) the genome encodes. In more complex genomes, more than one locus may be
 * associated with a trait. A locus consists of a pair of genetic sequences called alleles. In this class, distinct
 * alleles are represented by distinct integer values (rather than genetic sequences). The active allele for each locus
 * is the the allele that encodes the information that traits are based on for that locus (known as the genotype). The
 * inactive allele is ignored for the purpose of determining genotype, however, it participates in genetic combination.
 */
public final class GeneticsComponent implements Component<GeneticsComponent>, ItemDifferentiating { // TODO: Make dependence on Inventory conditional
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
     * Constructs a {@code GeneticsComponent} with no size. Intended only for deserialization. (All components must have
     * a default constructor to deserialize correctly.)
     */
    @SuppressWarnings("unused")
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

    public boolean equals(Object o) {
        if (!(o instanceof GeneticsComponent)) {
            return false;
        }
        GeneticsComponent geneticsComponent = (GeneticsComponent) o;
        if (size != geneticsComponent.size || activeGenes.size() != geneticsComponent.activeGenes.size() || inactiveGenes.size() != geneticsComponent.inactiveGenes.size()) {
            return false;
        }

        for (int i = 0; i < activeGenes.size(); i++) {
            if (!activeGenes.get(i).equals(geneticsComponent.activeGenes.get(i))) {
                return false;
            }
        }

        for (int i = 0; i < inactiveGenes.size(); i++) {
            if (!inactiveGenes.get(i).equals(geneticsComponent.inactiveGenes.get(i))) {
                return false;
            }
        }

        return true;
    }

    public int hashCode() {
        return Objects.hash(size, activeGenes, inactiveGenes);
    }

    @Override
    public void copy(GeneticsComponent other) {
        this.size = other.size;
        this.activeGenes = Lists.newArrayList(other.activeGenes);
        this.inactiveGenes = Lists.newArrayList(other.inactiveGenes);
    }
}
