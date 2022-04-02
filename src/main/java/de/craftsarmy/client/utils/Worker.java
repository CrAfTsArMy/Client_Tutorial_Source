package de.craftsarmy.client.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Worker {

    private static int count = 0;

    private final Thread thread;
    private final Touch<Task> touch;

    private final ConcurrentLinkedQueue<Class<? extends Task>> tasks = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Class<? extends Task>> repeatingTasks = new ConcurrentLinkedQueue<>();

    public Worker() {
        touch = new Touch<>();
        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                // Only run tasks once
                for (Class<? extends Task> task : tasks)
                    try {
                        touch.touch(task).run();
                        tasks.remove(task);
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                        tasks.remove(task);
                    }

                // Perform repeating tasks
                for (Class<? extends Task> task : repeatingTasks)
                    try {
                        touch.touch(task).run();
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
            }
        });
        count += 1;
        thread.setName("Worker #" + count);
        thread.start();
    }

    public void shutdown() {
        thread.interrupt();
    }

    public void submit(Class<? extends Task> clazz) {
        if (!tasks.contains(clazz))
            tasks.add(clazz);
    }

    public void repeat(Class<? extends Task> clazz) {
        if (!repeatingTasks.contains(clazz))
            repeatingTasks.add(clazz);
    }

    public void pause(Class<? extends Task> clazz) {
        repeatingTasks.remove(clazz);
    }

    public interface Task {
        void run();
    }

}
