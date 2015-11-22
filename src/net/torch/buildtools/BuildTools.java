package net.torch.buildtools;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The main class of the application
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
            logger.info("Verifying repository availability...");
            logger.info("Connecting to GitHub...");
            GitHub gitHub;
            try {
                gitHub = GitHub.connect();
            } catch (IOException e) {
                logger.severe("Could not connect to GitHub!");
                return;
            }
            GHOrganization torchPowered;
            try {
                torchPowered = gitHub.getOrganization("TorchPowered");
            } catch (IOException e) {
                logger.severe("Could not connect to GitHub organization!");
                return;
            }
            GHRepository repository;
            try {
                repository = torchPowered.getRepository("Torch");
            } catch (IOException e) {
                logger.severe("Could not connect to the Torch repository!");
                return;
            }
            logger.info("Repository verified! Cloning!");
            File workingDir = new File(System.getProperty("user.dir"));
            File endDir = new File(workingDir, "builds");
            endDir.mkdir();
            String repositoryLink = "git@github.com:TorchPowered/Torch.git";
            try {
                Git.cloneRepository().setURI(repositoryLink).setDirectory(endDir).call();
            } catch (GitAPIException e) {
                logger.severe("Could not clone Torch repository!");
                return;
            }
            logger.info("Torch repository cloned! Building repository!");
            logger.info("Checking for operating system to insure build compatiblity...");
            String operatingSystem = System.getProperty("os.name").toLowerCase();
            ArrayList<String> osSpecificCommand = new ArrayList<String>();
            boolean isUnix = false;
            if(operatingSystem.contains("win")){
                osSpecificCommand.add("gradlew assemble");
            }else{
                isUnix = true;
                osSpecificCommand.add("./gradlew assemble");
            }
            ArrayList<String> permissionGradle = new ArrayList<String>();
            if(isUnix){
                permissionGradle.add("chmod +x gradlew");
            }
            ArrayList<String> intoDirectory = new ArrayList<String>();
            intoDirectory.add("cd " + '"' + endDir.getAbsolutePath() + '"');
            ProcessBuilder build = new ProcessBuilder(osSpecificCommand);
            ProcessBuilder cddir = new ProcessBuilder(intoDirectory);
            ProcessBuilder addPermissionUnix = new ProcessBuilder(permissionGradle);
            if(!isUnix){
                try {
                    cddir.start();
                    build.start();
                } catch (IOException e) {
                    logger.info("Failed to build project!");
                    return;
                }
            }else{
                try{
                    cddir.start();
                    addPermissionUnix.start();
                    build.start();
                }catch (IOException e){
                    logger.info("Failed to build project!");
                    return;
                }
            }
            logger.info("BuildTools finished ");
        }
        logger.severe("Sorry, something went wrong.");
    }
}
