package com.test.repository;

import com.test.entity.Person;
import com.test.util.RandomPersonGenerator;
import com.test.util.RandomPersonGeneratorImpl;
import com.test.util.XMLConverter;
import com.test.util.XMLConverterImpl;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.test.data.Constants.*;

public class FileManagerImpl implements FileManager {

    private final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


    public List<Path> getListFilePathsInFolder(String folderName) {
        List<Path> pathList = new ArrayList<>();
        File directory = new File(DATA_FOLDER);
        Path pathAppData = Paths.get(directory.getAbsolutePath());
        Path folderPath = Paths.get(pathAppData + File.separator + folderName);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath)) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    pathList.add(folderPath.resolve(path.getFileName()));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error - " + e);
        }
        return pathList;
    }

    public boolean existFile(Path path) {
        return Files.exists(path);
    }

    public String loadFile(Path path) {
        byte[] loadBytes = new byte[0];
        try {
            if (Files.exists(path)) loadBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            LOGGER.error("Error load file: " + e.getMessage());
        }
        return new String(loadBytes, StandardCharsets.UTF_8);
    }

    public Path getPathToFile(String db, long id) {
        File directory = new File(DATA_FOLDER);
        Path folderPath = Paths.get(directory.getAbsolutePath());
        String fileName = new StringBuilder(File.separator).append(db).append(File.separator).append(id).append(XML).toString();//Don't touch this
        return Paths.get(folderPath + fileName);
    }

    public void saveFile(String data, Path path) {
        byte[] saveBytes = (data).getBytes();
        try {
            if (!Files.exists(path)) Files.createFile(path);
            Files.write(path, saveBytes, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("Error writing to file: " + e.getMessage());
        }
    }

    public boolean removeFile(Path path) {
        try {
            if (Files.exists(path)) Files.delete(path);
            return true;
        } catch (IOException e) {
            LOGGER.error("Error delete file: " + e.getMessage());
        }
        return false;
    }

    public void checkBeforeStart() {
        File directory = new File(DATA_FOLDER);
        Path path = Paths.get(directory.getAbsolutePath());
        LOGGER.info("Check folder for db and setting");
        if (!directory.exists()) {
            try {
                Files.createDirectories(path);
                LOGGER.info("Create folder - " + DATA_FOLDER);
            } catch (IOException e) {
                LOGGER.error("Error - " + e);
            }
        }
        checkNestedDbFolderAndIfNeedGeneratePersons(EXTERNAL_DB_FOLDER, path);
        checkNestedDbFolderAndIfNeedGeneratePersons(INTERNAL_DB_FOLDER, path);
        LOGGER.info("Max PersonId=" + Person.nextPersonId);
    }


    private void checkNestedDbFolderAndIfNeedGeneratePersons(String folderName, Path path) {
        Path nestedFolderPath = Paths.get(path + File.separator + folderName);
        if (!Files.exists(nestedFolderPath)) {
            try {
                Files.createDirectories(nestedFolderPath);
                LOGGER.info("Create folder - " + folderName);
            } catch (IOException e) {
                LOGGER.error("Error - " + e);
            }
        }
        if (Files.exists(nestedFolderPath) && Files.isDirectory(nestedFolderPath)) {
            findLastPersonId(nestedFolderPath);
            LOGGER.info("Nested folder for db " + folderName + " exist.");
            try {
                long fileCount = Files.list(nestedFolderPath).count();
                LOGGER.info("Persons data files in the folder: " + fileCount);
                if (fileCount < QUANTITY_TEST_PERSONS ) generateAndSavePersonDataFiles(nestedFolderPath, fileCount);
            } catch (IOException e) {
                LOGGER.error("Error - " + e);
            }
        } else {
            LOGGER.info("Nested folder for db " + folderName + " do not exist.");
        }
    }

    private void generateAndSavePersonDataFiles(Path folderPath, long fileCount) {
        long needFiles = QUANTITY_TEST_PERSONS - fileCount;
        LOGGER.info("Generate and save " + needFiles + " persons XML data files");

        RandomPersonGenerator rpg = new RandomPersonGeneratorImpl();
        XMLConverter xmlConverter = new XMLConverterImpl();
        for (long i = needFiles; i > 0; i--) {
            Person person = rpg.generateRandomPerson();
            String fileName = new StringBuilder(File.separator).append(person.getPersonId()).append(XML).toString();//Don't touch this
            Path filePath = Paths.get(folderPath + fileName);
            String data = xmlConverter.convertPersonToXMLData(person);
//            xmlConverter.convertXMLDataToPerson(data);// todo error
            saveFile(data, filePath);
        }
    }


    private void findLastPersonId(Path nestedFolderPath) {
        try (Stream<Path> paths = Files.walk(nestedFolderPath)) {
            paths.filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(name -> name.substring(0, name.lastIndexOf('.')))
                    .forEach(this::checkMaxId);
        } catch (Exception e) {
            LOGGER.info("Error " + e);
        }
    }

    private void checkMaxId(String name) {
        if (Long.parseLong(name) >= Person.nextPersonId) Person.nextPersonId = Long.parseLong(name);
    }

}// end of class
