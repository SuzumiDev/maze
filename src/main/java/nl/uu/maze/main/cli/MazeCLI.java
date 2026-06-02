package nl.uu.maze.main.cli;

import java.util.List;
import java.util.concurrent.Callable;

import nl.uu.maze.execution.concrete.objectinstantiation.ObjectInstantiator.SettersSelectionStrategy;
import nl.uu.maze.execution.concrete.objectinstantiation.ObjectInstantiator.ConstructorSelectionStrategy;
import nl.uu.maze.fuzzing.FuzzingOptions;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;

import nl.uu.maze.execution.DSEController;
import nl.uu.maze.main.cli.converters.*;
import nl.uu.maze.fuzzing.FuzzingFactory.FuzzingStrategy;
import nl.uu.maze.search.heuristic.SearchHeuristicFactory.ValidSearchHeuristic;
import nl.uu.maze.search.strategy.SearchStrategy;
import nl.uu.maze.search.strategy.SearchStrategyFactory;
import nl.uu.maze.search.strategy.SearchStrategyFactory.ValidSearchStrategy;
import nl.uu.maze.util.Z3ContextProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Main class for the MAZE application that provides a command-line interface
 * (CLI) for generating tests using dynamic symbolic execution (DSE).
 */
@Command(name = "maze", mixinStandardHelpOptions = true, version = "maze 1.0", descriptionHeading = "%nDescription:%n", description = "Generate tests for the specified Java class using dynamic symbolic execution (DSE).", optionListHeading = "%nOptions:%n", sortOptions = false)
public class MazeCLI implements Callable<Integer> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MazeCLI.class);

    @Option(names = { "-c",
            "--classpath" }, description = "Path to compiled classes", required = true, paramLabel = "<path>")
    private String classPath;

    @Option(names = { "-n",
            "--classname" }, description = "Fully qualified name of the class to generate tests for", required = true, paramLabel = "<class>")
    private String className;

    @Option(names = { "-o",
            "--output-path" }, description = "Output path to write generated test files to", required = true, paramLabel = "<path>")
    private String outPath;

    @Option(names = { "-m",
            "--method-name" }, description = "Name of the method to generate tests for (default: ${DEFAULT-VALUE})", defaultValue = "all", paramLabel = "<name>")
    private String methodName;

    @Option(names = { "-p",
            "--package-name" }, description = "Package name to use for generated test files (default: ${DEFAULT-VALUE})", defaultValue = "no package", paramLabel = "<name>", converter = PackageNameConverter.class)
    private String packageName;

    @Option(names = { "-l",
            "--log-level" }, description = "Log level (default: ${DEFAULT-VALUE}, options: OFF, INFO, WARN, ERROR, TRACE, DEBUG)", defaultValue = "INFO", paramLabel = "<level>", converter = LogLevelConverter.class)
    private Level logLevel;

    @Option(names = { "-s",
            "--strategy" }, description = "One or multiple of the available search strategies (default: ${DEFAULT-VALUE}, options: ${COMPLETION-CANDIDATES})", defaultValue = "DFS", split = ",", arity = "1..*", paramLabel = "<name>")
    private List<ValidSearchStrategy> searchStrategies;

    @Option(names = { "-u",
            "--heuristic" }, description = "One or multiple of the available search heuristics to use for probabilistic search (default: ${DEFAULT-VALUE}, options: ${COMPLETION-CANDIDATES})", defaultValue = "UH", split = ",", arity = "1..*", paramLabel = "<name>")
    private List<ValidSearchHeuristic> searchHeuristics;

    @Option(names = { "-w",
            "--weight" }, description = "Weights to use for the provided search heuristics (default: ${DEFAULT-VALUE})", defaultValue = "1.0", split = ",", arity = "1..*", converter = SearchHeuristicWeightConverter.class, paramLabel = "<double>")
    private List<Double> heuristicWeights;

    @Option(names = { "-d",
            "--max-depth" }, description = "Maximum depth of the search (default: ${DEFAULT-VALUE})", defaultValue = "200", paramLabel = "<int>")
    private int maxDepth;

    @Option(names = { "-b",
            "--time-budget" }, description = "Time budget for the search in seconds (default: ${DEFAULT-VALUE})", defaultValue = "no budget", paramLabel = "<long>", converter = TimeBudgetConverter.class)
    private long timeBudget;

    @Option(names = { "-t",
            "--test-timeout" }, description = "Timeout to apply to generated test cases in seconds (default: ${DEFAULT-VALUE})", defaultValue = "no timeout", paramLabel = "<long>", converter = TestTimeoutConverter.class)
    private long testTimeout;

    @Option(names = { "-j",
            "--junit-version" }, description = "JUnit version to target for generated test cases (default: ${DEFAULT-VALUE}, options: ${COMPLETION-CANDIDATES})", defaultValue = "JUnit5", paramLabel = "<version>")
    private JUnitVersion junitVersion;

    @Option(names = { "-C",
            "--concrete-driven" }, description = "Use concrete-driven DSE instead of symbolic-driven DSE (default: ${DEFAULT-VALUE})", defaultValue = "false", paramLabel = "<true|false>")
    private boolean concreteDriven;

    @Option(names = { "-f",
            "--fuzzer"}, description = "One of the available fuzzing methods for concrete-driven execution (must use -C for this to work) (default: ${DEFAULT-VALUE})", defaultValue = "NONE", paramLabel = "<name>")
    private FuzzingStrategy fuzzingStrategy;
    @Option(names = { "-Cs",
            "--constructor-selection" }, description = "Constructor selection strategy for concrete-driven DSE (default: ${DEFAULT-VALUE}, options: Smallest, Biggest, Random, Usage)", defaultValue = "Smallest", paramLabel = "<name>")
    private ConstructorSelectionStrategy constructorSelectionStrategy;

    @Option(names = { "-Se",
            "--setters-selection" }, description = "Setters selection strategy for concrete-driven DSE (default: ${DEFAULT-VALUE}, options: All, None, Usage)", defaultValue = "None", paramLabel = "<name>")
    private SettersSelectionStrategy settersSelectionStrategy;

    @Option(names = { "-Gn", "--genetic-generations"}, description = "The number of genetic generations performed before path condition generation. Only used if fuzzer is GENETIC.", defaultValue = "3", paramLabel = "<int>")
    private int geneticGenerations;

    @Option(names = { "Gs", "--genetic-suites"}, description = "The amount of suites randomly generated at the start of the genetic algorithm. Only used if fuzzer is GENETIC.", defaultValue = "5", paramLabel = "<int>")
    private int geneticSuites;

    @Option(names = { "Gg", "--genetic-genotypes"}, description = "The amount of genotypes randomly generated at the start of the genetic algorithm (per suite). Only used if fuzzer is GENETIC.", defaultValue = "5", paramLabel = "<int>")
    private int geneticGenotypes;

    @Option(names = { "Gms", "--genetic-maximumsuites"}, description = "The maximum amount of suites before the weakest ones are cut off. Only used if fuzzer is GENETIC.", defaultValue = "10", paramLabel = "<int>")
    private int geneticMaximumSuites;

    @Option(names = { "Gmg", "--genetic-maximumgenotypes"}, description = "The maximum amount of genotypes per suite before the weakest ones are cut off. Only used if fuzzer is GENETIC.", defaultValue = "10", paramLabel = "<int>")
    private int geneticMaximumGenotypes;

    @Option(names = { "Ri", "-- random-initialstates"}, description = "The amount of initial states to be used for random fuzzing. Only used if fuzzer is RANDOM.", defaultValue = "10", paramLabel = "<int>")
    private int ranomInitialStates;

    @Override
    public Integer call() {
        try {
            // Set logging level
            Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            rootLogger.setLevel(logLevel);
            timeBudget *= 1000L; // Convert to milliseconds
            testTimeout *= 1000L; // Convert to milliseconds

            List<String> searchStrategies = this.searchStrategies.stream().map(ValidSearchStrategy::name)
                    .toList();
            List<String> searchHeuristics = this.searchHeuristics.stream().map(ValidSearchHeuristic::name)
                    .toList();
            SearchStrategy<?> strategy = SearchStrategyFactory.createStrategy(searchStrategies,
                    searchHeuristics, heuristicWeights, timeBudget);

            FuzzingOptions fuzzingOptions = new FuzzingOptions(geneticGenerations, geneticSuites, geneticGenotypes, geneticMaximumSuites, ranomInitialStates, geneticMaximumGenotypes);

            Long start = System.currentTimeMillis();
            DSEController controller = new DSEController(classPath, concreteDriven, strategy, constructorSelectionStrategy, settersSelectionStrategy, fuzzingStrategy, outPath,
                    methodName, maxDepth, testTimeout, packageName, junitVersion.isJUnit4(), fuzzingOptions);
            controller.run(className, timeBudget);
            Long end = System.currentTimeMillis();
            logger.info("Execution time: {} ms", end - start);
            return 0;
        } catch (Exception e) {
            logger.error("An error occurred: {}: {}", e.getClass().getName(), e.getMessage());
            logger.error("Error stack trace: ", e);
            return 1;
        } finally {
            Z3ContextProvider.close();
        }
    }
}
