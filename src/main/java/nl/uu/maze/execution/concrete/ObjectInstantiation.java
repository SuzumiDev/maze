package nl.uu.maze.execution.concrete;

import java.lang.reflect.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.uu.maze.analysis.JavaAnalyzer;
import nl.uu.maze.execution.ArgMap;
import nl.uu.maze.execution.MethodType;
import sootup.core.jimple.common.ref.JArrayRef;
import sootup.core.jimple.common.ref.JInstanceFieldRef;
import sootup.core.jimple.common.stmt.JAssignStmt;
import sootup.core.jimple.common.stmt.JIfStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.jimple.javabytecode.stmt.JSwitchStmt;
import sootup.java.core.JavaSootMethod;

/**
 * Instantiates objects using Java reflection and randomly generated
 * arguments.
 */
public class ObjectInstantiation {
    private static final Logger logger = LoggerFactory.getLogger(ObjectInstantiation.class);

    private static final Random rand = new Random();

    /**
     * Attempt to create an instance of the given class.
     * 
     * @param clazz The class to instantiate
     * @return An instance of {@link ExecutionResult} containing the instance
     *         created or the exception thrown if the instance could not be created
     */
    public static ExecutionResult createInstance(Class<?> clazz) {
        if (clazz.isInterface()) {
            // Try to find an implementation of the interface
            // If the interface has a default implementation, use that
            Class<?> implClass = JavaAnalyzer.getDefaultImplementation(clazz);
            if (implClass != null) {
                logger.debug("Using default implementation for interface {} to create an instance: {}", clazz.getName(),
                        implClass.getName());
                return createInstance(implClass);
            } else {
                logger.warn("Cannot create instance of an interface without default implementation: {}",
                        clazz.getName());
                return new ExecutionResult(null,
                        new IllegalArgumentException("Cannot instantiate an interface: " + clazz.getName()),
                        true);
            }
        }

        // Try to create an instance using one of the constructors
        Constructor<?>[] ctors = clazz.getDeclaredConstructors();
        // Sort the constructors by the number of parameters to try the easiest first
        Arrays.sort(ctors, (a, b) -> Integer.compare(a.getParameterCount(), b.getParameterCount()));
        for (Constructor<?> ctor : ctors) {
            Object[] args = generateArgs(ctor.getParameters(), MethodType.CTOR, null, "");
            ExecutionResult result = createInstance(ctor, args);
            if (!result.isException()) {
                return result;
            }
        }

        return new ExecutionResult(null, new UnsupportedOperationException("No suitable constructor found"), true);
    }

    /**
     * Attempt to create an instance of a class using the given
     * {@link ArgMap} to determine the arguments to pass to the constructor.
     * 
     * @param ctor   The constructor to use to create the instance
     * @param argMap {@link ArgMap} containing the arguments to pass to the
     *               constructor
     * @return An instance of {@link ExecutionResult} containing the instance
     *         created or the exception thrown if the instance could not be created
     */
    public static ExecutionResult createInstance(Constructor<?> ctor, ArgMap argMap) {
        return createInstance(ctor, generateArgs(ctor.getParameters(), MethodType.CTOR, argMap, ""));
    }

    /**
     * Attempt to create an instance of the given class using the given arguments.
     * 
     * @param ctor The constructor to use to create the instance
     * @param args The arguments to pass to the constructor
     * @return An instance of {@link ExecutionResult} containing the instance
     *         created or the exception thrown if the instance could not be created
     */
    public static ExecutionResult createInstance(Constructor<?> ctor, Object[] args) {
        try {
            logger.debug("Creating instance of class {} with args: {}", ctor.getDeclaringClass().getSimpleName(),
                    args);
            ctor.setAccessible(true);
            Object instance = ctor.newInstance(args);
            return new ExecutionResult(instance, null, false);
        } catch (Exception e) {
            logger.warn("Constructor of {} threw an exception: {}", ctor.getDeclaringClass().getSimpleName(),
                    e.getMessage());
            return new ExecutionResult(null, e, true);
        }
    }

    /**
     * Generate random values for the given parameters, except for the ones present
     * in the {@link ArgMap}, in which case the known value is used.
     * This will attempt to recursively create instances of objects if the parameter
     * type is not a primitive type, up to a certain depth.
     * 
     * @param params     The parameters of the method
     * @param methodType The type of the method
     * @param argMap     {@link ArgMap} containing the arguments to pass to the
     *                   method invocation
     * @return An array of arguments corresponding to the given parameters
     */
    public static Object[] generateArgs(Parameter[] params, MethodType methodType, ArgMap argMap, String methodName) {
        Object[] arguments = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            // If the parameter is known, use the known value
            String name = ArgMap.getSymbolicName(methodType, methodType == MethodType.CTOR ? "" : methodName, i);
            if (argMap != null && argMap.containsKey(name)) {
                arguments[i] = argMap.toJava(name, params[i].getType());
                continue;
            }

            // Get a default value for the parameter type
            arguments[i] = getDefault(params[i].getType());

            // Add new argument to argMap
            if (argMap != null) {
                argMap.set(name, arguments[i]);
            }
        }

        return arguments;
    }

    public static Object[] generateRandomArgs(Parameter[] params, MethodType methodType, ArgMap argMap, String methodName, boolean canBeDefault) {
        Object[] arguments = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            // If the parameter is known, use the known value
            String name = ArgMap.getSymbolicName(methodType, methodType == MethodType.CTOR ? "" : methodName, i);
            if (argMap != null && argMap.containsKey(name)) {
                arguments[i] = argMap.toJava(name, params[i].getType());
                continue;
            }

            // Get a default value for the parameter type
            Object o = generateRandom(params[i].getType());

            if (!canBeDefault) {
                Object d = getDefault(params[i].getType());
                if (d != null && d.equals(o)) {
                    if (params[i].getType().isPrimitive())
                        return generateRandomArgs(params, methodType, argMap, methodName, false);
                }
            }

            arguments[i] = o;

            // Add new argument to argMap
            if (argMap != null) {
                argMap.set(name, arguments[i]);
            }
        }

        return arguments;
    }

    public static List<String> getSideEffects(Class<?> clazz, Constructor<?> ctor) {
        List<String> initializedFields = new ArrayList<>();

        if (clazz.getDeclaredFields().length == 0)
            return initializedFields;

        Object[] args = generateRandomArgs(ctor.getParameters(), MethodType.CTOR, null, "", false);
        try {
            Object instance = ctor.newInstance(args);
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(instance) != null && !f.get(instance).equals(getDefault(f.getType()))) {
                    initializedFields.add(f.getName());
                }
            }
        } catch (Exception e) {
            logger.info("Constructor {} for class {} threw an exception when analyzing its side effects", ctor, clazz);
            logger.info(e.getMessage());
        }

        return initializedFields;
    }

    public static Set<String> getSideEffects(JavaSootMethod method) {
        Set<String> variables = new HashSet<>();

        for (Stmt stmt : method.getBody().getStmts()) {
            if (stmt instanceof JAssignStmt jAssignStmt) {
                if (jAssignStmt.getLeftOp() instanceof JInstanceFieldRef jInstanceFieldRef) {
                    variables.add(jInstanceFieldRef.getFieldSignature().getName());
                }
            }
        }
        return variables;
    }

    public static Set<String> getAccessedVariables(JavaSootMethod method, Class<?> clazz) {
        Set<String> variables = new HashSet<>();

        for (Stmt stmt : method.getBody().getStmts()) {
            if (stmt instanceof JAssignStmt jAssignStmt) {
                if (!jAssignStmt.containsFieldRef()) {
                    if (jAssignStmt.containsArrayRef()) {
                        logger.debug("Todo: an array ref has appeared {}", jAssignStmt.getArrayRef().getBase().getName());
                        continue;
                    }
                    continue;
                }
                variables.add(jAssignStmt.getFieldRef().getFieldSignature().getName());
            }
        }

        logger.debug("accessed variables from method {} are {}", method.getName(), variables);

        return variables;
    }
    


    private static Object getDefault(Class<?> type) {
        // Create empty array
        if (type.isArray()) {
            // Note: the newInstance method automatically deals with multidimensional
            // arrays
            return Array.newInstance(type.getComponentType(), 0);
        }

        return switch (type.getName()) {
            case "int" -> 0;
            case "double" -> 0.0;
            case "float" -> 0.0f;
            case "long" -> 0L;
            case "short" -> (short) 0;
            case "byte" -> (byte) 0;
            case "char" -> (char) 0;
            case "boolean" -> false;
            case "java.lang.String" -> "";
            default ->
                // Objects are set to null
                null;
        };
    }

    /**
     * Generate a random value for the given type.
     * 
     * @param type The java class of the value to generate
     * @return A random value or default of the given type
     */
    @SuppressWarnings("unused")
    private static Object generateRandom(Class<?> type) {
        return switch (type.getName()) {
            case "int" -> rand.nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            case "double" -> {
                long randomBits64 = rand.nextLong();
                yield Double.longBitsToDouble(randomBits64);
            }
            case "float" -> {
                int randomBits32 = rand.nextInt();
                yield Float.intBitsToFloat(randomBits32);
            }
            case "long" -> rand.nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
            case "short" -> (short) rand.nextInt(Short.MIN_VALUE, Short.MAX_VALUE);
            case "byte" -> (byte) rand.nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE);
            case "char" -> (char) rand.nextInt(Character.MIN_VALUE, Character.MAX_VALUE);
            case "boolean" -> rand.nextBoolean();
            // For other types, return default value
            default -> getDefault(type);
        };
    }
}
