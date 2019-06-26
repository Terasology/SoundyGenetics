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

import java.util.Random;

public class Genome {
    private Random random = new Random(); // TODO: Use world random.

    public GeneticsComponent recombine(GeneticsComponent g0, GeneticsComponent g1) {
        if (g0.activeGenes.size() != g0.inactiveGenes.size() || g1.activeGenes.size() != g1.inactiveGenes.size()) {
            throw new RuntimeException("Malformed Genomes"); // TODO: Error handle properly
        }
        else if (g0.activeGenes.size() != g1.activeGenes.size()) {
            throw new RuntimeException("Mismatched Genomes");
        }

        GeneticsComponent result = new GeneticsComponent();

        for (int i = 0; i < g0.activeGenes.size(); i++) {
            result.activeGenes.add(random.nextBoolean() ? g0.activeGenes.get(i) : g0.inactiveGenes.get(i));
            result.inactiveGenes.add(random.nextBoolean() ? g1.activeGenes.get(i) : g1.inactiveGenes.get(i));
        }

        return result;
    }
}
