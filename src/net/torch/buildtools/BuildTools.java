package net.torch.buildtools;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The main class of the application
 * @author PizzaCrust
 */
public class BuildTools {
    public static void main(String[] args){
        Logger logger = Logger.getLogger("BuildTools");
        if(args.length > 1){
            logger.severe("Too many arguments!");
            return;
        }
        if(args.length < 1){
            logger.severe("Not enough arguments!");
            return;
        }
        if(args[0].equalsIgnoreCase("-latest")){
            logger.info("Running Build Tools!");
            File workingDir = new File(System.getProperty("user.dir"));
            File endDir = new File(workingDir, "builds");
            if(endDir.exists()){
                logger.severe("Builds directory has been already made! Assuming a build has been gone before, please remove builds directory!");
                return;
            }
            endDir.mkdir();
            String repositoryLink = "https://github.com/TorchPowered/Torch.git";
            try {
                Git.cloneRepository().setURI(repositoryLink).setDirectory(endDir).call();
            } catch (GitAPIException e) {
                logger.severe("Could not clone Torch repository!");
                return;
            }
            logger.info("Torch repository cloned! Building repository!");
            ProjectConnection projectConnection = GradleConnector.newConnector().forProjectDirectory(endDir).connect();
            projectConnection.newBuild().forTasks("assemble").run();
            logger.info("BuildTools finished with JAR file inside of builds/build/lib");
            System.exit(0);
            return;
        }
        if(args[0].equalsIgnoreCase("-stable")){
            logger.info("Running Build Tools!");
            File workingDir = new File(System.getProperty("user.dir"));
            File endDir = new File(workingDir, "builds");
            if(endDir.exists()){
                logger.severe("Builds directory has been already made! Assuming a build has been gone before, please remove builds directory!");
                return;
            }
            endDir.mkdir();
            String repositoryLink = "https://github.com/TorchPowered/Torch.git";
            try {
                Git.cloneRepository().setURI(repositoryLink).setDirectory(endDir).call().checkout().setName("04ca95a").call();
            } catch (GitAPIException e) {
                logger.severe("Could not clone Torch repository!");
                return;
            }
            logger.info("Torch repository cloned! Building repository!");
            ProjectConnection projectConnection = GradleConnector.newConnector().forProjectDirectory(endDir).connect();
            projectConnection.newBuild().forTasks("assemble").run();
            logger.info("BuildTools finished with JAR file inside of builds/build/lib");
            System.exit(0);
            return;
        }
        logger.severe("Sorry, something went wrong.");
    }
}
