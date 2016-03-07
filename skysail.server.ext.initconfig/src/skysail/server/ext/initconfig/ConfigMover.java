package skysail.server.ext.initconfig;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.skysail.server.Constants;
import io.skysail.server.utils.BundleUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * The ConfigMover, if included in a skysail installation, will try to copy the
 * configuration files from the current product bundle (defined by the system
 * property "product.bundle") into the installations config folder (target
 * folder).
 * 
 * The product bundle has to contain a directory ./config/default, which contains 
 * all the files to be copied.
 *
 * The target folder will be created if it doesn't exist. Only files which don't exist
 * in the target folder will be copied.
 *
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.optional)
@Slf4j
public class ConfigMover {

    /** comma-separated list of subdirectories of config dir to be copied. */
    private static final String CONFIG_PATH_SOURCES = "config/default";
    
    /** set to true, if a logback config file was discovered during copying. */ 
    private boolean logbackConfigurationExists = false;

    @Activate
    public void activate(ComponentContext context) {
        copyConfigurationFromProductJar(context);
        if (!logbackConfigurationExists) {
            log.info("setting logging level to INFO, no initial logback confi file was provided yet.");
            Logger root = (Logger) org.slf4j.LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            root.setLevel(Level.INFO);
        }
    }

    private void copyConfigurationFromProductJar(ComponentContext context) {
        String productBundleName = System.getProperty(Constants.PRODUCT_BUNDLE_IDENTIFIER);
        log.debug("determined product bundle to be '{}'", productBundleName);
        Optional<Bundle> productBundle = Arrays.stream(context.getBundleContext().getBundles())
                .filter(b -> b.getSymbolicName()
                .equals(productBundleName))
                .findFirst();
        copyConfigurationFilesOrWarn(productBundle);
    }

    private void copyConfigurationFilesOrWarn(Optional<Bundle> productBundle) {
        if (productBundle.isPresent()) {
            copyConfigurationFiles(productBundle.get());
        } else {
            log.warn("could not determine product bundle, no default configuration was copied");
        }
    }

    private void copyConfigurationFiles(Bundle bundle) {
        List<String> fromPaths = getFrom(bundle);
        log.debug("copyConfigurationFiles, found paths: {}", fromPaths);
        for (String fromPath : fromPaths) {
            log.debug("checking path {}", fromPath);
            Enumeration<String> entryPaths = bundle.getEntryPaths(fromPath);
            if (entryPaths == null) {
                log.debug("no configuration found in bundle {}", bundle.getSymbolicName());
                return;
            }
            Path copyToPath = Paths.get("./" + fromPath);
            try {
                log.debug("will try to create directory '{}' if it not exists", copyToPath.toAbsolutePath().toString());
                Files.createDirectories(copyToPath);
                handleConfigFiles(bundle, entryPaths);
            } catch (FileAlreadyExistsException faee) { // NOSONAR
                log.debug("file '{}' already exists, no config files will be copied", copyToPath.toAbsolutePath().toString());
            } catch (IOException e1) {
                log.error(e1.getMessage(), e1);
            }
        }

    }

    private List<String> getFrom(Bundle bundle) {
        log.debug("checking directories '{}' in bundle {} for configurations", CONFIG_PATH_SOURCES, bundle.getSymbolicName());
        return Arrays.stream(CONFIG_PATH_SOURCES.split(",")).map(String::trim).collect(Collectors.toList());
    }

    private void handleConfigFiles(Bundle bundle, Enumeration<String> entryPaths) {
        while (entryPaths.hasMoreElements()) {
            String sourceFileName = entryPaths.nextElement();
            String content = BundleUtils.readResource(bundle, sourceFileName);
            try {
                copyFileIfNotExists(sourceFileName, content);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void copyFileIfNotExists(String sourceFileName, String content) throws IOException {
        Path targetFilePath = Paths.get("./" + sourceFileName);
        if (Files.exists(targetFilePath)) {
            log.debug("not copying '{}', as it already exists in {}", sourceFileName, targetFilePath.toString());
            if (targetFilePath.toString().contains("logback.xml")) {
                logbackConfigurationExists = true;
            }
        } else {
            log.debug("about to copy configuration from product bundle to '{}'", targetFilePath.toString());
            Files.write(targetFilePath, content.getBytes());
        }
    }

}
