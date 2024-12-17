package com.axonivy.github.file;

import static com.axonivy.github.file.GitHubFiles.CODE_OF_CONDUCT;
import static com.axonivy.github.file.GitHubFiles.LICENSE;
import static com.axonivy.github.file.GitHubFiles.SECURITY;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.axonivy.github.file.GitHubFiles.FileMeta;

public class GitHubMissingFiles {

  private static final List<String> WORKING_ORGANIZATIONS = List.of("axonivy");
  private static final List<FileMeta> REQUIRED_FILES = List.of(LICENSE, SECURITY, CODE_OF_CONDUCT);
  private static final List<FileMeta> REMOVE_FILES = List.of();

  public static void main(String[] args) throws IOException {
    String user = "";
    if (args.length > 0) {
      user = args[0];
      System.out.println("running updates triggered by user "+user);
    }
    int status = 0;
    List<String> workingOrganizations = getWorkingOrganizations();
    for (var fileMeta : REQUIRED_FILES) {
      var detector = new GitHubMissingFilesDetector(fileMeta, user);
      var returnedStatus = detector.requireFile(workingOrganizations);
      status = returnedStatus != 0 ? returnedStatus : status;
    }
    for (var fileMeta : REMOVE_FILES) {
      var detector = new GitHubFilesRemover(fileMeta, user);
      var returnedStatus = detector.removeFile(workingOrganizations);
      status = returnedStatus != 0 ? returnedStatus : status;
    }
    System.exit(status);
  }

  private static List<String> getWorkingOrganizations() {
    String inputtedValue = System.getProperty("GITHUB.WORKING.ORGANIZATIONS", String.join(",", WORKING_ORGANIZATIONS));
    return Arrays.asList(inputtedValue.split(","));
  }

}