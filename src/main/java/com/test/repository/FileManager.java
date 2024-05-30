package com.test.repository;


import java.nio.file.Path;
import java.util.List;

public interface FileManager {

// todo for future check collisions

    public boolean existFile(Path path);

    public String loadFile(Path path);
    public Path getPathToFile(String db, long id);

    public void saveFile(String fileData, Path path);

    public boolean removeFile(Path path);

    public  void checkBeforeStart();

    public List<Path> getListFilePathsInFolder(String folderName);


}
