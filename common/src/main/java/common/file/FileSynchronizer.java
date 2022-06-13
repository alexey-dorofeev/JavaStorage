package common.file;

import common.data.FileInfo;
import common.data.SyncStatus;
import common.utils.CheckSumUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FileSynchronizer {

    /**
     * Ограничение на размер файла 1Кб
     */
    public static final long MAX_FILE_LENGTH = 1024;

    private String workDirectory;

    public FileSynchronizer(String workDirectory) throws IOException {
        this.workDirectory = workDirectory;

        if (Files.notExists(Paths.get(this.workDirectory))) {
            Files.createDirectories(Paths.get(this.workDirectory));
        }
    }

    /**
     * Возвращает список файлов источника
     */
    public Set<FileInfo> getFilesForSync() throws IOException {
        return checkFilesForMaxLength(getFilesInDirectory());
    }

    /**
     * Возвращает список файлов с данными источника в соответствии с переданным списком файлов в папке назначения,
     * которые нуждаются в синхронизации
     */
    public Set<FileInfo> getFilesForSync(Set<FileInfo> serverFiles) throws IOException {

        Set<FileInfo> clientFiles = checkFilesForMaxLength(getFilesInDirectory());

        for (FileInfo clientFile : clientFiles) {
            for (FileInfo serverFile : serverFiles) {
                if (serverFile.syncStatus == SyncStatus.NOT_SYNC && serverFile.file.getName().equals(clientFile.file.getName())) {
                    clientFile.data = Files.readAllBytes(clientFile.file.toPath());
                    return clientFiles;
                }
            }
        }
        return clientFiles;
    }

    /**
     * Синхронизирует файлы в папке назначения в соответствии с переданным списком файлов источника
     */
    public Set<FileInfo> syncFiles(Set<FileInfo> clientFiles) throws IOException {

        for (FileInfo clientFile : clientFiles) {
            clientFile.syncStatus = SyncStatus.NEED_CREATED;
        }
        Set<FileInfo> serverFiles = getFilesInDirectory();
        for (FileInfo serverFile : serverFiles) {
            serverFile.syncStatus = SyncStatus.NEED_DELETED;
        }
        //Проверка на совпадение контрольных сумм
        for (FileInfo clientFile : clientFiles) {

            for (FileInfo serverFile : serverFiles) {
                if (serverFile.file.getName().equals(clientFile.file.getName())) {
                    if (!Objects.equals(serverFile.checkSum, clientFile.checkSum)) {
                        if (clientFile.data != null) {
                            Files.write(serverFile.file.toPath(), clientFile.data);
                            //TODO Сборка содержимого файла из нескольких пакетов
                            clientFile.syncStatus = SyncStatus.SYNC;
                            serverFile.syncStatus = SyncStatus.SYNC;
                        } else {
                            clientFile.syncStatus = SyncStatus.NOT_SYNC;
                            serverFile.syncStatus = SyncStatus.NOT_SYNC;
                        }
                    } else {
                        clientFile.syncStatus = SyncStatus.SYNC;
                        serverFile.syncStatus = SyncStatus.SYNC;
                    }
                }
            }
        }

        //Создание файлов на сервере
        for (FileInfo clientFile : clientFiles) {
            if (clientFile.syncStatus == SyncStatus.NEED_CREATED) {
                Files.createFile(getLocalFilePath(clientFile));
                clientFile.syncStatus = SyncStatus.NOT_SYNC;
            }
        }

        //Удаление лишних файлов на сервере
        for (FileInfo serverFile : serverFiles) {
            if (serverFile.syncStatus == SyncStatus.NEED_DELETED) {
                Files.delete(serverFile.file.toPath());
            }
        }

        return clientFiles;
    }

    /**
     * Возвращает список файлов контрольными суммами в папке
     */
    private Set<FileInfo> getFilesInDirectory() throws IOException {
        Set<FileInfo> set = new HashSet<>();
        for (File file : Objects.requireNonNull(new File(workDirectory).listFiles())) {
            if (!file.isDirectory()) {
                FileInfo fileInfo = new FileInfo(file, CheckSumUtils.GetFileCheckSum(file.toPath()));
                set.add(fileInfo);
            }
        }
        return set;
    }

    /**
     * Проверяет список файлов на ограничение по длине файла
     */
    private Set<FileInfo> checkFilesForMaxLength(Set<FileInfo> fileInfoSet) {
        fileInfoSet.removeIf(fileInfo -> fileInfo.file.length() > MAX_FILE_LENGTH);
        return fileInfoSet;
    }

    private Path getLocalFilePath(FileInfo fileInfo) {
        return Paths.get(workDirectory, fileInfo.file.getName());
    }
}
