package me.erano.com.api.hologram;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

/**
 * {@code World#spawn(Location, Class, Consumer)} on every version: the callback runs before the entity is
 * added to the world, so no client sees its default state. The parameter was {@code org.bukkit.util.Consumer}
 * until 1.20.1 and is {@code java.util.function.Consumer} since 1.20.2.
 */
final class EntitySpawner {

    private static final Method SPAWN = findSpawn();

    private EntitySpawner() {
    }

    static <T extends Entity> T spawn(Location at, Class<T> type, Consumer<? super T> init) {
        World world = at.getWorld();
        if (SPAWN == null) {
            T entity = world.spawn(at, type);
            init.accept(entity);
            return entity;
        }
        Class<?> callbackType = SPAWN.getParameterTypes()[2];
        Object callback = callbackType == Consumer.class ? init : Proxy.newProxyInstance(callbackType.getClassLoader(), new Class<?>[] {callbackType},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "accept":
                            init.accept(type.cast(args[0]));
                            return null;
                        case "hashCode":
                            return System.identityHashCode(proxy);
                        case "equals":
                            return proxy == args[0];
                        default:
                            return "EntitySpawner callback";
                    }
                });
        try {
            return type.cast(SPAWN.invoke(world, at, type, callback));
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new IllegalStateException(cause);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Method findSpawn() {
        for (Method method : World.class.getMethods()) {
            Class<?>[] parameters = method.getParameterTypes();
            if (method.getName().equals("spawn") && parameters.length == 3 && parameters[0] == Location.class
                    && parameters[1] == Class.class && parameters[2].isInterface()) {
                return method;
            }
        }
        return null;
    }
}
