// SmartPredictor.java - IMPROVED VERSION
package src;

import java.util.*;

public class SmartPredictor {
    private Map<String, Integer> operationFrequency;
    private List<String> operationSequence;
    private Map<String, Map<String, Integer>> transitionMatrix;
    private final int MAX_HISTORY = 50;

    public SmartPredictor() {
        operationFrequency = new HashMap<>();
        operationSequence = new ArrayList<>();
        transitionMatrix = new HashMap<>();
        initializeOperations();
    }

    private void initializeOperations() {
        String[] operations = { "INSERT_START", "INSERT_END", "INSERT_AT",
                "DELETE_VALUE", "DELETE_AT", "SEARCH", "REVERSE", "CLEAR" };

        for (String op : operations) {
            operationFrequency.put(op, 0);
            transitionMatrix.put(op, new HashMap<>());

            // Initialize transitions for each operation
            for (String targetOp : operations) {
                transitionMatrix.get(op).put(targetOp, 0);
            }
        }
    }

    public void recordOperation(String operation, int value) {
        // Update frequency
        operationFrequency.put(operation, operationFrequency.getOrDefault(operation, 0) + 1);

        // Update transition probabilities
        if (!operationSequence.isEmpty()) {
            String previousOp = operationSequence.get(operationSequence.size() - 1);
            Map<String, Integer> transitions = transitionMatrix.get(previousOp);
            transitions.put(operation, transitions.getOrDefault(operation, 0) + 1);
        }

        // Add to sequence (limit history)
        operationSequence.add(operation);
        if (operationSequence.size() > MAX_HISTORY) {
            operationSequence.remove(0);
        }
    }

    public List<Prediction> getPredictions() {
        List<Prediction> predictions = new ArrayList<>();

        if (operationSequence.isEmpty()) {
            // Default predictions for new users
            predictions.add(new Prediction("INSERT_START", 0.4, "Most users start by inserting at beginning"));
            predictions.add(new Prediction("INSERT_END", 0.3, "Common second operation"));
            predictions.add(new Prediction("SEARCH", 0.2, "Try searching for values"));
            predictions.add(new Prediction("CLEAR", 0.1, "Clear the list when done"));
            return predictions;
        }

        String lastOperation = operationSequence.get(operationSequence.size() - 1);
        Map<String, Integer> transitions = transitionMatrix.get(lastOperation);

        // Calculate probabilities based on transitions
        int totalTransitions = transitions.values().stream().mapToInt(Integer::intValue).sum();

        // IMPROVEMENT: Mix transition-based and frequency-based predictions
        if (totalTransitions > 0) {
            // Use transition-based predictions (more accurate)
            for (Map.Entry<String, Integer> entry : transitions.entrySet()) {
                if (entry.getValue() > 0) {
                    double probability = (double) entry.getValue() / totalTransitions;
                    String reasoning = String.format("After %s, you usually %s next (%d times)",
                            getReadableName(lastOperation), getReadableName(entry.getKey()), entry.getValue());
                    predictions.add(new Prediction(entry.getKey(), probability, reasoning));
                }
            }
        }

        // IMPROVEMENT: Always include frequency-based predictions as fallback
        predictions.addAll(getEnhancedFrequencyBasedPredictions());

        // IMPROVEMENT: Remove duplicates and sort by confidence
        predictions = removeDuplicatePredictions(predictions);
        predictions.sort((a, b) -> Double.compare(b.confidence, a.confidence));

        return predictions.subList(0, Math.min(4, predictions.size()));
    }

    private List<Prediction> getEnhancedFrequencyBasedPredictions() {
        List<Prediction> predictions = new ArrayList<>();
        int totalOps = operationFrequency.values().stream().mapToInt(Integer::intValue).sum();

        if (totalOps == 0)
            return predictions;

        // IMPROVEMENT: Boost confidence for operations that are frequently used
        for (Map.Entry<String, Integer> entry : operationFrequency.entrySet()) {
            if (entry.getValue() > 0) {
                double baseProbability = (double) entry.getValue() / totalOps;
                // Boost probability for frequently used operations
                double boostedProbability = baseProbability * 1.5;
                String reasoning = String.format("You use this frequently (%d times)", entry.getValue());
                predictions.add(new Prediction(entry.getKey(), Math.min(boostedProbability, 0.8), reasoning));
            }
        }

        return predictions;
    }

    private List<Prediction> removeDuplicatePredictions(List<Prediction> predictions) {
        Map<String, Prediction> uniquePredictions = new LinkedHashMap<>();

        for (Prediction prediction : predictions) {
            if (!uniquePredictions.containsKey(prediction.operation) ||
                    uniquePredictions.get(prediction.operation).confidence < prediction.confidence) {
                uniquePredictions.put(prediction.operation, prediction);
            }
        }

        return new ArrayList<>(uniquePredictions.values());
    }

    private String getReadableName(String operation) {
        switch (operation) {
            case "INSERT_START":
                return "insert at start";
            case "INSERT_END":
                return "insert at end";
            case "INSERT_AT":
                return "insert at position";
            case "DELETE_VALUE":
                return "delete by value";
            case "DELETE_AT":
                return "delete at position";
            case "SEARCH":
                return "search";
            case "REVERSE":
                return "reverse";
            case "CLEAR":
                return "clear";
            default:
                return operation.toLowerCase();
        }
    }

    // Prediction data class (unchanged)
    public static class Prediction {
        public String operation;
        public double confidence;
        public String reasoning;

        public Prediction(String operation, double confidence, String reasoning) {
            this.operation = operation;
            this.confidence = confidence;
            this.reasoning = reasoning;
        }

        @Override
        public String toString() {
            return String.format("%s (%.0f%%)", getReadableName(operation), confidence * 100);
        }

        public String getDetailedString() {
            return String.format("%s - %.0f%% confident\n%s",
                    getReadableName(operation), confidence * 100, reasoning);
        }

        private String getReadableName(String operation) {
            switch (operation) {
                case "INSERT_START":
                    return "Insert Start";
                case "INSERT_END":
                    return "Insert End";
                case "INSERT_AT":
                    return "Insert At Position";
                case "DELETE_VALUE":
                    return "Delete Value";
                case "DELETE_AT":
                    return "Delete At Position";
                case "SEARCH":
                    return "Search";
                case "REVERSE":
                    return "Reverse List";
                case "CLEAR":
                    return "Clear List";
                default:
                    return operation;
            }
        }
    }
}