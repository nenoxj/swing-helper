package cn.note.service.toolkit.compress;

import cn.note.service.toolkit.filestore.RelativeFileStore;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.*;

/**
 * 压缩工具类
 *
 * @author jee
 */
public class ZipCompress {
    private static Logger logger = LoggerFactory.getLogger(ZipCompress.class);
    /**
     * 压缩后缀
     */
    private static final String SUFFIX_ZIP = ".zip";
    /**
     * 默认缓存大小
     */
    private static final int BUFFER_LENGTH = 1024 * 1024;


    /**
     * 快速压缩
     */
    public static void pack(File sourceDir) throws IOException {
        pack(sourceDir, sourceDir.getName(), sourceDir.getParentFile());
    }

    public static void pack(RelativeFileStore relativeFileStore) throws IOException {
        File sourceDir = relativeFileStore.homeDir();
        pack(relativeFileStore, sourceDir.getName(), sourceDir.getParentFile());
    }

    /**
     * 自定义压缩名称
     */
    public static void pack(File sourceDir, String zipName) throws IOException {
        pack(sourceDir, zipName, sourceDir.getParentFile());
    }

    /**
     * 自定义压缩名称 和存放目录
     */
    public static void pack(File sourceDir, String zipName, File targetDir) throws IOException {
        RelativeFileStore relativeFileStore = new RelativeFileStore(sourceDir);
        pack(relativeFileStore, zipName, targetDir);
    }

    /**
     * 压缩
     *
     * @param relativeFileStore 源目录
     * @param zipName           zip名称
     * @param targetDir         目标目录
     * @throws IOException 压缩异常
     */
    public static void pack(@Nonnull RelativeFileStore relativeFileStore, @Nonnull String zipName, @Nonnull File targetDir) throws IOException {
        zipName = zipName.endsWith(SUFFIX_ZIP) ? zipName : zipName.concat(SUFFIX_ZIP);
        File zipFile = FileUtils.getFile(targetDir, zipName);
        FileUtils.deleteQuietly(zipFile);
        try (FileOutputStream out = FileUtils.openOutputStream(zipFile)) {
            ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(zipFile);
            relativeFileStore.lists((path) -> {
                File file = path.toFile();
                // 在同一目录下,不扫描
                if (!file.equals(zipFile)) {
                    file2ZipOut(relativeFileStore.getRelativePath(file), file, zipOut);
                }
            });
            zipOut.finish();
            logger.debug("compress zip,complete!!!");
        }
    }


    /**
     * @param file   文件
     * @param zipOut zip输出流
     */
    private static void file2ZipOut(String relativePath, File file, ZipArchiveOutputStream zipOut) {
        ZipArchiveEntry archiveEntry = null;
        try {
            if (file.isDirectory()) {
                archiveEntry = new ZipArchiveEntry(relativePath + "/");
                zipOut.putArchiveEntry(archiveEntry);
            } else {
                archiveEntry = new ZipArchiveEntry(relativePath);
                zipOut.putArchiveEntry(archiveEntry);
                IOUtils.copy(FileUtils.openInputStream(file), zipOut);
            }
            zipOut.closeArchiveEntry();
        } catch (IOException e) {
            logger.error("写入压缩异常!", e);
        }

    }

    public static void unpack(File zipFile, boolean replace) throws IOException {
        File targetDir = new File(zipFile.getAbsolutePath().replace(SUFFIX_ZIP, ""));
        unpack(zipFile, targetDir, replace);
    }


    /**
     * 解压缩
     *
     * @param zipFile   zip文件
     * @param targetDir 目标目录
     * @param replace   文件存在是否替换
     * @throws IOException 解压缩异常
     */
    public static void unpack(File zipFile, File targetDir, boolean replace) throws IOException {
        try (ZipArchiveInputStream inputStream = readZipStream(zipFile)) {
            FileUtils.forceMkdir(targetDir);
            ZipArchiveEntry entry = null;
            while ((entry = inputStream.getNextZipEntry()) != null) {
                if (entry.isDirectory()) {
                    File directory = new File(targetDir, entry.getName());
                    FileUtils.forceMkdir(directory);
                } else {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    try {
                        File file = new File(targetDir, entry.getName());
                        if (replace) {
                            FileUtils.deleteQuietly(file);
                        }
                        FileUtils.forceMkdir(file.getParentFile());
                        IOUtils.copy(inputStream, out);
                        FileUtils.writeByteArrayToFile(file, out.toByteArray());
                    } finally {
                        IOUtils.closeQuietly(out);
                    }
                }
            }
            logger.debug("uncompress zip,complete!!!");
        }
    }

    /**
     * @param zipFile zip文件
     * @return 读取zip文件为ZipArchiveInputStream
     * @throws IOException 读取异常
     */
    private static ZipArchiveInputStream readZipStream(File zipFile) throws IOException {
        return new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_LENGTH), "GBK", true);
    }


}