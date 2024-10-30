package com.kodinghaejo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TestServiceImpl implements TestService {
	
	@Override
    public String testCode(String language, String filePath) {
        String absolutePath = new File(filePath).getAbsolutePath(); // 절대 경로로 변환
        String imageName;
        String containerName;

        if ("java".equals(language)) {
            imageName = "java-code";
            containerName = "java-container";
        } else if ("javascript".equals(language)) {
            imageName = "js-code";
            containerName = "js-container";
        } else {
            throw new IllegalArgumentException("Unsupported language: " + language);
        }

        // 빌드 명령어
        String buildCommand = "docker build -t " + imageName + " -f submissions/Dockerfile." + language + " submissions/";
        
        // 컨테이너 실행 명령어
        String runCommand = "docker run --name " + containerName + " --rm --memory=512m --cpus=0.5 -v " 
                             + absolutePath + ":/app/Solution." + language + " " + imageName;

        try {
            // Docker 빌드 명령어 실행
            ProcessBuilder buildProcessBuilder = new ProcessBuilder(buildCommand.split(" "));
            Process buildProcess = buildProcessBuilder.start();
            buildProcess.waitFor(); // 빌드 완료 대기

            if (buildProcess.exitValue() != 0) {
                return getProcessErrorOutput(buildProcess);
            }

            // Docker 컨테이너 실행 명령어 실행
            ProcessBuilder runProcessBuilder = new ProcessBuilder(runCommand.split(" "));
            Process runProcess = runProcessBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            runProcess.waitFor(); // 프로세스 종료 대기

            if (runProcess.exitValue() == 0) {
                return output.toString();
            } else {
                return getProcessErrorOutput(runProcess);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error executing code!";
        }
    }

    private String getProcessErrorOutput(Process process) throws Exception {
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        String line;
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }
        return "Error:\n" + errorOutput.toString();
    }
}