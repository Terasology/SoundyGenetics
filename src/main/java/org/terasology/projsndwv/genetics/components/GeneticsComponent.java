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

public final class GeneticsComponent implements Component {
    public int size;

    public List<Integer> activeGenes;
    public List<Integer> inactiveGenes;

    public GeneticsComponent() {
        activeGenes = new ArrayList<>();
        inactiveGenes = new ArrayList<>();
    }

    public GeneticsComponent(int size) {
        this.size = size;
        activeGenes = new ArrayList<>(size);
        inactiveGenes = new ArrayList<>(size);
    }

    public boolean isValid() {
        return activeGenes.size() == inactiveGenes.size() && activeGenes.size() == size;
    }
}
