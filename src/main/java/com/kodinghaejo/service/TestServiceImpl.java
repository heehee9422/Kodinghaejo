package com.kodinghaejo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestLngEntity;
import com.kodinghaejo.entity.repository.TestLngRepository;
import com.kodinghaejo.entity.repository.TestRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TestServiceImpl implements TestService {
	
<<<<<<< Updated upstream
=======
	private final TestRepository testRepository;
	private final TestLngRepository testLngRepository;
	
	//코딩테스트 문제 가져오기
	@Override
	public TestDTO loadTest(Long idx) throws Exception {
		return testRepository.findById(idx).map((test) -> new TestDTO(test)).get();
	}
	
	//문제에서 선택 가능한 언어 확인
	@Override
	public String lngAvlChk(Long idx, String lng) throws Exception {
		TestEntity testEntity = testRepository.findById(idx).get();
		
		return testLngRepository.findFirstByTestIdxAndLng(testEntity, lng) != null ? "Y" : "N";
	}
	
	//코딩테스트 언어별 문제 가져오기
	@Override
	public TestLngEntity loadTestLng(Long testIdx, String language) throws Exception {
		TestEntity testEntity = testRepository.findById(testIdx).get();
		
		return testLngRepository.findFirstByTestIdxAndLng(testEntity, language);
	}
	
>>>>>>> Stashed changes
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
    
    public void createMainJavaFile() {
        String filePath = "submissions/Main.java";  // 원하는 파일 경로 설정

        String mainTemplate = "import java.util.*;\n" +
        					  "\n" +
        		              "public class Main {\n" +
                              "    public static void main(String[] args) {\n" +
                              "        Solution solution = new Solution();\n" +
                              "        Verify verify = new Verify();\n" +
                              "        Random rnd = new Random();\n" +
                              "        int num1 = rnd.nextInt(10) + 1;\n" +
                              "        int num2 = rnd.nextInt(10) + 1;\n" +
                              "        int result_s = solution.solution(num1, num2);\n" +
                              "        int result_v = verify.verify(num1, num2);\n" +
                              "        System.out.println(\"Test: \" + (result_s == result_v ? \"Pass\" : \"Fail\"));\n" +
                              "    }\n" +
                              "}\n";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(mainTemplate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void createMainJsFile() {
    	String filePath = "submissions/Main.js";
    	
    	String mainTemplate = "function getRandomInt(min, max) {\n" +
    						  "    return Math.floor(Math.random() * (max - min + 1)) + min;\n" +
    						  "}\n" +
    						  "let num1 = getRandomInt(1, 10);\n" +
    						  "let num2 = getRandomInt(1, 10);\n" +
    						  "\n" +
    						  "const solution = require('./Solution');\n" +
    						  "const verify = require('./Verify');\n" +
    						  "\n" +
    						  "const result_s = solution(num1,num2);\n" +
    						  "const result_v = verify(num1,num2);\n" +
    						  "console.log(\"Test: \" + (result_s == result_v ? \"Pass\" : \"Fail\"));";
    	
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(mainTemplate);
        } catch (IOException e) {
            e.printStackTrace();
        }					  
    }
}