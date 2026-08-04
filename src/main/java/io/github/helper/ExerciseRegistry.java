package io.github.helper;

import java.util.HashMap;
import java.util.Map;

public class ExerciseRegistry {

    public static class ExerciseConfig {
        private final Runnable runnable;
        private final String methodName;
        private final int maxPrints;

        public ExerciseConfig(Runnable runnable, String methodName, int maxPrints) {
            this.runnable = runnable;
            this.methodName = methodName;
            this.maxPrints = maxPrints;
        }

        public Runnable getRunnable() { return runnable; }
        public String getMethodName() { return methodName; }
        public int getMaxPrints() { return maxPrints; }
    }

    private final Map<String, ExerciseConfig> exercises = new HashMap<>();

    public ExerciseRegistry register(String key, Runnable runnable, String methodName, int maxPrints) {
        exercises.put(key, new ExerciseConfig(runnable, methodName, maxPrints));
        return this;
    }

    public ExerciseConfig select(String key) {
        ExerciseConfig config = exercises.get(key);
        if (config == null) {
            throw new IllegalArgumentException("Esercizio '" + key + "' non trovato o non valido!");
        }
        return config;
    }
}