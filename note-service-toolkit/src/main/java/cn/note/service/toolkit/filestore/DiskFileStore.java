package cn.note.service.toolkit.filestore;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description: 磁盘文件存储 ,只处理磁盘文件
 * @author: jee
 */
@Slf4j
public class DiskFileStore implements FileStore {

    private File parentFolder;

    private File diskFile;

    private String fileName;


    public DiskFileStore(File rootFolder, String relativePath) {
        this(FileUtils.getFile(rootFolder, relativePath));
    }

    public DiskFileStore(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException(StrUtil.format("文件:{}不存在!", file));
        }

        if (file.isDirectory()) {
            throw new IllegalArgumentException(StrUtil.format("文件:{} 不是文件!", file));
        }
        this.diskFile = file;
        this.fileName = file.getName();
        this.parentFolder = file.getParentFile();
    }

    /**
     * @return 主目录
     */
    @Override
    public File homeDir() {
        return this.parentFolder;
    }


    /**
     * @param content 文件内容
     * @throws IOException 写入异常
     */
    public void write(String content) throws IOException {
        delete(fileName);
        writeFile(fileName, content);
        log.debug("update disk file: {}", this.diskFile);
    }


    /**
     * 写入文件
     *
     * @param bao 字节流
     */
    public void write(ByteArrayOutputStream bao) throws IOException {
        delete(fileName);
        writeFile(fileName, bao);
        log.debug("bytes update disk file: {}", this.diskFile);
    }

    /**
     * @return 文件内容
     * @throws IOException 读取异常
     */
    public String read() throws IOException {
        return readFile(fileName);
    }

    /**
     * @return 是否存在
     */
    public boolean exists() {
        return diskFile.exists();
    }


    /**
     * 文件转换为url
     *
     * @throws MalformedURLException url转换异常
     */
    public URL toURL() throws MalformedURLException {
        return diskFile.toURI().toURL();
    }
}
