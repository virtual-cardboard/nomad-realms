package engine.common.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;

/**
 * A loader that manages a dependency graph of other loaders.
 */
public class GraphLoader implements Loader<Map<String, Object>> {

    private final Map<String, Function<Map<String, Object>, Loader<?>>> loaderProviders = new HashMap<>();
    private final Map<String, String[]> dependencies = new HashMap<>();

    /**
     * Adds a loader to the graph.
     *
     * @param name The name of the resource to be loaded.
     * @param provider A function that takes a map of already loaded resources and returns a Loader.
     * @param deps The names of the resources that this loader depends on.
     */
    public void add(String name, Function<Map<String, Object>, Loader<?>> provider, String... deps) {
        loaderProviders.put(name, provider);
        dependencies.put(name, deps);
    }

    @Override
    public Map<String, Object> load() {
        // Build adjacency list for the graph
        Map<String, List<String>> adj = new HashMap<>();
        for (String name : loaderProviders.keySet()) {
            adj.put(name, new ArrayList<>());
        }
        for (Map.Entry<String, String[]> entry : dependencies.entrySet()) {
            String dependent = entry.getKey();
            for (String dependency : entry.getValue()) {
                adj.get(dependency).add(dependent);
            }
        }

        // Calculate in-degrees for each loader
        Map<String, Integer> inDegrees = new HashMap<>();
        for (String name : loaderProviders.keySet()) {
            inDegrees.put(name, 0);
        }
        for (String[] deps : dependencies.values()) {
            for (String dep : deps) {
                inDegrees.compute(dep, (k, v) -> v + 1);
            }
        }

        // Use Kahn's algorithm for topological sort
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegrees.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<String> sortedOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            String name = queue.poll();
            sortedOrder.add(name);

            for (String neighbor : adj.getOrDefault(name, Collections.emptyList())) {
                inDegrees.put(neighbor, inDegrees.get(neighbor) - 1);
                if (inDegrees.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Check for cycles
        if (sortedOrder.size() != loaderProviders.size()) {
            throw new IllegalStateException("A cycle was detected in the dependency graph.");
        }

        // Load resources in the sorted order
        Map<String, Object> loadedResources = new HashMap<>();
        for (String name : sortedOrder) {
            Function<Map<String, Object>, Loader<?>> provider = loaderProviders.get(name);
            Loader<?> loader = provider.apply(Collections.unmodifiableMap(loadedResources));
            Object resource = loader.load();
            loadedResources.put(name, resource);
        }

        return loadedResources;
    }

}
